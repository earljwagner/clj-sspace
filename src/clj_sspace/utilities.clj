(ns clj-sspace.utilities
  (:require
   [stout.porter-stemmer :as stemmer])
  )




(defn tokenize [text]
  (clojure.string/split text #"\W+"))


;; from: http://langref.org/fantom+clojure/maps/algorithms/histogram
(defn histogram [groups]
  (->> groups
       (group-by identity)
       (reduce (fn [m e] (assoc m (first e) (count (second e)))) {})))


(def memoized-stems (ref {}))


(defn stem [mixed-case-word]
  (let [word (clojure.string/lower-case mixed-case-word)]
    (if-let [word-stem (get @memoized-stems word)]
      word-stem
      (let [word-stem (stemmer/porter-stemmer word)]
        (dosync (alter memoized-stems assoc word word-stem))
        word-stem))))
          