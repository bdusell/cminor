package cminor.ast;

import cminor.parser.LocationInfo;
import cminor.visit.Visitor;

public abstract class BinaryExpression extends UnaryExpression {

	private Expression arg2;

	public BinaryExpression(LocationInfo info, Expression arg1, Expression arg2) {
		super(info, arg1);
		this.arg2 = arg2;
	}

	public Expression getArg2() {
		return this.arg2;
	}

	public void accept(Visitor v) {
		v.visit(this);
	}

}
