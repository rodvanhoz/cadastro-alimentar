(ns cadastro-alimentar.handler-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [cheshire.core :as json]
            [cadastro-alimentar.handler :refer :all]
            [cadastro-alimentar.utils.dates :as utils.dates]
            [cadastro-alimentar.db.alimentos :as db.alimentos]))

(def tipo-alimento-teste {:uuid "a3770a85-eb2a-4994-8502-fa8ebaea9fa3" :descricao "Alimento Teste"})
(def alimento-teste {:uuid "ada049ae-e92c-4795-b359-c84345ffa1bb" :nome "Alimento Teste Cozido" :peso 1.0 :qtde-carboidrato 0.281 :qtde-gorduras 0.002	:qtde-proteinas 0.025	:tipo-alimento-uuid "f1fd8177-d95c-47e7-ae69-6ad6ec8f48c2"})

(def complete-refeicao-teste {:alimentos (list {:uuid "1864c9a4-2dac-48d6-9a9f-12cc91b1cb50" :peso 150.0}
                                               {:uuid "057df894-5727-4789-898b-b4bcfe07e5d5" :peso 150.0}
                                               {:uuid "54b9ba0d-4429-4ffa-9298-e242ec3c79d4" :peso 170.0}
                                               {:uuid "e3265510-87da-4a88-a610-ea18620e7802" :peso 100.0})
                                               :refeicao {:uuid "d6e3ccca-cb0f-4fa4-a0ea-92576407f998" :moment "2021-04-10T15:34:40Z" :descricao "Refeicao Teste Handler"}})

(def mock-database-alimentos-get {:uuid "ff272397-2999-4896-9dda-4545c5ab4f33" :nome "Alimento Teste Mockado" :peso 1.0 :qtde-carboidrato 0.281 :qtde-gorduras 0.002	:qtde-proteinas 0.025	:tipo-alimento-uuid "f1fd8177-d95c-47e7-ae69-6ad6ec8f48c2" :tipo-alimento-descricao "Alimento Teste"})
(def mock-database-alimentos-updated {:uuid "ff272397-2999-4896-9dda-4545c5ab4f33" :nome "Alimento Cozido Teste Update" :peso 1.0 :qtde-carboidrato 0.281 :qtde-gorduras 0.002	:qtde-proteinas 0.5	:tipo-alimento-uuid "5e684cd9-ab77-4bcb-96f5-a3e46d82c454" :tipo-alimento-descricao "Alimento Teste Updated"})
(def mock-database-alimentos-insert-exists {:uuid "ada049ae-e92c-4795-b359-c84345ffa1bb" :nome "Alimento Teste Cozido" :peso 1.0 :qtde-carboidrato 0.281 :qtde-gorduras 0.002	:qtde-proteinas 0.025	:tipo-alimento-uuid "f1fd8177-d95c-47e7-ae69-6ad6ec8f48c2" :tipo-alimento-descricao "Alimento Teste"})

(defn- mock-db-alimentos-get
  [clauses]
  (-> mock-database-alimentos-get (list)))

(defn- mock-db-alimentos-get-not-exist
  [clauses]
  ())

(defn- mock-db-alimentos-get-for-insert
  [clauses]
  ())

(defn- mock-db-alimentos-get-for-update
  [clauses]
  (-> mock-database-alimentos-updated (list)))

(defn- mock-db-alimentos-get-for-update-not-exist
  [clauses]
  ())
  
(defn- mock-db-alimentos-get-for-delete-not-exist
  [clauses]
  ())

(defn- mock-db-alimentos-insert!
  [alimento])

(defn- mock-db-alimentos-update!
  [fields clauses])

(defn- mock-db-alimentos-delete-by-uuid
  [alimento-uuid])

(deftest handler-test
  (testing "testing main route (invlid)")
    (let [response (app (mock/request :get "/blabla"))]
      (is (= (:status response) 404)))

  (testing "testing main route")
  (let [response (app (mock/request :get "/"))]
    (is (= (:status response) 200))))


(deftest alimentos-test
  (testing "testing get all"
    (with-redefs [db.alimentos/get (fn [clauses] (mock-db-alimentos-get clauses))]
      (let [response (app (mock/request :get "/api/alimentos"))
      body (json/parse-string (:body response) #(keyword %))
      alimento (first body)]
  (is (= (:status response) 200))
  (is (> (count body) 0))
  (is (not (empty? (:nome alimento))))
  (is (not (empty? (:tipo-alimento-descricao alimento)))))))

    
  (testing "testing geting alimento with by uuid informed"
    (with-redefs [db.alimentos/get (fn [clauses] (mock-db-alimentos-get clauses))]
      (let [response (app (mock/request :get "/api/alimentos/ff272397-2999-4896-9dda-4545c5ab4f33"))
            body (json/parse-string (:body response) #(keyword %))
            alimento (first body)]
        (is (= (:status response) 200))
        (is (= (count body) 1))
        (is (= (:uuid alimento) "ff272397-2999-4896-9dda-4545c5ab4f33"))
        (is (= (:nome alimento) "Alimento Teste Mockado"))
        (is (= (:tipo-alimento-descricao alimento) "Alimento Teste")))))

  (testing "not-found status when inform not exist uuid"
    (with-redefs [db.alimentos/get (fn [clauses] (mock-db-alimentos-get-not-exist clauses))]
      (let [response (app (mock/request :get "/api/alimentos/d0659c2d-3564-484f-8ed7-b4f4bd6c9161"))]
        (is (= (:status response) 404)))))

  (testing "insert a alimento"
    (with-redefs [db.alimentos/get (fn [clauses] (mock-db-alimentos-get-for-insert clauses))
                  db.alimentos/insert! (fn [alimento] (mock-db-alimentos-insert! alimento))]
      (let [response (app (-> (mock/request :post "/api/alimentos")
                              (mock/json-body alimento-teste)
              ;(mock/body (json/generate-string tipo-alimento-teste))
                              (mock/content-type "application/json")
                              (mock/header "Accept" "application/json")))
            body (json/parse-string (:body response) #(keyword %))]
        (is (= (:status response) 200))
        (is (= (str  (:uuid (first body))) "ada049ae-e92c-4795-b359-c84345ffa1bb"))
        (is (= (:nome (first body)) "Alimento Teste Cozido"))
        (is (= (:peso (first body)) 1.0))
        (is (= (:qtde-carboidrato (first body)) 0.281))
        (is (= (:qtde-gorduras (first body)) 0.002))
        (is (= (:qtde-proteinas (first body)) 0.025))
        (is (= (str (:tipo-alimento-uuid (first body))) "f1fd8177-d95c-47e7-ae69-6ad6ec8f48c2")))))

  (testing "Not insert a alimento when it exists"
    (with-redefs [db.alimentos/get (fn [clauses] (mock-db-alimentos-get clauses))
                  db.alimentos/insert! (fn [alimento] (mock-db-alimentos-insert! alimento))]
      (let [response (app (-> (mock/request :post "/api/alimentos")
                              (mock/json-body alimento-teste)
                            ;(mock/body (json/generate-string tipo-alimento-teste))
                              (mock/content-type "application/json")
                              (mock/header "Accept" "application/json")))]
        (is (= (:status response) 409)))))

  (testing "update a alimento"
    (with-redefs [db.alimentos/get (fn [clauses] (mock-db-alimentos-get-for-update clauses))
                  db.alimentos/update! (fn [fields clauses] (mock-db-alimentos-update! fields clauses))]
      (let [response (app (-> (mock/request :put "/api/alimentos/ada049ae-e92c-4795-b359-c84345ffa1bb")
                              (mock/json-body {:nome "Alimento Cozido Teste Update" :qtde-proteinas 0.5 :tipo-alimento-uuid "5e684cd9-ab77-4bcb-96f5-a3e46d82c454"})
                            ;(mock/body (json/generate-string tipo-alimento-teste))
                              (mock/content-type "application/json")
                              (mock/header "Accept" "application/json")))
            body (json/parse-string (:body response) #(keyword %))]
        (is (= (:status response) 200))
        (is (= (:nome (first body)) "Alimento Cozido Teste Update"))
        (is (= (:qtde-proteinas (first body)) 0.5))
        (is (= (str (:tipo-alimento-uuid (first body))) "5e684cd9-ab77-4bcb-96f5-a3e46d82c454"))))) ;a400f357-0ab9-4819-9c82-1280d150e1de

  (testing "delete a tipo-alimento by uuid"
    (with-redefs [db.alimentos/get (fn [clauses] (mock-db-alimentos-get clauses))
                  db.alimentos/delete-by-uuid (fn [alimento-uuid] (mock-db-alimentos-delete-by-uuid alimento-uuid))]
      (let [response (app (-> (mock/request :delete "/api/alimentos/ada049ae-e92c-4795-b359-c84345ffa1bb")
                              (mock/content-type "application/json")
                              (mock/header "Accept" "application/json")))]
        (is (= (:status response) 200)))))

  (testing "not update a tipo-alimento when it not exissts"
    (with-redefs [db.alimentos/get (fn [clauses] (mock-db-alimentos-get-for-update-not-exist clauses))
                  db.alimentos/update! (fn [fields clauses] (mock-db-alimentos-update! fields clauses))]
      (let [response (app (-> (mock/request :put "/api/alimentos/a3770a85-eb2a-4994-8502-fa8ebaea9fa3")
                              (mock/json-body {:nome "Alimento Cozido Teste Update" :qtde-proteinas 0.5 :tipo-alimento-uuid "5e684cd9-ab77-4bcb-96f5-a3e46d82c454"})
                            ;(mock/body (json/generate-string tipo-alimento-teste))
                              (mock/content-type "application/json")
                              (mock/header "Accept" "application/json")))]
        (is (= (:status response) 204)))))

  (testing "not delete a tipo-alimento by uuid when it nos exists"
    (with-redefs [db.alimentos/get (fn [clauses] (mock-db-alimentos-get-for-delete-not-exist clauses))
                  db.alimentos/delete-by-uuid (fn [alimento-uuid] (mock-db-alimentos-delete-by-uuid alimento-uuid))]
      (let [response (app (-> (mock/request :delete "/api/alimentos/a3770a85-eb2a-4994-8502-fa8ebaea9fa3")
                              (mock/content-type "application/json")
                              (mock/header "Accept" "application/json")))]
        (is (= (:status response) 404))))))
            
(deftest refeicoes-test
  (testing "testing get all"
    (let [response (app (mock/request :get "/api/refeicoes"))
          body (json/parse-string (:body response) #(keyword %))
          refeicao (first body)]
      (is (= (:status response) 200))
      (is (> (count body) 0))
      (is (not (empty? (:moment refeicao))))))
    
  (testing "testing geting refeicao with uuid c8a2bfb9-2828-4c70-84ce-b2c3dd3db230"
    (let [response (app (mock/request :get "/api/refeicoes/c8a2bfb9-2828-4c70-84ce-b2c3dd3db230"))
          body (json/parse-string (:body response) #(keyword %))
          refeicao (first body)]
      (is (= (:status response) 200))
      (is (= (count body) 1))
      (is (= (str (:uuid refeicao)) "c8a2bfb9-2828-4c70-84ce-b2c3dd3db230"))
      (is (= (:moment refeicao) "2020-07-24T15:53:07Z"))))

  (testing "testind get all complete refeicoes information"
    (let [response (app (mock/request :get "/api/refeicoes/completas/2020-07-24"))
          body (json/parse-string (:body response) #(keyword %))
          refeicoes (:refeicoes (first body))
          calculated-macros (:calculated-macros (first body))]
      (is (= (:status response) 200))
      (is (= (count refeicoes) 5))
      (is (= (:kcal calculated-macros) 2.9097633600000004))
      (is (= (:peso calculated-macros)  1000.0))))

  (testing "not-found status when inform not exist uuid"
    (let [response (app (mock/request :get "/api/refeicoes/38200d40-c29c-488f-acb9-6ef183989672"))]
      (is (= (:status response) 404))))
      
  (testing "invalid route"
    (let [response (app (mock/request :get "/api/refeicoes/invalid"))]
      (is (= (:status response) 400)))))

(deftest complete-refeicao
  (testing "insert a new complete refeicao"
    (let [response (app (-> (mock/request :post "/api/refeicoes/completas")
              (mock/json-body complete-refeicao-teste)
              ;(mock/body (json/generate-string tipo-alimento-teste))
              (mock/content-type "application/json")
              (mock/header "Accept" "application/json")))
          body (json/parse-string (:body response) #(keyword %))]
      (is (= (count body) 2))
  
      (let [alimentos (apply list (:alimentos body))
            refeicao (:refeicao body)]
        (is (= (count alimentos) 4))
        (is (= (str (:uuid refeicao)) "d6e3ccca-cb0f-4fa4-a0ea-92576407f998"))
        (is (= (str (utils.dates/str->date (:moment refeicao))) "2021-04-10 12:34:40.0"))
        (is (= (str (:descricao refeicao)) "Refeicao Teste Handler")))))        
  
    (testing "testind get all complete refeicoes information"
      (let [response (app (mock/request :get "/api/refeicoes/completas/2021-04-10"))
            body (json/parse-string (:body response) #(keyword %))
            refeicoes (:refeicoes (first body))
            calculated-macros (:calculated-macros (first body))]
        (is (= (:status response) 200))
        (is (= (count refeicoes) 4))
        (is (= (:kcal calculated-macros) 0.033737760000000006))
        (is (= (:peso calculated-macros) 570.0))))

    (testing "delete a complete refeicao by refeicao-uuid"
      (let [response (app (-> (mock/request :delete "/api/refeicoes/completas/d6e3ccca-cb0f-4fa4-a0ea-92576407f998")
                            (mock/content-type "application/json")
                            (mock/header "Accept" "application/json")))]
      (is (= (:status response) 200)))))

(deftest tipos-alimento-test
  (testing "testing get all"
    (let [response (app (mock/request :get "/api/tipos_alimentos"))
          body (json/parse-string (:body response) #(keyword %))
          tipo-alimento (first body)]
      (is (= (:status response) 200))
      (is (> (count body) 0))
      (is (not (empty? (:descricao tipo-alimento))))))
    
  (testing "testing geting refeicao with uuid c4b96964-60fe-4e82-be05-ee38a555cde0"
    (let [response (app (mock/request :get "/api/tipos_alimentos/c4b96964-60fe-4e82-be05-ee38a555cde0"))
          body (json/parse-string (:body response) #(keyword %))
          tipo-alimento (first body)]
      (is (= (:status response) 200))
      (is (= (count body) 1))
      (is (= (:uuid tipo-alimento) "c4b96964-60fe-4e82-be05-ee38a555cde0"))
      (is (= (:descricao tipo-alimento) "Carne Bovina"))))

  (testing "not-found status when inform not exist uuid"
    (let [response (app (mock/request :get "/api/tipos_alimentos/13dde849-a5a5-473e-b74e-5543a7de83a1"))]
      (is (= (:status response) 404))))

  (testing "insert a tipo-alimento"
    (let [response (app (-> (mock/request :post "/api/tipos_alimentos")
                            (mock/json-body tipo-alimento-teste)
                            ;(mock/body (json/generate-string tipo-alimento-teste))
                            (mock/content-type "application/json")
                            (mock/header "Accept" "application/json")))
          body (json/parse-string (:body response) #(keyword %))]
      (is (= (:status response) 200))
      (is (= (str  (:uuid (first body))) "a3770a85-eb2a-4994-8502-fa8ebaea9fa3"))
      (is (= (:descricao (first body)) "Alimento Teste"))))

  (testing "Not insert a tipo-alimento when it exists"
    (let [response (app (-> (mock/request :post "/api/tipos_alimentos")
                            (mock/json-body tipo-alimento-teste)
                            ;(mock/body (json/generate-string tipo-alimento-teste))
                            (mock/content-type "application/json")
                            (mock/header "Accept" "application/json")))]
      (is (= (:status response) 409))))

  (testing "update a tipo-alimento"
    (let [response (app (-> (mock/request :put "/api/tipos_alimentos/a3770a85-eb2a-4994-8502-fa8ebaea9fa3")
                            (mock/json-body {:descricao "Tipo Alimento Update"})
                            ;(mock/body (json/generate-string tipo-alimento-teste))
                            (mock/content-type "application/json")
                            (mock/header "Accept" "application/json")))
          body (json/parse-string (:body response) #(keyword %))]
      (is (= (:status response) 200))
      (is (= (:descricao (first body)) "Tipo Alimento Update"))))
  
  (testing "delete a tipo-alimento by uuid"
    (let [response (app (-> (mock/request :delete "/api/tipos_alimentos/a3770a85-eb2a-4994-8502-fa8ebaea9fa3")
                            (mock/content-type "application/json")
                            (mock/header "Accept" "application/json")))]
      (is (= (:status response) 200))))

  (testing "not update a tipo-alimento when it not exissts"
    (let [response (app (-> (mock/request :put "/api/tipos_alimentos/a3770a85-eb2a-4994-8502-fa8ebaea9fa3")
                            (mock/json-body {:descricao "Tipo Alimento Update"})
                            ;(mock/body (json/generate-string tipo-alimento-teste))
                            (mock/content-type "application/json")
                            (mock/header "Accept" "application/json")))]
      (is (= (:status response) 204))))

  (testing "not delete a tipo-alimento by uuid when it nos exists"
    (let [response (app (-> (mock/request :delete "/api/tipos_alimentos/a3770a85-eb2a-4994-8502-fa8ebaea9fa3")
                            (mock/content-type "application/json")
                            (mock/header "Accept" "application/json")))]
      (is (= (:status response) 404))))
      
  (testing "invalid route"
    (let [response (app (mock/request :get "/api/tipos_alimentos/invalid"))]
      (is (= (:status response) 400)))))
