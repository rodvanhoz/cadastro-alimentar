(ns cadastro-alimentar.mocks.refeicoes
  (:require [cadastro-alimentar.utils.dates :refer [now str->date]]))

(def mock-database-refeicoes-get {:uuid "c8a2bfb9-2828-4c70-84ce-b2c3dd3db230" :moment (now) :descricao "Refeicao Teste"})
(def mock-database-refeicoes-updated {:uuid "c8a2bfb9-2828-4c70-84ce-b2c3dd3db230" :moment (now) :descricao "Refeicao Teste Updated"})

(defn mock-db-refeicoes-get
  [clauses]
  (-> mock-database-refeicoes-get (list)))

(defn mock-db-refeicoes-get-not-exist
  [clauses]
  ())

(defn mock-db-refeicoes-get-for-insert
  [clauses]
  ())

(defn mock-db-refeicoes-get-for-update
  [clauses]
  (-> mock-database-refeicoes-updated (list)))

(defn mock-db-refeicoes-get-for-update-not-exist
  [clauses]
  ())
  
(defn mock-db-refeicoes-get-for-delete-not-exist
  [clauses]
  ())

(defn mock-db-refeicoes-insert!
  [refeicao])

(defn mock-db-refeicoes-update!
  [fields clauses])

(defn mock-db-refeicoes-delete-by-uuid
  [refeicao-uuid])