package parserStatements;

import parser.Node;

public class ForEachStmt<T> {
	private Node<T> initNode;
	private Node<T> loopNode;
	private Node<T> bodyNode;
	
	public ForEachStmt(Node<T> initNode, Node<T> loopNode, Node<T> bodyNode) {
		this.initNode = initNode;
		this.loopNode = loopNode;
		this.setBodyNode(bodyNode);
	}
	
	public Node<T> getInitNode() {
		return initNode;
	}
	
	public void setExpNode(Node<T> initNode) {
		this.initNode = initNode;
	}
	
	public Node<T> getLoopNode() {
		return this.loopNode;
	}
	
	public void setLoopNode(Node<T> loopNode) {
		this.loopNode = loopNode;
	}

	public Node<T> getBodyNode() {
		return bodyNode;
	}

	public void setBodyNode(Node<T> bodyNode) {
		this.bodyNode = bodyNode;
	}
}
