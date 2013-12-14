package cminor.symbol;

import cminor.parser.LocationInfo;
import cminor.semantic.Type;
import cminor.visit.SymbolVisitor;

public class GlobalVariableSymbol extends Symbol {

	public GlobalVariableSymbol(LocationInfo info, String identifier, Type type) {
		super(info, identifier, type);
	}

	public String getLabel() {
		return "global_" + getIdentifier();
	}

	public void accept(SymbolVisitor v) {
		v.visit(this);
	}

}
