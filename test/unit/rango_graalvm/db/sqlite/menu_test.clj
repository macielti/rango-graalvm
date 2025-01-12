(ns rango-graalvm.db.sqlite.menu-test
  (:require [aux.components]
            [clojure.test :refer [is testing]]
            [common-test-clj.component.sqlite-mock :as component.sqlite-mock]
            [common-test-clj.helpers.schema :as test.helper.schema]
            [fixtures.menu]
            [java-time.api :as jt]
            [matcher-combinators.test :refer [match?]]
            [rango-graalvm.db.sqlite.menu :as database.menu]
            [rango-graalvm.models.menu :as models.menu]
            [schema.test :as s]))

(s/deftest insert-test
  (testing "That we are able to insert a menu into the database"
    (let [database-conn (component.sqlite-mock/sqlite-unit-mock aux.components/schemas)]
      (is (match? {:menu/id             fixtures.menu/menu-id
                   :menu/created-at     jt/local-date-time?
                   :menu/description    fixtures.menu/menu-description
                   :menu/reference-date jt/local-date?}
                  (database.menu/insert! fixtures.menu/menu database-conn))))))

(s/deftest lookup-test
  (testing "Should be able to query a menu by its id"
    (let [database-conn (component.sqlite-mock/sqlite-unit-mock aux.components/schemas)]
      (database.menu/insert! fixtures.menu/menu database-conn)
      (is (match? {:menu/id             fixtures.menu/menu-id
                   :menu/created-at     jt/local-date-time?
                   :menu/description    fixtures.menu/menu-description
                   :menu/reference-date jt/local-date?}
                  (database.menu/lookup fixtures.menu/menu-id database-conn)))

      (is (nil? (database.menu/lookup (random-uuid) database-conn))))))

(s/deftest all-test
  (testing "Should return all menus"
    (let [database-conn (component.sqlite-mock/sqlite-unit-mock aux.components/schemas)]
      (database.menu/insert! (test.helper.schema/generate models.menu/Menu {}) database-conn)
      (database.menu/insert! (test.helper.schema/generate models.menu/Menu {}) database-conn)
      (database.menu/insert! (test.helper.schema/generate models.menu/Menu {}) database-conn)

      (is (match? [{:menu/id uuid?}
                   {:menu/id uuid?}
                   {:menu/id uuid?}]
                  (database.menu/all database-conn))))))

(s/deftest retract-test
  (testing "Should return all menus"
    (let [database-conn (component.sqlite-mock/sqlite-unit-mock aux.components/schemas)]
      (database.menu/insert! fixtures.menu/menu database-conn)
      (database.menu/insert! (test.helper.schema/generate models.menu/Menu {}) database-conn)
      (database.menu/insert! (test.helper.schema/generate models.menu/Menu {}) database-conn)

      (is (= [{:next.jdbc/update-count 1}]
             (database.menu/retract! fixtures.menu/menu-id database-conn)))

      (is (match? [{:menu/id uuid?}
                   {:menu/id uuid?}]
                  (database.menu/all database-conn)))

      (is (= [{:next.jdbc/update-count 0}]
             (database.menu/retract! (random-uuid) database-conn))))))
