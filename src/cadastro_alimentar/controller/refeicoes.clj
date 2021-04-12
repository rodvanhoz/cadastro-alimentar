(ns cadastro-alimentar.controller.refeicoes
  (:require [clojure.tools.logging :as log]
            [ring.util.http-response :refer [ok bad-request unauthorized internal-server-error no-content created not-found]]
            [cadastro-alimentar.db.refeicoes :as db.refeicoes]
            [cadastro-alimentar.db :as db]
            [cadastro-alimentar.utils.dates :as utils.dates]))

(defn get-all
  []
  (let [result  (db.refeicoes/get-all)]
    (if (= (count result) 0)
      (not-found)
    (do
      (log/info "Encontrado" (count result) "registro(s)")
      result))))

(defn by-uuid
  [uuid]
  (let [result  (db.refeicoes/by-uuid uuid)]
    (if (= (count result) 0)
      (not-found)
      (do
        (log/info "Encontrado" (count result) "registro(s) com uuid:" uuid)
        result))))

(defn calcule-macros-of-refeicoes
  [refeicoes]
    (let [kcal (reduce + (doall (map #(-> (* (:qtde-carbo %) 4)
                                          (* (:qtde-prot %) 4)
                                          (* (:qtde-gordura %) 9)) 
                                  refeicoes)))
          peso (reduce + (doall (map #(:peso %) refeicoes)))]
      {:kcal kcal :peso peso}))

(defn get-all-refeicoes-by-date
  [date]
  (let [result (db.refeicoes/get-all-refeicoes-by-date date)]
    (if (= (count result) 0)
      (not-found)
      (do
        (log/info "Encontrado" (count result) "registro(s) ccom data:" date)
        result))))
  
(defn get-all-refeicoes-by-date-with-calculated-macros
  [date]
  (let [refeicoes (get-all-refeicoes-by-date date)
        calculated-macros (calcule-macros-of-refeicoes refeicoes)]
    (-> {}
        (assoc :date (utils.dates/str->date date))
        (assoc :refeicoes refeicoes)
        (assoc :calculated-macros calculated-macros)
        list)))