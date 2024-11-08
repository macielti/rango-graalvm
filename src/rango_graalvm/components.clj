(ns rango-graalvm.components
  (:require [common-clj.integrant-components.config :as component.config]
            [postgresql-component.core :as component.postgresql]
            [porteiro-component.admin-component :as porteiro.admin]
            [integrant.core :as ig]
            [taoensso.timbre.tools.logging])
  (:gen-class))

(taoensso.timbre.tools.logging/use-timbre)

(def config
  {::component.config/config         {:path "resources/config.edn"
                                      :env  :prod}
   ::component.postgresql/postgresql {:components {:config (ig/ref ::component.config/config)}}
   ::porteiro.admin/admin            {:components {:config     (ig/ref ::component.config/config)
                                                   :postgresql (ig/ref ::component.postgresql/postgresql)}}})

(defn start-system! []
  (ig/init config))

(defn -main [& _args]
  (start-system!))
