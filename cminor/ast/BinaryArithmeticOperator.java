package cminor.ast;

import cminor.parser.LocationInfo;
import cminor.visit.Visitor;

public abstract class BinaryArithmeticOperator extends BinaryExpression {

	public BinaryArithmeticOperator(LocationInfo info, Expression arg1, Expression arg2) {
		super(info, arg1, arg2);
	}

	public void accept(Visitor v) {
		v.visit(this);
	}

}
