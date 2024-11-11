(ns rango-graalvm.adapters.menu-test
  (:require [clj-uuid]
            [clojure.test :refer [is testing]]
            [fixtures.menu]
            [java-time.api :as jt]
            [matcher-combinators.test :refer [match?]]
            [rango-graalvm.adapters.menu :as adapters.menu]
            [schema.test :as s]))

(s/deftest wire->internal-test
  (testing "That we can covert a wire incoming Menu to a internal Menu"
    (is (match? {:menu/created-at     jt/local-date-time?
                 :menu/description    "Bolo de cenoura com cobertura de chocolate"
                 :menu/id             uuid?
                 :menu/reference-date jt/local-date?}
                (adapters.menu/wire->internal fixtures.menu/wire-in-menu)))))

(s/deftest internal->wire-test
  (testing "That we can convert a internal Menu to a wire outgoing Menu"
    (is (match? {:id             clj-uuid/uuid-string?
                 :reference-date string?
                 :description    string?
                 :created-at     string?}
                (adapters.menu/internal->wire fixtures.menu/menu)))))

(s/deftest postgresql->internal-test
  (testing "That we can internalize a postgresql Menu"
    (is (match? {:menu/id             uuid?
                 :menu/reference-date jt/local-date?
                 :menu/description    string?
                 :menu/created-at     jt/local-date-time?}
                (adapters.menu/postgresql->internal fixtures.menu/postgresql-menu)))))
