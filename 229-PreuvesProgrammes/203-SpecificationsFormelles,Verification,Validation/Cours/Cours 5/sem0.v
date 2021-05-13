(* sem0.v *)

(**** Expressions ****)

(* Integer expressions *)

Require Export ZArith.
Open Scope Z.

Inductive expr : Set :=
| Cte : Z -> expr
| Plus : expr -> expr -> expr
| Moins : expr -> expr -> expr
| Mult : expr -> expr -> expr
| Div : expr -> expr -> expr.

Inductive eval : expr -> Z -> Prop :=
| ECte : forall c : Z , eval (Cte c) c
| EPlus : forall (e1 e2 : expr ) (v1 v2 v : Z),
  eval e1 v1 -> eval e2 v2 -> v = v1 + v2 -> eval (Plus e1 e2) v
| EMoins : forall (e1 e2 : expr) (v1 v2 v : Z),
  eval e1 v1 -> eval e2 v2 -> v = v1 - v2 -> eval (Moins e1 e2) v
| EMult : forall (e1 e2 : expr) (v1 v2 v : Z),
  eval e1 v1 -> eval e2 v2 -> v = v1 * v2 -> eval (Mult e1 e2) v
| EDiv : forall (e1 e2 : expr) (v1 v2 v : Z),
  eval e1 v1 -> eval e2 v2 -> v = v1 / v2 -> eval (Div e1 e2) v.

Lemma eval0 : eval (Plus (Cte 1) (Cte 1)) 2.
Proof.
  eapply EPlus.
  apply ECte.
  apply ECte.
  auto.
Save.

Lemma eval1 : eval (Mult (Plus (Cte 4) (Cte 2)) (Moins (Cte 9) (Cte 2))) 42.
Proof.
  eapply EMult.
  eapply EPlus.
  apply ECte.
  apply ECte.
  auto.
  eapply EMoins.
  apply ECte.
  apply ECte.
  auto.
  auto.
Save.

Ltac apply_eval :=
  repeat
    eapply EPlus || eapply EMoins || eapply EMult || eapply EDiv ||
    apply ECte || auto.

Lemma eval0b : eval (Plus (Cte 1) (Cte 1)) 2.
Proof.
  apply_eval.
Save.

Lemma eval1b :
  eval (Mult (Plus (Cte 4) (Cte 2)) (Moins (Cte 9) (Cte 2))) 42.
Proof.
  apply_eval.
Save.

Fixpoint f_eval (e : expr) : Z :=
  match e with
  | Cte c => c
  | Plus e1 e2 =>
    let v1 := f_eval e1 in
    let v2 := f_eval e2 in
    v1 + v2
  | Moins e1 e2 =>
    let v1 := f_eval e1 in
    let v2 := f_eval e2 in
    v1 - v2
  | Mult e1 e2 =>
    let v1 := f_eval e1 in
    let v2 := f_eval e2 in
    v1 * v2
  | Div e1 e2 =>
    let v1 := f_eval e1 in
    let v2 := f_eval e2 in
    v1 / v2
  end.

Lemma eval0t : f_eval (Plus (Cte 1) (Cte 1)) = 2.
Proof.
  simpl; reflexivity.
Save.

Lemma eval1t :
  f_eval (Mult (Plus (Cte 4) (Cte 2)) (Moins (Cte 9) (Cte 2))) = 42.
Proof.
  simpl; reflexivity.
Save.

Functional Scheme f_eval_ind := Induction for f_eval Sort Prop.

Theorem f_eval_sound :
  forall (e : expr) (v : Z), (f_eval e) = v -> eval e v.
Proof.
  intro; functional induction (f_eval e) using f_eval_ind; intros.
  rewrite H; apply ECte.
  apply EPlus with (v1 := (f_eval e1)) (v2 := (f_eval e2));
    [ apply (IHz (f_eval e1)); auto | apply (IHz0 (f_eval e2)); auto | auto ].
  apply EMoins with (v1 := (f_eval e1)) (v2 := (f_eval e2));
    [ apply (IHz (f_eval e1)); auto | apply (IHz0 (f_eval e2)); auto | auto ].
  apply EMult with (v1 := (f_eval e1)) (v2 := (f_eval e2));
    [ apply (IHz (f_eval e1)); auto | apply (IHz0 (f_eval e2)); auto | auto ].
  apply EDiv with (v1 := (f_eval e1)) (v2 := (f_eval e2));
    [ apply (IHz (f_eval e1)); auto | apply (IHz0 (f_eval e2)); auto | auto ].
Save.
