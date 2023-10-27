package parserStatements;

import parser.Node;

public class CatchStmt<T> {
	private Node<T> exp;
	private Node<T> body;
	
	public CatchStmt(Node<T> exp, Node<T> body) {
		this.exp = exp;
		this.setBody(body);
	}

	public Node<T> getExp() {
		return exp;
	}

	public void setExp(Node<T> exp, Node<T> body) {
		this.exp = exp;
	}

	public Node<T> getBody() {
		return body;
	}

	public void setBody(Node<T> body) {
		this.body = body;
	}
}
