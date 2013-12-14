package cminor.symbol;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class StringTable {

	private Map<String, StringSymbol> map;
	private LabelGenerator labeler;

	public StringTable() {
		this.map = new HashMap<String, StringSymbol>();
		this.labeler = new LabelGenerator("string");
	}

	public StringSymbol getSymbol(String value) {
		StringSymbol result = this.map.get(value);
		if(result == null) {
			result = new StringSymbol(value, this.labeler.getLabel());
			this.map.put(value, result);
		}
		return result;
	}

	public List<StringSymbol> getSymbols() {
		return new ArrayList<StringSymbol>(this.map.values());
	}

}
