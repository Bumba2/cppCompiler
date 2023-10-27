package parserDatatypes;

import helpers.Vector;
import parser.Node;
import parser.Scope;

public class Var<T> {
	private Datatype<Node<T>> type; // Datentyp der Variable
	// Access Specifier einer Variable (Standard ist "private").
	private AccessSpecifierFlag accessSpecifier;
	private int padding;
	private int aoffset; // aligned-offset -> angepasstes Offset. Hier kann die Variable im Speicher gefunden werden.
	private String name; // Identifier der Variable
	private Node<T> val; // Referenz zum Value-Node der Variable
	// In welchen Scopes existieren Friend-Deklarationen der Variable?
	private Vector<Scope<T>> friendlyScopes;
	private String genericPlaceholder;
	
	public Var(Datatype<Node<T>> type, String name, Node<T> val, AccessSpecifierFlag accessSpecifier) {
		this.setType(type);
		this.setAccessSpecifier(accessSpecifier);
		this.setPadding(0);
		this.setAoffset(0);
		this.setName(name);
		this.setVal(val);
		this.setFriendlyScopes(Vector.vectorCreate());
	}

	public Datatype<Node<T>> getType() {
		return type;
	}

	public void setType(Datatype<Node<T>> type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Node<T> getVal() {
		return val;
	}

	public void setVal(Node<T> val) {
		this.val = val;
	}

	public int getPadding() {
		return padding;
	}

	public void setPadding(int padding) {
		this.padding = padding;
	}

	public int getAoffset() {
		return aoffset;
	}

	public void setAoffset(int aoffset) {
		this.aoffset = aoffset;
	}

	public AccessSpecifierFlag getAccessSpecifier() {
		return accessSpecifier;
	}

	public void setAccessSpecifier(AccessSpecifierFlag accessSpecifier) {
		this.accessSpecifier = accessSpecifier;
	}

	public Vector<Scope<T>> getFriendlyScopes() {
		return friendlyScopes;
	}

	public void setFriendlyScopes(Vector<Scope<T>> friendlyScopes) {
		this.friendlyScopes = friendlyScopes;
	}

	public String getGenericPlaceholder() {
		return genericPlaceholder;
	}

	public void setGenericPlaceholder(String genericPlaceholder) {
		this.genericPlaceholder = genericPlaceholder;
	}
}
