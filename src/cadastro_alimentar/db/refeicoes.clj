(ns cadastro-alimentar.db.refeicoes
  (:require [korma.db :refer :all]
            [korma.core :refer :all]
            [clojure.java.jdbc :as sql]
            [clojure.tools.logging :as log]
            [cadastro-alimentar.db :as db]
            [cadastro-alimentar.db.entities :as e]
            [cadastro-alimentar.utils.dates :as utils.dates]
            [cadastro-alimentar.utils.uuids :as utils.uuids]))


(defn get
  [clauses]
  (select e/refeicoes
    (where clauses)))

(defn get-all []
  (get true))

(defn by-uuid
  [uuid]
  (get {:refeicoes.uuid (utils.uuids/uuid-from-string uuid)}))

(defn by-descricao
  [descricao]
  (get {:refeicoes.descricao descricao}))

(defn insert!
  [refeicao]
  (insert e/refeicoes
          (values refeicao)))

(defn update!
  [fields clauses]
  (update e/refeicoes
          (set-fields (-> fields))
          (where clauses)))

(defn delete-by-uuid
  [uuid]
  (delete e/refeicoes
    (where {:uuid (utils.uuids/uuid-from-string uuid)})))

(defn delete-by-decricao
  [descricao]
  (delete e/refeicoes
    (where {:refeicoes.descricao descricao})))
  
(defn get-all-refeicoes-by-date
  [date]
  (let [start-date (utils.dates/str->sql-date-start-of-day date)
        final-date (utils.dates/str->sql-date-end-of-day date)]
    (select [e/refeicoes :refeicoes]
      (fields [:refeicoes.uuid :refeicao-uuid] [:refeicoes.moment :moment] [:refeicoes.descricao :refeicaoes-descricao] [:pesos-alimentos.peso :peso] 
              [:alimentos.uuid :alimento-uuid] [:alimentos.nome :nome] [:alimentos.peso :peso-unitario] [:alimentos.qtde_carboidrato :qtde-carbo] [:alimentos.qtde_proteinas :qtde-prot]
              [:alimentos.qtde_gorduras :qtde-gordura] [:tipos.descricao :tipos-descricao])
    
      (join :inner [e/pesos-alimentos :pesos-alimentos] (= :pesos-alimentos.refeicao_uuid :refeicoes.uuid))
      (join :inner [e/alimentos :alimentos] (= :pesos-alimentos.alimento_uuid :alimentos.uuid))
      (join :inner [e/tipos-alimentos :tipos] (= :alimentos.tipo_alimento_uuid :tipos.uuid))
      
      (where (-> {}
                (cond-> date (assoc :refeicoes.moment [between [start-date final-date]])))))))
    