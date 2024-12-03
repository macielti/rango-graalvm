(ns rango-graalvm.wire.postgresql.menu
  (:require [schema.core :as s])
  (:import (java.time LocalDate LocalDateTime)))

(def menu
  {:id             s/Uuid
   :reference_date LocalDate
   :description    s/Str
   :created_at     LocalDateTime})

(s/defschema Menu menu)
