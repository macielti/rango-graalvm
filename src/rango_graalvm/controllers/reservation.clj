(ns rango-graalvm.controllers.reservation
  (:require [pg.pool :as pool]
            [rango-graalvm.db.postgresql.menu :as database.menu]
            [rango-graalvm.db.postgresql.reservation :as database.reservation]
            [rango-graalvm.db.postgresql.student :as database.student]
            [rango-graalvm.logic.reservation :as logic.reservation]
            [rango-graalvm.models.reservation :as models.reservation]
            [schema.core :as s]))

(s/defn create! :- models.reservation/Reservation
  [student-code :- s/Str
   menu-id :- s/Uuid
   postgresql]
  (pool/with-connection [database-conn postgresql]
    (let [student (database.student/lookup-by-code student-code database-conn)
          menu (database.menu/lookup menu-id database-conn)]
      (if-let [reservation (database.reservation/lookup-by-student-and-menu (:student/id student) menu-id database-conn)]
        reservation
        (database.reservation/insert! (logic.reservation/->reservation student menu) database-conn)))))

(s/defn fetch-by-menu :- [models.reservation/Reservation]
  [menu-id :- s/Uuid
   postgresql]
  (pool/with-connection [database-conn postgresql]
    (database.reservation/by-menu menu-id database-conn)))
