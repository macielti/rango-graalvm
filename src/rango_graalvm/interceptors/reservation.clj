(ns rango-graalvm.interceptors.reservation
  (:require [sqlite-component.interceptors :as interceptors.sqlite])
  (:import (java.util UUID)))

(defn reservation-resource-identifier-fn
  [{{:keys [path-params]} :request}]
  (-> path-params :reservation-id UUID/fromString))

(def reservation-resource-existence-interceptor-check
  (interceptors.sqlite/resource-existence-check-interceptor reservation-resource-identifier-fn
                                                            "SELECT * FROM reservations WHERE id = ?"))
