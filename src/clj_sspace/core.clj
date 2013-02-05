(ns clj-sspace.core
  (:require [clj-sspace.utilities :as u]))




(defn line-vectors [lines]
  (for [line lines]
    (set (map u/stem (u/tokenize line)))))


(defn output-value [vector term]
  (if (.contains vector term) 1 0))


(defn output-vector-text [vectors]
  (let [all-terms (reduce clojure.set/union vectors)]
    (clojure.string/join
     "\n"
     (cons
      (str (count all-terms) " " (count vectors))
      (for [term all-terms]
        (str term "|" (clojure.string/join " " (vec (map #(output-value % term) vectors)))))))))



(defn get-svd [svd-name]
  (edu.ucla.sspace.matrix.SVD/getFactorization (edu.ucla.sspace.matrix.SVD$Algorithm/valueOf svd-name)))
      

(defn make-lsa [svd-name dims]
  (let [svd (get-svd svd-name)
        transform (edu.ucla.sspace.matrix.NoTransform.)]
    (edu.ucla.sspace.lsa.LatentSemanticAnalysis.
     true dims transform
     svd false (edu.ucla.sspace.basis.StringBasisMapping.))))


(defn process-lsa-docs [lsa docs]
  (doseq [doc docs]
    (.processDocument lsa (java.io.BufferedReader. (java.io.StringReader. doc)))))


(defn doc-count [lsa]
  (.documentSpaceSize lsa))


(defn process-lsa-space [lsa]
  (.processSpace lsa (java.lang.System/getProperties)))


(defn lsa-similarity-with-doc [lsa projected-query i]
  (let [doc (.getDocumentVector lsa i)]
    [i (edu.ucla.sspace.common.Similarity/cosineSimilarity projected-query doc)]))


(defn sort-lsa-docs-by-query [lsa query]
  (let [projected-query (.project lsa (edu.ucla.sspace.text.StringDocument. query))
        doc-count (range (.documentSpaceSize lsa))
        doc-is-and-scores (map #(lsa-similarity-with-doc lsa projected-query %) doc-count)
        sorted-doc-is-and-scores (sort-by second > doc-is-and-scores)]
    sorted-doc-is-and-scores))
