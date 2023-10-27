package parserDatatypes;

import parser.Node;
import parser.NodeType;
import helpers.Vector;

public class Array<T> {
	// Wie viele Klammern gibt es?
	private ArrayBrackets<T> brackets;
	// Wie viele Bytes Speicher besetzt der Array (Größe des Datentyps x Indizes des Arrays).
	// "int[4][4];" => 4x4x4 = 64 Bytes Größe des Arrays.
	private int size;
	
	public Array() {
		this.setBrackets(null);
		this.setSize(0);
	}

	public ArrayBrackets<T> getBrackets() {
		return brackets;
	}

	public void setBrackets(ArrayBrackets<T> brackets) {
		this.brackets = brackets;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
	
	public static ArrayBrackets<Node<Object>> arrayBracketsNew() {
		ArrayBrackets<Node<Object>> brackets = new ArrayBrackets<Node<Object>>();
		brackets.setNBrackets(Vector.vectorCreate());
		return brackets;
	}
	
	public static void arrayBracketsAdd(ArrayBrackets<Node<Object>> brackets, Node<Object> bracketNode) {
		// Sicherstellen, dass wir keinen Funktionsknoten übergeben bekommen.
		assert(bracketNode.getType() == NodeType.NODE_TYPE_BRACKET);
		brackets.getNBrackets().vectorPush(bracketNode);
	}
	
	public static Vector<Node<Object>> arrayBracketsNodeVector(ArrayBrackets<Node<Object>> brackets) {
		return brackets.getNBrackets();
	}
	
	// TO DO:
	public static int arrayBracketsCalculateSizeFromIndex(Datatype<Node<Object>> dtype, ArrayBrackets<Node<Object>> brackets, int index) {
		Vector<Node<Object>> arrayVec = Array.arrayBracketsNodeVector(brackets);
		// Berechnen der Größe des Arrays, beginnend mit der Größe des Datentyps.
		int size = dtype.getSize();
		// 1. Fall: Wenn der Index Größer ist als die Größe des Vektors, haben wir keinen Array, sondern einen Pointer.
		// Wird mit "char* abc" auf "abc[0]" zugegriffen, so geben wir nur die Größe des Datentyps zurück.
		if (index >= arrayVec.vectorCount()) {
			
			return size;
		}
		// 2: Fall: Tatsächlicher Zugriff auf einen Array.
		// Wir starten beim Index, der übergeben wurde. Nicht immer ist das die 0 (der Beginn des Arrays).
		arrayVec.vectorSetPeekPointer(index);
		Node<Object> arrayBracketNode = arrayVec.vectorPeekPtr();
		// Fall 2.1: Keine Elemente im Array gefunden, daher werden 0 Bytes als Größe zurückgegeben.
		if (arrayBracketNode == null) {
			return 0;
		}
		// Fall 2.2: Haben Elemente im Array gefunden.
		while(arrayBracketNode != null) {
			// Checken, ob sich eine statische Zahl zwischen den Arrayklammern Klammern befindet.
			@SuppressWarnings("unchecked")
			Bracket<Object> bracket = (Bracket<Object>) arrayBracketNode.getValue();
			assert(bracket.getInner().getType() == NodeType.NODE_TYPE_NUMBER);
			// Finden wir einen Bezeichner einer Integerzahl, konvertieren wir es zu einem Integer.
			// Falls dort eine Expression steht, und wir die Größe nicht ausrechnen können, setzen wir die Größe von -1 fest.
			long number = -1;
			if (bracket.getInner().getValue() instanceof Long) {
				number = (Long) bracket.getInner().getValue();
			}
			// Anschließend multiplizieren wir die bisherige Größe mit der Zahl in den Klammern, um am Ende die Arraygröße in Bytes zu erhalten.
			size *= number;
			arrayBracketNode = arrayVec.vectorPeekPtr();
		}
		return size;
	}
	
	public static int arrayBracketsCalculateSize(Datatype<Node<Object>> dtype, ArrayBrackets<Node<Object>> brackets) {
		return Array.arrayBracketsCalculateSizeFromIndex(dtype, brackets, 0);
	}
	
	public static int arrayTotalIndexes(Datatype<Node<Object>> dtype) {
		assert(dtype.getFlags() == DatatypeFlag.DATATYPE_FLAG_IS_ARRAY);
		ArrayBrackets<Node<Object>> brackets = dtype.getArray().getBrackets();
		return brackets.getNBrackets().vectorCount();
	}
}
