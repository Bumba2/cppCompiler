package parserFixup;

import compiler.CompileProcess;
import parser.Node;
import parser.NodeFunctions;
import parser.Parser;
import parser.Scope;
import parserDatatypes.Datatype;
import parserDatatypes.DatatypeType;
import parserDatatypes.Var;

public class FixupConfig {
	private Object privateData;
	
	public FixupConfig(Object privateData) {
		this.privateData = privateData;
	}
	
	// Repariert einen Struct-Knoten. Dieser Knoten besitzt als Datentyp einen Typ Struct, aber
	// der angehängte Struct-Knoten ist NULL. Das liegt daran, dass der Datentyp im Code verwendet
	// wird, bevor die Struktur im Code definiert wurde.
	// Wir stellen die Werte für den Strukturendatentyp so ein, wie es nach dem kompletten Parsen aussehen würde (für die Forward-deklarierte Struktur).
	public boolean datatypeStructNodeFix(Fixup<Object> fixup, CompileProcess currentProcess) {
		@SuppressWarnings("unchecked")
		DatatypeStructNodeFixPrivate<Object> privateData = (DatatypeStructNodeFixPrivate<Object>) Fixup.fixupPrivate(fixup);
		@SuppressWarnings("unchecked")
		Var<Object> var = (Var<Object>) privateData.getNode().getValue();
		Datatype<Node<Object>> dtype = var.getType();
		dtype.setType(DatatypeType.DATA_TYPE_STRUCT);
		dtype.setSize(Parser.sizeOfStruct(dtype.getTypeStr()));
		dtype.setStructOrUnionNode(NodeFunctions.structNodeForName(Parser.currentProcess, dtype.getTypeStr(), Scope.scopeCurrent(currentProcess)));
		// Falls wir den Strukturknoten immer noch nicht finden, hat der Fixup nicht geklappt.
		if (dtype.getStructOrUnionNode() == null) {
			return false; // Die Struktur existiert nicht im Kompilierungsprozess.
		}
		return true;
	}

	public Object getPrivateData() {
		return privateData;
	}

	public void setPrivateData(Object privateData) {
		this.privateData = privateData;
	}
}
