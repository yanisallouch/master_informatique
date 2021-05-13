(load "LISP_TO_LI_REFRACTOR.lisp")

(defun EVAL_LI (expr env)
  (ecase (car expr)
    (:LIT
          (EVAL_LI_const expr env))
    (:VAR
          (EVAL_LI_var expr env))
    (:SET_VAR
          (EVAL_LI_set_var expr env))
    (:IF
          (EVAL_LI_if expr env))
    (:CALL
          (EVAL_LI_call expr env))
    (:MCALL
          (EVAL_LI_mcall expr env))
    (:PROGN
          (EVAL_LI_progn expr env))
    (:LET
          (EVAL_LI_let expr env))
    (:LCLOSURE
          (EVAL_LI_lclosure expr env))
    (:SET_FUN
          (EVAL_LI_set_fun expr env))
    (:APPLY
          (EVAL_LI_apply expr env))
    (:CVAR
          (EVAL_LI_cvar expr env))
    (:SET_CVAR
          (EVAL_LI_set_cvar expr env))
    (:LCALL
          (EVAL_LI_lcall expr env))
    (:UNKNOWN
          (EVAL_LI_unknown expr env))))
(trace EVAL_LI)

(defun MAP_EVAL_LI (lexpr env)
  (if (atom lexpr)
    NIL
    (list* (EVAL_LI (first lexpr) env) (MAP_EVAL_LI (rest lexpr) env))))
(trace MAP_EVAL_LI)

(defun MAP_EVAL_LI_PROGN (expr env)
  (if (atom expr)
      NIL
      (if (atom (rest expr))
            (EVAL_LI (first expr) env)
            (MAP_EVAL_LI_PROGN (rest expr) env))))
(trace MAP_EVAL_LI_PROGN)

(defun MAKE_ENV_EVAL_LI (nbArgs listArgs)
  (make_env_rec listArgs 0 (make-array (+ nbArgs 1))))
(trace MAKE_ENV_EVAL_LI)

(defun MAKE_ENV_REC (listArgs pos envGenerated)
      (when listArgs
            (setf (aref envGenerated pos) (car listArgs))
            (make_env_rec (cdr listArgs) (+ pos 1) envGenerated))
      envGenerated)

(defun DISPLACE (cell1 cell2)
      (setf (car cell2) (car cell1)
            (cdr cell2) (car cell1))
      cell2)


;;Get-Defun
(defun get_defun (symb)
  (get symb :defun))
(trace get_defun)

;;Set-Defun
;;symb : expression evaluable mais pas evaluee
;;expr-lambda : expression evaluable evaluee
(defun set_defun (symb expr-lambda)
  (setf (get symb :defun)
    expr-lambda))
(trace set_defun)

(defun EVAL_LI_const  (expr env)
  (cdr expr))
(trace EVAL_LI_const)

(defun EVAL_LI_var  (expr env)
  (aref env (cdr expr)))
(trace EVAL_LI_var)

(defun EVAL_LI_if  (expr env)
  (if (EVAL_LI (second expr) env)
                        (EVAL_LI (third expr) env)
                        (EVAL_LI (cadddr expr) env)))
(trace EVAL_LI_if)

(defun EVAL_LI_progn  (expr env)
  (MAP_EVAL_LI_PROGN (cdr expr) env))
(trace EVAL_LI_progn)

(defun EVAL_LI_set_var  (expr env)
  (setf (aref env (cadr expr))
      (EVAL_LI (caddr expr) env)))
(trace EVAL_LI_set_var)

(defun EVAL_LI_mcall  (expr env)
  )
(trace EVAL_LI_mcall)

(defun EVAL_LI_call  (expr env)
  (apply (second expr)
      (MAP_EVAL_LI (cddr expr) env)))
(trace EVAL_LI_call)

(defun EVAL_LI_unknown  (expr env)
  )
(trace EVAL_LI_unknown)

(defun EVAL_LI_let  (expr env)
  )
(trace EVAL_LI_let)

(defun EVAL_LI_lclosure  (expr env)
  )
(trace EVAL_LI_lclosure)

(defun EVAL_LI_set_fun  (expr env)
  )
(trace EVAL_LI_set_fun)

(defun EVAL_LI_apply  (expr env)
  )
(trace EVAL_LI_apply)

(defun EVAL_LI_cvar  (expr env)
  )
(trace EVAL_LI_cvar)

(defun EVAL_LI_set_cvar  (expr env)
  )
(trace EVAL_LI_set_cvar)

(defun EVAL_LI_lcall  (expr env)
  )
(trace EVAL_LI_lcall)
