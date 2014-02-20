(ns explore-c2.core
  (:use-macros [c2.util :only [p pp bind! interval]])
  (:require [clojure.browser.repl :as repl]
            [c2.svg :as svg]
            [c2.core :as c2]))

(def root
  {:node "S"
   :children [{:node "S"
               :children [{:node "("} 
                          {:node "S" 
                           :children [{:node "("} 
                                      {:node ")"}]}
                          {:node ")"}]}
              {:node "S"
               :children [{:node "("}
                          {:node ")"}]}]})

(defn get-center [nodes]
  (let [a (first nodes)
        b (last nodes)]
    (/ (+ (:x a) (:x b)) 2.0)))

(defn bind-location 
  ([root] 
   (bind-location root 0 (atom -1)))
  ([node lvl right-most]
   "add relative position to each tree's node such that 
    root is at y=0, left-most node is at x=0, and each new leaf
    we visit will have x=1,2,3,..."
   (if (contains? node :children)
     (let [children (map #(bind-location % (inc lvl) right-most) (:children node))]
       (assoc node 
              :children children
              :x (get-center children)
              :y lvl))
     (do
       (swap! right-most inc)
       (assoc node :x @right-most :y lvl)))))

(defn get-max [node]
  (pp node)
  (loop [x (:x node)
         y (:y node)
         i 0]
    (if (and (contains? node :children) (< i (count (:children node))))
      (let [[x1 y1] (get-max (get i (:children node)))]
        (recur (max x x1) (max y y1) (inc y)))
      [(:x node) (:y node)])))

(def unit-width 50)
(def unit-height 50)

(p "hi")

(defn get-lines [parent-node children-nodes]
  (loop [i 0
         lines []]
    (if (>= i (count children-nodes))
      lines
      (let [node (get i children-nodes)
            x1 (* (:x parent-node) unit-width)
            y1 (* (:y parent-node) unit-height)
            x2 (* (:x node) unit-width)
            y2 (* (:y node) unit-height)
            line [:line {:x1 x1 :x2 x2 :y1 y1 :y2 y2}]
            lines (conj lines line)]
        (if (contains? node :children)
          (recur (inc i) (into lines (get-lines node (:children node))))
          (recur (inc i) lines))))))

#_(pp
(let [tree (bind-location root)
      [max-x max-y] (get-max tree)
      width (* (inc max-x) unit-width)
      height (* (inc max-y) unit-height)
      lines (get-lines tree (:children tree))]
  ;(bind! "#tree"
         [:svg#tree lines {:height height :width width}]))

#_(pp (bind-location root))

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

#_(bind! "#barchart"
       [:div#barchart
        [:h2 "Rad barchart!"]
        [:div.bars
         (c2/unify {"A" 1, "B" 2, "C" 4, "D" 3}
                (fn [[label val]]
                  [:div.bar
                   [:div.bar-fill {:style {:width val}}]
                   [:span.label label]]))]])

(bind! "#mydiv" 
       [:div#mydiv 
        (c2/unify {"a" 1} 
                  (fn [[k v]] 
                    [:div k v]))])

(let [data (atom [0])
      random-update (fn []
                      (let [r (rand)]
                        ;(p (str r))
                        (reset! data [r])))
                        ;(swap! data assoc 0 r)))
      ]
  (bind! "#mydiv" [:div#mydiv (c2/unify @data (fn [v] [:div v]))])
  (interval 1000 (random-update)))

