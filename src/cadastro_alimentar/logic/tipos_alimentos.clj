(ns cadastro-alimentar.logic.tipos-alimentos
  (:require [cadastro-alimentar.utils.uuids :refer [uuid uuid-from-string]]))

(defn build-tipo-alimento
  [tipo-alimento]
  (-> {}
      (assoc :uuid (if (nil? (:uuid tipo-alimento)) (uuid) (uuid-from-string (:uuid tipo-alimento))))
      (assoc :descricao (if (nil? (:descricao tipo-alimento)) (throw (Exception. (str "campo -descricao- nao pode seu nulo"))) (:descricao tipo-alimento)))))

(defn create-clauses
  [tipo-alimento]
  (-> {}
      (cond-> (not (nil? (:uuid tipo-alimento))) (assoc :uuid (uuid-from-string (:uuid tipo-alimento))))
      (cond-> (not (nil? (:descricao tipo-alimento))) (assoc :descricao (:descricao tipo-alimento)))))

(defn build-fields-clauses
  [tipo-alimento]
  (-> {}
      (cond-> (not (nil? (:descricao tipo-alimento))) (assoc :descricao (:descricao tipo-alimento)))))