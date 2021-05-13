(*Exercice (factorielle) :*)
    
Require Import FunInd.

Inductive is_fact : nat -> nat -> Prop :=
| is_fact_0 : is_fact 0 1
| is_fact_S : forall n m : nat, is_fact n m -> is_fact (S n) ((S n) * m).

Fixpoint fact (n : nat) : nat :=
  match n with
  | 0 => 1
  | (S n) => (S n) * (fact n)
  end.
  
Functional Scheme fact_ind := Induction for fact Sort Prop.

Lemma fact_sound : forall (n v : nat), (fact n) = v -> is_fact n v.
Proof.
  intro; functional induction (fact n) using fact_ind.
  intros; rewrite <- H; apply is_fact_0.
  intros; rewrite <- H; apply is_fact_S; apply IHn0; reflexivity.
Qed.


(*
Exercice (tri par insertion) :
*)
    
Require Export List.
Open Scope list_scope.
Import ListNotations.
  
Inductive is_perm : list nat -> list nat -> Prop :=
| is_perm_cons : forall (a : nat) (l0 l1 : list nat),
    is_perm l0 l1 -> is_perm (a :: l0) (a :: l1)
| is_perm_append : forall (a : nat) (l : list nat),
    is_perm (a :: l) (l ++ a :: nil)
| is_perm_refl : forall l : list nat, is_perm l l
| is_perm_trans : forall l0 l1 l2 : list nat,
    is_perm l0 l1 -> is_perm l1 l2 -> is_perm l0 l2
| is_perm_sym : forall l1 l2 : list nat, is_perm l1 l2 -> is_perm l2 l1.

Lemma is_perm_ex1 : is_perm [1; 2; 3] [3; 2; 1].
Proof.
  apply (is_perm_trans [1; 2; 3] ([2; 3] ++ [1]) [3; 2; 1]).
  apply is_perm_append.
  simpl.
  apply (is_perm_trans [2; 3; 1] ([3; 1] ++ [2]) [3; 2; 1]).
  apply is_perm_append.
  simpl.
  apply is_perm_cons.
  apply (is_perm_trans [1; 2] ([2] ++ [1]) [2; 1]).
  apply is_perm_append.
  simpl.
  apply is_perm_refl.
Qed.


Ltac is_perm_tac :=
  repeat
    (apply is_perm_refl || apply is_perm_cons ||
     (match goal with
      | |- is_perm (?a1::?tl1) ?l =>
        apply (is_perm_trans (a1::tl1) (tl1 ++ a1::nil) l);
          [ apply is_perm_append | simpl ]
      end)).

Lemma is_perm_ex2 : is_perm [1; 2; 3] [3; 2; 1].
Proof.
  is_perm_tac.
Qed.

