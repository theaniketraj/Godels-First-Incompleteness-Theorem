# Gödel’s First Incompleteness Theorem in Kotlin

This project implements a simple logic engine in Kotlin to illustrate Gödel’s First Incompleteness Theorem. It represents first-order Peano Arithmetic (PA), performs Gödel numbering of formulas, and constructs the self-referential Gödel sentence (one that says “I am not provable”).

## Project Structure

- **logic/Term.kt** – Defines the syntax for arithmetic *terms* (zero, successor, addition, multiplication, variables).
- **logic/Formula.kt** – Defines *formulas* (equality, order, logical connectives, quantifiers, and a special Proof predicate).
- **logic/PeanoAxioms.kt** – Contains example Peano axioms as `Formula` objects (e.g. “no successor of any number is 0”).
- **logic/GodelNumbering.kt** – Functions to encode any term or formula into its Gödel number (a `BigInteger`), by mapping symbols to digits.
- **Main.kt** – Demonstration code: builds example formulas, prints their Gödel numbers, and constructs the Gödel sentence `G = P(G(P))`. Comments explain each step.
- **build.gradle.kts** – Gradle build file for Kotlin (JVM target).

## Key Features

- **Term and Formula Classes:** Kotlin `data class` hierarchies (`Term`, `Formula`) allow building abstract syntax trees for logical expressions. Each class has a `toString` and a method to generate its symbol code sequence.
- **Gödel Encoding:** We assign each symbol (like `0`, `S`, `+`, `=`, `∀`, etc.) a unique digit string, and then join them with `0` separators to form a large number. This is implemented in `GodelEncoder.encodeFormulaToBigInt()`. For example, the formula `(x + S(0) = x + S(0))` gets mapped to a specific big integer.
- **Peano Axioms:** Axioms such as `∀x ¬(S(x) = 0)` (no number maps to 0 by successor) are hardcoded as `Formula` instances. Additional axioms (induction, etc.) can be similarly added.
- **Self-Reference (Gödel Sentence):** We programmatically build the formula `P(x) = ∀y ¬Proof(y,x)`, compute its Gödel number `g = ⌜P⌝`, and then instantiate `P(g)` to get the self-referential sentence `G`. This reflects the classic “This sentence is unprovable” construction.

## Running the Project

1. **Prerequisites:** Install JDK 11+ and [Gradle](https://gradle.org/), or use the Gradle wrapper.
2. **Build:** Run `gradle build` to compile.
3. **Run Demo:** Execute the `main` function (e.g. `gradle run`). It will print example formulas, their Gödel numbers, and show the Gödel sentence construction with explanations.
4. **Explore:** You can modify `Main.kt` to add more formulas, try different symbol assignments, or extend the logic engine.

This project is meant for educational purposes. It **demonstrates** the encoding and logic behind Gödel’s theorem, but it does not attempt to fully automate proofs. The comments and printouts guide the user through the key ideas of self-reference and incompleteness.
