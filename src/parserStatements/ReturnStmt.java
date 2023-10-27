package parserStatements;

import parser.Node;

public class ReturnStmt<T> {
	// Die Expression, die zurückgegeben wird.
	private Node<T> exp;
	
	public ReturnStmt(Node<T> exp) {
		this.setExp(exp);
	}

	public Node<T> getExp() {
		return exp;
	}

	public void setExp(Node<T> exp) {
		this.exp = exp;
	}
}
