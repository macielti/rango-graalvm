(ns fixtures.reservation
  (:require [common-test-clj.helpers.schema :as helpers.schema]
            [fixtures.menu]
            [fixtures.student]
            [rango-graalvm.models.reservation :as models.reservation]
            [rango-graalvm.wire.sqlite.reservation :as wire.sqlite.reservation]
            [schema.core :as s]))

(def reservation-id (random-uuid))

(s/def reservation :- models.reservation/Reservation
  (helpers.schema/generate models.reservation/Reservation {:reservation/id         reservation-id
                                                           :reservation/menu-id    fixtures.menu/menu-id
                                                           :reservation/student-id fixtures.student/student-id}))

(s/def sqlite-reservation :- wire.sqlite.reservation/Reservation
  (helpers.schema/generate wire.sqlite.reservation/Reservation {:reservations/id         (str reservation-id)
                                                                :reservations/menu_id    (str fixtures.menu/menu-id)
                                                                :reservations/student_id (str fixtures.student/student-id)}))
