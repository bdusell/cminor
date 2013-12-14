package cminor.ast;

import cminor.parser.LocationInfo;
import cminor.visit.Visitor;

public class Assignment extends Expression {

	private Identifier name;
	private Expression value;

	public Assignment(LocationInfo info, Identifier left, Expression right) {
		super(info);
		this.name = left;
		this.value = right;
	}

	public Identifier getIdentifier() {
		return this.name;
	}

	public Expression getValue() {
		return this.value;
	}

	public void accept(Visitor v) {
		v.visit(this);
	}

}
