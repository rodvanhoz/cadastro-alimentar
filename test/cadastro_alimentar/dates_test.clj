  
(ns cadastro-alimentar.dates-test
  (:require [clojure.test :refer :all]
            [clojure.tools.logging :as log]
            [cadastro-alimentar.utils.dates :refer :all]
            [clj-time.core :as t]
            [clj-time.coerce :as c]
            [clj-time.predicates :as pr]))
  
(deftest tenting-dates
  (testing "should test convert start-of-day"
    (let [date (c/from-sql-date (str->sql-date-start-of-day "2017-01-05 19:23:24"))]
      (is (= 2017 (t/year date)))
      (is (= 1 (t/month date)))
      (is (= 0 (t/hour date)))
      (is (= 0 (t/minute date)))
      (is (= 0 (t/second date)))))

  (testing "should test convert end-of-day"
    (let [date (c/from-sql-date (str->sql-date-end-of-day "2017-01-05 19:23:24"))]
      (is (= 2017 (t/year date)))
      (is (= 1 (t/month date)))
      (is (= 23 (t/hour date)))
      (is (= 59 (t/minute date)))
      (is (= 59 (t/second date)))))
      
  (testing "should convert a string date to java date"
    (let [date (c/from-sql-date (string->java-date "2017-01-05 19:23:24"))]
      (log/info "string->java-date -- " date)
      (is (= 2017 (t/year date)))
      (is (= 1 (t/month date)))
      (is (= 5 (t/day date)))
      (is (= 2 (t/hour date)))
      (is (= 0 (t/minute date)))
      (is (= 0 (t/second date)))))
      
  (testing "should iso 8601 string date to formated date with bar"
    (let [date (iso-8601-string-date->formatted-date-with-bar "2017-01-05 19:23:24")]
      (log/info "iso-8601-string-date->formatted-date-with-bar -- " date)
      (is (= date "05/01/2017 19:23:24"))))
      
  (testing "should convert from iso sql date"
    (let [date (string->to-local-date-time "2021-04-10T15:34:40.000000000-00:00")]
      (prn "######" date))))
  
  
