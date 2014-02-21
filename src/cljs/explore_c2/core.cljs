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
  (if (contains? node :children)
    (reduce 
      (fn [[x y] [x1 y1]]
        [(max x x1) (max y y1)])
      [(:x node) (:y node)]
      (map get-max (:children node)))
    [(:x node) (:y node)]))

(def unit-width 50)
(def unit-height 50)
(def text-height 20)

(defn get-scaled-pt [node]
  [(* (inc (:x node)) unit-width)
   (* (inc (:y node)) unit-height)])

(defn get-lines [parent-node children-nodes]
  (reduce
    (fn [lines child]
      (let [[x1 y1] (get-scaled-pt parent-node)
            [x2 y2] (get-scaled-pt child)
            dx (- x2 x1)
            dy (- y2 y1)
            m (/ dy dx)
            h (/ text-height 2)
            x12 (+ x1 (/ h m))
            y12 (+ y1 h)
            x22 (- x2 (/ h m))
            y22 (- y2 h)
            line [:line {:x1 x12 :x2 x22 :y1 y12 :y2 y22
                         :style {:stroke "#000000"
                                 :stroke-width 2}}]
            lines (conj lines line)]
        (if (contains? child :children)
          (into lines (get-lines child (:children child)))
          lines)))
    []
    children-nodes))

(defn get-legends [node]
  (let [[x y] (get-scaled-pt node)
        text (:node node)
        legends [[:text {:x x :y (+ y (/ text-height 4)) :text-anchor "middle" :fill "black"} text]]]
    (if (contains? node :children)
      (reduce
        (fn [legends child]
          (into legends (get-legends child)))
        legends
        (:children node))
      legends)))

(let [tree (bind-location root)
      [max-x max-y] (get-max tree)
      width (* (+ 2 max-x) unit-width)
      height (* (+ 2 max-y) unit-height)
      lines (get-lines tree (:children tree))
      legends (get-legends tree)
      svg-ele [:svg#tree {:height height :width width}]]
  (pp legends)
  (bind! "#tree" (vec (concat svg-ele lines legends))))

(repl/connect "http://betalabs:9000/repl")
;(repl/connect "http://localhost:9000/repl")

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

