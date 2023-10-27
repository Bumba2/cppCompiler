package parser;

import helpers.Vector;
import parserDatatypes.AccessSpecifierFlag;
import parserDatatypes.VariableSize;

public class Body<T> {
	/**
	 * Statements-Vektor nimmt Knoten von Statements in einem Körper auf.
	 * Beispielsweise "int x = 50;" in einem Körper würde als Element in dem Vektor gespeichert werden.
	 */
	private Vector<Node<T>> statements;
	private VariableSize size; // Größe aller Variablen in diesem Körper
	private boolean padded; // Ist wahr, wenn die Variablengröße vergrößert werden musste aufgrund von Padding im Körper.
	private Node<T> largestVarNode; // Referenz zur größten Variable in dem Körper. NULL, wenn noch keine Variablendeklaration vorhanden ist.
	// Welcher Access-Specifier wird aktuell auf die Elemente im Body angewendet?
	//Wird beispielsweise durch "public:" gesetzt.
	private AccessSpecifierFlag accessSpecifierFlag;
	private BodyFlag bodyFlag;
	private Scope<T> bodyScope;
	
	
	public Body(Vector<Node<T>> statements, VariableSize size, boolean padded, Node<T>largestVarNode, BodyFlag bodyFlag, Scope<T> bodyScope) {
		this.setStatements(statements);
		this.setSize(size);
		this.setPadded(padded);
		this.setLargestVarNode(largestVarNode);
		this.accessSpecifierFlag = null;
		this.bodyFlag = bodyFlag;
		this.bodyScope = bodyScope;
	}

	public Vector<Node<T>> getStatements() {
		return statements;
	}

	public void setStatements(Vector<Node<T>> statements) {
		this.statements = statements;
	}

	public VariableSize getSize() {
		return size;
	}

	public void setSize(VariableSize size) {
		this.size = size;
	}

	public boolean isPadded() {
		return padded;
	}

	public void setPadded(boolean padded) {
		this.padded = padded;
	}

	public Node<T> getLargestVarNode() {
		return largestVarNode;
	}

	public void setLargestVarNode(Node<T> largestVarNode) {
		this.largestVarNode = largestVarNode;
	}

	public AccessSpecifierFlag getAccessSpecifierFlag() {
		return accessSpecifierFlag;
	}

	public void setAccessSpecifierFlag(AccessSpecifierFlag accessSpecifierFlag) {
		this.accessSpecifierFlag = accessSpecifierFlag;
	}

	public BodyFlag getBodyFlag() {
		return bodyFlag;
	}

	public void setBodyFlag(BodyFlag bodyFlag) {
		this.bodyFlag = bodyFlag;
	}

	public Scope<T> getBodyScope() {
		return bodyScope;
	}

	public void setBodyScope(Scope<T> bodyScope) {
		this.bodyScope = bodyScope;
	}
}
