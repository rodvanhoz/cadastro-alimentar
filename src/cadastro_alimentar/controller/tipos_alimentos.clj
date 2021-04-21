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
      ()
    (do
      (log/info "Encontrado" (count result) "registro(s)")
      result))))

(defn by-uuid
  [uuid]
  (let [result  (db.tipos-alimentos/by-uuid uuid)]
    (if (= (count result) 0)
      ()
      (do
        (log/info "Encontrado" (count result) "registro(s) com uuid:" uuid)
        result))))

(defn by-descricao
  [descricao]
  (let [result  (db.tipos-alimentos/by-descricao descricao)]
    (if (= (count result) 0)
      ()
      (do
        (log/info "Encontrado" (count result) "registro(s) com descricao:" descricao)
        result))))

(defn create-tipo-alimento
  [tipo-alimento]
  (let [tipo-alimento-builded (logic.tipos-alimentos/build-tipo-alimento tipo-alimento)]
    (if (= (count (by-descricao (:descricao tipo-alimento-builded))) 0)
      (do
        (db.tipos-alimentos/insert!  tipo-alimento-builded)
        (log/info "tipo-alimento criado: " (:uuid tipo-alimento-builded))
        (-> tipo-alimento-builded
            list))
      (throw (Exception. (str "tipo-alimento ja existe: " (:descricao tipo-alimento-builded)))))))

(defn update-tipo-alimento
  [tipo-alimento-uuid tipo-alimento-fields]
  (let [where-clauses (logic.tipos-alimentos/create-clauses (-> {} (assoc :uuid tipo-alimento-uuid)))
        field-clauses (logic.tipos-alimentos/build-fields-clauses tipo-alimento-fields)
        tp (by-uuid tipo-alimento-uuid)]
    (if (= (count tp) 1)
      (do 
        (db.tipos-alimentos/update! field-clauses where-clauses)
        (log/info "tipo-alimento atualizado com sucesso: " (first tp))
        (by-uuid tipo-alimento-uuid))
      (throw (Exception. (str "UPDATE - tipo-alimento nao encontrado: " (:descricao tipo-alimento-fields)))))))

(defn delete-by-descricao
  [descricao]
  (if (= (count (by-descricao descricao)) 0)
    (throw (Exception. (str "tipo-alimento nao encontrado: " descricao)))
    (do
      (db.tipos-alimentos/delete-by-descricao descricao)
      (log/info "DELETE - tipo-alimento deletado: " descricao)
      true)))

(defn delete-by-uuid
  [uuid]
  (if (= (count (by-uuid uuid)) 0)
    (throw (Exception. (str "tipo-alimento nao encontrado: " uuid)))
    (do
      (db.tipos-alimentos/delete-by-uuid uuid)
      (log/info "DELETE - tipo-alimento deletado: " uuid)
      true)))