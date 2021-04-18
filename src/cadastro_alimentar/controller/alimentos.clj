(ns cadastro-alimentar.controller.alimentos
  (:require [clojure.tools.logging :as log]
            [ring.util.http-response :refer [ok bad-request unauthorized internal-server-error no-content created not-found]]
            [cadastro-alimentar.db.alimentos :as db.alimentos]
            [cadastro-alimentar.db :as db]))

(defn get-all
  []
  (let [result  (db.alimentos/get-all)]
    (if (= (count result) 0)
      ()
    (do
      (log/info "Encontrado" (count result) "registro(s)")
      result))))

(defn by-uuid
  [uuid]
  (let [result  (db.alimentos/by-uuid uuid)]
    (if (= (count result) 0)
      ()
      (do
        (log/info "Encontrado" (count result) "registro(s) com uuid: " uuid)
        result))))
  