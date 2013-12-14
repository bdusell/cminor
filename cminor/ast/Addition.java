package cminor.ast;

import cminor.parser.LocationInfo;
import cminor.visit.Visitor;

public class Addition extends BinaryArithmeticOperator {

	public Addition(LocationInfo info, Expression left, Expression right) {
		super(info, left, right);
	}

	public void accept(Visitor v) {
		v.visit(this);
	}

}
