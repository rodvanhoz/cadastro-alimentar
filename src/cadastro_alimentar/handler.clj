(ns cadastro-alimentar.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [clojure.tools.logging :as log]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [cadastro-alimentar.utils.adapters :as utils.adapters]
            [cadastro-alimentar.controller.alimentos :as controller.alimentos]
            [cadastro-alimentar.controller.refeicoes :as controller.refeicoes]
            [cadastro-alimentar.controller.tipos-alimentos :as controller.tipos-alimentos]
            [ring.util.http-response :refer [ok bad-request unauthorized not-found created no-content conflict]]))

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

(defn alimentos-insert
  [body]
  (try 
    (let [result (controller.alimentos/create-alimento body)]
      (if (= (count result) 1)
        result
        (bad-request)))
    (catch Exception e (conflict))))

(defn alimentos-update
  [uuid body]
  (try
    (let [result (controller.alimentos/update-alimento uuid body)]
      (if (= (count result) 1)
        result
        (no-content)))
    (catch Exception e (no-content))))

(defn alimentos-delete-by-uuid
  [uuid]
  (try 
    (if (controller.alimentos/delete-by-uuid uuid)
      (ok)
      (not-found))
    (catch Exception e (not-found))))

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
  (try 
    (let [result (controller.tipos-alimentos/create-tipo-alimento body)]
      (if (= (count result) 1)
        result
        (bad-request)))
    (catch Exception e (conflict))))

(defn tipos-alimentos-update
  [uuid body]
  (try
    (let [result (controller.tipos-alimentos/update-tipo-alimento uuid body)]
      (if (= (count result) 1)
        result
        (no-content)))
    (catch Exception e (no-content))))

(defn tipos-alimentos-delete-by-uuid
  [uuid]
  (try 
    (if (controller.tipos-alimentos/delete-by-uuid uuid)
      (ok)
      (not-found))
    (catch Exception e (not-found))))

(defroutes app-routes
  (GET "/" [] home)
  (context "/api" []
    (context "/alimentos" []
      (GET "/" [] (alimentos-get-all))
      (GET "/:uuid" [uuid] (alimentos-get-by-uuid uuid))
      (POST "/" {:keys [body]} (alimentos-insert body))
      (PUT "/:uuid" {{:keys [uuid]} :params body :body} (alimentos-update uuid body))
      (DELETE "/:uuid" [uuid] (alimentos-delete-by-uuid uuid)))
    
    (context "/refeicoes" []
      (GET "/" [] (refeicoes-get-all))
      (GET "/:uuid" [uuid] (refeicoes-get-by-uuid uuid))
      (GET "/completas/:date" [date] (refeicoes-get-all-refeicoes-by-date-with-calculated-macros date)))
    
    (context "/tipos_alimentos" []
      (GET "/" [] (tipos-alimentos-get-all))
      (GET "/:uuid" [uuid] (tipos-alimentos-get-by-uuid uuid))
      (POST "/" {:keys [body]} (tipos-alimentos-insert body))
      (PUT "/:uuid" {{:keys [uuid]} :params body :body} (tipos-alimentos-update uuid body))
      (DELETE "/:uuid" [uuid] (tipos-alimentos-delete-by-uuid uuid))))
  (route/not-found "Not Found"))

(defn log-request
  [handler]
  (fn [request]
    (log/info "[REQUEST] " (get-in request []))
    (handler request)))
  
(defn wrap-exception
  [handler]
  (fn [request]
    (try (handler request)
          (catch Exception e
            (do
              (log/error (.getMessage e))
              {:status 400
               :body (.getMessage e)})))))
            

(def app
  (-> app-routes
      (wrap-exception)
      (wrap-json-response)
      (wrap-json-body {:keywords? true})
      (log-request)))
