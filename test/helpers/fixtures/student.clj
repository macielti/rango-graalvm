(ns fixtures.student
  (:require [common-test-clj.helpers.schema :as helpers.schema]
            [rango-graalvm.models.sudent :as models.student]
            [rango-graalvm.wire.in.student :as wire.in.student]
            [rango-graalvm.wire.sqlite.student :as wire.sqlite.student]
            [schema.core :as s]))

(def student-id (random-uuid))
(def student-code "f2f203e4")
(def student-name "Manuel Gomes")

(s/def wire-in-student :- wire.in.student/Student
  (helpers.schema/generate wire.in.student/Student {:code student-code
                                                    :name student-name}))

(s/def student :- models.student/Student
  (helpers.schema/generate models.student/Student {:student/id   student-id
                                                   :student/code student-code}))

(s/def sqlite-student :- wire.sqlite.student/Student
  (helpers.schema/generate wire.sqlite.student/Student {:students/id   (str student-id)
                                                        :students/code student-code
                                                        :students/name student-name}))
