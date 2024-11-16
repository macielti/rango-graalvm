(ns rango-graalvm.diplomat.http-server
  (:require [common-clj.traceability.core :as common-traceability]
            [porteiro-component.interceptors.authentication :as io.interceptors.authentication]
            [rango-graalvm.diplomat.http-server.menu :as diplomat.http-server.menu]
            [rango-graalvm.diplomat.http-server.reservation :as diplomat.http-server.reservation]
            [rango-graalvm.diplomat.http-server.student :as diplomat.http-server.student]
            [rango-graalvm.interceptors.menu :as interceptors.menu]
            [rango-graalvm.interceptors.reservation :as interceptors.reservation]
            [rango-graalvm.interceptors.student :as interceptors.student]))

(def routes [["/api/students"
              :post [io.interceptors.authentication/identity-interceptor
                     (io.interceptors.authentication/required-roles-interceptor [:admin])
                     (common-traceability/http-with-correlation-id diplomat.http-server.student/create-student!)]
              :route-name :create-student]

             ["/api/students"
              :get [io.interceptors.authentication/identity-interceptor
                    (io.interceptors.authentication/required-roles-interceptor [:admin])
                    (common-traceability/http-with-correlation-id diplomat.http-server.student/fetch-all)]
              :route-name :fetch-all-students]

             ["/api/students/:student-id"
              :delete [io.interceptors.authentication/identity-interceptor
                       (io.interceptors.authentication/required-roles-interceptor [:admin])
                       interceptors.student/student-resource-existence-interceptor-check
                       (common-traceability/http-with-correlation-id diplomat.http-server.student/retract-student!)]
              :route-name :retract-student]

             ["/api/students-by-menu-reservations/:menu-id"
              :get [io.interceptors.authentication/identity-interceptor
                    (io.interceptors.authentication/required-roles-interceptor [:admin])
                    interceptors.menu/menu-resource-existence-interceptor-check
                    (common-traceability/http-with-correlation-id diplomat.http-server.student/fetch-students-by-reservations-menu)]
              :route-name :fetch-students-by-menu-reservations]

             ["/api/menus"
              :post [io.interceptors.authentication/identity-interceptor
                     (io.interceptors.authentication/required-roles-interceptor [:admin])
                     (common-traceability/http-with-correlation-id diplomat.http-server.menu/create-menu!)]
              :route-name :create-menu]

             ["/api/menus/:menu-id"
              :delete [io.interceptors.authentication/identity-interceptor
                       (io.interceptors.authentication/required-roles-interceptor [:admin])
                       interceptors.menu/menu-resource-existence-interceptor-check
                       (common-traceability/http-with-correlation-id diplomat.http-server.menu/retract-menu!)]
              :route-name :retract-menu]

             ["/api/menus/:menu-id"
              :get [(common-traceability/http-with-correlation-id diplomat.http-server.menu/fetch-one)]
              :route-name :fetch-one-menu]

             ["/api/menus"
              :get [(common-traceability/http-with-correlation-id diplomat.http-server.menu/fetch-all)]
              :route-name :fetch-all-menus]

             ["/api/reservations"
              :post [(common-traceability/http-with-correlation-id diplomat.http-server.reservation/create-reservation!)]
              :route-name :create-reservation]

             ["/api/reservation-by-student-and-menu"
              :get [(common-traceability/http-with-correlation-id diplomat.http-server.reservation/fetch-student-reservation-by-menu)]
              :route-name :fetch-reservation-by-student-and-menu]

             ["/api/reservations/:reservation-id"
              :delete [interceptors.reservation/reservation-resource-existence-interceptor-check
                       (common-traceability/http-with-correlation-id diplomat.http-server.reservation/retract-reservation!)]
              :route-name :retract-reservation]

             ["/api/reservations/:reservation-id"
              :get [interceptors.reservation/reservation-resource-existence-interceptor-check
                    (common-traceability/http-with-correlation-id diplomat.http-server.reservation/fetch-reservation)]
              :route-name :fetch-one-reservation]

             ["/api/reservations-by-menu/:menu-id"
              :get [interceptors.menu/menu-resource-existence-interceptor-check
                    (common-traceability/http-with-correlation-id diplomat.http-server.reservation/fetch-reservations-by-menu)]
              :route-name :fetch-reservations-by-menu]])
