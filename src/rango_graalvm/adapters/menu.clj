(ns rango-graalvm.adapters.menu
  (:require [java-time.api :as jt]
            [rango-graalvm.models.menu :as models.menu]
            [rango-graalvm.wire.in.menu :as wire.in.menu]
            [rango-graalvm.wire.out.menu :as wire.out.menu]
            [rango-graalvm.wire.postgresql.menu :as wire.postgresql.menu]
            [schema.core :as s]))

(s/defn wire->internal :- models.menu/Menu
  [{:keys [reference-date description]} :- wire.in.menu/Menu]
  {:menu/id             (random-uuid)
   :menu/description    description
   :menu/reference-date (jt/local-date reference-date)
   :menu/created-at     (jt/local-date-time)})

(s/defn internal->wire :- wire.out.menu/Menu
  [{:menu/keys [id description reference-date created-at]} :- models.menu/Menu]
  {:id             (str id)
   :reference-date (str reference-date)
   :description    description
   :created-at     (str created-at)})

(s/defn postgresql->internal :- models.menu/Menu
  [menu :- wire.postgresql.menu/Menu]
  {:menu/id             (:id menu)
   :menu/reference-date (:reference_date menu)
   :menu/description    (:description menu)
   :menu/created-at     (:created_at menu)})
