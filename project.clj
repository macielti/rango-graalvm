(defproject rango-graalvm "0.1.0-SNAPSHOT"

  :description "FIXME: write description"

  :url "http://example.com/FIXME"

  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url  "https://www.eclipse.org/legal/epl-2.0/"}

  :exclusions [amazonica]

  :dependencies [[org.clojure/clojure "1.12.0"]
                 [net.clojars.macielti/common-clj "37.71.70"]
                 [io.pedestal/pedestal.service "0.7.2"]
                 [io.pedestal/pedestal.jetty "0.7.2"]
                 [io.pedestal/pedestal.interceptor "0.7.2"]
                 [io.pedestal/pedestal.error "0.7.2"]
                 [net.clojars.macielti/porteiro-component "0.3.1"]
                 [net.clojars.macielti/postgresql-component "2.2.2"]
                 [com.github.clj-easy/graal-build-time "1.0.5"]
                 [ch.qos.logback/logback-classic "1.2.11"]]

  :resource-paths ["resources"]

  :main rango-graalvm.components)
