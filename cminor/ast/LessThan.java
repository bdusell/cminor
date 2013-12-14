package cminor.ast;

import cminor.parser.LocationInfo;
import cminor.visit.Visitor;

public class LessThan extends RelationalOperator {

	public LessThan(LocationInfo info, Expression left, Expression right) {
		super(info, left, right);
	}

	public void accept(Visitor v) {
		v.visit(this);
	}

}
