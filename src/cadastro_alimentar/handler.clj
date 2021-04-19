(ns cadastro-alimentar.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [cadastro-alimentar.utils.adapters :as utils.adapters]
            [cadastro-alimentar.controller.alimentos :as controller.alimentos]
            [cadastro-alimentar.controller.refeicoes :as controller.refeicoes]
            [cadastro-alimentar.controller.tipos-alimentos :as controller.tipos-alimentos]
            [ring.util.http-response :refer [ok bad-request unauthorized not-found created]]))

(defn home
  [_]
  {:status 200
    :body {:status "ok"}})

(defn alimentos-get-all
  []
  (let [result (controller.alimentos/get-all)]
    (if (= (count result) 0)
      (not-found)
      result)))

(defn alimentos-get-by-uuid
  [alimento-uuid]
  (let [result (controller.alimentos/by-uuid alimento-uuid)]
    (if (= (count result) 0)
      (not-found)
      result)))

(defn refeicoes-get-all
  []
  (let [result (controller.refeicoes/get-all)]
    (if (= (count result) 0)
      (not-found)
      result)))

(defn refeicoes-get-by-uuid
  [refeicao-uuid]
  (let [result (controller.refeicoes/by-uuid refeicao-uuid)]
    (if (= (count result) 0)
      (not-found)
      result)))

(defn refeicoes-get-all-refeicoes-by-date-with-calculated-macros
  [refeicao-date]
  (let [result (controller.refeicoes/get-all-refeicoes-by-date-with-calculated-macros refeicao-date)]
    (if (= (count result) 0)
      (not-found)
      result)))

(defn tipos-alimentos-get-all
  []
  (let [result (controller.tipos-alimentos/get-all)]
    (if (= (count result) 0)
      (not-found)
      result)))  
      
(defn tipos-alimentos-get-by-uuid
  [tipo-alimento-uuid]
  (let [result (controller.tipos-alimentos/by-uuid tipo-alimento-uuid)]
    (if (= (count result) 0)
      (not-found)
      result)))

(defn tipos-alimentos-insert
  [body]
  (let [result (controller.tipos-alimentos/create-tipo-alimento body)]
    (if (= (count result) 1)
      (created)
      (bad-request))))

(defroutes app-routes
  (GET "/" [] home)
  (context "/api" []
    (context "/alimentos" []
      (GET "/" [] (alimentos-get-all))
      (GET "/:uuid" [uuid] (alimentos-get-by-uuid uuid)))
    
    (context "/refeicoes" []
      (GET "/" [] (refeicoes-get-all))
      (GET "/:uuid" [uuid] (refeicoes-get-by-uuid uuid))
      (GET "/completas/:date" [date] (refeicoes-get-all-refeicoes-by-date-with-calculated-macros date)))
    
    (context "/tipos_alimentos" []
      (GET "/" [] (tipos-alimentos-get-all))
      (GET "/:uuid" [uuid] (tipos-alimentos-get-by-uuid uuid))
      (POST "/" {:keys [body]} (tipos-alimentos-insert body))))
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
      (wrap-json-response)
      (wrap-json-body {:keywords? true})))
