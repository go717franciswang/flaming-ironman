(ns explore-c2.core
  (:require [clojure.browser.repl :as repl]
            [c2.svg :as svg])
  (:use-macros [c2.util :only [p pp]]))

(repl/connect "http://betalabs:9000/repl")

(def coordinate 
  {:x 100 :y 200})

(pp coordinate)
(pp (svg/->xy coordinate))
(pp (svg/->xy [100 200]))

(pp {:a 150 :b [:c :d]})

(pp (svg/scale coordinate))
(pp (svg/translate coordinate))

(pp (svg/rotate coordinate))

(pp (svg/get-bounds (.getElementById js/document "s1")))

(pp (svg/circle [50 50] 10))

(pp (svg/arc {:inner-radius 10 :outer-radius 15 :angle-offset 1}))
