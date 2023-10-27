package parserStatements;

import parser.Node;

// Ein Case besteht lediglich aus einer Expression (bzw. einer Bedingung).
public class CaseStmt<T> {
	private Node<T> exp;
	private Node<T> body;
	
	public CaseStmt(Node<T> exp, Node<T> body) {
		this.setExp(exp);
		this.setBody(body);
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
}
