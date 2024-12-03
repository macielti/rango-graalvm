(ns rango-graalvm.db.sqlite.menu
  (:require [next.jdbc :as jdbc]
            [rango-graalvm.adapters.menu :as adapters.menu]
            [rango-graalvm.models.menu :as models.menu]
            [schema.core :as s]))

(s/defn insert! :- models.menu/Menu
  [{:menu/keys [id reference-date description created-at]} :- models.menu/Menu
   database-conn]
  (-> (jdbc/execute! database-conn ["INSERT INTO menus (id, reference_date, description, created_at) VALUES (?, ?, ?, ?)
                          returning *"
                                    (str id) reference-date description created-at])
      first
      adapters.menu/sqlite->internal))

(s/defn lookup :- (s/maybe models.menu/Menu)
  [menu-id :- s/Uuid
   database-conn]
  (some-> (jdbc/execute! database-conn ["SELECT * FROM menus WHERE id = $1" (str menu-id)])
          first
          adapters.menu/sqlite->internal))

(s/defn all :- [models.menu/Menu]
  [database-conn]
  (some-> (jdbc/execute! database-conn ["SELECT * FROM menus"])
          (->> (map adapters.menu/sqlite->internal))))

(s/defn retract!
  [menu-id :- s/Uuid
   database-conn]
  (jdbc/execute! database-conn ["DELETE FROM menus WHERE id = $1" (str menu-id)]))


