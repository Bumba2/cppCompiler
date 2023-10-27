package cleanCodeAnalyzer;

public class IndentationAnalysis {
	private int currentIndentationDepth;
	private int highestIndentationDepth;
	private int additionalNumberOfWhitespacesPerIndentation ;
	private int numberOfWhitespacesForIndentationInPriorLine;
	private int numberOfWhitespacesForIndentationInCurrentLine;
	private boolean consistentIndentation;
	
	public IndentationAnalysis() {
		setCurrentIndentationDepth(0);
		setHighestIndentationDepth(0);
		setAdditionalNumberOfWhitespacesPerIndentation(-1);
		setNumberOfWhitespacesForIndentationInPriorLine(0);
		setNumberOfWhitespacesForIndentationInCurrentLine(0);
		setConsistentIndentation(true);
	}

	public int getCurrentIndentationDepth() {
		return currentIndentationDepth;
	}

	public void setCurrentIndentationDepth(int currentIndentationDepth) {
		this.currentIndentationDepth = currentIndentationDepth;
	}

	public int getHighestIndentationDepth() {
		return highestIndentationDepth;
	}

	public void setHighestIndentationDepth(int highestIndentationDepth) {
		this.highestIndentationDepth = highestIndentationDepth;
	}

	public int getAdditionalNumberOfWhitespacesPerIndentation() {
		return additionalNumberOfWhitespacesPerIndentation;
	}

	public void setAdditionalNumberOfWhitespacesPerIndentation(int additionalNumberOfWhitespacesPerIndentation) {
		this.additionalNumberOfWhitespacesPerIndentation = additionalNumberOfWhitespacesPerIndentation;
	}

	public int getNumberOfWhitespacesForIndentationInPriorLine() {
		return numberOfWhitespacesForIndentationInPriorLine;
	}

	public void setNumberOfWhitespacesForIndentationInPriorLine(int numberOfWhitespacesForIndentationInPriorLine) {
		this.numberOfWhitespacesForIndentationInPriorLine = numberOfWhitespacesForIndentationInPriorLine;
	}

	public int getNumberOfWhitespacesForIndentationInCurrentLine() {
		return numberOfWhitespacesForIndentationInCurrentLine;
	}

	public void setNumberOfWhitespacesForIndentationInCurrentLine(int numberOfWhitespacesForIndentationInCurrentLine) {
		this.numberOfWhitespacesForIndentationInCurrentLine = numberOfWhitespacesForIndentationInCurrentLine;
	}

	public boolean getConsistentIndentation() {
		return consistentIndentation;
	}

	public void setConsistentIndentation(boolean consistentIndentation) {
		this.consistentIndentation = consistentIndentation;
	}
}
