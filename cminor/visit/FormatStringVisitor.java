package cminor.visit;

import java.util.List;
import cminor.ast.AstNode;
import cminor.ast.Program;
import cminor.ast.PrintStatement;
import cminor.ast.Expression;
import cminor.ast.ConstantExpression;
import cminor.ast.BooleanLiteral;
import cminor.ast.CharacterLiteral;
import cminor.ast.IntegerLiteral;
import cminor.ast.StringLiteral;
import cminor.symbol.StringTable;
import cminor.semantic.ErrorLogger;
import cminor.semantic.Type;

class FormatStringVisitor extends Visitor {

	private StringBuffer result;
	private StringBuffer literalContent;
	private List<Expression> actualArguments;
	private StringTable stringTable;
	private Program program;
	private ErrorLogger logger;

	public FormatStringVisitor(StringBuffer result, List<Expression> actualArguments,
			StringTable stringTable, Program program, ErrorLogger logger) {
		this.result = result;
		this.literalContent = new StringBuffer();
		this.actualArguments = actualArguments;
		this.stringTable = stringTable;
		this.program = program;
		this.logger = logger;
	}

	public void visit(AstNode n) {
		this.logger.log(n.getLocation(),
		"format string visitor in " + n.getDotLabel() + " is a stub");
	}

	public void visit(Expression n) {

		// Non-constant expressions

		// Add the expression to the list of printf arguments
		this.actualArguments.add(n);

		// Escape and add the literal content read so far to the format string
		this.result.append(escape(this.literalContent.toString()));
		this.literalContent.setLength(0);

		// Add the appropriate format specifier
		Type t = n.getType();
		if(t == Type.CHAR) this.result.append(PrintStatement.CHAR_FORMAT);
		else if(t == Type.BOOLEAN) {
			// If a boolean is being printed, set it up so that "true" or "false" will be printed
			this.result.append(PrintStatement.STRING_FORMAT);
			// Doing some kinky string table stuff here
			this.program.setBooleanStringSymbol(
				this.stringTable.getSymbol(PrintStatement.BOOL_STRING));
		}
		else if(t == Type.INT) this.result.append(PrintStatement.INT_FORMAT);
		else if(t == Type.STRING) this.result.append(PrintStatement.STRING_FORMAT);
	}

	public void visit(ConstantExpression n) {
		this.literalContent.append(n.getValue());
	}

	public void visit(BooleanLiteral n) {
		this.literalContent.append(n.getValue() ? PrintStatement.TRUE_STRING : PrintStatement.FALSE_STRING);
	}

	public void finish() {
		this.result.append(escape(this.literalContent.toString()));
	}

	private String escape(String s) {
		return s.replace("%", "%%");
	}

}
