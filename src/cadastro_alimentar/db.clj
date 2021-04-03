(ns cadastro-alimentar.db
  (:require [korma.db :as korma :refer [defdb]]))

  (def pg-uri
    {:connection-uri (str "postgresql://ieyunibbownizx:b7451ea78b286715bb4e0218544118d8be0c1d6118106a90c8713425c8514650@ec2-23-21-229-200.compute-1.amazonaws.com:5432/df7qbjv2f489d3"
                          "?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory")})
  
  ;; (def db {:dbtype "postgresql"
  ;;   :dbname "cadastro_alimentar"
  ;;   :host "192.168.0.11"
  ;;   :port 5432
  ;;   :user "postgres"
  ;;   :password "Geforce560ti"
  ;;   :ssl false
  ;;   ;:sslfactory "org.postgresql.ssl.NonValidatingFactory"
  ;;   })
  
(defn- db-conn []
  (korma/postgres
      {:db "cadastro_alimentar" :user "postgres" :password "Geforce560ti" :host "192.168.0.11" :port "5432"}))

(defdb db (db-conn))