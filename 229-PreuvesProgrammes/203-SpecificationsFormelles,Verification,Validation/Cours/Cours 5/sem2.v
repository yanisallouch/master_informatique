(* sem2.v *)

(**** Expressions ****)

(* Integer and boolean expressions with variables *)

Require Export ZArith.
Open Scope Z.
Open Scope list.

Inductive value : Set :=
| ValZ : Z -> value
| ValB : bool -> value
| Err : value.

Inductive env_value : Set :=
| EValZ : Z -> env_value
| EValB : bool -> env_value.

Definition context := list (nat * env_value).

Fixpoint get_val (i : nat) (env : context) : option env_value :=
  match env with
  | nil => None
  | (n, v)::tl =>
    match (eq_nat_dec i n) with
    | left _ => Some v
    | _ => get_val i tl
    end
  end.

Inductive expr : Set :=
| Cte : Z -> expr
| Var : nat -> expr
| Plus : expr -> expr -> expr
| Moins : expr -> expr -> expr
| Mult : expr -> expr -> expr
| Div : expr -> expr -> expr
| Vrai : expr
| Faux : expr
| Non : expr -> expr
| Et : expr -> expr -> expr
| Ou : expr -> expr -> expr
| Eg : expr -> expr -> expr
| Diff : expr -> expr -> expr
| Inf : expr -> expr -> expr
| InfEg : expr -> expr -> expr
| Sup :expr -> expr -> expr
| SupEg : expr -> expr -> expr.

Inductive eval (env : context) : expr -> value -> Prop :=
| ECte : forall c : Z , eval env (Cte c) (ValZ c)
| EVarZ : forall (i : nat) (v : Z), (get_val i env) = Some (EValZ v) ->
  eval env (Var i) (ValZ v)
| EVarB : forall (i : nat) (b : bool), (get_val i env) = Some (EValB b) ->
  eval env (Var i) (ValB b)
| EVar_Err : forall (i : nat), (get_val i env) = None -> eval env (Var i) Err
| EPlus : forall (e1 e2 : expr) (v1 v2 v : Z),
  eval env e1 (ValZ v1) -> eval env e2 (ValZ v2) -> v = v1 + v2 ->
  eval env (Plus e1 e2) (ValZ v)
| EPlus_Err1 : forall (e1 e2 : expr),
  eval env e1 Err -> eval env (Plus e1 e2) Err
| EPlus_Err2 : forall (e1 e2 : expr) (v1 : Z),
  eval env e1 (ValZ v1) -> eval env e2 Err -> eval env (Plus e1 e2) Err
| EPlus_Err3 : forall (e1 e2 : expr) (b : bool),
  eval env e1 (ValB b) -> eval env (Plus e1 e2) Err
| EPlus_Err4 : forall (e1 e2 : expr) (v1 : Z) (b : bool),
  eval env e1 (ValZ v1) -> eval env e2 (ValB b) -> eval env (Plus e1 e2) Err
| EMoins : forall (e1 e2 : expr) (v1 v2 v : Z),
  eval env e1 (ValZ v1) -> eval env e2 (ValZ v2) -> v = v1 - v2 ->
  eval env (Moins e1 e2) (ValZ v)
| EMoins_Err1 : forall (e1 e2 : expr),
  eval env e1 Err -> eval env (Moins e1 e2) Err
| EMoins_Err2 : forall (e1 e2 : expr) (v1 : Z),
  eval env e1 (ValZ v1) -> eval env e2 Err -> eval env (Moins e1 e2) Err
| EMoins_Err3 : forall (e1 e2 : expr) (b : bool),
  eval env e1 (ValB b) -> eval env (Moins e1 e2) Err
| EMoins_Err4 : forall (e1 e2 : expr) (v1 : Z) (b : bool),
  eval env e1 (ValZ v1) -> eval env e2 (ValB b) -> eval env (Moins e1 e2) Err
| EMult : forall (e1 e2 : expr) (v1 v2 v : Z),
  eval env e1 (ValZ v1) -> eval env e2 (ValZ v2) -> v = v1 * v2 ->
  eval env (Mult e1 e2) (ValZ v)
| EMult_Err1 : forall (e1 e2 : expr),
  eval env e1 Err -> eval env (Mult e1 e2) Err
| EMult_Err2 : forall (e1 e2 : expr) (v1 : Z),
  eval env e1 (ValZ v1) -> eval env e2 Err -> eval env (Mult e1 e2) Err
| EMult_Err3 : forall (e1 e2 : expr) (b : bool),
  eval env e1 (ValB b) -> eval env (Mult e1 e2) Err
| EMult_Err4 : forall (e1 e2 : expr) (v1 : Z) (b : bool),
  eval env e1 (ValZ v1) -> eval env e2 (ValB b) -> eval env (Mult e1 e2) Err
| EDiv : forall (e1 e2 : expr) (v1 v2 v : Z),
  eval env e1 (ValZ v1) -> eval env e2 (ValZ v2) -> v = v1 / v2 ->
  eval env (Div e1 e2) (ValZ v)
| EDiv_Err1 : forall (e1 e2 : expr),
  eval env e1 Err -> eval env (Div e1 e2) Err
| EDiv_Err2 : forall (e1 e2 : expr) (v1 : Z),
  eval env e1 (ValZ v1) -> eval env e2 Err -> eval env (Div e1 e2) Err
| EDiv_Err3 : forall (e1 e2 : expr) (b : bool),
  eval env e1 (ValB b) -> eval env (Div e1 e2) Err
| EDiv_Err4 : forall (e1 e2 : expr) (v1 : Z) (b : bool),
  eval env e1 (ValZ v1) -> eval env e2 (ValB b) -> eval env (Div e1 e2) Err
| EVrai : eval env Vrai (ValB true)
| EFaux : eval env Faux (ValB false)
| ENon : forall (e : expr) (v1 v2 : bool), eval env e (ValB v1) ->
  v2 = negb v1 -> eval env (Non e) (ValB v2)
| ENon_Err1 : forall e : expr, eval env e Err -> eval env (Non e) Err
| ENon_Err2 : forall (e : expr) (v : Z), eval env e (ValZ v) ->
  eval env (Non e) Err.

(* À continuer *)

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
