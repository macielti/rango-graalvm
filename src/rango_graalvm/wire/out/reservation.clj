(ns rango-graalvm.wire.out.reservation
  (:require [schema.core :as s]))

(def reservation
  {:id         s/Str
   :student-id s/Str
   :menu-id    s/Str
   :created-at s/Str})

(s/defschema Reservation reservation)
