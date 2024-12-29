(ns aoc.2024.day7
  (:require [clojure.string :as string]
            [clojure.math :as math]))

(defn parse []
  (->> (slurp "inputs/day7.txt")
       (string/split-lines)
       (map #(-> (string/split % #": ")
                 (update 1 string/split #"\s+")
                 (as-> $ (->> (apply cons $)
                              (map parse-long)))))))

(defn cutoff [^long number ^long suffix]
  (let [d (long (math/pow 10 (inc (int (math/log10 suffix)))))]
    (when (= suffix (rem number d))
      (quot number d))))

; optimization inspired by https://github.com/erdos/advent-of-code/blob/master/2024/day07_numeric.clj
(defn solve [operations ^long answer [^long n & n*]]
  (if (seq n*)
    (some (fn [f] (f operations answer n n*)) operations)
    (= answer n)))

(defn verify [operations [answer & operands]]
  (if (solve operations answer (reverse operands))
    answer
    0))

(defn process [operations]
  (->> (parse)
       (pmap (partial verify operations))
       (reduce + 0)))

(defn addition [operations answer n n*]
  (when (>= answer n) (solve operations (- answer n) n*)))

(defn multiplication [operations answer n n*]
  (when (zero? (rem answer n)) (solve operations (quot answer n) n*)))

(defn concatenation [operations answer n n*]
  (some-> (cutoff answer n) long (as-> $ (solve operations $ n*))))

(defn part1 []
  (process [addition multiplication]))

(defn part2 []
  (process [addition multiplication concatenation]))