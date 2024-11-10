(ns rango-graalvm.wire.out.student
  (:require [schema.core :as s]))

(def student
  {:id         s/Str
   :code       s/Str
   :name       s/Str
   :class      s/Str
   :created-at s/Str})

(s/defschema Student student)
