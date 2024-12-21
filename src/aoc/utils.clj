(ns aoc.utils)

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