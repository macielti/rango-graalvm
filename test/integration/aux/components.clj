(ns aux.components
  (:require [common-clj.integrant-components.config :as component.config]
            [common-test-clj.component.sqlite-mock :as component.sqlite-mock]
            [integrant.core :as ig]
            [porteiro-component.admin-component :as porteiro.admin]
            [rango-graalvm.components :as components]
            [service-component.core :as component.service]
            [sqlite-component.core :as component.sqlite]))

(def schemas ["CREATE TABLE customers (id TEXT PRIMARY KEY, username TEXT NOT NULL, name TEXT, hashed_password TEXT NOT NULL)"
              "CREATE TABLE roles (id TEXT PRIMARY KEY, customer_id TEXT NOT NULL, role TEXT NOT NULL)"
              "CREATE TABLE menus (id UUID PRIMARY KEY, reference_date DATE NOT NULL, description TEXT NOT NULL, created_at TIMESTAMP NOT NULL)"
              "CREATE TABLE reservations (id UUID PRIMARY KEY, student_id UUID NOT NULL, menu_id UUID NOT NULL, created_at TIMESTAMP NOT NULL)"
              "CREATE TABLE students (id UUID PRIMARY KEY, code VARCHAR(255) NOT NULL, name VARCHAR(255) NOT NULL, class VARCHAR(255) NOT NULL, created_at TIMESTAMP NOT NULL)"])

(def config-test
  (-> components/config
      (dissoc ::component.sqlite/sqlite)
      (merge {::component.config/config           {:path "resources/config.example.edn"
                                                   :env  :test}
              ::component.sqlite-mock/sqlite-mock {:schemas schemas}})
      (assoc-in [::component.service/service :components :sqlite] (ig/ref ::component.sqlite-mock/sqlite-mock))
      (assoc-in [::porteiro.admin/admin :components :sqlite] (ig/ref ::component.sqlite-mock/sqlite-mock))))
