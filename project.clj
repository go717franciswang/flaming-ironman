(defproject parse-tree "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/clojurescript "0.0-2138"]
                 [hiccup "1.0.5"]
                 [com.keminglabs/c2 "0.2.3"]]
  :plugins [[lein-cljsbuild "1.0.1"]]
  :cljsbuild {
              :builds [{:source-paths ["src/cljs"]
                       :compiler {:output-to "public/parse_tree.js"
                                  :optimizations :whitespace
                                  :pretty-print true}}]})
