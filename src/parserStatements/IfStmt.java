package parserStatements;

import parser.Node;

public class IfStmt<T> {
	// If (COND) { body }.
	private Node<T> condNode; 
	private Node<T> bodyNode;
	// else or else if.
	private Node<T> next;
	
	public IfStmt(Node<T> condNode, Node<T> bodyNode, Node<T> next) {
		this.condNode = condNode;
		this.bodyNode = bodyNode;
		this.next = next;
	}
	
	public Node<T> getCondNode() {
		return condNode;
	}
	public void setCondNode(Node<T> condNode) {
		this.condNode = condNode;
	}
	public Node<T> getBodyNode() {
		return bodyNode;
	}
	public void setBodyNode(Node<T> bodyNode) {
		this.bodyNode = bodyNode;
	}
	public Node<T> getNext() {
		return next;
	}
	public void setNext(Node<T> next) {
		this.next = next;
	}
}
