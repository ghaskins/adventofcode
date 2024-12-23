(ns aoc.2024.day5
  (:require [clojure.string :as string]
            [ubergraph.core :as uber]
            [ubergraph.alg :as uber.alg]
            [aoc.utils :as utils]))

(defn find-split [input]
  (first (utils/find-occurrences #"(?m)^\s" input)))

(defn parse-entries [re s]
  (->> (string/split-lines s)
       (map #(->> (string/split % re)
                  (mapv parse-long)))))

(defn parse-rules [rules]
  (->> (parse-entries #"\|" rules)
       (group-by first)))

(defn parse-update [updates]
  (parse-entries #"," updates))

(defn parse [input]
  (let [split   (find-split input)
        rules   (-> (subs input 0 (dec split))
                    (parse-rules))
        updates (-> (subs input split (count input))
                    (parse-update))]
    {:rules rules
     :updates updates}))

(defn topsort [rules update]
  (let [update (set update)]
    (->> (filter (comp update first) rules)
         (mapcat second)
         (apply uber/digraph)
         (uber.alg/topsort))))

(defn compute-weights [rules update]
  (->> (topsort rules update)
       (map-indexed (fn [i x] [x i]))
       (into {})))

(defn correct-order? [rules update]
  (let [weights (compute-weights rules update)]
    (->> (map #(get weights %) update)
         (apply <))))

(defn reorder [rules update]
  (let [weights (compute-weights rules update)]
    (sort-by #(get weights %) update)))

(defn get-middle-page [update]
  (nth update (/ (count update) 2)))

(defn process [f]
  (->> (parse (slurp "inputs/day5.txt"))
       (f)
       (map get-middle-page)
       (reduce + 0)))

(defn part1 []
  (process (fn [{:keys [rules updates]}]
             (filter (partial correct-order? rules) updates))))

(defn part2 []
  (process (fn [{:keys [rules updates]}]
             (->> (remove (partial correct-order? rules) updates)
                  (mapv (partial reorder rules))))))