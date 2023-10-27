package lexer;

public class Position {
	private int lineNumber;
	private int colNumber;
	private String filename;
	
	public Position(int lineNumber, int colNumber, String filename) {
		this.lineNumber = lineNumber;
		this.colNumber = colNumber;
		this.filename = filename;
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}

	public int getColNumber() {
		return colNumber;
	}

	public void setColNumber(int colNumber) {
		this.colNumber = colNumber;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
}
