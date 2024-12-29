(ns aoc.2024.day9.part2
  (:require [aoc.2024.day9.common :refer [parse]]))

(defn add-file [diskmap id offset len]
  (assoc diskmap offset {:id id :len len}))

(defn insert-freeblock [freespace len offset]
  (update freespace len #(conj (or % (sorted-set)) offset)))

(defn add-freespace [freespace offset len]
  (cond-> freespace
          (pos? len) (insert-freeblock len offset)))

(defn build-diskmap [input]
  (reduce (fn [{:keys [index offset] :as acc} [used free]]
            (let [free (or free 0)]
              (-> acc
                  (update :index inc)
                  (update :offset #(+ % used free))
                  (update :diskmap #(add-file % index offset used))
                  (update :freespace #(add-freespace % (+ offset used) free)))))
          {:index 0 :offset 0 :diskmap (sorted-map) :freespace (sorted-map)}
          input))

(defn find-free-block [freespace min-size max-offset]
  (->> (subseq freespace >= min-size)
       (remove (fn [[len offsets]] (empty? offsets)))
       (map (fn [[len offsets]] {:free-offset (first offsets) :free-len len}))
       (filter #(-> % :free-offset (< max-offset)))
       (sort-by :free-offset)
       (first)))

(defn move-block [state [offset {:keys [len] :as block}] {:keys [free-offset free-len] :as freeblock}]
  (let [remaining-len (- free-len len)
        remaining-offset (+ free-offset len)]
    (-> state
        (update :diskmap
                #(-> %
                     (dissoc offset)
                     (assoc free-offset block)))
        (update :freespace
                #(-> %
                     (update free-len disj free-offset)
                     (cond-> (pos? remaining-len)
                             (insert-freeblock remaining-len remaining-offset)))))))

(defn run []
  (let [input (parse)
        {:keys [diskmap] :as state} (build-diskmap input)]
    (->> (reverse diskmap)
         (reduce (fn [{:keys [freespace] :as acc} [offset {:keys [len]} :as block]]
                   (let [{:keys [free-offset] :or {free-offset offset} :as freeblock} (find-free-block freespace len offset)]
                     (cond-> acc
                             (< free-offset offset) (move-block block freeblock))))
                 state)
         :diskmap
         (mapcat (fn [[offset {:keys [id len]}]]
                (map #(* % id) (range offset (+ offset len)))))
         (reduce + 0))))