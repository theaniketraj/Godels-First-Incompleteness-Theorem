package logic

/** A few sample axioms of Peano Arithmetic, represented as Formula objects. */
object PeanoAxioms {
    // Axiom: ∀x ¬(S(x) = 0).  No successor equals zero.
    val axiom1 = ForAll(Var("x"), Not(Eq(Succ(Var("x")), Zero)))

    // Axiom: ∀x ∀y (S(x) = S(y) → x = y).  Successor is injective.
    val axiom2 =
            ForAll(
                    Var("x"),
                    ForAll(
                            Var("y"),
                            Or(Not(Eq(Succ(Var("x")), Succ(Var("y")))), Eq(Var("x"), Var("y")))
                    )
            )

    // Axiom: ∀x (x + 0 = x).
    val axiomAddZero = ForAll(Var("x"), Eq(Add(Var("x"), Zero), Var("x")))

    // Axiom: ∀x ∀y (x + S(y) = S(x + y)).
    val axiomAddSucc =
            ForAll(
                    Var("x"),
                    ForAll(
                            Var("y"),
                            Eq(Add(Var("x"), Succ(Var("y"))), Succ(Add(Var("x"), Var("y"))))
                    )
            )

    // (Additional axioms like for multiplication, induction scheme, etc., could be added
    // similarly.)
    val all = listOf(axiom1, axiom2, axiomAddZero, axiomAddSucc)
}
