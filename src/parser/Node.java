package parser;

import lexer.Position;

public class Node<T> {
	private NodeType type;
	private int flags;
	private Position pos;
	private NodeBinded<T> binded;
	// Falls der Knoten einen Literal-Wert besitzt, wie NODE_TYPE_NUMBER, NODE_TYPE_STRING und NODE_TYPE_IDENTIFIER.
	private T value;
	
	public Node(NodeType type) {
		this.type = type;
		this.flags = 0;
		this.pos = null;
		this.binded = null;
		this.value = null;
	}
	
	public Node(NodeType type, T value) {
		this.type = type;
		this.flags = 0;
		this.pos = null;
		this.binded = null;
		this.value = value;
	}
	
	public Node(NodeType type, T value, int flags) {
		this.type = type;
		this.flags = flags;
		this.pos = null;
		this.binded = null;
		this.value = value;
	}
	
	public Node(NodeType type, int flags, Position pos, NodeBinded<T> binded, T value) {
		this.type = type;
		this.flags = flags;
		this.pos = pos;
		this.binded = binded;
		this.value = value;
	}
	
	public int getFlags() {
		return flags;
	}
	public void setFlags(int flags) {
		this.flags = flags;
	}
	public NodeType getType() {
		return type;
	}
	public void setType(NodeType type) {
		this.type = type;
	}
	public Position getPos() {
		return pos;
	}
	public void setPos(Position pos) {
		this.pos = pos;
	}
	public NodeBinded<T> getBinded() {
		return binded;
	}
	public void setBinded(NodeBinded<T> binded) {
		this.binded = binded;
	}
	public T getValue() {
		return value;
	}
	public void setValue(T value) {
		this.value = value;
	}
}
