Parameters A B : Prop.
Lemma exo10 : (A -> B) -> (B -> A) -> (A <-> B).
Proof.
intros.
split.
2: {
  assumption.
}
1: {
  assumption.
}
Qed.

Lemma exo10v2 : (A -> B) -> (B -> A) -> (A <-> B).
Proof.
intros.
split.
assumption.
assumption.
Qed.