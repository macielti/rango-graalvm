(ns rango-graalvm.controllers.student
  (:require [rango-graalvm.db.sqlite.student :as database.student]
            [rango-graalvm.models.sudent :as models.student]
            [schema.core :as s]))

(s/defn create! :- models.student/Student
  [student :- models.student/Student
   database]
  (if-let [student' (database.student/lookup-by-code (:student/code student) database)]
    student'
    (database.student/insert! student database)))

(s/defn fetch-all :- [models.student/Student]
  [database]
  (database.student/all database))

(s/defn fetch-students-by-menu-reservations :- [models.student/Student]
  [menu-id :- s/Uuid
   database]
  (database.student/by-menu-reservation menu-id database))

(s/defn retract!
  [student-id :- s/Uuid
   database]
  (database.student/retract! student-id database))
