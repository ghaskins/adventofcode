(ns aoc.2024.day7
  (:require [clojure.string :as string]
            [clojure.math.combinatorics :as combo]))

(defn parse []
  (->> (slurp "inputs/day7.txt")
       (string/split-lines)
       (map #(-> (string/split % #": ")
                 (update 1 string/split #"\s+")
                 (as-> $ (->> (apply cons $)
                              (map parse-long)))))))

(defn compute [operands operators]
  (->> (map vector operators (rest operands))
       (reduce (fn [acc [operator operand]]
                 (operator acc operand))
               (first operands))))

(defn verify [operators [answer & operands]]
  (let [operator-sets (combo/selections operators (dec (count operands)))]
    (if (some #(= answer (compute operands %)) operator-sets)
      answer
      0)))

(defn process [operators]
  (->> (parse)
       (pmap (partial verify operators))
       (reduce + 0)))

(defn part1 []
  (process [+ *]))

(defn concatenation [a b]
  (parse-long (str a b)))

(defn part2 []
  (process [+ * concatenation]))