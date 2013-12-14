package cminor.ast;

import java.util.List;
import cminor.parser.LocationInfo;
import cminor.symbol.FunctionSymbol;
import cminor.visit.Visitor;

public class FunctionCall extends Expression {

	private Identifier name;
	private List<Expression> arguments;

	private FunctionSymbol symbol;

	public FunctionCall(LocationInfo info, Identifier name, List<Expression> arguments) {
		super(info);
		this.name = name;
		this.arguments = arguments;
	}

	public Identifier getIdentifier() {
		return this.name;
	}

	public List<Expression> getArguments() {
		return this.arguments;
	}

	public FunctionSymbol getSymbol() {
		return this.symbol;
	}

	public void setSymbol(FunctionSymbol symbol) {
		this.symbol = symbol;
	}

	public void accept(Visitor v) {
		v.visit(this);
	}

}
