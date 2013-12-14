package cminor.visit;

import cminor.ast.AstNode;
import cminor.ast.IdentifierExpression;
import cminor.ast.ConstantExpression;
import cminor.ast.BooleanLiteral;
import cminor.ast.CharacterLiteral;
import cminor.ast.IntegerLiteral;
import cminor.ast.StringLiteral;

public class ExpressionLocationVisitor extends Visitor {

	private String result;

	public static String get(AstNode n) {
		ExpressionLocationVisitor v = new ExpressionLocationVisitor();
		n.accept(v);
		return v.getResult();
	}

	public String getResult() {
		return this.result;
	}

	public void visit(IdentifierExpression n) {
		this.result = SymbolLocationVisitor.get(n.getIdentifier().getSymbol());
	}

	public void visit(ConstantExpression n) {
		this.result = "$" + GlobalInitVisitor.get(n);
	}

}
