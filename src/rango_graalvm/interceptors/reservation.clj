(ns rango-graalvm.interceptors.reservation
  (:import (java.util UUID)))

(defn reservation-resource-identifier-fn
  [{{:keys [path-params]} :request}]
  (-> path-params :reservation-id UUID/fromString))
