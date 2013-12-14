package cminor.ast;

import cminor.parser.LocationInfo;
import cminor.visit.Visitor;

public class GlobalVariableInitialization extends GlobalVariableDeclaration {

	private ConstantExpression value;

	public GlobalVariableInitialization(LocationInfo info, TypeSpecifier type, Identifier name, ConstantExpression value) {
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
