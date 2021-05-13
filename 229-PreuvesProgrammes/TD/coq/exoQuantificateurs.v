
Parameters E : Set.

(* ne pas declarer la variable x utiliser dans la formule *)

(* ATTENTION, on écrit (P x) pas, P(x) ! *)

Parameters a : E.
Parameters P : E -> Prop.
Parameters Q : E -> Prop.

(* forall x:P(x) =>  exist y:P(y) \/ Q(y) *)

Lemma prop1 : forall x, P(x) -> (exists y : E, P(y) \/ Q(y)).
Proof.
intros.
exists x.
left.
assumption.
Qed.

Lemma prop2 : (exists x, P(x)\/ Q(x)) -> (exists x, P(x)) \/ (exists x, Q(x)).
Proof.
intro.
elim H; intros.
elim H0; intros.
intros.
left.
exists x.
assumption.
right.
exists x.
assumption.
Qed.


Lemma prop3 : (forall x, P(x)) /\ (forall x, Q(x)) -> forall x, P(x) /\ Q(x).
Proof.
intros.
elim H.
intros.
split.
apply (H0 x).
apply (H1 x).
Qed.

Lemma prop4 : (forall x, P(x) /\ Q(x)) -> (forall x, P(x)) /\ (forall x, Q(x)).
Proof.
intros.
split.
intro.
(* il ne faut pas utiliser apply mais plutot un : elim (H x) parce que ici les conclusions sont différentes et ce n'est pas le scope du apply *)
2: {
intro.
apply (H x).
}
apply (H x).
Qed.

Lemma prop5 : (forall x, ~P(x)) -> ~(exists x, P(x)).
Proof.
intro.
intro.
elim H0.
intros.
apply (H x).
assumption.
Qed.

Require Export Classical.
Check NNPP.

Lemma prop6 : ~(forall x, P(x)) -> exists x, ~P(x).
Proof.
intro.
apply NNPP.
intro.
apply H.
intro.
apply NNPP.
intro.
apply H0.
exists x.
assumption.
Qed.
