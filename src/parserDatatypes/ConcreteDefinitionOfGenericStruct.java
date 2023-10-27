package parserDatatypes;

import helpers.Vector;
import parser.Node;

public class ConcreteDefinitionOfGenericStruct<T> {
	private Vector<Datatype<Node<T>>> concreteDatatypesVector;
	
	public ConcreteDefinitionOfGenericStruct(Vector<Datatype<Node<T>>> concreteDatatypesVector) {
		this.concreteDatatypesVector = concreteDatatypesVector;
	}

	public Vector<Datatype<Node<T>>> getConcreteDatatypesVector() {
		return concreteDatatypesVector;
	}

	public void setConcreteDatatypesVector(Vector<Datatype<Node<T>>> concreteDatatypesVector) {
		this.concreteDatatypesVector = concreteDatatypesVector;
	}
}
