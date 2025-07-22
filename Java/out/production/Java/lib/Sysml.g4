grammar Sysml;
@header
{
    package lib;
}
nlparch : sentences+;

sentences: sentence+;

sentence : ((structural_stmts)
           | (functional_stmts));

structural_stmts : (((structural_stmt)
                   |(connection_stmt)
                   |(instantitation_stmt)) End);

structural_stmt : Struct_noun Struct_verb
                  ((Internal|Port)Colon*)*
                  struct_multinoun;

connection_stmt : Struct_noun Connect_verb To Struct_noun
                  (Port Struct_noun And Struct_noun);

instantitation_stmt: Struct_noun Instantiates Struct_noun;

functional_stmts: ((functional_stmt) End);

functional_stmt: ((Struct_noun Func_verb multi_flow)
                    ((From Struct_noun To Struct_noun)
                 |((To|From) struct_multinoun)
                 |(To multi_flow))?)
                 | (Struct_noun Func_verb)
                   ((flow With flow To Form flow)
                 |(flow To Form multi_flow));

struct_multinoun : (Struct_noun Comma struct_multinoun)
                   | (Struct_noun And Struct_noun)
                   | (Struct_noun) ;

multi_flow: flow
            | (flow And flow )
            | (flow Comma multi_flow);

flow : (Adj_value Energy)
       | states
       | Signal;

states : Adj_value* State;

With : 'with';
Comma : ',';
And: 'and';
Form: 'form';
End: '.';
Colon: ':';
It: 'it';
To: 'to';
Them: 'them';
From: 'from';
Struct_verb: 'consists';
Connect_verb : 'connected';
Instantiates: 'instantiates';
Internal: 'internal_components';
Port: 'port_components';
Func_verb: 'imports'|'exports'|'transfers'|'guides'|'supplies'|'converts'|'mixes'|'couples'|'separates'
            | 'energizes' | 'deenergizes' | 'stores' | 'stops' | 'changes' | 'regulates' | 'distributes'|'actuates';
Energy : 'energy';

Adj_value: [A-Z][a-z]+;
Struct_noun : [A-Z]+[_A-Z0-9]*;
State : [a-z]+;
Signal : [a-zA-Z0-9_]+;
WS: [ \t\u000C\r\n]+ -> skip;
