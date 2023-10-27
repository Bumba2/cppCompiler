package cleanCodeAnalyzer;

import parserDatatypes.FunctionFlag;

public class FunctionCall {
	private String identifier;
	private int numberOfNullValues;
	private int numberOfArguments;
	private FunctionFlag functionFlag;
	
	public FunctionCall() {
		this.setIdentifier(null);
		this.numberOfNullValues = 0;
		this.setNumberOfArguments(0);
		this.functionFlag = null;
	}
	
	public FunctionCall(String identifier, FunctionFlag functionFlag) {
		this.identifier = identifier;
		this.numberOfNullValues = 0;
		this.numberOfArguments = 0;
		this.functionFlag = functionFlag;
	}

	public int getNumberOfNullValues() {
		return numberOfNullValues;
	}

	public void setNumberOfNullValues(int numberOfNullValues) {
		this.numberOfNullValues = numberOfNullValues;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public int getNumberOfArguments() {
		return numberOfArguments;
	}

	public void setNumberOfArguments(int numberOfArguments) {
		this.numberOfArguments = numberOfArguments;
	}

	public FunctionFlag getFunctionFlag() {
		return functionFlag;
	}

	public void setFunctionFlag(FunctionFlag functionFlag) {
		this.functionFlag = functionFlag;
	}
}
