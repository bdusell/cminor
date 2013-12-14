package cminor.ast;

import cminor.parser.LocationInfo;
import cminor.semantic.Type;
import cminor.visit.Visitor;

public class TypeSpecifier extends AstNode {

	Type type;

	public TypeSpecifier(LocationInfo info, Type type) {
		super(info);
		this.type = type;
	}

	public Type getType() {
		return this.type;
	}

	public void accept(Visitor v) {
		v.visit(this);
	}

}
