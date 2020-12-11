.data
	myTab: .space 12
.text
	main:	
		la $t0, myTab

		li $t1, 4 # value 1
			sw $t1, ($t0)
		li $t1, 9 # value 2
			sw $t1, 4($t0)
		li $t1, 3 # value 3
			sw $t1, 8($t0)

		lw $t2, 4($t0)
		sw $t2, 8($t0)
		lw $t2, ($t0)
		sw $t2, 4($t0)
		sw $t1, ($t0)