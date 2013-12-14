package cminor.ast;

import cminor.parser.LocationInfo;
import cminor.visit.Visitor;

public abstract class ConstantExpression<T> extends Expression {

	private T value;

	public ConstantExpression(LocationInfo info, T value) {
		super(info);
		this.value = value;
	}

	public T getValue() {
		return this.value;
	}

	public void accept(Visitor v) {
		v.visit(this);
	}

}
