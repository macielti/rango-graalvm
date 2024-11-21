(ns rango-graalvm.sqlite
  (:require [clojure.tools.logging :as log]
            [integrant.core :as ig]
            [next.jdbc :as jdbc]))

(defmethod ig/init-key ::sqlite
  [_ {:keys [components schemas]}]
  (log/info :starting ::sqlite)
  (let [sqlite (-> components :config :sqlite)]
    (with-open [conn (jdbc/get-connection sqlite)]
      (doseq [schema schemas]
        (try (jdbc/execute! conn [schema])
             (catch Exception e
               (log/error (ex-message e))))))
    sqlite))

(defmethod ig/halt-key! ::sqlite
  [_ _]
  (log/info :stopping ::sqlite))
