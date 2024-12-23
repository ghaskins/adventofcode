(ns aoc.2024.day4.part2
  (:require [clojure.string :as string]
            [clojure.data :refer [diff]]
            [aoc.2024.day4.common :as c]
            [aoc.utils :refer [parse-matrix index-elements ] :as utils]))

(defn find-occurrences [row]
  (utils/find-occurrences #"M(?=AS)|S(?=AM)" (string/join row)))

(defn analyze-row [row]
  (let [positions (mapv :pos row)
        occurrences (->> (mapv :element row)
                         (find-occurrences))]
    (map #(get positions %) occurrences)))

(defn remove-nils [coll]
  (map #(remove nil? %) coll))

(defn find-mas [data]
  (->> (map analyze-row data)
       (reduce (fn [acc coll] (reduce conj acc coll)) #{})))

(defn run []
  (-> (slurp "inputs/day4.txt")
      (parse-matrix)
      (update :data index-elements)
      ((juxt c/transform-rh-diag c/transform-lh-diag))
      (as-> $
            (mapv (comp find-mas remove-nils) $)
            (apply diff $))
      (nth 2)
      (count)))
