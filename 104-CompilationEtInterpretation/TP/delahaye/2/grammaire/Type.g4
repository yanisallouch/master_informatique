grammar Type;

type: 'integer' | 'boolean' | array;
array : 'array of ' type;
WS : [ \t\r\n]+ -> skip ;