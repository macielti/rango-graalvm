(ns rango-graalvm.wire.in.reservation
  (:require [common-clj.schema.extensions :as schema.extensions]
            [schema.core :as s]))

(def reservation
  {:menu-id      schema.extensions/UuidWire
   :student-code s/Str})

(s/defschema Reservation reservation)

(s/defschema ReservationDocument
  {:reservation Reservation})
