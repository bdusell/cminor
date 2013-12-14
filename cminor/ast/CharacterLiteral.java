package cminor.ast;

import cminor.parser.LocationInfo;
import cminor.visit.Visitor;

public class CharacterLiteral extends ConstantExpression<Character> {

	public CharacterLiteral(LocationInfo info, Character value) {
		super(info, value);
	}

	public void accept(Visitor v) {
		v.visit(this);
	}

}
