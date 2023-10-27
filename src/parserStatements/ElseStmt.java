package parserStatements;

import parser.Node;

public class ElseStmt<T> {
	private Node<T> bodyNode;
	
	public ElseStmt(Node<T> bodyNode) {
		this.setBodyNode(bodyNode);
	}

	public Node<T> getBodyNode() {
		return bodyNode;
	}

	public void setBodyNode(Node<T> bodyNode) {
		this.bodyNode = bodyNode;
	}
}
