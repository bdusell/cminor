package cminor.visit;

import cminor.symbol.Symbol;
import cminor.symbol.FunctionSymbol;
import cminor.symbol.GlobalVariableSymbol;
import cminor.symbol.StackVariableSymbol;
import cminor.symbol.LocalVariableSymbol;
import cminor.symbol.ParameterSymbol;

public class SymbolVisitor {

	public void visit(Symbol s) {}
	public void visit(FunctionSymbol s) { visit((Symbol) s); }
	public void visit(GlobalVariableSymbol s) { visit((Symbol) s); }
	public void visit(StackVariableSymbol s) { visit((Symbol) s); }
	public void visit(LocalVariableSymbol s) { visit((StackVariableSymbol) s); }
	public void visit(ParameterSymbol s) { visit((StackVariableSymbol) s); }

}
