package cminor.visit;

import java.util.List;
import cminor.symbol.Symbol;
import cminor.ast.AstNode;
import cminor.ast.Program;
import cminor.ast.Identifier;
import cminor.ast.TypeSpecifier;
import cminor.ast.Parameter;
import cminor.ast.ExternalDeclaration;
import cminor.ast.FunctionDefinition;
import cminor.ast.GlobalVariableDeclaration;
import cminor.ast.GlobalVariableInitialization;
import cminor.ast.Declaration;
import cminor.ast.Initialization;
import cminor.ast.Statement;
import cminor.ast.PrintStatement;
import cminor.ast.BlockStatement;
import cminor.ast.IfStatement;
import cminor.ast.IfElseStatement;
import cminor.ast.WhileStatement;
import cminor.ast.ReturnVoidStatement;
import cminor.ast.ReturnValueStatement;
import cminor.ast.Expression;
import cminor.ast.Assignment;
import cminor.ast.FunctionCall;
import cminor.ast.IdentifierExpression;
import cminor.ast.ConstantExpression;
import cminor.ast.BooleanLiteral;
import cminor.ast.CharacterLiteral;
import cminor.ast.IntegerLiteral;
import cminor.ast.StringLiteral;
import cminor.ast.UnaryExpression;
import cminor.ast.Negative;
import cminor.ast.LogicalNot;
import cminor.ast.BinaryExpression;
import cminor.ast.BinaryArithmeticOperator;
import cminor.ast.Addition;
import cminor.ast.Subtraction;
import cminor.ast.Multiplication;
import cminor.ast.Division;
import cminor.ast.RelationalOperator;
import cminor.ast.EqualTo;
import cminor.ast.NotEqualTo;
import cminor.ast.GreaterThan;
import cminor.ast.GreaterThanOrEqualTo;
import cminor.ast.LessThan;
import cminor.ast.LessThanOrEqualTo;
import cminor.ast.BinaryLogicalOperator;
import cminor.ast.LogicalAnd;
import cminor.ast.LogicalOr;

public class GetSymbolVisitor extends Visitor {

	private List<Symbol> symbols;

	public List<Symbol> getSymbols() {
		return this.symbols;
	}

	public void setSymbols(List<Symbol> symbols) {
		this.symbols = symbols;
	}

	public void visit(AstNode n) {
	}

	public void visit(Program n) {
		FunctionDefinition mainFunction = n.getMainFunction();
		if(mainFunction != null) {
			this.symbols.add(mainFunction.getSymbol());
		}
	}

	public void visit(Identifier n) {
		this.symbols.add(n.getSymbol());
	}

}

