package parser;

public class Expression<T> {
	private Node<T> left;
	private Node<T> right;
	private String op;
	
	public Node<T> getLeft() {
		return left;
	}
	public void setLeft(Node<T> left) {
		this.left = left;
	}
	public Node<T> getRight() {
		return right;
	}
	public void setRight(Node<T> right) {
		this.right = right;
	}
	public String getOp() {
		return op;
	}
	public void setOp(String op) {
		this.op = op;
	}
	
	public Expression(Node<T> left, Node<T> right, String op) {
		this.left = left;
		this.right = right;
		this.op = op;
	}
}
