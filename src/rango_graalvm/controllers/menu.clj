(ns rango-graalvm.controllers.menu
  (:require [rango-graalvm.db.sqlite.menu :as database.menu]
            [rango-graalvm.models.menu :as models.menu]
            [schema.core :as s]))

(s/defn create! :- models.menu/Menu
  [menu :- models.menu/Menu
   database]
  (try (database.menu/insert! menu database)
       (catch Exception e
         #p e)))

(s/defn fetch-one :- models.menu/Menu
  [menu-id :- s/Uuid
   database]
  (database.menu/lookup menu-id database))

(s/defn fetch-all :- [models.menu/Menu]
  [database]
  (database.menu/all database))

(s/defn retract!
  [menu-id :- s/Uuid
   database]
  (database.menu/retract! menu-id database))
