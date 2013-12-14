package cminor.ast;

import cminor.parser.LocationInfo;
import cminor.visit.Visitor;

public class BooleanLiteral extends ConstantExpression<Boolean> {

	public BooleanLiteral(LocationInfo info, Boolean value) {
		super(info, value);
	}

	public void accept(Visitor v) {
		v.visit(this);
	}

}
