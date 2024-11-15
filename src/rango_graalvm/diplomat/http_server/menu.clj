(ns rango-graalvm.diplomat.http-server.menu
  (:require [rango-graalvm.adapters.menu :as adapters.menu]
            [rango-graalvm.controllers.menu :as controllers.menu]
            [schema.core :as s])
  (:import (java.util UUID)))

(s/defn create-menu!
  [{{:keys [menu]}       :json-params
    {:keys [postgresql]} :components}]
  {:status 200
   :body   {:menu (-> (adapters.menu/wire->internal menu)
                      (controllers.menu/create! postgresql)
                      adapters.menu/internal->wire)}})

(s/defn fetch-one
  [{{:keys [menu-id]}    :path-params
    {:keys [postgresql]} :components}]
  {:status 200
   :body   {:menu (->> (controllers.menu/fetch-one (UUID/fromString menu-id) postgresql)
                       adapters.menu/internal->wire)}})

(s/defn fetch-all
  [{{:keys [postgresql]} :components}]
  {:status 200
   :body   {:menus (->> (controllers.menu/fetch-all postgresql)
                        (map adapters.menu/internal->wire))}})

(s/defn retract-menu!
  [{{:keys [menu-id]}    :path-params
    {:keys [postgresql]} :components}]
  (controllers.menu/retract! (UUID/fromString menu-id) postgresql)
  {:status 200
   :body   {}})
