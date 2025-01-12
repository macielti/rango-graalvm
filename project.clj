(defproject rango-graalvm "0.1.0-SNAPSHOT"

  :description "Rango is a REST API for school canteen management (GraalVM compliant version)"

  :url "https://github.com/macielti/rango-graalvm"

  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url  "https://www.eclipse.org/legal/epl-2.0/"}

  :exclusions [amazonica]

  :dependencies [[org.clojure/clojure "1.12.0"]
                 [net.clojars.macielti/common-clj "41.74.74"]
                 [net.clojars.macielti/http-client-component "0.1.0"]
                 [io.pedestal/pedestal.service "0.7.2"]
                 [io.pedestal/pedestal.jetty "0.7.2"]
                 [io.pedestal/pedestal.error "0.7.2"]
                 [net.clojars.macielti/service-component "2.4.2"]
                 [net.clojars.macielti/porteiro-component "0.5.2"]
                 [com.github.clj-easy/graal-build-time "1.0.5"]
                 [net.clojars.macielti/new-relic-component "0.2.0"]
                 [net.clojars.macielti/sqlite-component "2.1.1"]
                 [com.taoensso/timbre "6.6.1"]]

  :profiles {:dev {:plugins        [[com.github.clojure-lsp/lein-clojure-lsp "1.4.15"]
                                    [com.github.liquidz/antq "RELEASE"]
                                    [migratus-lein "0.7.3"]
                                    [lein-shell "0.5.0"]]

                   :resource-paths ["resources" "test/resources/"]

                   :test-paths     ["test/unit" "test/integration" "test/helpers"]

                   :dependencies   [[net.clojars.macielti/common-test-clj "5.2.3"]
                                    [danlentz/clj-uuid "0.2.0"]
                                    [hashp "0.2.2"]
                                    [nubank/matcher-combinators "3.9.1"]
                                    [com.github.igrishaev/pg2-migration "0.1.21"]
                                    [migratus "1.6.3"]]

                   :injections     [(require 'hashp.core)]

                   :migratus       {:store         :database
                                    :migration-dir "migrations/"}

                   :repl-options   {:init-ns rango-graalvm.components}

                   :aliases        {"clean-ns"     ["clojure-lsp" "clean-ns" "--dry"] ;; check if namespaces are clean
                                    "format"       ["clojure-lsp" "format" "--dry"] ;; check if namespaces are formatted
                                    "diagnostics"  ["clojure-lsp" "diagnostics"] ;; check if project has any diagnostics (clj-kondo findings)
                                    "lint"         ["do" ["clean-ns"] ["format"] ["diagnostics"]] ;; check all above
                                    "clean-ns-fix" ["clojure-lsp" "clean-ns"] ;; Fix namespaces not clean
                                    "format-fix"   ["clojure-lsp" "format"] ;; Fix namespaces not formatted
                                    "lint-fix"     ["do" ["clean-ns-fix"] ["format-fix"]] ;; Fix both

                                    "native"       ["shell"
                                                    "native-image"

                                                    "--enable-url-protocols=http,https"

                                                    "-march=compatibility"

                                                    "--initialize-at-run-time=io.prometheus.client.Striped64"

                                                    "--initialize-at-build-time"
                                                    "--initialize-at-build-time=org.pg.enums.TxLevel"
                                                    "--initialize-at-build-time=org.pg.enums.CopyFormat"
                                                    "--initialize-at-build-time=org.pg.enums.TXStatus"

                                                    "--report-unsupported-elements-at-runtime"
                                                    "--features=clj_easy.graal_build_time.InitClojureClasses"
                                                    "-Dio.pedestal.log.defaultMetricsRecorder=nil"
                                                    "-jar" "./target/${:uberjar-name:-${:name}-${:version}-standalone.jar}"
                                                    "-H:+UnlockExperimentalVMOptions"
                                                    "-H:ReflectionConfigurationFiles=reflect-config.json"
                                                    "-H:+StaticExecutableWithDynamicLibC"
                                                    "-H:Name=./target/${:name}"]}}}

  :resource-paths ["resources"]

  :main rango-graalvm.components)
