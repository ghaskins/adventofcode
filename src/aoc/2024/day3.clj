(ns aoc.2024.day3
  (:require [clojure.java.io :as io]
            [instaparse.core :as insta]))

(def part1-test "xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))")
(def part2-test "xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))")

(defn parse
  [start-rule]
  (let [input (slurp "inputs/day3.txt")]
    (-> (io/resource "day3.ebnf")
        (insta/parser)
        (insta/parse input :start start-rule))))

(defn process [start-rule]
  (->> (parse start-rule)
       (map rest)
       (map #(mapv parse-long %))
       (map #(apply * %))
       (reduce + 0)))

(defn part1 []
  (process :simple))

(defn part2 []
  (process :conditional))
