(ns rango-graalvm.interceptors.student
  (:require [postgresql-component.interceptors :as interceptors.postgresql])
  (:import (java.util UUID)))

(defn student-resource-identifier-fn
  [{{:keys [path-params]} :request}]
  (-> path-params :student-id UUID/fromString))

(def student-resource-existence-interceptor-check
  (interceptors.postgresql/resource-existence-check-interceptor student-resource-identifier-fn
                                                                "SELECT * FROM students WHERE id = $1"))
