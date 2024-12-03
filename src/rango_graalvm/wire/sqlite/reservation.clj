(ns rango-graalvm.wire.sqlite.reservation
  (:require [common-clj.schema.extensions :as schema.extensions]
            [schema.core :as s]))

(def reservation
  {:reservations/id         schema.extensions/UuidWire
   :reservations/student_id schema.extensions/UuidWire
   :reservations/menu_id    schema.extensions/UuidWire
   :reservations/created_at schema.extensions/LocalDateTimeWire})

(s/defschema Reservation reservation)
