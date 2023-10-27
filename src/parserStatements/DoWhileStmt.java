package parserStatements;

import parser.Node;

/*
 * do {
 * 	BODY
 * } while (COND);
 */

public class DoWhileStmt<T> {
	private Node<T> expNode; // Expression ist die Bedingung (COND).
	private Node<T> bodyNode; // Body ist der Körper (BODY).
	
	public DoWhileStmt(Node<T> bodyNode, Node<T> expNode) {
		this.setBodyNode(bodyNode);
		this.setExpNode(expNode);
	}

	public Node<T> getExpNode() {
		return expNode;
	}

	public void setExpNode(Node<T> expNode) {
		this.expNode = expNode;
	}

	public Node<T> getBodyNode() {
		return bodyNode;
	}

	public void setBodyNode(Node<T> bodyNode) {
		this.bodyNode = bodyNode;
	}
}
