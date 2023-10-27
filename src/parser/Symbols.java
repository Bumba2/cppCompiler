package parser;

import helpers.Vector;

// Klasse, die alle Symboltabellen eines Kompilierungsprozesses verwaltet.
public class Symbols<T> {
	// Alle aktuell aktiven Symbole sind im Vektor Table gespeichert. Es hält Referenzen auf Symbole.
	private Vector<T> table;
	
	public Symbols() {
		this.setTable(null);
	}

	public Vector<T> getTable() {
		return table;
	}

	public void setTable(Vector<T> table) {
		this.table = table;
	}
}
