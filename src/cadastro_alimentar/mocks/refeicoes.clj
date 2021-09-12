(ns cadastro-alimentar.mocks.refeicoes
  (:require [cadastro-alimentar.utils.dates :refer [str->date]]))

(defn- mock-now []
  "2020-07-24T15:53:07Z")

(def mock-database-refeicoes-get {:uuid "c8a2bfb9-2828-4c70-84ce-b2c3dd3db230" :moment (mock-now) :descricao "Refeicao Teste"})
(def mock-database-refeicoes-updated {:uuid "c8a2bfb9-2828-4c70-84ce-b2c3dd3db230" :moment (mock-now) :descricao "Refeicao Teste Updated"})

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

(defn mock-db-refeicoes-get-all-refecoes-by-date
  [date]
  (list (-> {}
          (assoc :refeicao-uuid "c8a2bfb9-2828-4c70-84ce-b2c3dd3db230")
          (assoc :moment (mock-now))
          (assoc :refeicaoes-descricao nil) 
          (assoc :peso 300.0)
          (assoc :nome "Feijão Carioca Cozido")
          (assoc :peso-unitario 1.0)
          (assoc :qtde-carbo 0.136)
          (assoc :qtde-prot 0.048)
          (assoc :qtde-gordura 0.005)
          (assoc :tipos-descricao "Grão/Cereal"))
      (-> {}
          (assoc :refeicao-uuid "c8a2bfb9-2828-4c70-84ce-b2c3dd3db230")
          (assoc :moment (mock-now))
          (assoc :refeicaoes-descricao nil) 
          (assoc :peso 200.0)
          (assoc :nome "Arroz Branco Cozido")
          (assoc :peso-unitario 1.0)
          (assoc :qtde-carbo 0.281)
          (assoc :qtde-prot 0.025)
          (assoc :qtde-gordura 0.002)
          (assoc :tipos-descricao "Grão/Cereal"))
      (-> {}
          (assoc :refeicao-uuid "c8a2bfb9-2828-4c70-84ce-b2c3dd3db230")
          (assoc :moment (mock-now))
          (assoc :refeicaoes-descricao nil) 
          (assoc :peso 400.0)
          (assoc :nome "Frango Refogado")
          (assoc :peso-unitario 1.0)
          (assoc :qtde-carbo 0.0)
          (assoc :qtde-prot 0.315)
          (assoc :qtde-gordura 0.022)
          (assoc :tipos-descricao "Carne de Frango"))
      (-> {}
          (assoc :refeicao-uuid "c8a2bfb9-2828-4c70-84ce-b2c3dd3db230")
          (assoc :moment (mock-now))
          (assoc :refeicaoes-descricao nil) 
          (assoc :peso 50.0)
          (assoc :nome "Creme de Avelã")
          (assoc :peso-unitario 50.0)
          (assoc :qtde-carbo 0.48)
          (assoc :qtde-prot 0.05)
          (assoc :qtde-gordura 0.42)
          (assoc :tipos-descricao "Oleonígeno"))))