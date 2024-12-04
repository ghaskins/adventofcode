(ns aoc.utils)

(defn remove-nth [n coll]
  (->> (map-indexed vector coll)
       (remove (comp #(= n %) first))
       (map second)))
