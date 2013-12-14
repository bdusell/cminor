package cminor.ast;

import cminor.parser.LocationInfo;
import cminor.visit.Visitor;

public class IfElseStatement extends IfStatement {

	private Statement elseStatement;

	public IfElseStatement(LocationInfo info, Expression condition, Statement ifStatement, Statement elseStatement) {
		super(info, condition, ifStatement);
		this.elseStatement = elseStatement;
	}

	public Statement getElseClause() {
		return this.elseStatement;
	}

	public void accept(Visitor v) {
		v.visit(this);
	}

}
