(defproject rango-graalvm "0.1.0-SNAPSHOT"

  :description "FIXME: write description"

  :url "http://example.com/FIXME"

  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url  "https://www.eclipse.org/legal/epl-2.0/"}

  :exclusions [amazonica]

  :dependencies [[org.clojure/clojure "1.12.0"]
                 [net.clojars.macielti/common-clj "36.71.70"]
                 [net.clojars.macielti/porteiro-component "0.2.1"]
                 [net.clojars.macielti/postgresql-component "2.2.2"]
                 [com.github.clj-easy/graal-build-time "1.0.5"]]

  :resource-paths ["resources"]

  :aot :all

  :main rango_graalvm.components)
