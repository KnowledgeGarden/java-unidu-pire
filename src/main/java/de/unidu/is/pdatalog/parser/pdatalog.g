/*
 * Parser
 */
 
header { package de.unidu.is.pdatalog.parser; }
{import de.unidu.is.pdatalog.*;
import de.unidu.is.pdatalog.ds.*;
import de.unidu.is.expressions.*;
import java.util.*;}
class SimpleParser extends Parser;
options {  k = 2;  }

imaginaryTokenDefinitions :
   SIGN_MINUS
   SIGN_PLUS
;

arg returns [Expression a=null] : 
	(a=constant | a=var | a=literalarg | id:TILDE { a = new VariableExpression(id.getText()); }) ;

constant returns [Expression arg=null] : 
	id:CONSTANT
		{ String con = id.getText(); arg=new Constant(con.substring(1,con.length()-1)); };

var returns [Expression arg=null] : 
	( "PROB" { arg =new ProbExpression(); }
	 | id:IDENTIFIER{ String t = id.getText(); arg=Character.isUpperCase(t.charAt(0)) || t.equals("_") || t.equals("#") ? (Expression) new Variable(t) : new Constant(t); } ); 

literalarg returns [Expression arg=null] { Literal l=null; }: 
	LCURL l=literal RCURL { arg=new LiteralExpression(l); };



tuple returns [Fact fact=null] 
	{Expression arg=null;List args = new ArrayList(); Object a;}: 
	 (prob:IDENTIFIER {arg=Rule.getMapping(prob.getText()); })? LPAREN a=arg {args.add(a);} (COMMA a=arg {args.add(a);} )* RPAREN 
		{ fact = new Fact(new Literal("",args,true),arg); };

rule returns [Rule rule=null] 
	{Literal head,l; List body = new ArrayList(); Expression arg=null;}: 
	 (prob:IDENTIFIER {arg=Rule.getMapping(prob.getText()); })? head=posliteral (RULE l=literal { body.add(l); } (AND l=literal { body.add(l); })* (arg=probdef)?)? ENDRULE 
		{ rule=body.isEmpty() ? new Fact(head,arg) : new Rule(head,body,arg); };
	
literal returns [Literal l=null] 
	{boolean pos = true;} : 
	(NOT {pos=false;})? l=posliteral 
		{l.setPositive(pos);};

posliteral returns [Literal l=null] 
	{ String pred; List args = new ArrayList(); Object a;}: 
	id:IDENTIFIER{pred = id.getText(); } LPAREN a=arg {args.add(a);} (COMMA a=arg {args.add(a);} )* RPAREN 
		{ l = new Literal(pred,args,true); };


/* Mathematical expressions */
probdef returns [Expression arg=null] : 
	COLON^  arg=expr;

expr returns [Expression arg=null] : 
	LPAREN arg=sumExpr RPAREN ;

sumExpr returns [Expression arg=null] 
	{ Expression arg1=null; Expression arg2=null; String op=null;}: 
	arg1=prodExpr ((PLUS { op="+"; } |MINUS { op="-"; }) arg2=prodExpr)? 
		{ arg=arg2==null ? arg1 : new Arg2Expression(op,arg1,arg2);} ;
	
prodExpr returns [Expression arg=null] 
	{ Expression arg1=null; Expression arg2=null; String op=null;}:  
	arg1=powExpr ((MUL { op="*"; }|DIV { op="/"; } | MOD { op="%"; }) arg2=powExpr)?  
		{ arg=arg=arg2==null ? arg1 : new Arg2Expression(op,arg1,arg2); } ;

powExpr  returns [Expression arg=null] : 
	arg=signExpr; // (POW signExpr)? ;

signExpr returns [Expression arg=null] { Expression arg1=null; boolean minus=false; }: 
		(
         m:MINUS^ {minus=true; }
         | p:PLUS^
         )? arg=atom {if(minus) arg=new ProductExpression(new PlainExpression("-1"),arg); };

atom returns [Expression argument=null] { Expression a=null; } : 
	 i:INT {argument=new PlainExpression(i.getText()); }
	 | a=arg {argument=new Str2NumFunctionExpression(a); }
	 | argument=expr 
	 | argument=function;
 
 function returns [Expression arg=null] 
 	{ Expression a=null;} :
 	AND id:IDENTIFIER LPAREN a=sumExpr RPAREN
 		{ arg = new FunctionExpression(id.getText(),a); };
 		


/*
 * Lexer
 */
 
class SimpleLexer extends Lexer;
options {  k = 2;  }
   
QUOTE : '"'|'\'';

//INT :  ('0'..'9')+('.' ('0'..'9')+)?;

IDENTIFIER : (HASH|('a'..'z'|'A'..'Z'|'_'|'0'..'9')('a'..'z'|'A'..'Z'|'_'|'0'..'9'|'.')*);
CONSTANT : QUOTE (~('"'|'\''))* QUOTE;
//CONSTANT : QUOTE ('a'..'z'|'A'..'Z'|'0'..'9'|':'|' '|'-')+ QUOTE;


RULE: ':''-';
AND: '&';
COLON : '|';
NOT : '!';
FUNCTION: "$";

HASH: '#';
LPAREN : '(';
RPAREN : ')';
COMMA : ',';
PLUS : '+';
MINUS : '-';
MUL : '*';
DIV: '/';
MOD: '%';
POW : '^';
ENDRULE : '.';
TILDE : '~';

LCURL: '{';
RCURL: '}';

WS     :
    (' ' 
    | '\t' 
    | '\r' '\n' { newline(); } 
    | '\n'      { newline(); }
    ) 
    { $setType(Token.SKIP); } 
  ;
