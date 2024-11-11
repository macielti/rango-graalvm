(ns rango-graalvm.wire.postgresql.reservation
  (:require [schema.core :as s])
  (:import (java.time LocalDateTime)))

(def reservation
  {:id         s/Uuid
   :student_id s/Uuid
   :menu_id    s/Uuid
   :created_at LocalDateTime})

(s/defschema Reservation reservation)
