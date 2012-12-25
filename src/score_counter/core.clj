(ns score-counter.core
  (:use quil.core)
  (:gen-class))

(def teams [{:name "Erik" :score 100}
            {:name "Jonas" :score 150}])

(defn setup []
  (text-font (create-font "Arial" 20)))

(defn draw-team [nr team]
  (fill 0)
  (text-size 100)
  (text (str (:name team) ": " (:score team))
        (+ 100 (* 0 nr))
        (+ 100 (* 100 nr))))

(defn draw []
  (background 220 220 120)
  (fill 0)
  (doall (map (fn [[nr team]] (draw-team nr team))
              (zipmap (range) teams))))

(defn -main [& args]
       (defsketch score-counter
         :title "Score Counter"
         :setup setup
         :draw draw
         :size [800 600]))

(-main)