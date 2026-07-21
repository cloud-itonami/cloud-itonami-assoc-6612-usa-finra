# cloud-itonami-assoc-6612-usa-finra

Industry self-regulatory rule catalog for the **Financial Industry
Regulatory Authority** (FINRA) — a 5th industry-association-level
source, and the first successfully-verified US industry-association
site in this family (ABA and UK Finance both returned 403 Forbidden to
WebFetch and were abandoned), alongside
[`cloud-itonami-assoc-6419-jpn-zenginkyo`](https://github.com/cloud-itonami/cloud-itonami-assoc-6419-jpn-zenginkyo),
[`cloud-itonami-assoc-6512-jpn-sonpo`](https://github.com/cloud-itonami/cloud-itonami-assoc-6512-jpn-sonpo),
[`cloud-itonami-assoc-6612-jpn-jsda`](https://github.com/cloud-itonami/cloud-itonami-assoc-6612-jpn-jsda),
and
[`cloud-itonami-assoc-6419-deu-bankenverband`](https://github.com/cloud-itonami/cloud-itonami-assoc-6419-deu-bankenverband).
Part of the [`cloud-itonami`](https://github.com/cloud-itonami)
compliance-fact family (ADR-2607141700,
`cloud-itonami-compliance-fact-federation`, in `com-junkawasaki/root`).

Aligned to **ISIC 6612** (securities brokerage) — the SAME code as jsda
(JPN), enabling a direct cross-country securities-SRO comparison via the
federation query.

## Scope

A **read-only reference/archive** catalog — not an Advisor⊣Governor
actuation actor. It proposes or executes nothing on FINRA's behalf.

Coverage is reported honestly by the fail-closed exported Kotoba ABI: an
association not explicitly admitted has **no spec-basis**, full stop — never
fabricate one.

## Data

- `src/association_facts.kotoba` — the sole production catalog authority.
- `schema/association-rule.edn` — DataScript schema.
- `data/datascript-tx.edn` — derived DataScript tx-data (query this
  alongside other `cloud-itonami`/`etzhayyim` compliance-fact sources via
  `com-junkawasaki/root`'s `scripts/compliance-fact-query.cljs`).

Both entries cite an official [finra.org](https://www.finra.org/) page,
directly WebFetch-verified (2026-07-14).

The catalog compiles through `kotoba-lang/compiler` to the reference evaluator,
restricted JavaScript, and typed WebAssembly. Clojure/JVM and Node are test and
compiler hosts only; neither is production authority. Compatibility is checked
by observable values, typed ABI, empty effects, bounds, and fail-closed
rejections—not compiler-output byte identity.

## License

AGPL-3.0-or-later (matches the `cloud-itonami-iso3166-*` /
`-municipality-*` / `-assoc-*` / `-lei-*` convention). Rule text itself
remains FINRA's; this repo stores only citation metadata (id/title/url/
dates), not full rule text.
