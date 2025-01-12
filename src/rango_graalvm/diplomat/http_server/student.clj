(ns rango-graalvm.diplomat.http-server.student
  (:require [rango-graalvm.adapters.student :as adapters.student]
            [rango-graalvm.controllers.student :as controllers.student]
            [schema.core :as s])
  (:import (java.util UUID)))

(s/defn create-student!
  [{{:keys [student]} :json-params
    {:keys [sqlite]}  :components}]
  {:status 200
   :body   {:student (-> (adapters.student/wire->internal student)
                         (controllers.student/create! sqlite)
                         adapters.student/internal->wire)}})

(s/defn fetch-all
  [{{:keys [sqlite]} :components}]
  {:status 200
   :body   {:students (->> (controllers.student/fetch-all sqlite)
                           (map adapters.student/internal->wire))}})

(s/defn fetch-students-by-reservations-menu
  [{{:keys [menu-id]} :path-params
    {:keys [sqlite]}  :components}]
  {:status 200
   :body   {:students (->> (controllers.student/fetch-students-by-menu-reservations (UUID/fromString menu-id) sqlite)
                           (map adapters.student/internal->wire))}})

(s/defn retract-student!
  [{{:keys [student-id]} :path-params
    {:keys [sqlite]}     :components}]
  (controllers.student/retract! (UUID/fromString student-id) sqlite)
  {:status 200
   :body   {}})
