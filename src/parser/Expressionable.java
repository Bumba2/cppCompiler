package parser;

// In diesem Parser werden die Prioritätsgruppen von C verwendet (im Unterschied dazu siehe C++: https://en.cppreference.com/w/c/language/operator_precedence).
// Die Klasse hält einen Array von Operator-Prioritätsgruppen, die sich an der Tabelle (https://en.cppreference.com/w/c/language/operator_precedence)
// orientiert. Sind Knoten in der falschen Reihenfolge angelegt, müssen sie getauscht werden, um den Assoziativitätsregeln zu genügen.
// Tabellen sind etwas anders definiert als in der Tabelle im Internet, da der Compiler nicht wie gcc funktioniert.
public class Expressionable {
	public static int TOTAL_OPERATORS_GROUPS = 14;
	private ExpressionableOpPrecedenceGroup[] opPrecedence;
	
	
	public Expressionable() {
		this.opPrecedence = new ExpressionableOpPrecedenceGroup[TOTAL_OPERATORS_GROUPS];
		for(int i = 0; i < Expressionable.TOTAL_OPERATORS_GROUPS; i++) {
			this.opPrecedence[i] = new ExpressionableOpPrecedenceGroup();
		}
		
		String[] operatorGroup1 = {"++", "--", "()", "[]", "(", "[", ".", "->"};
		this.opPrecedence[0].setPrecedence(1);
		this.opPrecedence[0].setOperators(operatorGroup1);
		this.opPrecedence[0].setType(AssociativityType.ASSOCIATIVITY_LEFT_TO_RIGHT);
		
		String[] operatorGroup2 = {"*", "/", "%"};
		this.opPrecedence[1].setPrecedence(2);
		this.opPrecedence[1].setOperators(operatorGroup2);
		this.opPrecedence[1].setType(AssociativityType.ASSOCIATIVITY_LEFT_TO_RIGHT);
			
		String[] operatorGroup3 = {"+", "-"};
		this.opPrecedence[2].setPrecedence(3);
		this.opPrecedence[2].setOperators(operatorGroup3);
		this.opPrecedence[2].setType(AssociativityType.ASSOCIATIVITY_LEFT_TO_RIGHT);
			
		String[] operatorGroup4 = {"<<", ">>"};
		this.opPrecedence[3].setPrecedence(4);
		this.opPrecedence[3].setOperators(operatorGroup4);
		this.opPrecedence[3].setType(AssociativityType.ASSOCIATIVITY_LEFT_TO_RIGHT);
			
		String[] operatorGroup5 = {"<", "<=", ">", ">="};
		this.opPrecedence[4].setPrecedence(5);
		this.opPrecedence[4].setOperators(operatorGroup5);
		this.opPrecedence[4].setType(AssociativityType.ASSOCIATIVITY_LEFT_TO_RIGHT);
			
		String[] operatorGroup6 = {"==", "!="};
		this.opPrecedence[5].setPrecedence(6);
		this.opPrecedence[5].setOperators(operatorGroup6);
		this.opPrecedence[5].setType(AssociativityType.ASSOCIATIVITY_LEFT_TO_RIGHT);
			
		String[] operatorGroup7 = {"&"};
		this.opPrecedence[6].setPrecedence(7);
		this.opPrecedence[6].setOperators(operatorGroup7);
		this.opPrecedence[6].setType(AssociativityType.ASSOCIATIVITY_LEFT_TO_RIGHT);
			
		String[] operatorGroup8 = {"^"};
		this.opPrecedence[7].setPrecedence(8);
		this.opPrecedence[7].setOperators(operatorGroup8);
		this.opPrecedence[7].setType(AssociativityType.ASSOCIATIVITY_LEFT_TO_RIGHT);
			
		String[] operatorGroup9 = {"|"};
		this.opPrecedence[8].setPrecedence(9);
		this.opPrecedence[8].setOperators(operatorGroup9);
		this.opPrecedence[8].setType(AssociativityType.ASSOCIATIVITY_LEFT_TO_RIGHT);
			
		String[] operatorGroup10 = {"&&"};
		this.opPrecedence[9].setPrecedence(10);
		this.opPrecedence[9].setOperators(operatorGroup10);
		this.opPrecedence[9].setType(AssociativityType.ASSOCIATIVITY_LEFT_TO_RIGHT);
			
		String[] operatorGroup11 = {"||"};
		this.opPrecedence[10].setPrecedence(11);
		this.opPrecedence[10].setOperators(operatorGroup11);
		this.opPrecedence[10].setType(AssociativityType.ASSOCIATIVITY_LEFT_TO_RIGHT);
			
		String[] operatorGroup12 = {"?", ":"};
		this.opPrecedence[11].setPrecedence(12);
		this.opPrecedence[11].setOperators(operatorGroup12);
		this.opPrecedence[11].setType(AssociativityType.ASSOCIATIVITY_RIGHT_TO_LEFT);
			
		String[] operatorGroup13 = {"=", "+=", "-=", "*=", "/=", "%=", ">>=", "<<=", "&=", "^=", "|="};
		this.opPrecedence[12].setPrecedence(13);
		this.opPrecedence[12].setOperators(operatorGroup13);
		this.opPrecedence[12].setType(AssociativityType.ASSOCIATIVITY_RIGHT_TO_LEFT);
			
		String[] operatorGroup14 = {","};
		this.opPrecedence[13].setPrecedence(14);
		this.opPrecedence[13].setOperators(operatorGroup14);
		this.opPrecedence[13].setType(AssociativityType.ASSOCIATIVITY_LEFT_TO_RIGHT);
	}
	
	public ExpressionableOpPrecedenceGroup[] getOpPrecedence() {
		return this.opPrecedence;
	}
	
}

