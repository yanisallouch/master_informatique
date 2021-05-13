(defun LISP2LI (expr env)
	 (if (atom expr)
		(if	(constantp expr)
			(cons :lit expr)
			(let ((pos (position expr env)))
				(if pos
					(cons :var (+ 1 pos))
					(warn "~s est non existante dans l'environnement" expr)
				)
			)
		)
		(let
			(
				(fun (car expr))
				(args (cdr expr))
			)
			(cond
				((get-defun fun)
					(list* :mcall fun (MAPLISP2LI args env))
				)

				((not (fboundp fun))
					(list :unknown
						(cons fun args) env)
				)

				((eq 'defun fun)
					(set-defun (first args)
						(list :call 'set-defun (cons :lit (first args))
							(list* :lit :lambda (length (second args))
								(list (LISP2LI (third args) (second args)))
							)
						)
					)
				)

				((eq 'let fun)
					(list* :let (length (first args))
      			(l2liLet (car args) (newenv (car args) env))
      			(MAPLISP2LI (rest args) (newenv (car args) env))
					)
				)

				((eq 'setf fun)
					(let ((pos (position (first args) env)))
						(if pos
							(if (symbolp (second expr))
								(list
									:set-var (cdr (LISP2LI (second expr) env))
									(LISP2LI (third expr) env)
								)
								(cons :setf (LISP2LI (second expr) env))
							)
							(warn "~s est non existante dans l'environnement" (first args))
						)
					)
				)

				((eq 'quote fun)
					(cons :lit (first args))
				)

				((eq 'if fun)
					(list
						:if (LISP2LI (first args) env)
						(LISP2LI (second args) env)
						(LISP2LI (third args) env)
					)
				)

				((eq 'progn fun)
					(cons :progn (MAPLISP2LI args env))
				)

				((eq 'cond fun)
						;On concatène :if et on fait un MAPLISP2LI des arguments avec
						;l'environnement pour faire le LI de l'expression
					(LISP2LI (macroexpand-1 expr) env)
				)

				((eq 'case fun)
					(LISP2LI (macroexpand-1 expr) env)
				)

				((eq 'loop fun)
					(if (eq (first args) 'while)
						(list* :While
							(if (atom (second args)) (second args) (LISP2LI  (second args) env)
							)
							(MAPLISP2LI (list (fourth args)) env)
						)
						(warn "~s Loop non traite " (first args))
					)
				)

				((eq 'macro-function fun)
					(cons :macro-function (LISP2LI (macroexpand-1 expr) env))
				)

				((fboundp fun)
					(list* :call fun (MAPLISP2LI args env))
				)
			)
		)
	)
)

(defun set-defun (symb lambda)
  (setf (get symb :defun) lambda))

(defun get-defun (symb)
  (get symb :defun))

(defun MAPLISP2LI (lexpr env)
	(if (atom lexpr)
		NIL
		(cons
			(LISP2LI (first lexpr) env)
			(MAPLISP2LI (rest lexpr) env)
		)
	)
)

(lisp2li '(defun mfi (l) (if (atom l) 0 (+ 1 (mf (rest l))))) nil)

(defun l2liLet(expr env)
	(let* ((mam (car expr)))
		(if (atom expr)
			()
			(cons
				(list :set-var (position (car mam) env)
				(lisp2li (cadr mam) env)) (l2liLet (cdr expr) env)
			)
		)
	)
)

(defun newenv (expre env)
	(if (atom expre)
		env
		(newenv (cdr expre) (append env (list (caar expre))))
	)
)

(defun fibo(n) (if (<= n 2) 1 (+ (fibo (- n 1)) (fibo (- n 2)))))
(defun factorielle(n) (if (<= n 0) 1 (* n (factorielle (- 1 n)))))
(defun facto(n) (if (= n 0) 1 2))

(trace newenv l2lilet MAPLISP2LI get-defun set-defun LISP2LI)
