(ns cadastro-alimentar.adapters-test
  (:require [clojure.test :refer :all]
            [cheshire.core :as json]
            [cadastro-alimentar.utils.adapters :as utils.adapters]))

(def json-test "{\"uuid\": \"a3770a85-eb2a-4994-8502-fa8ebaea9fa3\", \"descricao\": \"Alimento Teste\"}")

(deftest testing-json-converter
  (testing "should converter json to map"
    (let [result (utils.adapters/json->map json-test)]
      (is (= (:uuid "a3770a85-eb2a-4994-8502-fa8ebaea9fa3"))
      (is (= (:descricao "Alimento Teste")))))))