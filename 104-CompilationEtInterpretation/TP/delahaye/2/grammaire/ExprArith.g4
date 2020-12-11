grammar ExprArith;

expr : additionExpr ;

additionExpr : multiplyExpr ( '+' multiplyExpr | '-' multiplyExpr ) * ;

multiplyExpr : atomExpr ( '*' atomExpr | '/' atomExpr ) * ;

atomExpr : Number | '(' additionExpr ')' | '-' atomExpr ;

Number : ( '0' .. '9') + ;

WS : [ \t\r\n]+ -> skip ;