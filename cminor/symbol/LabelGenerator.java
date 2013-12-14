package cminor.symbol;

public class LabelGenerator {

	private String prefix;
	private int counter;

	public LabelGenerator(String prefix) {
		this.prefix = prefix;
		this.counter = 0;
	}

	public String getLabel() {
		return this.prefix + "_" + this.counter++;
	}

}
