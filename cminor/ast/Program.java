package cminor.ast;

import java.util.List;
import cminor.parser.LocationInfo;
import cminor.symbol.GlobalVariableSymbol;
import cminor.symbol.StringSymbol;
import cminor.visit.Visitor;

public class Program extends AstNode {

	public static final String MAIN_FUNCTION_NAME = "main";

	private List<ExternalDeclaration> declarations;

	private FunctionDefinition mainFunction;
	private List<FunctionDefinition> functions;
	private List<GlobalVariableDeclaration> globalVariables;

	private List<StringSymbol> stringSymbols;
	private StringSymbol boolStringSymbol;

	public Program(LocationInfo info, List<ExternalDeclaration> declarations) {
		super(info);
		this.declarations = declarations;
	}

	public List<ExternalDeclaration> getDeclarations() {
		return this.declarations;
	}

	public FunctionDefinition getMainFunction() {
		return this.mainFunction;
	}

	public void setMainFunction(FunctionDefinition mainFunction) {
		this.mainFunction = mainFunction;
	}

	public void setGlobalVariables(List<GlobalVariableDeclaration> globalVariables) {
		this.globalVariables = globalVariables;
	}

	public List<GlobalVariableDeclaration> getGlobalVariables() {
		return this.globalVariables;
	}

	public void setFunctions(List<FunctionDefinition> functions) {
		this.functions = functions;
	}

	public List<FunctionDefinition> getFunctions() {
		return this.functions;
	}

	public void setStringSymbols(List<StringSymbol> stringSymbols) {
		this.stringSymbols = stringSymbols;
	}

	public List<StringSymbol> getStringSymbols() {
		return this.stringSymbols;
	}

	public void setBooleanStringSymbol(StringSymbol symbol) {
		this.boolStringSymbol = symbol;
	}

	public StringSymbol getBooleanStringSymbol() {
		return this.boolStringSymbol;
	}

	public void accept(Visitor v) {
		v.visit(this);
	}

}
