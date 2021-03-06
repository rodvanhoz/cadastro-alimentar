(ns cadastro-alimentar.controller.refeicoes
  (:require [clojure.tools.logging :as log]
            [ring.util.http-response :refer [conflict]]
            [cadastro-alimentar.db.refeicoes :as db.refeicoes]
            [cadastro-alimentar.logic.refeicoes :as logic.refeicoes]
            [cadastro-alimentar.controller.pesos-alimentos :as controller.pesos-alimentos]
            [cadastro-alimentar.logic.pesos-alimentos :as logic.pesos-alimentos]
            [cadastro-alimentar.db :as db]
            [cadastro-alimentar.utils.dates :as utils.dates]))

(defn get-all
  []
  (let [result  (db.refeicoes/get-all)]
    (if (= (count result) 0)
      ()
    (do
      (log/info "Encontrado" (count result) "registro(s)")
      result))))

(defn by-clauses
  [clauses]
  (let [result (db.refeicoes/get clauses)]
    (if (= (count result) 0)
      ()
    (do
      (log/info "Encontrado" (count result) "registro(s)")
      result))))

(defn by-uuid
  [refeicao-uuid]
  (let [result  (db.refeicoes/by-uuid refeicao-uuid)]
    (if (= (count result) 0)
      ()
      (do
        (log/info "Encontrado" (count result) "registro(s) com uuid:" refeicao-uuid)
        result))))

(defn by-descricao
  [descricao]
  (let [result  (db.refeicoes/by-descricao descricao)]
    (if (= (count result) 0)
      ()
      (do
        (log/info "Encontrado" (count result) "registro(s) com descricao:" descricao)
        result))))

(defn create-refeicao
  [refeicao]
  (let [refeicao-builder (logic.refeicoes/build-refeicao refeicao)]
    (if (= (count (by-uuid (:uuid refeicao-builder))) 0)
      (do
        (db.refeicoes/insert! refeicao-builder)
        (log/info "refeicao criado: " refeicao-builder)
        (-> refeicao-builder
            list))
      (throw (Exception. (str "refeicao ja existe: " (:uuid refeicao-builder)))))))

(defn update-refeicao
  [refeicao-uuid refeicao-fields]
  (let [where-clauses (logic.refeicoes/build-where-clauses {:uuid refeicao-uuid})
        field-clauses (logic.refeicoes/build-fields-clauses refeicao-fields)
        ref (by-uuid refeicao-uuid)]
    (if (= (count ref) 1)
      (do 
        (db.refeicoes/update! field-clauses where-clauses)
        (log/info "refeicao atualizado com sucesso: " (first ref))
        (by-uuid refeicao-uuid))
      (do
        (throw (Exception. (str "UPDATE - refeicao nao encontrado: " refeicao-uuid)))))))

(defn delete-by-uuid
  [refeicao-uuid]
  (if (= (count (by-uuid refeicao-uuid)) 0)
    (throw (Exception. (str "refeicao nao encontrado: " refeicao-uuid)))
    (do
      (let [pesos-alimentos (controller.pesos-alimentos/by-refeicao refeicao-uuid)]
        (if (> (count pesos-alimentos) 0)
          (controller.pesos-alimentos/delete-by-refeicao refeicao-uuid)))
      (db.refeicoes/delete-by-uuid refeicao-uuid)
      (log/info "DELETE - refeicao deletado: " refeicao-uuid)
      true)))

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
      (throw (Exception. (str "refeicao nao encontrado na data: " date)))
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

(defn insert-complete-refeicao
  [complete-refeicao]
  (if (logic.refeicoes/valid? complete-refeicao)
    (do
      (log/info "-- refeicao-completa aceita --")
      (let [refeicao-builder (logic.refeicoes/build-refeicao (:refeicao complete-refeicao))
            pesos-alimentos-builder (logic.pesos-alimentos/build-pesos-alimentos (:alimentos complete-refeicao) (:uuid refeicao-builder))
            refeicao-inserted (create-refeicao refeicao-builder)
            pesos-alimentos-inserted (controller.pesos-alimentos/create-pesos-alimentos-by-list pesos-alimentos-builder)]
        (if (and (= (count refeicao-inserted) 1) (= (count pesos-alimentos-builder) (count pesos-alimentos-inserted)))
          (do
            (let [complete-refeicao-builder (logic.refeicoes/complete-refeicoes-build refeicao-inserted pesos-alimentos-inserted)]
              (log/info "Refeicao completa inserida: " (str (:uuid (first refeicao-builder))))
              complete-refeicao-builder))
          (throw (Exception. (str "INSERT - Erro ao inserir uma refeicao completa: " complete-refeicao))))))
    (throw (Exception. (str "INSERT - refeicao completa nao e valida: " complete-refeicao)))))