(ns cadastro-alimentar.utils.dates
  (:use [clojure.string :only (join split blank?)])
  (:require [clj-time.coerce :as c]
            [clj-time.core :as t]
            [clj-time.format :as f]
            [clojure.string :as str]
            [clojure.spec.alpha :as s]
            [clj-time.predicates :as pr])
  (:import (java.util Properties)))

(defn now [] (c/to-sql-time (t/now)))

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

(defn java-date-now []
  (new java.util.Date))

(defn clj-time-now []
  (t/now))
  