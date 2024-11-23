(ns rango-graalvm.wire.out.student
  (:require [common-clj.schema.extensions :as schema.extensions]
            [schema.core :as s]))

(def student
  {:id         schema.extensions/UuidWire
   :code       s/Str
   :name       s/Str
   :class      s/Str
   :created-at schema.extensions/LocalDateTimeWire})

(s/defschema Student student)
