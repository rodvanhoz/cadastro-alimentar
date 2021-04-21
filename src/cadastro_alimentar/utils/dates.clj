(ns cadastro-alimentar.utils.dates
  (:use [clojure.string :only (join split blank?)])
  (:require [clj-time.coerce :as c]
            [clj-time.core :as t]
            [clj-time.format :as f]
            [clojure.string :as str]
            [clojure.spec.alpha :as s]
            [clj-time.predicates :as pr]
            [clj-time.local :as l])
  (:import (java.util Properties)))

(defn now [] (c/to-sql-time (t/now)))
(defn get-utc [] 3)

(defn from-sql-date
  [date]
  (c/from-sql-date date))

(defn string->to-local-date-time
  [date]
  (l/to-local-date-time date))

(defn format-date-with-bar
  [date]
  (f/unparse (f/formatter "dd/MM/yyyy") (c/from-date date)))

(defn format-date-without-bar
  [date]
  (f/unparse (f/formatter "ddMMyy") (c/from-date date)))

(defn format-date-with-bar-and-hour
  [date]
  (f/unparse (f/formatter "dd/MM/yyyy hh:mm:ss") (c/from-date date)))

(defn start-of-day
  [date]
  (t/to-time-zone (t/with-time-at-start-of-day (c/to-date-time date)) (t/default-time-zone)))

(defn str->sql-date-start-of-day
  [date]
  (c/to-timestamp (start-of-day date)))

(defn str->sql-date-end-of-day
  [date]
  (c/to-timestamp (t/plus (start-of-day date) (t/hours 23) (t/minutes 59) (t/seconds 59))))

(defn str->date
  [date]
  (c/to-timestamp date))

(defn str->date-with-utc
  [date]
  (c/to-timestamp (t/plus date (t/hours (get-utc)))))

(defn java-date-now []
  (new java.util.Date))

(defn clj-time-now []
  (t/now))

(defn string->java-date
  [date]
  (.parse (java.text.SimpleDateFormat. "yyyy-MM-dd") date))

(defn java-date->string
  [date]
  (.format (java.text.SimpleDateFormat. "yyyy-MM-dd") date))

(defn iso-8601-string-date->formatted-date-with-bar
  [iso-date]
  (f/unparse (f/formatter "dd/MM/yyyy HH:mm:ss") (c/from-string iso-date)))

(defn iso-8601-string-date->joda-date-time-with-utc
  [iso-date]
  (c/to-timestamp (t/plus (c/to-date-time iso-date) (t/hours (get-utc)))))
  
(defn from-date
  [date]
  (c/from-date date))

(defn create-date-with-no-time
  [date]
  (t/date-time (t/year date) (t/month date) (t/day date)))
  