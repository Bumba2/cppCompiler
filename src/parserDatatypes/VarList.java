package parserDatatypes;

import helpers.Vector;
import parser.Node;

public class VarList<T> {
	// Eine Liste von Knoten, die Variablen enthalten
	private Vector<Node<T>> list;
	
	public VarList(Vector<Node<T>> list) {
		this.setList(list);
	}

	public Vector<Node<T>> getList() {
		return list;
	}

	public void setList(Vector<Node<T>> list) {
		this.list = list;
	}
}
