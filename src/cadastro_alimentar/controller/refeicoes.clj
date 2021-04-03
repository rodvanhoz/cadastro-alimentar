(ns cadastro-alimentar.controller.refeicoes
  (:require [clojure.tools.logging :as log]
            [ring.util.http-response :refer [ok bad-request unauthorized internal-server-error no-content created not-found]]
            [cadastro-alimentar.db.refeicoes :as db.refeicoes]
            [cadastro-alimentar.db :as db]))

(defn get-all
  []
  (let [result  (db.refeicoes/get-all)]
    (if (= (count result) 0)
      (not-found)
    (do
      (log/info "Encontrado" (count result) "registro(s)")
      result))))

(defn by-id
  [id]
  (let [result  (db.refeicoes/by-id id)]
    (if (= (count result) 0)
      (not-found)
      (do
        (log/info "Encontrado" (count result) "registro(s) com id:" id)
        result))))
  