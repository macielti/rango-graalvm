(ns fixtures.student
  (:require [common-test-clj.helpers.schema :as helpers.schema]
            [rango-graalvm.models.sudent :as models.student]
            [rango-graalvm.wire.in.student :as wire.in.student]
            [rango-graalvm.wire.postgresql.student :as wire.postgresql.student]
            [schema.core :as s]))

(def student-id (random-uuid))
(def student-code "f2f203e4")

(s/def wire-in-student :- wire.in.student/Student
  (helpers.schema/generate wire.in.student/Student {}))

(s/def student :- models.student/Student
  (helpers.schema/generate models.student/Student {:student/id   student-id
                                                   :student/code student-code}))

(s/def postgresql-student :- wire.postgresql.student/Student
  (helpers.schema/generate wire.postgresql.student/Student {}))
