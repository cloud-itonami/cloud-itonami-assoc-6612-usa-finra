(ns association-facts-test
  (:require [clojure.java.io :as io] [clojure.java.shell :as shell]
            [clojure.test :refer [deftest is testing]]
            [kotoba.compiler.core :as compiler] [kotoba.compiler.ir :as ir]))
(def source (slurp "src/association_facts.kotoba"))
(defn call [kir f & xs] (ir/execute kir f (vec xs)))
(defn present [x] (when (second x) (nth x 2)))
(def fields ["id" "title" "association" "isic" "country" "kind" "url" "url-provenance"
             "established-date" "last-revised-date" "retrieved-at"])
(def expected
  [{"id" "finra.rulebook" "title" "FINRA Rules (FINRA Manual)"
    "association" "finra" "isic" "6612" "country" "USA" "kind" "self-regulatory-code"
    "url" "https://www.finra.org/rules-guidance/rulebooks/finra-rules"
    "url-provenance" "official-association-site" "established-date" nil
    "last-revised-date" nil "retrieved-at" "2026-07-14"}
   {"id" "finra.by-laws-of-the-corporation" "title" "By-Laws of the Corporation"
    "association" "finra" "isic" "6612" "country" "USA" "kind" "self-regulatory-code"
    "url" "https://www.finra.org/rules-guidance/rulebooks/corporate-organization/laws-corporation"
    "url-provenance" "official-association-site" "established-date" nil
    "last-revised-date" nil "retrieved-at" "2026-07-14"}])
(deftest reference-preserves-authority
  (let [kir (:kir (compiler/compile-source source :js-kotoba-v1))
        observed (mapv (fn [i] (into {} (map (fn [f] [f (present (call kir 'entry-field "finra" i f))]) fields))) [0 1])]
    (is (= expected observed))
    (is (= [[nil nil] [nil nil]]
           (mapv (fn [i] (mapv #(present (call kir 'entry-field "finra" i %)) ["established-date" "last-revised-date"])) [0 1])))
    (is (= [["consumer-protection" "fair-transaction" "suitability"] ["governance"]]
           (mapv (fn [i] (mapv #(present (call kir 'topic "finra" i %)) (range (call kir 'topic-count "finra" i)))) [0 1])))
    (is (= "finra.rulebook" (present (call kir 'by-topic-id "finra" "suitability" 0))))
    (is (= "finra.by-laws-of-the-corporation" (present (call kir 'by-topic-id "finra" "governance" 0))))
    (is (= #{} (set (:effects kir))))
    (testing "fail closed"
      (is (zero? (call kir 'entry-count "financial-industry-regulatory-authority")))
      (is (zero? (call kir 'entry-count "jsda")))
      (is (nil? (present (call kir 'entry-field "finra" 2 "id"))))
      (is (nil? (present (call kir 'entry-field "finra" 0 "established-date"))))
      (is (nil? (present (call kir 'topic "finra" 0 3))))
      (is (nil? (present (call kir 'topic "finra" 1 1))))
      (is (zero? (call kir 'by-topic-count "finra" "labor")))
      (is (nil? (present (call kir 'by-topic-id "finra" "governance" 1)))))))
(defn compiler-root [] (nth (iterate #(.getParent ^java.nio.file.Path %)
  (java.nio.file.Path/of (.toURI (io/resource "kotoba/compiler/core.clj")))) 4))
(defn base64 [x] (.encodeToString (java.util.Base64/getEncoder) x))
(deftest restricted-js-and-wasm-conform-semantically
  (let [js (compiler/compile-source source :js-kotoba-v1) wasm (compiler/compile-source source :wasm32-browser-kotoba-v1)
        js64 (base64 (.getBytes ^String (:source js) "UTF-8")) wasm64 (base64 ^bytes (:bytes wasm))
        p (shell/sh "node" "--input-type=module" "-e"
            (str "import(process.argv[1]).then(async h=>{const j=await import('data:text/javascript;base64," js64 "');const w=await h.instantiateKotoba(Buffer.from(process.argv[2],'base64'));const r=x=>{if(x['entry-field']('finra',0n,'established-date')[1]!==false||x['entry-field']('finra',0n,'last-revised-date')[1]!==false||x['entry-field']('finra',1n,'established-date')[1]!==false||x['entry-field']('finra',1n,'last-revised-date')[1]!==false)throw Error('dates');if(x['topic-count']('finra',0n)!==3n||x['topic-count']('finra',1n)!==1n||x['by-topic-id']('finra','suitability',0n)[2]!=='finra.rulebook'||x['by-topic-id']('finra','governance',0n)[2]!=='finra.by-laws-of-the-corporation'||x['entry-count']('jsda')!==0n)throw Error('authority');};r(j.instantiateKotoba({}));r(w.instance.exports)}).catch(e=>{console.error(e);process.exit(99)})")
            (.toString (.toUri (.resolve (compiler-root) "runtime/browser-host.mjs"))) wasm64)]
    (is (zero? (:exit p)) (str (:out p) (:err p)))))
(deftest production-source-authority
  (is (= ["src/association_facts.kotoba"] (->> (file-seq (io/file "src")) (filter #(.isFile %)) (map str) sort vec))))
