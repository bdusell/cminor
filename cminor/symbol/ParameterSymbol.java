package cminor.symbol;

import cminor.parser.LocationInfo;
import cminor.semantic.Type;
import cminor.visit.SymbolVisitor;

public class ParameterSymbol extends StackVariableSymbol {

	public ParameterSymbol(LocationInfo info, String identifier, Type type) {
		super(info, identifier, type);
	}

	public void accept(SymbolVisitor v) {
		v.visit(this);
	}

}
