(ns cadastro-alimentar.db.refeicoes
  (:require [korma.db :refer :all]
            [korma.core :refer :all]
            [clojure.java.jdbc :as sql]
            [clojure.tools.logging :as log]
            [cadastro-alimentar.db :as db]
            [cadastro-alimentar.db.entities :as e]
            [cadastro-alimentar.utils.dates :as utils.dates]))


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
  (let [start-date (utils.dates/str->sql-date-start-of-day date)
        final-date (utils.dates/str->sql-date-end-of-day date)]
    (select [e/refeicoes :refeicoes]
      (fields [:refeicoes.id_refeicao :id-refeicao] [:refeicoes.moment :moment] [:peso-alimentos.id_peso_alimento :id-peso-alimento] [:peso-alimentos.peso :peso] 
              [:alimentos.id_alimento :id_alimento] [:alimentos.nome :nome] [:alimentos.peso :peso-unitario] [:alimentos.qtde_carboidrato :qtde-carbo] [:alimentos.qtde_proteinas :qtde-prot]
              [:alimentos.qtde_gorduras :qtde-gordura] [:tipos.descricao :tipos-descricao])
    
      (join :inner [e/peso-alimentos :peso-alimentos] (= :peso-alimentos.id_refeicao :refeicoes.id_refeicao))
      (join :inner [e/alimentos :alimentos] (= :peso-alimentos.id_alimento :alimentos.id_alimento))
      (join :inner [e/tipos-alimento :tipos] (= :alimentos.id_tipo_alimento :tipos.id_tipo_alimento))
      
      (where (-> {}
                (cond-> date (assoc :refeicoes.moment [between [start-date final-date]])))))))
