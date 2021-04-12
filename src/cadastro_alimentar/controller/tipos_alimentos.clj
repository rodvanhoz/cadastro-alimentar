(ns cadastro-alimentar.controller.tipos-alimentos
  (:require [clojure.tools.logging :as log]
            [ring.util.http-response :refer [ok bad-request unauthorized internal-server-error no-content created not-found]]
            [cadastro-alimentar.db.tipos-alimentos :as db.tipos-alimentos]
            [cadastro-alimentar.db :as db]
            [cadastro-alimentar.logic.tipos-alimentos :as logic.tipos-alimentos]))

(defn get-all
  []
  (let [result  (db.tipos-alimentos/get-all)]
    (if (= (count result) 0)
      (not-found)
    (do
      (log/info "Encontrado" (count result) "registro(s)")
      result))))

(defn by-uuid
  [uuid]
  (let [result  (db.tipos-alimentos/by-uuid uuid)]
    (if (= (count result) 0)
      (not-found)
      (do
        (log/info "Encontrado" (count result) "registro(s) com id:" uuid)
        result))))

(defn create-tipo-alimento
  [descricao]
  (let [tipo-alimento (logic.tipos-alimentos/build-tipo-alimento descricao)]
    (prn "########" tipo-alimento)))