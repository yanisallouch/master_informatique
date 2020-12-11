grammar OpUnaire;

opUnaire : '-' | 'not';

WS : [ \t\r\n]+ -> skip ;