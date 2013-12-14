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

public class DotLabelVisitor extends Visitor {

	private String label;

	public String getLabel() {
		return this.label;
	}

	public void visit(Program n) {
		this.label = "PROGRAM";
	}

	public void visit(Identifier n) {
		this.label = n.getString();
	}

	public void visit(TypeSpecifier n) {
		this.label = n.getType().getName();
	}

	public void visit(Parameter n) {
		this.label = "PARAM";
	}

	public void visit(FunctionDefinition n) {
		this.label = "FUNCTION";
	}

	public void visit(GlobalVariableDeclaration n) {
		this.label = "GLOBAL";
	}

	public void visit(Declaration n) {
		this.label = "LOCAL";
	}

	public void visit(PrintStatement n) {
		this.label = "PRINT";
	}

	public void visit(BlockStatement n) {
		this.label = "BLOCK";
	}

	public void visit(IfStatement n) {
		this.label = "IF";
	}

	public void visit(WhileStatement n) {
		this.label = "WHILE";
	}

	public void visit(ReturnVoidStatement n) {
		this.label = "RETURN";
	}

	public void visit(Assignment n) {
		this.label = "=";
	}

	public void visit(FunctionCall n) {
		this.label = "CALL";
	}

	public void visit(IdentifierExpression n) {
		this.label = "ID";
	}

	public void visit(BooleanLiteral n) {
		this.label = n.getValue() ? "true" : "false";
	}

	public void visit(CharacterLiteral n) {
		this.label = "\\\'" +
		StringLiteral.escape(
		StringLiteral.escape(
		n.getValue().toString())) +
		"\\\'";
	}

	public void visit(IntegerLiteral n) {
		this.label = n.getValue().toString();
	}

	public void visit(StringLiteral n) {
		this.label = "\\\"" +
		StringLiteral.escape(
		StringLiteral.escape(
		n.getValue())) + "\\\"";
	}

	public void visit(Negative n) {
		this.label = "-";
	}

	public void visit(LogicalNot n) {
		this.label = "!";
	}

	public void visit(Addition n) {
		this.label = "+";
	}

	public void visit(Subtraction n) {
		this.label = "-";
	}

	public void visit(Multiplication n) {
		this.label = "*";
	}

	public void visit(Division n) {
		this.label = "/";
	}

	public void visit(EqualTo n) {
		this.label = "==";
	}

	public void visit(NotEqualTo n) {
		this.label = "!=";
	}

	public void visit(GreaterThan n) {
		this.label = ">";
	}

	public void visit(GreaterThanOrEqualTo n) {
		this.label = ">=";
	}

	public void visit(LessThan n) {
		this.label = "<";
	}

	public void visit(LessThanOrEqualTo n) {
		this.label = "<=";
	}

	public void visit(LogicalAnd n) {
		this.label = "&&";
	}

	public void visit(LogicalOr n) {
		this.label = "||";
	}

}

