(defun make-vm (nom)
	(setf (get nom 'R0) 0)
	(setf (get nom 'R1) 0)
	(setf (get nom 'R2) 0)
	(setf (get nom 'R3) 0)
	(setf (get nom 'R4) 0)
	(setf (get nom 'R5) 0)
	(setf (get nom 'R6) 0)
	(setf (get nom 'R7) 0)
	(setf (get nom 'PC) 0)
	(setf (get nom 'BP) 0)
	(setf (get nom 'FP) 0)
	(setf (get nom 'memoire) (make-array '(1000)))
)

(defun set-registre (vm registre valeur)
	(setf (get vm registre) valeur)
)

(defun get-registre (vm registre)
	(get vm registre)
)

(defun vm-load-code (nomdevm code))

(defun vm-load-fichier-code (nomdevm fichier))

(defun vm-exec-instr (nomdevm instruction)
	(case (get-instruction-nom instruction)
		('MOVE	(vm-exec-instr-move nomdevm (get-instruction-src instruction) (get-instruction-dest instruction)))
		('LOAD	(vm-exec-load nomdevm (get-instruction-src instruction) (get-instruction-dest instruction)))
		('STORE	(vm-exec-store nomdevm (get-instruction-src instruction) (get-instruction-dest instruction)))
		('ADD		(vm-exec-add nomdevm (get-instruction-src instruction) (get-instruction-dest instruction)))
		;;	to be implemented
		;;	INCR, DECR, MULT, SUB, DIV, PUSH, POP
		(t (error "ERREUR : Jeu d'instruction inconnu ~s% " instruction))
	)
)

(defun vm-exec-instr-move (nomdevm src dest)
 ;;   (set-registre nomdevm dest (get-registre nomdevm src))
    (set-registre nomdevm dest  src)
)

(defun vm-exec-instr-load (nomdevm src dest)
	;; de case src vers registre dest
	(set-registre nomdevm dest (aref (get-memoire nomdevm) src))
)

(defun vm-exec-instr-store (nomdevm src dest)
	;; de registre dest vers case src
	(setf (aref (get-memoire nomdevm) dest) (get-registre nomdevm src))
)

(defun vm-exec-instr-add (nomdevm src dest)
	(set-registre nomdevm src (+ (get-registre nomdevm src) (get-registre nomdevm dest)))
)

(defun vm-exec (nomdevm)
;; une boucle ATTENTION pas de récursivité
)

;; voir chapitre 6 'analyse cas par cas' lec.pdf

(defun compiler (expressionlisp)
)

;; (compiler '(fibo 15)) ⇒ '(...      (MOVE (:CONST 15) R0)
;; 														(PUSH R0)
;;														…
;; creation de l'environnement
;;														(JSR 'fibo)
;;														…
;; restitution de l'environnement
;;														(HALT)
;;													)

(defun compiler-save  (expressionlisp chemindefichier)
)

(defun vm-compiler-load-exec (vm expr)
;; ca compile une expression expr, charge le code ASM dans la vm et l exécute
;; utile pour appeler la fonction
)

(defun set-memoire (vm valeur)
	(setf (get-memoire vm) valeur)
)

(defun get-memoire (vm)
	(get-registre vm 'memoire)
)

(defun get-instruction-nom (instruction)
	;; instruction <=> instr src dest
	(car instruction)
	;; car instruction <=> instr
)

(defun get-instruction-src (instruction)
	;; instruction <=> instr src dest
	(cadr instruction)
	;; cadr instruction <=> src
)

(defun get-instruction-dest (instruction)
	;; instruction <=> instr src dest
	(caddr instruction)
	;; caddr instruction <=> dest
)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(setf nomMachine-1 'machine0)
(setf instruction1 '(MOVE 29  R3))

(make-vm nomMachine-1)
(vm-exec-instr nomMachine-1 instruction1)
