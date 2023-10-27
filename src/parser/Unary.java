package parser;

public class Unary<T> {
	private int flags;
	private String op;
	private Node<T> operand;
	private Indirection indirection;
	
	public Unary(String op, Node<T> operand, int flags) {
		this.flags = flags;
		this.op = op;
		this.operand = operand;
		this.indirection = new Indirection(); // Indirection heißt so viel wie Dereferenzierung.
	}
	
	public Unary(int flags, String op, Node<T> operand, Indirection indirection) {
		this.setFlags(flags);
		this.setOp(op);
		this.setOperand(operand);
		this.setIndirection(indirection);
	}

	public int getFlags() {
		return flags;
	}

	public void setFlags(int flags) {
		this.flags = flags;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public Node<T> getOperand() {
		return operand;
	}

	public void setOperand(Node<T> operand) {
		this.operand = operand;
	}

	public Indirection getIndirection() {
		return indirection;
	}

	public void setIndirection(Indirection indirection) {
		this.indirection = indirection;
	}
}
