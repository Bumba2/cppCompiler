package parser;

public class Symbol<T> {
	// Jedes Symbol braucht einen einzigartigen Namen.
	// Z. B. dürfen Funktionen- und Variablennamen nicht gleich heißen.
	private String name;
	private SymbolType type;
	private Node<T> data;
	Scope<T> scope;
	
	public Symbol() {
		this.name = null;
		this.type = null;
		this.data = null;
		this.scope = null;
	}
	
	public Symbol(String name, SymbolType type, Node<T> data, Scope<T> scope) {
		this.name = name;
		this.type = type;
		this.data = data;
		this.scope = scope;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public SymbolType getType() {
		return type;
	}
	public void setType(SymbolType type) {
		this.type = type;
	}
	public Node<T> getData() {
		return data;
	}
	public void setData(Node<T> data) {
		this.data = data;
	}
	public Scope<T> getScope() {
		return this.scope;
	}
	public void setScope(Scope<T> scope) {
		this.scope = scope;
	}
}
