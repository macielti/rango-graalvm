(ns rango-graalvm.db.postgresql.reservation
  (:require [pg.core :as pg]
            [rango-graalvm.adapters.reservation :as adapters.reservation]
            [rango-graalvm.models.reservation :as models.reservation]
            [schema.core :as s]))

(s/defn insert! :- models.reservation/Reservation
  [{:reservation/keys [id student-id menu-id created-at]} :- models.reservation/Reservation
   database-conn]
  (-> (pg/execute database-conn
                  "INSERT INTO reservations (id, student_id, menu_id, created_at) VALUES ($1, $2, $3, $4)
                  returning *"
                  {:params [id student-id menu-id created-at]})
      first
      adapters.reservation/postgresql->internal))

(s/defn by-menu :- [models.reservation/Reservation]
  [menu-id :- s/Uuid
   database-conn]
  (some-> (pg/execute database-conn
                      "SELECT * FROM reservations WHERE menu_id = $1"
                      {:params [menu-id]})
          (->> (map adapters.reservation/postgresql->internal))))

(s/defn lookup-by-student-and-menu :- (s/maybe models.reservation/Reservation)
  [student-id :- s/Uuid
   menu-id :- s/Uuid
   database-conn]
  (some-> (pg/execute database-conn
                      "SELECT * FROM reservations WHERE menu_id = $1 AND student_id = $2"
                      {:params [menu-id student-id]})
          first
          adapters.reservation/postgresql->internal))
