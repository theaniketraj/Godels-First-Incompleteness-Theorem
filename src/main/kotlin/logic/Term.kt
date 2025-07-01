package logic

import java.math.BigInteger

/** Representation of terms in arithmetic: variables, zero, successor, addition, multiplication. */
sealed class Term

/** The constant 0. */
object Zero : Term() {
    override fun toString() = "0"
}

/** A variable (like x, x*, x**, ...). */
data class Var(val name: String) : Term() {
    init {
        require(name.isNotEmpty() && name[0] == 'x') { "Variable name must start with 'x'" }
    }
    override fun toString() = name
}

/** Successor: S(t). */
data class Succ(val t: Term) : Term() {
    override fun toString() = "S($t)"
}

/** Addition: (t1 + t2). */
data class Add(val left: Term, val right: Term) : Term() {
    override fun toString() = "($left + $right)"
}

/** Multiplication: (t1 × t2). */
data class Mul(val left: Term, val right: Term) : Term() {
    override fun toString() = "($left × $right)"
}

/*
 * Utility function to build the numeral for a given nonnegative BigInteger.
 * For example, numeral(3) = S(S(S(0))).  This is exponential in size, so use only for small n.
 */
fun numeral(n: BigInteger): Term {
    require(n >= BigInteger.ZERO) { "Numeral must be non-negative" }
    var result: Term = Zero
    // Add successor n times.
    var i = BigInteger.ZERO
    while (i < n) {
        result = Succ(result)
        i += BigInteger.ONE
    }
    return result
}
