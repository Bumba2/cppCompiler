package parserStatements;

import parser.Node;

public class DefaultStmt<T> {
	private Node<T> body;
	
	public DefaultStmt(Node<T> body) {
		this.body = body;
	}

	public Node<T> getBody() {
		return body;
	}

	public void setBody(Node<T> body) {
		this.body = body;
	}
}
