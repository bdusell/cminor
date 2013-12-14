package cminor.ast;

import cminor.parser.LocationInfo;
import cminor.visit.Visitor;

public abstract class ExternalDeclaration extends AstNode {

	private Identifier name;

	public ExternalDeclaration(LocationInfo info, Identifier name) {
		super(info);
		this.name = name;
	}

	public Identifier getIdentifier() {
		return this.name;
	}

	public void accept(Visitor v) {
		v.visit(this);
	}

}
