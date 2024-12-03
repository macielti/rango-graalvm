(ns rango-graalvm.db.postgresql.menu-test
  (:require [aux.components]
            [clojure.test :refer [is testing]]
            [common-test-clj.component.postgresql-mock :as component.postgresql-mock]
            [common-test-clj.helpers.schema :as test.helper.schema]
            [fixtures.menu]
            [java-time.api :as jt]
            [matcher-combinators.test :refer [match?]]
            [rango-graalvm.db.postgresql.menu :as database.menu]
            [rango-graalvm.models.menu :as models.menu]
            [schema.test :as s]))

(s/deftest insert-test
  (testing "Should insert a menu"
    (let [conn (component.postgresql-mock/postgresql-pool-mock aux.components/schemas)]
      (is (match? {:menu/description    string?
                   :menu/reference-date jt/local-date?
                   :menu/id             uuid?
                   :menu/created-at     jt/local-date-time?}
                  (database.menu/insert! fixtures.menu/menu conn))))))

(s/deftest lookup-test
  (testing "Should insert a menu"
    (let [conn (component.postgresql-mock/postgresql-pool-mock aux.components/schemas)
          {:menu/keys [id] :as menu} (database.menu/insert! fixtures.menu/menu conn)]
      (is (match? menu
                  (database.menu/lookup id conn))))))

(s/deftest all-test
  (testing "Should return all menus"
    (let [conn (component.postgresql-mock/postgresql-pool-mock aux.components/schemas)]
      (database.menu/insert! (test.helper.schema/generate models.menu/Menu {}) conn)
      (database.menu/insert! (test.helper.schema/generate models.menu/Menu {}) conn)
      (database.menu/insert! (test.helper.schema/generate models.menu/Menu {}) conn)

      (is (match? [{:menu/id uuid?}
                   {:menu/id uuid?}
                   {:menu/id uuid?}]
                  (database.menu/all conn))))))

(s/deftest retract-test
  (testing "Should be able to retract a menu"
    (let [conn (component.postgresql-mock/postgresql-pool-mock aux.components/schemas)]
      (database.menu/insert! fixtures.menu/menu conn)
      (database.menu/insert! (test.helper.schema/generate models.menu/Menu {}) conn)
      (database.menu/insert! (test.helper.schema/generate models.menu/Menu {}) conn)

      (is (= {:deleted 1}
             (database.menu/retract! fixtures.menu/menu-id conn)))

      (is (match? [{:menu/id uuid?}
                   {:menu/id uuid?}]
                  (database.menu/all conn)))

      (is (= {:deleted 0}
             (database.menu/retract! (random-uuid) conn))))))
