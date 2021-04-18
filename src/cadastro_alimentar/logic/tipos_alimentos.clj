(ns cadastro-alimentar.logic.tipos-alimentos
  (:require [cadastro-alimentar.utils.uuids :refer [uuid uuid-from-string]]))

(defn build-tipo-alimento
  [descricao]
  (-> {}
      (assoc :uuid (uuid))
      (assoc :descricao descricao)))

(defn create-clauses
  [tipo-alimento]
  (-> {}
      (cond-> (not (nil? (:uuid tipo-alimento))) (assoc :uuid (:uuid tipo-alimento)))
      (cond-> (not (nil? (:descricao tipo-alimento))) (assoc :descricao (:descricao tipo-alimento)))))

(defn build-fields-clauses
  [tipo-alimento]
  (-> {}
      (cond-> (not (nil? (:descricao tipo-alimento))) (assoc :descricao (:descricao tipo-alimento)))))