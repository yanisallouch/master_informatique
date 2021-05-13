# $Id$

.text
main:	li $a0, 1
	li $a1, 2
	jal sum
	move $a0, $v0
	li $v0, 1
	syscall
	li $v0, 10
	syscall

sum:	addiu $sp, $sp, -8
	sw $ra, 4($sp)
	sw $s0, ($sp)
	move $s0, $a1
	jal sqr
	move $a0, $s0
	move $s0, $v0
	jal sqr
	add $v0, $s0, $v0
	lw $ra, 4($sp)
	lw $s0, ($sp)
	addiu $sp, $sp, 8
	jr $ra

sqr:	mul $t0, $a0, $a0
	mflo $v0
	jr $ra
