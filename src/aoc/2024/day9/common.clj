(ns aoc.2024.day9.common)

(defn parse []
  (->> (slurp "inputs/day9.txt")
       (seq)
       (map (comp parse-long str))
       (partition-all 2)))
