(ns rango-graalvm.components
  (:require [clojure.tools.logging :as log]
            [common-clj.integrant-components.config :as component.config]
            [common-clj.integrant-components.prometheus :as component.prometheus]
            [common-clj.integrant-components.routes :as component.routes]
            [http-client-component.core :as component.http-client]
            [integrant.core :as ig]
            [new-relic-component.core :as component.new-relic]
            [porteiro-component.admin-component :as porteiro.admin]
            [porteiro-component.diplomat.http-server :as porteiro.diplomat.http-server]
            [postgresql-component.core :as component.postgresql]
            [rango-graalvm.diplomat.http-server :as diplomat.http-server]
            [service-component.core :as component.service]
            [taoensso.timbre.tools.logging])
  (:gen-class))

(taoensso.timbre.tools.logging/use-timbre)

(def config
  {::component.config/config           {:path "resources/config.edn"
                                        :env  :prod}
   ::component.prometheus/prometheus   {:metrics []}
   ::component.http-client/http-client {:components {:config     (ig/ref ::component.config/config)
                                                     :prometheus (ig/ref ::component.prometheus/prometheus)}}
   ::component.new-relic/new-relic     {:components {:config      (ig/ref ::component.config/config)
                                                     :http-client (ig/ref ::component.http-client/http-client)}}
   ::component.postgresql/postgresql   {:components {:config (ig/ref ::component.config/config)}}
   ::porteiro.admin/admin              {:components {:config     (ig/ref ::component.config/config)
                                                     :postgresql (ig/ref ::component.postgresql/postgresql)}}
   ::component.routes/routes           {:routes (concat diplomat.http-server/routes porteiro.diplomat.http-server/routes)}
   ::component.service/service         {:components {:config     (ig/ref ::component.config/config)
                                                     :routes     (ig/ref ::component.routes/routes)
                                                     :postgresql (ig/ref ::component.postgresql/postgresql)}}})

(defn start-system! []
  (ig/init config))

(defn -main [& _args]
  (start-system!)
  (log/info :service-is-ready))
