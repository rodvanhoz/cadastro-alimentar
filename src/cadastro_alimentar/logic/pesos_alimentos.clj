(ns cadastro-alimentar.logic.pesos-alimentos
  (:require [cadastro-alimentar.utils.uuids :refer [uuid uuid-from-string]]))

(defn build-pesos-alimentos
  [alimentos refeicao-uuid]
  (doall
    (map #(-> {}
              (assoc :peso (:peso %))
              (assoc :alimento_uuid (uuid-from-string (:uuid %)))
              (assoc :refeicao_uuid (uuid-from-string refeicao-uuid)))
      alimentos)))
    