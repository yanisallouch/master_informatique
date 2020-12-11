grammar ExprArithEval;

expr returns [int value] :
	e	=	additionExpr { $value = $e.value; } ;

additionExpr returns [int value] :
	e1	=	multiplyExpr {$value = $e1.value;}
	('+' e2 = multiplyExpr {$value += $e2.value;}
	| '-' e2 = multiplyExpr {$value -= $e2.value;})* ;

multiplyExpr returns [int value] :
	e1	= atomExpr {$value = $e1.value;}
	('*' e2	=	atomExpr {$value *= $e2.value;}
	| '/' e2 =	atomExpr {$value /= $e2.value;})* ;

atomExpr returns [int value] :
	c = Number { $value = Integer.parseInt ($c.text);}
	| '(' e1 = additionExpr ')' { $value = $e1.value; }
	| '-' e2 = atomExpr { $value = -$e2.value; } ;

Number : ( '0' .. '9')+ ;

WS : [ \t\r\n]+ -> skip ;