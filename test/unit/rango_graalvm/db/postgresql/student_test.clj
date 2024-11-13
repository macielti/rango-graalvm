(ns rango-graalvm.db.postgresql.student-test
  (:require [clojure.test :refer [is testing]]
            [common-test-clj.component.postgresql-mock :as component.postgresql-mock]
            [common-test-clj.helpers.schema :as test.helper.schema]
            [fixtures.menu]
            [fixtures.reservation]
            [fixtures.student]
            [java-time.api :as jt]
            [matcher-combinators.test :refer [match?]]
            [rango-graalvm.db.postgresql.reservation :as database.reservation]
            [rango-graalvm.db.postgresql.student :as database.student]
            [rango-graalvm.models.reservation :as models.reservation]
            [rango-graalvm.models.sudent :as models.student]
            [schema.test :as s]))

(s/deftest insert-test
  (testing "Should insert a reservation"
    (let [conn (component.postgresql-mock/postgresql-conn-mock)]
      (is (match? {:student/id         uuid?
                   :student/code       string?
                   :student/name       string?
                   :student/class      keyword?
                   :student/created-at jt/local-date-time?}
                  (database.student/insert! fixtures.student/student conn))))))

(s/deftest lookup-by-code-test
  (testing "Should be able to query a student by code"
    (let [conn (component.postgresql-mock/postgresql-conn-mock)]
      (database.student/insert! fixtures.student/student conn)

      (is (match? {:student/code fixtures.student/student-code}
                  (database.student/lookup-by-code fixtures.student/student-code conn)))

      (is (nil? (database.student/lookup-by-code "8c045cae" conn))))))

(s/deftest all-test
  (testing "Should be able to query all students"
    (let [conn (component.postgresql-mock/postgresql-conn-mock)]
      (database.student/insert! fixtures.student/student conn)
      (database.student/insert! (test.helper.schema/generate models.student/Student {}) conn)
      (database.student/insert! (test.helper.schema/generate models.student/Student {}) conn)

      (is (match? [{:student/code fixtures.student/student-code}
                   {:student/code string?}
                   {:student/code string?}]
                  (database.student/all conn))))))

(s/deftest by-menu-reservation-test
  (testing "Should be able to query students by menu reservation"
    (let [conn (component.postgresql-mock/postgresql-conn-mock)]
      (database.student/insert! fixtures.student/student conn)
      (database.reservation/insert! (test.helper.schema/generate models.reservation/Reservation fixtures.reservation/reservation) conn)
      (database.student/insert! (test.helper.schema/generate models.student/Student {}) conn)
      (database.student/insert! (test.helper.schema/generate models.student/Student {}) conn)

      (is (match? [{:student/code fixtures.student/student-code}]
                  (database.student/by-menu-reservation fixtures.menu/menu-id conn))))))

(s/deftest retract-test
  (testing "Should be able to retract a student"
    (let [conn (component.postgresql-mock/postgresql-conn-mock)]
      (database.student/insert! fixtures.student/student conn)

      (is (match? {:deleted 1}
                  (database.student/retract! fixtures.student/student-id conn)))

      (is (match? []
                  (database.student/all conn))))))
