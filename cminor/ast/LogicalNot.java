package cminor.ast;

import cminor.parser.LocationInfo;
import cminor.visit.Visitor;

public class LogicalNot extends UnaryExpression {

	public LogicalNot(LocationInfo info, Expression arg) {
		super(info, arg);
	}

	public void accept(Visitor v) {
		v.visit(this);
	}

}
