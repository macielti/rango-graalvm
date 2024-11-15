(ns aux.components
  (:require [rango-graalvm.components :as components]
            [common-test-clj.component.postgresql-mock :as component.postgresql-mock]
            [common-clj.integrant-components.config :as component.config]
            [postgresql-component.core :as component.postgresql]
            [porteiro-component.admin-component :as porteiro.admin]
            [service-component.core :as component.service]
            [integrant.core :as ig]))

(def config-test
  (-> components/config
      (dissoc ::component.postgresql/postgresql)
      (merge {::component.config/config                   {:path "resources/config.example.edn"
                                                           :env  :test}
              ::component.postgresql-mock/postgresql-mock {:components {:config (ig/ref ::component.config/config)}}})
      (assoc-in [::component.service/service :components :postgresql] (ig/ref ::component.postgresql-mock/postgresql-mock))
      (assoc-in [::porteiro.admin/admin :components :postgresql] (ig/ref ::component.postgresql-mock/postgresql-mock))))
