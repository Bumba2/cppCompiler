package parserDatatypes;

import helpers.Vector;
import parser.Node;

public class FunctionArguments<T> {
	// Vektor von Struct-Knoten. C: Müssen NODE_TYPE_VARIABLE sein.
	private Vector<Node<T>> vector;
	// Wie viel müssen wir zum Base-Pointer hinzuaddieren, um das erste Argument zu finden?
	private int stackAddition;
	
	public FunctionArguments() {
		this.setVector(null);
		this.setStackAddition(0);
	}
	
	public FunctionArguments(Vector<Node<T>> vector) {
		this.vector = vector;
		this.stackAddition = 0;
	}

	public Vector<Node<T>> getVector() {
		return vector;
	}

	public void setVector(Vector<Node<T>> vector) {
		this.vector = vector;
	}

	public int getStackAddition() {
		return stackAddition;
	}

	public void setStackAddition(int stackAddition) {
		this.stackAddition = stackAddition;
	}
}
