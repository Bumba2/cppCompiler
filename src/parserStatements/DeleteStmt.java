package parserStatements;

import parser.Node;

public class DeleteStmt<T> {
	private Node<T> expNode;
	private boolean isArray;
	
	public DeleteStmt(Node<T> expNode, boolean isArray) {
		this.expNode = expNode;
		this.isArray = isArray;
	}
	
	public Node<T> getExpNode() {
		return expNode;
	}
	
	public void setExpNode(Node<T> expNode) {
		this.expNode = expNode;
	}
	
	public boolean isArray() {
		return isArray;
	}
	
	public void setArray(boolean isArray) {
		this.isArray = isArray;
	}
}
