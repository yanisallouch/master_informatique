# Saisir un entier et afficher s'il est positive ou négatif

.data
	positive: .asciiz "positive\n"
	negative: .asciiz "negative\n"
	msg1:	.asciiz "Saisir un entier : "
.text
	li $v0, 4
	la $a0, msg1
	syscall
	
	li $v0, 5
	syscall
	
	blt $v0, $zero, neg
	
	li $v0, 4
	la $a0, positive
	syscall
	
	li $v0, 10
	syscall
	
	neg:	li $v0, 4
		la $a0, negative
		syscall
