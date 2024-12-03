(ns rango-graalvm.wire.sqlite.menu
  (:require [common-clj.schema.extensions :as schema.extensions]
            [schema.core :as s]))

(def menu
  {:menus/id             schema.extensions/UuidWire
   :menus/reference_date schema.extensions/LocalDateWire
   :menus/description    s/Str
   :menus/created_at     schema.extensions/LocalDateTimeWire})

(s/defschema Menu menu)
