package parserStackFrames;

import helpers.Vector;

public class StackFrame<T> {
	private Vector<T> elements;

	public Vector<T> getElements() {
		return elements;
	}

	public void setElements(Vector<T> elements) {
		this.elements = elements;
	}
}
