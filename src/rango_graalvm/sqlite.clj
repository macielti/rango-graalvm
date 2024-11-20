(ns rango-graalvm.sqlite
  (:require [clojure.tools.logging :as log]
            [integrant.core :as ig]
            [migratus.core :as migratus]))

(defmethod ig/init-key ::sqlite
  [_ {:keys [components]}]
  (log/info :starting ::sqlite)
  (let [sqlite (-> components :config :sqlite)]
    (migratus/migrate {:store         :database
                       :migration-dir "migrations-sqlite/"
                       :db            sqlite})
    sqlite))

(defmethod ig/halt-key! ::sqlite
  [_ _]
  (log/info :stopping ::sqlite))
