(ns rango-graalvm.components
  (:require [common-clj.integrant-components.config :as component.config]
            [common-clj.integrant-components.prometheus :as component.prometheus]
            [common-clj.integrant-components.http-client :as component.http-client]
            [integrant.core :as ig]
            [taoensso.timbre.tools.logging])
  (:gen-class))

(taoensso.timbre.tools.logging/use-timbre)

(def config
  {::component.config/config           {:path "resources/config.edn"
                                        :env  :prod}
   ::component.prometheus/prometheus   {:metrics []}
   ::component.http-client/http-client {:components {:config     (ig/ref ::component.config/config)
                                                     :prometheus (ig/ref ::component.prometheus/prometheus)}}})

(defn start-system! []
  (ig/init config))

(defn -main [& _args]
  (start-system!))
