package parser;

public class ParserScopeEntity<T> {
	// Entitätsflaggen der Scope-Entität.
	private int flags;
	// Der Stackoffset der Scope Entität (negativ für lokalen Scope, positiv für ein Funktionsargumentsscope).
	private int stackOffset;
	// Variablendeklaration
	private Node<T> node;
	
	public ParserScopeEntity(Node<T> node, int stackOffset, int flags) {
		this.node = node;
		this.stackOffset = stackOffset;
		this.flags = flags;
	}
	
	public int getFlags() {
		return flags;
	}
	public void setFlags(int flags) {
		this.flags = flags;
	}
	public int getStackOffset() {
		return stackOffset;
	}
	public void setStackOffset(int stackOffset) {
		this.stackOffset = stackOffset;
	}
	public Node<T> getNode() {
		return node;
	}
	public void setNode(Node<T> node) {
		this.node = node;
	}
}
