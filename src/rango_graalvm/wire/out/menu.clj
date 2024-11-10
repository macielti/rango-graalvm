(ns rango-graalvm.wire.out.menu
  (:require [schema.core :as s]))

(def menu
  {:id             s/Str
   :reference-date s/Str
   :description    s/Str
   :created-at     s/Str})

(s/defschema Menu menu)
