package cminor.ast;

import cminor.parser.LocationInfo;
import cminor.visit.Visitor;

public class Negative extends UnaryExpression {

	public Negative(LocationInfo info, Expression arg) {
		super(info, arg);
	}

	public void accept(Visitor v) {
		v.visit(this);
	}

}
