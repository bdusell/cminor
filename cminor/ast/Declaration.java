package cminor.ast;

import cminor.parser.LocationInfo;
import cminor.visit.Visitor;

public class Declaration extends AstNode {

	private TypeSpecifier type;
	private Identifier name;

	public Declaration(LocationInfo info, TypeSpecifier type, Identifier name) {
		super(info);
		this.type = type;
		this.name = name;
	}

	public TypeSpecifier getType() {
		return this.type;
	}

	public Identifier getIdentifier() {
		return this.name;
	}

	public void accept(Visitor v) {
		v.visit(this);
	}

}
