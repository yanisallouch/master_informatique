(* $Id$ *)

(**** Sorting algorithm ****)

Require Import Arith.
Require Import Omega.
Require Export List.
Open Scope list_scope.
Import ListNotations.

(* Specification *)

Inductive is_perm : list nat -> list nat -> Prop :=
| is_perm_cons : forall (a : nat) (l0 l1 : list nat),
    is_perm l0 l1 -> is_perm (a :: l0) (a :: l1)
| is_perm_append : forall (a : nat) (l : list nat),
    is_perm (a :: l) (l ++ a :: nil)
| is_perm_refl : forall l : list nat, is_perm l l
| is_perm_trans : forall l0 l1 l2 : list nat,
    is_perm l0 l1 -> is_perm l1 l2 -> is_perm l0 l2
| is_perm_sym : forall l1 l2 : list nat, is_perm l1 l2 -> is_perm l2 l1.

Lemma is_perm_ex1_decomposer : is_perm [1; 2; 3] [3; 2; 1].
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

Lemma is_perm_ex1_compact : is_perm [1; 2; 3] [3; 2; 1].
Proof.
  apply (is_perm_trans [1; 2; 3] ([2; 3] ++ [1]) [3; 2; 1]);
    [ apply is_perm_append | simpl ].
  apply (is_perm_trans [2; 3; 1] ([3; 1] ++ [2]) [3; 2; 1]);
    [ apply is_perm_append | simpl ].
  apply is_perm_cons.
  apply (is_perm_trans [1; 2] ([2] ++ [1]) [2; 1]);
    [ apply is_perm_append | simpl ].
  apply is_perm_refl.
Qed.

(* Ltac = Let tac |? Language tac *)

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

Inductive is_sorted : list nat -> Prop :=
| is_sorted_nil : is_sorted nil
| is_sorted_sing : forall n : nat, is_sorted (n::nil)
| is_sorted_cons : forall (n m : nat) (l : list nat),
    n <= m -> is_sorted (m::l) -> is_sorted (n::m::l).

Lemma is_sorted_ex1 : is_sorted [1; 2; 3; 4; 5].
Proof.
  apply is_sorted_cons.
  2: {
    apply is_sorted_cons.
    2: {
      apply is_sorted_cons.
      2: {
        apply is_sorted_cons.
        2: {
          apply is_sorted_sing.
        }
        auto.
      }
      auto.
    }
    auto.
  }
  auto.
Qed.

(* éclair de génie pendant mon écriture *)

Lemma is_sorted_ex1_v2 : is_sorted [1; 2; 3; 4; 5].
Proof.
  apply is_sorted_cons;  auto.
  apply is_sorted_cons;  auto.
  apply is_sorted_cons;  auto.
  apply is_sorted_cons;  auto.
  apply is_sorted_sing;  auto.
Qed.

(* J'ai envie d'utiliser l'écriture avec les [ ..|.. ] mais je ne comprends pas l'erreur ...*)


(* Automation *)

Ltac is_sorted_tac :=
  repeat
    (
    apply is_sorted_nil ||
     apply is_sorted_sing ||
     apply is_sorted_cons ||
     auto
    ).

Lemma is_sorted_ex2 : is_sorted [1; 2; 3; 4; 5].
Proof.
  is_sorted_tac.
Qed.

Lemma is_sorted_ex2_2 : is_sorted [0; 2; 4; 6; 8; 8; 10].
Proof.
  is_sorted_tac.
Qed.

Lemma is_sorted_ex3 : is_sorted [1].
Proof.
  is_sorted_tac.
Qed.

Lemma is_sorted_ex4 : is_sorted [].
Proof.
  is_sorted_tac.
Qed.

(* Sorting function: insertion sort *)

Fixpoint insert (x : nat) (l : list nat) {struct l} : list nat :=
  match l with
  | nil => x::nil
  | h::t =>
        (* le_dec = less equal decidable *)
        match le_dec x h with
        | left _ => x::h::t
        | right _ => h::(insert x t)
        end
  end.

Fixpoint isort (l : list nat) : list nat :=
  match l with
  | nil => nil
  | h::t => insert h (isort t)
  end.

Lemma isort_ex1 : isort [5; 4; 3; 2; 1] = [1; 2; 3; 4; 5].
Proof.
  simpl.
  reflexivity.
Qed.

(* Correctness proof *)

Lemma head_is_perm : forall (x1 x2 : nat) (l : list nat),
  is_perm (x1 :: x2 :: l) (x2 :: x1 :: l).
Proof.
  intros.
  apply is_perm_trans with (x2::l ++ x1::nil).
  apply is_perm_append.
  apply is_perm_cons.
  apply is_perm_sym.
  apply is_perm_append.
Qed.

Lemma insert_is_perm : forall (x : nat) (l : list nat),
  is_perm (x::l) (insert x l).
Proof.
  (* To do *)
Qed.

Lemma insert_is_sorted : forall (x : nat) (l : list nat),
  is_sorted l -> is_sorted (insert x l).
Proof.
  (* To do *)
Qed.

Lemma isort_correct : forall (l l' : list nat),
  l' = isort l -> is_perm l l' /\ is_sorted l'.
Proof.
  (* To do *)
Qed.

