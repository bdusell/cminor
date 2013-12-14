package cminor.lexer;

import java.util.HashMap;
import java_cup.runtime.Symbol;
import cminor.parser.LocationInfo;
import cminor.semantic.ErrorLogger;

%%
%class Lexer
%cup
%public
%apiprivate
%line
%column

%{

	private static final int MAX_STRING_LITERAL_LENGTH = 64;

	private StringBuffer stringBuffer;
	private HashMap<String, String> stringTable;
	private ErrorLogger logger;
	private String filename;

	public int currentLine() { return yyline; }
	public int currentColumn() { return yycolumn; }

	public LocationInfo info() {
		return new LocationInfo(this.filename, yyline, yycolumn);
	}

	public void setErrorLogger(ErrorLogger logger) {
		this.logger = logger;
	}

	public ErrorLogger getErrorLogger() {
		return this.logger;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getFilename() {
		return this.filename;
	}

	private Symbol makeSymbol(int symbol) {
		return new Symbol(symbol);
	}

	private Symbol makeSymbol(int symbol, Object value) {
		return new Symbol(symbol, value);
	}

	/* Check for string literal overflow. If there is a violation, return
	an error token. Otherwise return null. */
	private Symbol checkStringLiteralLength() {
		if(stringBuffer.length() > MAX_STRING_LITERAL_LENGTH) {
			yybegin(STRING_RECOVER);
			return makeErrorToken(
				"maximum string literal length exceeded " +
				"(maximum number of characters allowed is " +
				MAX_STRING_LITERAL_LENGTH + ")");
		}
		else return null;
	}

	private Symbol makeErrorToken(String message) {
		this.logger.log(info(), message);
		return makeSymbol(cminor.parser.Symbol.ERROR, message);
	}

	/* Returns the copy of the current identifier string which exists in
	the string table or creates a new entry in the table. */
	private String getIdentifierString() {
		String identifier = yytext();
		String result = stringTable.get(identifier);
		if(result == null) {
			stringTable.put(identifier, identifier);
			return identifier;
		}
		else {
			return result;
		}
	}

	/* Get the last character of the current yytext string. */
	private char getLastChar() {
		String text = yytext();
		return text.charAt(text.length() - 1);
	}

	/* Get a printable description of a particular character. */
	private String charName() {
		char c = getLastChar();
		if(' ' <= c && c <= '~') {
			return "\'" + c + "\'";
		}
		else {
			switch(c) {
			case '\n': return "newline";
			case '\t': return "tab";
			case '\r': return "carriage return";
			}
			return "byte 0x" + Integer.toHexString(c);
		}
	}

	/* Check if the buffer has more than one character. */
	private Symbol checkCharLiteralLength() {
		if(stringBuffer.length() > 1) {
			yybegin(CHAR_RECOVER);
			return makeErrorToken("more than one character inside character literal");
		}
		else return null;
	}

	/* Get the last character in the string buffer. */
	private char getLastBufferChar() {
		return stringBuffer.charAt(stringBuffer.length() - 1);
	}

%}

%init{
	this.stringBuffer = new StringBuffer(MAX_STRING_LITERAL_LENGTH);
	this.stringTable = new HashMap<String, String>();
	this.yyline = 1;
%init}

%eofval{
	switch(yystate()) {
	case YYINITIAL:
		/* If EOF is read during normal scanning mode, this is not an
		error. Return the normal EOF token. */
		break;
	case STRINGLITERAL:
	case STRING_RECOVER:
		/* Make sure to reset the state so that the EOF token is
		returned. */
		yybegin(YYINITIAL);
		return makeErrorToken("unexpected EOF in string literal");
	case CHARLITERAL:
	case CHAR_RECOVER:
		yybegin(YYINITIAL);
		return makeErrorToken("unexpected EOF in character literal");
	case LONG_COMMENT:
		yybegin(YYINITIAL);
		return makeErrorToken("unmatched \'/*\' (reached EOF)");
	case SHORT_COMMENT:
		/* Not an error. */
		break;
	}
	return new Symbol(cminor.parser.Symbol.EOF);
%eofval}

LineTerminator = \r|\n|\r\n
Identifier     = [A-Za-z_][A-Za-z_0-9]*
Integer        = [0-9]+

WhiteSpace     = {LineTerminator} | [ \t\f]

Printable      = [ -~]

Any            = . | \n

%state STRINGLITERAL, CHARLITERAL, STRING_RECOVER, CHAR_RECOVER, LONG_COMMENT, SHORT_COMMENT

%%

<YYINITIAL> {

	/* Control flow keywords */
	"if"    { return makeSymbol(cminor.parser.Symbol.IF); }
	"else"  { return makeSymbol(cminor.parser.Symbol.ELSE); }
	"while" { return makeSymbol(cminor.parser.Symbol.WHILE); }

	/* Primitive type keywords */
	"void"    { return makeSymbol(cminor.parser.Symbol.VOID); }
	"boolean" { return makeSymbol(cminor.parser.Symbol.BOOLEAN); }
	"int"     { return makeSymbol(cminor.parser.Symbol.INT); }
	"char"    { return makeSymbol(cminor.parser.Symbol.CHAR); }
	"string"  { return makeSymbol(cminor.parser.Symbol.STRING); }

	/* Command keywords */
	"return" { return makeSymbol(cminor.parser.Symbol.RETURN); }
	"print"  { return makeSymbol(cminor.parser.Symbol.PRINT); }

	/* Boolean literals */
	"false" { return makeSymbol(cminor.parser.Symbol.FALSE); }
	"true"  { return makeSymbol(cminor.parser.Symbol.TRUE); }

	/* Identifiers */
	{Identifier} { return makeSymbol(cminor.parser.Symbol.IDENTIFIER, getIdentifierString()); }

	/* Integer literals */
	{Integer} { return makeSymbol(cminor.parser.Symbol.INT_LITERAL, Integer.parseInt(yytext())); }

	/* Separators */
	"(" { return makeSymbol(cminor.parser.Symbol.LPAREN); }
	")" { return makeSymbol(cminor.parser.Symbol.RPAREN); }
	"{" { return makeSymbol(cminor.parser.Symbol.LBRACE); }
	"}" { return makeSymbol(cminor.parser.Symbol.RBRACE); }
	"," { return makeSymbol(cminor.parser.Symbol.COMMA); }
	";" { return makeSymbol(cminor.parser.Symbol.SEMI); }

	/* Operators */
	"+"  { return makeSymbol(cminor.parser.Symbol.ADD); }
	"-"  { return makeSymbol(cminor.parser.Symbol.SUB); }
	"*"  { return makeSymbol(cminor.parser.Symbol.STAR); }
	"/"  { return makeSymbol(cminor.parser.Symbol.DIV); }
	"!"  { return makeSymbol(cminor.parser.Symbol.NOT); }
	"&&" { return makeSymbol(cminor.parser.Symbol.AND); }
	"||" { return makeSymbol(cminor.parser.Symbol.OR); }
	"==" { return makeSymbol(cminor.parser.Symbol.EQ); }
	"!=" { return makeSymbol(cminor.parser.Symbol.NE); }
	"<=" { return makeSymbol(cminor.parser.Symbol.LE); }
	"<"  { return makeSymbol(cminor.parser.Symbol.LT); }
	">=" { return makeSymbol(cminor.parser.Symbol.GE); }
	">"  { return makeSymbol(cminor.parser.Symbol.GT); }
	"="  { return makeSymbol(cminor.parser.Symbol.ASSIGN); }

	/* String literal */
	\" { yybegin(STRINGLITERAL); stringBuffer.setLength(0); }

	/* Character literal */
	\' { yybegin(CHARLITERAL); stringBuffer.setLength(0); }

	/* Whitespace */
	{WhiteSpace} { /* ignore */ }

	/* Comments */
	"/*" { yybegin(LONG_COMMENT); }
	"//" { yybegin(SHORT_COMMENT); }
	"*/" { return makeErrorToken("stray comment terminator \"*/\""); }

	/* Any other illegal character. */
	{Any} { return makeErrorToken("stray " + charName()); }

}

<STRINGLITERAL> {

	/* End of string literal */
	\"            { yybegin(YYINITIAL); return new Symbol(cminor.parser.Symbol.STRING_LITERAL, stringBuffer.toString()); }

	/* Escape sequences */
	\\n           { stringBuffer.append('\n'); Symbol err = checkStringLiteralLength(); if(err != null) return err; }
	\\0           { stringBuffer.append('\0'); Symbol err = checkStringLiteralLength(); if(err != null) return err; }
	\\{Printable} { stringBuffer.append(getLastChar()); Symbol err = checkStringLiteralLength(); if(err != null) return err; }

	/* Legal unescaped characters */
	{Printable}   { stringBuffer.append(getLastChar()); Symbol err = checkStringLiteralLength(); if(err != null) return err; }

	/* Stray newline */
	{LineTerminator} { yybegin(YYINITIAL); return makeErrorToken("missing closing quote for string literal"); }

	/* Other, illegal characters */
	{Any}         { yybegin(STRING_RECOVER); return makeErrorToken("illegal " + charName() + " in string literal"); }

}

<CHARLITERAL> {

	/* End of char literal. */
	\'            { yybegin(YYINITIAL); return new Symbol(cminor.parser.Symbol.CHAR_LITERAL, getLastBufferChar()); }

	/* Escape sequences. */
	\\n           { stringBuffer.append('\n'); Symbol err = checkCharLiteralLength(); if(err != null) return err; }
	\\0           { stringBuffer.append('\0'); Symbol err = checkCharLiteralLength(); if(err != null) return err; }
	\\{Printable} { stringBuffer.append(getLastChar()); Symbol err = checkCharLiteralLength(); if(err != null) return err; }

	/* Regular unescaped characters. */
	{Printable}   { stringBuffer.append(getLastChar()); Symbol err = checkCharLiteralLength(); if(err != null) return err; }

	/* Illegal characters. */
	{Any}         { yybegin(CHAR_RECOVER); return makeErrorToken("illegal " + charName() + " in character literal"); }

}

<STRING_RECOVER> {
	\"      { yybegin(YYINITIAL); }
	\\{Any} { /* Continue to respect escaped double-quotes, but do nothing. */ }
	{Any}   { /* Do nothing. */ }
}

<CHAR_RECOVER> {
	\'      { yybegin(YYINITIAL); }
	\\{Any} { }
	{Any}   { }
}

<LONG_COMMENT> {
	"*/"    { yybegin(YYINITIAL); }
	{Any}   { }
}

<SHORT_COMMENT> {
	{LineTerminator} { yybegin(YYINITIAL); }
	{Any}            { }
}

