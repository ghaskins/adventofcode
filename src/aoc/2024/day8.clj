(ns aoc.2024.day8
  (:require [medley.core :as m]
            [clojure.math.combinatorics :as combo]
            [aoc.utils :refer [parse-matrix index-elements out-of-bounds?] :as utils]))

(defn group-frequencies [{:keys [data]}]
  (->> (apply concat (index-elements data))
       (filter #(->> % :element str (re-matches #"[0-9a-zA-Z]") some?))
       (group-by :element)
       (m/map-vals #(map :pos %))))

(defn parse []
  (-> (slurp "inputs/day8.txt")
      (parse-matrix)
      (as-> $ (assoc $ :frequencies (group-frequencies $)))))

(defn compute-single-antinode [[a b]]
  (let [delta (map - a b)]
    (vector (map + delta a) (map - b delta))))

(defn part1 []
  (let [{:keys [frequencies] :as input} (parse)]
    (->> frequencies
         (mapcat (comp #(combo/combinations % 2) second))
         (mapcat compute-single-antinode)
         (remove #(out-of-bounds? input %))
         (distinct)
         (count))))

(defn inbounds? [input pos]
  (not (out-of-bounds? input pos)))

(defn iterate-inbounds [input f x]
  (take-while #(inbounds? input %) (iterate f x)))

(defn compute-all-antinodes [input [a b]]
  (let [delta (map - a b)]
    (concat (iterate-inbounds input #(map + delta %) a)
            (iterate-inbounds input #(map - % delta) b))))

(defn part2 []
  (let [{:keys [frequencies] :as input} (parse)]
    (->> frequencies
         (mapcat (comp #(combo/combinations % 2) second))
         (mapcat #(compute-all-antinodes input %))
         (distinct)
         (count))))