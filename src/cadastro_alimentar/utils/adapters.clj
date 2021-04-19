(ns cadastro-alimentar.utils.adapters
  (:require [cheshire.core :as json]))

(defn json->map
  [data]
    (json/parse-string data #(keyword %)))