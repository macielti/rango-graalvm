(ns rango-graalvm.diplomat.http-server
  (:require [common-clj.integrant-components.prometheus :as component.prometheus]
            [common-clj.traceability.core :as common-traceability]
            [porteiro-component.interceptors.authentication :as io.interceptors.authentication]
            [rango-graalvm.diplomat.http-server.menu :as diplomat.http-server.menu]
            [rango-graalvm.diplomat.http-server.reservation :as diplomat.http-server.reservation]
            [rango-graalvm.diplomat.http-server.student :as diplomat.http-server.student]
            [rango-graalvm.interceptors.menu :as interceptors.menu]
            [rango-graalvm.interceptors.reservation :as interceptors.reservation]
            [rango-graalvm.interceptors.student :as interceptors.student]
            [service-component.interceptors :as service.interceptors]))

(def routes [["/api/students"
              :post [service.interceptors/http-request-in-handle-timing-interceptor
                     io.interceptors.authentication/identity-interceptor
                     (io.interceptors.authentication/required-roles-interceptor [:admin])
                     (common-traceability/http-with-correlation-id diplomat.http-server.student/create-student!)]
              :route-name :create-student]

             ["/api/students"
              :get [service.interceptors/http-request-in-handle-timing-interceptor
                    io.interceptors.authentication/identity-interceptor
                    (io.interceptors.authentication/required-roles-interceptor [:admin])
                    (common-traceability/http-with-correlation-id diplomat.http-server.student/fetch-all)]
              :route-name :fetch-all-students]

             ["/api/students/:student-id"
              :delete [service.interceptors/http-request-in-handle-timing-interceptor
                       io.interceptors.authentication/identity-interceptor
                       (io.interceptors.authentication/required-roles-interceptor [:admin])
                       interceptors.student/student-resource-existence-interceptor-check
                       (common-traceability/http-with-correlation-id diplomat.http-server.student/retract-student!)]
              :route-name :retract-student]

             ["/api/students-by-menu-reservations/:menu-id"
              :get [service.interceptors/http-request-in-handle-timing-interceptor
                    io.interceptors.authentication/identity-interceptor
                    (io.interceptors.authentication/required-roles-interceptor [:admin])
                    interceptors.menu/menu-resource-existence-interceptor-check
                    (common-traceability/http-with-correlation-id diplomat.http-server.student/fetch-students-by-reservations-menu)]
              :route-name :fetch-students-by-menu-reservations]

             ["/api/menus"
              :post [service.interceptors/http-request-in-handle-timing-interceptor
                     io.interceptors.authentication/identity-interceptor
                     (io.interceptors.authentication/required-roles-interceptor [:admin])
                     (common-traceability/http-with-correlation-id diplomat.http-server.menu/create-menu!)]
              :route-name :create-menu]

             ["/api/menus/:menu-id"
              :delete [service.interceptors/http-request-in-handle-timing-interceptor
                       io.interceptors.authentication/identity-interceptor
                       (io.interceptors.authentication/required-roles-interceptor [:admin])
                       interceptors.menu/menu-resource-existence-interceptor-check
                       (common-traceability/http-with-correlation-id diplomat.http-server.menu/retract-menu!)]
              :route-name :retract-menu]

             ["/api/menus/:menu-id"
              :get [service.interceptors/http-request-in-handle-timing-interceptor
                    (common-traceability/http-with-correlation-id diplomat.http-server.menu/fetch-one)]
              :route-name :fetch-one-menu]

             ["/api/menus"
              :get [service.interceptors/http-request-in-handle-timing-interceptor
                    (common-traceability/http-with-correlation-id diplomat.http-server.menu/fetch-all)]
              :route-name :fetch-all-menus]

             ["/api/reservations"
              :post [service.interceptors/http-request-in-handle-timing-interceptor
                     (common-traceability/http-with-correlation-id diplomat.http-server.reservation/create-reservation!)]
              :route-name :create-reservation]

             ["/api/reservation-by-student-and-menu"
              :get [service.interceptors/http-request-in-handle-timing-interceptor
                    (common-traceability/http-with-correlation-id diplomat.http-server.reservation/fetch-student-reservation-by-menu)]
              :route-name :fetch-reservation-by-student-and-menu]

             ["/api/reservations/:reservation-id"
              :delete [service.interceptors/http-request-in-handle-timing-interceptor
                       interceptors.reservation/reservation-resource-existence-interceptor-check
                       (common-traceability/http-with-correlation-id diplomat.http-server.reservation/retract-reservation!)]
              :route-name :retract-reservation]

             ["/api/reservations/:reservation-id"
              :get [service.interceptors/http-request-in-handle-timing-interceptor
                    interceptors.reservation/reservation-resource-existence-interceptor-check
                    (common-traceability/http-with-correlation-id diplomat.http-server.reservation/fetch-reservation)]
              :route-name :fetch-one-reservation]

             ["/api/reservations-by-menu/:menu-id"
              :get [service.interceptors/http-request-in-handle-timing-interceptor
                    interceptors.menu/menu-resource-existence-interceptor-check
                    (common-traceability/http-with-correlation-id diplomat.http-server.reservation/fetch-reservations-by-menu)]
              :route-name :fetch-reservations-by-menu]

             ["/metrics" :get [service.interceptors/http-request-in-handle-timing-interceptor
                               component.prometheus/expose-metrics-http-request-handler] :route-name :fetch-prometheus-metrics]])
