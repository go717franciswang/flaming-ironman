(ns parse-tree.display
  (:use-macros [c2.util :only [p pp bind! interval]])
  (:require [clojure.browser.repl :as repl]
            [clojure.walk :as w]
            [c2.svg :as svg]
            [c2.core :as c2]))

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

(defn draw-tree [root container-selector]
  (let [tree (bind-location root)
        [max-x max-y] (get-max tree)
        width (* (+ 2 max-x) unit-width)
        height (* (+ 2 max-y) unit-height)
        lines (if (contains? tree :children)
                (get-lines tree (:children tree))
                [])
        legends (get-legends tree)
        container-nodename (.-nodeName (c2.dom/select container-selector))
        svg-ele [:svg {:height height :width width}]]
    (bind! container-selector [container-nodename (vec (concat svg-ele lines legends))])))

(defn ^:export draw-tree-js [root-js container-selector]
  (let [root (w/keywordize-keys (js->clj root-js))]
    (pp root)
    (draw-tree root container-selector)))

(defn ^:export demo []
  (let [root {:node "S"
              :children [{:node "S"
                          :children [{:node "("} 
                                     {:node "S" 
                                      :children [{:node "("} 
                                                 {:node ")"}]}
                                     {:node ")"}]}
                         {:node "S"
                          :children [{:node "("}
                                     {:node ")"}]}]}]
    (draw-tree root "#tree")))

;(repl/connect "http://betalabs:9000/repl" )
(repl/connect "http://localhost:9000/repl")

