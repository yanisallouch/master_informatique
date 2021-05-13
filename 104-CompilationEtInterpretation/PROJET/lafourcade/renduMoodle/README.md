# Comment executer ?

* Dans Unix :
  * Via le terminal sh*, faire interpreter le code Lisp contenu dans [all.lisp](./ALL.lisp)
  ```bash
  $ clisp ./ALL.lisp
  ```
## Les test
  * Le fichier [test.lisp](./TEST.lisp) contient du code lisp a tester selon la [spécification donnée](./SPECIFICATIONS.md).
  * Par défaut les **test** sont commentés a la fin du fichier [EVAL_LI.lisp](./EVAL_LI.lisp).
    * Il faut les decommenter et comme les **instructions trace** ne sont pas commenter, on peut vérifier l'execution de chaque code lisp a tester. (Voir les 6 exemples déjà commenter pour ajouter des tests)

# Détail d'implémentation

## Partie VM
* La machine virtuelle que nous utilisons dispose d'une mémoire (fini, mais suffisement grande afin de ne pas être géné) et de trois registres R0, R1 et R2.
* La mémoire est un ensemble fini de N cellules.
  * La première cellule a par définition le numéro 0, la dernière le numéro N-1.
  * On parlera plutôt d'adresse mémoire que de numéro de cellule.
* La pile est gérée dans la mémoire.
  * La pile commence à l'adresse indique dans le registre spécial BP (base pointer). A priori, on ne touchera jamais à ce registre.
  * Le sommet de pile est indiqué dans le registre spécial SP (stack pointer).
  * La pile est vide quand SP = BP.
  * On a toujours SP >= BP (la pile est montante).
  * Le compteur de programme est contenu dans un registre spécial PC (program counter).
* Pour l'instant, nous avons donc les registres suivants : R0, R1, R2, BP, SP, PC
  * Nous introduirons dans la suite, un pointeur de cadre (frame pointer) : FP.
* Pour les comparaison et les sauts conditionnel, on dispose de trois registres booléens (1 bit) appelés drapeaux (flag) : DPP, DE, DPG (pour drapeau plus petit, drapeau égal, drapeau plus grand).

* Un adressage correspond soit à un source <src> d'où est lue une valeur, soit à une destination <dest> où une valeur est écrite.

* `:CONST` - `:LIT`
  * Une valeur constante s'écrit directement précédée par le symbole `#`
  * Une valeur constante ne peut être qu'une source (une telle destination n'aurait pas de sens).

### Manuel - Instructions

(LOAD <src> <dest>) = chargement de mémoire à registre
(STORE <src> <dest> = chargement de registre à mémoire
(MOVE <src> <dest>) = mouvement de registre à registre
(ADD <src> <dest>) = addition
(SUB <src> <dest>) = soustraction
(MUL <src> <dest>) = multiplication
(DIV <src> <dest>) = division
(INCR <dest>) = incrément
(DECR <dest>) = décrément
(PUSH <src>) = empiler
(POP <dest>) = dépiler
(LABEL <label>) = déclaration d’étiquette
(JMP <label>) = saut inconditionnel à une étiquette
(JSR <label>) = saut avec retour
(RTN) = retour
(CMP <src1> <src2>) = comparaison
(JGT <label>) = saut si plus grand
(JGE <label>) = saut si plus grand ou égal
(JLT <label>) = saut si plus petit
(JLE <label>) = saut si plus petit ou égal
(JEQ <label>) = saut si égal
(JNE <label>) = saut si différent
(TEST <src>) = comparaison à NIL
(JTRUE <label>) = saut si non-NIL
(JNIL <label>) = saut si NIL
(NOP) = rien
(HALT) = arrêt
