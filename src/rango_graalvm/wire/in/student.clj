(ns rango-graalvm.wire.in.student
  (:require [schema.core :as s]))

(def student
  {:code  s/Str
   :name  s/Str
   :class s/Str})

(s/defschema Student student)

(s/defschema StudentDocument
  {:student Student})
