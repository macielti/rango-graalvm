(ns rango-graalvm.interceptors.menu
  (:import (java.util UUID)))

(defn menu-resource-identifier-fn
  [{{:keys [path-params]} :request}]
  (-> path-params :menu-id UUID/fromString))
