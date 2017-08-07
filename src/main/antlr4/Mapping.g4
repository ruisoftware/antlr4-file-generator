grammar Mapping;

config:
    filename=Str
    '='+
    var+
    EOF;

var:
    varName=Str '=' value;

value:
    isInt=Int | isFloat=Float | isStr=Str | array | map;

array:
    '[' (value (',' value)*)? ']';

map:
    'map (' mapEntry+ ')';

mapEntry:
    key=Str '=' val=value;

Str:
    ('A'..'Z' | 'a'..'z' | '_')('A'..'Z' | 'a'..'z' | '0'..'9' | '_')*;

Int:
    ('+'|'-')?('0'..'9')+;

Float:
    Int(.('0'..'9')+)?;

WS: (' ' | '\t')+      -> channel(HIDDEN);
NL: '\r'? '\n'         -> channel(HIDDEN);
