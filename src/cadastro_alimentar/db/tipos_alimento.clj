(ns cadastro-alimentar.db.tipos-alimento
  (:require [korma.db :refer :all]
            [korma.core :refer :all]
            [clojure.java.jdbc :as sql]
            [clojure.tools.logging :as log]
            [cadastro-alimentar.db :as db]
            [cadastro-alimentar.db.entities :as e]))


(defn get
  [clauses]
  (select e/tipos-alimento
    (where clauses)))

(defn get-all []
  (get true))

(defn by-id
  [id]
  (get {:tipos_alimento.id_tipo_alimento (Integer/parseInt id)}))