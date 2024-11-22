(ns menu-test
  (:require [aux.components :as components]
            [aux.http :as http]
            [clj-uuid]
            [clojure.test :refer [is testing]]
            [common-test-clj.helpers.schema :as helpers.schema]
            [fixtures.menu]
            [integrant.core :as ig]
            [matcher-combinators.test :refer [match?]]
            [rango-graalvm.wire.in.menu :as wire.in.menu]
            [schema.test :as s]
            [service-component.core :as component.service]))

(s/deftest create-and-fetch-one-menu-test
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

(s/deftest retract-menu-test
  (let [system (ig/init components/config-test)
        service-fn (-> system ::component.service/service :io.pedestal.http/service-fn)
        token (-> (http/authenticate-admin {:customer {:username "admin" :password "da3bf409"}} service-fn)
                  :body :token)
        {menu-id :id} (-> (http/create-menu {:menu fixtures.menu/wire-in-menu} token service-fn) :body :menu)
        _ (http/create-menu {:menu (helpers.schema/generate wire.in.menu/Menu {:reference-date "2024-02-01"})} token service-fn)]

    (testing "Admin is authenticated"
      (is (string? token)))

    (testing "Menu was created"
      (is (clj-uuid/uuid-string? menu-id)))

    (testing "Created two menus"
      (is (match? {:status 200
                   :body   {:menus [{:id menu-id}
                                    {:id clj-uuid/uuid-string?}]}}
                  (http/fetch-all-menus service-fn))))

    (testing "Retract menu"
      (is (match? {:status 200
                   :body   {}}
                  (http/retract-menu menu-id token service-fn))))

    (testing "Only one menu left"
      (is (match? {:status 200
                   :body   {:menus [{:id clj-uuid/uuid-string?}]}}
                  (http/fetch-all-menus service-fn))))

    (ig/halt! system)))
