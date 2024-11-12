(ns fixtures.reservation
  (:require [common-test-clj.helpers.schema :as helpers.schema]
            [fixtures.menu]
            [fixtures.student]
            [rango-graalvm.models.reservation :as models.reservation]
            [rango-graalvm.wire.postgresql.reservation :as wire.postgresql.reservation]
            [schema.core :as s]))

(s/def reservation :- models.reservation/Reservation
  (helpers.schema/generate models.reservation/Reservation {:reservation/menu-id fixtures.menu/menu-id
                                                           :reservation/student-id fixtures.student/student-id}))

(s/def postgresql-reservation :- wire.postgresql.reservation/Reservation
  (helpers.schema/generate wire.postgresql.reservation/Reservation {}))
