(ns rango-graalvm.wire.in.reservation
  (:require [schema.core :as s]))

(def reservation
  {:reservation-id s/Str
   :student-code   s/Str})

(s/defschema Reservation reservation)
