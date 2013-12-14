package cminor.ast;

import cminor.parser.LocationInfo;
import cminor.visit.Visitor;

public class IntegerLiteral extends ConstantExpression<Integer> {

	public IntegerLiteral(LocationInfo info, Integer value) {
		super(info, value);
	}

	public void accept(Visitor v) {
		v.visit(this);
	}

}
