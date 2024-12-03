(ns aux.components
  (:require [common-clj.integrant-components.config :as component.config]
            [common-test-clj.component.postgresql-mock :as component.postgresql-mock]
            [integrant.core :as ig]
            [porteiro-component.admin-component :as porteiro.admin]
            [postgresql-component.core :as component.postgresql]
            [rango-graalvm.components :as components]
            [service-component.core :as component.service]))

(def schemas ["CREATE TABLE customers (id UUID PRIMARY KEY, username VARCHAR(255) NOT NULL, name VARCHAR(255), roles TEXT[], hashed_password VARCHAR(255) NOT NULL);"
              "CREATE TABLE menus (id UUID PRIMARY KEY, reference_date DATE NOT NULL, description TEXT NOT NULL, created_at TIMESTAMP NOT NULL)"
              "CREATE TABLE reservations (id UUID PRIMARY KEY, student_id UUID NOT NULL, menu_id UUID NOT NULL, created_at TIMESTAMP NOT NULL)"
              "CREATE TABLE students (id UUID PRIMARY KEY, code VARCHAR(255) NOT NULL, name VARCHAR(255) NOT NULL, class VARCHAR(255) NOT NULL, created_at TIMESTAMP NOT NULL)"])

(def config-test
  (-> components/config
      (dissoc ::component.postgresql/postgresql)
      (merge {::component.config/config                   {:path "resources/config.example.edn"
                                                           :env  :test}
              ::component.postgresql-mock/postgresql-mock {:schemas schemas}})
      (assoc-in [::component.service/service :components :postgresql] (ig/ref ::component.postgresql-mock/postgresql-mock))
      (assoc-in [::porteiro.admin/admin :components :postgresql] (ig/ref ::component.postgresql-mock/postgresql-mock))))
