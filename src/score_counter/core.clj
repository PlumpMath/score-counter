(ns score-counter.core
  (:use quil.core)
  (:import [java.awt.event KeyEvent])
  (:gen-class))

(def teams (atom [{:name "Erik" :score 0 :inc-key 81 :dec-key 65}
                  {:name "Jonas" :score 0 :inc-key 87 :dec-key 83}
                  {:name "Klara" :score 0 :inc-key 69 :dec-key 68}
                  {:name "Anna" :score 0 :inc-key 82 :dec-key 70}
                  {:name "Henrik & Mia" :score 0 :inc-key 84 :dec-key 71}
                  ]))

(defn setup []
  (text-font (create-font "Arial Black.ttf" 20)))

(defn draw-team [nr team]
  (let [slice (/ (height) (count @teams))]
    (fill 30)
    (text-size (* 0.8 slice))
    (text (str (:name team) ": " (:score team))
          (+ 20)
          (+ (* 0.8 slice) (* slice nr)))))

(defn osc [start end speed]
  (let [diff (- end start)]
    (+ start (/ diff 2) (* diff (sin (* speed (millis)))))))

(defn draw []
  (let [r (osc 200 255 0.0003)
        g (osc 155 100 0.001)
        b (osc 100 100 0.0005)]
    (background r g b))
  (doall (map (fn [[nr team]] (draw-team nr team))
              (zipmap (range) @teams))))

(defn get-index-of-team [team-name]
  (loop [index 0]
    (if (= (:name (get @teams index)) team-name)
      index
      (if (> index (count @teams))
        (throw (Exception. (str "Can't find team " team-name)))
        (recur (inc index))))))

(defn change-score [team-name f]
  (let [team-index (get-index-of-team team-name)
        team (nth @teams team-index)
        new-team (assoc team :score (f (:score team)))
        new-teams (assoc @teams team-index new-team)]
    (reset! teams new-teams)))

(defn get-value-of-key-in-team [team keykey]
  (let [k (get team keykey)] (= k (key-code))))

(defn check-change-key [keykey f]
  (if-let [team (first (filter #(get-value-of-key-in-team % keykey) @teams))]
    (change-score (:name team) f)))

(defn key-pressed []
  (condp = (key-code)
    59 (do 
         (spit "save.txt" (str @teams))
         (println "Saved save.txt"))
    76 (do
         (reset! teams (read-string (slurp "save.txt")))
         (println "Loaded save.txt"))
    nil)
  (check-change-key :inc-key inc)
  (check-change-key :dec-key dec))

(defn -main [& args]
  (defsketch score-counter
    :title "Score Counter"
    :setup setup
    :draw draw
    :key-pressed key-pressed
    :size [800 600]))
