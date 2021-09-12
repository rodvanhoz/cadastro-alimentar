(ns cadastro-alimentar.controller.alimentos
  (:require [clojure.tools.logging :as log]
            [ring.util.http-response :refer [ok bad-request unauthorized internal-server-error no-content created not-found]]
            [cadastro-alimentar.db.alimentos :as db.alimentos]
            [cadastro-alimentar.logic.alimentos :as logic.alimentos]
            [cadastro-alimentar.db :as db]))

(defn get-all
  []
  (let [result  (db.alimentos/get-all)]
    (if (= (count result) 0)
      ()
    (do
      (log/info "Encontrado" (count result) "registro(s)")
      result))))

(defn by-uuid
  [uuid]
  (let [result (db.alimentos/by-uuid uuid)]
    (if (= (count result) 0)
      ()
      (do
        (log/info "Encontrado" (count result) "registro(s) com uuid: " uuid)
        result))))
  
(defn create-alimento
  [alimento]
  (let [alimento-builder (logic.alimentos/build-alimento alimento)]
    (if (= (count (by-uuid (:uuid alimento-builder))) 0)
      (do
        (db.alimentos/insert! (logic.alimentos/build-fields-clauses alimento-builder))
        (log/info "alimento criado: " alimento-builder)
        (-> alimento-builder
            list))
      (throw (Exception. (str "alimento ja existe: " (:uuid alimento-builder)))))))

(defn update-alimento
  [alimento-uuid alimento-fields]
  (let [where-clauses (logic.alimentos/build-where-clauses {:uuid alimento-uuid})
        field-clauses (logic.alimentos/build-fields-clauses alimento-fields)
        alim (by-uuid alimento-uuid)]
    (if (= (count alim) 1)
      (do 
        (db.alimentos/update! field-clauses where-clauses)
        (log/info "alimento atualizado com sucesso: " (by-uuid alimento-uuid))
        (by-uuid alimento-uuid))
      (do
        (throw (Exception. (str "UPDATE - alimento nao encontrado: " alimento-uuid)))))))

(defn delete-by-uuid
  [alimento-uuid]
  (if (= (count (by-uuid alimento-uuid)) 0)
    (throw (Exception. (str "alimento nao encontrado: " alimento-uuid)))
    (do
      (db.alimentos/delete-by-uuid alimento-uuid)
      (log/info "DELETE - alimento deletado: " alimento-uuid)
      true)))