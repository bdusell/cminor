package cminor.symbol;

public class StringSymbol {

	private String value;
	private String label;

	public StringSymbol(String value, String label) {
		this.value = value;
		this.label = label;
	}

	public String getValue() {
		return this.value;
	}

	public String getLabel() {
		return this.label;
	}

}
