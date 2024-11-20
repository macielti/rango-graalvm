(ns rango-graalvm.adapters.reservation
  (:require [java-time.api :as jt]
            [rango-graalvm.models.reservation :as models.reservation]
            [rango-graalvm.wire.out.reservation :as wire.out.reservation]
            [rango-graalvm.wire.postgresql.reservation :as wire.postgresql.reservation]
            [schema.core :as s])
  (:import (java.util UUID)))

(s/defn internal->wire :- wire.out.reservation/Reservation
  [{:reservation/keys [id student-id menu-id created-at]} :- models.reservation/Reservation]
  {:id         (str id)
   :student-id (str student-id)
   :menu-id    (str menu-id)
   :created-at (str created-at)})

(s/defn postgresql->internal :- models.reservation/Reservation
  [{:keys [id student_id menu_id created_at]} :- wire.postgresql.reservation/Reservation]
  {:reservation/id         id
   :reservation/student-id student_id
   :reservation/menu-id    menu_id
   :reservation/created-at created_at})

(s/defn sqlite->internal :- models.reservation/Reservation
  [{:reservations/keys [id student_id menu_id created_at]}]
  {:reservation/id         (UUID/fromString id)
   :reservation/student-id (UUID/fromString student_id)
   :reservation/menu-id    (UUID/fromString menu_id)
   :reservation/created-at (jt/local-date-time created_at)})
