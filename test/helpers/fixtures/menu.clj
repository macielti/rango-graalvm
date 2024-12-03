(ns fixtures.menu
  (:require [common-test-clj.helpers.schema :as helpers.schema]
            [rango-graalvm.models.menu :as models.menu]
            [rango-graalvm.wire.in.menu :as wire.in.menu]
            [rango-graalvm.wire.sqlite.menu :as wire.sqlite.menu]
            [schema.core :as s]))
(def menu-id (random-uuid))
(def raw-menu-reference-date "2024-01-01")
(def menu-description "Bolo de cenoura com cobertura de chocolate")

(s/def wire-in-menu :- wire.in.menu/Menu
  (helpers.schema/generate wire.in.menu/Menu
                           {:reference-date raw-menu-reference-date
                            :description    menu-description}))

(s/def menu :- models.menu/Menu
  (helpers.schema/generate models.menu/Menu {:menu/id          menu-id
                                             :menu/description menu-description}))

(s/def sqlite-menu :- wire.sqlite.menu/Menu
  (helpers.schema/generate wire.sqlite.menu/Menu {:menus/id             (str menu-id)
                                                  :menus/reference_date raw-menu-reference-date
                                                  :menus/description    menu-description}))
