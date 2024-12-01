(ns aoc.2024.day1
  (:require [clojure.string :as string]))

(defn parse []
  (->> (slurp "inputs/day1.txt")
       (string/split-lines)
       (map #(map parse-long (string/split % #"\s+")))
       (reduce (fn [[lacc racc] [left right]] [(conj lacc left) (conj racc right)]) [[] []])
       (map sort)))

(defn part1 []
  (let [[left right] (parse)]
    (->> (zipmap left right)
         (map (fn [[left right]]
                (abs (- left right))))
         (reduce + 0))))

(defn part2 []
  (let [[left right] (parse)]
    (let [f (frequencies right)]
      (->> (map (fn [l] (* l (get f l 0))) left)
           (reduce + 0)))))
