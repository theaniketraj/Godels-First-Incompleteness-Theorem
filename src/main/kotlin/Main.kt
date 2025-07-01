import logic.*

fun main() {
    println("=== Gödel Incompleteness Demo in Kotlin ===\n")

    // Example: build a simple term and formula.
    val term = Add(Var("x"), Succ(Zero)) // x + S(0)
    println("Example term: $term")
    // Encode term as symbol list
    val termSymbols = encodeTerm(term)
    println("Symbol codes of term: ${termSymbols.joinToString(",")}")
    // Encode term into number (e.g. numeral for 5)
    val termNumber =
            GodelEncoder.encodeFormulaToBigInt(
                    Eq(term, term)
            ) // encode as formula Eq(term,term) for demo
    println("Gödel number of formula (x + S(0) = x + S(0)): $termNumber\n")

    // Show a Peano axiom and its Gödel number.
    PeanoAxioms.all.forEachIndexed { i, axiom ->
        println("Axiom ${i+1}: $axiom")
        val code = GodelEncoder.encodeFormulaToBigInt(axiom)
        println("  Gödel number: $code\n")
    }

    // Construct the Gödel sentence:
    // 1. Define variable symbols
    val x = Var("x")
    val y = Var("y")

    // 2. Define P(x) := ∀y ¬Proof(y, x).
    val PofX = ForAll(y, Not(Proof(y, x)))
    println("Formula P(x): $PofX")
    // Compute its Gödel number G(P).
    val gP = GodelEncoder.encodeFormulaToBigInt(PofX)
    println("Gödel number of P(x): $gP")

    // 3. Build the self-referential sentence G = P(⌜P⌝).
    //    Replace x with numeral(gP) in P(x): i.e. ∀y ¬Proof(y, gP).
    //    We represent gP as a term (in practice this term is extremely large).
    val numeralTerm = numeral(gP) // might be infeasible for very large gP
    val G_sentence = ForAll(y, Not(Proof(y, numeralTerm)))
    println("Gödel sentence G (P(g(P))): $G_sentence")
    val gG = GodelEncoder.encodeFormulaToBigInt(G_sentence)
    println("Gödel number of G: $gG")

    println("\nInterpretation: The sentence G says “G is not provable.”")
    println(
            "Gödel's argument shows that if Peano Arithmetic is consistent, G is not provable in the system:contentReference[oaicite:11]{index=11}."
    )
    println(
            "Likewise, its negation ¬G is not provable under ω-consistency:contentReference[oaicite:12]{index=12}."
    )
    println(
            "Thus PA cannot prove all true arithmetic statements, illustrating incompleteness:contentReference[oaicite:13]{index=13}."
    )
}
