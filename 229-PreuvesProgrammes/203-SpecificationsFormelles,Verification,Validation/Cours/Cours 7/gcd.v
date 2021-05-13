(* gcd.v *)

Require Export Arith.
Require Export Recdef.
Require Export Omega.

Definition f_gcd (a b : nat * nat) := (fst a) + (snd a) < (fst b) + (snd b).

Function gcd (c : nat * nat) {wf f_gcd c} : option nat :=
  match c with
  | (0, 0) => None
  | (0, _) => None
  | (_, 0) => None
  | _ =>
    let n := fst c in
    let m := snd c in
    match (lt_eq_lt_dec n m) with
    | inleft a =>
      match a with
      | left _ => gcd (n, (m - n))
      | right _ => Some n
      end
    | inright a => gcd ((n - m), m)
    end
  end.
intros; cbv beta delta [f_gcd]; simpl; omega.
intros; cbv beta delta [f_gcd]; simpl; omega.
apply well_founded_ltof.
Defined.

Eval compute in (gcd (6, 15)).
Eval compute in (gcd (0, 15)).

Definition divides (r : nat) (c : nat * nat) :=
  r <> 0 -> (exists q : nat, (fst c) = q * r) /\
  (exists q : nat, (snd c) = q * r).

Definition diff_0 (c : nat * nat) := (fst c) <> 0 /\ (snd c) <> 0.

Lemma gcd_sound1 : forall (a b r : nat),
  diff_0 (a, b) -> gcd (a, b) = Some r -> divides r (a, b).
Proof.
(* To do *)
