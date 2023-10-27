package parser;

import cleanCodeAnalyzer.CleanCodeAnalyzer;
import cleanCodeAnalyzer.CleanCodeObservation;
import compiler.CompileProcess;
import compiler.Compiler;

import helpers.Vector;
import lexer.SymbolTable;
import lexer.Token;
import parserDatatypes.Datatype;
import parserDatatypes.Function;
import parserDatatypes.FunctionFlag;
import parserDatatypes.Struct;
import parserDatatypes.Union;
import parserDatatypes.Var;

public class Symresolver {
	public static CleanCodeObservation<Object> newlineBeforeGlobalDefinedMethodFromOutsideStruct = null;
	
	public static void symresolverPushSymbol(CompileProcess compileProcess, Symbol<Object> sym) {
		compileProcess.getSymbols().getTable().vectorPush(sym);
	}
	
	// Initialisizert einen Symbolresolver
	public static void symresolverInitialize(CompileProcess compileProcess) {
		// Wir initialisieren einen Vektor, der es uns erlaubt, viele Symboltabellen anzulegen.
		compileProcess.getSymbols().setTable(Vector.vectorCreate());
	}

	// Gibt das Symbol aus der aktuellen Symboltabelle zurück, wenn der übergebene Name existiert.
	public static Symbol<Object> symresolverGetSymbol(CompileProcess compileProcess, String name, Scope<Object> currentScope, NodeType nodeType) {
		compileProcess.getSymbols().getTable().vectorSetPeekPointer(0);
		Symbol<Object> symbol = compileProcess.getSymbols().getTable().vectorPeekPtr();
		while(symbol != null) {
			// Wir haben das gesuchte Symbol gefunden
			if (SymbolTable.S_EQ(symbol.getName(), name) && symbol.getScope().equals(currentScope)) {
				if (nodeType == null) {
					break;
				}
				else {
					Node<Object> node = symbol.getData();
					if (nodeType == node.getType()) {
						break;
					}
				}
			}
			symbol = compileProcess.getSymbols().getTable().vectorPeekPtr();
		}
		if (symbol == null && currentScope != null) {
			return Symresolver.symresolverGetSymbol(compileProcess, name, currentScope.getParent(), nodeType);
		}
		return symbol;
	}
	
	// Gibt das Symbol mit dem übergebenen Namen zurück (falls vorhanden), aber nur, wenn es eine native Funktion ist.
	public static Symbol<Object> symresolverGetSymbolForNativeFunction(CompileProcess compileProcess, Token<Object> nameToken, Scope<Object> currentScope, Vector<Node<Object>> argumentsVec, boolean registering, FunctionFlag flags, Boolean isOverloadedConstructor) {
		String name = (String) nameToken.getValue();
		compileProcess.getSymbols().getTable().vectorSetPeekPointer(0);
		Symbol<Object> sym = compileProcess.getSymbols().getTable().vectorPeekPtr();
		while(sym != null) {
			if (sym.getType() != SymbolType.SYMBOL_TYPE_NATIVE_FUNCTION) {
				sym = compileProcess.getSymbols().getTable().vectorPeekPtr();
				continue;
			}
			Node<Object> funcNode = (Node<Object>) sym.getData();
			@SuppressWarnings("unchecked")
			Function<Object> func = (Function<Object>) funcNode.getValue();
			Vector<Node<Object>> argumentsVec2 = func.getArgs().getVector();
			// Wir möchten wissen, ob wir einen Konstruktor überladen, um daraus Erkenntnisse über die Sauberkeit des Codes zu ziehen (Body ist null bei Forward-Declaration).
			if (!(isOverloadedConstructor) && SymbolTable.S_EQ(sym.getName(), name) && sym.getScope().equals(currentScope) && func.getBodyN() != null && func.getFlags() == flags && func.getFlags() == FunctionFlag.FUNCTION_NODE_FLAG_IS_NATIVE_CONSTRUCTOR) {
				isOverloadedConstructor = true;
				CleanCodeAnalyzer.analyzeConstructorMethod(compileProcess, nameToken, funcNode);
			}
			// Wir haben das gesuchte Symbol gefunden
			if (SymbolTable.S_EQ(sym.getName(), name) && sym.getScope().equals(currentScope) && Symresolver.hasEqualFunctionArguments(argumentsVec, argumentsVec2) && func.getFlags() == flags) {
				break;
			}			
			sym = compileProcess.getSymbols().getTable().vectorPeekPtr();
		}
		// Wir suchen nach dem Symbol in allen Scopes, und wollen kein Neues anlegen (nur wenn wir ausschließlich nach dem Symbol suchen, sonst schauen wir nur im aktuellen Scope).
		if (sym == null && currentScope != null && registering == false) {
			return Symresolver.symresolverGetSymbolForNativeFunction(compileProcess, nameToken, currentScope.getParent(), argumentsVec, false, flags, isOverloadedConstructor);
		}
		if (sym == null) {
			return null;
		}
		return sym;
	}
	
	public static boolean hasEqualFunctionArguments(Vector<Node<Object>> argumentsVec1, Vector<Node<Object>> argumentsVec2) {
		if (argumentsVec1.vectorCount() != argumentsVec2.vectorCount()) {
			return false;
		}
		argumentsVec1.vectorSetPeekPointer(0);
		argumentsVec2.vectorSetPeekPointer(0);
		for(int i = 0; i < argumentsVec1.vectorCount(); i++) {
			@SuppressWarnings("unchecked")
			Var<Object> var1 = (Var<Object>) argumentsVec1.vectorPeekPtrAt(i).getValue();
			Datatype<Node<Object>> datatype1 = (Datatype<Node<Object>>) var1.getType();
			@SuppressWarnings("unchecked")
			Var<Object> var2 = (Var<Object>) argumentsVec2.vectorPeekPtrAt(i).getValue();
			Datatype<Node<Object>> datatype2 = (Datatype<Node<Object>>) var2.getType();
			// Falls der Name des Datentyps ungleich ist an der gleichen Stelle, sind die Funktionsparameter nicht identisch.
			if (!(SymbolTable.S_EQ(datatype1.getTypeStr(), datatype2.getTypeStr()))) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean symIsForwardDeclaration(Symbol<Object> sym) {
		Node<Object> possibleForwardDeclarationNode = (Node<Object>) sym.getData();
		@SuppressWarnings("unchecked")
		Function<Object> possibleForwardDeclaration = (Function<Object>) possibleForwardDeclarationNode.getValue();
		return possibleForwardDeclaration.getBodyN() == null;
	}
	
	public static boolean dataRepresentsForwardDeclaration(Object data) {
		@SuppressWarnings("unchecked")
		Node<Object> dataNode = (Node<Object>) data;
		@SuppressWarnings("unchecked")
		Function<Object> func = (Function<Object>) dataNode.getValue();
		return func.getBodyN() == null;
	}
	
	@SuppressWarnings("unchecked")
	public static Symbol<Object> symresolverRegisterFunctionSymbol(CompileProcess compileProcess, Token<Object> symToken, SymbolType type, Node<Object> data, Scope<Object> currentScope, Vector<Node<Object>> argumentsVec, boolean isFriendlyFunction, FunctionFlag flags) {
		String symName = (String) symToken.getValue();
		Symbol<Object> sym = Symresolver.symresolverGetSymbolForNativeFunction(compileProcess, symToken, currentScope, argumentsVec, true, flags, false);
		Function<Object> forwardDeclaration = null;
		Function<Object> definedFunction = null;
		// Existiert die Funktion bereits?
		if (sym != null) {
			// Ist es eine Forward-Declaration? Dann passen wir die Daten des bisherigen Eintrags an.
			if (Symresolver.symIsForwardDeclaration(sym)) {
				// Beschreiben die neuen Daten auch eine Forward-Declaration?
				if (Symresolver.dataRepresentsForwardDeclaration(data)) {
					Compiler.compilerError(compileProcess, "Sie können die Funktion \"" + symName + "\" nur in einer Forward-Declaration innerhalb eines Scopes deklarieren.\n");
				}
				forwardDeclaration = (Function<Object>) sym.getData().getValue();
				definedFunction = (Function<Object>) data.getValue();
				definedFunction.setHasForwardDeclaration(true);
				Symresolver.newlineBeforeGlobalDefinedMethodFromOutsideStruct = CleanCodeAnalyzer.analyzeFormattingForGlobalDefinedStructMethod(compileProcess, data, Symresolver.newlineBeforeGlobalDefinedMethodFromOutsideStruct);
				forwardDeclaration.setBodyN(definedFunction.getBodyN());
				return sym;
			}
			// Die Funktion existiert bereits, ist aber keine Forward-Declaration? Dann ist es ein Compiler-Error.
			Compiler.compilerError(compileProcess, "Sie können die Funktion \"" + symName + "\" nicht mit den gleichen Parametern überladen, die bereits eine gleichnamige andere Funktion im gleichen Scope bestitzt.\n");
		}
		if (SymbolTable.S_EQ(symName, "main") && currentScope.getParent() != null && !(isFriendlyFunction)) {
			Compiler.compilerError(compileProcess, "Die Funktion \"main\" kann nur auf dem globalen Scope angelegt werden.\n");
		}
		Function<Object> func = (Function<Object>) data.getValue();
		// Die Methodennamenanalyse beüglich Clean-Code macht für überladene Operatoren, Konstruktoren und Destruktoren keinen Sinn, da diese Bezeichner festgelegt sind.
		// Daher prüfen wir es nur für alle anderen Methoden.
		if (func.getFlags() == FunctionFlag.FUNCTION_NODE_FLAG_IS_NATIVE) {
			CleanCodeAnalyzer.analyzeMethodName(compileProcess, symToken, data, func.getRtype().getTypeStr(), Parser.currentSUDatatype);
		}
		// Haben wir kein Symbol gefunden? Dann erstellen wir ein neues Symbol.
		return Symresolver.symresolverRegisterSymbol(compileProcess, symName, type, data, currentScope);
	}
	
	// Legt ein neues Symbol in der aktuellen Symboltabelle an, falls der Name noch nicht existiert.
	public static Symbol<Object> symresolverRegisterSymbol(CompileProcess compileProcess, String symName, SymbolType type, Node<Object> data, Scope<Object> currentScope) {
		Symbol<Object> sym = new Symbol<Object>(symName, type, data, currentScope);
		Symresolver.symresolverPushSymbol(compileProcess, sym);
		return sym;
	}
	
	// Ist das Symbol ein Knoten, geben wir den Knoten zurück.
	public static Node<Object> symresolverNode(Symbol<Object> sym) {
		if (sym.getType() != SymbolType.SYMBOL_TYPE_NODE) {
			return null;
		}
		return (Node<Object>) sym.getData();
	}
	
	public static void symresolverBuildForVariableNode(CompileProcess compileProcess, Node<Object> node, Scope<Object> currentScope) {
		@SuppressWarnings("unchecked")
		Var<Object> var = (Var<Object>) node.getValue();
		Symresolver.symresolverRegisterSymbol(compileProcess, var.getName(), SymbolType.SYMBOL_TYPE_NODE, node, currentScope);
	}
	
	public static Symbol<Object> symresolverBuildForFunctionNode(CompileProcess compileProcess, Token<Object> nameToken, Node<Object> node, Scope<Object> currentScope, boolean isFriendlyFunction, FunctionFlag flags) {
		@SuppressWarnings("unchecked")
		Function<Object> func = (Function<Object>) node.getValue();
		return Symresolver.symresolverRegisterFunctionSymbol(compileProcess, nameToken, SymbolType.SYMBOL_TYPE_NATIVE_FUNCTION, node, currentScope, func.getArgs().getVector(), isFriendlyFunction, flags);
	}
	
	public static void symresolverBuildForStructureNode(CompileProcess compileProcess, Node<Object> node, Scope<Object> currentScope) {
		@SuppressWarnings("unchecked")
		Struct<Object> struct = (Struct<Object>) node.getValue();
		String identifier = struct.getName();
		// 1. Fall: Vorankündigung beim Parsen eines structs / einer Klasse, Pointer von Objekten der zu parsenden Klasse zu unterstützen.
		if (struct.getVar() == null) {
			Symresolver.symresolverRegisterSymbol(compileProcess, identifier, SymbolType.SYMBOL_TYPE_NODE, node, currentScope);
			return;
		}
		Symbol<Object> sym = Symresolver.symresolverGetSymbol(compileProcess, identifier, currentScope, NodeType.NODE_TYPE_STRUCT);
		Node<Object> structNode = null;
		if (sym != null) {
			structNode = (Node<Object>) sym.getData();
		}
		// 2. Fall: Forward Declaration. Wir haben noch keinen Platzhalter für die Struktur.
		if (node.getFlags() == NodeFlag.NODE_FLAG_IS_FORWARD_DECLARATION && structNode == null) {
			Symresolver.symresolverRegisterSymbol(compileProcess, identifier, SymbolType.SYMBOL_TYPE_NODE, node, currentScope);
			return;
		}
		else if (node.getFlags() == NodeFlag.NODE_FLAG_IS_FORWARD_DECLARATION && structNode != null) {
			Compiler.compilerError(compileProcess, "Für die Struktur oder Klasse \"" + identifier + "\" existiert bereits eine Forward-Declaration.\n");
		}
		// 3. Fall: Reguläre Struct / Klassenregistrierung mit Speicherung des vollwertigen Strukturknotens.
		//@SuppressWarnings("unchecked")
		//Struct<Object> struct = (Struct<Object>) node.getValue();
		//Symresolver.symresolverRegisterSymbol(compileProcess, struct.getName(), SymbolType.SYMBOL_TYPE_NODE, node, currentScope);
		else if (node.getFlags() != NodeFlag.NODE_FLAG_IS_FORWARD_DECLARATION && structNode != null) {
			if (struct.getBodyN() != null) {
				Compiler.compilerError(compileProcess, "Für die Struktur oder Klasse \"" + identifier + "\" existiert bereits eine Definition.\n");
			}
			sym.setData(node);
		}
	}
	
	public static void symresolverBuildForUnionNode(CompileProcess compileProcess, Node<Object> node, Scope<Object> currentScope) {
		// Wir registrieren keine Forward-Declarations.
		if (node.getFlags() == NodeFlag.NODE_FLAG_IS_FORWARD_DECLARATION) {
			return;
		}
		@SuppressWarnings("unchecked")
		Union<Object> union = (Union<Object>) node.getValue();
		Symresolver.symresolverRegisterSymbol(compileProcess, union.getName(), SymbolType.SYMBOL_TYPE_NODE, node, currentScope);
	}
	
	public static void symresolverBuildForNamespaceNode(CompileProcess compileProcess, Node<Object> node, Scope<Object> currentScope) {
		@SuppressWarnings("unchecked")
		Namespace<Object> namespace = (Namespace<Object>) node.getValue();
		int lastIndex = namespace.getIdentifierNodes().vectorCount() - 1;
		Symresolver.symresolverRegisterSymbol(compileProcess, (String) namespace.getIdentifierNodes().vectorPeekAt(lastIndex).getValue(), SymbolType.SYMBOL_TYPE_NODE, node, currentScope);
	}
	
	public static void symresolverBuildForNode(CompileProcess compileProcess, Token<Object> nameToken, Node<Object> node, Scope<Object> currentScope, FunctionFlag functionFlagIfFunctionSym) {
		// Hier machen wir eine Vorankündigung für Klassen und Strukturen, damit diese sich selbst beherbergen können.
		switch(node.getType()) {
			case NODE_TYPE_VARIABLE: {
				Symresolver.symresolverBuildForVariableNode(compileProcess, node, currentScope);
				break;
			}
			case NODE_TYPE_FUNCTION: {
				Symresolver.symresolverBuildForFunctionNode(compileProcess, nameToken, node, currentScope, false, functionFlagIfFunctionSym);
				break;
			}
			case NODE_TYPE_STRUCT: {
				Symresolver.symresolverBuildForStructureNode(compileProcess, node, currentScope);
				break;
			}
			case NODE_TYPE_UNION: {
				Symresolver.symresolverBuildForUnionNode(compileProcess, node, currentScope);
				break;
			}
			case NODE_TYPE_NAMESPACE: {
				Symresolver.symresolverBuildForNamespaceNode(compileProcess, node, currentScope);
			}
			// Wir ignorieren alle anderen Knotentypen, da sie keine Symbole werden können.
		}
	}
}
