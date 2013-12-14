package cminor.ast;

import cminor.parser.LocationInfo;
import cminor.visit.Visitor;

public class IdentifierExpression extends Expression {

	private Identifier name;

	public IdentifierExpression(LocationInfo info, Identifier name) {
		super(info);
		this.name = name;
	}

	public Identifier getIdentifier() {
		return this.name;
	}

	public void accept(Visitor v) {
		v.visit(this);
	}

}
