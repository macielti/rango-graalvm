(ns rango-graalvm.models.sudent
  (:require [schema.core :as s])
  (:import (java.time LocalDateTime)))

(def student
  {:student/id         s/Uuid
   :student/code       s/Str
   :student/name       s/Str
   :student/class      s/Keyword
   :student/created-at LocalDateTime})

(s/defschema Student student)
