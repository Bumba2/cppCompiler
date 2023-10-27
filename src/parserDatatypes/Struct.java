package parserDatatypes;

import helpers.Vector;
import parser.Node;
import parser.NodeType;

public class Struct<T> {
	private String name;
	// Access Specifier einer Struktur oder Klasse (Standard ist "private").
	private AccessSpecifierFlag accessSpecifier;
	private Node<T> bodyN; // Body des Structs, gespeichert als Knoten.
	/**
	 * struct abc {
	 * 
	 * } var_name;
	 * 
	 * In diesem Beispiel wird ein struct abc deklariert und gleichzeitig eine Variable mit Namen
	 * var_name initialisiert. Daher brauchen wir eine Referenz zum Variablenknoten.
	 * var ist NULL, wenn keine Initialisierung stattfindet.
	 */
	private boolean classDefinition; // Ist es ein Struct oder eine Klasse?
	private Node<T> var;
	// Diese Access-Specifier-Flagge wird standardmäßig auf "private" gesetzt. Beispielsweise 
	// durch ein "public:" wird sie auf public gesetzt, um Struktur- bzw. Klassenvariablen und
	// Funktionen mit dem Access-Specifier zu deklarieren.
	private Node<T> parentClassNode; // Ist eine Elternklasse für die Klasse angegeben (falls es kein Struct ist)?
	private Datatype<Node<T>> dtype;
	// Welche generischen Datentypbezeichner wurden für das Struct bzw. die Klasse angegeben?
	
	public Struct() {}
	
	public Struct(String name, Node<T> bodyN, AccessSpecifierFlag accessSpecifier, boolean classDefinition, Node<T> parentClassNode) {
		this.name = name;
		this.bodyN = bodyN;
		this.accessSpecifier = accessSpecifier;
		this.var = null;
		this.classDefinition = classDefinition;
		this.parentClassNode = parentClassNode;
		this.dtype = null;
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

	public AccessSpecifierFlag getAccessSpecifier() {
		return accessSpecifier;
	}

	public void setAccessSpecifier(AccessSpecifierFlag accessSpecifier) {
		this.accessSpecifier = accessSpecifier;
	}

	public boolean getClassDefinition() {
		return classDefinition;
	}

	public void setClassDefinition(boolean classDefinition) {
		this.classDefinition = classDefinition;
	}

	public Node<T> getParentClassNode() {
		return parentClassNode;
	}

	public void setParentClassNode(Node<T> parentClassNode) {
		this.parentClassNode = parentClassNode;
	}
	
	public void cloneFromStructNode(Node<T> structNode) {
		if (structNode.getType() == NodeType.NODE_TYPE_STRUCT) {
			@SuppressWarnings("unchecked")
			Struct<T> struct = (Struct<T>) structNode.getValue();
			this.accessSpecifier = struct.accessSpecifier;
			this.bodyN = struct.bodyN;
			this.classDefinition = struct.classDefinition;
			this.name = struct.name;
			this.parentClassNode = struct.parentClassNode;
			this.var = struct.var;
			this.dtype = struct.dtype;
		}
	}

	public Datatype<Node<T>> getDtype() {
		return this.dtype;
	}

	public void setDtype(Datatype<Node<T>> dtype) {
		this.dtype = dtype;
	}
}
