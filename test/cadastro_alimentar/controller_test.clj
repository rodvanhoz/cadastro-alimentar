(ns cadastro-alimentar.controller-test
  (:require [clojure.test :refer :all]
            [cheshire.core :as json]
            [cadastro-alimentar.controller.refeicoes :as controller.refeicoes]
            [cadastro-alimentar.controller.alimentos :as controller.alimentos]
            [cadastro-alimentar.controller.tipos-alimentos :as controller.tipos-alimentos]
            [cadastro-alimentar.utils.dates :as utils.dates]
            [cadastro-alimentar.utils.uuids :as utils.uuids]))

(def tipo-alimento-teste1 {:uuid (utils.uuids/uuid) :descricao "Alimento Teste Update"})
(def refeicao-teste {:uuid "1478fa0b-d996-489e-a1f0-649b0d28d7ee" :descricao "refeicao teste unitario"})

(deftest should-calculate-macros 
  (testing "should calculate macros based in information of refeicoes"
    (let [refeicoes (controller.refeicoes/get-all-refeicoes-by-date-with-calculated-macros "2020-07-24")]
      (is (= (count (:refeicoes (first refeicoes))) 5))
      (is (= (:date (first refeicoes)) (utils.dates/str->date "2020-07-24"))))))

(deftest testing-alimentos
  (testing "should calculate macros based in information of refeicoes"
    (let [alimento (first (controller.alimentos/by-uuid "1864c9a4-2dac-48d6-9a9f-12cc91b1cb50"))]
      (is (= (str (:uuid alimento)) "1864c9a4-2dac-48d6-9a9f-12cc91b1cb50")))))

(deftest testing-tipo-alimento
  (testing "should create a tipo-alimento"
    (let [tipo-alimento (controller.tipos-alimentos/create-tipo-alimento "Alimento Teste")
          tipo-alimento-saved (controller.tipos-alimentos/by-descricao "Alimento Teste")]
      (is (= (str (:descricao tipo-alimento-saved) "Alimento Teste")))))

  (testing "should not create a tipo-alimento when it exists"
      (is (thrown-with-msg? Exception #"tipo-alimento ja existe: Alimento Teste" (:processed (controller.tipos-alimentos/create-tipo-alimento "Alimento Teste")))))

  (testing "should update a tipo-alimento"
    (let [tipo-alimento-teste (first (controller.tipos-alimentos/by-descricao "Alimento Teste"))
          tipo-alimento (-> {}
                            (assoc :uuid (:uuid tipo-alimento-teste))
                            (assoc :descricao "Alimento Teste Update"))]
      (controller.tipos-alimentos/update-tipo-alimento tipo-alimento)
      (let [tipo-alimento-saved (controller.tipos-alimentos/by-descricao "Alimento Teste Update")]
        (is (= (str (:descricao tipo-alimento-saved) "Alimento Teste Update"))))))
      

  (testing "should not update a tipo-alimento when not it exists"
      (is (thrown-with-msg? Exception #"tipo-alimento nao encontrado: Alimento Teste" (:processed (controller.tipos-alimentos/update-tipo-alimento tipo-alimento-teste1)))))
      
  (testing "should not delete a tipo-alimento by uuid"
    (is (= (controller.tipos-alimentos/delete-by-descricao "Alimento Teste Update"))))

  (testing "should not delete a tipo-alimento by uuid when it not exists"
    (is (thrown-with-msg? Exception #"tipo-alimento nao encontrado: Alimento Teste Update" (:processed (controller.tipos-alimentos/delete-by-descricao "Alimento Teste Update"))))))

(deftest testing-refeicoes
  (testing "should create a refeicao"
    (let [refeicao (controller.refeicoes/create-refeicao refeicao-teste)]
      (is (= (count refeicao) 1))))
      
  (testing "should not create a refeicao when it exists"
    (is (thrown-with-msg? Exception #"refeicao ja existe: 1478fa0b-d996-489e-a1f0-649b0d28d7ee" (:processed (controller.refeicoes/create-refeicao refeicao-teste)))))
    
  (testing "should update a refeicao"
    (let [result (controller.refeicoes/update-refeicao (:uuid refeicao-teste) {:descricao "refeicao teste unitario update"})]
      (is (= (count result) 1))
      (is (= (:descricao (first result)) "refeicao teste unitario update"))))
  
  (testing "should not update refeicao when it not exists"
    (is (thrown-with-msg? Exception #"UPDATE - refeicao nao encontrado: 7e827d74-d02b-4371-8b9f-435aefde96d5" (controller.refeicoes/update-refeicao "7e827d74-d02b-4371-8b9f-435aefde96d5" {:descricao "refeicao teste unitario update"}))))
    
  (testing "should not delete a refeicoes by uuid"
    (is (= (controller.refeicoes/delete-by-uuid (:uuid refeicao-teste)))))

  (testing "should not delete a refeicoes by uuid when it not exists"
    (is (thrown-with-msg? Exception #"refeicao nao encontrado: 1478fa0b-d996-489e-a1f0-649b0d28d7ee" (:processed (controller.refeicoes/delete-by-uuid (:uuid refeicao-teste)))))))