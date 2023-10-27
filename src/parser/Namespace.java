package parser;

import compiler.CompileProcess;
import helpers.Vector;
import lexer.SymbolTable;

public class Namespace<T> {
	private Vector<Node<T>> identifierNodes; // Ein Namespace kann mehrere Alias' besitzen.
	private Node<T> bodyNode;
	// In welchem Scope ist der Namensraum definiert? Ungleich des Scopes des Körpers eines Namensraums.
	private Scope<T> scope;  
	private boolean completelyDefined;
	private Vector<Node<T>> declaredMember;
	
	public Namespace(Node<T> identifierNode, Node<T> bodyNode, Scope<T> scope, boolean completelyDefined) {
		this.identifierNodes = Vector.vectorCreate();
		this.identifierNodes.vectorPush(identifierNode);
		this.bodyNode = bodyNode; // Kann einen Körper haben, muss er aber nicht.
		this.scope = scope;
		this.completelyDefined = completelyDefined;
		if (!(completelyDefined)) {
			this.declaredMember = Vector.vectorCreate();
		}
	}
	
	public Vector<Node<T>> getIdentifierNodes() {
		return identifierNodes;
	}
	
	public void setIdentifierNodes(Vector<Node<T>> identifierNodes) {
		this.identifierNodes = identifierNodes;
	}
	
	public Node<T> getBodyNode() {
		return bodyNode;
	}
	
	public void setBodyNode(Node<T> bodyNode) {
		this.bodyNode = bodyNode;
	}
	
	public Scope<T> getScope() {
		return scope;
	}
	
	public void setScope(Scope<T> scope) {
		this.scope = scope;
	}

	public Vector<Node<T>> getDeclaredMember() {
		return declaredMember;
	}

	public void setDeclaredMember(Vector<Node<T>> declaredMember) {
		this.declaredMember = declaredMember;
	}

	public static boolean includesMemberName(Namespace<Object> namespace, String memberName) {
		if (namespace.getDeclaredMember() == null) {
			return false;
		}
		namespace.getDeclaredMember().vectorSetPeekPointer(0);
		for(int i = 0; i < namespace.getDeclaredMember().vectorCount(); i++) {
			Node<Object> node = namespace.getDeclaredMember().vectorPeekPtrAt(i);
			if (SymbolTable.S_EQ((String) node.getValue(), memberName)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean addMemberName(Namespace<Object> namespace, Node<Object> memberNode) {
		if (namespace.getDeclaredMember() == null) {
			return false;
		}
		namespace.getDeclaredMember().vectorPush(memberNode);
		return true;
	}
	
	public static boolean namespaceOrMemberIsDefined(CompileProcess currentProcess, String namespaceIdentifier, String memberIdentifier) {
		Scope<Object> scope = Scope.scopeCurrent(currentProcess);
		Symbol<Object> sym = Symresolver.symresolverGetSymbol(currentProcess, namespaceIdentifier, scope, null);
		if (sym != null) {
			Node<Object> namespaceNode = sym.getData();
			if (namespaceNode.getType() != NodeType.NODE_TYPE_NAMESPACE) {
				return false;
			}
			@SuppressWarnings("unchecked")
			Namespace<Object> namespace = (Namespace<Object>) namespaceNode.getValue();
			if (namespace.getCompletelyDefined()) {
				return true;
			}
			if (namespace.getDeclaredMember() != null) {
				Vector<Node<Object>> memberVec = namespace.getDeclaredMember();
				memberVec.vectorSetPeekPointer(0);
				String namespaceMember = null;
				Node<Object> node;
				for(int i = 0; i < memberVec.vectorCount(); i++) {
					node = memberVec.vectorPeekAt(i);
					namespaceMember = (String) node.getValue();
					if (SymbolTable.S_EQ(memberIdentifier, namespaceMember)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public boolean getCompletelyDefined() {
		return completelyDefined;
	}

	public void setCompletelyDefined(boolean completelyDefined) {
		this.completelyDefined = completelyDefined;
	}
}
