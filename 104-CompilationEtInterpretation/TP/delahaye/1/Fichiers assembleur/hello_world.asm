# $Id$

.data
hello: .asciiz "hello world\n"

.text
main:	li $v0, 4
	la $a0, hello
	syscall
