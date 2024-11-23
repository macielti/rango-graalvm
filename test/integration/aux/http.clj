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

(defn fetch-one-menu
  [menu-id
   service-fn]
  (let [{:keys [body status]} (test/response-for service-fn
                                                 :get (str "/api/menus/" menu-id)
                                                 :headers {"Content-Type" "application/json"})]
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

(defn fetch-student-reservation-by-menu
  [student-code
   menu-id
   service-fn]
  (let [{:keys [body status]} (test/response-for service-fn
                                                 :get (str "/api/reservation-by-student-and-menu?student-code=" student-code "&menu-id=" menu-id))]
    {:status status
     :body   (json/decode body true)}))

(defn fetch-one-reservation
  [reservation-id
   service-fn]
  (let [{:keys [body status]} (test/response-for service-fn
                                                 :get (str "/api/reservations/" reservation-id))]
    {:status status
     :body   (json/decode body true)}))

(defn fetch-all-students
  [token
   service-fn]
  (let [{:keys [body status]} (test/response-for service-fn
                                                 :get "/api/students"
                                                 :headers {"Content-Type"  "application/json"
                                                           "Authorization" (str "Bearer " token)})]
    {:status status
     :body   (json/decode body true)}))

(defn delete-one-student
  [student-id
   token
   service-fn]
  (let [{:keys [body status]} (test/response-for service-fn
                                                 :delete (str "/api/students/" student-id)
                                                 :headers {"Content-Type"  "application/json"
                                                           "Authorization" (str "Bearer " token)})]
    {:status status
     :body   (json/decode body true)}))

(defn fetch-students-that-has-reservation-for-a-menu
  [menu-id
   token
   service-fn]
  (let [{:keys [body status]} (test/response-for service-fn
                                                 :get (str "/api/students-by-menu-reservations/" menu-id)
                                                 :headers {"Content-Type"  "application/json"
                                                           "Authorization" (str "Bearer " token)})]
    {:status status
     :body   (json/decode body true)}))

(defn retract-menu
  [menu-id
   token
   service-fn]
  (let [{:keys [body status]} (test/response-for service-fn
                                                 :delete (str "/api/menus/" menu-id)
                                                 :headers {"Content-Type"  "application/json"
                                                           "Authorization" (str "Bearer " token)})]
    {:status status
     :body   (json/decode body true)}))

(defn fetch-all-menus
  [service-fn]
  (let [{:keys [body status]} (test/response-for service-fn
                                                 :get "/api/menus"
                                                 :headers {"Content-Type" "application/json"})]
    {:status status
     :body   (json/decode body true)}))

(defn fetch-reservations-by-menu
  [menu-id
   service-fn]
  (let [{:keys [body status]} (test/response-for service-fn
                                                 :get (str "/api/reservations-by-menu/" menu-id)
                                                 :headers {"Content-Type" "application/json"})]
    {:status status
     :body   (json/decode body true)}))
