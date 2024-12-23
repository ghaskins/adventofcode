(ns aoc.2024.day6
  (:require [clojure.core.match :refer [match]]
            [aoc.utils :refer [parse-matrix index-elements] :as utils]))

(defn find-start [{:keys [data]}]
  (->> (apply concat (index-elements data))
       (some (fn [{:keys [pos element]}] (when (= element \^) pos)))))

(defn parse []
  (-> (slurp "inputs/day6.txt")
      (parse-matrix)
      (as-> $ (assoc $ :start-pos (find-start $)))))

(defn advance-pos [[x y] direction]
  (case direction
    :up    [x       (dec y)]
    :right [(inc x)       y]
    :down  [x       (inc y)]
    :left  [(dec x)      y]))

(defn rotate-right [direction]
  (case direction
    :up    :right
    :right :down
    :down  :left
    :left  :up))

(def not-neg? (complement neg?))

(defn in-bounds? [{:keys [width height] :as input} [x y :as pos]]
  (and (every? not-neg? pos)
       (< x width)
       (< y height)))
(def out-of-bounds? (complement in-bounds?))

(defn obstructed? [{:keys [data] :as input} [x y :as pos]]
  (true? (some-> data (get-in [y x]) (= \#))))

(defn startpos? [{:keys [start-pos] :as input} pos]
  (= pos start-pos))

(defn previously-visited? [visits pos direction]
  (let [directions (get visits pos)]
    (contains? directions direction)))

(defn patrol
  [{:keys [start-pos] :as input}]
  (loop [current-pos start-pos
         current-direction :up
         visits {}]

    (let [visits (update visits current-pos (fn [entry] (conj (or entry #{}) current-direction)))
          candidate-pos (advance-pos current-pos current-direction)
          {:keys [stop? loop? next-pos next-direction] :or {stop? false loop? false next-direction current-direction}}
          (match [(previously-visited? visits candidate-pos current-direction) (in-bounds? input candidate-pos) (obstructed? input candidate-pos)]
                 [true  _     _]     {:stop? true :loop? true}
                 [false false _]     {:stop? true}
                 [false true  false] {:next-pos candidate-pos}
                 [false true  true]  {:next-pos current-pos :next-direction (rotate-right current-direction)})]
      (if stop?
        {:loop? loop? :visits visits}
        (recur next-pos next-direction visits)))))

(defn part1 []
  (-> (parse)
      (patrol)
      :visits
      (count)))

(defn part2 []
  (let [input (parse)]
    (->> (patrol input)
         :visits
         (mapcat (fn [[pos directions]] (mapv #(advance-pos pos %) directions)))
         (distinct)
         (remove (some-fn #(out-of-bounds? input %) #(obstructed? input %) #(startpos? input %)))
         (pmap (fn [candidate-pos] (patrol (update input :data #(assoc-in % (reverse candidate-pos) \#)))))
         (filter :loop?)
         (count))))