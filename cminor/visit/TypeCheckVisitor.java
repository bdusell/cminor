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
import cminor.semantic.Type;
import cminor.semantic.ErrorLogger;
import cminor.symbol.StringTable;
import cminor.symbol.Symbol;
import cminor.symbol.FunctionSymbol;
import cminor.symbol.ParameterSymbol;
import cminor.symbol.StringSymbol;

public class TypeCheckVisitor extends Visitor {

	private ErrorLogger logger;
	private StringTable stringTable;

	private FunctionSymbol currentFunction;
	private boolean endsWithReturn;
	private Program program;
	private boolean inPrintStatement;

	public TypeCheckVisitor(ErrorLogger logger) {
		this.logger = logger;
	}

	public void visit(AstNode n) {
		this.logger.log(n.getLocation(), "type checking in " + n.getDotLabel() + " is a stub");
	}

	public void visit(Program n) {

		this.program = n;

		this.stringTable = new StringTable();
		this.inPrintStatement = false;

		// Check the signature of the main function
		FunctionSymbol mainFunction = n.getMainFunction().getSymbol();
		if(mainFunction.getReturnType() != Type.INT) {
			this.logger.log(n.getLocation(),
			Program.MAIN_FUNCTION_NAME + " function must have return type " + Type.INT.getName());
		}
		if(mainFunction.getParameters().size() != 0) {
			this.logger.log(n.getLocation(),
			Program.MAIN_FUNCTION_NAME + " function must take no parameters");
		}

		// Check the declarations
		for(ExternalDeclaration c : n.getDeclarations()) c.accept(this);

		// Give the list of string symbols collected by the string table to the program node
		n.setStringSymbols(this.stringTable.getSymbols());

	}

	public void visit(Parameter n) {
		checkVariableType(n, n.getType(), "parameter");
	}

	public void visit(FunctionDefinition n) {

		for(Parameter c : n.getParameters()) c.accept(this);

		// Set the current function
		this.currentFunction = n.getSymbol();

		// Unset the return flag
		this.endsWithReturn = false;

		// Type check the body
		n.getBody().accept(this);

		// If a non-void function does not return in all paths of execution,
		// raise an error
		FunctionSymbol functionSymbol = n.getSymbol();
		if(!this.endsWithReturn && this.currentFunction.getReturnType().isVariableType()) {
			this.logger.log(n.getLocation(),
			"non-void function " + functionSymbol.getIdentifier() +
			" does not return a value in all paths of execution");
		}
		n.setEndsWithReturn(this.endsWithReturn);

		this.currentFunction = null;

	}

	public void visit(GlobalVariableDeclaration n) {
		checkVariableType(n, n.getType(), "variable");
	}

	public void visit(GlobalVariableInitialization n) {
		n.getValue().accept(this);
		if(checkVariableType(n, n.getType(), "variable")) {
			checkMatch(n,
			n.getType().getType(),
			n.getValue().getType(),
			"global variable initialization");
		}
	}

	public void visit(Declaration n) {
		checkVariableType(n, n.getType(), "variable");
	}

	public void visit(Initialization n) {
		n.getValue().accept(this);
		if(checkVariableType(n, n.getType(), "variable")) {
			checkMatch(n,
			n.getType().getType(),
			n.getValue().getType(),
			"local variable initialization");
		}
	}

	public void visit(PrintStatement n) {

		for(Expression c : n.getArguments()) {
			// Tell string literals that they are embedded in a print statement
			this.inPrintStatement = true;
			c.accept(this);
		}
		this.inPrintStatement = false;

		String formatString = makeFormatString(n);
		StringSymbol formatSymbol = this.stringTable.getSymbol(formatString);
		n.setSymbol(formatSymbol);

	}

	public void visit(BlockStatement n) {
		for(Declaration c : n.getDeclarations()) c.accept(this);

		for(Statement c : n.getStatements()) {
			if(this.endsWithReturn) {
				this.logger.log(c.getLocation(), "unreachable statement");
				break;
			}
			c.accept(this);
		}
	}

	public void visit(IfStatement n) {
		checkCondition(n, n.getCondition(), "if statement");
		n.getIfClause().accept(this);
		// Since we cannot be sure any nested return statements will be reached
		// within a conditional block, we cannot say that this statement ends
		// with one
	}

	public void visit(IfElseStatement n) {
		checkCondition(n, n.getCondition(), "if statement");
		n.getIfClause().accept(this);
		boolean temp = this.endsWithReturn;
		this.endsWithReturn = false;
		n.getElseClause().accept(this);
		// If both branches definitely end in a return statement, then we can
		// say that the whole thing always returns
		this.endsWithReturn = temp && this.endsWithReturn;
	}

	public void visit(WhileStatement n) {
		checkCondition(n, n.getCondition(), "while statement");
		n.getBody().accept(this);
		// Might not execute even once, cannot say that it definitely ends in a return
	}

	public void visit(ReturnVoidStatement n) {
		Type returnType = this.currentFunction.getReturnType();
		if(returnType != Type.VOID) {
			this.logger.log(n.getLocation(),
			"return statement missing value in function " +
			this.currentFunction.getIdentifier());
		}
		this.endsWithReturn = true;
	}

	public void visit(ReturnValueStatement n) {

		// Resolve the type of the returned value
		n.getValue().accept(this);

		// Check that the type of the returned value matches the function signature
		Type returnType = this.currentFunction.getReturnType();
		Type expressionType = n.getValue().getType();
		if(returnType != expressionType) {
			String msg = returnType == Type.VOID ?
			("function with " + Type.VOID.getName() + " return type cannot return a value") :
			("return statement in function " + this.currentFunction.getIdentifier() +
				" requires expression of type " + returnType.getName() +
				", got " + expressionType.getName());
			this.logger.log(n.getLocation(), msg);
		}
		this.endsWithReturn = true;

	}

	public void visit(Assignment n) {

		// Tell the string literals in the subtrees that they are not embedded in a print statement
		this.inPrintStatement = false;

		Expression value = n.getValue();
		value.accept(this);
		Type varType = n.getIdentifier().getSymbol().getType();
		Type valueType = value.getType();
		checkMatch(n, varType, valueType, "variable assignment");
		n.setType(varType);
	}

	public void visit(FunctionCall n) {

		this.inPrintStatement = false;

		List<Expression> arguments = n.getArguments();

		// Resolve all of the argument types
		for(Expression c : arguments) c.accept(this);

		// Make sure that the symbol being called has been declared as a function
		Symbol symbol = n.getIdentifier().getSymbol();
		Type calleeType = symbol.getType();
		if(calleeType != Type.FUNCTION) {
			this.logger.log(n.getLocation(),
			"symbol " + symbol.getIdentifier() +
			" called as function but is of type " + calleeType.getName());
			// For the time being set the type of the expression to that of the symbol
			n.setType(calleeType);
		}
		else {
			FunctionSymbol functionSymbol = (FunctionSymbol) symbol;
			n.setSymbol(functionSymbol);

			// Check that the types of the arguments match those of the parameters
			List<ParameterSymbol> parameters = functionSymbol.getParameters();

			int aSize = arguments.size(), pSize = parameters.size();
			List<Type> argumentTypes = new ArrayList<Type>(aSize), parameterTypes = new ArrayList<Type>(pSize);
			for(Expression c : arguments) argumentTypes.add(c.getType());
			for(ParameterSymbol c : parameters) parameterTypes.add(c.getType());

			if(!argumentTypes.equals(parameterTypes)) {
				this.logger.log(n.getLocation(),
				"actual and formal parameters in function call do not match\n\treceived: " +
				typeListString(argumentTypes) + "\n\texpected: " + typeListString(parameterTypes));
			}

			// Set the expression type to the return type
			n.setType(functionSymbol.getReturnType());

		}

	}

	public void visit(IdentifierExpression n) {
		// Look up the type of the identifier and set it as the expression type
		n.setType(n.getIdentifier().getSymbol().getType());
	}

	public void visit(BooleanLiteral n) {
		n.setType(Type.BOOLEAN);
	}

	public void visit(CharacterLiteral n) {
		n.setType(Type.CHAR);
	}

	public void visit(IntegerLiteral n) {
		n.setType(Type.INT);
	}

	public void visit(StringLiteral n) {
		// Fetch a symbol for the string literal, unless it is embedded in a print statement
		if(!this.inPrintStatement) {
			n.setSymbol(this.stringTable.getSymbol(n.getValue()));
		}
		n.setType(Type.STRING);
	}

	public void visit(Negative n) {
		requireType(n, "unary -", IS_ARITHMETIC);
	}

	public void visit(LogicalNot n) {
		requireType(n, "!", IS_LOGICAL);
	}

	public void visit(Addition n) {
		requireTypes(n, "+", IS_ARITHMETIC);
	}

	public void visit(Subtraction n) {
		requireTypes(n, "-", IS_ARITHMETIC);
	}

	public void visit(Multiplication n) {
		requireTypes(n, "*", IS_ARITHMETIC);
	}

	public void visit(Division n) {
		requireTypes(n, "/", IS_ARITHMETIC);
	}

	public void visit(EqualTo n) {
		requireTypes(n, "==", IS_EQUATABLE);
	}

	public void visit(NotEqualTo n) {
		requireTypes(n, "!=", IS_EQUATABLE);
	}

	public void visit(GreaterThan n) {
		requireTypes(n, ">", IS_COMPARABLE);
	}

	public void visit(GreaterThanOrEqualTo n) {
		requireTypes(n, ">=", IS_COMPARABLE);
	}

	public void visit(LessThan n) {
		requireTypes(n, "<", IS_COMPARABLE);
	}

	public void visit(LessThanOrEqualTo n) {
		requireTypes(n, "<=", IS_COMPARABLE);
	}

	public void visit(LogicalAnd n) {
		requireTypes(n, "&&", IS_LOGICAL);
	}

	public void visit(LogicalOr n) {
		requireTypes(n, "||", IS_LOGICAL);
	}

	private boolean checkVariableType(AstNode n, TypeSpecifier t, String name) {
		boolean result = t.getType().isVariableType();
		if(!result) {
			this.logger.log(n.getLocation(),
			"a " + name + " may not have type " + Type.VOID.getName());
		}
		return result;
	}

	private void checkMatch(AstNode n, Type t1, Type t2, String name) {
		if(t1 != t2) {
			this.logger.log(n.getLocation(),
			"type mismatch in " + name + " (got " +
			t1.getName() + " and " + t2.getName() + ")");
		}
	}

	private void checkCondition(AstNode n, Expression c, String name) {
		c.accept(this); // Resolve the type of the condition
		Type t = c.getType();
		if(t != Type.BOOLEAN) {
			this.logger.log(n.getLocation(),
			"condition in " + name + " must be of type " + t.getName());
		}
	}

	private String typeListString(List<Type> types) {
		StringBuffer result = new StringBuffer();
		boolean passed = false;
		for(Type t : types) {
			if(passed) result.append(", ");
			else passed = true;
			result.append(t.getName());
		}
		return result.toString();
	}

	private static abstract class TypeTester {
		public abstract boolean test(Type t);
		public abstract String desc();
		public abstract Type synth();
	}

	private static final TypeTester IS_ARITHMETIC = new TypeTester() {
		public boolean test(Type t) { return t == Type.INT; }
		public String desc() { return "arithmetic"; }
		/* This could be used, for example, to implement type promotion (with a
		couple of type arguments added) */
		public Type synth() { return Type.INT; }
	};

	private static final TypeTester IS_EQUATABLE = new TypeTester() {
		public boolean test(Type t) { return t == Type.INT || t == Type.CHAR || t == Type.BOOLEAN; }
		public String desc() { return "equatable"; }
		public Type synth() { return Type.BOOLEAN; }
	};

	private static final TypeTester IS_COMPARABLE = new TypeTester() {
		public boolean test(Type t) { return t == Type.INT; }
		public String desc() { return "comparable"; }
		public Type synth() { return Type.BOOLEAN; }
	};

	private static final TypeTester IS_LOGICAL = new TypeTester() {
		public boolean test(Type t) { return t == Type.BOOLEAN; }
		public String desc() { return "logical"; }
		public Type synth() { return Type.BOOLEAN; }
	};

	private void requireType(Expression n, Expression arg, String name, TypeTester t) {

		// As a convenience, turn the print statement switch off here since
		// this is called for all operator expressions
		this.inPrintStatement = false;

		arg.accept(this);
		Type argType = arg.getType();
		if(!t.test(argType)) {
			this.logger.log(n.getLocation(),
			name + " operator requires operands of " + t.desc() + " type (got " +
			argType.getName() + ")");
		}
	}

	private void requireType(UnaryExpression n, String name, TypeTester t) {
		requireType(n, n.getArg1(), name, t);
		n.setType(t.synth());
	}

	private void requireTypes(BinaryExpression n, String name, TypeTester t) {
		requireType(n, n.getArg1(), name, t);
		requireType(n, n.getArg2(), name, t);
		Type t1 = n.getArg1().getType();
		Type t2 = n.getArg2().getType();
		checkMatch(n, t1, t2, name);
		n.setType(t.synth());
	}

	private String makeFormatString(PrintStatement n) {

		// Build the format string and the list of actual arguments using a visitor
		StringBuffer result = new StringBuffer();
		List<Expression> actualArguments = new ArrayList<Expression>();

		FormatStringVisitor v = new FormatStringVisitor(
		result, actualArguments, this.stringTable, this.program, this.logger);

		for(Expression c : n.getArguments()) {
			c.accept(v);
		}
		v.finish(); // Flushes the literal content buffer so it can be escaped

		n.setActualArguments(actualArguments);
		return result.toString();

	}

}
