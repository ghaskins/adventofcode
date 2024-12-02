(ns aoc.2024.day2
  (:require [clojure.string :as string]))

(defn parse []
  (->> (slurp "inputs/day2.txt")
       (string/split-lines)
       (mapv #(map parse-long (string/split % #"\s+")))))

(defn analyze-pairs
  [f data]
  (loop [curr (take 2 data)
         next (rest data)
         acc []]
    (if (= 2 (count curr))
      (let [x (f curr)]
        (recur (take 2 next) (rest next) (conj acc x)))
      (every? true? acc))))

(defn verify-report [report]
  (let [[difference increasing decreasing]
        (map #(analyze-pairs % report)
             [#(<= (abs (apply - %)) 3)
              #(apply < %)
              #(apply > %)])]
    {:result (and difference (or increasing decreasing)) :report report}))

(def result-true? (comp true? :result))

(defn part1 []
  (let [data (parse)]
    (->> (map verify-report data)
         (filter result-true?)
         (count))))

(defn remove-nth [n coll]
  (->> (map-indexed vector coll)
       (remove (comp #(= n %) first))
       (map second)))

(defn dampen [{:keys [result report] :as x}]
  (if (false? result)
    (some result-true? (map #(verify-report (remove-nth % report)) (range (count report))))
    true))

(defn part2 []
  (let [data (parse)]
    (->> (map (comp dampen verify-report) data)
         (filter true?)
         (count))))
