package cminor.visit;

import java.util.List;
import java.io.PrintStream;
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
import cminor.symbol.Symbol;
import cminor.symbol.StringSymbol;
import cminor.symbol.LabelGenerator;
import cminor.semantic.Type;

public class CodeGenerationVisitor extends Visitor {

	// Not currently used for the sake of readability
	private static final String ACCUM  = "%eax";
	private static final String STACKP = "%esp";
	private static final String BASEP  = "%ebp";
	private static final String TEMP   = "%ebx";

	private PrintStream output;
	private Program program;
	private boolean inMainFunction;
	private LabelGenerator labeler;

	public CodeGenerationVisitor(PrintStream output) {
		this.output = output;
	}

	public void visit(AstNode n) {
		System.err.println("code generation in " + n.getDotLabel() + " is a stub");
	}

	public void visit(Program n) {
		this.program = n;
		this.labeler = new LabelGenerator("control");

		output.println(".data");

		for(StringSymbol c : n.getStringSymbols()) {
			output.println(c.getLabel() + ": .asciz \"" + StringLiteral.escape(c.getValue()) + "\"");
		}

		for(GlobalVariableDeclaration c : n.getGlobalVariables()) {
			c.accept(this);
		}

		output.println(".section .text\n.globl _start\n_start:");

		this.inMainFunction = true;
		n.getMainFunction().accept(this);
		this.inMainFunction = false;
		for(FunctionDefinition c : n.getFunctions()) {
			c.accept(this);
		}

	}

	public void visit(FunctionDefinition n) {

		if(this.inMainFunction) {
			// If the main function has local variables, set the frame pointer
			if(n.getNumLocals() != 0) output.println("movl %esp, %ebp");
			allocateLocals(n);
			n.getBody().accept(this);
		}
		else {
			// Save the caller's frame pointer and set the new one
			output.println(n.getSymbol().getLabel() + ":\npushl %ebp\nmovl %esp, %ebp");

			// Allocate space for the locals
			allocateLocals(n);

			// Generate code for the body
			n.getBody().accept(this);

			// End with a return if not all paths end in one
			if(!n.endsWithReturn()) printReturnCode();
		}

	}

	private void allocateLocals(FunctionDefinition n) {
		int numLocals = n.getNumLocals();
		if(numLocals != 0) {
			output.println("subl $" + (4 * numLocals) + ", %esp");
		}
	}

	private void printReturnCode() {
		// Deallocate the current frame and restore the caller's frame pointer
		output.println("movl %ebp, %esp\npopl %ebp\nret");
	}

	public void visit(GlobalVariableDeclaration n) {
		output.println(n.getSymbol().getLabel() + ": .long");
	}

	public void visit(GlobalVariableInitialization n) {
		output.println(n.getSymbol().getLabel() + ": .long " + GlobalInitVisitor.get(n.getValue()));
	}

	public void visit(Declaration n) {
		/* Do nothing */
	}

	public void visit(Initialization n) {
		output.println("movl " + ExpressionLocationVisitor.get(n.getValue()) +
			", " + SymbolLocationVisitor.get(n.getIdentifier().getSymbol()));
	}

	public void visit(PrintStatement n) {

		// Push the arguments onto the stack in reverse order
		List<Expression> arguments = n.getActualArguments();
		int numArgs = arguments.size();
		for(int i = numArgs - 1; i >= 0; --i) {
			Expression arg = arguments.get(i);
			arg.accept(this);

			// Select the right string to print for boolean values
			if(arg.getType() == Type.BOOLEAN) {
				// An efficient, jumpless version
				output.println("imull $" + PrintStatement.OFFSET +
					", %eax\naddl $" + program.getBooleanStringSymbol().getLabel() +
					", %eax");
			}

			output.println("pushl %eax");
		}

		// Push the format string and call printf
		output.println("pushl $" + n.getSymbol().getLabel() +
			"\ncall printf");

		// Reset the stack
		output.println("addl $" + (4 * (numArgs + 1)) + ", %esp");

	}

	public void visit(BlockStatement n) {
		for(Declaration c : n.getDeclarations()) c.accept(this);
		for(Statement c : n.getStatements()) c.accept(this);
	}

	public void visit(IfStatement n) {
		String A = this.labeler.getLabel();
		n.getCondition().accept(this);
		output.println("cmpl $0, %eax\nje " + A);
		n.getIfClause().accept(this);
		output.println(A + ":");
	}

	public void visit(IfElseStatement n) {
		String A = this.labeler.getLabel(), B = this.labeler.getLabel();
		n.getCondition().accept(this);
		output.println("cmpl $0, %eax\nje " + A);
		n.getIfClause().accept(this);
		output.println("jmp " + B + "\n" + A + ":");
		n.getElseClause().accept(this);
		output.println(B + ":");
	}

	public void visit(WhileStatement n) {
		String A = this.labeler.getLabel(), B = this.labeler.getLabel();
		output.println(A + ":");
		n.getCondition().accept(this);
		output.println("cmpl $0, %eax\nje " + B);
		n.getBody().accept(this);
		output.println("jmp " + A + "\n" + B + ":");
	}

	public void visit(ReturnVoidStatement n) {
		printReturnCode();
	}

	public void visit(ReturnValueStatement n) {
		n.getValue().accept(this);
		if(this.inMainFunction) {
			// If in the main function, just exit
			output.println("pushl %eax\ncall exit");
		}
		else {
			printReturnCode();
		}
	}

	public void visit(Assignment n) {
		n.getValue().accept(this);
		output.println("movl %eax, " +
			SymbolLocationVisitor.get(n.getIdentifier().getSymbol()));
		// Return value is already in %eax
	}

	public void visit(FunctionCall n) {

		// Push the arguments in reverse order
		List<Expression> arguments = n.getArguments();
		int numArgs = arguments.size();
		for(int i = numArgs - 1; i >= 0; --i) {
			arguments.get(i).accept(this);
			output.println("pushl %eax");
		}

		// Call the function
		output.println("call " + n.getSymbol().getLabel());

		// Reset the stack
		if(numArgs != 0) output.println("addl $" + (numArgs * 4) + ", %esp");

	}

	public void visit(IdentifierExpression n) {
		output.println("movl " + SymbolLocationVisitor.get(n.getIdentifier().getSymbol()) + ", %eax");
	}

	public void visit(ConstantExpression n) {
		output.println("movl " + ExpressionLocationVisitor.get(n) + ", %eax");
	}

	public void visit(Negative n) {
		n.getArg1().accept(this);
		output.println("negl %eax");
	}

	public void visit(LogicalNot n) {
		// Jumpless version, using identity !x = -x + 1
		n.getArg1().accept(this);
		output.println("negl %eax\nincl %eax");
	}

	public void visit(Addition n) {
		arith(n, "addl");
	}

	public void visit(Subtraction n) {
		arith(n, "subl");
	}

	public void visit(Multiplication n) {
		arith(n, "imull");
	}

	public void visit(EqualTo n) {
		comp(n, "je");
	}

	public void visit(NotEqualTo n) {
		comp(n, "jne");
	}

	public void visit(GreaterThan n) {
		comp(n, "jg");
	}

	public void visit(GreaterThanOrEqualTo n) {
		comp(n, "jge");
	}

	public void visit(LessThan n) {
		comp(n, "jl");
	}

	public void visit(LessThanOrEqualTo n) {
		comp(n, "jle");
	}

	public void visit(LogicalAnd n) {
		logical(n, "je");
	}

	public void visit(LogicalOr n) {
		logical(n, "jne");
	}

	private void arith(BinaryArithmeticOperator n, String inst) {
		
		// Evaluate the second expression and save the result
		n.getArg2().accept(this);
		output.println("pushl %eax");

		// Evaluate the other expression, operate on the results, and restore the stack
		n.getArg1().accept(this);
		output.println(inst + " (%esp), %eax\naddl $4, %esp");

	}

	private void comp(RelationalOperator n, String inst) {
		n.getArg2().accept(this);
		output.println("pushl %eax");
		n.getArg1().accept(this);
		String A = this.labeler.getLabel(), B = this.labeler.getLabel();
		output.println("popl %ebx\ncmpl %ebx, %eax\n" + inst + " " + A +
			"\nmovl $0, %eax\njmp " + B + "\n" + A + ":\nmovl $1, %eax\n" + B + ":");
	}

	private void logical(BinaryLogicalOperator n, String inst) {
		n.getArg1().accept(this);
		String label = this.labeler.getLabel();
		output.println("cmpl $0, %eax\n" + inst + " " + label);
		n.getArg2().accept(this);
		output.println(label + ":");
	}

}
