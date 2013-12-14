package cminor.ast;

import cminor.parser.LocationInfo;
import cminor.visit.Visitor;

public class LogicalAnd extends BinaryLogicalOperator {

	public LogicalAnd(LocationInfo info, Expression left, Expression right) {
		super(info, left, right);
	}

	public void accept(Visitor v) {
		v.visit(this);
	}

}
