(defun LI_TO_ASM (expr nbArgs)
 (ecase (car expr)

  (:LIT
    (LI_TO_ASM_const expr))

  (:VAR
    (LI_TO_ASM_var expr nbArgs))

  (:SET-VAR
    (LI_TO_ASM_SET-VAR (cdr expr) nbArgs))

  (:IF
    (LI_TO_ASM_if (cdr expr) nbArgs))

  (:CALL
    (LI_TO_ASM_call (cdr expr) nbArgs))

  (:MCALL
    (LI_TO_ASM_mcall (cdr expr) nbArgs))

  (:PROGN
    (LI_TO_ASM_progn (cdr expr) nbArgs))

  (:LET
    (LI_TO_ASM_let (cdr expr) nbArgs))

  (:WHILE
    (LI_TO_ASM_while (cdr expr) nbArgs))

  (:LCLOSURE
    (LI_TO_ASM_lclosure expr))

  (:SET_FUN
    (LI_TO_ASM_set_fun expr))

  (:APPLY
    (LI_TO_ASM_apply expr))

  (:CVAR
    (LI_TO_ASM_cvar expr))

  (:SET_CVAR
    (LI_TO_ASM_set_cvar expr))

  (:LCALL
    (LI_TO_ASM_lcall expr))

  (:UNKNOWN
    (LI_TO_ASM_unknown expr))))
(trace LI_TO_ASM)

(defun MAP_LI_TO_ASM (lexpr)
  (if (atom lexpr)
    NIL
    (list* (LI_TO_ASM (first lexpr)) (MAP_LI_TO_ASM (rest lexpr)))))

(defun LI_TO_ASM_const  (expr)
  (list
    (list 'MOVE expr 'R0)
    (list 'PUSH 'R0)))

(defun LI_TO_ASM_var  (expr nbArgs)
  (if (> (cdr expr) nbArgs)
    (warn "")
    (let ((decalage (- (+ nbArgs 1) (cdr expr))))
      (list
        (list 'LOAD (list '- 'FP decalage) 'R0)
        (list 'PUSH 'R0)))))

(defun LI_TO_ASM_var_old  (expr nbArgs)
  (if (> (cdr expr) nbArgs)
    (warn "")
    (let ((decalage (- (+ nbArgs 1) (cdr expr))))
      (list
        (list 'MOVE decalage 'R2)
        (list 'MOVE 'FP 'R1)
        (list 'SUB 'R2 'R1)
        (list 'LOAD  'R1 'R0)))))

(defun LI_TO_ASM_cvar  (expr))

(setf id_label 0)

(defun get_id_label ()
  (setf id_label (+ 1 id_label))
  id_label)

(defun LI_TO_ASM_if  (expr nbArgs)
  (setf id_label_if (get_id_label))
  (append
    (list
      (list 'LABEL (concatenate 'string "IF" (write-to-string id_label_if))))
    (if (eq (first (first expr)) ':call)
      (if (or (eq (second (first expr)) '<)
        (eq (second (first expr)) '<=)
        (eq (second (first expr)) '=)
        (eq (second (first expr)) 'eq)
        (eq (second (first expr)) 'eql)
        (eq (second (first expr)) '>=)
        (eq (second (first expr)) '>)
        (eq (second (first expr)) '/=))
      (append
          (LI_TO_ASM (third (first expr)) nbArgs)
          (LI_TO_ASM (fourth (first expr)) nbArgs)
          (list
          (list 'MOVE 'SP 'R1)
          (list 'LOAD 'R1 'R0)
          (list 'SUB 1 'R1)
          (list 'LOAD 'R1 'R1)
          (list 'CMP 'R0 'R1 )
          (cond
            ((eq (second (first expr)) '<)
              (list 'JLT (concatenate 'string "ELSE" (write-to-string id_label_if))))
              ((eq (second (first expr)) '<=)
              (list 'JLE (concatenate 'string "ELSE" (write-to-string id_label_if))))
              ((or (eq (second (first expr)) '=) (eq (second (first expr)) 'eql) (eq (second (first expr)) 'eq))
              (list 'JEQ (concatenate 'string "ELSE" (write-to-string id_label_if))))
              ((eq (second (first expr)) '>=)
              (list 'JGE (concatenate 'string "ELSE" (write-to-string id_label_if))))
              ((eq (second (first expr)) '>)
              (list 'JGT (concatenate 'string "ELSE" (write-to-string id_label_if))))
              ((eq (second (first expr)) '/=)
              (list 'JNE (concatenate 'string "ELSE" (write-to-string id_label_if))))))
          (append (LI_TO_ASM (second expr) nbArgs)
            (list (list 'JMP (concatenate 'string "FI" (write-to-string id_label_if)))
              (list 'LABEL (concatenate 'string "ELSE" (write-to-string id_label_if))))
              (append (LI_TO_ASM (third expr) nbArgs)
              (list
              (list 'LABEL (concatenate 'string "FI" (write-to-string id_label_if)))))))))))

(defun LI_TO_ASM_if_stable2  (expr nbArgs)
  (setf id_label_if (get_id_label))
  (append
    (list
      (list 'LABEL (concatenate 'string "FI" (write-to-string id_label_if))))
      (LI_TO_ASM (third expr) nbArgs)
      (list (list 'PUSH 'R0))
      (list (list 'LABEL (concatenate 'string "ELSE" (write-to-string id_label_if))))
      (LI_TO_ASM (second expr) nbArgs)
      (list (list 'PUSH 'R0))
      (list (list 'LABEL (concatenate 'string "IF" (write-to-string id_label_if))))
      (if (eq (first (first expr)) ':call)
        (if (or (eq (second (first expr)) '<)
          (eq (second (first expr)) '<=)
          (eq (second (first expr)) '=)
          (eq (second (first expr)) 'eq)
          (eq (second (first expr)) 'eql)
          (eq (second (first expr)) '>=)
          (eq (second (first expr)) '>)
          (eq (second (first expr)) '/=))
        (append
          (LI_TO_ASM (third (first expr)) nbArgs)
          (list
            (list 'PUSH 'R0))
          (append  (LI_TO_ASM (fourth (first expr)) nbArgs)
            (list (list 'PUSH 'R0)
          (list 'MOVE 'SP 'R0)
          (list 'LOAD 'R0 'R0)
          (list 'MOVE 'SP 'R1)
          (list 'SUB 1 'R1)
          (list 'LOAD 'R1 'R1)
          (list 'CMP 'R0 'R1 )
          (cond
            ((eq (second (first expr)) '<)
              (list 'JGE (concatenate 'string "ELSE" (write-to-string id_label_if))))
              ((eq (second (first expr)) '<=)
              (list 'JGT (concatenate 'string "ELSE" (write-to-string id_label_if))))
              ((or (eq (second (first expr)) '=) (eq (second (first expr)) 'eql) (eq (second (first expr)) 'eq))
              (list 'JNE (concatenate 'string "ELSE" (write-to-string id_label_if))))
              ((eq (second (first expr)) '>=)
              (list 'JLT (concatenate 'string "ELSE" (write-to-string id_label_if))))
              ((eq (second (first expr)) '>)
              (list 'JLE (concatenate 'string "ELSE" (write-to-string id_label_if))))
              ((eq (second (first expr)) '/=)
              (list 'JEQ (concatenate 'string "ELSE" (write-to-string id_label_if)))))
          (list 'LOAD 'PC 'R0))))))))

 (defun LI_TO_ASM_if_stable  (expr nbArgs)
  (setf id_label_if (get_id_label))
  (append
    (list
      (list 'LABEL (concatenate 'string "IF" (write-to-string id_label_if))))
    (if (eq (first (first expr)) ':call)
      (if (or (eq (second (first expr)) '<)
        (eq (second (first expr)) '<=)
        (eq (second (first expr)) '=)
        (eq (second (first expr)) 'eq)
        (eq (second (first expr)) 'eql)
        (eq (second (first expr)) '>=)
        (eq (second (first expr)) '>)
        (eq (second (first expr)) '/=))
      (append
          (LI_TO_ASM (third (first expr)) nbArgs)
          (list
            (list 'PUSH 'R0))
          (append  (LI_TO_ASM (fourth (first expr)) nbArgs)
            (list (list 'PUSH 'R0)
          (list 'MOVE 'SP 'R0)
          (list 'SUB 1 'R0)
          (list 'MOVE 'SP 'R1)
          (list 'SUB 2 'R1)
          (list 'CMP 'R0 'R1 )
          (cond
            ((eq (second (first expr)) '<)
              (list 'JGE (concatenate 'string "ELSE" (write-to-string id_label_if))))
              ((eq (second (first expr)) '<=)
              (list 'JGT (concatenate 'string "ELSE" (write-to-string id_label_if))))
              ((or (eq (second (first expr)) '=) (eq (second (first expr)) 'eql) (eq (second (first expr)) 'eq))
              (list 'JNE (concatenate 'string "ELSE" (write-to-string id_label_if))))
              ((eq (second (first expr)) '>=)
              (list 'JLT (concatenate 'string "ELSE" (write-to-string id_label_if))))
              ((eq (second (first expr)) '>)
              (list 'JLE (concatenate 'string "ELSE" (write-to-string id_label_if))))
              ((eq (second (first expr)) '/=)
              (list 'JEQ (concatenate 'string "ELSE" (write-to-string id_label_if))))))
(append (LI_TO_ASM (second expr) nbArgs)
  (list (list 'JMP (concatenate 'string "FI" (write-to-string id_label_if)))
    (list 'LABEL (concatenate 'string "ELSE" (write-to-string id_label_if))))
  (append (LI_TO_ASM (third expr) nbArgs)
    (list
      (list 'LABEL (concatenate 'string "FI" (write-to-string id_label_if))))))))))))

(defun displayAsm (asm)
  (if (atom asm)
    (print "end")
    (progn
      (print (car asm))
      (displayAsm (cdr asm)))))

(defun LI_TO_ASM_if_old_old  (expr nbArgs)
  (list
    (list 'LABEL 'IF)
    (list 'TEST (LI_TO_ASM (second expr) nbArgs))
    (list 'JNIL 'ELSE)
    (list (LI_TO_ASM (third expr) nbArgs))
    (list 'JMP 'FI)
    (list 'LABEL 'ELSE)
    (list (LI_TO_ASM (third expr) nbArgs))
    (list 'LABEL 'FI)))
(trace LI_TO_ASM_if)

(defun LI_TO_ASM_progn  (expr nbArgs)
  (MAP_LI_TO_ASM_PROGN expr nbArgs))
(trace LI_TO_ASM_progn)

(defun MAP_LI_TO_ASM_PROGN (expr nbArgs)
  (if (atom  expr)
    NIL
    (append (LI_TO_ASM (first expr) nbArgs)
      (MAP_LI_TO_ASM_PROGN (rest expr) nbArgs))))
(trace MAP_LI_TO_ASM_PROGN)

(defun LI_TO_ASM_SET-VAR (expr nbArgs)
  (if (> (first expr) nbArgs)
  	(warn "")
  	(let ((decalage (- (+ nbArgs 1) (first expr))))
  		(list
        (list 'MOVE (second expr) 'R0)
        (list 'MOVE decalage 'R2)
        (list 'MOVE 'FP 'R1)
        (list 'SUB 'R2 'R1)
        (list 'STORE 'R0 'R1)))))

 (defun LI_TO_ASM_call  (expr nbArgs)
  (if (eq (first expr) 'SET-DEFUN)
    (LI_TO_ASM_defun (cdr expr) nbArgs)
    (append
      (MAP_LI_TO_ASM_MCALL (cdr expr)  nbArgs 0)
    (list
    (list 'MOVE 'SP 'FP)
    (list 'APPLY (first expr) (length (cdr expr)))
    (list 'PUSH 'R0)))))

 (defun LI_TO_ASM_call_old  (expr nbArgs)
  (if (eq (first expr) 'SET-DEFUN)
    (LI_TO_ASM_defun (cdr expr) nbArgs)
    (append (list (list 'MOVE (list* :call (first expr)) 'R0)
      (list 'PUSH 'R0))
    (MAP_LI_TO_ASM_CALL (cdr expr))
    (list (list 'MOVE (length (cdr expr)) 'R0)
      (list 'PUSH 'R0)
      (list 'MOVE 'SP 'FP)))))
(trace LI_TO_ASM_call)

 (defun MAP_LI_TO_ASM_CALL (expr nbArgs)
  (if (not (atom expr))
    (append
      (LI_TO_ASM (car expr) nbArgs)
      (MAP_LI_TO_ASM_CALL (cdr expr) nbArgs))))
(trace MAP_LI_TO_ASM_CALL)

 (defun MAP_LI_TO_ASM_CALL_old (expr)
  (if (not (atom expr))
    (list*
      (list 'MOVE (car expr) 'R0)
      (list 'PUSH 'R0)
      (MAP_LI_TO_ASM_CALL (cdr expr)))))

 (defun LI_TO_ASM_defun  (expr nbArgs)
  (let (
    (fun (cdar expr))
    (nbArgs (third (second expr)))
    (corpsF (fourth (second expr))))
  (append
    (list (list 'LABEL fun))
    (LI_TO_ASM corpsF nbArgs)
    (list (list 'RTN)))))
(trace LI_TO_ASM_defun)

 (defun LI_TO_ASM_mcall (expr nbArgs)
  (let (
    (fun (car expr))
    (args (cdr expr)))
  (append
    (MAP_LI_TO_ASM_MCALL args nbArgs 0)
    (list
      (list 'MOVE 'SP 'FP)
      (list 'PUSH 'SP)
      (list 'PUSH 'FP)
      (list 'JSR fun)
      (list 'RTN)))))

 (defun LI_TO_ASM_mcall_old (expr nbArgs)
  (let (
    (fun (car expr))
    (args (cdr expr)))
  (append
    (MAP_LI_TO_ASM_MCALL args 0)
    (list
      (list 'MOVE 'SP 'FP)
      (list 'PUSH 'SP)
      (list 'PUSH 'FP)
      (list 'JSR fun)
      (list 'RTN)))))

(defun MAP_LI_TO_ASM_MCALL (expr nbArgs nbArgsCalc)
  (if (atom expr)
    (if (< nbArgs nbArgsCalc)
      (list (list 'PUSH nbArgsCalc))
      (list (list 'PUSH nbArgs)))
    (append
      (LI_TO_ASM (car expr) nbArgs)
      (MAP_LI_TO_ASM_MCALL (cdr expr) nbArgs (+ 1 nbArgsCalc)))))

(defun MAP_LI_TO_ASM_MCALL_old (expr nbArgs)
  (if (atom expr)
    (list (list 'PUSH nbArgs))
    (append
      (list (list 'PUSH (car expr)))
      (MAP_LI_TO_ASM_MCALL (cdr expr) (+ nbArgs 1)))))

(defun LI_TO_ASM_mcall_old (expr nbArgs)
  (let (
    (fun (car expr))
    (args (cdr expr)))
  (setf l (list
    (list 'JMP fun)))
  (loop while (> nbArgs 0) do
    (setf l (append l
      (list (list 'MOVE 'FP 'R0)
        (list 'SUB nbArgs 'R0)
        (list 'MOVE (car args) 'R1)
        (list 'STORE 'R1 'R0))))
    (progn
      (setf args (cdr args))
      (setf nbArgs (- nbArgs 1))))
  l))
(trace LI_TO_ASM_mcall)
(trace MAP_LI_TO_ASM_MCALL)

(defun LI_TO_ASM_let (expr nbArgs)
  (let ((i (car expr)))
    (print expr)
    (print i)
    (print (cadr expr))
    (setf l nil)
    (loop for x in (cadr expr)
      do
      (setf l (append l (LI_TO_ASM x nbArgs))))
    l))
(trace LI_TO_ASM_let)

(defun LI_TO_ASM_while (expr nbArgs)
  (setf id_label_if (get_id_label))
  (append
    (list
      (list 'LABEL (concatenate 'string "WHILE" (write-to-string id_label_if))))
    (if (eq (first (first expr)) ':call)
      (if (or (eq (second (first expr)) '<)
        (eq (second (first expr)) '<=)
        (eq (second (first expr)) '=)
        (eq (second (first expr)) 'eq)
        (eq (second (first expr)) 'eql)
        (eq (second (first expr)) '>=)
        (eq (second (first expr)) '>)
        (eq (second (first expr)) '/=))
      (append
          (LI_TO_ASM (third (first expr)) nbArgs)
          (list
            (list 'PUSH 'R0))
          (append  (LI_TO_ASM (fourth (first expr)) nbArgs)
            (list (list 'PUSH 'R0)
          (list 'MOVE 'SP 'R0)
          (list 'SUB 1 'R0)
          (list 'MOVE 'SP 'R1)
          (list 'SUB 2 'R1)
          (list 'CMP 'R0 'R1 )
          (cond
            ((eq (second (first expr)) '<)
              (list 'JLT (concatenate 'string "FWHILE" (write-to-string id_label_if))))
              ((eq (second (first expr)) '<=)
              (list 'JLE (concatenate 'string "FWHILE" (write-to-string id_label_if))))
              ((or (eq (second (first expr)) '=) (eq (second (first expr)) 'eql) (eq (second (first expr)) 'eq))
              (list 'JEQ (concatenate 'string "FWHILE" (write-to-string id_label_if))))
              ((eq (second (first expr)) '>=)
              (list 'JGE (concatenate 'string "FWHILE" (write-to-string id_label_if))))
              ((eq (second (first expr)) '>)
              (list 'JGT (concatenate 'string "FWHILE" (write-to-string id_label_if))))
              ((eq (second (first expr)) '/=)
              (list 'JNE (concatenate 'string "FWHILE" (write-to-string id_label_if))))))
(append (LI_TO_ASM (second expr) nbArgs)
  (list
    (list 'JMP (concatenate 'string "WHILE" (write-to-string id_label_if)))
    (list 'LABEL (concatenate 'string "FWHILE" (write-to-string id_label_if)))))))))))

(defun LI_TO_ASM_while_old (expr nbArgs)
  (setf id_label_if (get_id_label))
  (append
    (list
      (list 'LABEL (concatenate 'string "WHILE" (write-to-string id_label_if))))
    (if (eq (first (first expr)) ':call)
      (if (or (eq (second (first expr)) '<)
        (eq (second (first expr)) '<=)
        (eq (second (first expr)) '=)
        (eq (second (first expr)) 'eq)
        (eq (second (first expr)) 'eql)
        (eq (second (first expr)) '>=)
        (eq (second (first expr)) '>)
        (eq (second (first expr)) '/=))
      (append
          (LI_TO_ASM (third (first expr)) nbArgs)
          (list
            (list 'PUSH 'R0))
          (append  (LI_TO_ASM (fourth (first expr)) nbArgs)
            (list (list 'PUSH 'R0)
          (list 'MOVE 'SP 'R0)
          (list 'SUB 1 'R0)
          (list 'MOVE 'SP 'R1)
          (list 'SUB 2 'R1)
          (list 'CMP 'R0 'R1 )
          (cond
            ((eq (second (first expr)) '<)
              (list 'JLT (concatenate 'string "CWHILE" (write-to-string id_label_if))))
              ((eq (second (first expr)) '<=)
              (list 'JLE (concatenate 'string "CWHILE" (write-to-string id_label_if))))
              ((or (eq (second (first expr)) '=) (eq (second (first expr)) 'eql) (eq (second (first expr)) 'eq)
              (list 'JEQ (concatenate 'string "CWHILE" (write-to-string id_label_if)))))
              ((eq (second (first expr)) '>=)
              (list 'JGE (concatenate 'string "CWHILE" (write-to-string id_label_if))))
              ((eq (second (first expr)) '>)
              (list 'JGT (concatenate 'string "CWHILE" (write-to-string id_label_if))))
              ((eq (second (first expr)) '/=)
              (list 'JNE (concatenate 'string "CWHILE" (write-to-string id_label_if)))))
          (list 'JMP (concatenate 'string "FWHILE" (write-to-string id_label_if)))
          (list 'LABEL (concatenate 'string "CWHILE" (write-to-string id_label_if))))
(append (LI_TO_ASM (second expr) nbArgs)
  (list
    (list 'JMP (concatenate 'string "CWHILE" (write-to-string id_label_if)))
    (list 'LABEL (concatenate 'string "FWHILE" (write-to-string id_label_if)))))))))))
(trace LI_TO_ASM_while)

(defun LI_TO_ASM_unknown  (expr)
  )

(defun LI_TO_ASM_lclosure  (expr)
  )

(defun LI_TO_ASM_set_fun  (expr)
  )

(defun LI_TO_ASM_apply  (expr)
  )

(defun LI_TO_ASM_set_cvar  (expr)
  )

(defun LI_TO_ASM_lcall  (expr)
  )
