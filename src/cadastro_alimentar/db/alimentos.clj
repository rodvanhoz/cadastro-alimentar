(ns cadastro-alimentar.db.alimentos
  (:require [korma.db :refer :all]
            [korma.core :refer :all]
            [clojure.java.jdbc :as sql]
            [clojure.tools.logging :as log]
            [cadastro-alimentar.db :as db]
            [cadastro-alimentar.db.entities :as e]
            [cadastro-alimentar.utils.uuids :as utils.uuids]))

(defn get
  [clauses]
  (select e/alimentos
    (fields :uuid :nome :peso [:qtde_carboidrato :qtde-carboidrato] [:qtde_gorduras :qtde-gorduras] [:qtde_proteinas :qtde-proteinas] [:tipo_alimento_uuid :tipo-alimento-uuid]
            [:tipos.descricao :tipo-alimento-descricao])

    (join :inner [e/tipos-alimentos :tipos] (= :alimentos.tipo_alimento_uuid :tipos.uuid))
    
    (where clauses)))

(defn get-all []
  (get true))

(defn by-uuid
  [uuid]
  (get {:alimentos.uuid (utils.uuids/uuid-from-string uuid)}))

(defn insert!
  [alimento]
  (insert e/alimentos
          (values alimento)))

(defn update!
  [fields clauses]
  (update e/alimentos
          (set-fields (-> fields))
          (where clauses)))

(defn delete-by-uuid
  [alimento-uuid]
  (delete e/alimentos
    (where {:uuid (utils.uuids/uuid-from-string alimento-uuid)})))