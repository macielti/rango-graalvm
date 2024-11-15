(ns rango-graalvm.db.postgresql.student
  (:require [camel-snake-kebab.core :as csk]
            [pg.core :as pg]
            [rango-graalvm.adapters.student :as adapters.student]
            [rango-graalvm.models.sudent :as models.student]
            [schema.core :as s]))

(s/defn insert! :- models.student/Student
  [{:student/keys [id code name class created-at]} :- models.student/Student
   database-conn]
  (-> (pg/execute database-conn
                  "INSERT INTO students (id, code, name, class, created_at) VALUES ($1, $2, $3, $4, $5)
                  returning *"
                  {:params [id code name (csk/->snake_case_string class) created-at]})
      first
      adapters.student/postgresql->internal))

(s/defn lookup-by-code :- (s/maybe models.student/Student)
  [code :- s/Str
   database-conn]
  (some-> (pg/execute database-conn
                      "SELECT * FROM students WHERE code = $1"
                      {:params [code]})
          first
          adapters.student/postgresql->internal))

(s/defn all :- [models.student/Student]
  [database-conn]
  (some-> (pg/execute database-conn
                      "SELECT * FROM students")
          (->> (map adapters.student/postgresql->internal))))

(s/defn by-menu-reservation :- [models.student/Student]
  [menu-id :- s/Uuid
   database-conn]
  (some-> (pg/execute database-conn
                      "SELECT students.*
                       FROM students
                       JOIN reservations ON students.id = reservations.student_id
                       WHERE
                         reservations.menu_id = $1
                       ORDER BY students.class"
                      {:params [menu-id]})
          (->> (map adapters.student/postgresql->internal))))

(s/defn retract!
  [student-id :- s/Uuid
   database-conn]
  (pg/execute database-conn
              "DELETE FROM students WHERE id = $1"
              {:params [student-id]}))
