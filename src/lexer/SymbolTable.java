package lexer;

import parser.Expression;
import parser.Node;
import parser.NodeType;

public class SymbolTable {
	public static boolean isNumeric(char c) {
		if (c == '0' || c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6' || c == '7' || c == '8' || c == '9') {
			return true;
		}
		return false;
	}
	
	// Division wird ausgeklammert, da / auch einen Kommentar starten kann.
	public static boolean isOperatorExcludingDivision(char c) {
		if (c == '+' || c == '-' || c == '*' || c == '>' || c == '<' || c == '^' || c == '%' || c == '!' || c == '=' || c == '~' || c == '|' || c == '&' || c == '(' || c == '[' || c == ',' || c == '.' || c == '?') {
			return true;
		}
		return false;
	}
	
	public static boolean keywordIsDatatype(String str) {
		return SymbolTable.S_EQ(str, "void") || SymbolTable.S_EQ(str, "char") || SymbolTable.S_EQ(str, "int") || SymbolTable.S_EQ(str, "short") || SymbolTable.S_EQ(str, "float") || SymbolTable.S_EQ(str, "double") || SymbolTable.S_EQ(str, "long") || SymbolTable.S_EQ(str, "struct") || SymbolTable.S_EQ(str, "union") || SymbolTable.S_EQ(str, "class") || SymbolTable.S_EQ(str, "auto") || SymbolTable.S_EQ(str, "string") || SymbolTable.S_EQ(str, "bool");
	}
	
	public static boolean isKeyword(String str) {
		return SymbolTable.S_EQ(str, "unsigned") || SymbolTable.S_EQ(str, "signed") || SymbolTable.S_EQ(str, "char") || SymbolTable.S_EQ(str, "short") || SymbolTable.S_EQ(str, "int") || SymbolTable.S_EQ(str, "long") || SymbolTable.S_EQ(str, "float") || SymbolTable.S_EQ(str, "double") || SymbolTable.S_EQ(str, "void") || SymbolTable.S_EQ(str, "struct") || SymbolTable.S_EQ(str, "union") || SymbolTable.S_EQ(str, "static") || SymbolTable.S_EQ(str, "__ignore_typecheck") || SymbolTable.S_EQ(str, "return") || SymbolTable.S_EQ(str, "include") || SymbolTable.S_EQ(str, "sizeof") || SymbolTable.S_EQ(str, "if") || SymbolTable.S_EQ(str, "else") || SymbolTable.S_EQ(str, "while") || SymbolTable.S_EQ(str, "for") || SymbolTable.S_EQ(str, "do") || SymbolTable.S_EQ(str, "break") || SymbolTable.S_EQ(str, "continue") || SymbolTable.S_EQ(str, "switch") || SymbolTable.S_EQ(str, "case") || SymbolTable.S_EQ(str, "default") || SymbolTable.S_EQ(str, "goto") || SymbolTable.S_EQ(str, "typedef") || SymbolTable.S_EQ(str, "const") || SymbolTable.S_EQ(str, "define") || SymbolTable.S_EQ(str, "extern") || SymbolTable.S_EQ(str, "restrict") || SymbolTable.S_EQ(str, "auto") || SymbolTable.S_EQ(str, "catch") || SymbolTable.S_EQ(str, "class") || SymbolTable.S_EQ(str, "delete") || SymbolTable.S_EQ(str, "explicit") || SymbolTable.S_EQ(str, "friend") || SymbolTable.S_EQ(str, "mutable") || SymbolTable.S_EQ(str, "new") || SymbolTable.S_EQ(str, "operator") || SymbolTable.S_EQ(str, "private") || SymbolTable.S_EQ(str, "protected") || SymbolTable.S_EQ(str, "public") || SymbolTable.S_EQ(str, "register") || SymbolTable.S_EQ(str, "throw") || SymbolTable.S_EQ(str, "try") || SymbolTable.S_EQ(str, "virtual") || SymbolTable.S_EQ(str, "using") || SymbolTable.S_EQ(str, "namespace") || SymbolTable.S_EQ(str, "string") || SymbolTable.S_EQ(str, "inline") || SymbolTable.S_EQ(str, "bool") || SymbolTable.S_EQ(str, "generic") || SymbolTable.S_EQ(str, "typename");
	}
	
	public static boolean isEscaping(char c) {
		// https://en.wikipedia.org/wiki/Escape_sequences_in_C
		if (c == 'a' || c == 'b' || c == 'e' || c == 'f' || c == 'n' || c == 'r' || c == 't' || c == 'v' || c == '\\' || c == '\'' || c == '\"' || c == '?') {
			return true;
		}
		return false;
	}
	
	// String-Compare function die nützlich ist, um Operatoren zu finden, die aus mehreren Zeichen bestehen
	public static boolean S_EQ(String str, String str2) {
		return (str != null && str2 != null && str.equals(str2));
	}
	
	public static boolean isSymbolWithoutColon(char c) {
		if (c == '{' || c == '}' || c == ';' || c == '#' || c == '\\' || c == ')' || c == ']') {
			return true;
		}
		return false;
	}
	
	public static boolean isAlpha(char c) {
		if (c == 'a' || c == 'A' || c == 'b' || c == 'B' || c == 'c' || c == 'C' || c == 'd' || c == 'D' || c == 'e' || c == 'E' || c == 'f' || c == 'F' || c == 'g' || c == 'G' || c == 'h' || c == 'H' || c == 'i' || c == 'I' || c == 'j' || c == 'J' || c == 'k' || c == 'K' || c == 'l' || c == 'L' || c == 'm' || c == 'M' || c == 'n' || c == 'N' || c == 'o' || c == 'O' || c == 'p' || c == 'P' || c == 'q' || c == 'Q' || c == 'r' || c == 'R' || c == 's' || c == 'S' || c == 't' || c == 'T' || c == 'u' || c == 'U' || c == 'v' || c == 'V' || c == 'w' || c == 'W' || c == 'x' || c == 'X' || c == 'y' || c == 'Y' || c == 'z' || c == 'Z') {
			return true;
		}
		return false;
	}
	
	public static boolean isHexChar(char c) {
		c = Character.toLowerCase(c);
		return (c >= '0' && c <= '9') || (c >= 'a' && c <= 'f');
	}
	
	public static boolean keywordIsAccessSpecifier(String accessSpecifier) {
		return SymbolTable.S_EQ(accessSpecifier, "public") || SymbolTable.S_EQ(accessSpecifier, "protected") || (SymbolTable.S_EQ(accessSpecifier, "private")); 
	}
	
	public static boolean isAccessOperator(String op)
	{
	    return SymbolTable.S_EQ(op, "->") || SymbolTable.S_EQ(op, ".");
	}
	
	public static boolean isAccessNode(Node<Object> node) {
		if (node.getType() != NodeType.NODE_TYPE_EXPRESSION) {
			return false;
		}
		@SuppressWarnings("unchecked")
		Expression<Object> exp = (Expression<Object>) node.getValue();
		return SymbolTable.isAccessOperator(exp.getOp());
	}
	
	public static boolean isAccessNodeWithOp(Node<Object> node, String op) {
		if (node.getType() != NodeType.NODE_TYPE_EXPRESSION) {
			return false;
		}
		@SuppressWarnings("unchecked")
		Expression<Object> exp = (Expression<Object>) node.getValue();
		return SymbolTable.isAccessNode(node) && SymbolTable.S_EQ(exp.getOp(), op);
	}
	
	public static boolean isArrayOperator(String op)
	{
	    return SymbolTable.S_EQ(op, "[]");
	}
	
	public static boolean isArrayNode(Node<Object> node) {
		if (node.getType() != NodeType.NODE_TYPE_EXPRESSION) {
			return false;
		}
		@SuppressWarnings("unchecked")
		Expression<Object> exp = (Expression<Object>) node.getValue();
		return node.getType() == NodeType.NODE_TYPE_EXPRESSION && SymbolTable.isArrayOperator(exp.getOp());
	}
	
	public static boolean isParentheses(String op)
	{
	    return SymbolTable.S_EQ(op, "(");
	}
	
	public static boolean isParenthesesNode(Node<Object> node) {
		if (node.getType() != NodeType.NODE_TYPE_EXPRESSION) {
			return false;
		}
		@SuppressWarnings("unchecked")
		Expression<Object> exp = (Expression<Object>) node.getValue();
		return node.getType() == NodeType.NODE_TYPE_EXPRESSION && SymbolTable.isParentheses(exp.getOp());
	}
	
	public static boolean isArgumentOperator(String op)
	{
	    return SymbolTable.S_EQ(op, ",");
	}
	
	public static boolean isArgumentNode(Node<Object> node)
	{
		if (node.getType() != NodeType.NODE_TYPE_EXPRESSION) {
			return false;
		}
		@SuppressWarnings("unchecked")
		Expression<Object> exp = (Expression<Object>) node.getValue();
	    return SymbolTable.isArgumentOperator(exp.getOp());
	}
	
	public static boolean isKeywordVariableModifier(String val) {
		return SymbolTable.S_EQ(val, "unsigned") || SymbolTable.S_EQ(val, "signed") || SymbolTable.S_EQ(val, "static") || SymbolTable.S_EQ(val, "const") || SymbolTable.S_EQ(val, "extern") || SymbolTable.S_EQ(val, "__ignore_typecheck__");
	}
	
	public static boolean isUnaryOperator(String op) {
		return SymbolTable.S_EQ(op, "-") || SymbolTable.S_EQ(op, "!") || SymbolTable.S_EQ(op, "~") || SymbolTable.S_EQ(op, "*") || SymbolTable.S_EQ(op, "&") || SymbolTable.S_EQ(op, "++") || SymbolTable.S_EQ(op, "--");
	}
	
	public static boolean opIsIndirection(String op)
	{
	    return SymbolTable.S_EQ(op, "*");
	}
	
	public static boolean isLeftOperandedUnaryOperator(String op)
	{
	    return SymbolTable.S_EQ(op, "++") || SymbolTable.S_EQ(op, "--");
	}
	
	public boolean unaryOperandCompatible(Token<Object> token)
	{
	    return SymbolTable.isAccessOperator((String) token.getValue()) || SymbolTable.isArrayOperator((String) token.getValue()) || isParentheses((String) token.getValue());
	}
}
