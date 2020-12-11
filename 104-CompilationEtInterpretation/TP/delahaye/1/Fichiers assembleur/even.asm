# $Id$

.data
msg:	.asciiz "Enter an integer: "
meven:	.asciiz "Even"
modd:	.asciiz "Odd"

.text
main:	li $v0, 4
	la $a0, msg
	syscall
	li $v0, 5
	syscall
	li $t0, 2
	div $v0, $t0
	mfhi $t0
	bnez $t0, odd
	la $a0, meven
	j disp	
odd:	la $a0, modd
disp:	li $v0, 4
	syscall
