(ns rango-graalvm.wire.in.menu
  (:require [common-clj.schema.extensions :as schema.extensions]
            [schema.core :as s]))

(def menu
  {:reference-date schema.extensions/LocalDateWire
   :description    s/Str})

(s/defschema Menu menu)

(s/defschema MenuDocument
  {:menu Menu})
