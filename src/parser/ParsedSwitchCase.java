package parser;

public class ParsedSwitchCase {
	private int index; // Index des geparseden Switch-Statements.
	
	public ParsedSwitchCase() {}
	
	public ParsedSwitchCase(int index) {
		this.setIndex(index);
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
}
