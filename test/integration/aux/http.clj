(ns aux.http
  (:require [cheshire.core :as json]
            [io.pedestal.test :as test]))

(defn authenticate-admin
  [credentials
   service-fn]
  (let [{:keys [body status]} (test/response-for service-fn
                                                 :post "/api/customers/auth"
                                                 :headers {"Content-Type" "application/json"}
                                                 :body (json/encode credentials))]
    {:status status
     :body   (json/decode body true)}))

(defn create-student
  [student
   token
   service-fn]
  (let [{:keys [body status]} (test/response-for service-fn
                                                 :post "/api/students"
                                                 :headers {"Content-Type"  "application/json"
                                                           "Authorization" (str "Bearer " token)}
                                                 :body (json/encode student))]
    {:status status
     :body   (json/decode body true)}))

(defn create-menu
  [menu
   token
   service-fn]
  (let [{:keys [body status]} (test/response-for service-fn
                                                 :post "/api/menus"
                                                 :headers {"Content-Type"  "application/json"
                                                           "Authorization" (str "Bearer " token)}
                                                 :body (json/encode menu))]
    {:status status
     :body   (json/decode body true)}))

(defn create-reservation
  [reservation
   service-fn]
  (let [{:keys [body status]} (test/response-for service-fn
                                                 :post "/api/reservations"
                                                 :headers {"Content-Type" "application/json"}
                                                 :body (json/encode reservation))]
    {:status status
     :body   (json/decode body true)}))

(defn retract-reservation
  [reservation-id
   service-fn]
  (let [{:keys [body status]} (test/response-for service-fn
                                                 :delete (str "/api/reservations/" reservation-id))]
    {:status status
     :body   (json/decode body true)}))
