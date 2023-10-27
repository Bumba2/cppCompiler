package parserDatatypes;

import compiler.CompileProcess;
import compiler.Compiler;
import helpers.Vector;
import lexer.SymbolTable;
import parser.Body;
import parser.Node;
import parser.NodeType;
import parser.Parser;
import parser.Scope;
import parser.Symbol;
import parser.Symresolver;

public class DatatypeFunctions {
	// Größen der Datentypen (in Byte)
	public static int DATA_SIZE_ZERO = 0;
	public static int DATA_SIZE_BYTE = 1;
	public static int DATA_SIZE_WORD = 1;
	public static int DATA_SIZE_DWORD = 4;
	public static int DATA_SIZE_DDWORD = 8;
	
	// Erstellt eine Shallow-Copy eines Datentyps.
	public static Datatype<Node<Object>> memcpy(Datatype<Node<Object>> dtype) {
		Datatype<Node<Object>> datatypeOut = new Datatype<Node<Object>>();
		datatypeOut.setArray(dtype.getArray());
		datatypeOut.setFlags(dtype.getFlags());
		datatypeOut.setPointerDepth(dtype.getPointerDepth());
		if (dtype.getSecondary() != null) {
			datatypeOut.setSecondary(DatatypeFunctions.memcpy(dtype.getSecondary()));
		}
		else {
			datatypeOut.setSecondary(null);
		}
		datatypeOut.setSize(dtype.getSize());
		datatypeOut.setStructOrUnionNode(dtype.getStructOrUnionNode());
		datatypeOut.setType(dtype.getType());
		datatypeOut.setTypeStr(dtype.getTypeStr());
		datatypeOut.setStartToken(dtype.getStartToken());
		datatypeOut.setParentStructNode(dtype.getParentStructNode());
		datatypeOut.setStartTokenVecIndex(dtype.getStartTokenVecIndex());
		return datatypeOut;
	}
	
	public static int sizeOfString(String str) {
		return str.length() * DatatypeFunctions.DATA_SIZE_BYTE;
	}
	
	public static boolean datatypeIsStructOrUnionForName(String name) {
		return SymbolTable.S_EQ(name, "union") || SymbolTable.S_EQ(name, "struct") || SymbolTable.S_EQ(name, "class");
	}
	
	public static boolean datatypeIsStructOrUnion(Datatype<Node<Object>> dtype) {
		return dtype != null && (dtype.getType() == DatatypeType.DATA_TYPE_STRUCT || dtype.getType() == DatatypeType.DATA_TYPE_UNION || dtype.getType() == DatatypeType.DATA_TYPE_CLASS);
	}
	
	public static int datatypeSizeForArrayAccess(Datatype<Node<Object>> dtype) {
		if (DatatypeFunctions.datatypeIsStructOrUnion(dtype) && dtype.getFlags() == DatatypeFlag.DATATYPE_FLAG_IS_POINTER && dtype.getPointerDepth() == 1) {
			// In diesem Fall haben wir etwas wie "struct abc* abc; abc[0];".
			// Hier haben wir den Pointer in etwas konvertiert, was kein Pointer mehr ist.
			return dtype.getSize();
		}
		// Ist es keine Struktur oder Union, nutzen wir unsere Größenberechnungsfunktion.
		return DatatypeFunctions.datatypeSize(dtype);
	}
	
	// Gibt nur die Größe des kleinsten Elements zurück, bei einem char Array z. B. 1 Byte, statt 20 Bytes bei einem Array mit Länge 20.
	public static int datatypeElementSize(Datatype<Node<Object>> dtype) {
		if (dtype.getFlags() == DatatypeFlag.DATATYPE_FLAG_IS_POINTER) {
			return DatatypeFunctions.DATA_SIZE_DWORD;
		}
		return dtype.getSize();
	}
	
	// Diese Funktion ignoriert den Fakt, dass der Datentyp ein Pointer sein könnte.
	// Beispiel: Gib der Funktion einen "const char* name" und es gibt die Größe 1 Byte zurück (für char).
	public static int datatypeSizeNoPtr(Datatype<Node<Object>> dtype) {
		if (dtype.getFlags() == DatatypeFlag.DATATYPE_FLAG_IS_ARRAY) {
			return dtype.getArray().getSize();
		}
		return dtype.getSize();
	}
	
	public static int datatypeSize(Datatype<Node<Object>> dtype) {
		// Ist es ein Pointer? Pointer haben die Variablengröße von 4 Bytes.
		if (dtype.getFlags() == DatatypeFlag.DATATYPE_FLAG_IS_POINTER && dtype.getPointerDepth() > 0) {
			return DatatypeFunctions.DATA_SIZE_DWORD;
		}
		// Ist es ein Array?
		if (dtype.getFlags() == DatatypeFlag.DATATYPE_FLAG_IS_ARRAY) {
			return dtype.getArray().getSize();
		}
		// Einzelne Elemente haben in der Variable size des Datentyps ihre Größe gespeichert.
		return dtype.getSize();
	}
	
	// Ist der Datentyp ein primitiver Datentyp?
	public static boolean datatypeIsPrimitive(Datatype<Node<Object>> dtype) {
		return !(DatatypeFunctions.datatypeIsStructOrUnion(dtype));
	}
	
	public static boolean genericTypeHasAlreadyBeenDefined(Vector<Object> allValidGenerics, String genericIdentifier) {
		if (allValidGenerics == null) {
			return false;
		}
		allValidGenerics.vectorSetPeekPointer(0);
		for(int i = 0; i < allValidGenerics.vectorCount(); i++) {
			@SuppressWarnings("unchecked")
			Node<Object> genericIdentifierNode = (Node<Object>) allValidGenerics.vectorPeekAt(i);
			if (SymbolTable.S_EQ((String) genericIdentifierNode.getValue(), genericIdentifier)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean genericsAreValidForParentClass(Datatype<Node<Object>> parentClassDtype, Node<Object> parsedParentGenericsWhileChildClassDefinition) {
		@SuppressWarnings("unchecked")
		GenericPlaceholders<Object> parentGenerics = (GenericPlaceholders<Object>) parentClassDtype.getGenericDatatypesNode().getValue();
		Vector<Object> genericIdentifiersNodeVector = parentGenerics.getGenericIdentifierNodeVector();
		@SuppressWarnings("unchecked")
		GenericPlaceholders<Object> parentGenericsToCheck = (GenericPlaceholders<Object>) parsedParentGenericsWhileChildClassDefinition.getValue();
		Vector<Object> genericIdentifiersToCheckNodeVector = parentGenericsToCheck.getGenericIdentifierNodeVector();
		// Ist die Anzahl der angegebenen generischen Datentypen nicht identisch, sind die angegebenen Generics falsch.
		if (genericIdentifiersNodeVector.vectorCount() != genericIdentifiersToCheckNodeVector.vectorCount()) {
			return false;
		}
		for(int i = 0; i < genericIdentifiersToCheckNodeVector.vectorCount(); i++) {
			@SuppressWarnings("unchecked")
			Node<Object> genericDtypeIdentifierNode = (Node<Object>) genericIdentifiersNodeVector.vectorPeekAt(i);
			String genericDtypeIdentifier = (String) genericDtypeIdentifierNode.getValue();
			@SuppressWarnings("unchecked")
			Node<Object> genericDtypeIdentifierToCheckNode = (Node<Object>) genericIdentifiersToCheckNodeVector.vectorPeekAt(i);
			String genericDtypeIdentifierToCheck = (String) genericDtypeIdentifierToCheckNode.getValue();
			if (!(SymbolTable.S_EQ(genericDtypeIdentifier, genericDtypeIdentifierToCheck))) {
				return false;
			}
		}
		return true;
	}
}
