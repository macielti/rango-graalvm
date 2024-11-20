(ns rango-graalvm.db.sqlite.reservation
  (:require [next.jdbc :as jdbc]
            [rango-graalvm.adapters.reservation :as adapters.reservation]
            [rango-graalvm.models.reservation :as models.reservation]
            [schema.core :as s]))

(s/defn insert! :- models.reservation/Reservation
  [{:reservation/keys [id student-id menu-id created-at]} :- models.reservation/Reservation
   database]
  (with-open [conn (jdbc/get-connection database)]
    (-> (jdbc/execute! conn ["INSERT INTO reservations (id, student_id, menu_id, created_at) VALUES (?, ?, ?, ?) returning *"
                             (str id) (str student-id) (str menu-id) created-at])
        first
        adapters.reservation/sqlite->internal)))

(s/defn by-menu :- [models.reservation/Reservation]
  [menu-id :- s/Uuid
   database]
  (with-open [conn (jdbc/get-connection database)]
    (some->> (jdbc/execute! conn ["SELECT * FROM reservations WHERE menu_id = ?" (str menu-id)])
             (map adapters.reservation/sqlite->internal))))

(s/defn lookup :- (s/maybe models.reservation/Reservation)
  [reservation-id :- s/Uuid
   database]
  (with-open [conn (jdbc/get-connection database)]
    (some-> (jdbc/execute! conn ["SELECT * FROM reservations WHERE id = ?" (str reservation-id)])
            first
            adapters.reservation/sqlite->internal)))

(s/defn lookup-by-student-and-menu :- (s/maybe models.reservation/Reservation)
  [student-id :- s/Uuid
   menu-id :- s/Uuid
   database]
  (with-open [conn (jdbc/get-connection database)]
    (some-> (jdbc/execute! conn ["SELECT * FROM reservations WHERE menu_id = ? AND student_id = ?"
                                 (str menu-id) (str student-id)])
            first
            adapters.reservation/sqlite->internal)))

(s/defn retract!
  [reservation-id :- s/Uuid
   database]
  (with-open [conn (jdbc/get-connection database)]
    (jdbc/execute! conn ["DELETE FROM menus WHERE id = ?" (str reservation-id)])))

(s/defn fetch-student-reservation-by-menu :- (s/maybe models.reservation/Reservation)
  [student-id :- s/Uuid
   menu-id :- s/Uuid
   database]
  (with-open [conn (jdbc/get-connection database)]
    (some-> (jdbc/execute! conn ["SELECT *
                                  FROM reservations
                                  WHERE
                                    menu_id = ? AND student_id = ?"
                                 (str menu-id) (str student-id)])
            first
            adapters.reservation/sqlite->internal)))
