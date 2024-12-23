(ns aoc.utils
  (:require [clojure.string :as string]))

(defn remove-nth [n coll]
  (->> (map-indexed vector coll)
       (remove (comp #(= n %) first))
       (map second)))

(defn find-occurrences [re s]
  (loop [m (re-matcher re s)
         r #{}]
    (if (.find m)
      (recur m (conj r (inc (.start m))))
      r)))

(defn parse-matrix [input]
  (let [data (->> input
                  (string/split-lines)
                  (mapv (comp vec seq)))]
    {:width (-> data first count)
     :height (count data)
     :data data}))

(defn mapv-indexed [f coll]
  (vec (map-indexed f coll)))

(defn index-elements [data]
  (mapv-indexed (fn [i row]
                  (mapv-indexed (fn [j x] {:pos [j i] :element x}) row))
                data))
