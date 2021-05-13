(* N est le plus petit ensemble qui vérifie les propriétés suivantes :
1- O in N
2- Si n in N alors (S n) in N 

Les entiers naturels vérifient les 2 propriétés.
Mais {-1, 0, 1, ...} vérifie aussi les 2 propriétés.
{-2, -1, 0, 1, ...} aussi ! 

Tarski : on prend l'intersection de tous les ensembles vérifiant les 2 propriétés et c'est bien le plus petit. *)

Inductive my_nat : Set :=
| my_O : my_nat
| my_S : my_nat -> my_nat.

Check my_nat_ind.

(* my_nat_ind
     : forall P : my_nat -> Prop,
       P my_O ->
       (forall m : my_nat, P m -> P (my_S m)) -> forall m : my_nat, P m *)

Print nat.
Check O.
Check (S O).
Check 1.

Print plus.
Print Nat.add.

(* 
Nat.add = 
fix add (n m : nat) {struct n} : nat :=
  match n with
  | 0 => m
  | S p => S (add p m)
  end
     : nat -> nat -> nat

add (S p) m : add p m
R(S p, m) > R(p, m)
R (x, y) = x
(S p) > p : ordre structurel *)

Lemma ex1 : forall x : nat, 0 + x = x.
Proof.
  intro.
  simpl.
  reflexivity.
Qed.

Lemma ex2 : forall x : nat, x + 0 = x.
Proof.
  (* induction x. *)  
  intro.
  elim x.
  simpl; reflexivity.
  intros.
  simpl.
  rewrite H.
  reflexivity.
Qed.

Require Export List.
Open Scope list_scope.
Import ListNotations.

Print list.

(* Inductive list (A : Type) : Type :=
   | nil : list A
   | cons : A -> list A -> list A *)

Check (0 :: 1 :: 2 :: nil).

Print app.

(* 
app = 
fun A : Type =>
fix app (l m : list A) {struct l} : list A :=
  match l with
  | [] => m
  | a :: l1 => a :: app l1 m
  end
     : forall A : Type, list A -> list A -> list A *)

Lemma ex3 : forall (E : Type) (l : list E), nil ++ l = l.
Proof.
  intros.
  simpl.
  reflexivity.
Qed.

Lemma ex4 : forall (E : Type) (l : list E), l ++ nil = l.
Proof.
  intros.
  elim l.
  simpl.
  reflexivity.
  intros.
  simpl.
  rewrite H.
  reflexivity.
Qed.

Lemma prop1 : forall n: nat, n * 1 = n.
Proof.
  intro.
  elim n.
  simpl.
  reflexivity.
  intros.
  elim H; intros; simpl; rewrite H.
  rewrite H.
  reflexivity.  
Qed.


Fixpoint f (n : nat) : nat :=
  match n with
  | 0 => 1
  | (S p) => 2 * (f p)
  end.

Lemma prop2 : (f 10) = 1024.
Proof.
simpl.
reflexivity.
Qed.

(*Lemma prop3 : forall E : Type, forall I : (list E), forall A : E, rev (I ++ [A]) = A :: rev I .*)
Lemma prop3 : forall (E : Type) (L : (list E)) (A : E), rev (L ++ [A]) = A :: rev L .
Proof.
  intros.
  elim L; intros.
  simpl.
  reflexivity.
  simpl.
  rewrite H.
  simpl.
  reflexivity.
Qed.

Lemma prop4 : forall (E : Type) (L : (list E)), rev (rev L) = L .
Proof.
  intros.
  elim L; intros.
  simpl.
  reflexivity.
  simpl.
  (* rewrite prop3. *)
  rewrite (prop3 E (rev l) a).
  rewrite H.
  reflexivity.
Qed.




















