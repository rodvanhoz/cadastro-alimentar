(ns cadastro-alimentar.mocks.pesos-alimentos)

(defn- mock-now []
  "2020-07-24T15:53:07Z")

(def mock-database-pesos-alimentos-get {:peso 100.2 :alimento_uuid "ff272397-2999-4896-9dda-4545c5ab4f33" :refeicao_uuid "c8a2bfb9-2828-4c70-84ce-b2c3dd3db230"})
(def mock-database-pesos-alimentos-get-by-list (list {:peso 100.2 :alimento_uuid "ff272397-2999-4896-9dda-4545c5ab4f33" :refeicao_uuid "d6e3ccca-cb0f-4fa4-a0ea-92576407f998"}
                                                     {:peso 100.2 :alimento_uuid "ff272397-2999-4896-9dda-4545c5ab4f33" :refeicao_uuid "d6e3ccca-cb0f-4fa4-a0ea-92576407f998"}
                                                     {:peso 100.2 :alimento_uuid "ff272397-2999-4896-9dda-4545c5ab4f33" :refeicao_uuid "d6e3ccca-cb0f-4fa4-a0ea-92576407f998"}
                                                     {:peso 100.2 :alimento_uuid "ff272397-2999-4896-9dda-4545c5ab4f33" :refeicao_uuid "d6e3ccca-cb0f-4fa4-a0ea-92576407f998"}))

(defn mock-db-pesos-alimentos-get
  [clauses]
  (-> mock-database-pesos-alimentos-get (list)))

(defn mock-db-pesos-alimentos-get-by-refeicao-uuid
  [refeicao-uuid]
  (-> mock-database-pesos-alimentos-get (list)))

(defn mock-db-pesos-alimentos-get-not-exist
  [clauses]
  ())

(defn mock-db-pesos-alimentos-get-by-refeicao-uuid-not-exist
  [refeicao-uuid]
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

(defn mock-create-peso-alimentos-by-list
  [pesos-alimentos]
  mock-database-pesos-alimentos-get-by-list)