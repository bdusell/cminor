package cminor.symbol;

import cminor.parser.DotNode;
import cminor.parser.LocationInfo;
import cminor.semantic.Type;
import cminor.visit.SymbolVisitor;
import cminor.visit.SymbolDotLabelVisitor;

public abstract class Symbol implements DotNode {

	private static int numInstances = 0;

	private int instanceNumber;
	private LocationInfo info;
	private String identifier;
	private Type type;

	protected Symbol(LocationInfo info, String identifier, Type type) {
		this.instanceNumber = numInstances++;
		this.identifier = identifier;
		this.info = info;
		this.type = type;
	}

	public LocationInfo getLocation() {
		return this.info;
	}

	public String getIdentifier() {
		return this.identifier;
	}

	public Type getType() {
		return this.type;
	}

	public void accept(SymbolVisitor v) {
		v.visit(this);
	}

	public String getDotId() {
		return "Symbol" + this.instanceNumber;
	}

	public String getDotLabel() {
		SymbolDotLabelVisitor v = new SymbolDotLabelVisitor();
		accept(v);
		return v.getLabel();
	}

}
