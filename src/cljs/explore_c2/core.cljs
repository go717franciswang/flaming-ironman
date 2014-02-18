(ns explore-c2.core
  (:use-macros [c2.util :only [p pp bind!]])
  (:require [clojure.browser.repl :as repl]
            [c2.svg :as svg]
            [c2.core :as c2]))

;(repl/connect "http://betalabs:9000/repl")
(repl/connect "http://localhost:9000/repl")

; (def coordinate 
;   {:x 100 :y 200})
; 
; (pp coordinate)
; (pp (svg/->xy coordinate))
; (pp (svg/->xy [100 200]))
; 
; (pp {:a 150 :b [:c :d]})
; 
; (pp (svg/scale coordinate))
; (pp (svg/translate coordinate))
; 
; (pp (svg/rotate coordinate))
; 
; (pp (svg/get-bounds (.getElementById js/document "s1")))
; 
; (pp (svg/circle [50 50] 10))
; 
; (pp (svg/arc {:inner-radius 10 :outer-radius 15 :angle-offset 1}))

(bind! "#barchart" [:div#barchart "abc"])
; (bind! "#barchart"
;        [:div#barchart
;         [:h2 "Rad barchart!"]
;         [:div.bars
;          (unify {"A" 1, "B" 2, "C" 4, "D" 3}
;                 (fn [[label val]]
;                   [:div.bar
;                    [:div.bar-fill {:style {:width (x-scale val)}}]
;                    [:span.label label]]))]])

; (bind! "#mydiv"
;   [:div#mydiv
;    [:div.bars
;     (let [data [[:a 1]
;                 [:b 2]]]
;       (unify data (fn [[k v]]
;                     [:div [:span k] [:span v]])))]])

