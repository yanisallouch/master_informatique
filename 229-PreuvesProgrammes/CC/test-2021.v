Parameters E : Set.
Parameters P : E -> Prop.
Parameters Q : E -> Prop.


Lemma fol_1 : (forall x, ((P x) -> (Q x))) -> (exists x, (P x)) -> exists x, (Q x).
Proof.
intros.
elim H0; intros.
exists x.
apply H.
assumption.
Qed.

Lemma fol_2 : (exists x, (P x) /\ (Q x)) -> (exists x, (P x)) /\ (exists x, (Q x)).
Proof.
intros.
split.
elim H; intros.
elim H0.
intros.
exists x.
apply H1.
elim H; intros.
elim H0; intros.
exists x.
assumption.
Qed.

Fixpoint eq_nat (n m : nat) : Prop :=
  match n, m with
  | O, O => True
  | O, S _ => False
  | S _, O => False
  | S n1 , S m1 => eq_nat n1 m1
  end.

Lemma eq_nat_1: forall (n:  nat),
 eq_nat n n.
Proof.
intros.
induction n.
simpl.
reflexivity.
simpl.
apply IHn.
Qed.

(*
Lemma nat_eq_2: forall (n m:  nat),
 n = m -> eq_nat n m .
Proof.
intros.
induction n.
induction m.
simpl; reflexivity.
elim H; simpl; reflexivity.
rewrite H.
induction m.
simpl; reflexivity.
elim H.
elim IHn.

Qed.
*)
(*
Lemma nat_eq_3: forall (n m:  nat),
eq_nat n m -> n = m.
Proof.
Qed.
*)
Require Import Arith.
Require Import Omega.
Require Export List.
Open Scope list_scope.
Import ListNotations.

Inductive is_rev : list nat -> list nat -> Prop :=
  | is_rev_nil : is_rev nil nil
  | is_rev_cons : forall ( n : nat ) ( l1 l2 v : list nat ) ,
  is_rev l1 l2 -> v = l2++[n] -> is_rev ( n::l1 ) v
  | is_rev_sym : forall ( l1 l2 : list nat ) , is_rev l1 l2 ->
  is_rev l2 l1 .

(*  
Lemma is_rev_1 : (is_rev [1; 2; 3] [3; 2; 1]).
Proof.

Qed.
*)