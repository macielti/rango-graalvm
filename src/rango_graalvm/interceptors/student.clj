(ns rango-graalvm.interceptors.student
  (:require [sqlite-component.interceptors :as interceptors.sqlite])
  (:import (java.util UUID)))

(defn student-resource-identifier-fn
  [{{:keys [path-params]} :request}]
  (-> path-params :student-id UUID/fromString))

(def student-resource-existence-interceptor-check
  (interceptors.sqlite/resource-existence-check-interceptor student-resource-identifier-fn
                                                            "SELECT * FROM students WHERE id = ?"))

(defn student-resource-identifier-fn-for-reservation-creation
  [{{:keys [json-params]} :request}]
  (-> json-params :reservation :student-code))

(def student-resource-existence-interceptor-check-for-reservation-creation
  (interceptors.sqlite/resource-existence-check-interceptor student-resource-identifier-fn-for-reservation-creation
                                                            "SELECT * FROM students WHERE code = ?"))
