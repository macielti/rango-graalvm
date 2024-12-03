(ns rango-graalvm.wire.postgresql.student
  (:require [schema.core :as s])
  (:import (java.time LocalDateTime)))

(def student
  {:id         s/Uuid
   :code       s/Str
   :name       s/Str
   :class      s/Str
   :created_at LocalDateTime})

(s/defschema Student student)
