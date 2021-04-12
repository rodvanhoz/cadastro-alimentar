(ns cadastro-alimentar.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [cadastro-alimentar.controller.alimentos :as controller.alimentos]
            [cadastro-alimentar.controller.refeicoes :as controller.refeicoes]
            [cadastro-alimentar.controller.tipos-alimentos :as controller.tipos-alimentos]
            [ring.util.http-response :refer [ok bad-request unauthorized]]))

(defn home
  [_]
  {:status 200
    :body {:status "ok"}})

(defroutes app-routes
  (GET "/" [] home)
  (context "/api" []
    (context "/alimentos" []
      (GET "/" [] (controller.alimentos/get-all))
      (GET "/:uuid" [uuid] (controller.alimentos/by-uuid uuid)))
    
    (context "/refeicoes" []
      (GET "/" [] (controller.refeicoes/get-all))
      (GET "/:uuid" [uuid] (controller.refeicoes/by-uuid uuid))
      (GET "/completas/:date" [date] (controller.refeicoes/get-all-refeicoes-by-date-with-calculated-macros date)))
    
    (context "/tipos_alimentos" []
      (GET "/" [] (controller.tipos-alimentos/get-all))
      (GET "/:uuid" [uuid] (controller.tipos-alimentos/by-uuid uuid))))
  (route/not-found "Not Found"))

(defn wrap-bad-request
  [handler]
  (fn [request]
      (try (handler request)
        (catch Exception e
        (bad-request)))))

(def app
  (-> app-routes
      (wrap-bad-request)
      wrap-json-response
      wrap-json-body))
