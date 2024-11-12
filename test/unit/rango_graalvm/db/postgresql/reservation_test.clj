(ns rango-graalvm.db.postgresql.reservation-test
  (:require [clojure.test :refer [is testing]]
            [common-test-clj.component.postgresql-mock :as component.postgresql-mock]
            [common-test-clj.helpers.schema :as test.helper.schema]
            [fixtures.menu]
            [fixtures.reservation]
            [fixtures.student]
            [java-time.api :as jt]
            [matcher-combinators.test :refer [match?]]
            [rango-graalvm.db.postgresql.reservation :as database.reservation]
            [rango-graalvm.models.reservation :as models.reservation]
            [schema.test :as s]))

(s/deftest insert-test
  (testing "Should insert a reservation"
    (let [conn (component.postgresql-mock/postgresql-conn-mock)]
      (is (match? {:reservation/id         uuid?
                   :reservation/student-id uuid?
                   :reservation/menu-id    uuid?
                   :reservation/created-at jt/local-date-time?}
                  (database.reservation/insert! fixtures.reservation/reservation conn))))))

(s/deftest by-menu-test
  (testing "Should insert a reservation"
    (let [conn (component.postgresql-mock/postgresql-conn-mock)]
      (database.reservation/insert! fixtures.reservation/reservation conn)
      (database.reservation/insert! (test.helper.schema/generate models.reservation/Reservation {}) conn)
      (database.reservation/insert! (test.helper.schema/generate models.reservation/Reservation {}) conn)

      (is (match? [{:reservation/menu-id fixtures.menu/menu-id}]
                  (database.reservation/by-menu fixtures.menu/menu-id conn)))

      (is (= []
             (database.reservation/by-menu (random-uuid) conn))))))

(s/deftest lookup-by-student-and-menu-test
  (let [conn (component.postgresql-mock/postgresql-conn-mock)]
    (database.reservation/insert! fixtures.reservation/reservation conn)
    (database.reservation/insert! (test.helper.schema/generate models.reservation/Reservation {}) conn)
    (database.reservation/insert! (test.helper.schema/generate models.reservation/Reservation {}) conn)

    (is (match? {:reservation/menu-id    fixtures.menu/menu-id
                 :reservation/student-id fixtures.student/student-id}
                (database.reservation/lookup-by-student-and-menu fixtures.student/student-id fixtures.menu/menu-id conn)))

    (is (nil? (database.reservation/lookup-by-student-and-menu fixtures.student/student-id (random-uuid) conn)))

    (is (nil? (database.reservation/lookup-by-student-and-menu (random-uuid) fixtures.menu/menu-id conn)))

    (is (nil? (database.reservation/lookup-by-student-and-menu (random-uuid) (random-uuid) conn)))))
