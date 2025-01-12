(ns rango-graalvm.diplomat.http-server.reservation
  (:require [rango-graalvm.adapters.reservation :as adapters.reservation]
            [rango-graalvm.controllers.reservation :as controllers.reservation]
            [schema.core :as s])
  (:import (java.util UUID)))

(s/defn create-reservation!
  [{{:keys [reservation]} :json-params
    {:keys [sqlite]}      :components}]
  {:status 200
   :body   {:reservation (-> (controllers.reservation/create! (:student-code reservation)
                                                              (UUID/fromString (:menu-id reservation)) sqlite)
                             adapters.reservation/internal->wire)}})

(s/defn fetch-reservations-by-menu
  [{{:keys [menu-id]} :path-params
    {:keys [sqlite]}  :components}]
  {:status 200
   :body   {:reservations (->> (controllers.reservation/fetch-by-menu (UUID/fromString menu-id) sqlite)
                               (map adapters.reservation/internal->wire))}})

(s/defn fetch-reservation
  [{{:keys [reservation-id]} :path-params
    {:keys [sqlite]}         :components}]
  {:status 200
   :body   {:reservation (->> (controllers.reservation/fetch-reservation (UUID/fromString reservation-id) sqlite)
                              adapters.reservation/internal->wire)}})

(s/defn retract-reservation!
  [{{:keys [reservation-id]} :path-params
    {:keys [sqlite]}         :components}]
  (controllers.reservation/retract! (UUID/fromString reservation-id) sqlite)
  {:status 200
   :body   {}})

(s/defn fetch-student-reservation-by-menu
  [{{:keys [student-code menu-id]} :query-params
    {:keys [sqlite]}               :components}]
  {:status 200
   :body   {:reservation (-> (controllers.reservation/fetch-student-reservation-by-menu student-code
                                                                                        (UUID/fromString menu-id)
                                                                                        sqlite)
                             adapters.reservation/internal->wire)}})
