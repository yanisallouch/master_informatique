(* sem1.v *)

(**** Expressions ****)

(* Integer expressions with variables *)

Require Export ZArith.
Open Scope Z.

Inductive value : Set :=
| Val : Z -> value
| Err : value.

Definition context := nat -> (option Z).

Inductive expr : Set :=
| Cte : Z -> expr
| Var : nat -> expr
| Plus : expr -> expr -> expr
| Moins : expr -> expr -> expr
| Mult : expr -> expr -> expr
| Div : expr -> expr -> expr.

Inductive eval (env : context) : expr -> value -> Prop :=
| ECte : forall c : Z , eval env (Cte c) (Val c)
| EVar : forall (i : nat) (v : Z), (env i) = Some v -> eval env (Var i) (Val v)
| EVar_Err : forall (i : nat), (env i) = None -> eval env (Var i) Err
| EPlus : forall (e1 e2 : expr) (v1 v2 v : Z),
  eval env e1 (Val v1) -> eval env e2 (Val v2) -> v = v1 + v2 ->
  eval env (Plus e1 e2) (Val v)
| EPlus_Err1 : forall (e1 e2 : expr),
  eval env e1 Err -> eval env (Plus e1 e2) Err
| EPlus_Err2 : forall (e1 e2 : expr) (v1 : Z),
  eval env e1 (Val v1) -> eval env e2 Err -> eval env (Plus e1 e2) Err
| EMoins : forall (e1 e2 : expr) (v1 v2 v : Z),
  eval env e1 (Val v1) -> eval env e2 (Val v2) -> v = v1 - v2 ->
  eval env (Moins e1 e2) (Val v)
| EMoins_Err1 : forall (e1 e2 : expr),
  eval env e1 Err -> eval env (Moins e1 e2) Err
| EMoins_Err2 : forall (e1 e2 : expr) (v1 : Z),
  eval env e1 (Val v1) -> eval env e2 Err -> eval env (Moins e1 e2) Err
| EMult : forall (e1 e2 : expr) (v1 v2 v : Z),
  eval env e1 (Val v1) -> eval env e2 (Val v2) -> v = v1 * v2 ->
  eval env (Mult e1 e2) (Val v)
| EMult_Err1 : forall (e1 e2 : expr),
  eval env e1 Err -> eval env (Mult e1 e2) Err
| EMult_Err2 : forall (e1 e2 : expr) (v1 : Z),
  eval env e1 (Val v1) -> eval env e2 Err -> eval env (Mult e1 e2) Err
| EDiv : forall (e1 e2 : expr) (v1 v2 v : Z),
  eval env e1 (Val v1) -> eval env e2 (Val v2) -> v = v1 / v2 ->
  eval env (Div e1 e2) (Val v)
| EDiv_Err1 : forall (e1 e2 : expr),
  eval env e1 Err -> eval env (Div e1 e2) Err
| EDiv_Err2 : forall (e1 e2 : expr) (v1 : Z),
  eval env e1 (Val v1) -> eval env e2 Err -> eval env (Div e1 e2) Err.

Definition env0 (n : nat) : (option Z) :=
  match n with
  | 0%nat => Some 2
  | _ => None
  end.

Lemma eval0 : eval env0 (Plus (Var 0) (Cte 1)) (Val 3).
Proof.
  eapply EPlus.
  apply EVar; simpl; auto.
  apply ECte.
  auto.
Save.

Lemma eval1 :
  eval env0 (Mult (Plus (Cte 4) (Var 0)) (Moins (Cte 9) (Var 0))) (Val 42).
Proof.
  eapply EMult.
  eapply EPlus.
  apply ECte.
  apply EVar; simpl; auto.
  auto.
  eapply EMoins.
  apply ECte.
  apply EVar; simpl; auto.
  auto.
  auto.
Save.

Lemma eval2 :
  eval env0 (Mult (Plus (Cte 4) (Var 1)) (Moins (Cte 9) (Var 0))) Err.
Proof.
  apply EMult_Err1.
  eapply EPlus_Err2.
  apply ECte.
  apply EVar_Err; simpl; auto.
Save.

Ltac apply_eval_val :=
  repeat
    eapply EPlus || eapply EMoins || eapply EMult || eapply EDiv ||
    apply ECte || (apply EVar; simpl; auto) || auto.

Ltac apply_eval_err :=
  match goal with
  | |- eval _ (Var _) Err => apply EVar_Err; simpl; auto
  | |- eval _ (Plus _ _) Err =>
    (apply EPlus_Err1; apply_eval_err) ||
    (eapply EPlus_Err2; [ apply_eval_val | apply_eval_err])
  | |- eval _ (Moins _ _) Err =>
    (apply EMoins_Err1; apply_eval_err) ||
    (eapply EMoins_Err2; [ apply_eval_val | apply_eval_err])
  | |- eval _ (Mult _ _) Err =>
    (apply EMult_Err1; apply_eval_err) ||
    (eapply EMult_Err2; [ apply_eval_val | apply_eval_err])
  | |- eval _ (Div _ _) Err =>
    (apply EDiv_Err1; apply_eval_err) ||
    (eapply EDiv_Err2; [ apply_eval_val | apply_eval_err])
  end.

Lemma eval0b : eval env0 (Plus (Var 0) (Cte 1)) (Val 3).
Proof.
  apply_eval_val.
Save.

Lemma eval1b :
  eval env0 (Mult (Plus (Cte 4) (Var 0)) (Moins (Cte 9) (Var 0))) (Val 42).
Proof.
  apply_eval_val.
Save.

Lemma eval2b :
  eval env0 (Mult (Plus (Cte 4) (Var 1)) (Moins (Cte 9) (Var 0))) Err.
Proof.
  apply_eval_err.
Save.

Fixpoint f_eval (env : context) (e : expr) : value :=
  match e with
  | Cte c => Val c
  | Var i => 
    match (env i) with
    | Some v => Val v
    | None => Err
    end
  | Plus e1 e2 =>
    let v1 := f_eval env e1 in
    match v1 with
    | Err => Err
    | Val z1 =>
      let v2 := f_eval env e2 in
      match v2 with
      | Err => Err
      | Val z2 => Val (z1 + z2)
      end
    end
  | Moins e1 e2 =>
    let v1 := f_eval env e1 in
    match v1 with
    | Err => Err
    | Val z1 =>
      let v2 := f_eval env e2 in
      match v2 with
      | Err => Err
      | Val z2 => Val (z1 - z2)
      end
    end
  | Mult e1 e2 =>
    let v1 := f_eval env e1 in
    match v1 with
    | Err => Err
    | Val z1 =>
      let v2 := f_eval env e2 in
      match v2 with
      | Err => Err
      | Val z2 => Val (z1 * z2)
      end
    end
  | Div e1 e2 =>
    let v1 := f_eval env e1 in
    match v1 with
    | Err => Err
    | Val z1 =>
      let v2 := f_eval env e2 in
      match v2 with
      | Err => Err
      | Val z2 => Val (z1 / z2)
      end
    end
  end.

Lemma eval0t :  f_eval env0 (Plus (Var 0) (Cte 1)) = (Val 3).
Proof.
  simpl; reflexivity.
Save.

Lemma eval1t :
  f_eval env0 (Mult (Plus (Cte 4) (Var 0)) (Moins (Cte 9) (Var 0))) = (Val 42).
Proof.
  simpl; reflexivity.
Save.

Lemma eval2t :
  f_eval env0 (Mult (Plus (Cte 4) (Var 1)) (Moins (Cte 9) (Var 0))) = Err.
Proof.
  simpl; reflexivity.
Save.

Functional Scheme f_eval_ind := Induction for f_eval Sort Prop.

Theorem f_eval_sound : forall (env : context) (e : expr) (v : value),
  (f_eval env e) = v -> eval env e v.
Proof.
  do 2 intro; functional induction (f_eval env e) using f_eval_ind; intros.
  rewrite <- H; apply ECte.
  rewrite <- H; apply EVar; auto.
  rewrite <- H; apply EVar_Err; auto.
  rewrite <- H;
    apply EPlus with (v1 := z1) (v2 := z2);
      [ apply (IHv (Val z1)); auto | apply (IHv0 (Val z2)); auto | auto ].
  rewrite <- H;
    apply EPlus_Err2 with (v1 := z1);
      [ apply (IHv (Val z1)); auto | apply (IHv0 Err); auto ].
  rewrite <- H; apply EPlus_Err1; apply (IHv Err); auto.
  rewrite <- H;
    apply EMoins with (v1 := z1) (v2 := z2);
      [ apply (IHv (Val z1)); auto | apply (IHv0 (Val z2)); auto | auto ].
  rewrite <- H;
    apply EMoins_Err2 with (v1 := z1);
      [ apply (IHv (Val z1)); auto | apply (IHv0 Err); auto ].
  rewrite <- H; apply EMoins_Err1; apply (IHv Err); auto.
  rewrite <- H;
    apply EMult with (v1 := z1) (v2 := z2);
      [ apply (IHv (Val z1)); auto | apply (IHv0 (Val z2)); auto | auto ].
  rewrite <- H;
    apply EMult_Err2 with (v1 := z1);
      [ apply (IHv (Val z1)); auto | apply (IHv0 Err); auto ].
  rewrite <- H; apply EMult_Err1; apply (IHv Err); auto.
  rewrite <- H;
    apply EDiv with (v1 := z1) (v2 := z2);
      [ apply (IHv (Val z1)); auto | apply (IHv0 (Val z2)); auto | auto ].
  rewrite <- H;
    apply EDiv_Err2 with (v1 := z1);
      [ apply (IHv (Val z1)); auto | apply (IHv0 Err); auto ].
  rewrite <- H; apply EDiv_Err1; apply (IHv Err); auto.
Save.
