(ns aoc.2024.day4.part1
  (:require [clojure.string :as string]
            [clojure.core.matrix :as matrix]
            [aoc.2024.day4.common  :as c]
            [aoc.utils :refer [parse-matrix]]))

(defn count-row [row]
  (->> (string/join row)
       (re-seq #"X(?=MAS)|S(?=AMX)")
       (count)))

(defn count-occurrences [data]
  (->> (map count-row data)
       (reduce + 0)))

(defn horizontal [{:keys [data]}]
  data)

(defn vertical [{:keys [data]}]
  (matrix/transpose data))

(defn run []
  (->> (slurp "inputs/day4.txt")
       (parse-matrix)
       ((juxt horizontal vertical c/transform-rh-diag c/transform-lh-diag))
       (map count-occurrences)
       (reduce + 0)))