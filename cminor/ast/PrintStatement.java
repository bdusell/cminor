package cminor.ast;

import java.util.List;
import cminor.parser.LocationInfo;
import cminor.symbol.StringSymbol;
import cminor.visit.Visitor;

public class PrintStatement extends Statement {

	public static final String TRUE_STRING  = "true";
	public static final String FALSE_STRING = "false";
	public static final String BOOL_STRING = FALSE_STRING + "\0" + TRUE_STRING;
	public static final int OFFSET = FALSE_STRING.length() + 1;

	public static final String CHAR_FORMAT    = "%c";
	public static final String INT_FORMAT     = "%d";
	public static final String STRING_FORMAT  = "%s";

	private List<Expression> arguments;
	private StringSymbol symbol;
	private List<Expression> actualArguments;

	public PrintStatement(LocationInfo info, List<Expression> arguments) {
		super(info);
		this.arguments = arguments;
	}

	public List<Expression> getArguments() {
		return this.arguments;
	}

	public StringSymbol getSymbol() {
		return this.symbol;
	}

	public void setSymbol(StringSymbol symbol) {
		this.symbol = symbol;
	}

	public List<Expression> getActualArguments() {
		return this.actualArguments;
	}

	public void setActualArguments(List<Expression> actualArguments) {
		this.actualArguments = actualArguments;
	}

	public void accept(Visitor v) {
		v.visit(this);
	}

}
