package parserStatements;

import parser.Node;

public class ForStmt<T> {
	private Node<Object> initNode; // zum Beispiel "int i = 0;".
	private Node<Object> condNode; // zum Beispiel "i < strlen(str);".
	private Node<Object> loopNode; // zum Beispiel "i++;".
	private Node<Object> bodyNode; // Alles innerhalb von { }.
	
	public ForStmt(Node<Object> initNode, Node<Object> condNode, Node<Object> loopNode, Node<Object> bodyNode) {
		this.initNode = initNode;
		this.condNode = condNode;
		this.loopNode = loopNode;
		this.bodyNode = bodyNode;
	}
	
	public Node<Object> getInitNode() {
		return initNode;
	}
	public void setInitNode(Node<Object> initNode) {
		this.initNode = initNode;
	}
	public Node<Object> getCondNode() {
		return condNode;
	}
	public void setCondNode(Node<Object> condNode) {
		this.condNode = condNode;
	}
	public Node<Object> getLoopNode() {
		return loopNode;
	}
	public void setLoopNode(Node<Object> loopNode) {
		this.loopNode = loopNode;
	}
	public Node<Object> getBodyNode() {
		return bodyNode;
	}
	public void setBodyNode(Node<Object> bodyNode) {
		this.bodyNode = bodyNode;
	}
}
