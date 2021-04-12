(ns cadastro-alimentar.logic.tipos-alimentos
  (:require [cadastro-alimentar.utils.uuids :refer [uuid uuid-from-string]]))

(defn build-tipo-alimento
  [descricao]
  (-> {}
      (assoc :uuid (uuid))
      (assoc :descricao descricao)))