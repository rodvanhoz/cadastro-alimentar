(ns cadastro-alimentar.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [cadastro-alimentar.controller.alimentos :as controller.alimentos]
            [cadastro-alimentar.controller.refeicoes :as controller.refeicoes]
            [cadastro-alimentar.controller.tipos-alimento :as controller.tipos-alimento]
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
      (GET "/:id" [id] (controller.alimentos/by-id id)))
    
    (context "/refeicoes" []
      (GET "/" [] (controller.refeicoes/get-all))
      (GET "/:id" [id] (controller.refeicoes/by-id id))
      (GET "/completas/:date" [date] (controller.refeicoes/get-all-refeicoes-by-date date)))
    
    (context "/tipos_alimento" []
      (GET "/" [] (controller.tipos-alimento/get-all))
      (GET "/:id" [id] (controller.tipos-alimento/by-id id))))
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
