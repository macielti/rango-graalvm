(ns rango-graalvm.db.postgresql.menu
  (:require [pg.core :as pg]
            [pg.pool :as pool]
            [rango-graalvm.adapters.menu :as adapters.menu]
            [rango-graalvm.models.menu :as models.menu]
            [schema.core :as s]))

(s/defn insert! :- models.menu/Menu
  [{:menu/keys [id reference-date description created-at]} :- models.menu/Menu
   postgresql-pool]
  (pool/with-connection [database-conn postgresql-pool]
    (-> (pg/execute database-conn
                    "INSERT INTO menus (id, reference_date, description, created_at) VALUES ($1, $2, $3, $4)
                    returning *"
                    {:params [id reference-date description created-at]})
        first
        adapters.menu/postgresql->internal)))


(s/defn lookup :- (s/maybe models.menu/Menu)
  [menu-id :- s/Uuid
   postgresql-pool]
  (pool/with-connection [database-conn postgresql-pool]
    (some-> (pg/execute database-conn
                        "SELECT * FROM menus WHERE id = $1"
                        {:params [menu-id]})
            first
            adapters.menu/postgresql->internal)))

(s/defn all :- [models.menu/Menu]
  [postgresql-pool]
  (pool/with-connection [database-conn postgresql-pool]
    (some-> (pg/execute database-conn
                        "SELECT * FROM menus")
            (->> (map adapters.menu/postgresql->internal)))))

(s/defn retract!
  [menu-id :- s/Uuid
   postgresql-pool]
  (pool/with-connection [database-conn postgresql-pool]
    (pg/execute database-conn
                "DELETE FROM menus WHERE id = $1"
                {:params [menu-id]})))
