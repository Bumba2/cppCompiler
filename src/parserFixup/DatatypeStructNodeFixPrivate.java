package parserFixup;

import parser.Node;

public class DatatypeStructNodeFixPrivate<T> {
	// Struct-Knoten, der repariert werden soll -> Struct ist NULL, da der Datentyp im Code
	// zur Deklaration genutzt wird, bevor das Struct definiert ist.
	private Node<T> node;

	public DatatypeStructNodeFixPrivate(Node<T> node) {
		this.node = node;
	}
	
	public Node<T> getNode() {
		return node;
	}

	public void setNode(Node<T> node) {
		this.node = node;
	}
}
