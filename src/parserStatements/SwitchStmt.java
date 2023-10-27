package parserStatements;

import helpers.Vector;
import parser.Node;

/*
 * switch (EXP) {
 * 	case (EXP):
 * 	case (EXP):
 * 	case (EXP):
 * }
 */

public class SwitchStmt<T> {
	private Node<T> exp; // Expression des switch-Statements (vor dem Körper).
	private Node<T> body; // Körperknoten.
	private Vector<Node<T>> cases; // Vektor von Fällen (Cases).
	private Node<T> defaultCaseNode; // Gibt es einen Default-Fall? Funktioniert wie ein else bei einem If-Stmt.
	
	public SwitchStmt(Node<T> exp, Node<T> body, Vector<Node<T>> cases, Node<T> defaultCaseNode) {
		this.exp = exp;
		this.body = body;
		this.cases = cases;
		this.defaultCaseNode = defaultCaseNode;
	}
	
	public Node<T> getExp() {
		return exp;
	}
	
	public void setExp(Node<T> exp) {
		this.exp = exp;
	}
	
	public Node<T> getBody() {
		return body;
	}
	
	public void setBody(Node<T> body) {
		this.body = body;
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
