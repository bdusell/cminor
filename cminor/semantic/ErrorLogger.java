package cminor.semantic;

import cminor.parser.LocationInfo;

public class ErrorLogger {

	private int numErrors;

	public ErrorLogger() {
		this.numErrors = 0;
	}

	public boolean hasErrors() {
		return this.numErrors > 0;
	}

	public void log(LocationInfo info, String msg) {
		++this.numErrors;
		System.err.println(info + ": " + msg);
	}

}
