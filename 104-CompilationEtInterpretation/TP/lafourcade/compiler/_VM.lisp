(defun make-vm (nom 'mv) (tmem 10000) (aff ())
  (set-prop nom :nom nom)
  (set-prop nom :R0 0)
  (set-prop nom :R1 0)
  (set-prop nom :R2 0)
  (set-prop nom :BP 100)
  (set-prop nom :SP 100)
  (set-prop nom :VP 1)
  (set-prop nom :FP 0)
  (set-prop nom :DPP 0)
  (set-prop nom :DE 0)
  (set-prop nom :DPG 0)
  (reset-memoire nom tmem)
  (if (not (null aff)) (print-machine nom))
  )
