(load "LISP_TO_LI.lisp")

(defun eval-li (expr env)
	;:LIT, :VAR, :SET-VAR, :IF, :CALL, :MCALL, :LET, :PROGN , :UNKNOWN
	(ecase (car expr)
		(:LIT (cdr expr))
		(:VAR
			(aref env
				(- (cdr expr) 0)
			)
		)
		(:SET-VAR
			(setf
				(aref env
					(- (second expr) 0)
				)
				(eval-li (third expr) env )
			)
		)
		(:IF
			(if (eval-li (second expr) env) ; -> (:CALL = (:LIT . 1) (:LIT . 2))
				(eval-li (third expr) env) ;-> (:LIT . 2)
				(eval-li (cadddr expr) env) ; -> (:LIT . 1)
			)
		)
		(:CALL
			(apply
				(second expr)
				(map-eval-li (cddr expr) env)
			)
		)
		(:MCALL
			(let*
				(
					(fun (get-defun (second expr)))
				)
				(if (eq (car (cddr expr)) :LIT)
					(let
						((args (eval-li (cddr expr) env)))
						(eval-li (third fun)
							(make-env-eval-li args env (make-array (+ 1 (cadr fun))) 1)
						)
					)
					(let
						((args (cons (eval-li (car (cddr expr)) env) (map-eval-li (cdr (cddr expr)) env) )))
						(eval-li (third fun)
							(make-env-eval-li args env (make-array (+ 1 (cadr fun))) 1)
						)
					)
				)
			)
		)
		(:LET
			(map-eval-li (caddr expr) env)
			(map-eval-li (cdddr expr) env)
		)
		(:PROGN
			(map-eval-li-progn
				(PROGN
					(cdr expr)
				)
				env
			)
		)
		(:UNKNOWN
			(let
				(
					(nexpr
						(  lisp2li (second expr)
							(caddr expr)
						)
					)
				)
			    (if (eq (car nexpr) :UNKNOWN)
					(error "Error: eval-li ~s" expr)
			      	(eval-li (displace expr nexpr) env)
			    )
			)
		)
	)
)

(defun map-eval-li (expr env)
	(if (atom expr)
	    nil
	  	(cons
				(eval-li (first expr) env)
				(map-eval-li (rest expr) env)
	    )
	)
)

(defun map-eval-li-progn (expr env)
	(car
		(last
			(map-eval-li expr env)
		)
	)
)

(defun make-env-eval-li (args env nenv index)
	(if	(null args)
		nenv
		(progn
			(setf (aref nenv index) (car args))
			(make-env-eval-li (cdr args) env nenv (+ 1 index))
		)
	)
)

(defun displace (l ln)
	(RPLACA l (CAR ln))
	(RPLACD l (CDR ln))
    l
)

(defun set-defun (symb lambda)
	(setf
		(get symb :defun)
		lambda
	)
)

(defun get-defun (symb)
	(get symb :defun)
)

(trace displace make-env-eval-li map-eval-li-progn map-eval-li eval-li)

;TEST - FACT:
; (eval-li (lisp2li '(defun fact-j (n)  (if (< n 1)  1  (* n (fact-j (- n 1))))) '()) #())
; (eval-li (lisp2li '(fact-j 3) '()) #())
; (eval-li '(:MCALL FACT-J (:LIT . 3)) #())

;TEST -FIBO:
; (eval-li (lisp2li '(defun fibonae (n) (if (< n 2) n (+ (fibonae (- n 1)) (fibonae (- n 2)))))'() ) ())
; (eval-li (lisp2li '(defun fibonae (n) (if (< n 2) n (+ (fibonae (- n 1)) (fibonae (- n 2)))))'() ) ())
; (eval-li (lisp2li '(fibonae 6) '()) ())
