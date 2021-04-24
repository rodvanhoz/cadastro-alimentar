(ns cadastro-alimentar.controller-test
  (:require [clojure.test :refer :all]
            [cheshire.core :as json]
            [cadastro-alimentar.controller.refeicoes :as controller.refeicoes]
            [cadastro-alimentar.controller.alimentos :as controller.alimentos]
            [cadastro-alimentar.controller.tipos-alimentos :as controller.tipos-alimentos]
            [cadastro-alimentar.utils.dates :as utils.dates]
            [cadastro-alimentar.utils.uuids :as utils.uuids]))

(def tipo-alimento-teste {:uuid "6c7ef568-be99-4093-a3a6-ecfcfe13fd3a" :descricao "Alimento Teste"})
(def refeicao-teste {:uuid "1478fa0b-d996-489e-a1f0-649b0d28d7ee" :descricao "refeicao teste unitario"})
(def alimento-teste {:uuid "31dbcee6-9e58-4ee8-ab52-d0d2e5d9d3d8" :nome "alimento teste" :peso 1 :qtde-carboidrato 0.281 :qtde-gorduras 0.002 :qtde-proteinas 0.025 :tipo-alimento-uuid "5e684cd9-ab77-4bcb-96f5-a3e46d82c454"})

(def complete-refeicao-teste {:alimentos (list {:uuid "1864c9a4-2dac-48d6-9a9f-12cc91b1cb50" :peso 150.0}
                                               {:uuid "057df894-5727-4789-898b-b4bcfe07e5d5" :peso 150.0}
                                               {:uuid "54b9ba0d-4429-4ffa-9298-e242ec3c79d4" :peso 170.0}
                                               {:uuid "e3265510-87da-4a88-a610-ea18620e7802" :peso 100.0})
                              :refeicao {:uuid "d6e3ccca-cb0f-4fa4-a0ea-92576407f998" :moment "2021-04-10T15:34:40Z" :descricao "Refeicao Teste"}})

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
    (let [tipo-alimento (first (controller.tipos-alimentos/create-tipo-alimento tipo-alimento-teste))
          tipo-alimento-saved (controller.tipos-alimentos/by-descricao "Alimento Teste")]
      (is (= (str (:descricao tipo-alimento-saved) "Alimento Teste")))
      (is (= (str (:descricao tipo-alimento) "Alimento Teste")))
      (is (= (str (:uuid tipo-alimento-saved) "6c7ef568-be99-4093-a3a6-ecfcfe13fd3a")))
      (is (= (str (:uuid tipo-alimento)) "6c7ef568-be99-4093-a3a6-ecfcfe13fd3a"))))

  (testing "should not create a tipo-alimento when it exists"
      (is (thrown-with-msg? Exception #"tipo-alimento ja existe: Alimento Teste" (:processed (controller.tipos-alimentos/create-tipo-alimento tipo-alimento-teste)))))

  (testing "should update a tipo-alimento"
    (let [tipo-alimento (-> {}
                            (assoc :uuid (:uuid tipo-alimento-teste))
                            (assoc :descricao "Alimento Teste Update"))]
      (controller.tipos-alimentos/update-tipo-alimento (:uuid tipo-alimento-teste) 
                                                       {:descricao "tipo alimento teste update"})
      (let [tipo-alimento-saved (controller.tipos-alimentos/by-descricao "Alimento Teste Update")]
        (is (= (str (:descricao tipo-alimento-saved) "Alimento Teste Update"))))))
      

  (testing "should not update a tipo-alimento when not it exists"
      (is (thrown-with-msg? Exception #"tipo-alimento nao encontrado: tipo alimento teste update" (:processed (controller.tipos-alimentos/update-tipo-alimento "3158db7c-9f7f-4857-aa0c-3afc2ee35520" 
                                                                                                                                                    {:descricao "tipo alimento teste update"})))))
      
  (testing "should not delete a tipo-alimento by descricao"
    (is (= (controller.tipos-alimentos/delete-by-descricao "tipo alimento teste update"))))

  (testing "should not delete a tipo-alimento by descricao when it not exists"
    (is (thrown-with-msg? Exception #"tipo-alimento nao encontrado: tipo alimento teste update" (:processed (controller.tipos-alimentos/delete-by-descricao "tipo alimento teste update")))))
    
  (testing "should delete tipo-alimento by uuid"
    (let [tipo-alimento-created (controller.tipos-alimentos/create-tipo-alimento tipo-alimento-teste)]
      (is (controller.tipos-alimentos/delete-by-uuid "6c7ef568-be99-4093-a3a6-ecfcfe13fd3a"))))
      
  (testing "should not delete a tipo-alimento by uuid when it not exists"
    (is (thrown-with-msg? Exception #"tipo-alimento nao encontrado: 6c7ef568-be99-4093-a3a6-ecfcfe13fd3a" (:processed (controller.tipos-alimentos/delete-by-descricao "6c7ef568-be99-4093-a3a6-ecfcfe13fd3a"))))))

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

(deftest testing-complete-refeicoes
  (testing "should insert a new complete refeicao"
    (let [result (controller.refeicoes/insert-complete-refeicao complete-refeicao-teste)]
      (is (= (count result) 2))
      
      (let [alimentos (:alimentos result)
            refeicao (:refeicao result)]
        (is (= (count alimentos) 4))
        (is (= (str (:uuid refeicao)) "d6e3ccca-cb0f-4fa4-a0ea-92576407f998"))
        (is (= (str (utils.dates/str->date (:moment refeicao))) "2021-04-10 12:34:40.0"))
        (is (= (str (:descricao refeicao)) "Refeicao Teste")))))
  
  (testing "should get a complete refeicao"
    (let [result (controller.refeicoes/get-all-refeicoes-by-date-with-calculated-macros "2021-04-10")]
      (is (= (count result) 1))
      (is (= (:date (first result)) (utils.dates/str->date "2021-04-10")))
      
      (let [calculated-macros (:calculated-macros (first result))
            refeicoes (:refeicoes (first result))]
        (is (= (count refeicoes) 4))
        (is (= (str (:kcal calculated-macros)) "0.033737760000000006"))
        (is (= (str (:peso calculated-macros)) "570.0")))))

  (testing "shoul delete a refeicao and yours pesos-alimentos by refeicao-uuid"
    (is (= (controller.refeicoes/delete-by-uuid "d6e3ccca-cb0f-4fa4-a0ea-92576407f998")))))

(deftest testing-alimentos
  (testing "should create a alimento"
    (let [alimento (controller.alimentos/create-alimento alimento-teste)]
      (is (= (count alimento) 1))))
      
  (testing "should not create a alimento when if not exists"
    (is (thrown-with-msg? Exception #"alimento ja existe: 31dbcee6-9e58-4ee8-ab52-d0d2e5d9d3d8" (:processed (controller.alimentos/create-alimento alimento-teste)))))
          
  (testing "should update a alimento"
    (let [result (controller.alimentos/update-alimento (:uuid alimento-teste) {:nome "alimento teste update" :qtde-carboidrato 0.555 :qtde-gorduras 0.222 :qtde-proteinas 0.1})]
      (is (= (count result) 1))
      (is (= (:nome (first result)) "alimento teste update"))
      (is (= (:qtde-carboidrato (first result))  0.555))
      (is (= (:qtde-gordduras (first result)) 0.222))
      (is (= (:qtde-proteinas (first result)) 0.1))))

  (testing "should not update alimento when it not exists"
    (is (thrown-with-msg? Exception #"UPDATE - alimento nao encontrado: d9619362-b603-4b2f-8c9c-9ff5f2c70478" 
                          (controller.alimentos/update-alimento "d9619362-b603-4b2f-8c9c-9ff5f2c70478" 
                                                                {:nome "alimento teste update" :qtde-carboidrato 0.555 :qtde-gorduras 0.222 :qtde-proteinas 0.1}))))

  (testing "should not delete a alimento by uuid"
    (is (= (controller.alimentos/delete-by-uuid (:uuid alimento-teste)))))

  (testing "should not delete a alimento by uuid when it not exists"
    (is (thrown-with-msg? Exception #"alimento nao encontrado: 31dbcee6-9e58-4ee8-ab52-d0d2e5d9d3d8" (:processed (controller.alimentos/delete-by-uuid (:uuid alimento-teste)))))))