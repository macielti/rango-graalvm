(ns rango-graalvm.db.sqlite.menu
  (:require [next.jdbc :as jdbc]
            [rango-graalvm.adapters.menu :as adapters.menu]
            [rango-graalvm.models.menu :as models.menu]
            [schema.core :as s]))

(s/defn insert! :- models.menu/Menu
  [{:menu/keys [id reference-date description created-at]} :- models.menu/Menu
   database]
  (with-open [conn (jdbc/get-connection database)]
    #p (-> (jdbc/execute! conn ["INSERT INTO menus (id, reference_date, description, created_at) VALUES (?, ?, ?, ?)
                          returning *"
                             (str id) reference-date description created-at])
        first
        #_adapters.menu/sqlite->internal)))

(s/defn lookup :- (s/maybe models.menu/Menu)
  [menu-id :- s/Uuid
   database]
  (with-open [conn (jdbc/get-connection database)]
    (some-> (jdbc/execute! conn ["SELECT * FROM menus WHERE id = $1" (str menu-id)])
            first
            adapters.menu/sqlite->internal)))

(s/defn all :- [models.menu/Menu]
  [database]
  (with-open [conn (jdbc/get-connection database)]
    (some-> (jdbc/execute! conn ["SELECT * FROM menus"])
            (->> (map adapters.menu/sqlite->internal)))))

(s/defn retract!
  [menu-id :- s/Uuid
   database]
  (with-open [conn (jdbc/get-connection database)]
    (jdbc/execute! conn ["DELETE FROM menus WHERE id = $1" (str menu-id)])))


