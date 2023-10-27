package parserDatatypes;

import helpers.Vector;
import parser.Node;

public class GenericPlaceholders<T> {
	private Vector<T> genericIdentifierNodeVector;
	
	public GenericPlaceholders(Vector<T> genericIdentifierNodeVector) {
		this.genericIdentifierNodeVector = genericIdentifierNodeVector;
	}

	public Vector<T> getGenericIdentifierNodeVector() {
		return genericIdentifierNodeVector;
	}

	public void setGenericIdentifierNodeVector(Vector<T> genericIdentifierNodeVector) {
		this.genericIdentifierNodeVector = genericIdentifierNodeVector;
	}
}
