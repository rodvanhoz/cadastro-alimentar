(ns cadastro-alimentar.controller.tipos-alimentos
  (:require [clojure.tools.logging :as log]
            [ring.util.http-response :refer [ok bad-request unauthorized internal-server-error no-content created not-found conflict]]
            [cadastro-alimentar.db.tipos-alimentos :as db.tipos-alimentos]
            [cadastro-alimentar.db :as db]
            [cadastro-alimentar.logic.tipos-alimentos :as logic.tipos-alimentos]))

(defn get-all
  []
  (let [result  (db.tipos-alimentos/get-all)]
    (if (= (count result) 0)
      nil
    (do
      (log/info "Encontrado" (count result) "registro(s)")
      result))))

(defn by-uuid
  [uuid]
  (let [result  (db.tipos-alimentos/by-uuid uuid)]
    (if (= (count result) 0)
      nil
      (do
        (log/info "Encontrado" (count result) "registro(s) com uuid:" uuid)
        result))))

(defn by-descricao
  [descricao]
  (let [result  (db.tipos-alimentos/by-descricao descricao)]
    (if (= (count result) 0)
      nil
      (do
        (log/info "Encontrado" (count result) "registro(s) com descricao:" descricao)
        result))))

(defn create-tipo-alimento
  [descricao]
  (let [tipo-alimento (logic.tipos-alimentos/build-tipo-alimento descricao)]
    (if (= (count (by-descricao descricao)) 0)
      (do
        (db.tipos-alimentos/insert!  tipo-alimento)
        (log/info "tipo-alimento criado: " (:uuid tipo-alimento)))
      (do
        (throw (Exception. (str "tipo-alimento ja existe: " descricao)))
        (conflict)))))

(defn update-tipo-alimento
  [tipo-alimento]
  (let [where-clauses (logic.tipos-alimentos/create-clauses (-> {} (assoc :uuid (:uuid tipo-alimento))))
        field-clauses (logic.tipos-alimentos/build-fields-clauses tipo-alimento)
        tp (by-uuid (:uuid tipo-alimento))]
    (if (= (count tp) 1)
      (do 
        (db.tipos-alimentos/update! field-clauses where-clauses)
        (log/info "tipo-alimento atualizado com sucesso: " (first tp)))
      (do
        (throw (Exception. (str "UPDATE - tipo-alimento nao encontrado: " (:descricao tipo-alimento))))))))

(defn delete-by-descricao
  [descricao]
  (if (= (count (by-descricao descricao)) 0)
    (throw (Exception. (str "tipo-alimento nao encontrado: " descricao)))
    (do
      (db.tipos-alimentos/delete-by-descricao descricao)
      (log/info "DELETE - tipo-alimento deletado: " descricao)
      true)))