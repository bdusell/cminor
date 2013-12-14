package cminor.semantic;

public class Type {

	private String name;
	private boolean variableType;
	
	public static final Type CHAR     = new Type("char",     true);
	public static final Type BOOLEAN  = new Type("boolean",  true);
	public static final Type INT      = new Type("int",      true);
	public static final Type STRING   = new Type("string",   true);
	public static final Type VOID     = new Type("void",     false);
	public static final Type FUNCTION = new Type("function", false);

	private Type(String name, boolean isVariableType) {
		this.name = name;
		this.variableType = isVariableType;
	}

	public String getName() {
		return this.name;
	}

	public boolean isVariableType() {
		return this.variableType;
	}

}
