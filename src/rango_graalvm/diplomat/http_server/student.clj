(ns rango-graalvm.diplomat.http-server.student
  (:require [rango-graalvm.adapters.student :as adapters.student]
            [rango-graalvm.controllers.student :as controllers.student]
            [schema.core :as s])
  (:import (java.util UUID)))

(s/defn create-student!
  [{{:keys [student]}    :json-params
    {:keys [postgresql]} :components}]
  {:status 200
   :body   {:student (-> (adapters.student/wire->internal student)
                         (controllers.student/create! postgresql)
                         adapters.student/internal->wire)}})

(s/defn fetch-all
  [{{:keys [postgresql]} :components}]
  {:status 200
   :body   {:students (->> (controllers.student/fetch-all postgresql)
                           (map adapters.student/internal->wire))}})

(s/defn fetch-students-by-reservations-menu
  [{{:keys [menu-id]}    :path-params
    {:keys [postgresql]} :components}]
  {:status 200
   :body   {:students (->> (controllers.student/fetch-students-by-menu-reservations (UUID/fromString menu-id) postgresql)
                           (map adapters.student/internal->wire))}})

(s/defn retract-student!
  [{{:keys [student-id]} :path-params
    {:keys [postgresql]} :components}]
  (controllers.student/retract! (UUID/fromString student-id) postgresql)
  {:status 200
   :body   {}})
