package cminor.visit;

import cminor.ast.AstNode;
import cminor.ast.BooleanLiteral;
import cminor.ast.CharacterLiteral;
import cminor.ast.IntegerLiteral;
import cminor.ast.StringLiteral;

public class GlobalInitVisitor extends Visitor {

	private String result;

	public static String get(AstNode n) {
		GlobalInitVisitor v = new GlobalInitVisitor();
		n.accept(v);
		return v.getResult();
	}

	public String getResult() {
		return this.result;
	}

	public void visit(BooleanLiteral n) {
		this.result = n.getValue() ? "1" : "0";
	}

	public void visit(CharacterLiteral n) {
		this.result = "" + (int) n.getValue();
	}

	public void visit(IntegerLiteral n) {
		this.result = n.getValue().toString();
	}

	public void visit(StringLiteral n) {
		this.result = n.getSymbol().getLabel();
	}

}
