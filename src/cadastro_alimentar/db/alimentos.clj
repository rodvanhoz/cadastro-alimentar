(ns cadastro-alimentar.db.alimentos
  (:require [korma.db :refer :all]
            [korma.core :refer :all]
            [clojure.java.jdbc :as sql]
            [clojure.tools.logging :as log]
            [cadastro-alimentar.db :as db]
            [cadastro-alimentar.db.entities :as e]))

(defn get
  [clauses]
  (select e/alimentos
    (fields :id_alimento :nome :peso :qtde_carboidrato :qtde_gorduras :qtde_proteinas :id_tipo_alimento
      [:tipos.descricao :tipos-descricao])

    (join :inner [e/tipos-alimento :tipos] (= :alimentos.id_tipo_alimento :tipos.id_tipo_alimento))
    
    (where clauses)))

(defn get-all []
  (get true))

(defn by-id
  [id]
  (get {:alimentos.id_alimento (Integer/parseInt id)}))