(ns aoc.2024.day10
  (:require [medley.core :as m]
            [aoc.utils :refer [parse-matrix index-elements out-of-bounds?] :as utils]))

(defn parse []
  (->> (slurp "inputs/day10.txt")
       (parse-matrix)
       :data
       index-elements
       (apply concat)
       (map (fn [x] (update x :element (comp parse-long str))))
       (m/index-by :pos)
       (m/map-vals :element)))

(defn find-neighbors [data depth [x y :as pos]]
  (filter #(->> % (get data) (= depth)) [[x (dec y)] [(inc x) y] [x (inc y)] [(dec x) y]]))

(defn solve [data distinct? depth candidates]
  (let [next-depth (inc depth)]
    (if (< depth 9)
      (solve data distinct? next-depth
             (-> (mapcat (partial find-neighbors data next-depth) candidates)
                 (cond-> distinct? distinct)))
      (count candidates))))

(defn process [distinct?]
  (let [data (parse)]
    (->> (map #(solve data distinct? 0 [%]) (keys (m/filter-vals zero? data)))
         (reduce + 0))))

(defn part1 []
  (process true))

(defn part2 []
  (process false))