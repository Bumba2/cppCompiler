package parserStatements;

import parser.Node;

public class WhileStmt<T> {
	// while (COND) { BODY }
	private Node<T> expNode; // While-Schleife hat eine Expression.
	private Node<T> bodyNode; // Und einen Körper.
	
	public WhileStmt(Node<T> expNode, Node<T> bodyNode) {
		this.setExpNode(expNode);
		this.setBodyNode(bodyNode);
	}

	public Node<T> getExpNode() {
		return expNode;
	}

	public void setExpNode(Node<T> expNode) {
		this.expNode = expNode;
	}

	public Node<T> getBodyNode() {
		return this.bodyNode;
	}

	public void setBodyNode(Node<T> bodyNode) {
		this.bodyNode = bodyNode;
	}
}
