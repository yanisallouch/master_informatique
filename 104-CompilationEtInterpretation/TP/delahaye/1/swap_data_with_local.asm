.data
	var123:	.word	123
	var321:	.word	321
	cR:	.asciiz	"\n"
	.align 2
	spce:	.asciiz " "
	.align 2
.text

main:	
	jal	print
	lw	$a0, var123
	lw	$a1, var321
	jal	swap
	jal	print
	j	end

print:
	li	$v0, 1
	lw	$a0, var123
	syscall # print var123
	li	$v0, 11
	lw	$a0, spce
	syscall # print space
	li	$v0, 1
	lw	$a0, var321
	syscall #print var321
	li	$v0, 11
	lw	$a0, cR
	syscall # print carriage return
	jr	$ra
	
swap:
	subi	$sp, $sp, 4
	move	$sp, $a0
	move	$a0, $a1
	move	$a1, $sp
	addi	$sp, $sp, 4
	sw	$a0, var123
	sw	$a1, var321
	jr	$ra
	
end: