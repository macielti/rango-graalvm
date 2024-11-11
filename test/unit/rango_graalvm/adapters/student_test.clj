(ns rango-graalvm.adapters.student-test
  (:require [clj-uuid]
            [clojure.test :refer [is testing]]
            [fixtures.student]
            [java-time.api :as jt]
            [matcher-combinators.test :refer [match?]]
            [rango-graalvm.adapters.student :as adapters.student]
            [schema.test :as s]))

(s/deftest wire->internal-test
  (testing "That we can convert a wire incoming Student to a internal Student"
    (is (match? {:student/id         uuid?
                 :student/code       string?
                 :student/name       string?
                 :student/class      keyword?
                 :student/created-at jt/local-date-time?}
                (adapters.student/wire->internal fixtures.student/wire-in-student)))))

(s/deftest internal->wire-test
  (testing "That we can convert a internal Student to a wire outgoing Student"
    (is (match? {:id         clj-uuid/uuid-string?
                 :code       string?
                 :name       string?
                 :class      string?
                 :created-at string?}
                (adapters.student/internal->wire fixtures.student/student)))))

(s/deftest postgresql->internal-test
  (testing "That we can internalize a postgresql Student"
    (is (match? {:student/id         uuid?
                 :student/code       string?
                 :student/name       string?
                 :student/class      keyword?
                 :student/created-at jt/local-date-time?}
                (adapters.student/postgresql->internal fixtures.student/postgresql-student)))))
