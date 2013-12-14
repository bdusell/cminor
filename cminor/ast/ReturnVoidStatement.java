package cminor.ast;

import cminor.parser.LocationInfo;
import cminor.visit.Visitor;

public class ReturnVoidStatement extends Statement {

	public ReturnVoidStatement(LocationInfo info) {
		super(info);
	}

	public void accept(Visitor v) {
		v.visit(this);
	}

}
