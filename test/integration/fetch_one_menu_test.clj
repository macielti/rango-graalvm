(ns fetch-one-menu-test
  (:require [aux.components :as components]
            [aux.http :as http]
            [clj-uuid]
            [clojure.test :refer [is testing]]
            [fixtures.menu]
            [integrant.core :as ig]
            [matcher-combinators.test :refer [match?]]
            [schema.test :as s]
            [service-component.core :as component.service]))

(s/deftest fetch-one-menu-test
  (let [system (ig/init components/config-test)
        service-fn (-> system ::component.service/service :io.pedestal.http/service-fn)
        token (-> (http/authenticate-admin {:customer {:username "admin" :password "da3bf409"}} service-fn)
                  :body :token)
        {menu-id :id} (-> (http/create-menu {:menu fixtures.menu/wire-in-menu} token service-fn) :body :menu)]

    (testing "Admin is authenticated"
      (is (string? token)))

    (testing "Menu was created"
      (is (clj-uuid/uuid-string? menu-id)))

    (testing "Fetch one menu"
      (is (match? {:status 200
                   :body   {:menu {:id clj-uuid/uuid-string?}}}
                  (http/fetch-one-menu menu-id service-fn))))

    (ig/halt! system)))
