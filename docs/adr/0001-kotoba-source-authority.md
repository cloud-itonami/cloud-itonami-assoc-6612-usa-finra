# ADR 0001: Kotoba is the FINRA catalog source authority

- Status: Accepted
- Date: 2026-07-21

`src/association_facts.kotoba` is the sole production source. It preserves
both entries' absent establishment and revision dates, official finra.org
citations, and the asymmetric ordered topic sets (three for the rulebook,
one for the by-laws). Unknown associations, aliases, fields, topics, and
indexes fail closed; no effects are declared.

Conformance is observable semantics across the reference evaluator, restricted
JavaScript, and instantiated typed WebAssembly, including typed ABI, bounds,
effects, and rejection behavior. Compiler-output byte identity is not a
language gate. Clojure and the JVM are compiler/test hosts only.
