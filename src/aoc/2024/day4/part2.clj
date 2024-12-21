(ns aoc.2024.day4.part2
  (:require [clojure.string :as string]
            [clojure.data :refer [diff]]
            [aoc.2024.day4.common :refer [parse] :as c]
            [aoc.utils :as utils]))

(defn find-occurrences [row]
  (utils/find-occurrences #"M(?=AS)|S(?=AM)" (string/join row)))

(defn analyze-row [row]
  (let [positions (mapv :pos row)
        occurrences (->> (mapv :element row)
                         (find-occurrences))]
    (map #(get positions %) occurrences)))

(defn remove-nils [coll]
  (map #(remove nil? %) coll))

(defn mapv-indexed [f coll]
  (vec (map-indexed f coll)))

(defn index-elements [data]
  (mapv-indexed (fn [i row]
                  (mapv-indexed (fn [j x] {:pos [i j] :element x}) row))
                data))

(defn find-mas [data]
  (->> (map analyze-row data)
       (reduce (fn [acc coll] (reduce conj acc coll)) #{})))

(defn run []
  (-> (slurp "inputs/day4.txt")
      (parse)
      (update :data index-elements)
      ((juxt c/transform-rh-diag c/transform-lh-diag))
      (as-> $
            (mapv (comp find-mas remove-nils) $)
            (apply diff $))
      (nth 2)
      (count)))
