package parser;

import parserDatatypes.Datatype;

// Beispiel: "(int) 56;". dtype -> int, operand -> 56.
public class Cast<T> {
	private Datatype<Node<T>> dtype;
	private Node<T> operand;
	
	public Cast(Datatype<Node<T>> dtype, Node<T> operand) {
		this.dtype = dtype;
		this.operand = operand;
	}
	
	public Datatype<Node<T>> getDtype() {
		return dtype;
	}
	public void setDtype(Datatype<Node<T>> dtype) {
		this.dtype = dtype;
	}
	public Node<T> getOperand() {
		return operand;
	}
	public void setOperand(Node<T> operand) {
		this.operand = operand;
	}
}
