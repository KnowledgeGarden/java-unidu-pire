// $ANTLR : "pdatalog.g" -> "SimpleLexer.java"$
package de.unidu.is.pdatalog.parser;

import antlr.*;
import antlr.collections.impl.BitSet;

import java.io.InputStream;
import java.io.Reader;
import java.util.Hashtable;

public class SimpleLexer extends antlr.CharScanner implements SimpleParserTokenTypes, TokenStream {
    public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
    public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());
    public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());

    public SimpleLexer(InputStream in) {
        this(new ByteBuffer(in));
    }

    public SimpleLexer(Reader in) {
        this(new CharBuffer(in));
    }

    public SimpleLexer(InputBuffer ib) {
        this(new LexerSharedInputState(ib));
    }

    public SimpleLexer(LexerSharedInputState state) {
        super(state);
        caseSensitiveLiterals = true;
        setCaseSensitive(true);
        literals = new Hashtable();
        literals.put(new ANTLRHashString("PROB", this), 8);
    }

    private static long[] mk_tokenSet_0() {
        return new long[]{576460748008465920L, 9223372031620284414L, 0L, 0L};
    }

    private static long[] mk_tokenSet_1() {
        return new long[]{287948935534739456L, 576460745995190270L, 0L, 0L};
    }

    private static long[] mk_tokenSet_2() {
        return new long[]{576460181072782848L, 9223372031620284414L, 0L, 0L};
    }

    public Token nextToken() throws TokenStreamException {
        Token theRetToken = null;
        tryAgain:
        for (; ; ) {
            Token _token = null;
            int _ttype;
            resetText();
            try {   // for char stream error handling
                try {   // for lexical error handling
                    switch (LA(1)) {
                        case ':': {
                            mRULE(true);
							break;
                        }
                        case '&': {
                            mAND(true);
							break;
                        }
                        case '|': {
                            mCOLON(true);
							break;
                        }
                        case '!': {
                            mNOT(true);
							break;
                        }
                        case '$': {
                            mFUNCTION(true);
							break;
                        }
                        case '(': {
                            mLPAREN(true);
							break;
                        }
                        case ')': {
                            mRPAREN(true);
							break;
                        }
                        case ',': {
                            mCOMMA(true);
							break;
                        }
                        case '+': {
                            mPLUS(true);
							break;
                        }
                        case '-': {
                            mMINUS(true);
							break;
                        }
                        case '*': {
                            mMUL(true);
							break;
                        }
                        case '/': {
                            mDIV(true);
							break;
                        }
                        case '%': {
                            mMOD(true);
							break;
                        }
                        case '^': {
                            mPOW(true);
							break;
                        }
                        case '.': {
                            mENDRULE(true);
							break;
                        }
                        case '~': {
                            mTILDE(true);
							break;
                        }
                        case '{': {
                            mLCURL(true);
							break;
                        }
                        case '}': {
                            mRCURL(true);
							break;
                        }
                        case '\t':
                        case '\n':
                        case '\r':
                        case ' ': {
                            mWS(true);
							break;
                        }
                        default:
                            if ((LA(1) == '"' || LA(1) == '\'') && (_tokenSet_0.member(LA(2)))) {
                                mCONSTANT(true);
							} else if ((LA(1) == '"' || LA(1) == '\'')) {
                                mQUOTE(true);
							} else if ((_tokenSet_1.member(LA(1)))) {
                                mIDENTIFIER(true);
							} else if ((LA(1) == '#')) {
                                mHASH(true);
							} else {
                                if (LA(1) == EOF_CHAR) {
                                    uponEOF();
                                    _returnToken = makeToken(Token.EOF_TYPE);
                                } else {
                                    throw new NoViableAltForCharException(LA(1), getFilename(), getLine(), getColumn());
                                }
                            }
                    }
                    if (_returnToken == null) continue tryAgain; // found SKIP token
                    _ttype = _returnToken.getType();
                    _ttype = testLiteralsTable(_ttype);
                    _returnToken.setType(_ttype);
                    return _returnToken;
                } catch (RecognitionException e) {
                    throw new TokenStreamRecognitionException(e);
                }
            } catch (CharStreamException cse) {
                if (cse instanceof CharStreamIOException) {
                    throw new TokenStreamIOException(((CharStreamIOException) cse).io);
                } else {
                    throw new TokenStreamException(cse.getMessage());
                }
            }
        }
    }

    public final void mQUOTE(boolean _createToken) throws RecognitionException, CharStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = QUOTE;
        int _saveIndex;

        switch (LA(1)) {
            case '"': {
                match('"');
                break;
            }
            case '\'': {
                match('\'');
                break;
            }
            default: {
                throw new NoViableAltForCharException(LA(1), getFilename(), getLine(), getColumn());
            }
        }
        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    public final void mIDENTIFIER(boolean _createToken) throws RecognitionException, CharStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = IDENTIFIER;
        int _saveIndex;

        {
            switch (LA(1)) {
                case '#': {
                    mHASH(false);
                    break;
                }
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                case 'A':
                case 'B':
                case 'C':
                case 'D':
                case 'E':
                case 'F':
                case 'G':
                case 'H':
                case 'I':
                case 'J':
                case 'K':
                case 'L':
                case 'M':
                case 'N':
                case 'O':
                case 'P':
                case 'Q':
                case 'R':
                case 'S':
                case 'T':
                case 'U':
                case 'V':
                case 'W':
                case 'X':
                case 'Y':
                case 'Z':
                case '_':
                case 'a':
                case 'b':
                case 'c':
                case 'd':
                case 'e':
                case 'f':
                case 'g':
                case 'h':
                case 'i':
                case 'j':
                case 'k':
                case 'l':
                case 'm':
                case 'n':
                case 'o':
                case 'p':
                case 'q':
                case 'r':
                case 's':
                case 't':
                case 'u':
                case 'v':
                case 'w':
                case 'x':
                case 'y':
                case 'z': {
                    {
                        switch (LA(1)) {
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                            case 'g':
                            case 'h':
                            case 'i':
                            case 'j':
                            case 'k':
                            case 'l':
                            case 'm':
                            case 'n':
                            case 'o':
                            case 'p':
                            case 'q':
                            case 'r':
                            case 's':
                            case 't':
                            case 'u':
                            case 'v':
                            case 'w':
                            case 'x':
                            case 'y':
                            case 'z': {
                                matchRange('a', 'z');
                                break;
                            }
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                            case 'G':
                            case 'H':
                            case 'I':
                            case 'J':
                            case 'K':
                            case 'L':
                            case 'M':
                            case 'N':
                            case 'O':
                            case 'P':
                            case 'Q':
                            case 'R':
                            case 'S':
                            case 'T':
                            case 'U':
                            case 'V':
                            case 'W':
                            case 'X':
                            case 'Y':
                            case 'Z': {
                                matchRange('A', 'Z');
                                break;
                            }
                            case '_': {
                                match('_');
                                break;
                            }
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9': {
                                matchRange('0', '9');
                                break;
                            }
                            default: {
                                throw new NoViableAltForCharException(LA(1), getFilename(), getLine(), getColumn());
                            }
                        }
                    }
                    {
                        _loop41:
                        do {
                            switch (LA(1)) {
                                case 'a':
                                case 'b':
                                case 'c':
                                case 'd':
                                case 'e':
                                case 'f':
                                case 'g':
                                case 'h':
                                case 'i':
                                case 'j':
                                case 'k':
                                case 'l':
                                case 'm':
                                case 'n':
                                case 'o':
                                case 'p':
                                case 'q':
                                case 'r':
                                case 's':
                                case 't':
                                case 'u':
                                case 'v':
                                case 'w':
                                case 'x':
                                case 'y':
                                case 'z': {
                                    matchRange('a', 'z');
                                    break;
                                }
                                case 'A':
                                case 'B':
                                case 'C':
                                case 'D':
                                case 'E':
                                case 'F':
                                case 'G':
                                case 'H':
                                case 'I':
                                case 'J':
                                case 'K':
                                case 'L':
                                case 'M':
                                case 'N':
                                case 'O':
                                case 'P':
                                case 'Q':
                                case 'R':
                                case 'S':
                                case 'T':
                                case 'U':
                                case 'V':
                                case 'W':
                                case 'X':
                                case 'Y':
                                case 'Z': {
                                    matchRange('A', 'Z');
                                    break;
                                }
                                case '_': {
                                    match('_');
                                    break;
                                }
                                case '0':
                                case '1':
                                case '2':
                                case '3':
                                case '4':
                                case '5':
                                case '6':
                                case '7':
                                case '8':
                                case '9': {
                                    matchRange('0', '9');
                                    break;
                                }
                                case '.': {
                                    match('.');
                                    break;
                                }
                                default: {
                                    break _loop41;
                                }
                            }
                        } while (true);
                    }
                    break;
                }
                default: {
                    throw new NoViableAltForCharException(LA(1), getFilename(), getLine(), getColumn());
                }
            }
        }
        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    public final void mHASH(boolean _createToken) throws RecognitionException, CharStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = HASH;
        int _saveIndex;

        match('#');
        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    public final void mCONSTANT(boolean _createToken) throws RecognitionException, CharStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = CONSTANT;
        int _saveIndex;

        mQUOTE(false);
        {
            _loop45:
            do {
                if ((_tokenSet_2.member(LA(1)))) {
                    {
                        match(_tokenSet_2);
                    }
                } else {
                    break _loop45;
                }

            } while (true);
        }
        mQUOTE(false);
        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    public final void mRULE(boolean _createToken) throws RecognitionException, CharStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = RULE;
        int _saveIndex;

        match(':');
        match('-');
        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    public final void mAND(boolean _createToken) throws RecognitionException, CharStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = AND;
        int _saveIndex;

        match('&');
        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    public final void mCOLON(boolean _createToken) throws RecognitionException, CharStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = COLON;
        int _saveIndex;

        match('|');
        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    public final void mNOT(boolean _createToken) throws RecognitionException, CharStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = NOT;
        int _saveIndex;

        match('!');
        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    public final void mFUNCTION(boolean _createToken) throws RecognitionException, CharStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = FUNCTION;
        int _saveIndex;

        match("$");
        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    public final void mLPAREN(boolean _createToken) throws RecognitionException, CharStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = LPAREN;
        int _saveIndex;

        match('(');
        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    public final void mRPAREN(boolean _createToken) throws RecognitionException, CharStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = RPAREN;
        int _saveIndex;

        match(')');
        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    public final void mCOMMA(boolean _createToken) throws RecognitionException, CharStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = COMMA;
        int _saveIndex;

        match(',');
        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    public final void mPLUS(boolean _createToken) throws RecognitionException, CharStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = PLUS;
        int _saveIndex;

        match('+');
        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    public final void mMINUS(boolean _createToken) throws RecognitionException, CharStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = MINUS;
        int _saveIndex;

        match('-');
        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    public final void mMUL(boolean _createToken) throws RecognitionException, CharStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = MUL;
        int _saveIndex;

        match('*');
        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    public final void mDIV(boolean _createToken) throws RecognitionException, CharStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = DIV;
        int _saveIndex;

        match('/');
        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    public final void mMOD(boolean _createToken) throws RecognitionException, CharStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = MOD;
        int _saveIndex;

        match('%');
        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    public final void mPOW(boolean _createToken) throws RecognitionException, CharStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = POW;
        int _saveIndex;

        match('^');
        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    public final void mENDRULE(boolean _createToken) throws RecognitionException, CharStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = ENDRULE;
        int _saveIndex;

        match('.');
        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    public final void mTILDE(boolean _createToken) throws RecognitionException, CharStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = TILDE;
        int _saveIndex;

        match('~');
        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    public final void mLCURL(boolean _createToken) throws RecognitionException, CharStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = LCURL;
        int _saveIndex;

        match('{');
        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    public final void mRCURL(boolean _createToken) throws RecognitionException, CharStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = RCURL;
        int _saveIndex;

        match('}');
        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    public final void mWS(boolean _createToken) throws RecognitionException, CharStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
		int _saveIndex;

        {
            switch (LA(1)) {
                case ' ': {
                    match(' ');
                    break;
                }
                case '\t': {
                    match('\t');
                    break;
                }
                case '\r': {
                    match('\r');
                    match('\n');
                    newline();
                    break;
                }
                case '\n': {
                    match('\n');
                    newline();
                    break;
                }
                default: {
                    throw new NoViableAltForCharException(LA(1), getFilename(), getLine(), getColumn());
                }
            }
        }
        _ttype = Token.SKIP;
        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

}
