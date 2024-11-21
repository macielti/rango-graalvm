(ns rango-graalvm.interceptors.menu
  (:require [sqlite-component.interceptors :as interceptors.sqlite])
  (:import (java.util UUID)))

(defn menu-resource-identifier-fn
  [{{:keys [path-params]} :request}]
  (-> path-params :menu-id UUID/fromString))

(def menu-resource-existence-interceptor-check
  (interceptors.sqlite/resource-existence-check-interceptor menu-resource-identifier-fn
                                                            "SELECT * FROM menus WHERE id = ?"))
