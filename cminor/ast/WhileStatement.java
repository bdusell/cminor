package cminor.ast;

import cminor.parser.LocationInfo;
import cminor.visit.Visitor;

public class WhileStatement extends Statement {

	private Expression condition;
	private Statement body;

	public WhileStatement(LocationInfo info, Expression condition, Statement body) {
		super(info);
		this.condition = condition;
		this.body = body;
	}

	public Expression getCondition() {
		return this.condition;
	}

	public Statement getBody() {
		return this.body;
	}

	public void accept(Visitor v) {
		v.visit(this);
	}

}
