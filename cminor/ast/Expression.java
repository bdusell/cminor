package cminor.ast;

import cminor.parser.LocationInfo;
import cminor.semantic.Type;
import cminor.visit.Visitor;

public abstract class Expression extends Statement {

	private Type type;

	protected Expression(LocationInfo info) {
		super(info);
	}

	public Type getType() {
		return this.type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public void accept(Visitor v) {
		v.visit(this);
	}

}
