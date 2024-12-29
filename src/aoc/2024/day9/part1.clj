(ns aoc.2024.day9.part1
  (:require [aoc.2024.day9.common :refer [parse]]))

(defn add-file [diskmap id offset len]
  (reduce #(assoc %1 %2 id) diskmap (range offset (+ offset len))))

(defn add-freespace [freespace offset len]
  (add-file freespace :free offset len))

(defn build-diskmap [input]
  (-> (reduce (fn [{:keys [index offset] :as acc} [used free]]
                (let [free (or free 0)]
                  (-> acc
                      (update :index inc)
                      (update :offset #(+ % used free))
                      (update :diskmap #(add-file % index offset used))
                      (update :freespace #(add-freespace % (+ offset used) free)))))
              {:index 0 :offset 0 :diskmap (sorted-map) :freespace (sorted-map)}
              input)
      (update :freespace keys)))

(defn move-block [diskmap id old-offset new-offset]
  (-> diskmap
      (dissoc old-offset)
      (assoc new-offset id)))

(defn run []
  (let [input (parse)
        {:keys [diskmap freespace]} (build-diskmap input)]
    (->> (map vector (reverse diskmap) freespace)
         (reduce (fn [acc [[offset id] proposed-offset]]
                   (cond-> acc
                           (< proposed-offset offset) (move-block id offset proposed-offset)))
                 diskmap)
         (map #(apply * %))
         (reduce + 0))))