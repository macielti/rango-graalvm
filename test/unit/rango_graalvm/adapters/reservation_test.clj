(ns rango-graalvm.adapters.reservation-test
  (:require [clj-uuid]
            [clojure.test :refer [is testing]]
            [fixtures.reservation]
            [java-time.api :as jt]
            [matcher-combinators.test :refer [match?]]
            [rango-graalvm.adapters.reservation :as adapters.reservation]
            [schema.test :as s]))

(s/deftest internal->wire-test
  (testing "That we can convert a internal Reservation to a wire outgoing Reservation"
    (is (match? {:id         clj-uuid/uuid-string?
                 :student-id clj-uuid/uuid-string?
                 :menu-id    clj-uuid/uuid-string?
                 :created-at string?}
                (adapters.reservation/internal->wire fixtures.reservation/reservation)))))
