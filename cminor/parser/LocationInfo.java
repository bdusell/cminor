package cminor.parser;

public class LocationInfo {

	private String file;
	private int line;
	private int col;

	public LocationInfo(String file, int line, int col) {
		this.file = file;
		this.line = line;
		this.col = col;
	}

	public String toString() {
		return file + ':' + line + ':' + col;
	}

}
