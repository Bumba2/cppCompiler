package parserStatements;

import helpers.Vector;
import parser.Node;

/* try { 
 * 	BODY 
 * } catch (EXP);
 * */

public class TryCatchStmt<T> {
	private Node<T> body;
	private Vector<Node<T>> catchExpressions; 
	
	public TryCatchStmt(Node<T> body, Vector<Node<T>> catchExpressions) {
		this.body = body;
		this.catchExpressions = catchExpressions;
	}
	
	public Node<T> getBody() {
		return body;
	}
	
	public void setBody(Node<T> body) {
		this.body = body;
	}
	
	public Vector<Node<T>> getCatchExpressions() {
		return this.catchExpressions;
	}
	
	public void setCatchExp(Vector<Node<T>> catchExpressions) {
		this.catchExpressions = catchExpressions;
	}
}
