grammar OpBinaire;

opBinaire :  '+' | '-' | '*' | '/' | 'and' | 'or' | '<' | '<=' | '=' | '!=' | '>=' | '>' |;

WS : [ \t\r\n]+ -> skip ;