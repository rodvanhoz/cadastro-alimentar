(ns cadastro-alimentar.db.refeicoes
  (:require [korma.db :refer :all]
            [korma.core :refer :all]
            [clojure.java.jdbc :as sql]
            [clojure.tools.logging :as log]
            [cadastro-alimentar.db :as db]
            [cadastro-alimentar.db.entities :as e]))


(defn get
  [clauses]
  (select e/refeicoes
    (where clauses)))

(defn get-all []
  (get true))

(defn by-id
  [id]
  (get {:refeicoes.id_refeicao (Integer/parseInt id)}))

(defn get-all-refeicoes-by-date
  [date]
  (select [e/refeicoes :refeicoes]
    ;;(fields [:refeicoes.id_refeicao :id-refeicao] [:refeicoes.moment :moment])
  
    (join :inner [e/peso-alimentos :peso-alimentos] (= :peso-alimentos.id_refeicao :refeicoes.id_refeicao))
    (join :inner [e/alimentos :alimentos] (= :peso-alimentos.id_alimento :alimentos.id_alimento))
    (join :inner [e/tipos-alimento :tipos-alimento] (= :alimentos.id_tipo_alimento :tipos-alimento.id_tipo_alimento))))