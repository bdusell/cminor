package cminor.visit;

import cminor.symbol.Symbol;
import cminor.symbol.FunctionSymbol;
import cminor.symbol.GlobalVariableSymbol;
import cminor.symbol.LocalVariableSymbol;
import cminor.symbol.ParameterSymbol;

public class SymbolDotLabelVisitor extends SymbolVisitor {

	private String label;

	public String getLabel() {
		return this.label;
	}

	public void visit(FunctionSymbol s) {
		this.label = "FUNCTION " + s.getIdentifier();
	}

	public void visit(GlobalVariableSymbol s) {
		this.label = "GLOBAL " + s.getIdentifier();
	}

	public void visit(LocalVariableSymbol s) {
		this.label = "LOCAL " + s.getIdentifier();
	}

	public void visit(ParameterSymbol s) {
		this.label = "PARAM " + s.getIdentifier();
	}

}

