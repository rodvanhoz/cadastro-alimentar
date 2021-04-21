(ns cadastro-alimentar.handler-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [cheshire.core :as json]
            [cadastro-alimentar.handler :refer :all]))

(def tipo-alimento-teste {:uuid "a3770a85-eb2a-4994-8502-fa8ebaea9fa3" :descricao "Alimento Teste"})

(deftest handler-test
  (testing "testing main route (invlid)")
    (let [response (app (mock/request :get "/blabla"))]
      (is (= (:status response) 404)))

  (testing "testing main route")
  (let [response (app (mock/request :get "/"))]
    (is (= (:status response) 200))))


(deftest alimentos-test
  (testing "testing get all"
    (let [response (app (mock/request :get "/api/alimentos"))
          body (json/parse-string (:body response) #(keyword %))
          alimento (first body)]
      (is (= (:status response) 200))
      (is (> (count body) 0))
      (is (not (empty? (:nome alimento))))
      (is (not (empty? (:tipo-alimento-descricao alimento))))))
    
  (testing "testing geting alimento with by uuid 1864c9a4-2dac-48d6-9a9f-12cc91b1cb50"
    (let [response (app (mock/request :get "/api/alimentos/1864c9a4-2dac-48d6-9a9f-12cc91b1cb50"))
          body (json/parse-string (:body response) #(keyword %))
          alimento (first body)]
      (is (= (:status response) 200))
      (is (= (count body) 1))
      (is (= (:uuid alimento) "1864c9a4-2dac-48d6-9a9f-12cc91b1cb50"))
      (is (= (:nome alimento) "Arroz Branco Cozido"))
      (is (= (:tipo-alimento-descricao alimento) "Grão/Cereal"))))

  (testing "not-found status when inform not exist uuid"
    (let [response (app (mock/request :get "/api/alimentos/d0659c2d-3564-484f-8ed7-b4f4bd6c9161"))]
      (is (= (:status response) 404))))
  
  (testing "invalid route"
    (let [response (app (mock/request :get "/api/alimentos/invalid"))]
      (is (= (:status response) 400)))))
    
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
                            (mock/header "Accept" "application/json")))]
      (is (= (:status response) 200))))
  
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
