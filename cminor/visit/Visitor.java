package cminor.visit;

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

public class Visitor {

	public void visit(AstNode n) {}
	public void visit(Program n) { visit((AstNode) n); }
	public void visit(Identifier n) { visit((AstNode) n); }
	public void visit(TypeSpecifier n) { visit((AstNode) n); }
	public void visit(Parameter n) { visit((AstNode) n); }
	public void visit(ExternalDeclaration n) { visit((AstNode) n); }
	public void visit(FunctionDefinition n) { visit((ExternalDeclaration) n); }
	public void visit(GlobalVariableDeclaration n) { visit((ExternalDeclaration) n); }
	public void visit(GlobalVariableInitialization n) { visit((GlobalVariableDeclaration) n); }
	public void visit(Declaration n) { visit((AstNode) n); }
	public void visit(Initialization n) { visit((Declaration) n); }
	public void visit(Statement n) { visit((AstNode) n); }
	public void visit(PrintStatement n) { visit((Statement) n); }
	public void visit(BlockStatement n) { visit((Statement) n); }
	public void visit(IfStatement n) { visit((Statement) n); }
	public void visit(IfElseStatement n) { visit((IfStatement) n); }
	public void visit(WhileStatement n) { visit((Statement) n); }
	public void visit(ReturnVoidStatement n) { visit((Statement) n); }
	public void visit(ReturnValueStatement n) { visit((ReturnVoidStatement) n); }
	public void visit(Expression n) { visit((Statement) n); }
	public void visit(Assignment n) { visit((Expression) n); }
	public void visit(FunctionCall n) { visit((Expression) n); }
	public void visit(IdentifierExpression n) { visit((Expression) n); }
	public void visit(ConstantExpression n) { visit((Expression) n); }
	public void visit(BooleanLiteral n) { visit((ConstantExpression) n); }
	public void visit(CharacterLiteral n) { visit((ConstantExpression) n); }
	public void visit(IntegerLiteral n) { visit((ConstantExpression) n); }
	public void visit(StringLiteral n) { visit((ConstantExpression) n); }
	public void visit(UnaryExpression n) { visit((Expression) n); }
	public void visit(Negative n) { visit((UnaryExpression) n); }
	public void visit(LogicalNot n) { visit((UnaryExpression) n); }
	public void visit(BinaryExpression n) { visit((UnaryExpression) n); }
	public void visit(BinaryArithmeticOperator n) { visit((BinaryExpression) n); }
	public void visit(Addition n) { visit((BinaryArithmeticOperator) n); }
	public void visit(Subtraction n) { visit((BinaryArithmeticOperator) n); }
	public void visit(Multiplication n) { visit((BinaryArithmeticOperator) n); }
	public void visit(Division n) { visit((BinaryArithmeticOperator) n); }
	public void visit(RelationalOperator n) { visit((BinaryExpression) n); }
	public void visit(EqualTo n) { visit((RelationalOperator) n); }
	public void visit(NotEqualTo n) { visit((RelationalOperator) n); }
	public void visit(GreaterThan n) { visit((RelationalOperator) n); }
	public void visit(GreaterThanOrEqualTo n) { visit((RelationalOperator) n); }
	public void visit(LessThan n) { visit((RelationalOperator) n); }
	public void visit(LessThanOrEqualTo n) { visit((RelationalOperator) n); }
	public void visit(BinaryLogicalOperator n) { visit((BinaryExpression) n); }
	public void visit(LogicalAnd n) { visit((BinaryLogicalOperator) n); }
	public void visit(LogicalOr n) { visit((BinaryLogicalOperator) n); }

}
