(ns rango-graalvm.logic.reservation
  (:require [java-time.api :as jt]
            [rango-graalvm.models.menu :as models.menu]
            [rango-graalvm.models.reservation :as models.reservation]
            [rango-graalvm.models.sudent :as models.student]
            [schema.core :as s]))

(s/defn ->reservation :- models.reservation/Reservation
  [{student-id :student/id} :- models.student/Student
   {menu-id :menu/id} :- models.menu/Menu]
  {:reservation/id         (random-uuid)
   :reservation/student-id student-id
   :reservation/menu-id    menu-id
   :reservation/created-at (jt/local-date-time)})
