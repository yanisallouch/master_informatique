.data
	nL: .asciiz "\n"
	cR: .asciiz "\r"
	space: .asciiz " "
.text
	main:	li $v0, 5
		syscall
		ble $v0, 0, end # get rid of input <= 0
		move $a1, $v0 # save the input in $a1
		# init $t0 with N = 1
		li $t0, 1
		
	do:	li $v0, 1
		move $a0, $t0
		syscall
		li $v0, 4
		la $a0, nL
		syscall
		addi $t0, $t0, 1
	while:	ble $t0, $a1, do
	end: