(ns aux.components
  (:require [common-clj.integrant-components.config :as component.config]
            [common-test-clj.component.sqlite-mock :as component.sqlite-mock]
            [integrant.core :as ig]
            [porteiro-component.admin-component :as porteiro.admin]
            [rango-graalvm.components :as components]
            [sqlite-component.core :as component.sqlite]
            [rango-graalvm.db.sqlite.config :as sqlite.config]
            [service-component.core :as component.service]))

(def config-test
  (-> components/config
      (dissoc ::component.sqlite/sqlite)
      (merge {::component.config/config           {:path "resources/config.example.edn"
                                                   :env  :test}
              ::component.sqlite-mock/sqlite-mock {:schemas sqlite.config/schemas}})
      (assoc-in [::component.service/service :components :sqlite] (ig/ref ::component.sqlite-mock/sqlite-mock))
      (assoc-in [::porteiro.admin/admin :components :sqlite] (ig/ref ::component.sqlite-mock/sqlite-mock))))
