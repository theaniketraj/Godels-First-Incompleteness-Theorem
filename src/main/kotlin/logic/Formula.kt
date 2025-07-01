package logic

import java.math.BigInteger

/**
 * Representation of first-order formulas in Peano arithmetic.
 */
sealed class Formula {
    /** Collects all string tokens (symbol codes) representing this formula for Gödel encoding. */
    abstract fun toSymbolCodes(): List<String>
    override fun toString(): String = "FORMULA"
}

// Atomic formula: equality t1 = t2.
data class Eq(val left: Term, val right: Term) : Formula() {
    override fun toSymbolCodes(): List<String> {
        // Sequence: '(' left '=' right ')'
        val codes = mutableListOf<String>()
        codes += "6"  // '('
        codes += *encodeTerm(left).toTypedArray()
        codes += "4"  // '=' symbol code
        codes += *encodeTerm(right).toTypedArray()
        codes += "7"  // ')'
        return codes
    }
    override fun toString() = "($left = $right)"
}

// Atomic formula: less-than t1 < t2.
data class Lt(val left: Term, val right: Term) : Formula() {
    override fun toSymbolCodes(): List<String> {
        val codes = mutableListOf<String>()
        codes += "6"  // '('
        codes += *encodeTerm(left).toTypedArray()
        codes += "5"  // '<' symbol code
        codes += *encodeTerm(right).toTypedArray()
        codes += "7"  // ')'
        return codes
    }
    override fun toString() = "($left < $right)"
}

// Logical NOT.
data class Not(val f: Formula) : Formula() {
    override fun toSymbolCodes(): List<String> {
        // Sequence: '¬' then codes of (f)
        val codes = mutableListOf<String>()
        codes += "15"  // '¬' symbol code
        codes += *f.toSymbolCodes().toTypedArray()
        return codes
    }
    override fun toString() = "¬($f)"
}

// Logical AND.
data class And(val left: Formula, val right: Formula) : Formula() {
    override fun toSymbolCodes(): List<String> {
        // Sequence: '(' left '∧' right ')'
        val codes = mutableListOf<String>()
        codes += "6"  // '('
        codes += *left.toSymbolCodes().toTypedArray()
        codes += "13" // '∧' symbol code
        codes += *right.toSymbolCodes().toTypedArray()
        codes += "7"  // ')'
        return codes
    }
    override fun toString() = "($left ∧ $right)"
}

// Logical OR.
data class Or(val left: Formula, val right: Formula) : Formula() {
    override fun toSymbolCodes(): List<String> {
        val codes = mutableListOf<String>()
        codes += "6"  // '('
        codes += *left.toSymbolCodes().toTypedArray()
        codes += "14" // '∨' symbol code
        codes += *right.toSymbolCodes().toTypedArray()
        codes += "7"  // ')'
        return codes
    }
    override fun toString() = "($left ∨ $right)"
}

/** Universal quantifier: ∀ var (body). */
data class ForAll(val variable: Var, val body: Formula) : Formula() {
    override fun toSymbolCodes(): List<String> {
        // Sequence: '∀' var '(' body ')'
        val codes = mutableListOf<String>()
        codes += "11"  // '∀'
        // Encode variable name (x followed by stars)
        codes += "8"   // 'x'
        // Count '*' in name to add star symbols:
        val starCount = variable.name.count { it == '*' }
        repeat(starCount) { codes += "9" } // '*' symbol code
        codes += "6"  // '('
        codes += *body.toSymbolCodes().toTypedArray()
        codes += "7"  // ')'
        return codes
    }
    override fun toString() = "∀${variable.name}($body)"
}

/** Existential quantifier: ∃ var (body). */
data class Exists(val variable: Var, val body: Formula) : Formula() {
    override fun toSymbolCodes(): List<String> {
        // Sequence: '∃' var '(' body ')'
        val codes = mutableListOf<String>()
        codes += "12"  // '∃'
        codes += "8"   // 'x'
        val starCount = variable.name.count { it == '*' }
        repeat(starCount) { codes += "9" }
        codes += "6"   // '('
        codes += *body.toSymbolCodes().toTypedArray()
        codes += "7"
        return codes
    }
    override fun toString() = "∃${variable.name}($body)"
}

/** Proof predicate: Proof(p, f) meaning "p is a proof of formula code f". */
data class Proof(val proofNum: Term, val formulaNum: Term) : Formula() {
    override fun toSymbolCodes(): List<String> {
        // Sequence: '(' proofNum 'P' formulaNum ')'
        val codes = mutableListOf<String>()
        codes += "6"   // '('
        codes += *encodeTerm(proofNum).toTypedArray()
        codes += "17"  // 'P' code for Proof predicate
        codes += *encodeTerm(formulaNum).toTypedArray()
        codes += "7"   // ')'
        return codes
    }
    override fun toString() = "Proof($proofNum, $formulaNum)"
}

/*
 * Helper: encode a Term into list of symbol codes (as strings).
 */
fun encodeTerm(t: Term): List<String> = when (t) {
    is Zero   -> listOf("1")  // '0' -> code "1"
    is Var    -> {
        // Sequence: 'x' (code 8) followed by '*' codes for each star in name.
        val codes = mutableListOf<String>()
        codes += "8"
        val starCount = t.name.count { it == '*' }
        repeat(starCount) { codes += "9" }
        codes
    }
    is Succ   -> {
        // 'S' (2), '(' (6), encode subterm, ')' (7)
        val codes = mutableListOf<String>()
        codes += "2"
        codes += "6"
        codes += *encodeTerm(t.t).toTypedArray()
        codes += "7"
        codes
    }
    is Add    -> {
        // '(' left '+' right ')'
        val codes = mutableListOf<String>()
        codes += "6"
        codes += *encodeTerm(t.left).toTypedArray()
        codes += "3"  // '+' code
        codes += *encodeTerm(t.right).toTypedArray()
        codes += "7"
        codes
    }
    is Mul    -> {
        // '(' left '×' right ')'
        val codes = mutableListOf<String>()
        codes += "6"
        codes += *encodeTerm(t.left).toTypedArray()
        codes += "16" // '×' code
        codes += *encodeTerm(t.right).toTypedArray()
        codes += "7"
        codes
    }
}

/*
 * Function to encode a Formula into its Gödel number (BigInteger).
 * It concatenates all symbol codes with '0' separators and parses as a decimal integer.
 */
fun encodeFormula(f: Formula): BigInteger {
    val symbols: List<String> = f.toSymbolCodes()
    // Join with '0' separator:
    val numberStr = symbols.joinToString("0")
    // Parse as BigInteger (very large)
    return BigInteger(numberStr)
}
