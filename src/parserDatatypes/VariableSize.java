package parserDatatypes;

// Hält die Variablengröße aller Variablen eines Körpers
public class VariableSize {
	private int variableSize;
	
	public VariableSize() {
		this.variableSize = 0;
	}
	
	public VariableSize(int variableSize) {
		this.variableSize = variableSize;
	}

	public int getVariableSize() {
		return variableSize;
	}

	public void setVariableSize(int variableSize) {
		this.variableSize = variableSize;
	};
	
	
}
