(ns rango-graalvm.models.reservation
  (:require [schema.core :as s])
  (:import (java.time LocalDateTime)))

(def reservation
  {:reservation/id         s/Uuid
   :reservation/student-id s/Uuid
   :reservation/menu-id    s/Uuid
   :reservation/created-at LocalDateTime})

(s/defschema Reservation reservation)
