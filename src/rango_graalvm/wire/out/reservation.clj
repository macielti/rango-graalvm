(ns rango-graalvm.wire.out.reservation
  (:require [common-clj.schema.extensions :as schema.extensions]
            [schema.core :as s]))

(def reservation
  {:id         schema.extensions/UuidWire
   :student-id schema.extensions/UuidWire
   :menu-id    schema.extensions/UuidWire
   :created-at schema.extensions/LocalDateTimeWire})

(s/defschema Reservation reservation)
