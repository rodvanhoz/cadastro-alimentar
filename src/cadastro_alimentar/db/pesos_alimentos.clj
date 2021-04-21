(ns cadastro-alimentar.db.pesos-alimentos
  (:require [korma.db :refer :all]
            [korma.core :refer :all]
            [clojure.java.jdbc :as sql]
            [clojure.tools.logging :as log]
            [cadastro-alimentar.db :as db]
            [cadastro-alimentar.db.entities :as e]
            [cadastro-alimentar.utils.uuids :as utils.uuids]))
  

(defn get
  [clauses]
  (select e/pesos-alimentos
    (where clauses)))

(defn get-all []
  (get true))

(defn by-refeicao
  [refeicao-uuid]
  (get {:pesos_alimentos.refeicao_uuid (utils.uuids/uuid-from-string refeicao-uuid)}))

(defn insert!
  [peso-alimento]
  (insert e/pesos-alimentos
          (values peso-alimento)))
        
(defn update!
  [fields clauses]
  (update e/pesos-alimentos
          (set-fields (-> fields))
          (where clauses)))

(defn delete-by-refeicao
  [refeicao-uuid]
  (delete e/pesos-alimentos
    (where {:pesos_alimentos.refeicao_uuid (utils.uuids/uuid-from-string refeicao-uuid)})))