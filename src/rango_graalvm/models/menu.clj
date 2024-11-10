(ns rango-graalvm.models.menu
  (:require [schema.core :as s])
  (:import (java.time LocalDate LocalDateTime)
           (org.pg.processor Uuid)))

(def menu
  {:menu/id             Uuid
   :menu/reference-date LocalDate
   :menu/description    s/Str
   :menu/created-at     LocalDateTime})

(s/defschema Menu menu)