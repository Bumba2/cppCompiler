package parserDatatypes;

import parser.Node;

// Klasse Hält Klammern für Arrays.
public class Bracket<T> {
	// Beispiel: "int x[50];" => [50] wäre der Wert unseres Klammerknotens.
	// Der Knoten inner wäre NODE_TYPE_NUMBER mit dem Wert 50.
	private Node<T> inner;
	
	public Bracket(Node<T> inner) {
		this.setInner(inner);
	}

	public Node<T> getInner() {
		return inner;
	}

	public void setInner(Node<T> inner) {
		this.inner = inner;
	}
}
