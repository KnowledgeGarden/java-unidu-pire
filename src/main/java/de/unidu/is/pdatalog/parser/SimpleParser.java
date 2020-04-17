// $ANTLR : "pdatalog.g" -> "SimpleParser.java"$
 package de.unidu.is.pdatalog.parser; 
import antlr.TokenBuffer;
import antlr.TokenStreamException;
import antlr.TokenStreamIOException;
import antlr.ANTLRException;
import antlr.LLkParser;
import antlr.Token;
import antlr.TokenStream;
import antlr.RecognitionException;
import antlr.NoViableAltException;
import antlr.MismatchedTokenException;
import antlr.SemanticException;
import antlr.ParserSharedInputState;
import antlr.collections.impl.BitSet;
import de.unidu.is.pdatalog.*;
import de.unidu.is.pdatalog.ds.*;
import de.unidu.is.expressions.*;
import java.util.*;
public class SimpleParser extends antlr.LLkParser       implements SimpleParserTokenTypes
 {

protected SimpleParser(TokenBuffer tokenBuf, int k) {
  super(tokenBuf,k);
  tokenNames = _tokenNames;
}

public SimpleParser(TokenBuffer tokenBuf) {
  this(tokenBuf,2);
}

protected SimpleParser(TokenStream lexer, int k) {
  super(lexer,k);
  tokenNames = _tokenNames;
}

public SimpleParser(TokenStream lexer) {
  this(lexer,2);
}

public SimpleParser(ParserSharedInputState state) {
  super(state,2);
  tokenNames = _tokenNames;
}

	public final void imaginaryTokenDefinitions() throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			match(SIGN_MINUS);
			match(SIGN_PLUS);
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_0);
		}
	}
	
	public final Expression  arg() throws RecognitionException, TokenStreamException {
		Expression a=null;
		
		Token  id = null;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case CONSTANT:
			{
				a=constant();
				break;
			}
			case LITERAL_PROB:
			case IDENTIFIER:
			{
				a=var();
				break;
			}
			case LCURL:
			{
				a=literalarg();
				break;
			}
			case TILDE:
			{
				id = LT(1);
				match(TILDE);
				a = new VariableExpression(id.getText());
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_1);
		}
		return a;
	}
	
	public final Expression  constant() throws RecognitionException, TokenStreamException {
		Expression arg=null;
		
		Token  id = null;
		
		try {      // for error handling
			id = LT(1);
			match(CONSTANT);
			String con = id.getText(); arg=new Constant(con.substring(1,con.length()-1));
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_1);
		}
		return arg;
	}
	
	public final Expression  var() throws RecognitionException, TokenStreamException {
		Expression arg=null;
		
		Token  id = null;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case LITERAL_PROB:
			{
				match(LITERAL_PROB);
				arg =new ProbExpression();
				break;
			}
			case IDENTIFIER:
			{
				id = LT(1);
				match(IDENTIFIER);
				String t = id.getText(); arg=Character.isUpperCase(t.charAt(0)) || t.equals("_") || t.equals("#") ? (Expression) new Variable(t) : new Constant(t);
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_1);
		}
		return arg;
	}
	
	public final Expression  literalarg() throws RecognitionException, TokenStreamException {
		Expression arg=null;
		
		Literal l=null;
		
		try {      // for error handling
			match(LCURL);
			l=literal();
			match(RCURL);
			arg=new LiteralExpression(l);
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_1);
		}
		return arg;
	}
	
	public final Literal  literal() throws RecognitionException, TokenStreamException {
		Literal l=null;
		
		boolean pos = true;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case NOT:
			{
				match(NOT);
				pos=false;
				break;
			}
			case IDENTIFIER:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			l=posliteral();
			l.setPositive(pos);
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_2);
		}
		return l;
	}
	
	public final Fact  tuple() throws RecognitionException, TokenStreamException {
		Fact fact=null;
		
		Token  prob = null;
		Expression arg=null;List args = new ArrayList(); Object a;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case IDENTIFIER:
			{
				prob = LT(1);
				match(IDENTIFIER);
				arg=Rule.getMapping(prob.getText());
				break;
			}
			case LPAREN:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			match(LPAREN);
			a=arg();
			args.add(a);
			{
			_loop11:
			do {
				if ((LA(1)==COMMA)) {
					match(COMMA);
					a=arg();
					args.add(a);
				}
				else {
					break _loop11;
				}
				
			} while (true);
			}
			match(RPAREN);
			fact = new Fact(new Literal("",args,true),arg);
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_0);
		}
		return fact;
	}
	
	public final Rule  rule() throws RecognitionException, TokenStreamException {
		Rule rule=null;
		
		Token  prob = null;
		Literal head,l; List body = new ArrayList(); Expression arg=null;
		
		try {      // for error handling
			{
			if ((LA(1)==IDENTIFIER) && (LA(2)==IDENTIFIER)) {
				prob = LT(1);
				match(IDENTIFIER);
				arg=Rule.getMapping(prob.getText());
			}
			else if ((LA(1)==IDENTIFIER) && (LA(2)==LPAREN)) {
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
			}
			head=posliteral();
			{
			switch ( LA(1)) {
			case RULE:
			{
				match(RULE);
				l=literal();
				body.add(l);
				{
				_loop16:
				do {
					if ((LA(1)==AND)) {
						match(AND);
						l=literal();
						body.add(l);
					}
					else {
						break _loop16;
					}
					
				} while (true);
				}
				{
				switch ( LA(1)) {
				case COLON:
				{
					arg=probdef();
					break;
				}
				case ENDRULE:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				break;
			}
			case ENDRULE:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			match(ENDRULE);
			rule=body.isEmpty() ? new Fact(head,arg) : new Rule(head,body,arg);
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_0);
		}
		return rule;
	}
	
	public final Literal  posliteral() throws RecognitionException, TokenStreamException {
		Literal l=null;
		
		Token  id = null;
		String pred; List args = new ArrayList(); Object a;
		
		try {      // for error handling
			id = LT(1);
			match(IDENTIFIER);
			pred = id.getText();
			match(LPAREN);
			a=arg();
			args.add(a);
			{
			_loop22:
			do {
				if ((LA(1)==COMMA)) {
					match(COMMA);
					a=arg();
					args.add(a);
				}
				else {
					break _loop22;
				}
				
			} while (true);
			}
			match(RPAREN);
			l = new Literal(pred,args,true);
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_3);
		}
		return l;
	}
	
	public final Expression  probdef() throws RecognitionException, TokenStreamException {
		Expression arg=null;
		
		
		try {      // for error handling
			match(COLON);
			arg=expr();
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_4);
		}
		return arg;
	}
	
	public final Expression  expr() throws RecognitionException, TokenStreamException {
		Expression arg=null;
		
		
		try {      // for error handling
			match(LPAREN);
			arg=sumExpr();
			match(RPAREN);
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_5);
		}
		return arg;
	}
	
	public final Expression  sumExpr() throws RecognitionException, TokenStreamException {
		Expression arg=null;
		
		Expression arg1=null; Expression arg2=null; String op=null;
		
		try {      // for error handling
			arg1=prodExpr();
			{
			switch ( LA(1)) {
			case PLUS:
			case MINUS:
			{
				{
				switch ( LA(1)) {
				case PLUS:
				{
					match(PLUS);
					op="+";
					break;
				}
				case MINUS:
				{
					match(MINUS);
					op="-";
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				arg2=prodExpr();
				break;
			}
			case RPAREN:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			arg=arg2==null ? arg1 : new Arg2Expression(op,arg1,arg2);
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_6);
		}
		return arg;
	}
	
	public final Expression  prodExpr() throws RecognitionException, TokenStreamException {
		Expression arg=null;
		
		Expression arg1=null; Expression arg2=null; String op=null;
		
		try {      // for error handling
			arg1=powExpr();
			{
			switch ( LA(1)) {
			case MUL:
			case DIV:
			case MOD:
			{
				{
				switch ( LA(1)) {
				case MUL:
				{
					match(MUL);
					op="*";
					break;
				}
				case DIV:
				{
					match(DIV);
					op="/";
					break;
				}
				case MOD:
				{
					match(MOD);
					op="%";
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				arg2=powExpr();
				break;
			}
			case RPAREN:
			case PLUS:
			case MINUS:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			arg=arg=arg2==null ? arg1 : new Arg2Expression(op,arg1,arg2);
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_7);
		}
		return arg;
	}
	
	public final Expression  powExpr() throws RecognitionException, TokenStreamException {
		Expression arg=null;
		
		
		try {      // for error handling
			arg=signExpr();
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_8);
		}
		return arg;
	}
	
	public final Expression  signExpr() throws RecognitionException, TokenStreamException {
		Expression arg=null;
		
		Token  m = null;
		Token  p = null;
		Expression arg1=null; boolean minus=false;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case MINUS:
			{
				m = LT(1);
				match(MINUS);
				minus=true;
				break;
			}
			case PLUS:
			{
				p = LT(1);
				match(PLUS);
				break;
			}
			case TILDE:
			case CONSTANT:
			case LITERAL_PROB:
			case IDENTIFIER:
			case LCURL:
			case LPAREN:
			case AND:
			case INT:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			arg=atom();
			if(minus) arg=new ProductExpression(new PlainExpression("-1"),arg);
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_8);
		}
		return arg;
	}
	
	public final Expression  atom() throws RecognitionException, TokenStreamException {
		Expression argument=null;
		
		Token  i = null;
		Expression a=null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case INT:
			{
				i = LT(1);
				match(INT);
				argument=new PlainExpression(i.getText());
				break;
			}
			case TILDE:
			case CONSTANT:
			case LITERAL_PROB:
			case IDENTIFIER:
			case LCURL:
			{
				a=arg();
				argument=new Str2NumFunctionExpression(a);
				break;
			}
			case LPAREN:
			{
				argument=expr();
				break;
			}
			case AND:
			{
				argument=function();
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_8);
		}
		return argument;
	}
	
	public final Expression  function() throws RecognitionException, TokenStreamException {
		Expression arg=null;
		
		Token  id = null;
		Expression a=null;
		
		try {      // for error handling
			match(AND);
			id = LT(1);
			match(IDENTIFIER);
			match(LPAREN);
			a=sumExpr();
			match(RPAREN);
			arg = new FunctionExpression(id.getText(),a);
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_8);
		}
		return arg;
	}
	
	
	public static final String[] _tokenNames = {
		"<0>",
		"EOF",
		"<2>",
		"NULL_TREE_LOOKAHEAD",
		"SIGN_MINUS",
		"SIGN_PLUS",
		"TILDE",
		"CONSTANT",
		"\"PROB\"",
		"IDENTIFIER",
		"LCURL",
		"RCURL",
		"LPAREN",
		"COMMA",
		"RPAREN",
		"RULE",
		"AND",
		"ENDRULE",
		"NOT",
		"COLON",
		"PLUS",
		"MINUS",
		"MUL",
		"DIV",
		"MOD",
		"INT",
		"QUOTE",
		"FUNCTION",
		"HASH",
		"POW",
		"WS"
	};
	
	private static final long[] mk_tokenSet_0() {
		long[] data = { 2L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
	private static final long[] mk_tokenSet_1() {
		long[] data = { 32530432L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());
	private static final long[] mk_tokenSet_2() {
		long[] data = { 722944L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());
	private static final long[] mk_tokenSet_3() {
		long[] data = { 755712L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_3 = new BitSet(mk_tokenSet_3());
	private static final long[] mk_tokenSet_4() {
		long[] data = { 131072L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_4 = new BitSet(mk_tokenSet_4());
	private static final long[] mk_tokenSet_5() {
		long[] data = { 32653312L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_5 = new BitSet(mk_tokenSet_5());
	private static final long[] mk_tokenSet_6() {
		long[] data = { 16384L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_6 = new BitSet(mk_tokenSet_6());
	private static final long[] mk_tokenSet_7() {
		long[] data = { 3162112L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_7 = new BitSet(mk_tokenSet_7());
	private static final long[] mk_tokenSet_8() {
		long[] data = { 32522240L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_8 = new BitSet(mk_tokenSet_8());
	
	}
