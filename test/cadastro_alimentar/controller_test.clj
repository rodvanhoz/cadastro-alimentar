(ns cadastro-alimentar.controller-test
  (:require [clojure.test :refer :all]
            [cheshire.core :as json]
            [cadastro-alimentar.controller.refeicoes :as controller.refeicoes]
            [cadastro-alimentar.controller.alimentos :as controller.alimentos]
            [cadastro-alimentar.utils.dates :as utils.dates]
            [cadastro-alimentar.utils.uuids :as utils.uuids]))

(deftest should-calculate-macros 
  (testing "should calculate macros based in information of refeicoes"
    (let [refeicoes (controller.refeicoes/get-all-refeicoes-by-date-with-calculated-macros "2020-07-24")]
      (is (= (count (:refeicoes (first refeicoes))) 5))
      (is (= (:date (first refeicoes)) (utils.dates/str->date "2020-07-24"))))))

(deftest should-get-alimento
  (testing "should calculate macros based in information of refeicoes"
    (let [alimento (first (controller.alimentos/by-uuid "1864c9a4-2dac-48d6-9a9f-12cc91b1cb50"))]
      (is (= (str (:uuid alimento)) "1864c9a4-2dac-48d6-9a9f-12cc91b1cb50")))))

;(deftest shold-create-tipo-alimento)