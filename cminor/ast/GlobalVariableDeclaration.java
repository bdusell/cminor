package cminor.ast;

import cminor.parser.LocationInfo;
import cminor.symbol.GlobalVariableSymbol;
import cminor.visit.Visitor;

public class GlobalVariableDeclaration extends ExternalDeclaration {

	private TypeSpecifier type;

	private GlobalVariableSymbol symbol;

	public GlobalVariableDeclaration(LocationInfo info, TypeSpecifier type, Identifier name) {
		super(info, name);
		this.type = type;
	}

	public TypeSpecifier getType() {
		return this.type;
	}

	public GlobalVariableSymbol getSymbol() {
		return this.symbol;
	}

	public void setSymbol(GlobalVariableSymbol symbol) {
		this.symbol = symbol;
	}

	public void accept(Visitor v) {
		v.visit(this);
	}

}
