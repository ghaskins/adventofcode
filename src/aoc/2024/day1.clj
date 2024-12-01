(ns aoc.2024.day1
  (:require [clojure.string :as string]))

(defn parse []
  (->> (slurp "inputs/day1.txt")
       (string/split-lines)
       (map #(map parse-long (string/split % #"\s+")))
       (reduce (fn [acc items] (mapv conj acc items)) [[] []])
       (map sort)))

(defn part1 []
  (let [[left right] (parse)]
    (->> (map (comp abs -) left right)
         (reduce + 0))))

(defn part2 []
  (let [[left right] (parse)
        f (frequencies right)]
    (->> (map #(* % (get f % 0)) left)
         (reduce + 0))))
