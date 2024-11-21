(ns rango-graalvm.db.sqlite.reservation-test
  (:require [clojure.test :refer [is testing]]
            [common-test-clj.component.sqlite-mock :as component.sqlite-mock]
            [common-test-clj.helpers.schema :as test.helper.schema]
            [fixtures.menu]
            [fixtures.reservation]
            [fixtures.student]
            [java-time.api :as jt]
            [matcher-combinators.test :refer [match?]]
            [rango-graalvm.db.sqlite.reservation :as database.reservation]
            [rango-graalvm.models.reservation :as models.reservation]
            [rango-graalvm.db.sqlite.config :as sqlite.config]
            [schema.test :as s]))

(s/deftest insert-test
  (testing "Should insert a reservation"
    (let [database (component.sqlite-mock/sqlite-unit-mock sqlite.config/schemas)]
      (is (match? {:reservation/id         fixtures.reservation/reservation-id
                   :reservation/student-id fixtures.student/student-id
                   :reservation/menu-id    fixtures.menu/menu-id
                   :reservation/created-at jt/local-date-time?}
                  (database.reservation/insert! fixtures.reservation/reservation database))))))

(s/deftest by-menu-test
  (testing "Should insert a reservation"
    (let [database (component.sqlite-mock/sqlite-unit-mock sqlite.config/schemas)]
      (database.reservation/insert! fixtures.reservation/reservation database)
      (database.reservation/insert! (test.helper.schema/generate models.reservation/Reservation {}) database)
      (database.reservation/insert! (test.helper.schema/generate models.reservation/Reservation {}) database)

      (is (match? [{:reservation/menu-id fixtures.menu/menu-id}]
                  (database.reservation/by-menu fixtures.menu/menu-id database)))

      (is (= []
             (database.reservation/by-menu (random-uuid) database))))))

(s/deftest lookup-test
  (let [database (component.sqlite-mock/sqlite-unit-mock sqlite.config/schemas)]
    (database.reservation/insert! fixtures.reservation/reservation database)

    (is (match? {:reservation/id fixtures.reservation/reservation-id}
                (database.reservation/lookup fixtures.reservation/reservation-id database)))

    (is (nil? (database.reservation/lookup (random-uuid) database)))))

(s/deftest lookup-by-student-and-menu-test
  (let [database (component.sqlite-mock/sqlite-unit-mock sqlite.config/schemas)]
    (database.reservation/insert! fixtures.reservation/reservation database)
    (database.reservation/insert! (test.helper.schema/generate models.reservation/Reservation {}) database)
    (database.reservation/insert! (test.helper.schema/generate models.reservation/Reservation {}) database)

    (is (match? {:reservation/menu-id    fixtures.menu/menu-id
                 :reservation/student-id fixtures.student/student-id}
                (database.reservation/lookup-by-student-and-menu fixtures.student/student-id fixtures.menu/menu-id database)))

    (is (nil? (database.reservation/lookup-by-student-and-menu fixtures.student/student-id (random-uuid) database)))

    (is (nil? (database.reservation/lookup-by-student-and-menu (random-uuid) fixtures.menu/menu-id database)))

    (is (nil? (database.reservation/lookup-by-student-and-menu (random-uuid) (random-uuid) database)))))

(s/deftest retract-test
  (let [database (component.sqlite-mock/sqlite-unit-mock sqlite.config/schemas)]

    (is (match? {:reservation/id         fixtures.reservation/reservation-id
                 :reservation/student-id fixtures.student/student-id
                 :reservation/menu-id    fixtures.menu/menu-id
                 :reservation/created-at jt/local-date-time?}
                (database.reservation/insert! fixtures.reservation/reservation database)))

    (is (match? [{:next.jdbc/update-count 0}]
                (database.reservation/retract! fixtures.reservation/reservation-id database)))))

(s/deftest fetch-student-reservation-by-menu-test
  (let [database (component.sqlite-mock/sqlite-unit-mock sqlite.config/schemas)]
    (database.reservation/insert! fixtures.reservation/reservation database)
    (database.reservation/insert! (test.helper.schema/generate models.reservation/Reservation {}) database)
    (database.reservation/insert! (test.helper.schema/generate models.reservation/Reservation {}) database)

    (is (match? {:reservation/id         fixtures.reservation/reservation-id
                 :reservation/student-id fixtures.student/student-id
                 :reservation/menu-id    fixtures.menu/menu-id
                 :reservation/created-at jt/local-date-time?}
                (database.reservation/fetch-student-reservation-by-menu fixtures.student/student-id fixtures.menu/menu-id database)))

    (is (nil? (database.reservation/fetch-student-reservation-by-menu (random-uuid) fixtures.menu/menu-id database)))

    (is (nil? (database.reservation/fetch-student-reservation-by-menu fixtures.student/student-id (random-uuid) database)))

    (is (nil? (database.reservation/fetch-student-reservation-by-menu (random-uuid) (random-uuid) database)))))
