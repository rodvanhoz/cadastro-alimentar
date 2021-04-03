(ns cadastro-alimentar.controller.alimentos
  (:require [clojure.tools.logging :as log]
            [ring.util.http-response :refer [ok bad-request unauthorized internal-server-error no-content created not-found]]
            [cadastro-alimentar.db.alimentos :as db.alimentos]
            [cadastro-alimentar.db :as db]))

(defn get-all
  []
  (let [result  (db.alimentos/get-all)]
    (if (= (count result) 0)
      (not-found)
    (do
      (log/info "Encontrado" (count result) "registro(s)")
      result))))

(defn by-id
  [id]
  (let [result  (db.alimentos/by-id id)]
    (if (= (count result) 0)
      (not-found)
      (do
        (log/info "Encontrado" (count result) "registro(s) com id:" id)
        result))))
  