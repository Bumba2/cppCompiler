package parserDatatypes;

import parser.Node;

public class Union<T> {
	private String name;
	private Node<T> bodyN; // Body der Union, gespeichert als Knoten.
	/**
	 * union abc {
	 * 
	 * } var_name;
	 * 
	 * In diesem Beispiel wird eine Union abc deklariert und gleichzeitig eine Variable mit Namen
	 * var_name initialisiert. Daher brauchen wir eine Referenz zum Variablenknoten.
	 * var ist NULL, wenn keine Initialisierung stattfindet.
	 */
	private Node<T> var;
	
	public Union(String name, Node<T> bodyN) {
		this.name = name;
		this.bodyN = bodyN;
		this.var = null;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Node<T> getBodyN() {
		return bodyN;
	}
	public void setBodyN(Node<T> bodyN) {
		this.bodyN = bodyN;
	}
	public Node<T> getVar() {
		return var;
	}
	public void setVar(Node<T> var) {
		this.var = var;
	}
}
