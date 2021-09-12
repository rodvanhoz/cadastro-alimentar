(ns cadastro-alimentar.handler-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [cheshire.core :as json]
            [cadastro-alimentar.handler :refer :all]
            [cadastro-alimentar.utils.dates :as utils.dates]
            [cadastro-alimentar.mocks.alimentos :as mock.alimentos]
            [cadastro-alimentar.db.alimentos :as db.alimentos]
            [cadastro-alimentar.db.refeicoes :as db.refeicoes]
            [cadastro-alimentar.mocks.refeicoes :as mock.refeicoes]
            [cadastro-alimentar.db.tipos-alimentos :as db.tipos-alimentos]
            [cadastro-alimentar.mocks.tipos-alimentos :as mock.tipos-alimentos]
            [cadastro-alimentar.db.pesos-alimentos :as db.pesos-alimentos]
            [cadastro-alimentar.mocks.pesos-alimentos :as mock.pesos-alimentos]
            [cadastro-alimentar.controller.pesos-alimentos :as controller.pesos-alimentos]))

(def tipo-alimento-teste {:uuid "a3770a85-eb2a-4994-8502-fa8ebaea9fa3" :descricao "Alimento Teste"})
(def alimento-teste {:uuid "ada049ae-e92c-4795-b359-c84345ffa1bb" :nome "Alimento Teste Cozido" :peso 1.0 :qtde-carboidrato 0.281 :qtde-gorduras 0.002	:qtde-proteinas 0.025	:tipo-alimento-uuid "f1fd8177-d95c-47e7-ae69-6ad6ec8f48c2"})

(def complete-refeicao-teste {:alimentos (list {:uuid "1864c9a4-2dac-48d6-9a9f-12cc91b1cb50" :peso 150.0}
                                                {:uuid "057df894-5727-4789-898b-b4bcfe07e5d5" :peso 150.0}
                                                {:uuid "54b9ba0d-4429-4ffa-9298-e242ec3c79d4" :peso 170.0}
                                                {:uuid "e3265510-87da-4a88-a610-ea18620e7802" :peso 100.0})
                                                :refeicao {:uuid "d6e3ccca-cb0f-4fa4-a0ea-92576407f998" :moment "2021-04-10T15:34:40Z" :descricao "Refeicao Teste Handler"}})

(deftest handler-test
  (testing "testing main route (invlid)")
    (let [response (app (mock/request :get "/blabla"))]
      (is (= (:status response) 404)))

  (testing "testing main route")
  (let [response (app (mock/request :get "/"))]
    (is (= (:status response) 200))))


(deftest alimentos-test
  (testing "testing get all"
    (with-redefs [db.alimentos/get (fn [clauses] (mock.alimentos/mock-db-alimentos-get clauses))]
      (let [response (app (mock/request :get "/api/alimentos"))
      body (json/parse-string (:body response) #(keyword %))
      alimento (first body)]
  (is (= (:status response) 200))
  (is (> (count body) 0))
  (is (not (empty? (:nome alimento))))
  (is (not (empty? (:tipo-alimento-descricao alimento)))))))

    
  (testing "testing geting alimento with by uuid informed"
    (with-redefs [db.alimentos/get (fn [clauses] (mock.alimentos/mock-db-alimentos-get clauses))]
      (let [response (app (mock/request :get "/api/alimentos/ff272397-2999-4896-9dda-4545c5ab4f33"))
            body (json/parse-string (:body response) #(keyword %))
            alimento (first body)]
        (is (= (:status response) 200))
        (is (= (count body) 1))
        (is (= (:uuid alimento) "ff272397-2999-4896-9dda-4545c5ab4f33"))
        (is (= (:nome alimento) "Alimento Teste Mockado"))
        (is (= (:tipo-alimento-descricao alimento) "Alimento Teste")))))

  (testing "not-found status when inform not exist uuid"
    (with-redefs [db.alimentos/get (fn [clauses] (mock.alimentos/mock-db-alimentos-get-not-exist clauses))]
      (let [response (app (mock/request :get "/api/alimentos/d0659c2d-3564-484f-8ed7-b4f4bd6c9161"))]
        (is (= (:status response) 404)))))

  (testing "insert a alimento"
    (with-redefs [db.alimentos/get (fn [clauses] (mock.alimentos/mock-db-alimentos-get-for-insert clauses))
                  db.alimentos/insert! (fn [alimento] (mock.alimentos/mock-db-alimentos-insert! alimento))]
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
    (with-redefs [db.alimentos/get (fn [clauses] (mock.alimentos/mock-db-alimentos-get clauses))
                  db.alimentos/insert! (fn [alimento] (mock.alimentos/mock-db-alimentos-insert! alimento))]
      (let [response (app (-> (mock/request :post "/api/alimentos")
                              (mock/json-body alimento-teste)
                            ;(mock/body (json/generate-string tipo-alimento-teste))
                              (mock/content-type "application/json")
                              (mock/header "Accept" "application/json")))]
        (is (= (:status response) 409)))))

  (testing "update a alimento"
    (with-redefs [db.alimentos/get (fn [clauses] (mock.alimentos/mock-db-alimentos-get-for-update clauses))
                  db.alimentos/update! (fn [fields clauses] (mock.alimentos/mock-db-alimentos-update! fields clauses))]
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

  (testing "delete a alimento by uuid"
    (with-redefs [db.alimentos/get (fn [clauses] (mock.alimentos/mock-db-alimentos-get clauses))
                  db.alimentos/delete-by-uuid (fn [alimento-uuid] (mock.alimentos/mock-db-alimentos-delete-by-uuid alimento-uuid))]
      (let [response (app (-> (mock/request :delete "/api/alimentos/ada049ae-e92c-4795-b359-c84345ffa1bb")
                              (mock/content-type "application/json")
                              (mock/header "Accept" "application/json")))]
        (is (= (:status response) 200)))))

  (testing "not update a alimento when it not exissts"
    (with-redefs [db.alimentos/get (fn [clauses] (mock.alimentos/mock-db-alimentos-get-for-update-not-exist clauses))
                  db.alimentos/update! (fn [fields clauses] (mock.alimentos/mock-db-alimentos-update! fields clauses))]
      (let [response (app (-> (mock/request :put "/api/alimentos/a3770a85-eb2a-4994-8502-fa8ebaea9fa3")
                              (mock/json-body {:nome "Alimento Cozido Teste Update" :qtde-proteinas 0.5 :tipo-alimento-uuid "5e684cd9-ab77-4bcb-96f5-a3e46d82c454"})
                            ;(mock/body (json/generate-string tipo-alimento-teste))
                              (mock/content-type "application/json")
                              (mock/header "Accept" "application/json")))]
        (is (= (:status response) 204)))))

  (testing "not delete a alimento by uuid when it nos exists"
    (with-redefs [db.alimentos/get (fn [clauses] (mock.alimentos/mock-db-alimentos-get-for-delete-not-exist clauses))
                  db.alimentos/delete-by-uuid (fn [alimento-uuid] (mock.alimentos/mock-db-alimentos-delete-by-uuid alimento-uuid))]
      (let [response (app (-> (mock/request :delete "/api/alimentos/a3770a85-eb2a-4994-8502-fa8ebaea9fa3")
                              (mock/content-type "application/json")
                              (mock/header "Accept" "application/json")))]
        (is (= (:status response) 404))))))
            
(deftest refeicoes-test
  (testing "testing get all"
    (with-redefs [db.refeicoes/get (fn [clauses] (mock.refeicoes/mock-db-refeicoes-get clauses))]
      (let [response (app (mock/request :get "/api/refeicoes"))
            body (json/parse-string (:body response) #(keyword %))
            refeicao (first body)]
        (is (= (:status response) 200))
        (is (> (count body) 0))
        (is (not (empty? (:moment refeicao)))))))
    
  (testing "testing geting refeicao with uuid infomed"
    (with-redefs [db.refeicoes/get (fn [clauses] (mock.refeicoes/mock-db-refeicoes-get clauses))]
      (let [response (app (mock/request :get "/api/refeicoes/c8a2bfb9-2828-4c70-84ce-b2c3dd3db230"))
            body (json/parse-string (:body response) #(keyword %))
            refeicao (first body)]
        (is (= (:status response) 200))
        (is (= (count body) 1))
        (is (= (str (:uuid refeicao)) "c8a2bfb9-2828-4c70-84ce-b2c3dd3db230"))
        (is (= (:moment refeicao) "2020-07-24T15:53:07Z")))))

  (testing "not-found status when inform not exist uuid"
    (with-redefs [db.refeicoes/get (fn [clauses] (mock.refeicoes/mock-db-refeicoes-get-not-exist clauses))]
      (let [response (app (mock/request :get "/api/refeicoes/38200d40-c29c-488f-acb9-6ef183989672"))]
        (is (= (:status response) 404))))))

(deftest complete-refeicao
  (testing "insert a new complete refeicao"
    (with-redefs [db.refeicoes/get (fn [clauses] (mock.refeicoes/mock-db-refeicoes-get-not-exist clauses))
                  db.refeicoes/insert! (fn [refeicao-builder] (mock.refeicoes/mock-db-refeicoes-get-for-insert refeicao-builder))
                  db.pesos-alimentos/insert! (fn [peso-alimento] (mock.pesos-alimentos/mock-db-pesos-alimentos-insert! peso-alimento))
                  db.pesos-alimentos/by-refeicao (fn [refeicao-uuid] (mock.pesos-alimentos/mock-db-pesos-alimentos-get-by-refeicao-uuid-not-exist refeicao-uuid))
                  controller.pesos-alimentos/create-pesos-alimentos-by-list (fn [pesos-alimentos] (mock.pesos-alimentos/mock-create-peso-alimentos-by-list pesos-alimentos))]
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
          (is (= (str (:descricao refeicao)) "Refeicao Teste Handler"))))))
  
    (testing "testing get all complete refeicoes information"
      (with-redefs [db.refeicoes/get (fn [clauses] (mock.refeicoes/mock-db-refeicoes-get clauses))
                    db.refeicoes/insert! (fn [refeicao-builder] (mock.refeicoes/mock-db-refeicoes-get-for-insert refeicao-builder))
                    db.pesos-alimentos/insert! (fn [peso-alimento] (mock.pesos-alimentos/mock-db-pesos-alimentos-insert! peso-alimento))
                    db.refeicoes/get-all-refeicoes-by-date (fn [date] (mock.refeicoes/mock-db-refeicoes-get-all-refecoes-by-date date))]

        (let [response (app (mock/request :get "/api/refeicoes/completas/2021-04-10"))
              body (json/parse-string (:body response) #(keyword %))
              refeicoes (:refeicoes (first body))
              calculated-macros (:calculated-macros (first body))]
          (is (= (:status response) 200))
          (is (= (count refeicoes) 4))
          (is (= (:kcal calculated-macros) 1.4582433600000002))
          (is (= (:peso calculated-macros) 950.0)))))

    (testing "delete a complete refeicao by refeicao-uuid"
      (with-redefs [db.pesos-alimentos/delete-by-refeicao (fn [refeicao-uuid] (mock.pesos-alimentos/mock-db-pesos-alimentos-delete-by-refeicao refeicao-uuid))
                    db.pesos-alimentos/by-refeicao (fn [refeicao-uuid] (mock.pesos-alimentos/mock-db-pesos-alimentos-get-by-refeicao-uuid refeicao-uuid))
                    db.refeicoes/delete-by-uuid (fn [refeicao-uuid] (mock.refeicoes/mock-db-refeicoes-delete-by-uuid refeicao-uuid))
                    db.refeicoes/get (fn [clauses] (mock.refeicoes/mock-db-refeicoes-get clauses))]
      (let [response (app (-> (mock/request :delete "/api/refeicoes/completas/d6e3ccca-cb0f-4fa4-a0ea-92576407f998")
                            (mock/content-type "application/json")
                            (mock/header "Accept" "application/json")))]
      (is (= (:status response) 200))))))

(deftest tipos-alimento-test
  (testing "testing get all"
    (with-redefs [db.tipos-alimentos/get (fn [clauses] (mock.tipos-alimentos/mock-db-tipos-alimentos-get clauses))]
      (let [response (app (mock/request :get "/api/tipos_alimentos"))
            body (json/parse-string (:body response) #(keyword %))
            tipo-alimento (first body)]
        (is (= (:status response) 200))
        (is (> (count body) 0))
        (is (not (empty? (:descricao tipo-alimento)))))))
    
  (testing "testing geting refeicao with uuid informed"
    (with-redefs [db.tipos-alimentos/get (fn [clauses] (mock.tipos-alimentos/mock-db-tipos-alimentos-get clauses))]
      (let [response (app (mock/request :get "/api/tipos_alimentos/a3770a85-eb2a-4994-8502-fa8ebaea9fa3"))
            body (json/parse-string (:body response) #(keyword %))
            tipo-alimento (first body)]
        (is (= (:status response) 200))
        (is (= (count body) 1))
        (is (= (:uuid tipo-alimento) "a3770a85-eb2a-4994-8502-fa8ebaea9fa3"))
        (is (= (:descricao tipo-alimento) "Alimento Teste")))))

  (testing "not-found status when inform an not exist uuid"
    (with-redefs [db.tipos-alimentos/get (fn [clauses] (mock.tipos-alimentos/mock-db-tipos-alimentos-get-not-exist clauses))]
      (let [response (app (mock/request :get "/api/tipos_alimentos/13dde849-a5a5-473e-b74e-5543a7de83a1"))]
        (is (= (:status response) 404)))))

  (testing "insert a tipo-alimento"
    (with-redefs [db.tipos-alimentos/get (fn [clauses] (mock.tipos-alimentos/mock-db-alimentipos-alimentos-get-for-insert clauses))
                  db.tipos-alimentos/insert! (fn [tipo-alimento] (mock.tipos-alimentos/mock-db-tipos-alimentos-insert! tipo-alimento))]
      (let [response (app (-> (mock/request :post "/api/tipos_alimentos")
                              (mock/json-body tipo-alimento-teste)
                            ;(mock/body (json/generate-string tipo-alimento-teste))
                              (mock/content-type "application/json")
                              (mock/header "Accept" "application/json")))
            body (json/parse-string (:body response) #(keyword %))]
        (is (= (:status response) 200))
        (is (= (str  (:uuid (first body))) "a3770a85-eb2a-4994-8502-fa8ebaea9fa3"))
        (is (= (:descricao (first body)) "Alimento Teste")))))

  (testing "Not insert a tipo-alimento when it exists"
    (with-redefs [db.tipos-alimentos/get (fn [clauses] (mock.tipos-alimentos/mock-db-tipos-alimentos-get clauses))
                  db.tipos-alimentos/insert! (fn [tipo-alimento] (mock.tipos-alimentos/mock-db-tipos-alimentos-insert! tipo-alimento))]
      (let [response (app (-> (mock/request :post "/api/tipos_alimentos")
                              (mock/json-body tipo-alimento-teste)
                            ;(mock/body (json/generate-string tipo-alimento-teste))
                              (mock/content-type "application/json")
                              (mock/header "Accept" "application/json")))]
        (is (= (:status response) 409)))))

  (testing "update a tipo-alimento"
    (with-redefs [db.tipos-alimentos/get (fn [clauses] (mock.tipos-alimentos/mock-db-tipos-alimentos-get-for-update clauses))
                  db.tipos-alimentos/update! (fn [fields clauses] (mock.tipos-alimentos/mock-db-tipos-alimentos-update! fields clauses))]
      (let [response (app (-> (mock/request :put "/api/tipos_alimentos/a3770a85-eb2a-4994-8502-fa8ebaea9fa3")
                              (mock/json-body {:descricao "Tipo Alimento Update"})
                            ;(mock/body (json/generate-string tipo-alimento-teste))
                              (mock/content-type "application/json")
                              (mock/header "Accept" "application/json")))
            body (json/parse-string (:body response) #(keyword %))]
        (is (= (:status response) 200))
        (is (= (:descricao (first body)) "Tipo Alimento Update")))))
  
  (testing "delete a tipo-alimento by uuid"
    (with-redefs [db.tipos-alimentos/get (fn [clauses] (mock.tipos-alimentos/mock-db-tipos-alimentos-get clauses))
                  db.tipos-alimentos/delete-by-uuid (fn [tipo-alimento-uuid] (mock.tipos-alimentos/mock-db-tipos-alimentos-delete-by-uuid tipo-alimento-uuid))]
      (let [response (app (-> (mock/request :delete "/api/tipos_alimentos/a3770a85-eb2a-4994-8502-fa8ebaea9fa3")
                              (mock/content-type "application/json")
                              (mock/header "Accept" "application/json")))]
        (is (= (:status response) 200)))))

  (testing "not update a tipo-alimento when it not exissts"
    (with-redefs [db.tipos-alimentos/get (fn [clauses] (mock.tipos-alimentos/mock-db-tipos-alimentos-get-not-exist clauses))
                  db.tipos-alimentos/update! (fn [fields clauses] (mock.tipos-alimentos/mock-db-tipos-alimentos-update! fields clauses))]
      (let [response (app (-> (mock/request :put "/api/tipos_alimentos/a3770a85-eb2a-4994-8502-fa8ebaea9fa3")
                              (mock/json-body {:descricao "Tipo Alimento Update"})
                            ;(mock/body (json/generate-string tipo-alimento-teste))
                              (mock/content-type "application/json")
                              (mock/header "Accept" "application/json")))]
        (is (= (:status response) 204)))))

  (testing "not delete a tipo-alimento by uuid when it not exists"
    (with-redefs [db.tipos-alimentos/get (fn [clauses] (mock.tipos-alimentos/mock-db-tipos-alimentos-get-not-exist clauses))
                  db.tipos-alimentos/delete-by-uuid (fn [tipo-alimento-uuid] (mock.tipos-alimentos/mock-db-tipos-alimentos-delete-by-uuid tipo-alimento-uuid))]
      (let [response (app (-> (mock/request :delete "/api/tipos_alimentos/a3770a85-eb2a-4994-8502-fa8ebaea9fa3")
                              (mock/content-type "application/json")
                              (mock/header "Accept" "application/json")))]
        (is (= (:status response) 404))))))
