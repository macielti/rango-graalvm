(ns rango-graalvm.components
  (:require [common-clj.integrant-components.config :as component.config]
            [postgresql-component.core :as component.postgresql]
            [porteiro-component.admin-component :as porteiro.admin]
            [porteiro-component.diplomat.http-server :as porteiro.diplomat.http-server]
            [common-clj.integrant-components.routes :as component.routes]
            [rango-graalvm.service-component.core :as component.service]
            [integrant.core :as ig]
            [taoensso.timbre.tools.logging])
  (:gen-class))

(taoensso.timbre.tools.logging/use-timbre)

(def config
  {::component.config/config         {:path "resources/config.edn"
                                      :env  :prod}
   ::component.postgresql/postgresql {:components {:config (ig/ref ::component.config/config)}}
   ::porteiro.admin/admin            {:components {:config     (ig/ref ::component.config/config)
                                                   :postgresql (ig/ref ::component.postgresql/postgresql)}}
   ::component.routes/routes         {:routes porteiro.diplomat.http-server/routes}
   ::component.service/service       {:components {:config     (ig/ref ::component.config/config)
                                                   :routes     (ig/ref ::component.routes/routes)
                                                   :postgresql (ig/ref ::component.postgresql/postgresql)}}})

(defn start-system! []
  (ig/init config))

(defn -main [& _args]
  (start-system!))
