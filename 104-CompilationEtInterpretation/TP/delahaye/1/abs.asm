# Fonction absolue d'un entier
.data

.text
	main:	li $v0, 5
		syscall
		
		blt $v0, $zero, neg
		move $a0, $v0
		li $v0, 1
		syscall
		j end
	neg:	sub $a0, $zero, $v0
		li $v0, 1
		syscall

	end: