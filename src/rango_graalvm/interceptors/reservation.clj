(ns rango-graalvm.interceptors.reservation
  (:require [postgresql-component.interceptors :as interceptors.postgresql])
  (:import (java.util UUID)))

(defn reservation-resource-identifier-fn
  [{{:keys [path-params]} :request}]
  (-> path-params :reservation-id UUID/fromString))

(def reservation-resource-existence-interceptor-check
  (interceptors.postgresql/resource-existence-check-interceptor reservation-resource-identifier-fn
                                                                "SELECT * FROM reservations WHERE id = $1"))
