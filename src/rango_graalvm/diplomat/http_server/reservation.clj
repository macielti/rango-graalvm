(ns rango-graalvm.diplomat.http-server.reservation
  (:require [rango-graalvm.adapters.reservation :as adapters.reservation]
            [rango-graalvm.controllers.reservation :as controllers.reservation]
            [schema.core :as s])
  (:import (java.util UUID)))

(s/defn create-reservation!
  [{{:keys [reservation]} :json-params
    {:keys [postgresql]}  :components}]
  {:status 200
   :body   {:reservation (-> (controllers.reservation/create! (:student-code reservation)
                                                              (UUID/fromString (:menu-id reservation)) postgresql)
                             adapters.reservation/internal->wire)}})

(s/defn fetch-reservations-by-menu
  [{{:keys [menu-id]}    :path-params
    {:keys [postgresql]} :components}]
  {:status 200
   :body   {:reservations (->> (controllers.reservation/fetch-by-menu (UUID/fromString menu-id) postgresql)
                               (map adapters.reservation/internal->wire))}})
