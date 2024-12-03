(ns rango-graalvm.interceptors.student
  (:require [postgresql-component.interceptors :as interceptors.postgresql])
  (:import (java.util UUID)))

(defn student-resource-identifier-fn
  [{{:keys [path-params]} :request}]
  (-> path-params :student-id UUID/fromString))

(def student-resource-existence-interceptor-check
  (interceptors.postgresql/resource-existence-check-interceptor student-resource-identifier-fn
                                                                "SELECT * FROM students WHERE id = $1"))

(defn student-resource-identifier-fn-for-reservation-creation
  [{{:keys [json-params]} :request}]
  (-> json-params :reservation :student-code))

(def student-resource-existence-interceptor-check-for-reservation-creation
  (interceptors.postgresql/resource-existence-check-interceptor student-resource-identifier-fn-for-reservation-creation
                                                                "SELECT * FROM students WHERE code = $1"))
