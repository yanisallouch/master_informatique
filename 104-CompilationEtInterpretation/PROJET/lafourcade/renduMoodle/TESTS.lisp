(defun fact(n)
  (if (= n 0)
    1
    (* n (fact(- n 1)))
  )
)

(defun fibonacci (n)
  (cond( (<= n 2) 1 )
    (t(+(fibonacci (- n 1)) (fibonacci (- n 2) )  )
    )
  )
)

(equal (car (list ) ) (car (list () ) ) ) ;; true
(equal (car (list ) ) (car (list (list (list  ) ) ) ) ) ;; NIL
(equal (car (list () ) ) (car (list (list (list () ) ) ) ) ) ;;NIL

(equal (cdr (list ) ) (cdr (list () ) ) ) ;; true
(equal (cdr (list ) ) (cdr (list (list (list  ) ) ) ) ) ;; true
(equal ( cdr (list () ) ) (cdr (list (list (list  ) ) ) ) ) ;; true


(equal (list ) (list () ) ) ;; NIL
(equal (list () ) (list (list (list  ) ) ) ) ;; NIL

(eq (list ) (list () ) ) ;; NIL

;; ( = (list ) (list () ) ) ;;erreur, comparateur pour les nombres

(defun member( x l)
  ( if (= (car l ) x )
    l
    (member x (cdr l) )
  )
)

(defun length( l )
  ( if (equal NIL l )
    0
    ( + 1 (length (cdr l ) ) )
  )
)

(defun last(l )
  ( if (equal NIL (cdr l ) )
    (car l )
    ( last ( cdr l ) )
  )
)

(defun makelist( n )
  ( if ( = n 0 )
    NIL
    (append ( makelist ( - n 1 ) ) (list n) )
  )
)

(defun copylist( l )
  ( if ( equal NIL l )
    NIL
    (append (list (car l ) ) ( copylist ( cdr l ) ) )
  )
)

(defun remove( x l )
  ( if ( = (car l ) x )
    ( cdr l )
    (if  (and (equal NIL ( cdr l ) ) ( = ( car l ) x ) )
        NIL
        (if ( = ( car ( cdr l ) ) x )
          (append (list (car l ) ) (cdr ( cdr l ) ) )
          (append (remove x (cdr l ) ) (list (car l ) ) )
        )
    )
  )
)

(defun myappend ( l1 l2 )
  ( if  ( equal ( cdr l1 )  NIL )
    (append l1 l2 )
    myappend( (cdr l1) l2 )
  )
)
