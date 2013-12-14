package cminor.ast;

import java.util.List;
import cminor.parser.LocationInfo;
import cminor.symbol.FunctionSymbol;
import cminor.visit.Visitor;

public class FunctionDefinition extends ExternalDeclaration {

	private TypeSpecifier returnType;
	private List<Parameter> parameters;
	private BlockStatement body;

	private FunctionSymbol symbol;
	private boolean doesEndWithReturn;
	private int numLocals;

	public FunctionDefinition(LocationInfo info, TypeSpecifier returnType, Identifier name,
			List<Parameter> parameters, BlockStatement body) {
		super(info, name);
		this.returnType = returnType;
		this.parameters = parameters;
		this.body = body;
		this.numLocals = 0;
	}

	public TypeSpecifier getReturnType() {
		return this.returnType;
	}

	public List<Parameter> getParameters() {
		return this.parameters;
	}

	public BlockStatement getBody() {
		return this.body;
	}

	public FunctionSymbol getSymbol() {
		return this.symbol;
	}

	public void setSymbol(FunctionSymbol symbol) {
		this.symbol = symbol;
	}

	public boolean endsWithReturn() {
		return this.doesEndWithReturn;
	}

	public void setEndsWithReturn(boolean endsWithReturn) {
		this.doesEndWithReturn = endsWithReturn;
	}

	public int getNumLocals() {
		return this.numLocals;
	}

	public void raiseNumLocals(int max) {
		if(max > this.numLocals) this.numLocals = max;
	}

	public void accept(Visitor v) {
		v.visit(this);
	}

}
