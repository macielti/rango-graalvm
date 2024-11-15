(ns retract-reservation-test
  (:require [aux.components :as components]
            [aux.http :as http]
            [clj-uuid]
            [clojure.test :refer [is testing]]
            [fixtures.menu]
            [fixtures.student]
            [integrant.core :as ig]
            [schema.test :as s]
            [service-component.core :as component.service]))

(s/deftest retract-reservation-test
  (let [system (ig/init components/config-test)
        service-fn (-> system ::component.service/service :io.pedestal.http/service-fn)
        token (-> (http/authenticate-admin {:customer {:username "admin" :password "da3bf409"}} service-fn)
                  :body :token)
        {student-access-code :code} (-> {:student fixtures.student/wire-in-student}
                                        (http/create-student token service-fn)
                                        :body :student)
        {menu-id :id} (-> (http/create-menu {:menu fixtures.menu/wire-in-menu} token service-fn) :body :menu)
        {reservation-id :id} (-> (http/create-reservation {:reservation {:student-code student-access-code
                                                                         :menu-id      menu-id}} service-fn)
                                 :body :reservation)]

    (testing "Admin is authenticated"
      (is (string? token)))

    (testing "Student was created"
      (is (string? student-access-code)))

    (testing "Menu was created"
      (is (clj-uuid/uuid-string? menu-id)))

    (testing "Reservation was created"
      (is (clj-uuid/uuid-string? reservation-id)))

    (testing "Retract reservation"
      (is (= {:status 200
              :body   {}}
             (http/retract-reservation reservation-id service-fn))))

    (testing "Retract reservation that does not exist"
      (is (= {:status 404
              :body   {:detail  "Not Found"
                       :error   "resource-not-found"
                       :message "Resource could not be found"}}
             (http/retract-reservation (random-uuid) service-fn))))

    (ig/halt! system)))
