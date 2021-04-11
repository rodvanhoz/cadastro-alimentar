(ns cadastro-alimentar.handler-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [cheshire.core :as json]
            [cadastro-alimentar.handler :refer :all]))

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
      (is (int? (:id_alimento alimento)))
      (is (not (empty? (:nome alimento))))
      (is (int? (:id_tipo_alimento alimento)))
      (is (not (empty? (:tipos-descricao alimento))))))
    
  (testing "testing geting alimento with id 1"
    (let [response (app (mock/request :get "/api/alimentos/1"))
          body (json/parse-string (:body response) #(keyword %))
          alimento (first body)]
      (is (= (:status response) 200))
      (is (= (count body) 1))
      (is (= (:id_alimento alimento) 1))
      (is (= (:nome alimento) "Arroz Branco Cozido"))
      (is (= (:id_tipo_alimento alimento) 2))
      (is (= (:tipos-descricao alimento) "Grão/Cereal"))))

  (testing "not-found status when inform invalid id"
    (let [response (app (mock/request :get "/api/alimentos/9999999"))]
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
      (is (int? (:id_refeicao refeicao)))
      (is (not (empty? (:moment refeicao))))))
    
  (testing "testing geting refeicao with id 1"
    (let [response (app (mock/request :get "/api/refeicoes/1"))
          body (json/parse-string (:body response) #(keyword %))
          refeicao (first body)]
      (is (= (:status response) 200))
      (is (= (count body) 1))
      (is (= (:id_refeicao refeicao) 1))
      (is (= (:moment refeicao) "2020-07-24T15:53:07Z"))))

  (testing "testind get all complete refeicoes information"
    (let [response (app (mock/request :get "/api/refeicoes/completas/2020-07-24"))
          body (json/parse-string (:body response) #(keyword %))
          refeicoes (:refeicoes (first body))
          calculated-macros (:calculated-macros (first body))]
      (is (= (:status response) 200))
      (is (= (count refeicoes) 6))
      (is (= (:kcal calculated-macros) 2.9367777600000005))
      (is (= (:peso calculated-macros)  1170.0))))

  (testing "not-found status when inform invalid id"
    (let [response (app (mock/request :get "/api/refeicoes/9999999"))]
      (is (= (:status response) 404))))
  
  (testing "invalid route"
    (let [response (app (mock/request :get "/api/refeicoes/invalid"))]
      (is (= (:status response) 400)))))

(deftest tipos-alimento-test
  (testing "testing get all"
    (let [response (app (mock/request :get "/api/tipos_alimento"))
          body (json/parse-string (:body response) #(keyword %))
          tipo-alimento (first body)]
      (is (= (:status response) 200))
      (is (> (count body) 0))
      (is (int? (:id_tipo_alimento tipo-alimento)))
      (is (not (empty? (:descricao tipo-alimento))))))
    
  (testing "testing geting refeicao with id 3"
    (let [response (app (mock/request :get "/api/tipos_alimento/3"))
          body (json/parse-string (:body response) #(keyword %))
          tipo-alimento (first body)]
      (is (= (:status response) 200))
      (is (= (count body) 1))
      (is (= (:id_tipo_alimento tipo-alimento) 3))
      (is (= (:descricao tipo-alimento) "Carne Bovina"))))

  (testing "not-found status when inform invalid id"
    (let [response (app (mock/request :get "/api/tipos_alimento/9999999"))]
      (is (= (:status response) 404))))
  
  (testing "invalid route"
    (let [response (app (mock/request :get "/api/tipos_alimento/invalid"))]
      (is (= (:status response) 400)))))
