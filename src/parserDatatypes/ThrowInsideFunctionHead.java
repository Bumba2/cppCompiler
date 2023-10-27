package parserDatatypes;

import helpers.Vector;
import parser.Node;

public class ThrowInsideFunctionHead<T> {
	private Vector<Datatype<Node<T>>> exceptionClassesToThrowVector;
	
	public ThrowInsideFunctionHead (Vector<Datatype<Node<T>>> exceptionClassesToThrowVector) {
		this.exceptionClassesToThrowVector = exceptionClassesToThrowVector;
	}
	
	public Vector<Datatype<Node<T>>> getExceptionClassesToThrowVector() {
		return exceptionClassesToThrowVector;
	}
	
	public void setExceptionClassesToThrowVector(Vector<Datatype<Node<T>>> exceptionClassesToThrowVector) {
		this.exceptionClassesToThrowVector = exceptionClassesToThrowVector;
	}
}
