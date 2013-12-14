package cminor.ast;

import cminor.parser.LocationInfo;
import cminor.visit.Visitor;

public class IfStatement extends Statement {

	private Expression condition;
	private Statement ifClause;

	public IfStatement(LocationInfo info, Expression condition, Statement ifClause) {
		super(info);
		this.condition = condition;
		this.ifClause = ifClause;
	}

	public Expression getCondition() {
		return this.condition;
	}

	public Statement getIfClause() {
		return this.ifClause;
	}

	public void accept(Visitor v) {
		v.visit(this);
	}

}
