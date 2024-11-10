(ns rango-graalvm.porteiro.routes
  (:require [porteiro-component.adapters.customers :as adapters.customer]
            [porteiro-component.controllers.customer :as controllers.customer]
            [schema.core :as s]))

(s/defn create-customer!
  [{{:keys [customer]}   :json-params
    {:keys [postgresql]} :components}]
  {:status 201
   :body   {:customer (-> (adapters.customer/wire->internal customer)
                          (controllers.customer/create-customer! postgresql)
                          adapters.customer/internal->wire)}})

(def routes
  #{["/api/customers" :post create-customer! :route-name :create-customer]})
