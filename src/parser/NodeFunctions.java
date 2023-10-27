package parser;

import compiler.CompileProcess;
import helpers.Vector;
import lexer.SymbolTable;
import parserDatatypes.AccessSpecifierFlag;
import parserDatatypes.Bracket;
import parserDatatypes.ConcreteDefinitionOfGenericStruct;
import parserDatatypes.Datatype;
import parserDatatypes.DatatypeFunctions;
import parserDatatypes.Function;
import parserDatatypes.FunctionArguments;
import parserDatatypes.GenericPlaceholders;
import parserDatatypes.Struct;
import parserDatatypes.ThrowInsideFunctionHead;
import parserDatatypes.Union;
import parserDatatypes.Var;
import parserDatatypes.VariableSize;
import parserStatements.CaseStmt;
import parserStatements.CatchStmt;
import parserStatements.DefaultStmt;
import parserStatements.DeleteStmt;
import parserStatements.DoWhileStmt;
import parserStatements.ElseStmt;
import parserStatements.ForEachStmt;
import parserStatements.ForStmt;
import parserStatements.GotoStmt;
import parserStatements.IfStmt;
import parserStatements.ReturnStmt;
import parserStatements.Stmt;
import parserStatements.SwitchStmt;
import parserStatements.ThrowStmt;
import parserStatements.TryCatchStmt;
import parserStatements.WhileStmt;

public class NodeFunctions {
	public static Vector<Node<Object>> nodeVector = null; // Vector, der für normale Operationsen genututzt wird.
	public static Vector<Node<Object>> nodeVectorRoot = null; // Treevector.
	
	public static void nodeSetVector(Vector<Node<Object>> vec, Vector<Node<Object>> rootVec) {
		NodeFunctions.nodeVector = vec;
		NodeFunctions.nodeVectorRoot = rootVec;
	}
	
	// Fügt einen Knoten am Ende des Knotenvektors ein.
	public static void nodePush(Node<Object> node) {
		NodeFunctions.nodeVector.vectorPush(node);
	}
	
	// Wir nehmen den letzten Node des Vektors, außer der Vektor ist leer, dann wird NULL zurückgegeben.
	public static Node<Object> nodePeekOrNull() {
		return NodeFunctions.nodeVector.vectorBackPtrOrNull();
	}
	
	public static Node<Object> nodePeek() {
		return NodeFunctions.nodeVector.vectorBack();
	}
	
	public static Node<Object> nodePop() {
		// Nehmen uns den letzten Nodevektor und
		Node<Object> lastNode = NodeFunctions.nodeVector.vectorBackPtr();
		// Wenn der rootNodeVektor null ist, setzen wir den lastNodeRoot auf null, sonst auf das letzte Element des rootNodeVektors.
		Node<Object> lastNodeRoot = NodeFunctions.nodeVectorRoot.vectorEmpty() ? null : NodeFunctions.nodeVectorRoot.vectorBackPtrOrNull();
	
		NodeFunctions.nodeVector.vectorPop();
		
		// Duplikate auf dem Rootvektor und dem normalen Vektor wollen wir löschen. Wenn der letzte Knoten des normalen Vektors gleich
		// dem letzten Knoten des Rootvektors ist, löschen wir ihn aus dem Rootvektor.
		if (lastNode == lastNodeRoot) {
			NodeFunctions.nodeVectorRoot.vectorPop();
		}
		
		return lastNode;
	}
	
	// Gibt zurück, ob der übergebene Knoten in einen Expressionknoten überführt werden kann.
	// Es werden alle Nodetypen abgeglichen, die Teil einer Expression (wie 50+20) sein können.
	public static boolean nodeIsExpressionable(Node<Object> node) {
		return node.getType() == NodeType.NODE_TYPE_EXPRESSION || node.getType() == NodeType.NODE_TYPE_EXPRESSION_PARENTHESES || node.getType() == NodeType.NODE_TYPE_UNARY || node.getType() == NodeType.NODE_TYPE_IDENTIFIER || node.getType() == NodeType.NODE_TYPE_NUMBER || node.getType() == NodeType.NODE_TYPE_STRING;
	}
	
	// Die Funktion sieht sich den lezten Knoten an und gibt ihn zurück, falls er in eine Expression überführt werden kann, sonst gibt sie null zurück.
	// Finden wir beispielsweise einen Funktionsknoten, können wir diesen nicht in eine Expression überführen und die Funktion gibt null zurück.
	public static Node<Object> nodePeekExpressionableOrNull() {
		Node<Object> lastNode = NodeFunctions.nodePeekOrNull();
		return NodeFunctions.nodeIsExpressionable(lastNode) ? lastNode : null;
	}
	
	public static void makeDefaultNode(Node<Object> bodyNode) {
		NodeFunctions.nodeCreate(new Node<Object>(NodeType.NODE_TYPE_STATEMENT_DEFAULT, new DefaultStmt<Object>(bodyNode)));
	}
	
	public static void makeCastNode(Datatype<Node<Object>> dtype, Node<Object> operand) {
		NodeFunctions.nodeCreate(new Node<Object>(NodeType.NODE_TYPE_CAST, new Cast<Object>(dtype, operand)));
	}
	
	public static void makeTenaryNode(Node<Object> trueNode, Node<Object> falseNode) {
		NodeFunctions.nodeCreate(new Node<Object>(NodeType.NODE_TYPE_TENARY, new Tenary<Object>(trueNode, falseNode)));
	}
	
	public static void makeCaseNode(Node<Object> expNode, Node<Object> bodyNode) {
		NodeFunctions.nodeCreate(new Node<Object>(NodeType.NODE_TYPE_STATEMENT_CASE, new Stmt<Object>(new CaseStmt<Object>(expNode, bodyNode))));
	}
	
	public static void makeGotoNode(Node<Object> labelNode) {
		NodeFunctions.nodeCreate(new Node<Object>(NodeType.NODE_TYPE_STATEMENT_GOTO, new Stmt<Object>(new GotoStmt<Object>(labelNode))));
	}
	
	public static void makeLabelNode(Node<Object> nameNode) {
		NodeFunctions.nodeCreate(new Node<Object>(NodeType.NODE_TYPE_LABEL, new NodeLabel<Object>(nameNode)));
	}
	
	public static void makeContinueNode() {
		NodeFunctions.nodeCreate(new Node<Object>(NodeType.NODE_TYPE_STATEMENT_CONTINUE));
	}
	
	public static void makeBreakNode() {
		NodeFunctions.nodeCreate(new Node<Object>(NodeType.NODE_TYPE_STATEMENT_BREAK));
	}
	
	// Erstellt eine neue Expression aus zwei expressionable-Nodes und einem Operator. Dieser Knoten wird auf den Nodestack gepusht.
	public static void makeExpNode(Node<Object> leftNode, Node<Object> rightNode, String op) {
		assert(leftNode != null);
		assert(rightNode != null);
		NodeFunctions.nodeCreate(new Node<Object>(NodeType.NODE_TYPE_EXPRESSION, new Expression<Object>(leftNode, rightNode, op)));
	}
	
	public static void makeExpParenthesesNode(Node<Object> expNode) {
		NodeFunctions.nodeCreate(new Node<Object>(NodeType.NODE_TYPE_EXPRESSION_PARENTHESES, new Parenthesis<Object>(expNode)));
	}
	
	public static void makeBracketNode(Node<Object> node) {
		NodeFunctions.nodeCreate(new Node<Object>(NodeType.NODE_TYPE_BRACKET, new Bracket<Object>(node)));
	}
	
	public static void makeBodyNode(Vector<Node<Object>> bodyVec, VariableSize size, boolean padded, Node<Object> largestVarNode, BodyFlag bodyFlag, Scope<Object> bodyScope) {
		NodeFunctions.nodeCreate(new Node<Object>(NodeType.NODE_TYPE_BODY, new Body<Object>(bodyVec, size, padded, largestVarNode, bodyFlag, bodyScope)));
	}
	
	public static void makeLibraryNode(Node<Object> nameNode) {
		NodeFunctions.nodeCreate(new Node<Object>(NodeType.NODE_TYPE_DIRECTIVE_LIBRARY, new LibraryDirective<Object>(nameNode)));
	}
	
	public static void makeNamespaceNode(Node<Object> nameNode, Node<Object> bodyNode, Scope<Object> scope, boolean completelyDefined) {
		NodeFunctions.nodeCreate(new Node<Object>(NodeType.NODE_TYPE_NAMESPACE, new Namespace<Object>(nameNode, bodyNode, scope, completelyDefined)));
	}
	
	// Bekommt den Körperknoten einer Struktur und den Namen des structs übergeben und erstellt daraus einen struct-Knoten.
	public static void makeStructNode(String name, Node<Object> bodyNode, AccessSpecifierFlag accessSpecifier, boolean classDefinition, Node<Object> parentClassNode) {
		int flags = 0;
		if (bodyNode == null) {
			flags |= NodeFlag.NODE_FLAG_IS_FORWARD_DECLARATION;
		}
		NodeFunctions.nodeCreate(new Node<Object>(NodeType.NODE_TYPE_STRUCT, new Struct<Object>(name, bodyNode, accessSpecifier, classDefinition, parentClassNode), flags));
	}
	
	public static void makeUnionNode(String name, Node<Object> bodyNode) {
		int flags = 0;
		if (bodyNode == null) {
			flags |= NodeFlag.NODE_FLAG_IS_FORWARD_DECLARATION;
		}
		NodeFunctions.nodeCreate(new Node<Object>(NodeType.NODE_TYPE_UNION, new Union<Object>(name, bodyNode), flags));
	}
	
	public static void makeFunctionNode(Datatype<Node<Object>> retType, String name, Vector<Node<Object>> arguments, Node<Object> bodyN, AccessSpecifierFlag accessSpecifier, Node<Object> namespaceIdentifierNode) {
		// Legen eine Standard-Stackgröße von 8 Bytes an.
		Node<Object> funcNode = NodeFunctions.nodeCreate(new Node<Object>(NodeType.NODE_TYPE_FUNCTION, new Function<Object>(name, new FunctionArguments<Object>(arguments), bodyN, retType, DatatypeFunctions.DATA_SIZE_DDWORD, accessSpecifier, namespaceIdentifierNode)));
		@SuppressWarnings("unchecked")
		Function<Object> func = (Function<Object>) funcNode.getValue();
		func.getFrame().setElements(Vector.vectorCreate());
		func.setFunctionNode(funcNode);
	}
	
	public static void makeDeleteNode(Node<Object> expNode, boolean isArray) {
		NodeFunctions.nodeCreate(new Node<Object>(NodeType.NODE_TYPE_STATEMENT_DELETE, new Stmt<Object>(new DeleteStmt<Object>(expNode, isArray))));
	}
	
	public static void makeThrowInsideFunctionHeadNode(Vector<Datatype<Node<Object>>> exceptionClassesToThrowVector) {
		NodeFunctions.nodeCreate(new Node<Object>(NodeType.NODE_TYPE_THROW_INSIDE_FUNCTION_HEAD, new ThrowInsideFunctionHead<Object>(exceptionClassesToThrowVector)));
	}
	
	public static void makeThrowNode(Node<Object> expNode) {
		NodeFunctions.nodeCreate(new Node<Object>(NodeType.NODE_TYPE_STATEMENT_THROW, new Stmt<Object>(new ThrowStmt<Object>(expNode))));
	}
	
	public static void makeCatchNode(Node<Object> expNode, Node<Object> bodyNode) {
		NodeFunctions.nodeCreate(new Node<Object>(NodeType.NODE_TYPE_STATEMENT_CATCH, new Stmt<Object>(new CatchStmt<Object>(expNode, bodyNode))));
	}
	
	public static void makeTryCatchNode(Node<Object> bodyNode, Vector<Node<Object>> catchExpNodes) {
		NodeFunctions.nodeCreate(new Node<Object>(NodeType.NODE_TYPE_STATEMENT_TRY_CATCH, new Stmt<Object>(new TryCatchStmt<Object>(bodyNode, catchExpNodes))));
	}
	
	public static void makeSwitchNode(Node<Object> expNode, Node<Object> bodyNode, Vector<Node<Object>> cases, Node<Object> defaultCaseNode) {
		NodeFunctions.nodeCreate(new Node<Object>(NodeType.NODE_TYPE_STATEMENT_SWITCH, new Stmt<Object>(new SwitchStmt<Object>(expNode, bodyNode, cases, defaultCaseNode))));
	}
	
	public static void makeDoWhileNode(Node<Object> bodyNode, Node<Object> expNode) {
		NodeFunctions.nodeCreate(new Node<Object>(NodeType.NODE_TYPE_STATEMENT_DO_WHILE, new Stmt<Object>(new DoWhileStmt<Object>(bodyNode, expNode))));
	}
	
	public static void makeWhileNode(Node<Object> expNode, Node<Object> bodyNode) {
		NodeFunctions.nodeCreate(new Node<Object>(NodeType.NODE_TYPE_STATEMENT_WHILE, new Stmt<Object>(new WhileStmt<Object>(expNode, bodyNode))));
	}
	
	public static void makeForEachNode(Node<Object> initNode, Node<Object> loopNode, Node<Object> bodyNode) {
		NodeFunctions.nodeCreate(new Node<Object>(NodeType.NODE_TYPE_STATEMENT_FOR_EACH, new Stmt<Object>(new ForEachStmt<Object>(initNode, loopNode, bodyNode))));
	}
	
	public static void makeForNode(Node<Object> initNode, Node<Object> condNode, Node<Object> loopNode, Node<Object> bodyNode) {
		NodeFunctions.nodeCreate(new Node<Object>(NodeType.NODE_TYPE_STATEMENT_FOR, new Stmt<Object>(new ForStmt<Object>(initNode, condNode, loopNode, bodyNode))));
	}
	
	public static void makeReturnNode(Node<Object> expNode) {
		NodeFunctions.nodeCreate(new Node<Object>(NodeType.NODE_TYPE_STATEMENT_RETURN, new Stmt<Object>(new ReturnStmt<Object>(expNode))));
	}
	
	public static void makeElseNode(Node<Object> bodyNode) {
		NodeFunctions.nodeCreate(new Node<Object>(NodeType.NODE_TYPE_STATEMENT_ELSE, new Stmt<Object>(new ElseStmt<Object>(bodyNode))));
	}
	
	public static void makeIfNode(Node<Object> condNode, Node<Object> bodyNode, Node<Object> nextNode) {
		NodeFunctions.nodeCreate(new Node<Object>(NodeType.NODE_TYPE_STATEMENT_IF, new Stmt<Object>(new IfStmt<Object>(condNode, bodyNode, nextNode))));
	}
	
	public static void makeUnaryNode(String op, Node<Object> operandNode, int flags) {
		NodeFunctions.nodeCreate(new Node<Object>(NodeType.NODE_TYPE_UNARY, new Unary<Object>(op, operandNode, flags)));
	}
	
	public static void makeGenericTypenameNode(Vector<Object> genericsVector) {
		NodeFunctions.nodeCreate(new Node<Object>(NodeType.NODE_TYPE_GENERIC_TYPENAME, new GenericPlaceholders<Object>(genericsVector))); 	
	}
	
	public static void makeConcreteDefinitionOfGenericStructNode(Vector<Datatype<Node<Object>>> genericsVector) {
		NodeFunctions.nodeCreate(new Node<Object>(NodeType.NODE_TYPE_CONCRETE_DEFINITION_OF_GENERIC_STRUCT, new ConcreteDefinitionOfGenericStruct<Object>(genericsVector))); 	
	}
	
	// Gibt eine shallow-Copy des Knotens zurück, der die Methode aufruft.
	public static Node<Object> memcpy(Node<Object> node) {
		Node<Object> _node = new Node<Object>(node.getType(), node.getFlags(), node.getPos(), node.getBinded(), node.getValue());
		return _node;
	}
	
	public static Node<Object> nodeFromSym(Symbol<Object> sym) {
		if (sym.getType() != SymbolType.SYMBOL_TYPE_NODE) {
			return null;
		}
		return (Node<Object>) sym.getData();
	}
	
	public static Node<Object> nodeFromSymbol(CompileProcess currentProcess, String name, Scope<Object> scope) {
		Symbol<Object> sym = Symresolver.symresolverGetSymbol(currentProcess, name, scope, null);
		if (sym == null) {
			return null;
		}
		return NodeFunctions.nodeFromSym(sym);
	}
	
	// Gibt einen Struct-Knoten zurück, falls der übergebene Name in der Symboltabelle auf einen Struct-Knoten zeigt, sonst NULL.
	public static Node<Object> structNodeForName(CompileProcess currentProcess, String name, Scope<Object> scope) {
		Node<Object> node = NodeFunctions.nodeFromSymbol(currentProcess, name, scope);
		if (node == null) {
			return null;
		}
		if (node.getType() != NodeType.NODE_TYPE_STRUCT) {
			return null;
		}
		return node;
	}
	
	// Gibt einen Union-Knoten zurück, falls der übergebene Name in der Symboltabelle auf einen Union-Knoten zeigt, sonst NULL.
	public static Node<Object> unionNodeForName(CompileProcess currentProcess, String name, Scope<Object> scope) {
		Node<Object> node = NodeFunctions.nodeFromSymbol(currentProcess, name, scope);
		if (node == null) {
			return null;
		}
		if (node.getType() != NodeType.NODE_TYPE_UNION) {
			return null;
		}
		return node;
	}
	
	// Erstellt eine Shallow-Copy des übergebenen Knoten und pusht sie auf den Nodestack.
	public static Node<Object> nodeCreate(Node<Object> _node) {
		Node<Object> node = NodeFunctions.memcpy(_node);
		// Erlaubt uns zu sehen in welcher Function und welchem Körper der Knoten sich befindet.
		node.setBinded(new NodeBinded<Object>(Parser.parserCurrentBody, Parser.parserCurrentFunction));
		NodeFunctions.nodePush(node);
		return node;
	}
	
	public static boolean nodeIsStructOrUnion(Node<Object> node) {
		return node.getType() == NodeType.NODE_TYPE_STRUCT || node.getType() == NodeType.NODE_TYPE_UNION;
	}
	
	// Funktion gibt zurück, ob es eine Struktur oder Unionvariable ist, oder nicht.
	public static boolean nodeIsStructOrUnionVariable(Node<Object> node) {
		if (node.getType() != NodeType.NODE_TYPE_VARIABLE) {
			return false;
		}
		@SuppressWarnings("unchecked")
		Var<Object> var = (Var<Object>) node.getValue();
		return DatatypeFunctions.datatypeIsStructOrUnion(var.getType());
	}
	
	public static Node<Object> variableNode(Node<Object> node) {
		Node<Object> varNode = null;
		switch(node.getType()) {
			case NODE_TYPE_VARIABLE: {
				// Gibt den Variablenknoten der primitiven Variable zurück.
				varNode = node;
				break;
			}
			case NODE_TYPE_STRUCT: {
				// Gibt den Variablenknoten der Struct-Deklaration zurück.
				@SuppressWarnings("unchecked" )
				Struct<Object> struct = (Struct<Object>) node.getValue();
				varNode = struct.getVar();
				break;
			}
			case NODE_TYPE_UNION: {
				// Gibt den Variablenknoten der Union-Deklaration zurück.
				@SuppressWarnings("unchecked")
				Union<Object> union = (Union<Object>) node.getValue();
				varNode = union.getVar();
				break;
			}
		}
		return varNode;
	}
	
	// Gehört der übergebene Knoten zu einer primitiven Variable?
	public static boolean variableNodeIsPrimitive(Node<Object> node) {
		assert(node.getType() == NodeType.NODE_TYPE_VARIABLE);
		@SuppressWarnings("unchecked")
		Var<Object> var = (Var<Object>) node.getValue();
		return DatatypeFunctions.datatypeIsPrimitive(var.getType());
	}
	
	// Gibt die Variablenliste zurück oder den richtigen Variablenknoten abhängig davon, ob es eine Struktur/Union/Variable ist.
	public static Node<Object> variableNodeOrList(Node<Object> node) {
		if (node.getType() == NodeType.NODE_TYPE_VARIABLE_LIST) {
			return node;
		}
		return NodeFunctions.variableNode(node);
	}
	
	public static int functionNodeArgumentStackAddition(Node<Object> node) {
		assert(node.getType() == NodeType.NODE_TYPE_FUNCTION);
		@SuppressWarnings("unchecked")
		Function<Object> func = (Function<Object>) node.getValue();
		return func.getArgs().getStackAddition();
	}
	
	public static boolean nodeIsExpressionOrParentheses(Node<Object> node) {
		return node.getType() == NodeType.NODE_TYPE_EXPRESSION_PARENTHESES || node.getType() == NodeType.NODE_TYPE_EXPRESSION;
	}
	
	// Gibt zurück, ob der Knoten ein Wert ist, das heißt ob er prinzipiell in eine Variable geparsed werden kann.
	public static boolean nodeIsValueType(Node<Object> node) {
		return NodeFunctions.nodeIsExpressionOrParentheses(node) || node.getType() == NodeType.NODE_TYPE_IDENTIFIER || node.getType() == NodeType.NODE_TYPE_NUMBER || node.getType() == NodeType.NODE_TYPE_UNARY || node.getType() == NodeType.NODE_TYPE_TENARY || node.getType() == NodeType.NODE_TYPE_STRING;
	}
	
	public static boolean nodeIsExpression(Node<Object> node, String op) {
		if (node.getType() == NodeType.NODE_TYPE_EXPRESSION) {
			@SuppressWarnings("unchecked")
			Expression<Object> exp = (Expression<Object>) node.getValue();
			return SymbolTable.S_EQ(exp.getOp(), op);
		}
		return false;
	}
	
	public static boolean isArrayNode(Node<Object> node) {
		return NodeFunctions.nodeIsExpression(node, "[]");
	}
	
	public static boolean isNodeAssignment(Node<Object> node) {
		if (node.getType() != NodeType.NODE_TYPE_EXPRESSION) {
			return false;
		}
		@SuppressWarnings("unchecked")
		Expression<Object> exp = (Expression<Object>) node.getValue();
		return SymbolTable.S_EQ(exp.getOp(), "=") || SymbolTable.S_EQ(exp.getOp(), "+=") || SymbolTable.S_EQ(exp.getOp(), "-=") || SymbolTable.S_EQ(exp.getOp(), "/=") || SymbolTable.S_EQ(exp.getOp(), "*=") || SymbolTable.S_EQ(exp.getOp(), "<<=") || SymbolTable.S_EQ(exp.getOp(), ">>=");
	}
}