grammar Constante;

constante : Const | 'true' | 'false';
Const : ('0'..'9')+ ;

WS : [ \t\r\n]+ -> skip ;