(ns rango-graalvm.interceptors.menu
  (:require [postgresql-component.interceptors :as interceptors.postgresql])
  (:import (java.util UUID)))

(defn menu-resource-identifier-fn
  [{{:keys [path-params]} :request}]
  (-> path-params :menu-id UUID/fromString))

(def menu-resource-existence-interceptor-check
  (interceptors.postgresql/resource-existence-check-interceptor menu-resource-identifier-fn
                                                                "SELECT * FROM menus WHERE id = $1"))
