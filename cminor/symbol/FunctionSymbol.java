package cminor.symbol;

import java.util.List;
import cminor.parser.LocationInfo;
import cminor.semantic.Type;
import cminor.visit.SymbolVisitor;

public class FunctionSymbol extends Symbol {

	private Type returnType;
	private List<ParameterSymbol> parameters;

	public FunctionSymbol(LocationInfo info, String identifier, Type returnType, List<ParameterSymbol> parameters) {
		super(info, identifier, Type.FUNCTION);
		this.returnType = returnType;
		this.parameters = parameters;
	}

	public Type getReturnType() {
		return this.returnType;
	}

	public List<ParameterSymbol> getParameters() {
		return this.parameters;
	}

	public String getLabel() {
		return "function_" + getIdentifier();
	}

	public void accept(SymbolVisitor v) {
		v.visit(this);
	}

}
