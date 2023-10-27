package parser;

import helpers.Vector;

public class HistoryCases<T> {
	private Vector<Node<T>> cases; // Ein Vektor von geparseden switch-Fällen.
	private Node<T> defaultCaseNode; // Hat das Switch-Statement einen Default-Fall im Körper?
	
	public HistoryCases() {
		this.cases = null;
		this.defaultCaseNode = null;
	}
	
	public Vector<Node<T>> getCases() {
		return cases;
	}
	
	public void setCases(Vector<Node<T>> cases) {
		this.cases = cases;
	}
	
	public Node<T> getDefaultCaseNode() {
		return defaultCaseNode;
	}
	
	public void setDefaultCaseNode(Node<T> defaultCaseNode) {
		this.defaultCaseNode = defaultCaseNode;
	}
}
