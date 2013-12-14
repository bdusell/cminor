package cminor.ast;

import cminor.parser.LocationInfo;
import cminor.visit.Visitor;

public class Initialization extends Declaration {

	private ConstantExpression value;

	public Initialization(LocationInfo info, TypeSpecifier type, Identifier name, ConstantExpression value) {
		super(info, type, name);
		this.value = value;
	}

	public ConstantExpression getValue() {
		return this.value;
	}

	public void accept(Visitor v) {
		v.visit(this);
	}

}
