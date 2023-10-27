package parserStatements;

import parser.Node;

public class ThrowStmt<T> {
	Node<T> expNode;
	
	public ThrowStmt(Node<T> expNode) {
		this.expNode = expNode;
	}
	
	public Node<T> getExpNode() {
		return this.expNode;
	}
	
	public void setExpNode(Node<T> expNode) {
		this.expNode = expNode;
	}
}
