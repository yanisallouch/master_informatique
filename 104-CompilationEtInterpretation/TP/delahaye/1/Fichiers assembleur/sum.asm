# $Id$

.text
main:	li $v0, 5
	syscall
	move $a0, $v0
	jal sum
	move $a0, $v0
	li $v0, 1
	syscall
	li $v0, 10
	syscall

sum:	addiu $sp, $sp, -8
	sw $ra, 4($sp)
	sw $s0, ($sp)
	move $s0, $a0
	blez $s0, basis
	addiu $a0, $s0, -1
	jal sum
	add $v0, $s0, $v0
	j exit
basis:	li $v0, 0
exit:	lw $ra, 4($sp)
	lw $s0, ($sp)
	addiu $sp, $sp, 8
	jr $ra
