(ns score-counter.core
  (:use quil.core)
  (:gen-class))

(def teams [{:name "Erik" :score 100}
            {:name "Jonas" :score 150}
            {:name "Klara" :score 200}
            {:name "Anna" :score 40}])

(defn setup []
  (text-font (create-font "Arial" 20)))

(defn draw-team [nr team]
  (let [slice (/ (height) (count teams))]
    (fill 0)
    (text-size (* 0.8 slice))
    (text (str (:name team) ": " (:score team))
          (+ 20 (* 0 nr))
          (+ (* 0.8 slice) (* slice nr)))))

(defn draw []
  (background 220 (+ 155 (* 100 (sin (* 0.001 (millis))))) 220)
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