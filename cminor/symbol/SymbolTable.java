package cminor.symbol;

import java.util.List;
import java.util.HashMap;
import cminor.parser.LocationInfo;
import cminor.ast.Identifier;
import cminor.ast.Parameter;
import cminor.semantic.Type;
import cminor.semantic.ErrorLogger;

public class SymbolTable {

	private Scope currentScope;
	private ErrorLogger errorLogger;

	public SymbolTable(ErrorLogger errorLogger) {
		this.currentScope = new Scope();
		this.errorLogger = errorLogger;
	}

	private class Scope {

		private HashMap<String, Symbol> symbols;
		private Scope parentScope;

		public Scope() {
			this.symbols = new HashMap<String, Symbol>();
			this.parentScope = null;
		}

		public Scope(Scope parentScope) {
			this.symbols = new HashMap<String, Symbol>();
			this.parentScope = parentScope;
		}

		public boolean containsKey(String identifier) {
			return this.symbols.containsKey(identifier);
		}

		private void declareSymbol(String identifier, Symbol symbol) {
			Symbol check = probe(identifier);
			if(check == null) {
				this.symbols.put(identifier, symbol);
			}
			else {
				SymbolTable.this.errorLogger.log(symbol.getLocation(),
				"cannot re-declare symbol \'" + identifier +
				"\' in same scope (previously declared at " +
				check.getLocation() + ")");
			}
		}

		public Scope parentScope() {
			return this.parentScope;
		}

		public boolean hasParentScope() {
			return this.parentScope != null;
		}

		public Symbol probe(String identifier) {
			return this.symbols.get(identifier);
		}

	}

	public void enterScope() {
		this.currentScope = new Scope(this.currentScope);
	}

	public void exitScope() {
		this.currentScope = this.currentScope.parentScope();
	}

	public Symbol lookup(LocationInfo info, String identifier) {
		Scope curr = this.currentScope;
		Symbol result;
		while(true) {
			result = curr.probe(identifier);
			if(result != null) return result;
			else if(curr.hasParentScope()) curr = curr.parentScope();
			else break;
		}
		this.errorLogger.log(info, "symbol \'" + identifier + "\' has not been declared");
		return null;
	}

	public Symbol probe(LocationInfo info, String identifier) {
		Symbol result = this.currentScope.probe(identifier);
		if(result == null) {
			this.errorLogger.log(info,
			"symbol \'" + identifier + "\' has not been declared");
		}
		return result;
	}

	public void lookupIdentifier(Identifier identifier) {
		identifier.setSymbol(lookup(identifier.getLocation(), identifier.getString()));
	}

	public void declareSymbol(Identifier identifier, Symbol symbol) {
		this.currentScope.declareSymbol(identifier.getString(), symbol);
		identifier.setSymbol(symbol);
	}

}
