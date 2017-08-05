grammar Mapping;

config:
    filename=Str
    '='+
    map+
    EOF;

map:
    mapHeader
    '(' mapEntry+ ')';

mapHeader:
    'map' name=Str;

mapEntry:
    key=Str '=' val=value;

value:
    isNumber=Number | isStr=Str | array | mapValue;

array:
    '[' (value (',' value)*)? ']';

mapValue:
    map;

Str:
    ('A'..'Z' | 'a'..'z' | '_')('A'..'Z' | 'a'..'z' | '0'..'9' | '_')*;

Number:
    ('0'..'9')+(.('0'..'9')+)?;

WS: (' ' | '\t')+      -> channel(HIDDEN);
NL: '\r'? '\n'         -> channel(HIDDEN);
