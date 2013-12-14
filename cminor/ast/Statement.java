package cminor.ast;

import cminor.parser.LocationInfo;
import cminor.visit.Visitor;

public abstract class Statement extends AstNode {

	protected Statement(LocationInfo info) {
		super(info);
	}

	public void accept(Visitor v) {
		v.visit(this);
	}

}
