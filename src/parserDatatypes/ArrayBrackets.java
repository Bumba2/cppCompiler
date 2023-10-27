package parserDatatypes;

import helpers.Vector;

public class ArrayBrackets<T> {
	// Enthält einen Vektor von n Arrayklammern (als Knoten abgespeichert).
	// Beispielsweise bei "int a[20][50]" hätte nBrackets eine Größe von 2.
	private Vector<T> nBrackets;
	
	public ArrayBrackets() {
		this.setNBrackets(null);
	}

	public Vector<T> getNBrackets() {
		return nBrackets;
	}

	public void setNBrackets(Vector<T> nBrackets) {
		this.nBrackets = nBrackets;
	}
}
