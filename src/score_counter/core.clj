(ns score-counter.core
  (:use quil.core)
  (:import [java.awt.event KeyEvent])
  (:gen-class))

(def teams (atom [{:name "Erik" :score 0 :add-key 81 :dec-key 65}]))

(defn setup []
  (text-font (create-font "Arial Black.ttf" 20)))

(defn draw-team [nr team]
  (let [slice (/ (height) (count @teams))]
    (fill 0)
    (text-size (* 0.8 slice))
    (text (str (:name team) ": " (:score team))
          (+ 20)
          (+ (* 0.8 slice) (* slice nr)))))

(defn draw []
  (let [r (+ 200 (* 55 (sin (* 0.0003 (millis)))))
        g (+ 155 (* 100 (sin (* 0.001 (millis)))))
        b (+ 155 (* -100 (sin (* 0.0005 (millis)))))]
    (background r g b))
  (fill 0)
  (doall (map (fn [[nr team]] (draw-team nr team))
              (zipmap (range) @teams))))

(defn get-index-of-team [team-name]
  (loop [index 0]
    (if (= (:name (get @teams index)) team-name)
      index
      (if (> index (count @teams))
        (throw (Exception. (str "Can't find team " team-name ", index is " index)))
        (recur (inc index))))))

(defn change-score [team-name f]
  (let [team-index (get-index-of-team team-name)
        team (nth @teams team-index)
        new-team (assoc team :score (f (:score team)))
        new-teams (assoc @teams team-index new-team)]
    (reset! teams new-teams)))

(defn key-pressed []
  (condp = (key-code)
    59 (do 
         (spit "save.txt" (str @teams))
         (println "Saved save.txt"))
    76 (do
         (reset! teams (read-string (slurp "save.txt")))
         (println "Loaded save.txt"))
      (do
        (if-let [team (first (filter (fn [{k :add-key}] (= k (key-code))) @teams))]
          (change-score (:name team) inc))
        (if-let [team (first (filter (fn [{k :dec-key}] (= k (key-code))) @teams))]
          (change-score (:name team) dec)))))

(defn -main [& args]
       (defsketch score-counter
         :title "Score Counter"
         :setup setup
         :draw draw
         :key-pressed key-pressed
         :size [800 600]))



