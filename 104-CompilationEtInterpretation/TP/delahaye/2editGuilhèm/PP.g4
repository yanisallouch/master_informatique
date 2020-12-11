/**
*	@author	Guilhem	Blanchard
*	@author	Yanis	Allouch
*	@date	04 octobre 2020
*/

grammar PP;

type : 'integer'  | 'boolean'  | array   ;
array : 'array of '	type ;

constante  :
	Const | 'true' | 'false' ;
Const : ('0'..'9')+ ;

cibleAppel : 'read' | 'write' | nomFonction ;
nomFonction :	ID  ;
ID: (('a'..'z')|('A'..'Z'))(('a'..'z')|('A'..'Z')|('0'..'9'))*;



invE :
	'-'	expressions ;
notE :
	'not' expressions ;
sumE :
	expressions '+'	expressions ;
minusE :
	expressions '-'	expressions ;
prodE :
	expressions '*'	expressions ;
divE :
	expressions '/'	expressions ;
andE :
	expressions 'and' expressions ;
orE :
	expressions 'or' expressions ;
infE :
	expressions '<'	expressions ;
infEqE :
	expressions '<=' expressions ;
eqE :
	expressions '='	expressions ;
diffE :
	expressions '!=' expressions ;
suppEqE :
	expressions '>=' expressions ;
supE :
	expressions '>'	expressions ;
tabE :
	expressions '[' expressions ']'  ;
declareNewArrayE :
	'new array of '	type '['	expressions ']'	;


expressions :
	constante													|
	ID															|
	invE														|
	notE														|
	sumE														|
	minusE														|
	prodE														|
	divE														|
	andE														|
	orE															|
	infE														|
	infEqE														|
	eqE															|
	diffE														|
	suppEqE														|
	supE														|
	cibleAppel '(' (	expressions (','	expressions)*)? ')' |
	tabE														|
	declareNewArrayE
	;


variables :
	('var' (ID ':' type ( ID ':' type)*) )? ;

listeDeclarationArguments :
	(ID ':' type (',' ID ':' type)*)? ;

listeArguments :
	(expressions (',' expressions)*)? ;

instructions :
	'skip'														|
	ID ':' '=' expressions										|
	expressions '[' expressions ']' ':' '=' expressions			|
	'if' expressions 'then' instructions 'else' instructions	|
	'while' expressions 'do' instructions						|
	cibleAppel '(' listeArguments ')'		|
	instructions ';' instructions ;

definitionFonctionProcedure :
    nomFonction '(' listeDeclarationArguments ')' (':' type)?
    variables
    instructions;

programme :
	variables
	definitionFonctionProcedure*
	instructions;


WS : [ \t\r\n]+ -> skip ;
