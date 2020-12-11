;; main function for the compiler
(defun compiler (exp &optional (env ()) (envf ())  (namef ()) )
;;args of the instruction is the cdr of this list that represent the expression
  (let ((arg (if (atom exp) () (cdr exp))))
  ;;testing all the case posisble
	(cond
	 ;;case ATOM
	 ((atom exp)
		(compilation-litt exp env envf namef)
	 )
	 ;;end case ATOM

	 ;;case OPERATION
	 ((member (car exp) '(+ - * /))
		(compilation-op exp env envf namef)
	 )
	 ;;end case OPERATION


	 ;;case COMPARE
	 ((member (car exp) '(< > = <= >= ))
		(compilation-comp exp env envf namef)
	 )
	 ;;end case COMPARE

	 ;;case AND
	 ((isAPossibleCase exp 'and)
		(compilation-and arg (gensym "finAnd") env envf namef)
	 )
	 ;;end AND

	 ;;case OR
	 ((isAPossibleCase exp 'or) (compilation-or arg (gensym "finOr") env envf namef))
	 ;;case IF
	 ((isAPossibleCase exp 'if) (compilation-if arg env envf namef))
	 ;;case COND
	 ((isAPossibleCase exp 'cond) (compilation-cond arg (gensym "fincond") env envf namef))
	 ;;case PROGN
	 ((isAPossibleCase exp 'progn) (compilation-progn arg env envf namef))
	 ;;case LOOP
	 ((isAPossibleCase exp 'loop) (compilation-boucle arg env envf namef))
	 ;;CASE SETF
	 ((isAPossibleCase exp 'setf) (compilation-setf arg env envf namef))
	 ;;case DEFUN
	 ((isAPossibleCase exp 'defun) (compilation-defun arg env envf namef))
	 ;;case LET
	 ((isAPossibleCase exp 'let ) (compilation-let arg env envf namef))
	 ;;case LABELS
	 ((isAPossibleCase exp 'labels) (compilation-labels arg env envf namef))


	 ((and (consp (car exp)) (eql (caar exp) 'lambda)) (compilation-lambda exp env envf namef))
	 ('(function ,(car exp)) (compilation-appel exp env envf namef))
	)
	)
)

(defun isAPossibleCase (exp inst)
	(eql (car exp) inst)
)