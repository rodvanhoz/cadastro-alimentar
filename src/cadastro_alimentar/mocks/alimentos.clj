(ns cadastro-alimentar.mocks.alimentos)

(def mock-database-alimentos-get {:uuid "ff272397-2999-4896-9dda-4545c5ab4f33" :nome "Alimento Teste Mockado" :peso 1.0 :qtde-carboidrato 0.281 :qtde-gorduras 0.002	:qtde-proteinas 0.025	:tipo-alimento-uuid "f1fd8177-d95c-47e7-ae69-6ad6ec8f48c2" :tipo-alimento-descricao "Alimento Teste"})
(def mock-database-alimentos-updated {:uuid "ff272397-2999-4896-9dda-4545c5ab4f33" :nome "Alimento Cozido Teste Update" :peso 1.0 :qtde-carboidrato 0.281 :qtde-gorduras 0.002	:qtde-proteinas 0.5	:tipo-alimento-uuid "5e684cd9-ab77-4bcb-96f5-a3e46d82c454" :tipo-alimento-descricao "Alimento Teste Updated"})
(def mock-database-alimentos-insert-exists {:uuid "ada049ae-e92c-4795-b359-c84345ffa1bb" :nome "Alimento Teste Cozido" :peso 1.0 :qtde-carboidrato 0.281 :qtde-gorduras 0.002	:qtde-proteinas 0.025	:tipo-alimento-uuid "f1fd8177-d95c-47e7-ae69-6ad6ec8f48c2" :tipo-alimento-descricao "Alimento Teste"})

(defn mock-db-alimentos-get
  [clauses]
  (-> mock-database-alimentos-get (list)))

(defn mock-db-alimentos-get-not-exist
  [clauses]
  ())

(defn mock-db-alimentos-get-for-insert
  [clauses]
  ())

(defn mock-db-alimentos-get-for-update
  [clauses]
  (-> mock-database-alimentos-updated (list)))

(defn mock-db-alimentos-get-for-update-not-exist
  [clauses]
  ())
  
(defn mock-db-alimentos-get-for-delete-not-exist
  [clauses]
  ())

(defn mock-db-alimentos-insert!
  [alimento])

(defn mock-db-alimentos-update!
  [fields clauses])

(defn mock-db-alimentos-delete-by-uuid
  [alimento-uuid])