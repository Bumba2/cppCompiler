package parserDatatypes;

import helpers.Vector;
import parser.Node;
import parser.NodeType;
import parser.Scope;
import parserStackFrames.StackFrame;

public class Function<T> {
	// Special Flags.
	private FunctionFlag flags;
	// Access Specifier
	private AccessSpecifierFlag accessSpecifier;
	// Rückgabetyp, zum Beispiel void, int, long, ..
	private Datatype<Node<T>> rtype; 
	// Funktionsname, zum Beispiel "main".
	private String name;
	// Übergabeparameter.
	private FunctionArguments<T> args;
	// Enthält einen Vektor von Stack-Frame-Elementen.
	private StackFrame<T> frame;
	// Referenz zum Funktionskörperknoten. Ist NULL, falls es ein Prototyp ist.
	private Node<T> bodyN;
	// Die Stackgröße für alle Variablen in dieser Funktion.
	private int stackSize;
	// In welchen Scopes existieren Friend-Deklarationen der Funktion?
	private Vector<Scope<T>> friendlyScopes;
	private Node<T> functionNode; // Hier ist die Funktion abgelegt.
	// Ist ein "throw" Teil des Funktionskopfs?
	private Node<T> ThrowInsideFunctionHead;
	// Welcher Index besitzt das erste Token der Funktion?
	private Integer startTokenVecIndex;
	// Besitzt es eine Forward-Declaration?
	private boolean hasForwardDeclaration;
	
	public Function() {
		this.setFlags(null);
		this.setRtype(null);
		this.setName(null);
		this.setArgs(null);
		this.setBodyN(null);
		this.setFrame(null);
		this.setStackSize(0);
		this.setFriendlyScopes(Vector.vectorCreate());
		this.setStartTokenVecIndex(null);
		this.setHasForwardDeclaration(false);
	}
	
	public Function(String name, FunctionArguments<T> args, Node<T> bodyN, Datatype<Node<T>> rtype, int stackSize, AccessSpecifierFlag accessSpecifier, Node<T> namespaceIdentifier) {
		this.flags = null;
		this.rtype = rtype;
		this.name = name;
		this.args = args;
		this.frame = new StackFrame<T>();
		this.bodyN = bodyN;
		this.stackSize = stackSize;
		this.setFriendlyScopes(Vector.vectorCreate());
		this.setStartTokenVecIndex(null);
	}

	public FunctionFlag getFlags() {
		return flags;
	}

	public void setFlags(FunctionFlag flags) {
		this.flags = flags;
	}

	public Datatype<Node<T>> getRtype() {
		return rtype;
	}

	public void setRtype(Datatype<Node<T>> rtype) {
		this.rtype = rtype;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public FunctionArguments<T> getArgs() {
		return args;
	}

	public void setArgs(FunctionArguments<T> args) {
		this.args = args;
	}

	public Node<T> getBodyN() {
		return bodyN;
	}

	public void setBodyN(Node<T> bodyN) {
		this.bodyN = bodyN;
	}

	public int getStackSize() {
		return stackSize;
	}

	public void setStackSize(int stackSize) {
		this.stackSize = stackSize;
	}

	public StackFrame<T> getFrame() {
		return frame;
	}

	public void setFrame(StackFrame<T> frame) {
		this.frame = frame;
	}

	public AccessSpecifierFlag getAccessSpecifier() {
		return accessSpecifier;
	}

	public void setAccessSpecifier(AccessSpecifierFlag accessSpecifier) {
		this.accessSpecifier = accessSpecifier;
	}

	public Vector<Scope<T>> getFriendlyScopes() {
		return friendlyScopes;
	}

	public void setFriendlyScopes(Vector<Scope<T>> friendlyScopes) {
		this.friendlyScopes = friendlyScopes;
	}

	public Node<T> getFunctionNode() {
		return functionNode;
	}

	public void setFunctionNode(Node<T> functionNode) {
		this.functionNode = functionNode;
	}

	public Node<T> getThrowInsideFunctionHead() {
		return ThrowInsideFunctionHead;
	}

	public void setThrowInsideFunctionHead(Node<T> throwInsideFunctionHead) {
		ThrowInsideFunctionHead = throwInsideFunctionHead;
	}

	public Integer getStartTokenVecIndex() {
		return this.startTokenVecIndex;
	}

	public void setStartTokenVecIndex(Integer startTokenVecIndex) {
		this.startTokenVecIndex = startTokenVecIndex;
	}

	public boolean getHasForwardDeclaration() {
		return hasForwardDeclaration;
	}

	public void setHasForwardDeclaration(boolean hasForwardDeclaration) {
		this.hasForwardDeclaration = hasForwardDeclaration;
	}
}
