Require Import FunInd.
(* Fact function *)

(*1*)

Inductive is_fact : nat -> nat -> Prop :=
| is_fact_0 : is_fact 0 1
| is_fact_S : forall n m : nat, is_fact n m -> is_fact (S n) ((S n) * m).

(*2*)
Fixpoint fact (x : nat) : nat :=
  match x with
  | 0 => 1
  | (S n) => x * fact(n)
  end.

(*3*)
Functional Scheme fact_ind := Induction for fact Sort Prop.

Print fact_ind.
Check fact_ind.

(* Power function *)

Inductive is_power : nat->nat->nat->Prop :=
|  is_power_0 : forall x : nat, is_power x 0 1
|  is_power_S : forall n x r : nat, is_power x n r -> is_power x (S n) (r*x).

Fixpoint f_u (n x : nat) : nat :=
  match n with
  | 0 => 1
  | S p => x * f_u p x
  end.

Functional Scheme f_u_ind := Induction for f_u Sort Prop.
Check f_u_ind.

Lemma fact_sound : forall (n v : nat), (fact n) = v -> is_fact n v.
Proof.
  intro; functional induction (fact n) using fact_ind.
  intros; rewrite <- H; apply is_fact_0.
  intros; rewrite <- H; apply is_fact_S; apply IHn0; reflexivity.
Qed.