(defproject cadastro-alimentar "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [compojure "1.6.1"]
                 [ring/ring-defaults "0.3.2"]
                 [org.clojure/tools.logging "1.1.0"]
                 [ring/ring-json "0.4.0"]
                 [korma "0.4.3"]
                 [org.postgresql/postgresql "42.1.4"]
                 [metosin/ring-http-response "0.9.0"]]
  :plugins [[lein-ring "0.12.5"]]
  :ring {:handler cadastro-alimentar.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.2"]]}})
