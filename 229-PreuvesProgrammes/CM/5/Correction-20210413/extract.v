(* $Id$ *)

Require Extraction.

(* Successor function *)

Definition succ (n : nat) : nat := S n.

Extraction succ.

(* Double function *)

Lemma double : forall n : nat, {x : nat | x = 2 * n}.
Proof.
  intro.
  exists (2 * n).
  reflexivity.
Defined.

Extraction double.

(* Factorial function *)

Inductive is_fact : nat -> nat -> Prop :=
| is_fact_O : is_fact 0 1
| is_fact_S : forall n v : nat, is_fact n v -> is_fact (S n) ((S n) * v).

Lemma fact : forall n : nat, { v : nat | is_fact n v }.
Proof.
  induction n.
  exists 1; apply is_fact_O.
  elim IHn; intros; exists ((S n) * x); apply is_fact_S; assumption.
Defined.

Extraction fact.

(* Map function over lists of natural numbers *)

Open Scope list_scope.

Inductive is_map (f : nat -> nat) : list nat -> list nat -> Prop :=
| is_map_nil : is_map f nil nil
| is_map_cons : forall (a : nat) (l v : list nat),
  is_map f l v -> is_map f (a :: l) (f a :: v).

Lemma map : forall (f : nat -> nat) (l : list nat),
  {v : list nat | is_map f l v}.
Proof.
  induction l.
  exists nil; apply is_map_nil.
  elim IHl; intros.
  exists (f a :: x); apply is_map_cons; assumption.
Defined.

Extraction map.

(* Equality over natural numbers *)

Lemma eq_nat : forall n m : nat, {n = m} + {n <> m}.
Proof.
  double induction n m; intros.
  left; reflexivity.
  right; discriminate.
  right; discriminate.
  elim (H0 n0); intro; [ rewrite a; left; reflexivity | right; auto ].
Defined.

Extraction eq_nat.

(* Successor function defined by tactics *)

Definition succ_tac (n : nat) : nat.
Proof.
  exact (S n).
Defined.

Print succ_tac.
Eval compute in (succ_tac 2).

(* Factorial function defined by tactics *)

Definition fact_tac (n : nat) : nat.
Proof.
  elim n.
  exact 1.
  intros; exact ((S n0) * H).
Defined.

Eval compute in (fact_tac 3).

(* Addition over natural numbers defined by tactics *)

Definition my_plus (n m : nat) : nat.
Proof.
  elim n.
  exact m.
  intros; exact (S H).
Defined.

Eval compute in (my_plus 2 3).
