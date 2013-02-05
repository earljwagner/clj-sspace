(ns clj-sspace.core-test
  (:use clojure.test
        clj-sspace.core))


(def docs-1
  ["He drank the foobar at the game."
   "Foobar is the number three beverage."
   "A case of foobar is cheap compared to other sodas."
   "Foobar tastes better when cold."])

(def query-1 "foobar")


(def docs-2
  ["shipment of gold damaged in a fire",
   "delivery of silver arrived in a silver truck",
   "shipment of gold arrived in a truck"
   "shipment delivery arrived"])

(def query-2 "gold silver truck")
        
    
;;(prn "assertEquals(2, projected.length()):" (= 2 (.length projected)))
;;(prn "assertEquals(0.2140, Math.abs(projected.get(0)), 0.001):" (> 0.001 (- 0.2140 (java.lang.Math/abs (.get projected 0)))))
;;(prn "assertEquals(0.1821, Math.abs(projected.get(1)), 0.001):" (> 0.001 (- 0.1821 (java.lang.Math/abs (.get projected 1)))))
;;(prn "projected" projected)



(defn test-ss [docs query]
  ;;(.setLevel (java.util.logging.Logger/getLogger (.getName edu.ucla.sspace.common.GenericTermDocumentVectorSpace)) edu.ucla.sspace.util.LoggerUtil.veryVerbose);
  ;;(.setLevel (java.util.logging.Logger/getLogger (.getName edu.ucla.sspace.matrix.factorization.SingularValueDecompositionLibC)) java.util.logging.Level/VERBOSE)
  (prn "docs: " docs)
  (prn "query: " query)
  (let [lsa (make-lsa "JAMA" 2)]
    ;;(println "count docs: " (count docs))
    (process-lsa-docs lsa docs)
    ;;(println (.U svd))
    (process-lsa-space lsa)
    (let [sorted-docs (sort-lsa-docs-by-query lsa query)]
      (doseq [[i score] sorted-docs]
        (let [doc (nth docs i)]
          (println "score:" score "i:" i "doc:" doc))))
    lsa))


;; (clj-sspace.core-test/test-ss clj-sspace.core-test/docs-2 clj-sspace.core-test/query-2)