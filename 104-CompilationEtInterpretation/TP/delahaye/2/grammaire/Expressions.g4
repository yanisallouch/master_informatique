grammar Expressions;
import Type;
import Constante;
import OpUnaire;
import OpBinaire;
import CibleAppel;

expressions : constante | (opUnaire expressions) | (expressions opBinaire expressions) | expressions '[' expressions ']' | 'new array of ' type '[' expressions ']';

WS : [ \t\r\n]+ -> skip ;