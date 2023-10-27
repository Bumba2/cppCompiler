package parser;

public class LibraryDirective <T>{
	private Node<T> nameNode;
	
	public LibraryDirective(Node<T> nameNode) {
		this.nameNode = nameNode;
	}

	public Node<T> getNameNode() {
		return nameNode;
	}

	public void setName(Node<T> nameNode) {
		this.nameNode = nameNode;
	}
}
