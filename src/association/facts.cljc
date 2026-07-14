(ns association.facts
  "Industry self-regulatory rule catalog for the Financial Industry
  Regulatory Authority (FINRA) -- a 5th industry-association-level
  source (see cloud-itonami-assoc-6419-jpn-zenginkyo, -6512-jpn-sonpo,
  -6612-jpn-jsda, -6419-deu-bankenverband for the first four) per
  ADR-2607141700 (cloud-itonami-compliance-fact-federation). Aligned to
  ISIC 6612 (securities brokerage) -- the SAME ISIC code as jsda (JPN),
  enabling a direct cross-country securities-SRO comparison, mirroring
  what bankenverband/zenginkyo already do for ISIC 6419 (banking). Every
  entry cites an OFFICIAL finra.org URL -- never fabricated. A rule not
  in this table has NO spec-basis, full stop; extend `catalog`, do not
  invent an id/url.

  Both entries below were directly WebFetch-verified against the live
  finra.org page on 2026-07-14 (this is the first successfully-fetched
  US industry-association site in this ADR's work -- ABA and UK Finance
  both 403'd earlier; finra.org rendered cleanly).")

(def catalog
  "assoc-slug -> vector of self-regulatory rule entries."
  {"finra"
   [{:association-rule/id "finra.rulebook"
     :association-rule/title "FINRA Rules (FINRA Manual)"
     :association-rule/association "finra"
     :association-rule/isic "6612"
     :association-rule/country "USA"
     :association-rule/kind :self-regulatory-code
     :association-rule/url "https://www.finra.org/rules-guidance/rulebooks/finra-rules"
     :association-rule/url-provenance :official-association-site
     :association-rule/retrieved-at "2026-07-14"
     :association-rule/topic #{:consumer-protection :fair-transaction :suitability}}
    {:association-rule/id "finra.by-laws-of-the-corporation"
     :association-rule/title "By-Laws of the Corporation"
     :association-rule/association "finra"
     :association-rule/isic "6612"
     :association-rule/country "USA"
     :association-rule/kind :self-regulatory-code
     :association-rule/url "https://www.finra.org/rules-guidance/rulebooks/corporate-organization/laws-corporation"
     :association-rule/url-provenance :official-association-site
     :association-rule/retrieved-at "2026-07-14"
     :association-rule/topic #{:governance}}]})

(defn spec-basis [assoc-slug] (get catalog assoc-slug))

(defn coverage
  ([] (coverage (keys catalog)))
  ([slugs]
   (let [have (filter catalog slugs)
         missing (remove catalog slugs)]
     {:requested (count slugs)
      :covered (count have)
      :covered-associations (vec (sort have))
      :missing-associations (vec (sort missing))
      :note (str "cloud-itonami-assoc-6612-usa-finra Wave 0 (ADR-2607141700): "
                 (count (get catalog "finra")) " finra rules seeded with an "
                 "official finra.org citation. Extend "
                 "`association.facts/catalog`, never fabricate a rule id/url.")})))

(defn by-topic [assoc-slug topic]
  (filterv #(contains? (:association-rule/topic %) topic) (spec-basis assoc-slug)))
