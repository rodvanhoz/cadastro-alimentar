(ns cadastro-alimentar.controller.pesos-alimentos
  (:require [clojure.tools.logging :as log]
            [cadastro-alimentar.db.pesos-alimentos :as db.pesos-alimentos]
            [cadastro-alimentar.logic.pesos-alimentos :as logic.pesos-alimentos]))

(defn by-refeicao
  [refeicao-uuid]
  (let [result  (db.pesos-alimentos/by-refeicao refeicao-uuid)]
    (if (= (count result) 0)
      ()
      (do
        (log/info "Encontrado" (count result) "registro(s) com refeicao-uuid:" refeicao-uuid)
        result))))


(defn create-peso-alimento
  [peso-alimento]
  (let [result (list (db.pesos-alimentos/insert! peso-alimento))]
    (if (> (count result) 0)
      result
      (throw (Exception. (str "INSERT - Erro ao criar peso-alimento: " result))))))
      
(defn create-pesos-alimentos-by-list
  [pesos-alimentos]
  (doall
    (map #(create-peso-alimento %)
      pesos-alimentos))
  (let [result (by-refeicao (reduce :refeicao_uuid pesos-alimentos))]
    (if (= (count result) (count pesos-alimentos))
      (do
        (log/info "Inserido total de refeicoes: " (count result))
        result)
      (throw (Exception. (str "INSERT - Erro ao criar os pesos-alimentos: " pesos-alimentos))))))