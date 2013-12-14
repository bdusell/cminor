package cminor.ast;

import cminor.parser.LocationInfo;
import cminor.visit.Visitor;

public abstract class UnaryExpression extends Expression {

	private Expression arg1;

	public UnaryExpression(LocationInfo info, Expression arg1) {
		super(info);
		this.arg1 = arg1;
	}

	public Expression getArg1() {
		return this.arg1;
	}

	public void accept(Visitor v) {
		v.visit(this);
	}

}
