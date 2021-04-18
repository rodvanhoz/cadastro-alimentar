(ns cadastro-alimentar.logic.refeicoes
  (:require [cadastro-alimentar.utils.uuids :refer [uuid uuid-from-string]]
            [cadastro-alimentar.utils.dates :refer [now str->date]]))

(defn build-refeicao
  [refeicao]
  (-> {}
      (assoc :uuid (if (nil? (:uuid refeicao)) (uuid) (uuid-from-string (:uuid refeicao))))
      (assoc :moment (if (nil? (:moment refeicao)) (now) (str->date (:moment refeicao))))
      (assoc :descricao (if (nil? (:descricao refeicao)) nil (:descricao refeicao)))))

(defn build-where-clauses
  [refeicao]
  (-> {}
      (cond-> (not (nil? (:uuid refeicao))) (assoc :uuid (uuid-from-string (:uuid refeicao))))
      (cond-> (not (nil? (:moment refeicao))) (assoc :moment (str->date (:moment refeicao))))
      (cond-> (not (nil? (:descricao refeicao))) (assoc :descricao (:descricao refeicao)))))

(defn build-fields-clauses
  [refeicao]
  (-> {}
      (cond-> (not (nil? (:moment refeicao))) (assoc :moment (str->date (:moment refeicao))))
      (cond-> (not (nil? (:descricao refeicao))) (assoc :descricao (:descricao refeicao)))))