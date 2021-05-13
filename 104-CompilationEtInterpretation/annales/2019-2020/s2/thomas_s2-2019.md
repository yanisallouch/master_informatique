# Exercice 1
1. Que fait la fonction *f* ?
f(0) => 0
f(1) => 2 - 0 - 1 = 1
f(2) => 2*2 - 1 - 1 = 2
f(3) => 6 - 2 - 1 = 3
f(4) => 8 - 3 - 1 = 4

La fonction identité

1. fonction f en PP

```ocaml
function f(n : integer) : integer
	if n = 0 then
		f := 0
	else
		f := (2*n) - f(n-1) - 1
```

3. fonction f en UPP
```ocaml
function f(n);
begin
	f := 0;
	if n <= 0 then
		f := 0
	else
		f := (2*n) - f(n-1) - 1
end;
```

4. fonction f en RTL
```mips
function f(%0) : %1
var %0, %1, %2, %3, %4, %5
entry f1
exit f0
f1: li %1, 0 -> f2			;; %1 := 0
f2: blez %0 -> f3, f4		;; if (n <= 0)
;; then
f4: mul %2, %0, 2 -> f5		;; %2 := 2*n
f5: addiu %3, %2, -1 -> f6	;; %3 := (2*n)-1
f6: addiu %4, %0, -1 -> f7 	;; %4 := n-1
f7: call %5, f(%4) -> f6	;; %5 := f(n-1)
f6: sub %1, %3, %5 -> f0 	;; %1 := (2*n)-1-f(n-1)
;; else
f3: li %1, 0 -> f0			;; %1 := 0
```

5. fonction f en ERTL
```mips
procedure f(1)
var %0, %1, %2, %3, %4, %5, %6, %7, %8
entry f1
f1: newframe -> f2
f2: move %6, $ra -> f3
f3: move %8, $s0 -> f4
f4: move %0, $a0 -> f5
f5: li %1, 0 -> f6			;; %1 := 0
f6: blez %0 -> f7, f8		;; if (n <= 0)
;; then
f8: mul %2, %0, 2 -> f9		;; %2 := 2*n
f9: addiu %3, %2, -1 -> f10	;; %3 := (2*n)-1
f10: addiu %4, %0, -1 -> f11 	;; %4 := n-1
f11: move $a0, %4 -> f12
f12: call f(1) -> f13		;; appel f(n-1)
f13: move $s0, $v0 -> f14			;; $s0 := f(n-1)
f14: sub %1, %3, $s0 -> f0 	;; %1 := (2*n)-1-f(n-1)
f0: j -> f15
f15: move $v0, %1 -> f16
f15: move $ra, %6 -> f16
f16: move $s0, %8 -> f17
f4: move %8, $s0 -> f18
f18: delframe -> f19
f19: jr $ra
;; else
f7: li %1, 0 -> f0			;; %1 := 0
```