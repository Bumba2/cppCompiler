package parserDatatypes;

import lexer.Position;
import lexer.Token;
import parser.Node;

public class Datatype<T> {
	// Unterschiedliche Ausprägungen eines Datentyps. "unsigned int" statt "int" beispielsweise.
	private int flags;
	// zum Beispiel long, int, float etc..
	private DatatypeType type;
	// Haben wir die Variable "long long a", haben wir 2 Datentypen (ist in C(++) erlaubt).
	private Datatype<T> secondary;
	// Datentyp long ist beispielsweise repräsentiert als "long". Custom-Datentypen (struct und union) sind hier abgespeichert.
	private String typeStr;
	// int *** a; -> hätte eine Pointertiefe von 3.
	private int pointerDepth;
	// Ist es ein structNode oder ein unionNode?
	private T structOrUnionNode;
	// Gibt es eine Elternstruktur / Elternklasse?
	private T parentStructNode;
	// Falls es ein Struct ist, sind generische Datentypen angegeben?
	// Welche generischen Datentypbezeichner wurden für das Struct bzw. die Klasse angegeben?
	private Node<Object> genericDatatypesNode;
	// Falls es ein Struct ist, sind explizite Typen für die generischen Typen angegeben worden?
	private Node<Object> concreteDatatypesNode;
	// Wie groß ist der Datentyp (in Byte)?
	private int size;
	// Ist es ein Array?
	private Array<T> array;
	// Ist der Array in C++-Style angegeben? Zur Kontrolle zählen wir die Klammern. Geparsed werden die Klammern nur rechts von der Zuweisung.
	private int cppStyleLeftSideOfAssignmentBrackets;
	// Bestitzt es einen Unary-Operator (beispielsweise bei "classname&" ist "&" der Unary-Operator).
	private String unaryOperator;
	// Mit welchem Token startete das Parsen des Datentyps?
	private Token<Object> startToken;
	// Wo befindet sich das startToken im Tokenvektor?
	private Integer startTokenVecIndex;
	
	public Datatype() {
	}
	
	public Datatype(int flags, DatatypeType type, Datatype<T> secondary, String typeStr, int pointerDepth, T structOrUnionNode, int size, Token<Object> startToken, Integer startTokenVecIndex) {
		this.flags = flags;
		this.type = type;
		this.secondary = secondary;
		this.typeStr = typeStr;
		this.pointerDepth = pointerDepth;
		this.structOrUnionNode = structOrUnionNode;
		this.startToken = startToken;
		this.setStartTokenVecIndex(startTokenVecIndex);
	}
	
	public int getFlags() {
		return flags;
	}
	public void setFlags(int flags) {
		this.flags = flags;
	}
	public DatatypeType getType() {
		return type;
	}
	public void setType(DatatypeType type) {
		this.type = type;
	}
	public Datatype<T> getSecondary() {
		return secondary;
	}
	public void setSecondary(Datatype<T> secondary) {
		this.secondary = secondary;
	}
	public String getTypeStr() {
		return typeStr;
	}
	public void setTypeStr(String typeStr) {
		this.typeStr = typeStr;
	}
	public int getPointerDepth() {
		return pointerDepth;
	}
	public void setPointerDepth(int pointerDepth) {
		this.pointerDepth = pointerDepth;
	}
	public T getStructOrUnionNode() {
		return structOrUnionNode;
	}
	public void setStructOrUnionNode(T structNode) {
		this.structOrUnionNode = structNode;
	}

	public int getSize() {
		return this.size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public Array<T> getArray() {
		return array;
	}

	public void setArray(Array<T> array) {
		this.array = array;
	}

	public String getUnaryOperator() {
		return unaryOperator;
	}

	public void setUnaryOperator(String unaryOperator) {
		this.unaryOperator = unaryOperator;
	}

	public  Node<Object> getGenericDatatypesNode() {
		return genericDatatypesNode;
	}

	public void setGenericDatatypesNode(Node<Object> genericDatatypesNode) {
		this.genericDatatypesNode = genericDatatypesNode;
	}

	public Node<Object> getConcreteDatatypesNode() {
		return concreteDatatypesNode;
	}

	public void setConcreteDatatypesNode(Node<Object> concreteDatatypesNode) {
		this.concreteDatatypesNode = concreteDatatypesNode;
	}

	public int getCppStyleLeftSideOfAssignmentBrackets() {
		return cppStyleLeftSideOfAssignmentBrackets;
	}

	public void setCppStyleLeftSideOfAssignmentBrackets(int cppStyleLeftSideOfAssignmentBrackets) {
		this.cppStyleLeftSideOfAssignmentBrackets = cppStyleLeftSideOfAssignmentBrackets;
	}

	public Token<Object> getStartToken() {
		return this.startToken;
	}

	public void setStartToken(Token<Object> startToken) {
		this.startToken = startToken;
	}

	public T getParentStructNode() {
		return parentStructNode;
	}

	public void setParentStructNode(T parentStructNode) {
		this.parentStructNode = parentStructNode;
	}

	public Integer getStartTokenVecIndex() {
		return startTokenVecIndex;
	}

	public void setStartTokenVecIndex(Integer startTokenVecIndex) {
		this.startTokenVecIndex = startTokenVecIndex;
	}
}
