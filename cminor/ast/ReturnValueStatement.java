package cminor.ast;

import cminor.parser.LocationInfo;
import cminor.visit.Visitor;

public class ReturnValueStatement extends ReturnVoidStatement {

	private Expression value;

	public ReturnValueStatement(LocationInfo info, Expression value) {
		super(info);
		this.value = value;
	}

	public Expression getValue() {
		return this.value;
	}

	public void accept(Visitor v) {
		v.visit(this);
	}

}
