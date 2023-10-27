package parser;

public class ExpressionableOpPrecedenceGroup {
	public Integer precedence;
	public String[] operators;
	private AssociativityType type;
	
	public ExpressionableOpPrecedenceGroup() {
		precedence = null;
	}
	
	public int getPrecedence() {
		return this.precedence;
	}
	
	public void setPrecedence(int precedence) {
		this.precedence = precedence;
	}
	
	public AssociativityType getType() {
		return this.type;
	}
	
	public void setType(AssociativityType type) {
		this.type = type;
	}
	
	public String[] getOperators() {
		return this.operators;
	}
	
	public void setOperators(String[] operators) {
		this.operators = operators;
	}
	
	/**
	 * Format: {operator1, operator2, operator3}
	 */
}
