(ns cadastro-alimentar.controller-test
  (:require [clojure.test :refer :all]
            [cheshire.core :as json]
            [cadastro-alimentar.controller.refeicoes :as controller.refeicoes]
            [cadastro-alimentar.controller.alimentos :as controller.alimentos]
            [cadastro-alimentar.controller.tipos-alimentos :as controller.tipos-alimentos]
            [cadastro-alimentar.utils.dates :as utils.dates]
            [cadastro-alimentar.utils.uuids :as utils.uuids]))

(def tipo-alimento-teste1 {:uuid (utils.uuids/uuid) :descricao "Alimento Teste Update"})

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

  (testing "should not delete a tipo-alimento by uuid"
    (is (thrown-with-msg? Exception #"tipo-alimento nao encontrado: Alimento Teste Update" (:processed (controller.tipos-alimentos/delete-by-descricao "Alimento Teste Update"))))))