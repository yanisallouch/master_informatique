Require Export List.
Open Scope list_scope.
Import ListNotations.

Open Scope type_scope.
Section Iso_axioms.
Variables A B C : Set.
Axiom Com : A * B = B * A.
Axiom Ass : A * (B * C) = A * B * C.
Axiom Cur : (A * B -> C) = (A -> B -> C).
Axiom Dis : (A -> B * C) = (A -> B) * (A -> C).
Axiom P_unit : A * unit = A.
Axiom AR_unit : (A -> unit) = unit.
Axiom AL_unit : (unit -> A) = A.
End Iso_axioms.

Inductive is_sort : nat -> Prop :=
  | is_sort_0 : is_sort O
  | is_sort_S : forall n : nat,
  is_sort n -> is_sort (S n).

Print list.
Open Scope list.

Check (0 :: 1 :: nil).

is_sorted =

fun A : Type =>
fix is_sorted ( l : list A) {struct l} : list A :=
  match (car l) with
  | nil => (is_sorted l)
  end.