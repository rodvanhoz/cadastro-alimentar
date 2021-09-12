(ns cadastro-alimentar.mocks.tipos-alimentos)

(def mock-database-tipos-alimentos-get {:uuid "a3770a85-eb2a-4994-8502-fa8ebaea9fa3" :descricao "Alimento Teste"})
(def mock-database-tipos-alimentos-updated {:uuid "a3770a85-eb2a-4994-8502-fa8ebaea9fa3" :descricao "Tipo Alimento Update"})

(defn mock-db-tipos-alimentos-get
  [clauses]
  (-> mock-database-tipos-alimentos-get (list)))

(defn mock-db-tipos-alimentos-get-not-exist
  [clauses]
  ())

(defn mock-db-alimentipos-alimentos-get-for-insert
  [clauses]
  ())

(defn mock-db-tipos-alimentos-get-for-update
  [clauses]
  (-> mock-database-tipos-alimentos-updated (list)))

(defn mock-db-tipos-alimentos-get-for-update-not-exist
  [clauses]
  ())
  
(defn mock-db-tipos-alimentos-get-for-delete-not-exist
  [clauses]
  ())

(defn mock-db-tipos-alimentos-insert!
  [tipo-alimento])

(defn mock-db-tipos-alimentos-update!
  [fields clauses])

(defn mock-db-tipos-alimentos-delete-by-uuid
  [tipo-alimento-uuid])

(defn mock-db-tipos-alimentos-delete-by-descricao
  [descricao])