(ns rango-graalvm.db.sqlite.student
  (:require [camel-snake-kebab.core :as csk]
            [next.jdbc :as jdbc]
            [rango-graalvm.adapters.student :as adapters.student]
            [rango-graalvm.models.sudent :as models.student]
            [schema.core :as s]))

(s/defn insert! :- models.student/Student
  [{:student/keys [id code name class created-at]} :- models.student/Student
   database-conn]
  (-> (jdbc/execute! database-conn ["INSERT INTO students (id, code, name, class, created_at) VALUES (?, ?, ?, ?, ?) returning *"
                                    (str id) code name (csk/->snake_case_string class) created-at])
      first
      adapters.student/sqlite->internal))

(s/defn lookup-by-code :- (s/maybe models.student/Student)
  [code :- s/Str
   database-conn]
  (some-> (jdbc/execute! database-conn ["SELECT * FROM students WHERE code = ?" code])
          first
          adapters.student/sqlite->internal))

(s/defn all :- [models.student/Student]
  [database-conn]
  (some->> (jdbc/execute! database-conn ["SELECT * FROM students"])
           (map adapters.student/sqlite->internal)))

(s/defn by-menu-reservation :- [models.student/Student]
  [menu-id :- s/Uuid
   database-conn]
  (some->> (jdbc/execute! database-conn ["SELECT students.*
                                          FROM students
                                          JOIN reservations ON students.id = reservations.student_id
                                          WHERE
                                            reservations.menu_id = ?
                                          ORDER BY students.class" (str menu-id)])
           (map adapters.student/sqlite->internal)))

(s/defn retract!
  [student-id :- s/Uuid
   database-conn]
  (jdbc/execute! database-conn ["DELETE FROM students WHERE id = ?" (str student-id)]))
