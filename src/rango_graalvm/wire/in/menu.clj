(ns rango-graalvm.wire.in.menu
  (:require [schema.core :as s]))

(def menu
  {:reference-date s/Str
   :description    s/Str})

(s/defschema Menu menu)
