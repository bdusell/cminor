package cminor.ast;

import java.util.List;
import cminor.parser.LocationInfo;
import cminor.visit.Visitor;

public class BlockStatement extends Statement {

	private List<Declaration> declarations;
	private List<Statement> statements;

	public BlockStatement(LocationInfo info, List<Declaration> declarations, List<Statement> statements) {
		super(info);
		this.declarations = declarations;
		this.statements = statements;
	}

	public List<Declaration> getDeclarations() {
		return this.declarations;
	}

	public List<Statement> getStatements() {
		return this.statements;
	}

	public void accept(Visitor v) {
		v.visit(this);
	}

}
