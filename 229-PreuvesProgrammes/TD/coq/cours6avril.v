Lemma eq_nat : forall n m : nat, {n = m} + { n <> m}.
Proof.
  (* decide equality *)
  double induction n m ; intros.
  left.
  reflexivity.
  right.
  discriminate.
  right.
  discriminate.
  elim (H0 n0); intros.
  left.
  rewrite a.
  reflexivity.
  right.
  auto.
Qed.

Lemma eq_nat2 : forall n m : nat, {n = m} + { n <> m}.
Proof.
  (* decide equality *)
  induction n; induction m ; intros.
  left.
  reflexivity.
  right.
  discriminate.
  right.
  discriminate.
  elim (IHn m); intros.
  left.
  auto.
  right.
  auto.
Qed.