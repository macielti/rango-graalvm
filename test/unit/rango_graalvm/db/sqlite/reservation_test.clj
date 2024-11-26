(ns rango-graalvm.db.sqlite.reservation-test
  (:require [aux.components]
            [clojure.test :refer [is testing]]
            [common-test-clj.component.sqlite-mock :as component.sqlite-mock]
            [common-test-clj.helpers.schema :as test.helper.schema]
            [fixtures.menu]
            [fixtures.reservation]
            [fixtures.student]
            [java-time.api :as jt]
            [matcher-combinators.test :refer [match?]]
            [rango-graalvm.db.sqlite.reservation :as database.reservation]
            [rango-graalvm.models.reservation :as models.reservation]
            [schema.test :as s]))

(s/deftest insert-test
  (testing "Should insert a reservation"
    (let [database-conn (component.sqlite-mock/sqlite-unit-mock aux.components/schemas)]
      (is (match? {:reservation/id         fixtures.reservation/reservation-id
                   :reservation/student-id fixtures.student/student-id
                   :reservation/menu-id    fixtures.menu/menu-id
                   :reservation/created-at jt/local-date-time?}
                  (database.reservation/insert! fixtures.reservation/reservation database-conn))))))

(s/deftest by-menu-test
  (testing "Should insert a reservation"
    (let [database-conn (component.sqlite-mock/sqlite-unit-mock aux.components/schemas)]
      (database.reservation/insert! fixtures.reservation/reservation database-conn)
      (database.reservation/insert! (test.helper.schema/generate models.reservation/Reservation {}) database-conn)
      (database.reservation/insert! (test.helper.schema/generate models.reservation/Reservation {}) database-conn)

      (is (match? [{:reservation/menu-id fixtures.menu/menu-id}]
                  (database.reservation/by-menu fixtures.menu/menu-id database-conn)))

      (is (= []
             (database.reservation/by-menu (random-uuid) database-conn))))))

(s/deftest lookup-test
  (let [database-conn (component.sqlite-mock/sqlite-unit-mock aux.components/schemas)]
    (database.reservation/insert! fixtures.reservation/reservation database-conn)

    (is (match? {:reservation/id fixtures.reservation/reservation-id}
                (database.reservation/lookup fixtures.reservation/reservation-id database-conn)))

    (is (nil? (database.reservation/lookup (random-uuid) database-conn)))))

(s/deftest lookup-by-student-and-menu-test
  (let [database-conn (component.sqlite-mock/sqlite-unit-mock aux.components/schemas)]
    (database.reservation/insert! fixtures.reservation/reservation database-conn)
    (database.reservation/insert! (test.helper.schema/generate models.reservation/Reservation {}) database-conn)
    (database.reservation/insert! (test.helper.schema/generate models.reservation/Reservation {}) database-conn)

    (is (match? {:reservation/menu-id    fixtures.menu/menu-id
                 :reservation/student-id fixtures.student/student-id}
                (database.reservation/lookup-by-student-and-menu fixtures.student/student-id fixtures.menu/menu-id database-conn)))

    (is (nil? (database.reservation/lookup-by-student-and-menu fixtures.student/student-id (random-uuid) database-conn)))

    (is (nil? (database.reservation/lookup-by-student-and-menu (random-uuid) fixtures.menu/menu-id database-conn)))

    (is (nil? (database.reservation/lookup-by-student-and-menu (random-uuid) (random-uuid) database-conn)))))

(s/deftest retract-test
  (let [database-conn (component.sqlite-mock/sqlite-unit-mock aux.components/schemas)]

    (is (match? {:reservation/id         fixtures.reservation/reservation-id
                 :reservation/student-id fixtures.student/student-id
                 :reservation/menu-id    fixtures.menu/menu-id
                 :reservation/created-at jt/local-date-time?}
                (database.reservation/insert! fixtures.reservation/reservation database-conn)))

    (is (match? [{:next.jdbc/update-count 1}]
                (database.reservation/retract! fixtures.reservation/reservation-id database-conn)))))

(s/deftest fetch-student-reservation-by-menu-test
  (let [database-conn (component.sqlite-mock/sqlite-unit-mock aux.components/schemas)]
    (database.reservation/insert! fixtures.reservation/reservation database-conn)
    (database.reservation/insert! (test.helper.schema/generate models.reservation/Reservation {}) database-conn)
    (database.reservation/insert! (test.helper.schema/generate models.reservation/Reservation {}) database-conn)

    (is (match? {:reservation/id         fixtures.reservation/reservation-id
                 :reservation/student-id fixtures.student/student-id
                 :reservation/menu-id    fixtures.menu/menu-id
                 :reservation/created-at jt/local-date-time?}
                (database.reservation/fetch-student-reservation-by-menu fixtures.student/student-id fixtures.menu/menu-id database-conn)))

    (is (nil? (database.reservation/fetch-student-reservation-by-menu (random-uuid) fixtures.menu/menu-id database-conn)))

    (is (nil? (database.reservation/fetch-student-reservation-by-menu fixtures.student/student-id (random-uuid) database-conn)))

    (is (nil? (database.reservation/fetch-student-reservation-by-menu (random-uuid) (random-uuid) database-conn)))))
