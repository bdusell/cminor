package cminor.visit;

import java.util.List;
import java.util.ArrayList;
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
import cminor.symbol.SymbolTable;
import cminor.symbol.FunctionSymbol;
import cminor.symbol.GlobalVariableSymbol;
import cminor.symbol.LocalVariableSymbol;
import cminor.symbol.ParameterSymbol;
import cminor.symbol.StringSymbol;
import cminor.semantic.ErrorLogger;

public class SymbolResolutionVisitor extends Visitor {

	private ErrorLogger logger;
	private SymbolTable table;

	private FunctionDefinition mainFunction;
	private List<GlobalVariableDeclaration> globalVariables;
	private List<FunctionDefinition> functions;

	private int numLocals;
	private FunctionDefinition currentFunction;

	public SymbolResolutionVisitor(ErrorLogger logger) {
		this.logger = logger;
		this.table = new SymbolTable(logger);
	}

	public void visit(AstNode n) {
		this.logger.log(n.getLocation(), "symbol resolution in " + n.getDotLabel() + " is a stub");
	}

	public void visit(Program n) {
		this.mainFunction = null;
		this.globalVariables = new ArrayList<GlobalVariableDeclaration>();
		this.functions = new ArrayList<FunctionDefinition>();

		for(ExternalDeclaration c : n.getDeclarations()) c.accept(this);

		if(this.mainFunction == null) {
			logger.log(n.getLocation(),
			"missing " + Program.MAIN_FUNCTION_NAME + " function");
		}
		else n.setMainFunction(this.mainFunction);

		n.setGlobalVariables(this.globalVariables);
		n.setFunctions(this.functions);

		this.globalVariables = null;
		this.functions = null;
	}

	public void visit(FunctionDefinition n) {

		// Get the function definition's parameter list and convert it
		// into a list of parameter symbols
		List<Parameter> parameters = n.getParameters();
		List<ParameterSymbol> parameterSymbols = new ArrayList<ParameterSymbol>(parameters.size());
		int paramOffset = 8;
		for(Parameter p : parameters) {
			ParameterSymbol symbol = new ParameterSymbol(p.getLocation(),
				p.getIdentifier().getString(), p.getType().getType());
			parameterSymbols.add(symbol);
			symbol.setOffset(paramOffset);
			paramOffset += 4;
		}

		// Get the name of the function
		String functionName = n.getIdentifier().getString();

		// Create a new symbol for the function
		FunctionSymbol functionSymbol = new FunctionSymbol(
			n.getLocation(), functionName,
			n.getReturnType().getType(), parameterSymbols);

		// Declare the function in the current (global) scope
		this.table.declareSymbol(n.getIdentifier(), functionSymbol);

		// Store a reference to the function symbol
		n.setSymbol(functionSymbol);

		// If the function is declared as "main", set it as the main function
		// Otherwise add it to the list of regular functions
		if(functionName.equals(Program.MAIN_FUNCTION_NAME)) {
			this.mainFunction = n;
		}
		else {
			this.functions.add(n);
		}

		// Enter the function's scope and declare its parameters, then
		// resolve the symbols in its body
		this.table.enterScope();
		for(int i = 0, m = parameters.size(); i < m; ++i) {
			table.declareSymbol(
				parameters.get(i).getIdentifier(),
				parameterSymbols.get(i));
		}
		this.numLocals = 0;
		this.currentFunction = n;
		visitInSameScope(n.getBody());
		this.table.exitScope();

	}

	public void visit(GlobalVariableDeclaration n) {
		GlobalVariableSymbol symbol = new GlobalVariableSymbol(n.getLocation(),
			n.getIdentifier().getString(), n.getType().getType());
		this.table.declareSymbol(n.getIdentifier(), symbol);
		n.setSymbol(symbol);
		this.globalVariables.add(n);
	}

	public void visit(GlobalVariableInitialization n) {
		visit((GlobalVariableDeclaration) n);
		n.getValue().accept(this);
	}

	public void visit(Declaration n) {
		LocalVariableSymbol symbol = new LocalVariableSymbol(n.getLocation(),
			n.getIdentifier().getString(), n.getType().getType());
		this.table.declareSymbol(n.getIdentifier(), symbol);
		this.currentFunction.raiseNumLocals(++this.numLocals);
		symbol.setOffset(-4 * this.numLocals);
	}

	public void visit(Initialization n) {
		visit((Declaration) n);
		n.getValue().accept(this);
	}

	public void visit(PrintStatement n) {
		for(Expression c : n.getArguments()) c.accept(this);
	}

	public void visit(BlockStatement n) {
		this.table.enterScope();
		visitInSameScope(n);
		this.table.exitScope();
	}

	public void visitInSameScope(BlockStatement n) {
		for(Declaration c : n.getDeclarations()) c.accept(this);
		for(Statement c : n.getStatements()) c.accept(this);
		this.numLocals -= n.getDeclarations().size();
	}

	public void visit(IfStatement n) {
		n.getCondition().accept(this);
		n.getIfClause().accept(this);
	}

	public void visit(IfElseStatement n) {
		visit((IfStatement) n);
		n.getElseClause().accept(this);
	}

	public void visit(WhileStatement n) {
		n.getCondition().accept(this);
		n.getBody().accept(this);
	}

	public void visit(ReturnVoidStatement n) {
		/* Do nothing */
	}

	public void visit(ReturnValueStatement n) {
		n.getValue().accept(this);
	}

	public void visit(Assignment n) {
		this.table.lookupIdentifier(n.getIdentifier());
		n.getValue().accept(this);
	}

	public void visit(FunctionCall n) {
		this.table.lookupIdentifier(n.getIdentifier());
		for(Expression c : n.getArguments()) c.accept(this);
	}

	public void visit(IdentifierExpression n) {
		this.table.lookupIdentifier(n.getIdentifier());
	}

	public void visit(ConstantExpression n) {
		/* No symbols here! */
	}

	public void visit(UnaryExpression n) {
		n.getArg1().accept(this);
	}

	public void visit(BinaryExpression n) {
		n.getArg1().accept(this);
		n.getArg2().accept(this);
	}

	public void visit(Division n) {
		this.logger.log(n.getLocation(),
		"/ operator not supported");
		visit((BinaryExpression) n);
	}

}
