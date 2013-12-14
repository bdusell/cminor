package cminor.visit;

import cminor.symbol.Symbol;
import cminor.symbol.GlobalVariableSymbol;
import cminor.symbol.StackVariableSymbol;

public class SymbolLocationVisitor extends SymbolVisitor {

	private String result;

	public static String get(Symbol s) {
		SymbolLocationVisitor v = new SymbolLocationVisitor();
		s.accept(v);
		return v.getResult();
	}

	public String getResult() {
		return this.result;
	}

	public void visit(GlobalVariableSymbol s) {
		this.result = s.getLabel();
	}

	public void visit(StackVariableSymbol s) {
		this.result = s.getOffset() + "(%ebp)";
	}

}
