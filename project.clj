(defproject aoc-2024 "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.12.0"]
                 [org.clojure/math.combinatorics "0.3.0"]
                 [org.clojure/core.match "1.1.0"]
                 [dev.weavejester/medley "1.8.0"]
                 [net.mikera/core.matrix "0.63.0"]
                 [instaparse "1.5.0"]
                 [ubergraph "0.9.0"]]
  :repl-options {:init-ns user}
  :profiles {:dev     {:dependencies   [[org.clojure/tools.namespace "1.5.0"]
                                        [criterium "0.4.6"]
                                        [eftest "0.6.0"]]}})
