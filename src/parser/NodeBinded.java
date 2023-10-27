package parser;

public class NodeBinded<T> {
	// Referenz zum Body Node / Besitzer (gebundener Knoten ist innerhalb des Owner-Knotens).
	private Node<T> ownerBody;
	// Referenz zur Entität (Funktion, Klasse oder Struktur), in der der Knoten liegt.
	private Node<T> entity;
	
	public NodeBinded() {
		this.ownerBody = null;
		this.entity = null;
	}
	
	public NodeBinded(Node<T> ownerBody, Node<T> function) {
		this.ownerBody = ownerBody;
		this.entity = function;
	}
	
	public Node<T> getOwnerBody() {
		return ownerBody;
	}
	public void setOwnerBody(Node<T> owner) {
		this.ownerBody = owner;
	}
	public Node<T> getEntity() {
		return entity;
	}
	public void setEntity (Node<T> entity) {
		this.entity = entity;
	}
	
}
