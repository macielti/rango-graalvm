(ns rango-graalvm.wire.out.menu
  (:require [common-clj.schema.extensions :as schema.extensions]
            [schema.core :as s]))

(def menu
  {:id             schema.extensions/UuidWire
   :reference-date schema.extensions/LocalDateWire
   :description    s/Str
   :created-at     schema.extensions/LocalDateTimeWire})

(s/defschema Menu menu)
