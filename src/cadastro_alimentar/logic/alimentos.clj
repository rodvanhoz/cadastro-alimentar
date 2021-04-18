(ns cadastro-alimentar.logic.alimentos
  (:require [cadastro-alimentar.utils.uuids :refer [uuid uuid-from-string]]
            [cadastro-alimentar.utils.dates :refer [now]]))

(defn build-alimento
  [alimento]
  (-> {}
      (assoc :uuid (if (nil? (:uuid alimento)) (uuid) (uuid-from-string (:uuid alimento))))
      (assoc :nome (if (nil? (:nome alimento)) (throw (Exception. (str "campo -nome- nao pode seu nulo"))) (:nome alimento)))
      (assoc :peso (if (nil? (:peso alimento)) (throw (Exception. (str "campo -peso- nao pode seu nulo"))) (:peso alimento)))
      (assoc :qtde-carboidrato (if (nil? (:qtde-carboidrato alimento)) (throw (Exception. (str "campo -qtde-carboidrato- nao pode seu nulo"))) (:qtde-carboidrato alimento)))
      (assoc :qtde-gorduras (if (nil? (:qtde-gorduras alimento)) (throw (Exception. (str "campo -qtde-gorduras- nao pode seu nulo"))) (:qtde-gorduras alimento)))
      (assoc :qtde-proteinas (if (nil? (:qtde-proteinas alimento)) (throw (Exception. (str "campo -qtde-proteinas- nao pode seu nulo"))) (:qtde-proteinas alimento)))
      (assoc :tipo-alimento-uuid (if (nil? (:tipo-alimento-uuid alimento)) (throw (Exception. (str "campo -peso- nao pode seu nulo"))) (:tipo-alimento-uuid alimento)))))

(defn build-where-clauses
  [alimento]
  (-> {}
      (cond-> (not (nil? (:uuid alimento))) (assoc :uuid (uuid-from-string (:uuid alimento))))
      (cond-> (not (nil? (:nome alimento))) (assoc :nome (:nome alimento)))
      (cond-> (not (nil? (:peso alimento))) (assoc :peso (:peso alimento)))
      (cond-> (not (nil? (:qtde-carboidrato alimento))) (assoc :qtde_carboidrato (:qtde-carboidrato alimento)))
      (cond-> (not (nil? (:qtde-gorduras alimento))) (assoc :qtde_gorduras (:qtde-gorduras alimento)))
      (cond-> (not (nil? (:qtde-proteinas alimento))) (assoc :qtde_proteinas (:qtde-proteinas alimento)))
      (cond-> (not (nil? (:tipo-alimento-uuid alimento))) (assoc :tipo_alimento_uuid (:tipo-alimento-uuid alimento)))))

(defn build-fields-clauses
  [alimento]
  (-> {}
    (cond-> (not (nil? (:uuid alimento))) (assoc :uuid (uuid-from-string (:uuid alimento))))
    (cond-> (not (nil? (:nome alimento))) (assoc :nome (:nome alimento)))
    (cond-> (not (nil? (:peso alimento))) (assoc :peso (:peso alimento)))
    (cond-> (not (nil? (:qtde-carboidrato alimento))) (assoc :qtde_carboidrato (:qtde-carboidrato alimento)))
    (cond-> (not (nil? (:qtde-gorduras alimento))) (assoc :qtde_gorduras (:qtde-gorduras alimento)))
    (cond-> (not (nil? (:qtde-proteinas alimento))) (assoc :qtde_proteinas (:qtde-proteinas alimento)))
    (cond-> (not (nil? (:tipo-alimento-uuid alimento))) (assoc :tipo_alimento_uuid (uuid-from-string (:tipo-alimento-uuid alimento))))))
