(ns aoc.2024.day4.common
  (:require [clojure.string :as string]
            [clojure.core.matrix :as matrix]))

(defn parse [input]
  (let [data (->> input
                  (string/split-lines)
                  (mapv (comp vec seq)))]
    {:width (-> data first count)
     :height (count data)
     :data data}))

(defn get-diag-row [{:keys [width data]} row]
  (->> (take width (iterate (fn [[a b]] [(dec a) (inc b)]) [row 0]))
       (map #(get-in data %))))

(defn transform-diag [{:keys [width height] :as spec}]
  (mapv #(get-diag-row spec %) (range (dec (+ width height)))))

(def transform-rh-diag transform-diag)

(defn rotate-matrix [m]
  (->> (matrix/transpose m)
       (mapv (comp vec reverse))))

(defn transform-lh-diag [{:keys [width height data] :as spec}]
  (transform-diag {:width height :height width :data (rotate-matrix data)}))
