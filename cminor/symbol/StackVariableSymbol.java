package cminor.symbol;

import cminor.parser.LocationInfo;
import cminor.semantic.Type;
import cminor.visit.SymbolVisitor;

public class StackVariableSymbol extends Symbol {

	private int offset;

	public StackVariableSymbol(LocationInfo info, String identifier, Type type) {
		super(info, identifier, type);
	}

	public int getOffset() {
		return this.offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public void accept(SymbolVisitor v) {
		v.visit(this);
	}

}
