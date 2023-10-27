package lexer;

public class Token<T> {
	public static int PRIMITIVE_TYPES_TOTAL = 7;
	public static String[] primitiveTypes = {"void", "char", "short", "int", "long", "float", "double"};
	
	private TokenType type;
	private int flags;
	private T value;
	private TokenNumberType numberType;
	// True wenn ein Whitespace zwischen diesem und dem nächsten Token ist
	// Z. B. * a für einen Operatortoken * würde heißen dass whitespace true sein 
	//würde für a, bei *a wäre es false
	private int whitespaces;
	// Zeigt, wo der Anfang eines Ausdrucks in Klammern ist
	// Z. B. (Hello World), alle betweenBrackets-Variablen der Tokens in Klammern würden
	// zum Start des Ausdrucks zeigen (hier das "H")
	private String betweenBrackets;
	private Position pos;
	
	public Token() {}
	
	public Token(TokenType type, int flags, T value, int whitespaces, TokenNumberType numberType) {
		this.setType(type);
		this.setFlags(flags);
		this.value = value;
		this.setWhitespaces(whitespaces);
	}
	
	public Token(TokenType type, int flags, T value, int whitespaces) {
		this.setType(type);
		this.setFlags(flags);
		this.value = value;
		this.setWhitespaces(whitespaces);
	}
	
	public Token(TokenType type, T value, TokenNumberType numberType) {
		this.setType(type);
		this.value = value;
		this.whitespaces = 0;
		this.numberType = numberType;
	}
	
	public Token(TokenType type, T value) {
		this.setType(type);
		this.value = value;
		this.whitespaces = 0;
	}

	public TokenType getType() {
		return type;
	}

	public void setType(TokenType type) {
		this.type = type;
	}

	public int getFlags() {
		return flags;
	}

	public void setFlags(int flags) {
		this.flags = flags;
	}
	
	public T getValue() {
		return this.value;
	}
	
	public void setValue(T value) {
		this.value = value;
	}
	
	public TokenNumberType getNumberType() {
		return this.numberType;
	}
	
	public void setNumberType(TokenNumberType numberType) {
		this.numberType = numberType;
	}

	public String getBetweenBrackets() {
		return betweenBrackets;
	}

	public void setBetweenBrackets(String betweenBrackets) {
		this.betweenBrackets = betweenBrackets;
	}

	public Position getPos() {
		return pos;
	}

	public void setPos(Position pos) {
		this.pos = pos;
	}

	public int getWhitespaces() {
		return this.whitespaces;
	}

	public void setWhitespaces(int whitespaces) {
		this.whitespaces = whitespaces;
	}
	
	// Erstellt eine Shallow-Copy des aufrufenden Objekts und gibt sie zurück.
	public Token<T> memcpy() {
		Token<T> token = new Token<T>(this.type, this.flags, this.value, this.whitespaces, this.numberType);
		return token;
	}
	
	// Kopiert die Werte des aufrufenden Tokens in das übergebene Token
	public void copyTokenValues(Token<T> token) {
		token.setType(this.type);
		token.setFlags(this.flags);
		token.setValue(this.value);
		token.setWhitespaces(this.whitespaces);
		token.setNumberType(this.numberType);
		token.setPos(this.pos);
		token.setBetweenBrackets(this.betweenBrackets);
	}
	
	@Override
	public String toString() {
		return this.getValue().toString();
	}
	
	public static boolean tokenIsIdentifier(Token<Object> token) {
		return token != null && token.getType() == TokenType.TOKEN_TYPE_IDENTIFIER;
	}
	
	public static boolean tokenIsKeyword(Token<Object> token, String value) {
		if (token == null) return false;
		return token.getType() == TokenType.TOKEN_TYPE_KEYWORD && SymbolTable.S_EQ((String) token.getValue(), value);
	}
	
	public static boolean tokenIsSymbol(Token<Object> token, char c) {
		if (token == null) return false;
		if (token.getType() == TokenType.TOKEN_TYPE_SYMBOL) {
			if ((Character) token.getValue() == c) {
				return true;
			}
		}
		return false;
	}
	
	// übergebener Token ist ein Operatortoken und der Operator ist gleich zum übergebenen Operator.
	public static boolean tokenIsOperator(Token<Object> token, String val) {
		if (token == null) return false;
		return token.getType() == TokenType.TOKEN_TYPE_OPERATOR && SymbolTable.S_EQ((String) token.getValue(), val);
	}
	
	public static boolean tokenIsNlOrCommentOrNewlineSeperator(Token<Object> token) {
		if (token == null) return false;
		return token.getType() == TokenType.TOKEN_TYPE_NEWLINE || token.getType() == TokenType.TOKEN_TYPE_COMMENT || Token.tokenIsSymbol(token, '\\');
	}
	
	// Sagt uns ob ein Keyword zu den primitiven Datentypen gehört (siehe primitiveTypes Array oben).
	public static boolean tokenIsPrimitiveKeyword(Token<Object> token) {
		if (token == null) return false;
		if (token.getType() != TokenType.TOKEN_TYPE_KEYWORD) {
			return false;
		}
		for(int i = 0; i < Token.PRIMITIVE_TYPES_TOTAL; i++) {
			if (SymbolTable.S_EQ(Token.primitiveTypes[i], (String) token.getValue())) {
				return true;
			}
		}
		return false;
	}
}
