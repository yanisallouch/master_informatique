(require "LISP_TO_LI.lisp")
(require "LI_TO_ASM.lisp")

(setq liste_registre '(R0 R1 R2 SP BP FP PC PCO))
(setq liste_drapeau '(FLT FEQ FGT FNIL))
(setq mem_size_default 200)

(defun is_register_? (registre)
  (position registre liste_registre))
;(trace is_register_?)

;permet d'acceder à la valeur du registre
(defun vm_get_register (vm registre)
  (if (is_register_? registre)
    (get vm registre)
    (warn "ERR 1: doit etre un registre")))
;(trace vm_get_register)

;permet de changer la valeur du registre
(defun vm_set_register (vm registre valeur)
  (if (is_register_? registre)
    (setf (get vm registre) valeur)
    (warn "ERR 2: doit etre un registre")))
(trace vm_set_register)

(defun is_flag_? (drapeau)
  (position drapeau liste_drapeau))
(trace is_register_?)

;permet d'acceder à la valeur du drapeau
(defun vm_get_flag (vm drapeau)
  (if (is_flag_? drapeau)
    (get vm drapeau)
    (warn "ERR 1: doit etre un drapeau")))
(trace vm_get_flag)

;permet de changer la valeur du drapeau
(defun vm_set_flag_ON (vm drapeau)
  (if (is_flag_? drapeau)
    (setf (get vm drapeau) T)
    (warn "ERR 2: doit etre un drapeau")))
(trace vm_set_flag_ON)

;permet de changer la valeur du drapeau
(defun vm_set_flag_OFF (vm drapeau)
  (if (is_flag_? drapeau)
    (setf (get vm drapeau) nil)
    (warn "ERR 2: doit etre un drapeau")))
(trace vm_set_flag_OFF)

(defun vm_init (vm &optional size)
	(if (null size)
		(setf size mem_size_default))
  (vm_init_memory vm size)
  (vm_set_register vm 'R0 nil)
  (vm_set_register vm 'R1 nil)
  (vm_set_register vm 'R2 nil)
    (vm_set_register vm 'SP (floor (/ size 2)));Plafond pile
    (vm_set_register vm 'BP (floor (/ size 2)));Plancher pile
    (vm_set_register vm 'FP (floor (/ size 2)));Pointeur de cadre
    (vm_set_register vm 'PC 0);Compteur ordinal pour execution
    (vm_set_register vm 'PCO 0);Compteur ordinal pour chargeur
    (vm_set_flag_OFF vm 'FLT);drapeau <
    (vm_set_flag_OFF vm 'FEQ);drapeau =
    (vm_set_flag_OFF vm 'FGT);drapeau >
    (vm_set_flag_OFF vm 'FNIL);drapeau test
  	(vm_init_hashTab_etq_resolu vm) ;table pour les ref en avance
    (vm_init_hashTab_etq_non_resolu vm)) ;table pour les ref en avance
(trace vm_init)

(defun vm_state (vm)
  (progn (print "=====Registres=====")
    (print "===Registres généraux===")
    (print (list 'RO (vm_get_register vm 'R0)))
    (print (list 'R1 (vm_get_register vm 'R1)))
    (print (list 'R2 (vm_get_register vm 'R2)))
    (print "===Registres dédiés===")
    (print (list 'SP (vm_get_register vm 'SP)))
    (print (list 'BP (vm_get_register vm 'BP)))
    (print (list 'FP (vm_get_register vm 'FP)))
    (print (list 'PC (vm_get_register vm 'PC)))
    (print (list 'PCO (vm_get_register vm 'PCO)))
    (print "=====Drapeaux=====")
    (print (list 'FLT (vm_get_flag vm 'FLT)))
    (print (list 'FEQ (vm_get_flag vm 'FEQ)))
    (print (list 'FGT (vm_get_flag vm 'FGT)))
    (print (list 'FNIL (vm_get_flag vm 'FNIL)))
    (print "=====Memory=====")
    (print (list 'memory (vm_state_memory vm)))
    (print "=====HashTab_etq=====")
    (print (list 'hashTab_etq (vm_state_hashTab_etq_resolu vm)))
    (print (list 'hashTab_etq (vm_state_hashTab_etq_non_resolu vm)))))
(trace vm_state)

(defun vm_state_old (vm)
  (and (print "=====Registres=====")
    (print "===Registres généraux===")
    (print (concatenate 'string "RO : " (write-to-string (vm_get_register vm 'R0))))
    (print (concatenate 'string "R1 : " (write-to-string (vm_get_register vm 'R1))))
    (print (concatenate 'string "R2 : " (write-to-string (vm_get_register vm 'R2))))
    (print "===Registres dédiés===")
    (print (concatenate 'string "SP : " (write-to-string (vm_get_register vm 'SP))))
    (print (concatenate 'string "BP : " (write-to-string (vm_get_register vm 'BP))))
    (print (concatenate 'string "FP : " (write-to-string (vm_get_register vm 'FP))))
    (print (concatenate 'string "PC : " (write-to-string (vm_get_register vm 'PC))))
    (print "=====Drapeaux=====")
    (print (concatenate 'string "FLT : " (write-to-string (vm_get_flag vm 'FLT))))
    (print (concatenate 'string "FEQ : " (write-to-string (vm_get_flag vm 'FEQ))))
    (print (concatenate 'string "FGT : " (write-to-string (vm_get_flag vm 'FGT))))
    (print (concatenate 'string "FNIL : " (write-to-string (vm_get_flag vm 'FNIL))))
    (print "=====Memory=====")
    (print (concatenate 'string "memory : (vm_state_memory '" (write-to-string vm) ")"))
    (print "=====HashTab_etq=====")
    (print (concatenate 'string "hashTab_etq : (vm_state_hashTab_etq_resolu '" (write-to-string vm) ")"))
    (print (concatenate 'string "hashTab_etq : (vm_state_hashTab_etq_non_resolu '" (write-to-string vm) ")"))
    (print "")))
(trace vm_state)

(defun vm_init_memory (vm size)
  (setf (get vm :memory) (make-array size))
  (setf (get vm :memory_size) size))

(defun vm_init_memory_1 (vm size)
  (setf (get vm :memory) (make-array size))
  (setf (get vm :memory_size) size)
  (loop for x from 0 to (- size 1)
    do (setf (aref (get vm :memory) x)'notInit)))
(trace vm_init_memory)

(defun vm_get_memory (vm)
  (get vm :memory))
(trace vm_get_memory)

(defun vm_get_memory_size (vm)
  (get vm :memory_size))
(trace vm_get_memory)

(defun vm_set_memory (vm dest)
  (warn "To Do"))
(trace vm_set_memory)

(defun vm_state_memory (vm)
  (list 'memory
    (vm_get_memory vm)))
(trace vm_state_memory)

(defun vm_state_memory_elt (vm i)
    (aref (vm_get_memory vm) i))
(trace vm_state_memory)

(defun vm_init_hashTab_etq_resolu (vm)
  (setf (get vm :hashTab_etq_resolu) (make-hash-table :test 'equal)))
(trace vm_init_hashTab_etq_resolu)

(defun vm_get_hashTab_etq_resolu (vm)
  (get vm :hashTab_etq_resolu))
(trace vm_get_hashTab_etq_resolu)

(defun vm_get_hashTab_etq_resolu_val (vm etq)
  (gethash etq (vm_get_hashTab_etq_resolu vm)))
(trace vm_get_hashTab_etq_resolu_val)

(defun vm_set_hashTab_etq_resolu (vm etq valeur)
  (setf (gethash etq (vm_get_hashTab_etq_resolu vm)) valeur))
(trace vm_set_hashTab_etq_resolu)

(defun vm_state_hashTab_etq_resolu (vm)
  (list 'hashTab_etq (vm_get_hashTab_etq_resolu vm)))
(trace vm_state_hashTab_etq_resolu)

(defun vm_init_hashTab_etq_non_resolu (vm)
  (setf (get vm :hashTab_etq_non_resolu ) (make-hash-table)))
(trace vm_init_hashTab_etq_non_resolu)

(defun vm_get_hashTab_etq_non_resolu (vm)
  (get vm :hashTab_etq_non_resolu ))
(trace vm_get_hashTab_etq_non_resolu)

(defun vm_get_hashTab_etq_non_resolu_val (vm etq)
  (gethash etq (vm_get_hashTab_etq_non_resolu  vm)))
(trace vm_get_hashTab_etq_non_resolu_val)

(defun vm_set_hashTab_etq_non_resolu (vm etq valeur)
  (setf (gethash etq (vm_get_hashTab_etq_non_resolu vm)) valeur))
(trace vm_set_hashTab_etq_non_resolu)

(defun vm_state_hashTab_etq_non_resolu (vm)
  (list 'hashTab_etq : (vm_get_hashTab_etq_non_resolu vm)))
(trace vm_state_hashTab_etq_non_resolu)

(defun vm_move (vm src dest)
  (if (not (is_register_? dest))
    (warn "ERR : <dest> : ~s doit être un registre" dest)
    (if (is_register_? src)
      (vm_set_register  vm dest (vm_get_register vm src))
      (vm_set_register  vm dest src))))
;(trace vm_move)

; (LOAD <src> <dest>
(defun vm_load (vm src dest)
  (print src)
  (if (not (is_register_? dest))
    (warn "ERR : <dest> : ~s doit être un registre" dest)
    (if (not (atom src))
      (cond
        ((eq (first src) '-)
          (vm_set_register vm dest (svref (vm_get_memory vm) (- (vm_get_register vm (second src)) (third src)))))
        ((eq (first src) '+)
          (vm_set_register vm dest (svref (vm_get_memory vm) (+ (vm_get_register vm (second src)) (third src)))))
        ((eq (first src) '*)
          (vm_set_register vm dest (svref (vm_get_memory vm) (* (vm_get_register vm (second src)) (third src)))))
        ((eq (first src) '/)
          (vm_set_register vm dest (svref (vm_get_memory vm) (/ (vm_get_register vm (second src)) (third src))))))
    (if (not (or (integerp src) (is_register_? src)))
      (warn "ERR : <src> : ~s doit être un registre ou (registre - n) ou une adresse mémoire (int)" src)
      (if (is_register_? src)
        (if (or (< (- (vm_get_memory_size vm) 1) (vm_get_register vm src)) (> 0 (vm_get_register vm src)))
          (warn "ERR : <src> l'adresse mémoire @~s est hors limite [~s , ~s]" (vm_get_register vm src) 0 (- (vm_get_memory_size vm) 1))
          (vm_set_register vm dest (svref (vm_get_memory vm) (vm_get_register vm src))))
        (if (or (< (- (vm_get_memory_size vm) 1) src) (> 0 src))
          (warn "ERR : <src> l'adresse mémoire @~s est hors limite [~s , ~s]" src 0 (- (vm_get_memory_size vm) 1))
          (vm_set_register vm dest (svref (vm_get_memory vm) src))))))))

(defun vm_load_old (vm src dest)
  (if (not (is_register_? dest))
    (warn "ERR : <dest> : ~s doit être un registre" dest)
    (if (not (or (integerp src) (is_register_? src)))
      (warn "ERR : <src> : ~s doit être un registre ou une adresse mémoire (int)" src)
      (if (is_register_? src)
        (if (or (< (- (vm_get_memory_size vm) 1) (vm_get_register vm src)) (> (vm_get_register vm 'BP) (vm_get_register vm src)))
          (warn "ERR : <src> l'adresse mémoire @~s est hors limite [~s , ~s]" (vm_get_register vm src) (vm_get_register vm 'BP) (- (vm_get_memory_size vm) 1))
          (vm_set_register vm dest (svref (vm_get_memory vm) (vm_get_register vm src))))
        (if (or (< (- (vm_get_memory_size vm) 1) src) (> (vm_get_register vm 'BP) src))
          (warn "ERR : <src> l'adresse mémoire @~s est hors limite [~s , ~s]" src (vm_get_register vm 'BP) (- (vm_get_memory_size vm) 1))
          (vm_set_register vm dest (svref (vm_get_memory vm) src)))))))

(defun vm_load_old_old (vm src dest)
  (if (not (is_register_? dest))
    (warn "ERR : <dest> : ~s doit être un registre" dest)
    (if (not (or (constantp src) (is_register_? src)))
      (warn "ERR : <src> : ~s doit être un registre ou une adresse mémoire (int)" src)
      (if (is_register_? src)
        (if (< (- (get vm :memory_size) 1) (vm_get_register src))
          (warn (concatenate 'string "ERR : <src> l'adresse mémoire @" (write-to-string src) " est hors limites [0 , " (write-to-string (- (get vm :memory_size) 1)) "]"))
          (let ((getSrc
            (svref (get vm :memory) (vm_get_register src))))
          (if (not getSrc)
            (warn "ERR : <src> l'emplacement mémoire est vide")
            (vm_set_register  vm dest getSrc))))
        (if (< (- (get vm :memory_size) 1) src)
          (warn (concatenate 'string "ERR : <src> l'adresse mémoire @" (write-to-string src) " est hors limites [0 , " (write-to-string (- (get vm :memory_size) 1)) "]"))
          (let ((getSrc
            (svref (get vm :memory) src)))
          (if (not getSrc)
            (warn "ERR : <src> l'emplacement mémoire est vide")
            (vm_set_register  vm dest getSrc))))))))
;(trace vm_load)

; (STORE <src> <dest> = chargement de registre à mémoire
  (defun vm_store (vm src dest)
    (if (not (is_register_? src))
      (warn "ERR : <src> : ~s doit être un registre" src)
      (if (not (or (integerp dest) (is_register_? dest)))
        (warn "ERR : <dest> : ~s doit être un registre ou une adresse mémoire (int)" dest)
        (if (is_register_? dest)
          (if (or (< (- (vm_get_memory_size vm) 1) (vm_get_register vm dest)) (> -1 (vm_get_register vm dest)))
            (warn "ERR : <dest> l'adresse mémoire @~s est hors limite [~s , ~s]" (vm_get_register vm dest) 0 (- (vm_get_memory_size vm) 1))
            (setf (aref (vm_get_memory vm) (vm_get_register vm dest)) (vm_get_register vm src)))
          (if (or (< (- (vm_get_memory_size vm) 1) dest) (> -1 dest))
            (warn "ERR : <dest> l'adresse mémoire @~s est hors limite [~s , ~s]" dest 0 (- (vm_get_memory_size vm) 1))
            (setf (aref (vm_get_memory vm) dest) (vm_get_register vm src)))))))

(defun vm_store_old (vm src dest)
  (if (not (is_register_? src))
    (warn "ERR : <src> : ~s doit être un registre" src)
    (if (not (or (integerp dest) (is_register_? dest)))
      (warn "ERR : <dest> : ~s doit être un registre ou une adresse mémoire (int)" dest)
      (if (is_register_? dest)
        (if (or (< (- (vm_get_memory_size vm) 1) (vm_get_register vm dest)) (> (vm_get_register vm 'BP) (vm_get_register vm dest)))
          (warn "ERR : <dest> l'adresse mémoire @~s est hors limite [~s , ~s]" (vm_get_register vm dest) (vm_get_register vm 'BP) (- (vm_get_memory_size vm) 1))
          (setf (aref (vm_get_memory vm) (vm_get_register vm dest)) (vm_get_register vm src)))
        (if (or (< (- (vm_get_memory_size vm) 1) dest) (> (vm_get_register vm 'BP) dest))
          (warn "ERR : <dest> l'adresse mémoire @~s est hors limite [~s , ~s]" dest (vm_get_register vm 'BP) (- (vm_get_memory_size vm) 1))
          (setf (aref (vm_get_memory vm) dest) (vm_get_register vm src)))))))

(defun vm_store_old_old (vm src dest)
  (if (not (is_register_? src))
    (warn "ERR : <src> doit être un registre")
    (if (not (or (constantp dest) (is_register_? dest)))
      (warn "ERR : <dest> doit être un registre ou une adresse mémoire (int)")
      (if (is_register_? dest)
        (setf (aref (get vm :memory) (vm_get_register vm dest)) (vm_get_register vm src))
        (if (< (- (get vm :memory_size) 1) dest)
          (warn (concatenate 'string "ERR : <dest> l'adresse mémoire @" (write-to-string dest) " est hors limites [0 , " (write-to-string (- (get vm :memory_size) 1)) "]"))
          (let ((oldV (vm_get_register vm src))
            (getDest (svref (get vm :memory) dest)))
          (setf (aref (get vm :memory) dest) (vm_get_register vm src))))))))
;(trace vm_store)

; (ADD <src> <dest>)  = addition
(defun vm_add (vm src dest)
  (cond
    ((not (is_register_? dest))
      (warn "ERR : <dest> doit être un registre"))
    ((atom src)
      (if (not (is_register_? src))
        (if (integerp src)
          (vm_move vm (+ (vm_get_register vm dest) src) dest)
          (warn "ERR : <src> doit être un registre ou '(:LIT . n) ou n avec n entier"))
        (vm_move vm (+ (vm_get_register vm src) (vm_get_register vm dest)) dest)))
    ((not (atom src))
      (if (and (eq (car src) ':LIT) (integerp (cdr src)))
        (vm_move vm (+ (cdr src) (vm_get_register vm dest)) dest)
        (warn "ERR : <src> doit être un registre ou (:LIT . n) ou n avec n entier")))))
;(trace vm_add)

; (SUB <src> <dest>)  = soustraction
(defun vm_sub (vm src dest)
  (cond
    ((not (is_register_? dest))
      (warn "ERR : <dest> doit être un registre"))
    ((atom src)
      (if (not (is_register_? src))
        (if (integerp src)
          (vm_move vm (- (vm_get_register vm dest) src) dest)
          (warn "ERR : <src> doit être un registre ou '(:LIT . n) ou n avec n entier"))
        (vm_move vm (- (vm_get_register vm dest) (vm_get_register vm src)) dest)))
    ((not (atom src))
      (if (and (eq (car src) ':LIT) (integerp (cdr src)))
        (vm_move vm (- (vm_get_register vm dest) (cdr src)) dest)
        (warn "ERR : <src> doit être un registre ou (:LIT n) avec n entier")))))
;(trace vm_sub)

; (MUL <src> <dest>)  = multiplication
(defun vm_mul (vm src dest)
  (cond
    ((not (is_register_? dest))
      (warn "ERR : <dest> doit être un registre"))
    ((atom src)
      (if (not (is_register_? src))
        (if (integerp src)
          (vm_move vm (* src (vm_get_register vm dest)) dest)
          (warn "ERR : <src> doit être un registre ou '(:LIT . n) ou n avec n entier"))
        (vm_move vm (* (vm_get_register vm src) (vm_get_register vm dest)) dest)))
    ((not (atom src))
      (if (and (eq (car src) ':LIT) (integerp (cdr src)))
        (vm_move vm (* (cdr src) (vm_get_register vm dest)) dest)
        (warn "ERR : <src> doit être un registre ou (:LIT n) avec n entier")))))
;(trace vm_mul)

; (DIV <src> <dest>)  = division
(defun vm_div (vm src dest)
  (cond
    ((not (is_register_? dest))
      (warn "ERR : <dest> doit être un registre"))
    ((atom src)
      (if (not (is_register_? src))
        (if (integerp src)
          (if (eq src 0)
            (warn "ERR : DIVISION PAR 0:<src> ")
            (vm_move vm (/ (vm_get_register vm dest) src) dest))
          (warn "ERR : <src> doit être un registre ou '(:LIT . n) ou n avec n entier"))
        (if (eq (vm_get_register vm src) 0)
          (warn "ERR : DIVISION PAR 0:<src> ")
          (vm_move vm (/ (vm_get_register vm dest) (vm_get_register vm src)) dest))))
    ((not (atom src))
      (if (and (eq (car src) ':LIT) (integerp (cdr src)))
        (if (eq 0 (cdr src))
          (warn "ERR : DIVISION PAR 0:<src> ")
          (vm_move vm (/ (vm_get_register vm dest) (cdr src)) dest))
        (warn "ERR : <src> doit être un registre ou (:LIT n) avec n entier")))))
;(trace vm_div)

; (INCR <dest>)     = incrément
(defun vm_incr (vm dest)
  (if (not (is_register_? dest))
    (warn "ERR3 : <dest> doit être un registre")
    (if (eq dest 'PC)
      (if (>= (vm_get_register vm 'PC) (vm_get_register vm 'BP))
        (warn "ERR : débordement pile instruction sur pile mémoire")
        (vm_set_register vm dest (+ (vm_get_register vm dest) 1)))
      (vm_set_register vm dest (+ (vm_get_register vm dest) 1)))))
;(trace vm_incr)

; (DECR <dest>)     = décrément
(defun vm_decr (vm dest)
  (if (not (is_register_? dest))
    (warn "ERR4 : <dest> doit être un registre")
    (vm_set_register vm dest (- (vm_get_register vm dest) 1))))
;(trace vm_decr)

; (PUSH <src>)      = empiler
(defun vm_push (vm src)
  (if (is_register_? src)
    (progn (vm_incr vm 'SP)
      (vm_store vm src 'SP))
    (progn (vm_incr vm 'SP)
      (vm_move vm src 'R0)
      (vm_store vm 'R0 (vm_get_register vm 'SP)))))
;(trace vm_push)

(defun vm_push_old (vm src)
  (if (atom src)
    (if (not (is_register_? src))
      (warn "ERR : <src> doit être un registre ou '(:LIT . n)")
      (progn (vm_incr vm 'SP)
        (vm_store vm src (vm_get_register vm 'SP))))
    (if (and (eq (car src) ':LIT) (integerp (cdr src)))
      (progn (vm_incr vm 'SP)
        (vm_move vm src 'R0)
        (vm_store vm 'R0 (vm_get_register vm 'SP)))
      (warn "ERR : <src> doit être un registre ou (:LIT n) avec n entier"))))
;(trace vm_push)

; (POP <dest>)      = dépiler
(defun vm_pop (vm dest)
  (if (not (is_register_? dest))
    (warn "ERR : <dest> doit être un registre")
    (progn (vm_load vm 'SP dest)
      (vm_decr vm 'SP))))
;(trace vm_pop)

; (LABEL <label>)   = déclaration d’étiquette
(defun vm_label (vm label)
  (if (consp label)
    (setf label (car label)))
  (if (not (vm_get_hashTab_etq_resolu_val vm label))
   (vm_set_hashTab_etq_resolu vm label (vm_get_register vm 'PCO))))

(defun vm_label_old (vm label)
  (vm_set_hashTab_etq_resolu vm label (vm_get_register vm 'SP))
  (if (vm_get_hashTab_etq_non_resolu_val vm label)
    (vm_move vm (vm_get_hashTab_etq_resolu_val vm label) 'PC)))

(defun vm_label_old_old (vm label)
  (vm_set_hashTab_etq_resolu vm label (vm_get_register vm 'SP)))
;(trace vm_label)

; (JMP <label>)     = saut inconditionnel à une étiquette ou une adresse
(defun vm_jmp (vm label)
  (print label)
  (read)
  (if (integerp label)
    (vm_move vm label 'PC)
    (progn
      (print '(vm_get_hashTab_etq_resolu_val vm label))
      (print (vm_get_hashTab_etq_resolu_val vm label))
      (if (vm_get_hashTab_etq_resolu_val vm label)
        (progn  (vm_move vm (vm_get_hashTab_etq_resolu_val vm label) 'PC)
          (vm_set_hashTab_etq_non_resolu vm label (vm_get_register vm 'SP)))))))

(defun vm_jmp_old (vm label)
  (if (integerp label)
    (vm_move vm (cons :lit label) 'PC)
    (vm_move vm (cons :lit (vm_get_hashTab_etq_val vm label)) 'PC)))
;(trace vm_jmp)

; (JSR <label>)     = saut avec retour
(defun vm_jsr (vm label)
  (print (list 'label label))
  (read)
  (vm_push vm 'PC)
  (vm_jmp vm label))
;(trace vm_jsr)

; (RTN)         = retour
(defun vm_rtn (vm)
  (progn (vm_move vm (vm_get_register vm 'SP) 'R0)
    (vm_decr vm 'SP)
    (vm_jmp vm (vm_get_register vm 'R0))))
;(trace vm_rtn)

(defun vm_rtn_old (vm)
  (and (vm_move vm (cons :lit (vm_get_register vm 'SP)) 'R0)
    (vm_decr vm 'SP)
    (vm_jmp vm (vm_get_register vm 'R0))))
;(trace vm_rtn)

(defun vm_test_constante (src)
  (if (not (atom src))
    (and (eq (car src) ':LIT) (integerp (cdr src)))))
;(trace vm_test_constante)

; (CMP <src1> <src2>) = comparaison
; (CMP R0 R1)
; (CMP R0 1)
; (CMP R0 (:lit . 1))
(defun vm_cmp (vm src1 src2)
  (print (list 'vm_cmp src1 src2))
  (if (is_register_? src1)
    (setf src1 (vm_get_register vm src1)))
  (if (vm_test_constante src1)
    (setf src1 (cdr src1)))
  (if (is_register_? src2)
    (setf src2 (vm_get_register vm src2)))
  (if (vm_test_constante src2)
    (setf src2 (cdr src2)))
  (print (list 'vm_cmp src1 src2))
  (read)
  (cond
    ((< src1 src2)
      (vm_set_flag_ON vm 'FLT)
      (vm_set_flag_OFF vm 'FEQ)
      (vm_set_flag_OFF vm 'FGT))
    ((= src1 src2)
      (vm_set_flag_OFF vm 'FLT)
      (vm_set_flag_ON vm 'FEQ)
      (vm_set_flag_OFF vm 'FGT))
    ((> src1 src2)
      (vm_set_flag_OFF vm 'FLT)
      (vm_set_flag_OFF vm 'FEQ)
      (vm_set_flag_ON vm 'FGT))))
;(trace vm_cmp)

(defun vm_cmp_old (vm src1 src2)
  (if (and (vm_test_constante src1) (vm_test_constante src2))
    (cond
      ((< (cdr src1) (cdr src2))
        (vm_set_flag_ON vm 'FLT)
        (vm_set_flag_OFF vm 'FEQ)
        (vm_set_flag_OFF vm 'FGT))
      ((= (cdr src1) (cdr src2))
        (vm_set_flag_OFF vm 'FLT)
        (vm_set_flag_ON vm 'FEQ)
        (vm_set_flag_OFF vm 'FGT))
      ((> (cdr src1) (cdr src2))
        (vm_set_flag_OFF vm 'FLT)
        (vm_set_flag_OFF vm 'FEQ)
        (vm_set_flag_ON vm 'FGT)))))

;(JGT <label>)     = saut si plus grand
(defun vm_jgt (vm label)
  (if (and  (not (vm_get_flag vm 'FLT))
    (not (vm_get_flag vm 'FEQ))
    (vm_get_flag vm 'FGT))
  (vm_jmp vm label)
  (vm_incr vm 'PC)))
;(trace vm_jgt)

; (JGE <label>)     = saut si plus grand ou égal
(defun vm_jge (vm label)
  (if (and  (not (vm_get_flag vm 'FLT))
    (or (vm_get_flag vm 'FEQ)
      (vm_get_flag vm 'FGT)))
  (vm_jmp vm label)
  (vm_incr vm 'PC)))
;(trace vm_jge)

; (JLT <label>)     = saut si plus petit
(defun vm_jlt (vm label)
  (if (and  (vm_get_flag vm 'FLT)
    (not (vm_get_flag vm 'FEQ))
    (not (vm_get_flag vm 'FGT)))
  (vm_jmp vm label)
  (vm_incr vm 'PC)))
;(trace vm_jlt)

; (JLE <label>)     = saut si plus petit ou égal
(defun vm_jle (vm label)
  (if (and  (or (vm_get_flag vm 'FLT)
    (vm_get_flag vm 'FEQ))
  (not (vm_get_flag vm 'FGT)))
  (vm_jmp vm label)
  (vm_incr vm 'PC)))
;(trace vm_jle)

; (JEQ <label>)     = saut si égal
(defun vm_jeq (vm label)
  (if (and  (not (vm_get_flag vm 'FLT))
    (vm_get_flag vm 'FEQ)
    (not (vm_get_flag vm 'FGT)))
  (vm_jmp vm label)
  (vm_incr vm 'PC)))
;(trace vm_jeq)

; (JNE <label>)     = saut si différent
(defun vm_jne (vm label)
  (if (and  (vm_get_flag vm 'FLT)
    (not (vm_get_flag vm 'FEQ))
    (vm_get_flag vm 'FGT))
  (vm_jmp vm label)
  (vm_incr vm 'PC)))
;(trace vm_jne)

; (TEST <src>)      = comparaison à NIL
(defun vm_test (vm src)
  (if src
    vm_set_flag_ON 'FNIL
    vm_set_flag_OFF 'FNIL))
;(trace vm_test)

; (JTRUE <label>)   = saut si non-NIL
(defun vm_jtrue (vm label)
  (if (vm_get_flag vm 'FNIL)
    (vm_jmp vm label)
    (vm_incr vm 'PC)))
;(trace vm_jtrue)

; (JNIL <label>)      = saut si NIL
(defun vm_jnil (vm label)
  (if (not (vm_get_flag vm 'FNIL))
    (vm_jmp vm label)
    (vm_incr vm 'PC)))
;(trace vm_jnil)

; (APPLY fun nbArgs)      = saut si NIL
(defun vm_apply (vm fun nbArgs)
  (let ((i nbArgs) (l nil))
  (loop while (> i 0) do
    (progn
      (vm_load vm (- (vm_get_register vm 'FP) i) 'R0)
      (print (vm_get_register vm 'R0))
      (if (atom (vm_get_register vm 'R0))
      (setf l (append l (list (vm_get_register vm 'R0))))
      (setf l (append l (list (cdr (vm_get_register vm 'R0))))))
      (print l)
      (setf i (- i 1))))
  (print "~~~~~ apply ~~~~~")
  (print l)
  (if (and (not (atom l))
    (not (eq nil (car l))))
    (vm_move vm (apply fun l) 'R0)))
    (read))
;(trace vm_jnil)

; (NOP)         = rien
(defun vm_nop (vm)
  )
;(trace vm_nop)

; (HALT)        = arrêt
(defun vm_halt (vm)
  (vm_set_flag_ON 'vm 'FNIL))
;(trace vm_halt)

(defun vm_chargeur(vm exprLisp)
  (let ((asm (LI_TO_ASM (LISP2LI exprLisp nil) 0)))
  (print "~~~~~~CHARGEUR~~~~~~")
  (loop
    while (not (atom asm))
    do
    (progn
      (print (car asm))
      (vm_move vm (car asm) 'R0)
      (vm_store vm 'R0 'PCO)
      (vm_load vm 'PCO 'R1)
      (if (eq (car (vm_get_register vm 'R1)) 'LABEL)
        (vm_label vm (cdr (vm_get_register vm 'R1))))
    (vm_incr vm 'PCO)
    (setf asm (cdr asm))))
  (vm_move vm '(HALT) 'R0)
  (vm_store vm 'R0 'PCO)
  (vm_incr vm 'PCO)))
;(trace vm_chargeur)

(defun vm_exec (vm exprLisp)
  (vm_move vm 'PCO 'PC)
  (print (list 'PCO (vm_get_register vm 'PC)))
  (vm_set_flag_OFF vm 'FNIL)
  (read)
  (vm_chargeur vm exprLisp)
  (print "~EXECUTION~")
  (loop
    ;while (vm_get_register vm 'R2)
    while (not (vm_get_flag vm 'FNIL))
    do
     (progn
      (vm_load vm 'PC 'R2)
      (let ((fun (car (vm_get_register vm 'R2)))
      (args (cdr (vm_get_register vm 'R2))))
    (vm_state vm)
    (print (vm_get_register vm 'R2))
    (read)
    (cond
      ((eq fun 'LOAD)
        (progn
          (vm_load vm (first args) (second args))
          (vm_incr vm 'PC)))
      ((eq fun 'STORE)
        (progn
          (vm_store vm (first args) (second args))
          (vm_incr vm 'PC)))
      ((eq fun 'MOVE)
        (progn
          (vm_move vm (first args) (second args))
          (vm_incr vm 'PC)))
      ((eq fun 'ADD)
        (progn
          (vm_add vm (first args) (second args))
          (vm_incr vm 'PC)))
      ((eq fun 'SUB)
        (progn
          (vm_sub vm (first args) (second args))
          (vm_incr vm 'PC)))
      ((eq fun 'MUL)
        (progn
          (vm_mul vm (first args) (second args))
          (vm_incr vm 'PC)))
      ((eq fun 'DIV)
        (progn
          (vm_div vm (first args) (second args))
          (vm_incr vm 'PC)))
      ((eq fun 'INCR)
        (progn
          (vm_incr vm (first args))
          (vm_incr vm 'PC)))
      ((eq fun 'DECR)
        (progn
          (vm_decr vm (first args))
          (vm_incr vm 'PC)))
      ((eq fun 'PUSH)
        (progn
          (vm_push vm (first args))
          (vm_incr vm 'PC)))
      ((eq fun 'POP)
        (progn
          (vm_pop vm (first args))
          (vm_incr vm 'PC)))
      ((eq fun 'LABEL)
        (progn
          (vm_label vm (first args))
          (vm_incr vm 'PC)))
      ((eq fun 'JMP)
        (progn
          (vm_jmp vm (first args))))
      ((eq fun 'JSR)
        (progn
          (vm_jsr vm (first args))))
      ((eq fun 'RTN)
        (progn
          (vm_rtn vm)))
      ((eq fun 'CMP)
        (progn
          (vm_cmp vm (first args) (second args))
          (vm_incr vm 'PC)))
      ((eq fun 'JGT)
        (progn
          (vm_jgt vm (first args))))
      ((eq fun 'JGE)
        (progn
          (vm_jge vm (first args))))
      ((eq fun 'JLT)
        (progn
          (vm_jlt vm (first args))))
      ((eq fun 'JLE)
        (progn
          (vm_jle vm (first args))))
      ((eq fun 'JEQ)
        (progn
          (vm_jeq vm (first args))))
      ((eq fun 'JNE)
        (progn
          (vm_jne vm (first args))))
      ((eq fun 'TEST)
        (progn
          (vm_test vm (first args))
          (vm_incr vm 'PC)))
      ((eq fun 'JTRUE)
        (progn
          (vm_jtrue vm (first args))))
      ((eq fun 'JNIL)
        (progn
          (vm_jnil vm (first args))))
      ((eq fun 'APPLY)
          (progn
            (vm_apply vm (first args) (second args))
            (vm_incr vm 'PC)))
      ((eq fun 'NOP)
        (progn
          (vm_nop vm)
          (vm_incr vm 'PC)))
      ((eq fun 'HALT)
        (progn
          (vm_halt vm)
          (vm_incr vm 'PC)))))))
  (print (list "Le resultat de " exprLisp "est :" (vm_get_register vm 'R0))))
;(trace vm_exec)

;Chargeur
(defun vm_read_asm (vm asm)
  (loop
    while (not (atom asm))
    do
    (progn (let ((fun (caar asm))
      (args (cdar asm))
      (rest (cdr asm)))
    (print (car asm))
    (cond
      ((eq fun 'LOAD)
        (progn
          (vm_load vm (first args) (second args))
          (setf asm rest)))
      ((eq fun 'STORE)
        (progn
          (vm_store vm (first args) (second args))
          (setf asm rest)))
      ((eq fun 'MOVE)
        (progn
          (vm_move vm (first args) (second args))
          (setf asm rest)))
      ((eq fun 'ADD)
        (progn
          (vm_add vm (first args) (second args))
          (setf asm rest)))
      ((eq fun 'SUB)
        (progn
          (vm_sub vm (first args) (second args))
          (setf asm rest)))
      ((eq fun 'MUL)
        (progn
          (vm_mul vm (first args) (second args))
          (setf asm rest)))
      ((eq fun 'DIV)
        (progn
          (vm_div vm (first args) (second args))
          (setf asm rest)))
      ((eq fun 'INCR)
        (progn
          (vm_incr vm (first args))
          (setf asm rest)))
      ((eq fun 'DECR)
        (progn
          (vm_decr vm (first args))
          (setf asm rest)))
      ((eq fun 'PUSH)
        (progn
          (vm_push vm (first args))
          (setf asm rest)))
      ((eq fun 'POP)
        (progn
          (vm_pop vm (first args))
          (setf asm rest)))
      ((eq fun 'LABEL)
        (progn
          (vm_label vm (first args))
          (setf asm rest)))
      ((eq fun 'JMP)
        (progn
          (vm_jmp vm (first args))
          (setf asm rest)))
      ((eq fun 'JSR)
        (progn
          (vm_jsr vm (first args))
          (setf asm rest)))
      ((eq fun 'RTN)
        (progn
          (vm_rtn vm)
          (setf asm rest)))
      ((eq fun 'CMP)
        (progn
          (vm_cmp vm (first args) (second args))
          (setf asm rest)))
      ((eq fun 'JGT)
        (progn
          (vm_jgt vm (first args))
          (setf asm rest)))
      ((eq fun 'JGE)
        (progn
          (vm_jge vm (first args))
          (setf asm rest)))
      ((eq fun 'JLT)
        (progn
          (vm_jlt vm (first args))
          (setf asm rest)))
      ((eq fun 'JLE)
        (progn
          (vm_jle vm (first args))
          (setf asm rest)))
      ((eq fun 'JEQ)
        (progn
          (vm_jeq vm (first args))
          (setf asm rest)))
      ((eq fun 'JNE)
        (progn
          (vm_jne vm (first args))
          (setf asm rest)))
      ((eq fun 'TEST)
        (progn
          (vm_test vm (first args))
          (setf asm rest)))
      ((eq fun 'JTRUE)
        (progn
          (vm_jtrue vm (first args))
          (setf asm rest)))
      ((eq fun 'JNIL)
        (progn
          (vm_jnil vm (first args))
          (setf asm rest)))
      ((eq fun 'NOP)
        (progn
          (vm_nop vm)
          (setf asm rest)))
      ((eq fun 'HALT)
        (progn
          (vm_halt vm)
          (setf asm rest))))))))
;(trace vm_read_asm)

(defun vm_new_read_asm (vm asm)
  (print "first read")
  (vm_new_read_asm_first_read vm asm)
  (loop
    while (> (length asm) (vm_get_register vm 'PC))
    do
    (progn (let ((inst (getListElt asm (vm_get_register vm 'PC))))
      (let ((fun (car inst))
        (args (cdr inst)))
      (print inst)
      (print "second read")
      (print (vm_get_register vm 'PC))
      (cond
        ((eq fun 'LOAD)
          (progn
            (vm_load vm (first args) (second args))
            (vm_incr vm 'PC)))
        ((eq fun 'STORE)
          (progn
            (vm_store vm (first args) (second args))
            (vm_incr vm 'PC)))
        ((eq fun 'MOVE)
          (progn
            (vm_move vm (first args) (second args))
            (vm_incr vm 'PC)))
        ((eq fun 'ADD)
          (progn
            (vm_add vm (first args) (second args))
            (vm_incr vm 'PC)))
        ((eq fun 'SUB)
          (progn
            (vm_sub vm (first args) (second args))
            (vm_incr vm 'PC)))
        ((eq fun 'MUL)
          (progn
            (vm_mul vm (first args) (second args))
            (vm_incr vm 'PC)))
        ((eq fun 'DIV)
          (progn
            (vm_div vm (first args) (second args))
            (vm_incr vm 'PC)))
        ((eq fun 'INCR)
          (progn
            (vm_incr vm (first args))
            (vm_incr vm 'PC)))
        ((eq fun 'DECR)
          (progn
            (vm_decr vm (first args))
            (vm_incr vm 'PC)))
        ((eq fun 'PUSH)
          (progn
            (vm_push vm (first args))
            (vm_incr vm 'PC)))
        ((eq fun 'POP)
          (progn
            (vm_pop vm (first args))
            (vm_incr vm 'PC)))
        ((eq fun 'LABEL)
          (progn
            (vm_label vm (first args))
            (vm_incr vm 'PC)))
        ((eq fun 'JMP)
          (progn
            (vm_jmp vm (first args))
            (vm_incr vm 'PC)))
        ((eq fun 'JSR)
          (progn
            (vm_jsr vm (first args))
            (vm_incr vm 'PC)))
        ((eq fun 'RTN)
          (progn
            (vm_rtn vm)
            (vm_incr vm 'PC)))
        ((eq fun 'CMP)
          (progn
            (vm_cmp vm (first args) (second args))
            (vm_incr vm 'PC)))
        ((eq fun 'JGT)
          (progn
            (vm_jgt vm (first args))))
        ((eq fun 'JGE)
          (progn
            (vm_jge vm (first args))))
        ((eq fun 'JLT)
          (progn
            (vm_jlt vm (first args))))
        ((eq fun 'JLE)
          (progn
            (vm_jle vm (first args))))
        ((eq fun 'JEQ)
          (progn
            (vm_jeq vm (first args))))
        ((eq fun 'JNE)
          (progn
            (vm_jne vm (first args))))
        ((eq fun 'TEST)
          (progn
            (vm_test vm (first args))
            (vm_incr vm 'PC)))
        ((eq fun 'JTRUE)
          (progn
            (vm_jtrue vm (first args))
            (vm_incr vm 'PC)))
        ((eq fun 'JNIL)
          (progn
            (vm_jnil vm (first args))
            (vm_incr vm 'PC)))
        ((eq fun 'APPLY)
          (progn
            (vm_apply vm (first args) (second args))
            (vm_incr vm 'PC)))
        ((eq fun 'NOP)
          (progn
            (vm_nop vm)
            (vm_incr vm 'PC)))
        ((eq fun 'HALT)
          (progn
            (vm_halt vm)
            (vm_incr vm 'PC)))))))))

(defun vm_new_read_asm_first_read (vm asm)
  (loop
    while (> (length asm) (vm_get_register vm 'PC))
    do
    (progn (let ((inst (getListElt asm (vm_get_register vm 'PC))))
      (let ((fun (car inst))
        (args (cdr inst)))
      (print "first read")
      (print inst)
      (print (vm_get_register vm 'PC))
      (if (eq fun 'LABEL)
        (progn
          (vm_label vm (first args))))
      (vm_incr vm 'PC)))))
  (vm_move vm 0 'PC))
;(trace vm_new_read_asm)

(defun getListElt (l i) (if (atom l) -1 (if (eq i 0) (car l) (getListElt (cdr l) (- i 1)))))

(defun vm_run_old (vm)
  (loop
    while (<= (vm_get_register vm 'BP) (vm_get_register vm 'PC))
    do
    (progn (print (concatenate 'string "PC : "(write-to-string (vm_get_register vm 'PC))))
      (vm_load vm 'PC 'R2)
      (print (vm_get_register vm 'R2))
      (vm_decr vm 'PC)
      (if (not (atom (vm_get_register vm 'R2)))
       (if (eq (car (vm_get_register vm 'R2)) ':call)
        (progn
          (print "Cas call" )
            (vm_load vm 'FP 'R1);nbArgs -> R1
            (let ((i (vm_get_register vm 'R1)) (l nil))
              (loop while (> i 0) do
                (progn
                  (vm_load vm (- (vm_get_register vm 'FP) i) 'R0)
                  (print (vm_get_register vm 'R0))
                  (setf l (append l (list (cdr (vm_get_register vm 'R0)))))
                  (print l)
                  (setf i (- i 1))
                  ))
              (vm_move vm (apply (cdr (vm_get_register vm 'R2)) l) 'R0)
              )))))))
