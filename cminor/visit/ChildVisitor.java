package cminor.visit;

import java.util.List;
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

public class ChildVisitor extends Visitor {

	private List<AstNode> children;

	public List<AstNode> getChildren() {
		return this.children;
	}

	public void setChildren(List<AstNode> children) {
		this.children = children;
	}

	public void visit(Program n) {
		this.children.addAll(n.getDeclarations());
	}

	public void visit(Parameter n) {
		this.children.add(n.getType());
		this.children.add(n.getIdentifier());
	}

	public void visit(FunctionDefinition n) {
		this.children.add(n.getReturnType());
		this.children.add(n.getIdentifier());
		this.children.addAll(n.getParameters());
		this.children.add(n.getBody());
	}

	public void visit(GlobalVariableDeclaration n) {
		this.children.add(n.getType());
		this.children.add(n.getIdentifier());
	}

	public void visit(GlobalVariableInitialization n) {
		this.children.add(n.getType());
		this.children.add(n.getIdentifier());
		this.children.add(n.getValue());
	}

	public void visit(Declaration n) {
		this.children.add(n.getType());
		this.children.add(n.getIdentifier());
	}

	public void visit(Initialization n) {
		this.children.add(n.getType());
		this.children.add(n.getIdentifier());
		this.children.add(n.getValue());
	}

	public void visit(PrintStatement n) {
		this.children.addAll(n.getArguments());
	}

	public void visit(BlockStatement n) {
		this.children.addAll(n.getDeclarations());
		this.children.addAll(n.getStatements());
	}

	public void visit(IfStatement n) {
		this.children.add(n.getCondition());
		this.children.add(n.getIfClause());
	}

	public void visit(IfElseStatement n) {
		this.children.add(n.getCondition());
		this.children.add(n.getIfClause());
		this.children.add(n.getElseClause());
	}

	public void visit(WhileStatement n) {
		this.children.add(n.getCondition());
		this.children.add(n.getBody());
	}

	public void visit(ReturnValueStatement n) {
		this.children.add(n.getValue());
	}

	public void visit(Assignment n) {
		this.children.add(n.getIdentifier());
		this.children.add(n.getValue());
	}

	public void visit(FunctionCall n) {
		this.children.add(n.getIdentifier());
		this.children.addAll(n.getArguments());
	}

	public void visit(IdentifierExpression n) {
		this.children.add(n.getIdentifier());
	}

	public void visit(UnaryExpression n) {
		this.children.add(n.getArg1());
	}

	public void visit(BinaryExpression n) {
		this.children.add(n.getArg1());
		this.children.add(n.getArg2());
	}

}

