package parser;

import helpers.Vector;
import lexer.SymbolTable;
import parserDatatypes.DatatypeType;
import parserDatatypes.DatatypeFunctions;
import parserDatatypes.Var;
import parserDatatypes.VarList;
import parserDatatypes.Struct;
import parserDatatypes.Union;

public class Helper {
	
	/**
	 * Gibt die Größe des übergebenen Variablenknoten zurück.
	 * @param varNode
	 * @return
	 */
	public static int variableSize(Node<Object> varNode) {
		assert(varNode.getType() == NodeType.NODE_TYPE_VARIABLE);
		@SuppressWarnings("unchecked")
		Var<Object> var = (Var<Object>) varNode.getValue();
		return DatatypeFunctions.datatypeSize(var.getType());
	}
	
	/**
	 * Addiert die Größen aller Variablenknoten innerhalb des Variablenlistenknoten und gibt das Resultat zurück.
	 * @param varListNode
	 * @return
	 */
	public static int variableSizeForList(Node<Object> varListNode) {
		assert(varListNode.getType() == NodeType.NODE_TYPE_VARIABLE_LIST);
		int size = 0;
		@SuppressWarnings("unchecked")
		VarList<Object> varList = (VarList<Object>) varListNode.getValue();
		varList.getList().vectorSetPeekPointer(0);
		Node<Object> varNode = varList.getList().vectorPeekPtr();
		while(varNode != null) {
			size += variableSize(varNode);
			varNode = varList.getList().vectorPeekPtr();
		}
		return size;
	}
	
	// Gibt den Körperknoten der Struktur oder der Union zurück, die übergeben wurde.
	public static Node<Object> variableStructOrUnionBodyNode(Node<Object> node) {
		if (!(NodeFunctions.nodeIsStructOrUnionVariable(node))) {
			return null;
		}
		// Gib den Struct-Körper zurück.
		@SuppressWarnings("unchecked")
		Var<Object> var = (Var<Object>) node.getValue();
		if (var.getType().getType() == DatatypeType.DATA_TYPE_STRUCT) {
			Node<Object> structNode = (Node<Object>) var.getType().getStructOrUnionNode();
			@SuppressWarnings("unchecked")
			Struct<Object> struct = (Struct<Object>) structNode.getValue();
			if (struct.getBodyN() != null) {
				return struct.getBodyN();
			}
			return Parser.parserCurrentBody;
		}
		// Gib den Union-Körper zurück.
		else if (var.getType().getType() == DatatypeType.DATA_TYPE_UNION) {
			Node<Object> unionNode = (Node<Object>) var.getType().getStructOrUnionNode();
			@SuppressWarnings("unchecked")
			Union<Object> union = (Union<Object>) unionNode.getValue();
			if (union.getBodyN() != null) {
				return union.getBodyN();
			}
			return Parser.parserCurrentBody;
		}
		
		return null;
	}
	
	// Wir wollen unsere Variablen in 4 Bit-Blöcke anpassen.
	// Haben wir val = 21 und to = 4, so haben wir 21 % 4 = 1 Byte überstehen.
	// Wir müssen also 4 - (21 % 4) % 21 = 3 Bytes hinzufügen, um 4 Byte-Blöcke zu haben.
	// Die Funktion gibt also die Anzahl der Bytes zurück, die wir hinzufügen müssen, um 4 Byte-Blöcke zu erhalten.
	public static int padding(int val, int to) {
		if (to <= 0) {
			return 0;
		}
		if ((val % to) == 0) {
			return 0;
		}
		return to - (val % to) % to;
	}
	
	// Wir passen unsere Variablen in 4 Byte-Blöcke an, damit der Prozessor sie mit möglichst wenig Lesezugriffen bei Bedarf lesen kann.
	public static int alignValue(int val, int to) {
		// Haben wir keine 4 Byte-Blöcke, berechnen wir die Anzahl von Bytes, die wir einfügen müssen.
		if (val % to != 0) {
			val += Helper.padding(val, to);
		}
		return val;
	}
	
	// Anpassen von negativen Zahlen. Wir passen dann beispielsweise -20 an -4-Blöcke an.
	public static int alignValueTreatPositive(int val, int to) {
		assert(to >= 0);
		if (val < 0) {
			to = -to;
		}
		return Helper.alignValue(val, to);
	}
	
	// Wir addieren alle Paddings in dem Knotenvektor.
	public static int computeSumPadding(Vector<Node<Object>> vec) {
		int padding = 0;
		DatatypeType lastType = null;
		boolean mixedTypes = false;
		vec.vectorSetPeekPointer(0);
		Node<Object> curNode = vec.vectorPeekPtr();
		Node<Object> lastNode = null;
		while(curNode != null) {
			if (curNode.getType() != NodeType.NODE_TYPE_VARIABLE) {
				curNode = vec.vectorPeekPtr();
				continue;
			}
			@SuppressWarnings("unchecked")
			Var<Object> var = (Var<Object>) curNode.getValue();
			padding += var.getPadding();
			lastType = var.getType().getType();
			lastNode = curNode;
			curNode = vec.vectorPeekPtr();
		}
		return padding;
	}
}
