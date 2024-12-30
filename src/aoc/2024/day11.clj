(ns aoc.2024.day11
  (:require [clojure.string :as string]
            [medley.core :as m]))

(defn parse []
  (-> (slurp "inputs/day11.txt")
      (string/split #"\s+")
      (as-> $ (mapv parse-long $))))

(defn even-digits? [stone]
  (-> stone str count even?))

(defn split-stone [stone]
  (let [s (str stone)
        l (count s)
        h (/ l 2)]
    (->> [(subs s 0 h) (subs s h l)]
         (map parse-long)
         (frequencies))))

(defn step [stone]
  (cond
    (= stone 0) {1 1}
    (even-digits? stone) (split-stone stone)
    :default {(* stone 2024) 1}))

(defn solve [stones]
  (reduce (fn [acc [stone freq]]
            (merge-with + acc (m/map-vals #(* % freq) (step stone))))
          {}
          stones))

(defn process [blinks]
  (-> (parse)
      (frequencies)
      (->> (iterate solve))
      (nth blinks)
      (vals)
      (->> (reduce + 0))))

(defn part1 []
  (process 25))

(defn part2 []
  (process 75))


