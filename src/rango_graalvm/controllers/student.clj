(ns rango-graalvm.controllers.student
  (:require [pg.pool :as pool]
            [rango-graalvm.db.postgresql.student :as database.student]
            [rango-graalvm.models.sudent :as models.student]
            [schema.core :as s]))

(s/defn create! :- models.student/Student
  [student :- models.student/Student
   postgresql]
  (pool/with-connection [database-conn postgresql]
    (if-let [student' (database.student/lookup-by-code (:student/code student) database-conn)]
      student'
      (database.student/insert! student database-conn))))

(s/defn fetch-all :- [models.student/Student]
  [postgresql]
  (pool/with-connection [database-conn postgresql]
    (database.student/all database-conn)))

(s/defn fetch-students-by-menu-reservations :- [models.student/Student]
  [menu-id :- s/Uuid
   postgresql]
  (pool/with-connection [database-conn postgresql]
    (database.student/by-menu-reservation menu-id database-conn)))

(s/defn retract!
  [student-id :- s/Uuid
   postgresql]
  (pool/with-connection [database-conn postgresql]
    (database.student/retract! student-id database-conn)))
