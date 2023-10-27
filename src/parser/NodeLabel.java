package parser;

public class NodeLabel<T> {
	private Node<T> name; // Enthält den Bezeichnerknoten, der den Namen des Labels repräsentiert.
	
	public NodeLabel(Node<T> name) {
		this.setName(name);
	}

	public Node<T> getName() {
		return name;
	}

	public void setName(Node<T> name) {
		this.name = name;
	}
}
