# $Id$

.text
main:	li $v0, 5
	syscall
	move $a0, $v0
	jal fib
	move $a0, $v0
	li $v0, 1
	syscall
	li $v0, 10
	syscall

fib:	addiu $sp, $sp, -12
	sw $ra, 8($sp)
	sw $s0, 4($sp)
	sw $s1, ($sp)
	move $s0, $a0
	li $t0, 1
	ble $s0, $t0, basis
	addiu $a0, $s0, -1
	jal fib
	move $s1, $v0
	addiu $a0, $s0, -2
	jal fib
	add $v0, $s1, $v0
	j exit
basis:	move $v0, $s0
exit:	lw $ra, 8($sp)
	lw $s0, 4($sp)
	lw $s1, ($sp)
	addiu $sp, $sp, 12
	jr $ra
