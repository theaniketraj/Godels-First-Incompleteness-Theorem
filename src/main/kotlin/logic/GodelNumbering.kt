package logic

import java.math.BigInteger

/** Object to perform Gödel numbering (encode terms/formulas as numbers) and decoding if needed. */
object GodelEncoder {
    /**
     * Encode a Term into its Gödel number string (digits without separators). Here we actually use
     * encodeTerm() and join with zeros.
     */
    fun encodeTermToString(t: Term): String {
        return encodeTerm(t).joinToString("0")
    }
    /** Encode a Formula into a BigInteger Gödel number. */
    fun encodeFormulaToBigInt(f: Formula): BigInteger {
        val symbols = f.toSymbolCodes()
        val numStr = symbols.joinToString("0")
        return BigInteger(numStr)
    }
    /**
     * (Optional) Decoding functions could be implemented by reversing the process: parse the number
     * into digits and map back to symbols, then reconstruct Terms/Formulas. This is omitted for
     * brevity.
     */
}
