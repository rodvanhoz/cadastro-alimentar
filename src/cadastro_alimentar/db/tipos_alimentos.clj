(ns cadastro-alimentar.db.tipos-alimentos
  (:require [korma.db :refer :all]
            [korma.core :refer :all]
            [clojure.java.jdbc :as sql]
            [clojure.tools.logging :as log]
            [cadastro-alimentar.db :as db]
            [cadastro-alimentar.db.entities :as e]
            [cadastro-alimentar.utils.uuids :as utils.uuids]))


(defn get
  [clauses]
  (select e/tipos-alimentos
    (where clauses)))

(defn get-all []
  (get true))

(defn by-uuid
  [uuid]
  (get {:tipos_alimentos.uuid (utils.uuids/uuid-from-string uuid)}))