(ns rango-graalvm.db.sqlite.student-test
  (:require [clojure.test :refer [is testing]]
            [common-test-clj.component.sqlite-mock :as component.sqlite-mock]
            [common-test-clj.helpers.schema :as test.helper.schema]
            [fixtures.menu]
            [fixtures.reservation]
            [fixtures.student]
            [java-time.api :as jt]
            [matcher-combinators.test :refer [match?]]
            [aux.components]
            [rango-graalvm.db.sqlite.reservation :as database.reservation]
            [rango-graalvm.db.sqlite.student :as database.student]
            [rango-graalvm.models.reservation :as models.reservation]
            [rango-graalvm.models.sudent :as models.student]
            [schema.test :as s]))

(s/deftest insert-test
  (testing "Should insert a Student entity to database"
    (let [database (component.sqlite-mock/sqlite-unit-mock aux.components/schemas)]
      (is (match? {:student/id         fixtures.student/student-id
                   :student/code       fixtures.student/student-code
                   :student/name       string?
                   :student/class      keyword?
                   :student/created-at jt/local-date-time?}
                  (database.student/insert! fixtures.student/student database))))))

(s/deftest lookup-by-code-test
  (testing "Should be able to query a student by code"
    (let [database (component.sqlite-mock/sqlite-unit-mock aux.components/schemas)]
      (database.student/insert! fixtures.student/student database)

      (is (match? {:student/code fixtures.student/student-code}
                  (database.student/lookup-by-code fixtures.student/student-code database)))

      (is (nil? (database.student/lookup-by-code "8c045cae" database))))))

(s/deftest all-test
  (testing "Should be able to query all students"
    (let [database (component.sqlite-mock/sqlite-unit-mock aux.components/schemas)]
      (database.student/insert! fixtures.student/student database)
      (database.student/insert! (test.helper.schema/generate models.student/Student {}) database)
      (database.student/insert! (test.helper.schema/generate models.student/Student {}) database)

      (is (match? [{:student/code fixtures.student/student-code}
                   {:student/code string?}
                   {:student/code string?}]
                  (database.student/all database))))))

(s/deftest by-menu-reservation-test
  (testing "Should be able to query students by menu reservation"
    (let [database (component.sqlite-mock/sqlite-unit-mock aux.components/schemas)]
      (database.student/insert! fixtures.student/student database)
      (database.reservation/insert! (test.helper.schema/generate models.reservation/Reservation fixtures.reservation/reservation) database)
      (database.student/insert! (test.helper.schema/generate models.student/Student {}) database)
      (database.student/insert! (test.helper.schema/generate models.student/Student {}) database)

      (is (match? [{:student/code fixtures.student/student-code}]
                  (database.student/by-menu-reservation fixtures.menu/menu-id database))))))

(s/deftest retract-test
  (testing "Should be able to retract a student"
    (let [database (component.sqlite-mock/sqlite-unit-mock aux.components/schemas)]
      (database.student/insert! fixtures.student/student database)

      (is (match? [{:next.jdbc/update-count 1}]
                  (database.student/retract! fixtures.student/student-id database)))

      (is (match? []
                  (database.student/all database))))))
