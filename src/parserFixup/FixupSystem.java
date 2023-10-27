package parserFixup;

import helpers.Vector;

public class FixupSystem<T> {
	private Vector<T> fixups; // Ein Vektor von Fixups.
	
	public FixupSystem() {}

	public Vector<T> getFixups() {
		return fixups;
	}

	public void setFixups(Vector<T> fixups) {
		this.fixups = fixups;
	}
}
