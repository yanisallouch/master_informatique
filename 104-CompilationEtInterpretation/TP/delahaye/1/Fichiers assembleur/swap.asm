# $Id$

.data
x:	.word 1
y:	.word 1

.text
main:	li $t0, 1	# Initialization of the variables
	la $t1, x
	sw $t0, ($t1)
	li $t0, 2
	la $t1, y
	sw $t0, ($t1)

	li $v0, 1	# Display of the variables
	la $t0, x
	lw $a0, ($t0)
	syscall
	li $v0, 1
	la $t0, y
	lw $a0, ($t0)
	syscall	

	la $a0, x	# Call of the swap routine
	la $a1, y
	jal swap
	
	la $t0, x	# Display of the variables (after swapping)
	lw $a0, ($t0)
	syscall
	li $v0, 1
	la $t0, y
	lw $a0, ($t0)
	syscall	
	li $v0, 10	# Exit of the program
	syscall

swap:	sub $sp, $sp, 4
	lw $t0, ($a0)
	sw $t0, ($sp)
	lw $t0, ($a1)
	sw $t0, ($a0)
	lw $t0, ($sp)
	sw $t0, ($a1)
	add $sp, $sp, 4
	jr $ra
