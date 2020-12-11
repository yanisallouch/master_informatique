grammar CibleAppel;
import Type;
import Expressions;

cibleAppel : 'read' | 'write' | fonction ;

fonction : String '(' (argFonction (',' argFonction)*)? ')' (':' type)?;

argFonction : String ':' type  ;
String: (('a'..'z')|('A'..'Z'))(('a'..'z')|('A'..'Z')|('0'..'9'))*;

WS : [ \t\r\n]+ -> skip ;