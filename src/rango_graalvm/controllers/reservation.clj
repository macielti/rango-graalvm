(ns rango-graalvm.controllers.reservation
  (:require [rango-graalvm.db.postgresql.menu :as database.menu]
            [rango-graalvm.db.postgresql.reservation :as database.reservation]
            [rango-graalvm.db.postgresql.student :as database.student]
            [rango-graalvm.logic.reservation :as logic.reservation]
            [rango-graalvm.models.reservation :as models.reservation]
            [schema.core :as s]))

(s/defn create! :- models.reservation/Reservation
  [student-code :- s/Str
   menu-id :- s/Uuid
   database]
  (let [student (database.student/lookup-by-code student-code database)
        menu (database.menu/lookup menu-id database)]
    (if-let [reservation (database.reservation/lookup-by-student-and-menu (:student/id student) menu-id database)]
      reservation
      (database.reservation/insert! (logic.reservation/->reservation student menu) database))))

(s/defn retract!
  [reservation-id :- s/Uuid
   database]
  (database.reservation/retract! reservation-id database))

(s/defn fetch-by-menu :- [models.reservation/Reservation]
  [menu-id :- s/Uuid
   database]
  (database.reservation/by-menu menu-id database))

(s/defn fetch-reservation :- models.reservation/Reservation
  [reservation-id :- s/Uuid
   database]
  (database.reservation/lookup reservation-id database))

(s/defn fetch-student-reservation-by-menu :- models.reservation/Reservation
  [student-code :- s/Str
   menu-id :- s/Uuid
   database]
  (-> (database.student/lookup-by-code student-code database)
      :student/id
      (database.reservation/lookup-by-student-and-menu menu-id database)))
