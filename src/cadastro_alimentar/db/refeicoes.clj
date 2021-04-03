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