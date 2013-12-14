package cminor.ast;

import cminor.parser.LocationInfo;
import cminor.symbol.StringSymbol;
import cminor.visit.Visitor;

public class StringLiteral extends ConstantExpression<String> {

	private StringSymbol symbol;

	public StringLiteral(LocationInfo info, String value) {
		super(info, value);
	}

	public StringSymbol getSymbol() {
		return this.symbol;
	}

	public void setSymbol(StringSymbol symbol) {
		this.symbol = symbol;
	}

	public void accept(Visitor v) {
		v.visit(this);
	}

	public static String escape(String s) {
		return s.replace("\\", "\\\\")
			.replace("\"", "\\\"")
			.replace("\'", "\\\'")
			.replace("\n", "\\n")
			.replace("\0", "\\0");
	}

}
