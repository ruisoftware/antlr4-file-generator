grammar Mapping;

config:
    filename=Str
    '='+
    map+
    EOF;

map:
    mapH=mapHeader
    mapE=mapEntry+;

mapHeader:
    'map' mapName=Str;

mapEntry:
    mapKey=Str '=' mapValue=value;

value:
    isNumber=Number | isStr=Str | isArray=array | isMap=map;

array:
    '[' (value (',' value)*)? ']';

Str:
    ('A'..'Z' | 'a'..'z' | '_')('A'..'Z' | 'a'..'z' | '0'..'9' | '_')*;

Number:
    ('0'..'9')+(.('0'..'9')+)?;

WS: (' ' | '\t')+      -> channel(HIDDEN);
NL: '\r'? '\n'         -> channel(HIDDEN);
