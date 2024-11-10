(ns rango-graalvm.adapters.student
  (:require [camel-snake-kebab.core :as csk]
            [java-time.api :as jt]
            [rango-graalvm.models.sudent :as models.student]
            [rango-graalvm.wire.in.student :as wire.in.student]
            [rango-graalvm.wire.out.student :as wire.out.student]
            [schema.core :as s]))

(s/defn wire->internal :- models.student/Student
  [{:keys [code name class]} :- wire.in.student/Student]
  {:student/id         (random-uuid)
   :student/code       code
   :student/name       name
   :student/class      (csk/->kebab-case-keyword class)
   :student/created-at (jt/local-date-time)})

(s/defn internal->wire :- wire.out.student/Student
  [{:student/keys [id code name class created-at]} :- models.student/Student]
  {:id         (str id)
   :code       code
   :name       name
   :class      (clojure.core/name class)
   :created-at (str created-at)})

(s/defn postgresql->internal :- models.student/Student
  [{:keys [id code name class created_at]}]
  {:student/id         id
   :student/code       code
   :student/name       name
   :student/class      (csk/->kebab-case-keyword class)
   :student/created-at created_at})
