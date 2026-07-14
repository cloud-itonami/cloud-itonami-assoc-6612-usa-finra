(ns association.facts-test
  (:require [clojure.string :as str]
            [clojure.test :refer [deftest is]]
            [association.facts :as facts]))

(deftest finra-has-spec-basis
  (let [sb (facts/spec-basis "finra")]
    (is (= 2 (count sb)))
    (is (every? #(str/starts-with? (:association-rule/url %) "https://www.finra.org/") sb))
    (is (every? #(= "6612" (:association-rule/isic %)) sb))))

(deftest unknown-association-has-no-spec-basis
  (is (nil? (facts/spec-basis "keidanren")))
  (is (nil? (facts/spec-basis "zzz"))))

(deftest coverage-is-honest
  (let [c (facts/coverage ["finra" "keidanren"])]
    (is (= 2 (:requested c)))
    (is (= 1 (:covered c)))
    (is (= ["keidanren"] (:missing-associations c)))))

(deftest by-topic-filters
  (is (= ["finra.by-laws-of-the-corporation"]
         (mapv :association-rule/id (facts/by-topic "finra" :governance))))
  (is (empty? (facts/by-topic "finra" :labor)))
  (is (empty? (facts/by-topic "keidanren" :governance))))
