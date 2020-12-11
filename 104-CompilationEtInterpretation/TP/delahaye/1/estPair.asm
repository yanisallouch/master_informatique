.data
	# Demander la saisie d’un entier 
	# et dire si cet entier est pair ou non (afficher le résultat).

	msg:	.asciiz "N = \n"
	msgEven:	.asciiz "Even\n"
	msgOdd:	.asciiz "Odd\n"

.text
	main:	li $v0, 4
		la $a0, msg
		syscall # affichage du msg

		li $v0, 5
		syscall
		move $t0, $v0 # sauvegarde de n

		li $t1 2
		div $t0, $t1
		mfhi $t2
		beqz $t2, pair
		j impair

	pair:	li $v0, 4
		la $a0, msgEven
		syscall
		j end

	impair:	li $v0, 4
		la $a0, msgOdd
		syscall
		j end
	end: