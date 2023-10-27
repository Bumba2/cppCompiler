package parserStatements;

import parser.Node;

public class GotoStmt<T> {
	private Node<T> label;
	
	public GotoStmt(Node<T> label) {
		this.label = label;
	}

	public Node<T> getLabel() {
		return label;
	}

	public void setLabel(Node<T> label) {
		this.label = label;
	}
}
