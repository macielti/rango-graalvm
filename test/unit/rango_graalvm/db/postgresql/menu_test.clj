(ns rango-graalvm.db.postgresql.menu-test
  (:require [clojure.test :refer :all]
            [java-time.api :as jt]
            [fixtures.menu]
            [rango-graalvm.db.postgresql.menu :as database.menu]
            [common-test-clj.component.postgresql-mock :as component.postgresql-mock]
            [common-test-clj.helpers.schema :as test.helper.schema]
            [matcher-combinators.test :refer [match?]]
            [rango-graalvm.models.menu :as models.menu]
            [schema.test :as s]))

(s/deftest insert-test
  (testing "Should insert a menu"
    (let [conn (component.postgresql-mock/postgresql-conn-mock)]
      (is (match? {:menu/description    string?
                   :menu/reference-date jt/local-date?
                   :menu/id             uuid?
                   :menu/created-at     jt/local-date-time?}
                  (database.menu/insert! fixtures.menu/menu conn))))))

(s/deftest lookup-test
  (testing "Should insert a menu"
    (let [conn (component.postgresql-mock/postgresql-conn-mock)
          {:menu/keys [id] :as menu} (database.menu/insert! fixtures.menu/menu conn)]
      (is (match? menu
                  (database.menu/lookup id conn))))))

(s/deftest all-test
  (testing "Should return all menus"
    (let [conn (component.postgresql-mock/postgresql-conn-mock)]
      (database.menu/insert! (test.helper.schema/generate models.menu/Menu {}) conn)
      (database.menu/insert! (test.helper.schema/generate models.menu/Menu {}) conn)
      (database.menu/insert! (test.helper.schema/generate models.menu/Menu {}) conn)

      (is (match? [{:menu/id uuid?}
                   {:menu/id uuid?}
                   {:menu/id uuid?}]
                  (database.menu/all conn))))))

(s/deftest retract-test
  (testing "Should return all menus"
    (let [conn (component.postgresql-mock/postgresql-conn-mock)]
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
