(ns student-test
  (:require [aux.components :as components]
            [aux.http :as http]
            [clj-uuid]
            [clojure.test :refer [is testing]]
            [common-test-clj.helpers.schema :as helpers.schema]
            [fixtures.menu]
            [fixtures.student]
            [integrant.core :as ig]
            [matcher-combinators.test :refer [match?]]
            [rango-graalvm.wire.in.student :as wire.in.student]
            [schema.test :as s]
            [service-component.core :as component.service]))

(s/deftest create-student-test
  (let [system (ig/init components/config-test)
        service-fn (-> system ::component.service/service :io.pedestal.http/service-fn)
        token (-> (http/authenticate-admin {:customer {:username "admin" :password "da3bf409"}} service-fn)
                  :body :token)]

    (testing "Admin is authenticated"
      (is (string? token)))

    (testing "Student was created"
      (is (match? {:status 200
                   :body   {:student {:id         clj-uuid/uuid-string?
                                      :code       fixtures.student/student-code
                                      :name       fixtures.student/student-name
                                      :class      string?
                                      :created-at string?}}}
                  (http/create-student {:student fixtures.student/wire-in-student} token service-fn))))

    (ig/halt! system)))

(s/deftest fetch-student-test
  (let [system (ig/init components/config-test)
        service-fn (-> system ::component.service/service :io.pedestal.http/service-fn)
        token (-> (http/authenticate-admin {:customer {:username "admin" :password "da3bf409"}} service-fn)
                  :body :token)
        student-id (-> (http/create-student {:student fixtures.student/wire-in-student} token service-fn)
                       :body :student :id)]

    (testing "Admin is authenticated"
      (is (string? token)))

    (testing "Student was created"
      (is (clj-uuid/uuid-string? student-id)))

    (testing "Fetch all students"
      (is (match? {:body   {:students [{:id         student-id
                                        :code       fixtures.student/student-code
                                        :name       fixtures.student/student-name
                                        :class      string?
                                        :created-at string?}]}
                   :status 200}
                  (aux.http/fetch-all-students token service-fn))))

    (ig/halt! system)))

(s/deftest delete-one-student-test
  (let [system (ig/init components/config-test)
        service-fn (-> system ::component.service/service :io.pedestal.http/service-fn)
        token (-> (http/authenticate-admin {:customer {:username "admin" :password "da3bf409"}} service-fn)
                  :body :token)
        student-id (-> (http/create-student {:student fixtures.student/wire-in-student} token service-fn)
                       :body :student :id)]

    (testing "Admin is authenticated"
      (is (string? token)))

    (testing "Student was created"
      (is (clj-uuid/uuid-string? student-id)))

    (testing "Delete one student"
      (is (match? {:status 200
                   :body   {}}
                  (aux.http/delete-one-student student-id token service-fn))))

    (testing "Student was deleted"
      (is (match? {:status 200
                   :body   {:students []}}
                  (aux.http/fetch-all-students token service-fn))))

    (ig/halt! system)))

(s/deftest fetch-student-that-has-reservation-for-a-menu-test
  (let [system (ig/init components/config-test)
        service-fn (-> system ::component.service/service :io.pedestal.http/service-fn)
        token (-> (http/authenticate-admin {:customer {:username "admin" :password "da3bf409"}} service-fn)
                  :body :token)
        {student-access-code :code} (-> {:student fixtures.student/wire-in-student}
                                        (http/create-student token service-fn)
                                        :body :student)
        _ (-> {:student (helpers.schema/generate wire.in.student/Student {})}
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

    (testing "Two Students created"
      (is (match? {:status 200
                   :body   {:students [{:id clj-uuid/uuid-string?}
                                       {:id clj-uuid/uuid-string?}]}}
                  (http/fetch-all-students token service-fn))))

    (testing "Fetch student that has reservation for a menu"
      (is (match? {:status 200
                   :body   {:students [{:id   clj-uuid/uuid-string?
                                        :code student-access-code}]}}
                  (http/fetch-students-that-has-reservation-for-a-menu menu-id token service-fn))))

    (ig/halt! system)))
