package parser;

public class Parenthesis<T> {
	private Node<T> exp; // Knoten, der die Expression innerhalb der Klammern enthält.
	
	public Parenthesis(Node<T> exp) {
		this.setExp(exp);
	}

	public Node<T> getExp() {
		return exp;
	}

	public void setExp(Node<T> exp) {
		this.exp = exp;
	}
}
