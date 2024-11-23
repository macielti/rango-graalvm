(ns rango-graalvm.wire.sqlite.menu
  (:require [common-clj.schema.extensions :as schema.extensions]
            [schema.core :as s]))

(def menu
  {:id             schema.extensions/UuidWire
   :reference_date schema.extensions/LocalDateWire
   :description    s/Str
   :created_at     schema.extensions/LocalDateTimeWire})

(s/defschema Menu menu)
