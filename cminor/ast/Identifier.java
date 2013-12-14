package cminor.ast;

import cminor.parser.LocationInfo;
import cminor.symbol.Symbol;
import cminor.visit.Visitor;

public class Identifier extends AstNode {

	private String name;
	private Symbol symbol;

	public Identifier(LocationInfo info, String name) {
		super(info);
		this.name = name;
		this.symbol = null;
	}

	public String getString() {
		return this.name;
	}

	public Symbol getSymbol() {
		return this.symbol;
	}

	public void setSymbol(Symbol symbol) {
		this.symbol = symbol;
	}

	public void accept(Visitor v) {
		v.visit(this);
	}

}
