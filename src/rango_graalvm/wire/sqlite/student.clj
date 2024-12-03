(ns rango-graalvm.wire.sqlite.student
  (:require [common-clj.schema.extensions :as schema.extensions]
            [schema.core :as s]))

(def student
  {:students/id         schema.extensions/UuidWire
   :students/code       s/Str
   :students/name       s/Str
   :students/class      s/Str
   :students/created_at schema.extensions/LocalDateTimeWire})

(s/defschema Student student)
