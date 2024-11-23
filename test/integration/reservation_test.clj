(ns reservation-test
  (:require [aux.components :as components]
            [aux.http :as http]
            [clj-uuid]
            [clojure.test :refer [is testing]]
            [common-test-clj.helpers.schema :as helpers.schema]
            [fixtures.menu]
            [fixtures.student]
            [integrant.core :as ig]
            [matcher-combinators.test :refer [match?]]
            [rango-graalvm.wire.in.menu :as wire.in.menu]
            [schema.test :as s]
            [service-component.core :as component.service]))

(s/deftest create-reservation-test
  (let [system (ig/init components/config-test)
        service-fn (-> system ::component.service/service :io.pedestal.http/service-fn)
        token (-> (http/authenticate-admin {:customer {:username "admin" :password "da3bf409"}} service-fn)
                  :body :token)
        {student-access-code :code} (-> {:student fixtures.student/wire-in-student}
                                        (http/create-student token service-fn)
                                        :body :student)
        {menu-id :id} (-> (http/create-menu {:menu fixtures.menu/wire-in-menu} token service-fn) :body :menu)]

    (testing "Admin is authenticated"
      (is (string? token)))

    (testing "Student was created"
      (is (string? student-access-code)))

    (testing "Menu was created"
      (is (clj-uuid/uuid-string? menu-id)))

    (testing "Reservation was created"
      (is (match? {:created-at string?
                   :id         clj-uuid/uuid-string?
                   :menu-id    clj-uuid/uuid-string?
                   :student-id clj-uuid/uuid-string?}
                  (-> (http/create-reservation {:reservation {:student-code student-access-code
                                                              :menu-id      menu-id}} service-fn)
                      :body :reservation))))

    (testing "Attempt to create reservation with invalid student code"
      (is (= {:status 404
              :body   {:error   "resource-not-found"
                       :message "Resource could not be found"
                       :detail  "Not Found"}}
             (http/create-reservation {:reservation {:student-code "random-student-code-wrong"
                                                     :menu-id      menu-id}} service-fn))))

    (ig/halt! system)))

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

(s/deftest fetch-student-reservation-by-menu-test
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

    (testing "Fetch reservation"
      (is (match? {:status 200
                   :body   {:reservation {:id         clj-uuid/uuid-string?
                                          :student-id clj-uuid/uuid-string?
                                          :menu-id    menu-id}}}
                  (http/fetch-student-reservation-by-menu student-access-code menu-id service-fn))))

    (ig/halt! system)))

(s/deftest fetch-one-reservation-test
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

    (testing "Fetch one reservation"
      (is (match? {:status 200
                   :body   {:reservation {:id         clj-uuid/uuid-string?
                                          :student-id clj-uuid/uuid-string?
                                          :menu-id    menu-id}}}
                  (http/fetch-one-reservation reservation-id service-fn))))

    (ig/halt! system)))

(s/deftest fetch-reservations-by-menu-test
  (let [system (ig/init components/config-test)
        service-fn (-> system ::component.service/service :io.pedestal.http/service-fn)
        token (-> (http/authenticate-admin {:customer {:username "admin" :password "da3bf409"}} service-fn)
                  :body :token)
        {student-access-code :code} (-> {:student fixtures.student/wire-in-student}
                                        (http/create-student token service-fn)
                                        :body :student)
        {menu-id :id} (-> (http/create-menu {:menu fixtures.menu/wire-in-menu} token service-fn) :body :menu)
        {menu-id-ii :id} (-> (http/create-menu {:menu (helpers.schema/generate wire.in.menu/Menu {:reference-date "2024-02-01"})} token service-fn) :body :menu)
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

    (testing "Fetch reservation by menu-id"
      (is (match? {:status 200
                   :body   {:reservations [{:id reservation-id}]}}
                  (http/fetch-reservations-by-menu menu-id service-fn))))

    (testing "No reservations for the second menu"
      (is (match? {:status 200
                   :body   {:reservations []}}
                  (http/fetch-reservations-by-menu menu-id-ii service-fn))))

    (ig/halt! system)))
