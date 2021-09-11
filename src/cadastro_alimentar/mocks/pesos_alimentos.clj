(ns cadastro-alimentar.mocks.pesos-alimentos)

(def mock-database-pesos-alimentos-get {:peso 100.2 :alimento_uuid "ff272397-2999-4896-9dda-4545c5ab4f33" :refeicao_uuid "c8a2bfb9-2828-4c70-84ce-b2c3dd3db230"})

(defn mock-db-pesos-alimentos-get
  [clauses]
  (-> mock-database-pesos-alimentos-get (list)))

(defn mock-db-pesos-alimentos-get-by-refeicao-uuid
  [refeicao-uuid]
  (-> mock-database-pesos-alimentos-get (list)))

(defn mock-db-pesos-alimentos-get-not-exist
  [clauses]
  ())

(defn mock-db-pesos-alimentos-get-for-update-not-exist
  [clauses]
  ())
  
(defn mock-db-pesos-alimentos-get-for-delete-not-exist
  [clauses]
  ())

(defn mock-db-pesos-alimentos-insert!
  [peso-alimento])

(defn mock-db-pesos-alimentos-update!
  [fields clauses])

(defn mock-db-pesos-alimentos-delete-by-refeicao
  [refeicao-uuid])