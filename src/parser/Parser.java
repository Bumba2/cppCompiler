package parser;


import cleanCodeAnalyzer.CleanCodeAnalysisConcreteCategory;
import cleanCodeAnalyzer.CleanCodeAnalyzer;
import cleanCodeAnalyzer.CleanCodeObservation;
import cleanCodeAnalyzer.FunctionCall;
import compiler.CompileProcess;
import compiler.Compiler;
import helpers.Vector;
import lexer.Lexer;
import lexer.SymbolTable;
import lexer.Token;
import lexer.TokenType;
import parserStatements.Stmt;
import parserStatements.CaseStmt;
import parserDatatypes.AccessSpecifierFlag;
import parserDatatypes.Array;
import parserDatatypes.ArrayBrackets;
import parserDatatypes.ConcreteDefinitionOfGenericStruct;
import parserDatatypes.Datatype;
import parserDatatypes.DatatypeExpectedType;
import parserDatatypes.DatatypeFunctions;
import parserDatatypes.DatatypeType;
import parserDatatypes.Function;
import parserDatatypes.FunctionFlag;
import parserDatatypes.DatatypeFlag;
import parserDatatypes.GenericPlaceholders;
import parserDatatypes.Struct;
import parserDatatypes.Union;
import parserDatatypes.Var;
import parserDatatypes.VarList;
import parserDatatypes.VariableSize;
import parserFixup.DatatypeStructNodeFixPrivate;
import parserFixup.Fixup;
import parserFixup.FixupConfig;
import parserFixup.FixupSystem;

public class Parser {
	public static CompileProcess currentProcess;
	public static FixupSystem<Object> parserFixupSys;
	public static Token<Object> parserLastToken;
	public static Datatype<Node<Object>> currentSUDatatype = null;
	public static String currentEntityName = null; // Falls wir uns in einer Struktur, Klasse oder Union befinden. Wie heißt sie?
	public static Node<Object> parserCurrentBody = null; // aktueller Körperknoten, den wir uns merken.
	public static Node<Object> parserCurrentFunction = null;
	public static Node<Object> parserBlankNode = null; // Repräsentiert einen leeren Knoten.
	public static int x = 0; // Index von Customvariablennamen.
	public static boolean insideForEachLoop = false;
	public static boolean possibleFunctionCallInsideExpression = false;
	public static boolean identifierIsStructDeclarationMethodOrAssignment = false;
	public static String currentNamespace = null; // Wird etwas in der Form "std::cout" verwendet, so wird "std" zwischengespeichert.
	public static boolean functionIsFriendFlag = false;
	public static boolean varIsFriendFlag = false;
	public static boolean temporaryStdNamespaceFlag = false;
	public static boolean iostreamLibraryIsIncluded = false;
	public static boolean iomanipLibraryIsiIncluded = false;
	public static boolean possibleFunctionCallInsideExpressionInCoutStatementStart = false;
	public static boolean possibleFunctionCallInsideExpressionInCoutStatementEnd = false;
	public static boolean possibleArrayAccessInsideExpressionInCoutStatementStart = false;
	public static boolean possibleArrayAccessInsideExpressionInCoutStatementEnd = false;
	public static boolean[] coutFlags = new boolean[4];
	public static boolean[] cinFlags = new boolean [4];
	public static boolean insideFunctionArgumentParsing = false;
	public static boolean functionIsOperatorFlag = false;
	public static Vector<Object> currentValidGenericPlaceholders = null;
	public static boolean cinOrCoutStatementOverFlag = false;
	public static boolean isStructWithDefinedGenerics = false;
	public static boolean arrayExpressionFlag = false;
	public static boolean arrayExpressionIsNotFinished = false;
	public static boolean isConstructorCall = false;
	public static boolean arrayCppStyleFlag = false;
	public static boolean insideGenericsParsing = false;
	public static boolean insideThrowInsideFunctionHead = false;
	public static boolean insideVariableParsing = false;
	public static Integer forLoopStmtEndTokenVecIndex = null;
	public static Integer priorForLoopStmtEndTokenVecIndex = null;
	public static Node<Object> tempNamespaceIdentifierNode = null;
	public static boolean parsedFunctionCallAsVarInitialization = false;
	public static boolean isParsingStructFunction = false;
	public static boolean isParsingString = false;
	public static boolean structDeclarationWithoutParentheses = false;
	public static boolean insideParsingExpressionAsArrayBracketSize = false;
	public static String currentNamespaceIdentifierInDeclarationFromOutside = null;
	public static boolean declaredFunctionFromOutsideNamespace = false;
	public static boolean insideForLoopConditional = false;
	public static CleanCodeObservation<Object> currentStructOrUnionNameObservation = null;
	public static Integer functionEndLine = null;
	public static Integer functionStartLineTokenVecIndex = null;
	public static Integer functionEndLineTokenVecIndex = null;
	public static Integer priorStructEndLineTokenVecIndex = null;
	public static Integer priorStructLineTokenVecIndex = null;
	public static Integer structEndLineTokenVecIndex = null;
	public static Token<Object> currentDestructorTildeToken = null;
	public static Integer ifStmtEndTokenVecIndex = null;
	public static Integer priorIfStmtEndTokenVecIndex = null;
	public static Integer elseStmtEndTokenVecIndex = null;
	public static Integer priorElseStmtEndTokenVecIndex = null;
	public static Integer priorForEachLoopStmtEndTokenVecIndex = null;
	public static Integer forEachLoopStmtEndTokenVecIndex = null;
	public static Integer priorDoWhileLoopStmtEndTokenVecIndex = null;
	public static Integer doWhileLoopStmtEndTokenVecIndex = null;
	public static Integer whileLoopStmtEndTokenVecIndex = null;
	public static Integer priorWhileLoopStmtEndTokenVecIndex = null;
	public static Integer priorCatchStmtEndTokenVecIndex = null;
	public static Integer catchStmtEndTokenVecIndex = null;
	public static Integer priorTryCatchStmtEndTokenVecIndex = null;
	public static Integer tryCatchStmtEndTokenVecIndex = null;
	public static Integer priorCaseStmtEndTokenVecIndex = null;
	public static Integer caseStmtEndTokenVecIndex = null;
	public static Integer priorDefaultStmtEndTokenVecIndex = null;
	public static Integer defaultStmtEndTokenVecIndex = null;
	public static boolean tryCatchBlockFlagInsideFunction = false;
	public static Integer tempTokenVecIndex = null;
	public static int numberOfNestedStructs = 0;
	public static Integer priorTempAssignmentTokenIndex = null;
	public static Integer tempAssignmentTokenIndex = null;
	public static Integer tempHashTokenVecIndex = null;
	public static CleanCodeObservation<Object> noNewlinesBetweenLibraryDirectivesObservation = null;
	public static Integer tempStartNamespaceDefinitionTokenVecIndex = null;
	public static CleanCodeObservation<Object> noNewlinesBetweenNamespaceDefinitionsObservation = null;
	public static boolean insideForLoopInitializing = false;
	public static Vector<Node<Object>> functionNodeVec = null;
	
	public static History<Object> historyBegin(Integer flags) {
		History<Object> history = new History<Object>(flags);
		return history;
	}
	
	public static History<Object> historyDown(History<Object> history, int flags) {
		History<Object> newHistory = new History<Object>();
		newHistory.memcpy(history);
		newHistory.setFlags(flags);
		return newHistory;
	}
	
	public static ParserHistorySwitch<Object> parserNewSwitchStatement(History<Object> history) {
		history.set_switch(new ParserHistorySwitch<Object>(new HistoryCases<Object>()));
		history.get_switch().getCaseData().setCases(Vector.vectorCreate());
		history.setFlags(history.getFlags() | HistoryFlag.HISTORY_FLAG_IN_SWITCH_STATEMENT);
		return history.get_switch();
	}
	
	public static void parserEndSwitchStatement(ParserHistorySwitch<Object> switchHistory) {
		// Nichts passiert (brauchen einen Platzhalter, um zu wissen, dass das switch-Statement vorbei ist).
	}
	
	// Registriert einen neuen Fall im Fallvektor von History.
	public static void parserRegisterCase(History<Object> history, Node<Object> caseNode) {
		assert((history.getFlags() & HistoryFlag.HISTORY_FLAG_IN_SWITCH_STATEMENT) == 1);
		ParsedSwitchCase scase = new ParsedSwitchCase(0);
		@SuppressWarnings("unchecked")
		Stmt<Object> stmt = (Stmt<Object>) caseNode.getValue();
		@SuppressWarnings("unchecked")
		CaseStmt<Object> caseStmt = (CaseStmt<Object>) stmt.getStmt();
		Long caseIndex = (Long) caseStmt.getExp().getValue();
		// In unserem Subset von C++ können die Fälle nur Integers sein. Keine Strings oder longs, etc.
		scase.setIndex(caseIndex.intValue());
		history.get_switch().getCaseData().getCases().vectorPush(caseNode);
	}
	
	public static ParserScopeEntity<Object> parserNewScopeEntity(Node<Object> node, int stackOffset, int flags) {
		ParserScopeEntity<Object> entity = new ParserScopeEntity<Object>(node, stackOffset, flags);
		return entity;
	}
	
	// Findet die letzte Entität des aktuellen Scopes (stoppt beim Global Scope, falls zuvor keine vorhanden) -> nützlich um lokale Variablen zu finden.
	@SuppressWarnings("unchecked")
	public static ParserScopeEntity<Object> parserScopeLastEntityStopGlobalScope() {
		return (ParserScopeEntity<Object>) Scope.scopeLastEntityStopAt(Parser.currentProcess, Parser.currentProcess.getScope().getRoot());
	}
	
	public static void parserScopeNew() {
		Scope.scopeNew(Parser.currentProcess, 0);
	}
	
	public static void parserScopeFinish() {
		Scope.scopeFinish(Parser.currentProcess);
	}
	
	// Gibt den letzten erstellten Scope zurück.
	@SuppressWarnings("unchecked")
	public static ParserScopeEntity<Object> parserScopeLastEntity() {
		return (ParserScopeEntity<Object>) Scope.scopeLastEntity(Parser.currentProcess);
	}
	
	// Bekommt den Variablennoten und die Variablengröße übergeben.
	// Schiebt die übergebene Größe der Variable in den aktuellen Scope (bzw dessen Stackvektor).
	public static void parserScopePush(ParserScopeEntity<Object> entity, int size) {
		Scope.scopePush(Parser.currentProcess, entity, size);
	}
	
	public static void parserIgnoreNlOrComment(Token<Object> token) {
		while (token != null && Token.tokenIsNlOrCommentOrNewlineSeperator(token)) {
			// Überspringe den Token (Parser interessiert sich nicht für Newline oder Comment Tokens)
			currentProcess.getTokenVec().vectorPeek();
			token = currentProcess.getTokenVec().vectorPeekNoIncrement();
		}
	}
	
	// Nimmt das nächste Token aus dem Tokenvektor des Kompilierungsprozesses.
	public static Token<Object> tokenNext() {
		// Wir nutzen hier vectorPeekNoIncrement statt vectorPeek, da parserIgnoreNewlineOrComment
		// das lastToken ändern könnte.
		Token<Object> nextToken = currentProcess.getTokenVec().vectorPeekNoIncrement();
		Parser.parserIgnoreNlOrComment(nextToken);
		// Wir müssen wissen, wo wir uns im Kompilierungsprozess befinden.
		if (nextToken != null) {
			Parser.currentProcess.setPos(nextToken.getPos());
		}
		Parser.parserLastToken = nextToken;
		return currentProcess.getTokenVec().vectorPeek();
	}
	
	public static Token<Object> tokenPeekNext() {
		Token<Object> nextToken = currentProcess.getTokenVec().vectorPeekNoIncrement();
		Parser.parserIgnoreNlOrComment(nextToken);
		// Wieder geben wir nicht nur nextToken zurück, da parserIgnoreNlOrComment das nextToken verändern könnte nach dem Aufruf.
		return currentProcess.getTokenVec().vectorPeekNoIncrement();
	}
	
	// Liest ein an einem bestimmten Index, ausgehend vom aktuellen Peekindex aus.
	public static Token<Object> tokenPeekAt(int index) {
		int peekIndex = Parser.currentProcess.getTokenVec().getPeekIndex();
		Token<Object> token = Parser.tokenPeekNext();
		for(int i = 0; i < index + 1; i++) {
			Parser.parserIgnoreNlOrComment(token);
			token = Parser.currentProcess.getTokenVec().vectorPeek();
			if (Token.tokenIsNlOrCommentOrNewlineSeperator(token)) {
				token = Parser.currentProcess.getTokenVec().vectorPeek();
			}
		}
		Parser.currentProcess.getTokenVec().vectorSetPeekPointer(peekIndex);
		return token;
	}
	
	public static boolean tokenNextIsOperator(String op) {
		Token<Object> token = Parser.tokenPeekNext();
		return Token.tokenIsOperator(token, op);
	}
	
	public static boolean tokenNextIsKeyword(String keyword) {
		Token<Object> token = Parser.tokenPeekNext();
		return Token.tokenIsKeyword(token, keyword);
	}
	
	public static boolean tokenNextIsSymbol(char c) {
		Token<Object> token = Parser.tokenPeekNext();
		return Token.tokenIsSymbol(token, c);
	}
	
	public static boolean tokenNextAtIsOperator(int index, String op) {
		Token<Object> token = Parser.tokenPeekAt(index);
		return Token.tokenIsOperator(token, op);
	}
	
	// Erwarte ein Semikolon.
	public static void expectSym(char c) {
		Token<Object> nextToken = Parser.tokenNext();
		if (nextToken != null && nextToken.getValue() instanceof String) {
			Compiler.compilerError(Parser.currentProcess, "Erwarten das Symbol \'" + c + "\', aber \"" + nextToken.getValue() + "\" wurde eingegeben.\n");
		}
		if (nextToken == null || nextToken.getType() != TokenType.TOKEN_TYPE_SYMBOL || (Character) nextToken.getValue() != c) {
			if (nextToken != null) Compiler.compilerError(Parser.currentProcess, "Erwarten das Symbol \'" + c + "\', aber \'" + (Character) nextToken.getValue() + "\' wurde eingegeben.\n");
			Compiler.compilerError(Parser.currentProcess, "Erwarten das Symbol \'" + c + "\', aber etwas wurde eingegeben.\n");
		}
	}
	
	// Erwarte den übergebenen Operator.
	public static void expectOp(String op) {
		Token<Object> nextToken = Parser.tokenNext();
		if (nextToken == null || nextToken.getType() != TokenType.TOKEN_TYPE_OPERATOR || !(SymbolTable.S_EQ((String) nextToken.getValue(), op))) {
			if (nextToken != null) Compiler.compilerError(Parser.currentProcess, "Erwarten den Operator \"" + op + "\", aber \"" + (String) nextToken.getValue() + "\" wurde eingegeben.\n");
			Compiler.compilerError(Parser.currentProcess, "Erwarten den Operator \"" + op + "\", aber etwas anderes wurde eingegeben.\n");
		}
	}
	
	public static void expectKeyword(String keyword) {
		Token<Object> nextToken = Parser.tokenNext();
		if (nextToken == null || nextToken.getType() != TokenType.TOKEN_TYPE_KEYWORD || !(SymbolTable.S_EQ((String) nextToken.getValue(), keyword))) {
			if (nextToken != null) Compiler.compilerError(Parser.currentProcess, "Erwarten das Keyword \"" + keyword + "\", aber \"" + (String) nextToken.getValue() + "\" wurde eingegeben.\n");
			Compiler.compilerError(Parser.currentProcess, "Erwarten das Keyword \"" + keyword + "\", aber etwas anderes wurde eingegeben.\n");
		}
	}
	
	public static void markCurrentTokenVecIndexForCleanCodeAnalysis(BodyFlag bodyFlag, boolean singleStatementBody) {
		if (bodyFlag == BodyFlag.BODY_IS_FLAG_FUNCTION) {
			Parser.functionEndLineTokenVecIndex = Parser.currentProcess.getTokenVec().getPeekIndex();
			Parser.functionEndLine = Parser.tokenPeekNext().getPos().getLineNumber();
		}
		else if (bodyFlag == BodyFlag.BODY_IS_FLAG_IF_STATEMENT) {
			Parser.priorIfStmtEndTokenVecIndex = Parser.ifStmtEndTokenVecIndex;
			if (singleStatementBody) {
				Parser.ifStmtEndTokenVecIndex = Parser.currentProcess.getTokenVec().getPeekIndex() - 1;
			}
			else {
				Parser.ifStmtEndTokenVecIndex = Parser.currentProcess.getTokenVec().getPeekIndex();
			}
		}
		else if (bodyFlag == BodyFlag.BODY_IS_FLAG_ELSE_STATEMENT) {
			Parser.priorElseStmtEndTokenVecIndex = Parser.elseStmtEndTokenVecIndex;
			if (singleStatementBody) {
				Parser.elseStmtEndTokenVecIndex = Parser.currentProcess.getTokenVec().getPeekIndex() - 1;
			}
			Parser.elseStmtEndTokenVecIndex = Parser.currentProcess.getTokenVec().getPeekIndex();
		}
		else if (bodyFlag == BodyFlag.BODY_IS_FLAG_FOR_LOOP_STATEMENT) {
			Parser.priorForLoopStmtEndTokenVecIndex = Parser.forLoopStmtEndTokenVecIndex;
			Parser.forLoopStmtEndTokenVecIndex = Parser.currentProcess.getTokenVec().getPeekIndex();
		}
		else if (bodyFlag == BodyFlag.BODY_IS_FLAG_FOR_EACH_LOOP_STATEMENT) {
			Parser.priorForEachLoopStmtEndTokenVecIndex = Parser.forEachLoopStmtEndTokenVecIndex;
			Parser.forEachLoopStmtEndTokenVecIndex = Parser.currentProcess.getTokenVec().getPeekIndex();
		}
		else if (bodyFlag == BodyFlag.BODY_IS_FLAG_DO_WHILE_LOOP_STATEMENT) {
			Parser.priorDoWhileLoopStmtEndTokenVecIndex = Parser.doWhileLoopStmtEndTokenVecIndex;
			Parser.doWhileLoopStmtEndTokenVecIndex = Parser.currentProcess.getTokenVec().getPeekIndex();
		}
		else if (bodyFlag == BodyFlag.BODY_IS_FLAG_WHILE_LOOP_STATEMENT) {
			Parser.priorWhileLoopStmtEndTokenVecIndex = Parser.whileLoopStmtEndTokenVecIndex;
			Parser.whileLoopStmtEndTokenVecIndex = Parser.currentProcess.getTokenVec().getPeekIndex();
		}
		else if (bodyFlag == BodyFlag.BODY_IS_FLAG_CATCH_STATEMENT) {
			Parser.priorCatchStmtEndTokenVecIndex = Parser.catchStmtEndTokenVecIndex;
			Parser.catchStmtEndTokenVecIndex = Parser.currentProcess.getTokenVec().getPeekIndex();
		}
		else if (bodyFlag == BodyFlag.BODY_IS_FLAG_TRY_CATCH_STATEMENT) {
			Parser.priorTryCatchStmtEndTokenVecIndex = Parser.tryCatchStmtEndTokenVecIndex;
			Parser.tryCatchStmtEndTokenVecIndex = Parser.currentProcess.getTokenVec().getPeekIndex();
		}
		else if (bodyFlag == BodyFlag.BODY_IS_FLAG_CASE_STATEMENT) {
			Parser.priorCaseStmtEndTokenVecIndex = Parser.caseStmtEndTokenVecIndex;
			if (singleStatementBody) {
				Parser.caseStmtEndTokenVecIndex = Parser.currentProcess.getTokenVec().getPeekIndex() - 1;
			}
			else {
				Parser.caseStmtEndTokenVecIndex = Parser.currentProcess.getTokenVec().getPeekIndex();
			}
		}
		else if (bodyFlag == BodyFlag.BODY_IS_FLAG_DEFAULT_STATEMENT) {
			Parser.priorDefaultStmtEndTokenVecIndex = Parser.defaultStmtEndTokenVecIndex;
			if (singleStatementBody) {
				Parser.defaultStmtEndTokenVecIndex = Parser.currentProcess.getTokenVec().getPeekIndex() - 1;
			}
			else {
				Parser.defaultStmtEndTokenVecIndex = Parser.currentProcess.getTokenVec().getPeekIndex();
			}
		}
		else if (bodyFlag == BodyFlag.BODY_IS_FLAG_STRUCT || bodyFlag == BodyFlag.BODY_IS_FLAG_CLASS) {
			Parser.priorStructEndLineTokenVecIndex = Parser.structEndLineTokenVecIndex;
			Parser.structEndLineTokenVecIndex = Parser.currentProcess.getTokenVec().getPeekIndex();
		}
	}
	
	public static void parseSingleTokenToNode() {
		Token<Object> token = Parser.tokenNext();
		Node<Object> node = null;
		switch(token.getType()) {
			case TOKEN_TYPE_NUMBER: {
				node = NodeFunctions.nodeCreate(new Node<Object>(NodeType.NODE_TYPE_NUMBER, token.getValue()));
				break;
			}
			case TOKEN_TYPE_IDENTIFIER: {
				node = NodeFunctions.nodeCreate(new Node<Object>(NodeType.NODE_TYPE_IDENTIFIER, token.getValue()));
				break;
			}
			case TOKEN_TYPE_STRING: {
				node = NodeFunctions.nodeCreate(new Node<Object>(NodeType.NODE_TYPE_STRING, token.getValue()));
				break;
			}
			case TOKEN_TYPE_KEYWORD: {
				if (SymbolTable.S_EQ((String) token.getValue(), "string")) {
					node = NodeFunctions.nodeCreate(new Node<Object>(NodeType.NODE_TYPE_IDENTIFIER, token.getValue()));
					break;
				}
			}
			default: {
				Compiler.compilerError(Parser.currentProcess, "Das ist kein einzelner Token, der in einen Knoten umgewandelt werden kann.");
			}
		}
	}
	
	// Ruft parseExpressionable() auf (So wird im Beispiel 50+20 aus der 20 auch ein Numbertoken, falls wir ein Number Token finden).
	public static void parseExpressionableForOp(History<Object> history, String op) {
		Parser.parseExpressionable(history);
	}
	
	public static ExpressionableOpPrecedenceGroup parserGetPrecedenceForOperator(String op) {
		Expressionable expressionable = new Expressionable();
		ExpressionableOpPrecedenceGroup groupOut = null;
		for(int i = 0; i < Expressionable.TOTAL_OPERATORS_GROUPS; i++) {
			for(int b = 0; b < expressionable.getOpPrecedence()[i].getOperators().length; b++) {
				String _op = expressionable.getOpPrecedence()[i].getOperators()[b];
				if (SymbolTable.S_EQ(op, _op)) {
					groupOut = expressionable.getOpPrecedence()[i];
					return groupOut;
				}
			}
		}
		// Wir haben den Operator in unserer Prioritätentabelle nicht gefunden und geben null zurück.
		return groupOut;
	}
	
	// Die Funktion gibt die Priorität zurück bezüglich zweier Operatoren (ist links prioritär zu rechts zu behandeln?).
	public static boolean parserLeftOpHasPriority(String opLeft, String opRight) {
		ExpressionableOpPrecedenceGroup groupLeft = null;
		ExpressionableOpPrecedenceGroup groupRight = null;
		if (SymbolTable.S_EQ(opLeft, opRight)) {
			return false;
		}
		groupLeft = Parser.parserGetPrecedenceForOperator(opLeft);
		groupRight = Parser.parserGetPrecedenceForOperator(opRight);
		Integer precedenceLeft = null;
		Integer precedenceRight = null;
		if (groupLeft != null && groupRight != null) {
			precedenceLeft = groupLeft.getPrecedence();
			precedenceRight = groupRight.getPrecedence();
		}
		else return false;
		if (groupLeft.getType() == AssociativityType.ASSOCIATIVITY_RIGHT_TO_LEFT) {
			return false;
		}
		return (precedenceLeft <= precedenceRight);
	}
	
	// Reordert Knoten von Expressions. Macht aus 50*E(20+120) E(50*20)+120.
	public static void parserNodeShiftChildrenLeft(Node<Object> node) {
		assert(node.getType() == NodeType.NODE_TYPE_EXPRESSION);
		@SuppressWarnings("unchecked")
		Expression<Object> exp = (Expression<Object>) node.getValue();
		assert(exp.getRight().getType() == NodeType.NODE_TYPE_EXPRESSION);
		@SuppressWarnings("unchecked")
		Expression<Object> rightExp = (Expression<Object>) exp.getRight().getValue();
		
		String rightOp = rightExp.getOp();
		// Wir erstellen einen neuen Expression-Node, bestehend aus der linken Kindknotens des übergebenen Knotens (links),
		// und dem linken Kindknoten der Expression, die das rechte Kind des übergebenen Knotens ist -> Wir verlagern die Kindknoten eine Stelle nach links.
		// Bei 50*E(20+120) wird 50 der linke Knoten und 20 der rechte Knoten einer neuen Expression, * wird der Operator dieser Epxression.
		Node<Object> newExpLeftNode = exp.getLeft();
		Node<Object> newExpRightNode = rightExp.getLeft();
		NodeFunctions.makeExpNode(newExpLeftNode, newExpRightNode, exp.getOp());
		
		// (50*20)
		Node<Object> newLeftOperand = NodeFunctions.nodePop();
		// 120
		Node<Object> newRightOperand = rightExp.getRight();
		exp.setLeft(newLeftOperand);
		exp.setRight(newRightOperand);
		exp.setOp(rightOp);
	}
	
	// Das machen wir, um Arrays zu handhaben. Nehmen wir das Beispiel "struct book{char name[30] books[0];};",
	// in der main-Funktion wird eine Funktion Test so aufgerufen: "return test(56, books[0].name, 1000);".So haben wir vor
	// dem Reordering die Struktur im Abstract-Syntax-Tree (E steht für Expression):
	// Ebene 0: E (Root Expression) -> Operator ist ein ".".
	//		Ebene 1: linker Knoten: E -> Operator sind die Array-Klammern "[]".
	//			Ebene 2: linker Knoten: "books" -> Bezeichner.
	//			Ebene 2: rechter Knoten: Bracket. Enthält in "Inner" die Zahl "0" (Länge des Arrays).
	//		Ebene 1: rechter Knoten: E - Operator ist ein ",".
	//			Ebene 2:linker Knoten: "name" -> Bezeichner.
	//			Ebene 2: rechter Knoten: Bracket. Enthalt in "Inner" die Zahl "1000".
	// Gerade die 1000 beim rechten Knoten in Ebene 2 ist überhaupt nicht sinnvoll, der Compiler interpretiert die Zahl aber so.
	// Nach dem Reordering durch die Funktion parserNodeMoveRightLeftToLeft ergibt sich folgende Struktur:
	// Ebene 0: E (Root Expression) -> Operator ist ein ",".
	// 		Ebene 1: linker Knoten: E -> Operator ist ein ".".
	//			Ebene 2: linker Knoten: E -> Operator sind die "[]" Array-Klammern.
	//				Ebene 3: linker Knoten: "books" -> Bezeichner.
	//				Ebene 3: rechnter Knoten: Bracket. Enthält in "Inner" die Zahl "0" (Länge des Arrays).
	//			Ebene 2: rechter Knoten: "name" -> Bezeichner.
	//		Ebene 1: rechter Knoten: Zahl "1000".
	// Wir machen aus "E(books[0]).E(name, 1000)", was falsch ist, ein E(books[0].name, 1000).
	public static void parserNodeMoveRightLeftToLeft(Node<Object> node) {
		@SuppressWarnings("unchecked")
		Expression<Object> exp = (Expression<Object>) node.getValue();
		@SuppressWarnings("unchecked")
		Expression<Object> expRight = (Expression<Object>) exp.getRight().getValue();
		// Wir erstellen einen neuen Expression-Knoten. Der linke Knoten des übergebenen Knotens bleibt der linke Knoten.
		// Wir nehmen den linken Knoten des rechten Knotens der Expression als rechten Knoten der neuen Expression.
		NodeFunctions.makeExpNode(exp.getLeft(), expRight.getLeft(), exp.getOp());
		Node<Object> completedNode = NodeFunctions.nodePop();
		// Wir müssen uns noch um den rechten Knoten der ursprünglichen Expression kümmern.
		String newOp = expRight.getOp();
		exp.setLeft(completedNode);
		exp.setRight(expRight.getRight());
		exp.setOp(newOp);
	}
	
	public static void parserReorderExpression(Node<Object> node) {
		if (node.getType() != NodeType.NODE_TYPE_EXPRESSION) {
			return;
		}
		// Wenn wir einen linken und einen rechten Knoten haben, die keine Expressions sind, dann verlasse die Funktion.
		// -> Dann können wir keine Expression mit dem Operator bilden, da wir keine Expressions vorliegen haben.
		@SuppressWarnings("unchecked")
		Expression<Object> exp = (Expression<Object>) node.getValue();
		if (exp.getLeft().getType() != NodeType.NODE_TYPE_EXPRESSION && exp.getRight() != null) {
			if (exp.getRight().getType() != NodeType.NODE_TYPE_EXPRESSION) {
				return;
			}
		}
		// 50*E(30+20) -> linker Knoten ist keine Expression (50), aber rechter Knoten ist eine Expression (50+20).
		// 50*EXPRESSION(30+20)
		// (50*EXPRESSION) ist ebenefalls eine Expression, Root-Expression genannt).
		// Die Root-Expression hat einen linken Operanden 50 und einen rechten Operanden Expression in diesem Fall.
		// (50*30)+20 wird gebildet, da Multiplikation Priorität besitzt.
		if (exp.getLeft().getType() != NodeType.NODE_TYPE_EXPRESSION && exp.getRight().getType() == NodeType.NODE_TYPE_EXPRESSION) {
			@SuppressWarnings("unchecked")
			Expression<Object> expRight = (Expression<Object>) exp.getRight().getValue();
			String rightOp = expRight.getOp(); // +
			String leftOp = exp.getOp(); // *
			if (Parser.parserLeftOpHasPriority(leftOp, rightOp)) {
				// Eingang: 50*E(20+120)
				// Ausgang: E(50*20)+120
				Parser.parserNodeShiftChildrenLeft(node);
				// Rekursive Calls, da der geänderte Absract-Syntax-Tree wieder auf Neuordnung geprüft werden muss.
				Parser.parserReorderExpression(exp.getLeft());
				Parser.parserReorderExpression(exp.getRight());
			}
		}
		if ((NodeFunctions.isArrayNode(exp.getLeft()) && NodeFunctions.isNodeAssignment(exp.getRight()) || ((NodeFunctions.nodeIsExpression(exp.getLeft(), "()")) && NodeFunctions.nodeIsExpression(exp.getRight(), ",")))) {
			Parser.parserNodeMoveRightLeftToLeft(node);
		}
	}
	
	public static boolean parserIsUnaryOperator(String op) {
		return SymbolTable.isUnaryOperator(op);
	}
	
	public static void parseForIndirectionUnary() {
		int depth = Parser.parserGetPointerDepth();
		Parser.parseExpressionable(Parser.historyBegin(ExpressionFlag.EXPRESSION_IS_UNARY));
		Node<Object> unaryOperandNode = NodeFunctions.nodePop();
		NodeFunctions.makeUnaryNode("*", unaryOperandNode, 0);
		Node<Object> unaryNode = NodeFunctions.nodePop();
		@SuppressWarnings("unchecked")
		Unary<Object> unary = (Unary<Object>) unaryNode.getValue();
		unary.getIndirection().setPointerDepth(depth);
		NodeFunctions.nodePush(unaryNode);
	}
	
	public static void parseForNormalUnary() {
		String unaryOp = (String) Parser.tokenNext().getValue();
		Parser.parseExpressionable(Parser.historyBegin(ExpressionFlag.EXPRESSION_IS_UNARY));
		Node<Object> unaryOperandNode = NodeFunctions.nodePop();
		NodeFunctions.makeUnaryNode(unaryOp, unaryOperandNode, 0);
	}
	
	public static void parseForUnary() {
		Token<Object> unaryOpToken = Parser.tokenPeekNext();
		String unaryOp = (String) unaryOpToken.getValue();
		if (SymbolTable.opIsIndirection(unaryOp)) {
			Parser.parseForIndirectionUnary();
			return;
		}
		Parser.parseForNormalUnary();
		if (SymbolTable.S_EQ(unaryOp, "!")) {
			Node<Object> unaryNode = NodeFunctions.nodePeek();
			CleanCodeAnalyzer.analyzeNegativeUnaryOperator(Parser.currentProcess, unaryOpToken, unaryNode, Parser.currentSUDatatype, Parser.parserCurrentFunction);
		}
		Parser.parserDealWithAdditionalExpression();
	}
	
	public static void parseForLeftOperandedUnary(Node<Object> leftOperandNode, String unaryOp) {
		NodeFunctions.makeUnaryNode(unaryOp, leftOperandNode, UnaryFlag.UNARY_FLAG_IS_LEFT_OPERANDED_UNARY);
	}
	
	// Für normale Expressions wie 50+20
	public static void parseExpNormal(History<Object> history) {
		Token<Object> opToken = Parser.tokenPeekNext();
		String op = (String) opToken.getValue();
		Node<Object> nodeLeft = NodeFunctions.nodePeekExpressionableOrNull(); // Schauen uns den letzten Knoten auf dem Nodestack an.
		// Ist es ein Namensraum?
		Node<Object> priorTempNamespaceIdentifierNode = Parser.tempNamespaceIdentifierNode;
		if (SymbolTable.S_EQ(op, "::")) {
			Parser.tempNamespaceIdentifierNode = nodeLeft;
		}
		// Ist der Knoten nicht in eine Expression überführbar, können wir hier nichts damit anfangen.
		// nodeLeft wird also im  Beispiel 50+20 zu 50.
		if (nodeLeft == null) {
			if (!(Parser.parserIsUnaryOperator(op))) {
				Compiler.compilerError(Parser.currentProcess, "Die gegebene Expression besitzt keinen linken Operanden.\n");
			}
			Parser.parseForUnary();
			return;
		}
		// Wirf den Operatorknoten ab (er ist in opToken zwischengespeichert)
		Parser.tokenNext(); 
		// Wirf den leftNode ab (haben ihn schon gecheckt).
		NodeFunctions.nodePop();
		// Ist es ein linker Unary Operator?
		if (SymbolTable.isLeftOperandedUnaryOperator(op)) {
			Parser.parseForLeftOperandedUnary(nodeLeft, op);
			return;
		}
		nodeLeft.setFlags(NodeFlag.NODE_FLAG_INSIDE_EXPRESSION);
		if (Parser.tokenPeekNext().getType() == TokenType.TOKEN_TYPE_OPERATOR) {
			if (SymbolTable.S_EQ((String) Parser.tokenPeekNext().getValue(), "(")) {
				Parser.parseForParentheses(Parser.historyDown(history, history.getFlags() | HistoryFlag.HISTORY_FLAG_PARENTHESES_IS_NOT_A_FUNCTION_CALL));
			}
			else if (Parser.parserIsUnaryOperator((String) Parser.tokenPeekNext().getValue())) {
				Parser.parseForUnary();
			}
			else {
				Compiler.compilerError(Parser.currentProcess, "Für den Operator \"" + (String) Parser.tokenPeekNext().getValue() + "\" werden zwei Operatoren bei der gegebenen Expression erwartet.\n");
			}
		}
		else {
			// Operator parsen
			Parser.parseExpressionableForOp(Parser.historyDown(history, history.getFlags()), op);
		}
		// Das gleiche wie zuvor beim linken Knoten (wir nehmen den rechten Knoten, die 20, vom Nodestack).
		Node<Object> nodeRight = NodeFunctions.nodePop();
		nodeRight.setFlags(NodeFlag.NODE_FLAG_INSIDE_EXPRESSION);
		
		// Erstelle den Expressionnode aus den beiden zwischengespeicherten Knoten und dem Operator und pushe den neuen Knoten auf den Stack.
		NodeFunctions.makeExpNode(nodeLeft, nodeRight, op);
		Node<Object> expNode = NodeFunctions.nodePop();
		// Neuordnen der Expression (z. B. weil Multiplikation Priorität vor Addition hat).
		Parser.parserReorderExpression(expNode);
		NodeFunctions.nodePush(expNode); // Zurück in den Baum pushen nach dem neu-Ordnen.
		Parser.tempNamespaceIdentifierNode = priorTempNamespaceIdentifierNode;
	}
	
	public static void parserDealWithAdditionalExpression() {
		if (Parser.tokenPeekNext().getType() == TokenType.TOKEN_TYPE_OPERATOR) {
			Parser.parseExpressionable(Parser.historyBegin(0));
		}
	}
	
	// Handhabt die Klammern einer Expression.
	public static void parseForParentheses(History<Object> history) {
		Token<Object> parenthesisToken = Parser.tokenPeekNext();
		Parser.expectOp("(");
		// 1. Fall: Ist es ein Cast?
		if (Parser.tokenPeekNext().getType() == TokenType.TOKEN_TYPE_KEYWORD) {
			Parser.parseForCast();
			return;
		}
		// 2. Fall: Kein Cast.
		Node<Object> leftNode = null;
		Node<Object> tmpNode = NodeFunctions.nodePeekOrNull();
		// Beispiel: "test(50+20)" -> Wir holen uns den Identifier test mit nodePeekOrNull().
		// Falls der temp-Knoten, nach dem wir gepeeked haben, ein Value ist (das heißt, in eine
		// Variable geparsed werden kann, und das kann der Idenentifier "test"), dann sollte
		// die Variable leftNode auf diesen Knoten gesetzt werden. Dann werfen wir den Identifier
		// vom Stack.
		if (tmpNode != null && NodeFunctions.nodeIsValueType(tmpNode)) {
			leftNode = tmpNode;
			NodeFunctions.nodePop();
		}
		// Setze einen Pointer auf den leeren Knoten, der beim Parsen initialisiert wurde.
		Node<Object> expNode = Parser.parserBlankNode;
		// Solange wir keine schließende Klammer finden, parsen wir die Expression innerhalb der Klammern.
		// Haben wir etwas wie "(50+20)", ist zu diesem Zeitpunkt noch "50+20)" übrig.
		// Die 50 ist keine ')', sodass parseExpressionableRoot die "50+20" parsed.
		// Den Expression-Knoten speichern wir zwischen und nehmen ihn vom Stack.
		if (!(Parser.tokenNextIsSymbol(')'))) {
			Parser.parseExpressionableRoot(Parser.historyBegin(0));
			expNode = NodeFunctions.nodePop();
		}
		// Nun erwarten wir nur noch ein ')'.
		Parser.expectSym(')');
		// Falls es einen linken Knoten gibt, werfen wir den Klammer-Knoten aus, den wir gerade erstellt haben.
		// Beim Beispiel "test(50+20)" wurde "test" zum linken Knoten (dem Identifier) und (50+20) zu einem Expression-Parentheses-Node.
		NodeFunctions.makeExpParenthesesNode(expNode);
		if (leftNode != null) {
			Node<Object> parenthesesNode = NodeFunctions.nodePop();
			NodeFunctions.makeExpNode(leftNode, parenthesesNode, "()");
			CleanCodeAnalyzer.analyzeNullValuesInMethodCalls(Parser.currentProcess, parenthesisToken, NodeFunctions.nodePeek(), Parser.currentSUDatatype, Parser.parserCurrentFunction);
		}
		// Haben wir so etwas wie "test(50+20)+90+30+40" haben wir weitere Expressions und müssen diese Parsen.
		Parser.parserDealWithAdditionalExpression();
	}
	
	public static void parseForComma(History<Object> history) {
		// Überspringe das Komma.
		Parser.tokenNext();
		// Wirf den linken Knoten vom Stack.
		// Haben wir eine Aufzählung wie "50, 20" nehmen wir die 50 vom Stack.
		Node<Object> leftNode = NodeFunctions.nodePop();
		// Parse den rechten Knoten.
		Parser.parseExpressionableRoot(history);
		Node<Object> rightNode = NodeFunctions.nodePop();
		// Erstelle einen neuen Expression-Knoten.
		NodeFunctions.makeExpNode(leftNode, rightNode, ",");
	}
	
	// Beispiel: "int abc[50];"
	public static void parseForArray(History<Object> history) {
		// Wirf abc vom Stack.
		Node<Object> leftNode = NodeFunctions.nodePeekOrNull();
		if (leftNode != null) {
			NodeFunctions.nodePop();
		}
		// Wirf die linke eckige Klammer vom Stack.
		Parser.expectOp("[");
		// Parse die Expression in den eckigen Klammern (hier 50).
		boolean priorParserinsideParsingExpressionAsArrayBracketSize = Parser.insideParsingExpressionAsArrayBracketSize;
		Parser.insideParsingExpressionAsArrayBracketSize = true;
		Parser.parseExpressionable(history);
		Parser.insideParsingExpressionAsArrayBracketSize = priorParserinsideParsingExpressionAsArrayBracketSize;
		// Wirf die rechte eckige Klammer vom Stack.
		Parser.expectSym(']');
		if (Parser.possibleArrayAccessInsideExpressionInCoutStatementEnd) {
			Parser.handleStdCout(Parser.tokenPeekNext());
			Parser.possibleArrayAccessInsideExpressionInCoutStatementEnd = false;
		}
		// Wirf die Expression innerhalb der eckigen Klammern vom Stack und erstelle damit einen Klammerknoten.
		Node<Object> expNode = NodeFunctions.nodePop();
		NodeFunctions.makeBracketNode(expNode);
		// Wirf den Klammer-Knoten vom Stack und erstelle einen Expression-Knoten.
		if (leftNode != null) {
			Node<Object> bracketNode = NodeFunctions.nodePop();
			NodeFunctions.makeExpNode(leftNode, bracketNode, "[]");
		}
		if (Parser.tokenNextIsOperator("[")) {
			Parser.arrayExpressionIsNotFinished = true;
		}
		else if (Parser.tokenPeekNext().getType() == TokenType.TOKEN_TYPE_OPERATOR) {
			Parser.arrayExpressionFlag = true;
		}
	}
	
	public static void parseForCast() {
		// Zu diesem Zeitpunkt haben wir bereits die "(" des Casts geparsed.
		// Zum Beispiel "(char)" kommt als "char)" bei dieser Funktion an.
		// Das heißt wir können parseDatatype rufen und müssen uns nur noch um die rechte Klammer kümmern.
		Datatype<Node<Object>> dtype = new Datatype<Node<Object>>();
		Parser.parseDatatype(dtype);
		Parser.expectSym(')');
		// Ein Cast ist immer Teil einer Expression.
		Parser.parseExpressionable(Parser.historyBegin(0));
		// Wirf den Operand vom Stack und parse den Cast in einen Cast-Knoten.
		Node<Object> operandNode = NodeFunctions.nodePop();
		NodeFunctions.makeCastNode(dtype, operandNode);
	}
	
	// Die Funktion parsed Operators und merged sie mit den zugehörigen Operanden.
	public static int parseExp(History<Object> history) {
		// Finden wir eine linke Klammer, parsen wir die Klammern.
		if (SymbolTable.S_EQ((String) Parser.tokenPeekNext().getValue(), "(")) {
			Parser.parseForParentheses(history);
		}
		else if (SymbolTable.S_EQ((String) Parser.tokenPeekNext().getValue(), "[")) {
			Parser.parseForArray(history);
		}
		// Finden wir ein Fragezeichen, parsen wir nach einem Tenary.
		else if (SymbolTable.S_EQ((String) Parser.tokenPeekNext().getValue(), "?")) {
			Parser.parseForTenary(history);
		}
		// Finden wir ein Komma, parsen wir ein Komma.
		else if (SymbolTable.S_EQ((String) Parser.tokenPeekNext().getValue(), ",")) {
			Parser.parseForComma(history);
		}
		// Sonst parsen wir die Expression ohne Klammern.
		else {
			Parser.parseExpNormal(history);
		}
		return 0;
	}
	
	public static void parseIdentifier(History<Object> history) {
		assert(Parser.tokenPeekNext().getType() == TokenType.TOKEN_TYPE_IDENTIFIER);
		Parser.parseSingleTokenToNode();
	}
	
	public static void parseDatatypeModifiers(Datatype<Node<Object>> dtype) {
		int startTokenVecIndex = Parser.currentProcess.getTokenVec().getPeekIndex();
		Token<Object> token = Parser.tokenPeekNext();
		int index = 0;
		while(token != null && token.getType() == TokenType.TOKEN_TYPE_KEYWORD) {
			// Verlassen der While-Schleife, falls wir keinen Modifier mehr finden.
			if(!(SymbolTable.isKeywordVariableModifier((String) token.getValue()))) {
				break;
			}
			// Falls wir einen Modifier gefunden haben, setzen wir die entsprechende Flag im Datatype-Objekt.
			if (SymbolTable.S_EQ((String) token.getValue(), "signed")) {
				dtype.setFlags(dtype.getFlags() | DatatypeFlag.DATATYPE_FLAG_IS_SIGNED);
			}
			else if (SymbolTable.S_EQ((String) token.getValue(), "unsigned")) {
				dtype.setFlags(dtype.getFlags() & ~DatatypeFlag.DATATYPE_FLAG_IS_SIGNED);
			}
			else if (SymbolTable.S_EQ((String) token.getValue(), "static")) {
				dtype.setFlags(dtype.getFlags() | DatatypeFlag.DATATYPE_FLAG_IS_STATIC);
			}
			else if (SymbolTable.S_EQ((String) token.getValue(), "const")) {
				dtype.setFlags(dtype.getFlags() | DatatypeFlag.DATATYPE_FLAG_IS_CONST);
			}
			else if (SymbolTable.S_EQ((String) token.getValue(), "extern")) {
				dtype.setFlags(dtype.getFlags() | DatatypeFlag.DATATYPE_FLAG_IS_EXTERN);
			}
			else if (SymbolTable.S_EQ((String) token.getValue(), "__ignore_typecheck__")) {
				dtype.setFlags(dtype.getFlags() | DatatypeFlag.DATATYPE_FLAG_IGNORE_TYPE_CHECKING);
			}
			if (index == 0) {
				Parser.functionStartLineTokenVecIndex = Parser.currentProcess.getTokenVec().getPeekIndex();
				dtype.setStartToken(token);
				dtype.setStartTokenVecIndex(startTokenVecIndex);
				Parser.tempTokenVecIndex = null;
				index++;
			}
			Parser.tokenNext(); // Wirf das letzte Token ab.
			token = Parser.tokenPeekNext();
		}
	}
	
	// Die Funktion schaut ob es ein Datatype-Token ist und falls ja, schaut sie ob es noch ein zweites Datatypetoken gibt.
	// Die Werte werden dann in die übergebenen "leeren" Token kopiert.
	public static void parserGetDatatypeTokens(Token<Object> datatypeToken, Token<Object> datatypeSecondaryToken) {
		Token<Object> tmpToken = null;
		if (Parser.identifierIsStructDeclarationMethodOrAssignment) {
			Parser.functionStartLineTokenVecIndex = Parser.currentProcess.getTokenVec().getPeekIndex();
			tmpToken = new Token<Object>(TokenType.TOKEN_TYPE_KEYWORD, "struct");
			if (tmpToken != null) {
				tmpToken.copyTokenValues(datatypeToken);
			}
			Parser.identifierIsStructDeclarationMethodOrAssignment = false;
			return;
		}
		tmpToken = null;
		if (Parser.tempTokenVecIndex == null) {
			Parser.tempTokenVecIndex = Parser.currentProcess.getTokenVec().getPeekIndex();
		}
		// Müssen wir nebenbei führen, da Destruktoren keinen Datentyp verwenden.
		Parser.functionStartLineTokenVecIndex = Parser.currentProcess.getTokenVec().getPeekIndex();
		tmpToken = Parser.tokenNext();
		if (tmpToken.getType() == TokenType.TOKEN_TYPE_IDENTIFIER && SymbolTable.S_EQ((String) tmpToken.getValue(), "std")) {
			Parser.temporaryStdNamespaceFlag = true;
			Parser.expectOp("::");
			tmpToken = Parser.tokenNext();
		}
		if (tmpToken != null) tmpToken.copyTokenValues(datatypeToken);
		else datatypeToken.setValue(null);
		Token<Object> nextToken = Parser.tokenPeekNext();
		if (Token.tokenIsPrimitiveKeyword(nextToken)) {
			nextToken.copyTokenValues(datatypeSecondaryToken);
			Parser.tokenNext();
		}
	}
	
	public static DatatypeExpectedType parserDatatypeExpectedForTypeString(String str) {
		DatatypeExpectedType type = DatatypeExpectedType.DATA_TYPE_EXPECT_PRIMITIVE;
		if (SymbolTable.S_EQ(str, "union")) {
			type = DatatypeExpectedType.DATA_TYPE_EXPECT_UNION;
		}
		else if (SymbolTable.S_EQ(str, "struct")) {
			type = DatatypeExpectedType.DATA_TYPE_EXPECT_STRUCT;
		}
		else if (SymbolTable.S_EQ(str, "class")) {
			type = DatatypeExpectedType.DATA_TYPE_EXPECT_CLASS;
		}
		return type;
	}
	
	// Erstellt einen Index anhand der statischen Variable x, die bei 0 beginnt und pro Typ um eins inkrementiert wird.
	public static int parserGetRandomTypeIndex() {
		Parser.x++;
		return x;
	}
	
	public static Token<Object> parserBuildRandomTypeName() {
		String sval = "customtypename_";
		sval += Parser.parserGetRandomTypeIndex();
		Token<Object> token = Lexer.tokenCreate(new Token<Object>(TokenType.TOKEN_TYPE_IDENTIFIER, sval));
		return token;
	}
	
	public static int parserGetPointerDepth() {
		int depth = 0;
		while(Parser.tokenNextIsOperator("*")) {
			depth++;
			Parser.tokenNext();
		}
		return depth;
	}
	
	public static void parsePointerDepthAfterGenerics(Datatype<Node<Object>> dtype) {
		Parser.isStructWithDefinedGenerics = false;
		int pointerDepth = Parser.parserGetPointerDepth();
		if (pointerDepth > 0) {
			dtype.setFlags(dtype.getFlags() | DatatypeFlag.DATATYPE_FLAG_IS_POINTER);
			dtype.setPointerDepth(pointerDepth);
		}
	}
	
	public static boolean parserDatatypeIsSecondaryAllowed(DatatypeExpectedType expectedType) {
		return expectedType == DatatypeExpectedType.DATA_TYPE_EXPECT_PRIMITIVE;
	}
	
	// Nur diese primitiven Datentypen als erster Datentyp dürfen einen zweiten Datentyp besitzen.
	public static boolean parserDatatypeIsSecondaryAllowedForType(String type) {
		return SymbolTable.S_EQ(type, "long") || SymbolTable.S_EQ(type, "short") || SymbolTable.S_EQ(type, "double") || SymbolTable.S_EQ(type, "float");
	}
	
	public static void parserDatatypeAdjustSizeForSecondary(Datatype<Node<Object>> datatype, Token<Object> datatypeSecondaryToken) {
		// 1. Fall: Der zweite Datentyp existiert nicht.
		if (datatypeSecondaryToken.getValue() == null) {
			return;
		}
		// 2. Fall: Zweiter Datentyp existiert.
		Datatype<Node<Object>> secondaryDatatype = new Datatype<Node<Object>>();
		Token<Object> dummyDatatypeToken = new Token<Object>();
		Parser.parserDatatypeInitTypeAndSizeForPrimitive(datatypeSecondaryToken, dummyDatatypeToken, secondaryDatatype);
		datatype.setSecondary(secondaryDatatype);
		datatype.setSize(datatype.getSize() + datatype.getSecondary().getSize()); // Bytegrößen werden addiert.
		datatype.setFlags(datatype.getFlags() | DatatypeFlag.DATATYPE_FLAG_IS_SECONDARY);
	}
	
	// Initialisierung primitiver Datentypen
	public static void parserDatatypeInitTypeAndSizeForPrimitive(Token<Object> datatypeToken, Token<Object> datatypeSecondaryToken, Datatype<Node<Object>> datatypeOut) {
		// Darf der erste Datentyp keinen zweiten Datentyp besitzen und wir haben trotzdem einen zweiten Datentyp?
		if (!(Parser.parserDatatypeIsSecondaryAllowedForType((String) datatypeToken.getValue())) && datatypeSecondaryToken.getValue() != null) {
			Compiler.compilerError(Parser.currentProcess, "Sie dürfen an dieser Stelle keinen zweiten Datentyp verwenden für den bereitgestellten Datentyp " + (String) datatypeToken.getValue() + ".\n");
		}
		if (SymbolTable.S_EQ((String) datatypeToken.getValue(), "void")) {
			datatypeOut.setType(DatatypeType.DATA_TYPE_VOID);
			datatypeOut.setSize(DatatypeFunctions.DATA_SIZE_ZERO);
		}
		else if (SymbolTable.S_EQ((String) datatypeToken.getValue(), "char")) {
			datatypeOut.setType(DatatypeType.DATA_TYPE_CHAR);
			datatypeOut.setSize(DatatypeFunctions.DATA_SIZE_BYTE);
		}
		else if (SymbolTable.S_EQ((String) datatypeToken.getValue(), "short")) {
			datatypeOut.setType(DatatypeType.DATA_TYPE_SHORT);
			datatypeOut.setSize(DatatypeFunctions.DATA_SIZE_WORD);
		}
		else if (SymbolTable.S_EQ((String) datatypeToken.getValue(), "int")) {
			datatypeOut.setType(DatatypeType.DATA_TYPE_INTEGER);
			datatypeOut.setSize(DatatypeFunctions.DATA_SIZE_DWORD);
		}
		else if (SymbolTable.S_EQ((String) datatypeToken.getValue(), "long")) {
			datatypeOut.setType(DatatypeType.DATA_TYPE_LONG);
			datatypeOut.setSize(DatatypeFunctions.DATA_SIZE_DWORD);
		}
		else if (SymbolTable.S_EQ((String) datatypeToken.getValue(), "float")) {
			datatypeOut.setType(DatatypeType.DATA_TYPE_FLOAT);
			datatypeOut.setSize(DatatypeFunctions.DATA_SIZE_DWORD);
		}
		else if (SymbolTable.S_EQ((String) datatypeToken.getValue(), "double")) {
			datatypeOut.setType(DatatypeType.DATA_TYPE_DOUBLE);
			datatypeOut.setSize(DatatypeFunctions.DATA_SIZE_DWORD);
		}
		else if (SymbolTable.S_EQ((String) datatypeToken.getValue(), "string")) {
			if (!(Namespace.namespaceOrMemberIsDefined(Parser.currentProcess, "std", "string")) && !(Parser.temporaryStdNamespaceFlag)) {
				Compiler.compilerError(Parser.currentProcess, "Der Datentyp \"string\" wird erst unterstützt, wenn der Namensraum \"std\" definiert ist.\n");
			}
			Parser.temporaryStdNamespaceFlag = false;
			datatypeOut.setType(DatatypeType.DATA_TYPE_STRING);
			datatypeOut.setSize(DatatypeFunctions.sizeOfString((String) Parser.tokenPeekNext().getValue()));
		}
		else if (SymbolTable.S_EQ((String) datatypeToken.getValue(), "bool")) {
			datatypeOut.setType(DatatypeType.DATA_TYPE_BOOL);
			datatypeOut.setSize(DatatypeFunctions.DATA_SIZE_BYTE);
		}
		else if (SymbolTable.S_EQ((String) datatypeToken.getValue(), "auto")) {
			Compiler.compilerError(Parser.currentProcess, "Der Datentyp \"auto\" wird von diesem Compiler noch nicht unterstützt.\n");
		}
		else {
			Compiler.compilerError(Parser.currentProcess, "COMPILER-FEHLER: Nicht valider primitiver Datentyp.\n");
		}
		// Anpassen des Speicherplatzes für die primitiven Datentypen
		// Wenn beispielsweise ein long long übergeben wird, dann wird die Größe des Datentyps von 4 zu 8 Byte angepsst.
		Parser.parserDatatypeAdjustSizeForSecondary(datatypeOut, datatypeSecondaryToken);
	}
	
	// Gibt die Größe einer Struktur zurück (in Byte).
	public static int sizeOfStruct(String structName) {
		Symbol<Object> sym = Symresolver.symresolverGetSymbol(Parser.currentProcess, structName, Scope.scopeCurrent(Parser.currentProcess), NodeType.NODE_TYPE_STRUCT);
		// 1. Fall: Keine Struktur mit dem übergebenen Namen vorhanden.
		if (sym == null) {
			return 0;
		}
		// 2. Fall: Es ist eine Struktur mit dem übergebenen Namen vorhanden, und das Symbol repräsentiert einen Knoten.
		assert(sym.getType() == SymbolType.SYMBOL_TYPE_NODE);
		Node<Object> node = (Node<Object>) sym.getData();
		assert(node.getType() == NodeType.NODE_TYPE_STRUCT);
		@SuppressWarnings("unchecked")
		Struct<Object> struct = (Struct<Object>) node.getValue();
		// Es ist nur eine Forward-Declaration vorhanden.
		if (struct.getBodyN() == null) return 0;
		@SuppressWarnings("unchecked")
		Body<Object> body = (Body<Object>) struct.getBodyN().getValue();
		return body.getSize().getVariableSize();
	}
	
	// Gibt die Größe einer Union zurück (in Byte).
		public static int sizeOfUnion(String unionName) {
			Symbol<Object> sym = Symresolver.symresolverGetSymbol(Parser.currentProcess, unionName, Scope.scopeCurrent(Parser.currentProcess), NodeType.NODE_TYPE_UNION);
			// 1. Fall: Keine Union mit dem übergebenen Namen vorhanden.
			if (sym == null) {
				return 0;
			}
			// 2. Fall: Es ist eine Union mit dem übergebenen Namen vorhanden, und das Symbol repräsentiert einen Knoten.
			assert(sym.getType() == SymbolType.SYMBOL_TYPE_NODE);
			@SuppressWarnings("unchecked")
			Node<Object> node = (Node<Object>) sym.getData();
			assert(node.getType() == NodeType.NODE_TYPE_UNION);
			@SuppressWarnings("unchecked")
			Union<Object> union = (Union<Object>) node.getValue();
			// Es ist nur eine Forward-Declaration vorhanden.
			if (union.getBodyN() == null) return 0;
			@SuppressWarnings("unchecked")
			Body<Object> body = (Body<Object>) union.getBodyN().getValue();
			return body.getSize().getVariableSize();
		}
	
	public static void parserDatatypeInitTypeAndSize(Token<Object> datatypeToken, Token<Object> datatypeSecondaryToken, Datatype<Node<Object>> datatypeOut, int pointerDepth, DatatypeExpectedType expectedType) {
		if (!(Parser.parserDatatypeIsSecondaryAllowed(expectedType)) && datatypeSecondaryToken.getValue() != null) {
			Compiler.compilerError(Parser.currentProcess, "Sie haben einen nicht validen zweiten Datentyp bereitgestellt.\n");
		}
		
		switch(expectedType) {
			case DATA_TYPE_EXPECT_PRIMITIVE: {
				Parser.parserDatatypeInitTypeAndSizeForPrimitive(datatypeToken, datatypeSecondaryToken, datatypeOut);
				break;
			}
			case DATA_TYPE_EXPECT_STRUCT: {
				datatypeOut.setType(DatatypeType.DATA_TYPE_STRUCT);
				datatypeOut.setSize(Parser.sizeOfStruct((String) datatypeToken.getValue()));
				datatypeOut.setStructOrUnionNode(NodeFunctions.structNodeForName(Parser.currentProcess, (String) datatypeToken.getValue(), Scope.scopeCurrent(Parser.currentProcess)));
				break;
			}
			case DATA_TYPE_EXPECT_CLASS: {
				datatypeOut.setType(DatatypeType.DATA_TYPE_CLASS);
				datatypeOut.setSize(Parser.sizeOfStruct((String) datatypeToken.getValue()));
				datatypeOut.setStructOrUnionNode(NodeFunctions.structNodeForName(Parser.currentProcess, (String) datatypeToken.getValue(), Scope.scopeCurrent(Parser.currentProcess)));
				break;
			}
			case DATA_TYPE_EXPECT_UNION: {
				datatypeOut.setType(DatatypeType.DATA_TYPE_UNION);
				datatypeOut.setSize(Parser.sizeOfUnion((String) datatypeToken.getValue()));
				datatypeOut.setStructOrUnionNode(NodeFunctions.unionNodeForName(Parser.currentProcess, (String) datatypeToken.getValue(), Scope.scopeCurrent(Parser.currentProcess)));
				break;
			}
			default: {
				Compiler.compilerError(Parser.currentProcess, "COMPILER-FEHLER: Nicht unterstützte Datentyperwartung.\n");
				break;
			}
		}
		if (pointerDepth > 0) {
			datatypeOut.setFlags(datatypeOut.getFlags() | DatatypeFlag.DATATYPE_FLAG_IS_POINTER);
			datatypeOut.setPointerDepth(pointerDepth);
		}
	}
	
	public static void parserDatatypeInit(Token<Object> datatypeToken, Token<Object> datatypeSecondaryToken, Datatype<Node<Object>> datatypeOut, int pointerDepth, DatatypeExpectedType expectedType) {
		Parser.parserDatatypeInitTypeAndSize(datatypeToken, datatypeSecondaryToken, datatypeOut, pointerDepth, expectedType);
		datatypeOut.setTypeStr((String) datatypeToken.getValue());
		
		if (SymbolTable.S_EQ((String) datatypeToken.getValue(), "long") && datatypeSecondaryToken.getValue() != null && SymbolTable.S_EQ((String) datatypeSecondaryToken.getValue(), "long")) {
			Compiler.compilerWarning(Parser.currentProcess, "Unser Compiler unterstützt keine 64 bit longs, daher wird Ihr long long üblicherweise mit 32 bits angelegt.\n");
			datatypeOut.setSize(DatatypeFunctions.DATA_SIZE_DWORD);
		}
	}
	
	public static void parseDatatypeType(Datatype<Node<Object>> dtype) {
		// Erstellen von leeren Tokens, deren Werte erst in parserGetDatatypeTokens gesetzt werden.
		Token<Object> datatypeToken = new Token<Object>();
		Token<Object> datatypeSecondaryToken = new Token<Object>();
		if (!(Parser.identifierIsStructDeclarationMethodOrAssignment) && Parser.isStructDatatypeType((String) Parser.tokenPeekNext().getValue())) {
			Parser.identifierIsStructDeclarationMethodOrAssignment = true;
		}
		// 1. Fall: Primitiver Datentyp
		Parser.parserGetDatatypeTokens(datatypeToken, datatypeSecondaryToken);
		DatatypeExpectedType expectedType = null;
		if (datatypeToken.getValue() instanceof String) {
			expectedType = Parser.parserDatatypeExpectedForTypeString((String) datatypeToken.getValue());
		}
		else {
			Compiler.compilerError(Parser.currentProcess, "Es wird ein gültiger Bezeichner erwartet, aber \'" + (char) datatypeToken.getValue() + "\' wurde eingegeben.\n");
		}
		// 2. Fall: Struct (Class) oder Union
		if (DatatypeFunctions.datatypeIsStructOrUnionForName((String) datatypeToken.getValue())) {
			// Beim Beispiel "struct abc" wird hier der Bezeichner "abc" geparsed.
			// Wenn das nächste Token ein Identifier ist, ist dieser der wahre Datentyp-Token.
			// Der datatypeToken wird von "struct" zu "abc" verändert.
			if (Parser.tokenPeekNext().getType() == TokenType.TOKEN_TYPE_IDENTIFIER) {
				if (dtype.getStartToken() == null) {
					Parser.tempTokenVecIndex = Parser.currentProcess.getTokenVec().getPeekIndex();
					dtype.setStartTokenVecIndex(Parser.tempTokenVecIndex);
				}
				datatypeToken = Parser.tokenNext();
			}
			// Funktioniert aber nicht für kompliziertere Beispiele wie "struct {int x;} abc;".
			// Diese Struktur hat keinen Namen, und wird im else-Fall gehandelt.
			else {
				datatypeToken = Parser.parserBuildRandomTypeName();
				dtype.setFlags(dtype.getFlags() | DatatypeFlag.DATATYPE_FLAG_STRUCT_UNION_NO_NAME);
			}
			Parser.currentStructOrUnionNameObservation = CleanCodeAnalyzer.analyzeStructOrUnionName(Parser.currentProcess, datatypeToken, Parser.currentSUDatatype);
		}
		// 3. Fall: Pointer (falls wir nicht noch Generics parsen müssen).
		// int**
		int pointerDepth = 0;
		if (!((Parser.isStructWithDefinedGenerics))) {
			pointerDepth = Parser.parserGetPointerDepth();
		}
		if (dtype.getStartToken() == null) {
			dtype.setStartToken(datatypeToken);
			if (dtype.getStartTokenVecIndex() == null) {
				dtype.setStartTokenVecIndex(Parser.tempTokenVecIndex);
			}
			Parser.tempTokenVecIndex = null;
		}
		Parser.parserDatatypeInit(datatypeToken, datatypeSecondaryToken, dtype, pointerDepth, expectedType);
	}
	
	public static void parseUnaryOperators(Datatype<Node<Object>> dtype) {
		if (Parser.tokenPeekNext().getType() == TokenType.TOKEN_TYPE_OPERATOR && SymbolTable.isUnaryOperator((String) Parser.tokenPeekNext().getValue()))  {
			String unaryOperator = (String) Parser.tokenNext().getValue();
			dtype.setUnaryOperator(unaryOperator);
		}
	}
	
	public static void parseCppStyleArrayBrackets(Datatype<Node<Object>> dtype) {
		if (!(tokenNextIsOperator("["))) {
			return;
		}
		int numberOfArrayBrackets = 0;
		Token<Object> leftBracketToken = Parser.tokenPeekNext();
		while(leftBracketToken.getType() == TokenType.TOKEN_TYPE_OPERATOR) {
			if (Token.tokenIsOperator(leftBracketToken, "[")) {
				Parser.tokenNext();
				Token<Object> rightBracketToken = Parser.tokenNext();
				if (rightBracketToken.getType() == TokenType.TOKEN_TYPE_SYMBOL && (char) rightBracketToken.getValue() == ']') {
					numberOfArrayBrackets++;
				}
				else {
					Compiler.compilerError(Parser.currentProcess, "Bei Arrays im C++-Style werden auf der linken Seite des Zuweisungsstatements nur leere eckige Klammern erwartet, aber etwas anderes wurde angegeben.\n");
				}
				leftBracketToken = Parser.tokenPeekNext();
			}
		}
		dtype.setCppStyleLeftSideOfAssignmentBrackets(numberOfArrayBrackets);
	}
	
	public static void parseDatatype(Datatype<Node<Object>> dtype) {
		dtype.setFlags(dtype.getFlags() | DatatypeFlag.DATATYPE_FLAG_IS_SIGNED);
		Parser.parseDatatypeModifiers(dtype);
		Parser.parseDatatypeType(dtype);
		Parser.parseDatatypeModifiers(dtype);
		Parser.parseGenerics(null, false, dtype, false);
		if (Parser.isStructWithDefinedGenerics && !(Parser.insideGenericsParsing)) {
			Parser.parsePointerDepthAfterGenerics(dtype);
		}
		Parser.parseUnaryOperators(dtype);
		Parser.parseCppStyleArrayBrackets(dtype);
	}
	
	// "int int" ist beispielsweise kein valider Datentyp. Wir müssen checken ob der Typ int nach einem validen ersten Datentyp gesetzt wurde.
	public static boolean parserIsIntValidAfterDatatype(Datatype<Node<Object>> dtype) {
		return dtype.getType() == DatatypeType.DATA_TYPE_LONG || dtype.getType() == DatatypeType.DATA_TYPE_FLOAT || dtype.getType() == DatatypeType.DATA_TYPE_DOUBLE;
	}
		
	// Falls jemand "long int abc;" angibt, wird das int ignoriert.
	// Das liegt daran, dass ein long int ebenso 8 Byte groß´ist wie ein long, sie sind also sehr identisch.
	public static void parserIgnoreInt(Datatype<Node<Object>> dtype) {
		if (!(Token.tokenIsKeyword(Parser.tokenPeekNext(), "int"))) {
			// Kein Integer zum ignorieren vorhanden.
			return;
		}
		// Steht ein valider Datentyp vor dem int?
		if (!(Parser.parserIsIntValidAfterDatatype(dtype))) {
			Compiler.compilerError(currentProcess, "Sie haben einen zweiten \"int\" Typ bereitgestellt, jedoch wird das mit der aktuellen Abkürzung nicht unterstützt.\n");
		}
		// Ignoriere den int-Token.
		Parser.tokenNext();
	}
	
	public static void parseExpressionableRoot(History<Object> history) {
		Parser.parseExpressionable(history);
		Node<Object> resultNode = NodeFunctions.nodePop();
		NodeFunctions.nodePush(resultNode);
	}
	
	public static void makeVariableNode(Datatype<Node<Object>> dtype, Token<Object> nameToken, Node<Object> valueNode, AccessSpecifierFlag accessSpecifier) {
		String nameStr = null;
		// Wir wollen, dass null ein valider Variablenname ist (Name wird erst später gesetzt).
		if (nameToken != null) {
			nameStr = (String) nameToken.getValue();
		}
		NodeFunctions.nodeCreate(new Node<Object>(NodeType.NODE_TYPE_VARIABLE, new Var<Object>(dtype, nameStr, valueNode, accessSpecifier)));
		// Fixup-Registrierung. Das Fixup sollte registriert werden, wenn wir eine Struktur nicht auflösen können (wurde Forward-deklariert).
		Node<Object> varNode = NodeFunctions.nodePeekOrNull();
		@SuppressWarnings("unchecked")
		Var<Object> var = (Var<Object>) varNode.getValue();
		// Wenn der Datentyp eine Struktur ist, und der Strukturknoten NULL ist (wir konnten die Struktur nicht auflösen, obwohl sie existiert).
		if (var.getType().getType() == DatatypeType.DATA_TYPE_STRUCT && var.getType().getStructOrUnionNode() == null) {
			DatatypeStructNodeFixPrivate<Object> privateData = new DatatypeStructNodeFixPrivate<Object>(varNode);
			Fixup.fixupRegister(Parser.parserFixupSys, new FixupConfig(privateData));
		}
	}
	
	public static void parserScopeOffsetForStack(Node<Object> varNode, History<Object> history) {
		// Holen uns die letzte Entität vom Scope. Wenn es auf dem aktuellen Scope nichts findet, wird immer weiter hoch geschaut.
		// Die letzte Entität bestimmt die Position im Speicher, wo wir uns befinden. Wir müssen alle Entitäten auf dem Stack
		// die es schon gibt berücksichtigen, um die korrekte Position im Speicher für eine Variable zu finden.
		ParserScopeEntity<Object> lastEntity = Parser.parserScopeLastEntityStopGlobalScope();
		// Festlegen, ob wir den Stack hoch oder herunter schauen müssen.
		// Ist varNode ein Funktionsargument, müssen wir hochschauen um die Variable zu finden.
		// Ist varNode eine lokale Variable, müssen wir herunterschauen um die Variable zu finden.
		boolean upwardStack = (history.getFlags() == HistoryFlag.HISTORY_FLAG_IS_UPWARD_STACK);
		// Der Stack wächst nach unten, daher ist der Offset die negative Variablengröße.
		int offset = -Helper.variableSize(varNode); // Bei einem int hätten wir ein offset von -4.
		if (upwardStack) {
			// Speichere die stack-Addition der Argumente der aktuellen Funktion (Minimum, das addiert werden muss vom BPT, bevor die Funktionsargumente erreicht werden können).
			int stackAddition = NodeFunctions.functionNodeArgumentStackAddition(Parser.parserCurrentFunction);
			offset = stackAddition; // Setzen die Stack-Addition als unser Default-Offset.
			// Falls wir keine letzte Entität haben, zeigt unser Offset auf die erste Funktionsvariable auf dem Stack.
			if (lastEntity != null) {
				// Falls wir eine letzte Entität haben, holen wir uns die Größe des letzten Datentyps der letzten Entität auf dem Stack, was zum neuen Offset wird (zum Beispiel 4 Bytes für int).
				@SuppressWarnings("unchecked")
				Var<Object> lastEntityVar = (Var<Object>) NodeFunctions.variableNode(lastEntity.getNode()).getValue();
				offset = DatatypeFunctions.datatypeSize(lastEntityVar.getType());
			}
		}
		// Finden wir keine letzte Entität, wäre es das erste int und die Position im Speicher wäre -4.
		// Hat die letzte Entität ein alignedOffset von -4 (für das erste int), so legen wir -8 für das zweite int fest.
		// Das heißt für offset wird in dem if Statement -8 berechnet (-4 + -4).
		// Am Ende fügen wir bei primitiven Variablen noch Padding hinzu.
		if (lastEntity != null) {
			@SuppressWarnings("unchecked")
			Var<Object> lastEntityVar = (Var<Object>) (NodeFunctions.variableNode(lastEntity.getNode()).getValue());
			offset += lastEntityVar.getAoffset();
			// Wenn es eine primtive Variable ist, fügen wir Padding hinzu.
			if (NodeFunctions.variableNodeIsPrimitive(varNode)) {
				@SuppressWarnings("unchecked")
				Var<Object> var = (Var<Object>) NodeFunctions.variableNode(varNode).getValue();
				// Wenn es ein upwardStack ist fügen wir den Wert von offset als Padding hinzu, sonst den negierten Wert von offset.
				var.setPadding(Helper.padding(upwardStack ? offset : -offset, var.getType().getSize()));
			}
		}
	}
	
	// Globale Variablen haben keine Offsets. Sie werden nicht auf dem Stack gespeichert. Daher passiert hier nichts.
	public static void parserScopeOffsetForGlobal(Node<Object> node, History<Object> history) {
		
	}
	
	// Wir befinden uns im Kölrper einer Struktur.
	/* Funktionsweise:
	 * struct abc {
	 * 	int x;
	 * 	int y;
	 * 	int e;
	 * };
	 * Beim Parsen des Körpers der Struktur abc wird zuerst keine letzte Entität gefunden,
	 * sodass für int x nichts passiert. Beim nächsten Aufruf wird das int y übergeben,
	 * wobei int x als letzte Entität gefunden wird. Dann wird das Offset berechnet
	 * anhand des stack-Offsets und der Größe der letzten Entität (hier 4 Bytes).
	 * Finden wir eine primitive Variable (int y ist keine Struktur/Union), wenden wir Padding an.
	 * Dann setzen wir das absolute Offset indem wir das Padding zum Offset addieren.
	 */
	public static void parserScopeOffsetForStructure(Node<Object> node, History<Object> history) {
		int offset = 0;
		ParserScopeEntity<Object> lastEntity = Parser.parserScopeLastEntity();
		if (lastEntity != null) {
			@SuppressWarnings("unchecked")
			Var<Object> lastEntityVar = (Var<Object>) lastEntity.getNode().getValue();
			offset += lastEntity.getStackOffset() + lastEntityVar.getType().getSize();
			@SuppressWarnings("unchecked")
			Var<Object> var = (Var<Object>) lastEntity.getNode().getValue();
			if (NodeFunctions.variableNodeIsPrimitive(node)) {
				var.setPadding(Helper.padding(offset, var.getType().getSize()));
			}
			// Berechnen des absoluten Offsets.
			var.setAoffset(offset + var.getPadding());
		}
	}
	
	public static void parserScopeOffset(Node<Object> varNode, History<Object> history) {
		if ((history.getFlags() == HistoryFlag.HISTORY_FLAG_IS_GLOBAL_SCOPE)) {
			Parser.parserScopeOffsetForGlobal(varNode, history);
			return;
		}
		// Wenn wir einen Strukturkörper parsen, callen wir diese Funktion.
		if ((history.getFlags() == HistoryFlag.HISTORY_FLAG_INSIDE_STRUCTURE)) {
			Parser.parserScopeOffsetForStructure(varNode, history);
			return;
		}
		Parser.parserScopeOffsetForStack(varNode, history);
	}
	
	@SuppressWarnings("unchecked")
	public static boolean parserVariableNameIsInCurrentScope(String varName) {
		Scope<Object> scope = Scope.scopeCurrent(Parser.currentProcess);
		if (scope == null) {
			return false;
		}
		ParserScopeEntity<Object> entity = (ParserScopeEntity<Object>) Scope.scopeIterateBack(scope);
		if (entity == null) return false;
		while (entity != null) {
			if (entity.getNode().getValue() instanceof Var<?>) {
				if (SymbolTable.S_EQ((((Var<Object>) entity.getNode().getValue()).getName()), varName)) {
					return true;
				}
			}
			entity = (ParserScopeEntity<Object>) Scope.scopeIterateBack(scope);
		}
		return false;
	}
	
	public static boolean parserVariableIsDuplicate(Var<Object> var) {
		if (Parser.parserVariableNameIsInCurrentScope(var.getName())) {
			return true;
		}
		return false;
	}
	
	public static Var<Object> getFriendlyVariableIfExistingAndRegister (History<Object> history, String varName, String currentNamespace) {
		Symbol<Object> sym = Symresolver.symresolverGetSymbol(Parser.currentProcess, varName, Parser.getDeclaredNamespaceOrStructBodyScope(history, currentNamespace), NodeType.NODE_TYPE_VARIABLE);
		if (sym != null) {
			@SuppressWarnings("unchecked")
			Var<Object> friendlyVar = (Var<Object>) sym.getData().getValue();
			friendlyVar.getFriendlyScopes().vectorPush(Scope.scopeCurrent(Parser.currentProcess));
			return friendlyVar;
		}
		Compiler.compilerError(Parser.currentProcess, "Die friend-Variable \"" + varName + "\" im Namensraum \"" + currentNamespace + "\" existiert nicht.\n");
		return null;
	}
	
	public static Var<Object> findVarInGlobalScope(String varName) {
		Scope<Object> globalScope = Parser.currentProcess.getScope().getRoot();
		for(int i = 0; i < globalScope.getEntities().getCount(); i++) {
			@SuppressWarnings("unchecked")
			ParserScopeEntity<Object> entity = (ParserScopeEntity<Object>) globalScope.getEntities().vectorPeekPtrAt(i);
			Node<Object> node = entity.getNode();
			@SuppressWarnings("unchecked")
			Var<Object> var = (Var<Object>) node.getValue();
			if (SymbolTable.S_EQ(var.getName(), varName)) {
				var.getFriendlyScopes().vectorPush(Scope.scopeCurrent(Parser.currentProcess));
				return var;
			}
		}
		return null;
	}
	
	public static void makeVariableNodeAndRegister(History<Object> history, Datatype<Node<Object>> dtype, Token<Object> nameToken, Node<Object> valueNode, AccessSpecifierFlag accessSpecifier, Node<Object> alreadyDefinedVarNodeInGivenNamespace) {
		Parser.makeVariableNode(dtype, nameToken, valueNode, accessSpecifier);
		Node<Object> varNode = NodeFunctions.nodePop();
		// Berechne das Scope-Offset.
		Parser.parserScopeOffset(varNode, history);
		// Drücke den Variablenknoten in den Scope.
		@SuppressWarnings("unchecked")
		Var<Object> var = (Var<Object>) varNode.getValue();
		if (Parser.tempAssignmentTokenIndex != null) {
			CleanCodeAnalyzer.analyzeWhitespacesAroundAssignmentOperator(Parser.currentProcess, Parser.tempAssignmentTokenIndex, nameToken, varNode);
		}
		Parser.tempAssignmentTokenIndex = Parser.priorTempAssignmentTokenIndex;
		// Gibt es die Variable schon im aktuellen Scope?
		if (Parser.parserVariableIsDuplicate(var)) {
			Compiler.compilerError(Parser.currentProcess, "In diesem Scope existiert die Variable \"" + var.getName() + "\" bereits. Duplikate sind nicht erlaubt.\n");
		}
		Var<Object> friendlyVar = null;
		// Ist es lediglich eine friends-Deklaration?
		if (Parser.varIsFriendFlag) {
			// Wir drücken statt dem erstellten Variablenknoten die vorhandene Variable der angegebenen Klasse auf den Stack und tragen
			// die Klasse als Freund ein.
			if (Parser.currentNamespace != null) {
				friendlyVar = Parser.getFriendlyVariableIfExistingAndRegister(history, var.getName(), Parser.currentNamespace);
			}
			else {
				friendlyVar = Parser.findVarInGlobalScope(var.getName());
			}
			if (friendlyVar == null) {
				Compiler.compilerError(Parser.currentProcess, "Die befreundete Variable \"" + var.getName() + "\" existiert nicht.\n");
			}
			Parser.varIsFriendFlag = false;
			// Legen eine Referenz des Variablenknotens auf die befreundete Variable.
			varNode.setValue(friendlyVar);
		}
		// Falls die Variable nicht schon existiert, legen wir sie an und analysieren die Sauberkeit.
		if (alreadyDefinedVarNodeInGivenNamespace == null) {
			CleanCodeAnalyzer.analyzeVarName(Parser.currentProcess, nameToken, varNode, dtype.getTypeStr(), Parser.currentSUDatatype, Parser.parserCurrentFunction, Parser.insideForLoopInitializing);
			Parser.parserScopePush(Parser.parserNewScopeEntity(varNode, var.getAoffset(), 0), var.getType().getSize());
			NodeFunctions.nodePush(varNode);
			Symresolver.symresolverBuildForVariableNode(Parser.currentProcess, varNode, Scope.scopeCurrent(Parser.currentProcess));
		}
		// Sonst ändern wir nur den Pointer des existierenden Variablenknotens auf die neu angelegte Variable.
		else {
			alreadyDefinedVarNodeInGivenNamespace.setValue(varNode.getValue());
		}
	}
	
	public static void makeVariableListNode(Vector<Node<Object>> varListVec) {
		NodeFunctions.nodeCreate(new Node<Object>(NodeType.NODE_TYPE_VARIABLE_LIST, new VarList<Object>(varListVec)));
	}
	
	public static ArrayBrackets<Node<Object>> parseArrayBrackets(History<Object> history) {
		ArrayBrackets<Node<Object>> brackets = Array.arrayBracketsNew();
		// Linke Klammer sind immer Operatoren und rechte Klammern sind Symbole.
		while(Parser.tokenNextIsOperator("[")) {
			Parser.expectOp("[");
			// 1. Fall: Keine Größe des Arrays angegeben.
			if (Token.tokenIsSymbol(Parser.tokenPeekNext(), ']')) {
				// Es befindet sich nichts zwischen den Klammern.
				// Beispiel: int x[] = {50, 30, 20}; ist valide.
				Parser.expectSym(']');
				break;
			}
			// 2. Fall: Größe des Arrays ist angegeben (als Expression).
			boolean priorParserinsideParsingExpressionAsArrayBracketSize = Parser.insideParsingExpressionAsArrayBracketSize;
			Parser.insideParsingExpressionAsArrayBracketSize = true;
			Parser.parseExpressionable(history);
			Parser.insideParsingExpressionAsArrayBracketSize = priorParserinsideParsingExpressionAsArrayBracketSize;
			Parser.expectSym(']');
			// Wirf die Expression vom Knotenstack.
			Node<Object> expNode = NodeFunctions.nodePop();
			// Erstelle einen Knoten für die Klammern, wirf ihn vom Stack und füge ihn zu dem Arrayklammernvektor hinzu.
			NodeFunctions.makeBracketNode(expNode);
			Node<Object> bracketNode = NodeFunctions.nodePop();
			Array.arrayBracketsAdd(brackets, bracketNode);
		}
		return brackets;
	}
	
	public static void parseVariable(Datatype<Node<Object>> dtype, Token<Object> nameToken, History<Object> history, AccessSpecifierFlag accessSpecifier, Node<Object> namespaceIdentifierNode) {
		// Möchten wir in einem Namespace eine existierende Variable initialisieren?
		Node<Object> varNode = null;
		if (namespaceIdentifierNode != null) {
			String namespaceIdentifier = (String) namespaceIdentifierNode.getValue();
			Symbol<Object> varSym = Symresolver.symresolverGetSymbol(Parser.currentProcess, (String) nameToken.getValue(), Parser.getDeclaredNamespaceOrStructBodyScope(history, namespaceIdentifier), NodeType.NODE_TYPE_VARIABLE);
			if (varSym != null) {
				varNode = varSym.getData();
				@SuppressWarnings("unchecked")
				Var<Object> var = (Var<Object>) varSym.getData().getValue();
				if (!(SymbolTable.S_EQ(var.getType().getTypeStr(), dtype.getTypeStr()))) {
					Compiler.compilerError(Parser.currentProcess, "Die Variable \"" + var.getName() + "\" ist vom Typ \"" + var.getType().getTypeStr() + "\", nicht vom Typ \"" + dtype.getTypeStr() + "\". Eine Zuweisungsoperation ist nicht möglich.\n");
				}
			}
		}
		Node<Object> valueNode = null;
		// 1. Fall: Haben wir "int a;", haben wir zu diesem Zeitpunkt bis zum Semikolon alles gecheckt.
		// 2. Fall: Haben wir "int b[30];", müssen wir uns um die Array-Klammern kümmern.
		ArrayBrackets<Node<Object>> brackets = null;
		if (Parser.tokenNextIsOperator("[")) {
			brackets = Parser.parseArrayBrackets(history);
			dtype.setArray(new Array<Node<Object>>());
			dtype.getArray().setBrackets(brackets);
			dtype.getArray().setSize(Array.arrayBracketsCalculateSize(dtype, brackets));
			dtype.setFlags(dtype.getFlags() | DatatypeFlag.DATATYPE_FLAG_IS_ARRAY);
		}
		// 3. Fall: Haben wir "int c = 50;" müssen wir für den Zuweisungsoperator checken.
		if (Parser.tokenNextIsOperator("=") && !(Parser.isConstructorCall)) {
			Parser.priorTempAssignmentTokenIndex = Parser.tempAssignmentTokenIndex;
			Parser.tempAssignmentTokenIndex = Parser.currentProcess.getTokenVec().getPeekIndex();
			// Ignoriere den "=" Operator.
			Parser.tokenNext();
			// Fall: Array-Deklaration (C++ Style).
			if (Token.tokenIsKeyword(Parser.tokenPeekNext(), "new")) {
				Parser.expectKeyword("new");
				// Variablentypen checken und vom Stack nehmen.
				if (SymbolTable.S_EQ((String) Parser.tokenPeekNext().getValue(), dtype.getTypeStr())) {
					Parser.tokenNext();
				}
				//Generics checken und vom Stack nehmen.
				if (dtype.getConcreteDatatypesNode() != null && Parser.tokenNextIsOperator("<")) {
					Parser.checkForCorrectConcreteGenericsWhileStructInit(dtype);
				}
				// Zählen der Unary-Operatoren.
				int unaryOperators = 0;
				if (Parser.tokenNextIsOperator("*")) {
					while(Token.tokenIsOperator(Parser.tokenPeekNext(), "*")) {
						Parser.expectOp("*");
						unaryOperators++;
					}
				}
				if (Parser.tokenNextIsOperator("[")) {
					brackets = Parser.parseArrayBrackets(history);
					dtype.setArray(new Array<Node<Object>>());
					dtype.getArray().setBrackets(brackets);
					dtype.getArray().setSize(Array.arrayBracketsCalculateSize(dtype, brackets));
					dtype.setFlags(dtype.getFlags() | DatatypeFlag.DATATYPE_FLAG_IS_ARRAY);
				}
				// Zusammen müssen die Pointertiefe plus die Klammern auf beiden seiten der Zuweisung identisch sein.
				int cppStyleRightSideOfAssignmentBrackets = dtype.getArray().getBrackets().getNBrackets().vectorCount();
				if ((dtype.getCppStyleLeftSideOfAssignmentBrackets() + dtype.getPointerDepth()) != (unaryOperators + cppStyleRightSideOfAssignmentBrackets)) {
					Compiler.compilerError(Parser.currentProcess, "Bei Arrays im C++-Style müssen die Pointertiefe plus die Anzahl der Klammern auf beiden Seiten des Zuweisungsstatements identisch sein.\n");
				}
				Parser.makeVariableNodeAndRegister(history, dtype, nameToken, valueNode, accessSpecifier, varNode);
				return;
			}
			boolean priorInsideVariableParsing = Parser.insideVariableParsing;
			Parser.insideVariableParsing = true;
			Parser.parseExpressionableRoot(history);
			Parser.possibleFunctionCallInsideExpressionInCoutStatementStart = false;
			valueNode = NodeFunctions.nodePop();
			Parser.insideVariableParsing = priorInsideVariableParsing;
		}
		// 4. Fall: For-Each-Schleife.
		else if (Parser.tokenNextIsOperator(":")) {
			// Ignoriere den ":" Operator.
			Parser.tokenNext();
			Parser.parseExpressionableRoot(history);
			valueNode = NodeFunctions.nodePop();
		}
		// 5. Fall: Konstruktoraufruf.
		if (Parser.isConstructorCall) {
			Parser.priorTempAssignmentTokenIndex = Parser.tempAssignmentTokenIndex;
			Parser.tempAssignmentTokenIndex = Parser.currentProcess.getTokenVec().getPeekIndex();
			Parser.expectOp("=");
			Datatype<Node<Object>> priorSUDatatype = Parser.currentSUDatatype;
			Parser.currentSUDatatype = dtype;
			if (Token.tokenIsKeyword(Parser.tokenPeekNext(), "new")) {
				Parser.expectKeyword("new");
			}
			Parser.parseExpressionableRoot(history);
			valueNode = NodeFunctions.nodePop();
			Parser.currentSUDatatype = priorSUDatatype;
		}
		Parser.makeVariableNodeAndRegister(history, dtype, nameToken, valueNode, accessSpecifier, varNode);
	}
	
	public static Scope<Object> getDeclaredNamespaceOrStructBodyScope(History<Object> history, String namespaceIdentifier) {
		Node<Object> node = Parser.getNamespaceNodeIfExisting(history, namespaceIdentifier);
		if (node == null) {
			return null;
		}
		else if (node.getType() == NodeType.NODE_TYPE_STRUCT) {
			@SuppressWarnings("unchecked")
			Struct<Object> struct = (Struct<Object>) node.getValue();
			@SuppressWarnings("unchecked")
			Body<Object> body = (Body<Object>) struct.getBodyN().getValue();
			return body.getBodyScope();
		}
		else if (node.getType() == NodeType.NODE_TYPE_NAMESPACE) {
			@SuppressWarnings("unchecked")
			Namespace<Object> namespace = (Namespace<Object>) node.getValue();
			if (namespace.getBodyNode() != null) {
				@SuppressWarnings("unchecked")
				Body<Object> body = (Body<Object>) namespace.getBodyNode().getValue();
				return body.getBodyScope();
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static Function<Object> traverseBodyForForwardDeclaration(String namespaceIdentifier, Node<Object> bodyNode, Node<Object> functionNode) {
		Body<Object> body = (Body<Object>) bodyNode.getValue();
		Function<Object> func = (Function<Object>) functionNode.getValue();
		String funcName = func.getName();
		Vector<Node<Object>> bodyVec = body.getStatements();
		boolean functionDeclarationIsExisting = false;
		bodyVec.vectorSetPeekPointer(0);
		Node<Object> currentNode = null;
		Function<Object> currentFunction = null;
		for(int i = 0; i < bodyVec.vectorCount(); i++) {
			currentNode = bodyVec.vectorPeekPtrAt(i);
			if (currentNode.getType() == NodeType.NODE_TYPE_FUNCTION) {
				currentFunction = (Function<Object>) currentNode.getValue(); 
				if (SymbolTable.S_EQ(funcName, currentFunction.getName()) && currentFunction.getFlags() == func.getFlags()) {
					functionDeclarationIsExisting = true;
					break;
				}
			}
		}
		if (!(functionDeclarationIsExisting)) {
			Compiler.compilerError(Parser.currentProcess, "Die Funktion \"" + funcName + "\", die Sie von außerhalb der Klasse \"" + namespaceIdentifier + "\" definieren möchten, muss zuerst innerhalb der Klasse mit einer Forward-Declaration angekündigt werden.\n");
		}
		if (currentFunction.getBodyN() != null) {
			Compiler.compilerError(Parser.currentProcess, "Die Funktion \"" + funcName + "\", die Sie von außerhalb der Klasse \"" + namespaceIdentifier + "\" definieren möchten, ist bereits definiert.\n");
		}
		return currentFunction;
	}
	
	public static boolean checkForExistingForwardDeclarationInNamespaceOrStructBody(History<Object> history, String namespaceIdentifier, Node<Object> functionNode, AccessSpecifierFlag accessSpecifier) {
		Node<Object> node = Parser.getNamespaceNodeIfExisting(history, namespaceIdentifier);
		Function<Object> forwardDeclaration = null;
		if (node.getType() == NodeType.NODE_TYPE_STRUCT) {
			@SuppressWarnings("unchecked")
			Struct<Object> struct = (Struct<Object>) node.getValue();
			if (struct.getBodyN() != null) {
				forwardDeclaration = Parser.traverseBodyForForwardDeclaration(namespaceIdentifier, struct.getBodyN(), functionNode);
			}
		}
		else if (node.getType() == NodeType.NODE_TYPE_NAMESPACE) {
			@SuppressWarnings("unchecked")
			Namespace<Object> namespace = (Namespace<Object>) node.getValue();
			if (namespace.getBodyNode() != null) {
				forwardDeclaration = Parser.traverseBodyForForwardDeclaration(namespaceIdentifier, namespace.getBodyNode(), functionNode);
			}
		}
		if (forwardDeclaration != null) {
			return true;
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public static Node<Object> addScopeAsFriendToFriendlyFunctionAndReturnIt(History<Object> history, Token<Object> nameToken, Node<Object> namespaceIdentifierNode, Node<Object> functionNode, AccessSpecifierFlag accessSpecifier) {
		Scope<Object> namespaceScope = null;
		Function<Object> func = (Function<Object>) functionNode.getValue();
		if (namespaceIdentifierNode != null) {
			namespaceScope = Parser.getDeclaredNamespaceOrStructBodyScope(history, (String) namespaceIdentifierNode.getValue());
		}
		else {
			namespaceScope = Parser.currentProcess.getScope().getRoot();
		}
		Symbol<Object> sym = Symresolver.symresolverGetSymbolForNativeFunction(Parser.currentProcess, nameToken, namespaceScope, func.getArgs().getVector(), false, func.getFlags(), false);
		Function<Object> friendlyFunction = null;
		if (sym != null) {
			friendlyFunction = (Function<Object>) sym.getData().getValue();
			// Eintragen des Scopes, in der sich die friends-Deklaration befindet.
			friendlyFunction.getFriendlyScopes().vectorPush(Scope.scopeCurrent(Parser.currentProcess));
		}
		else {
			func.getFriendlyScopes().vectorPush(Scope.scopeCurrent(Parser.currentProcess));
			return null;
		}
		return friendlyFunction.getFunctionNode();
	}
	
	public static void parseFunctionBody(History<Object> history) {
		// Wir interessieren uns nicht für die Variablengröße beim Funktionsparsen.
		Parser.parseBody(null, Parser.historyDown(history, history.getFlags() | HistoryFlag.HISTORY_FLAG_INSIDE_FUNCTION_BODY), BodyFlag.BODY_IS_FLAG_FUNCTION);
	}
	
	public static void parseFunction(Datatype<Node<Object>> retType, Token<Object> nameToken, History<Object> history, AccessSpecifierFlag accessSpecifier, FunctionFlag flags, Node<Object> namespaceIdentifierNode) {
		int functionStartLineTokenVecIndex = Parser.functionStartLineTokenVecIndex;
		Vector<Node<Object>> argumentsVector = null;
		Parser.parserScopeNew();
		// Erstellen einen Funktionsknoten mit dem Rückgabetyp und dem Funktionsnamen, ohne Körperknoten und ohne Übergabeparametervektor.
		NodeFunctions.makeFunctionNode(retType, (String) nameToken.getValue(), null, null, accessSpecifier, namespaceIdentifierNode);
		Node<Object> functionNode = NodeFunctions.nodePop();
		Node<Object> currentFunctionParent = Parser.parserCurrentFunction;
		Parser.parserCurrentFunction = functionNode;
		String priorCurrentNamespaceIdentifierInDeclarationFromOutside = Parser.currentNamespaceIdentifierInDeclarationFromOutside;
		if (namespaceIdentifierNode != null) {
			Parser.currentNamespaceIdentifierInDeclarationFromOutside = (String) namespaceIdentifierNode.getValue();
		}
		@SuppressWarnings("unchecked")
		Function<Object> func = (Function<Object>) functionNode.getValue();
		if (DatatypeFunctions.datatypeIsStructOrUnion(retType)) {
			func.getArgs().setStackAddition(func.getArgs().getStackAddition() + DatatypeFunctions.DATA_SIZE_DWORD);
		}
		Parser.expectOp("(");
		argumentsVector = Parser.parseFunctionArguments(Parser.historyBegin(0));
		Parser.expectSym(')');
		// Setzen die Funktionsparameter der Funktion auf den Argumentenvektor (NULL, wenn keine Paramete übergeben wurden).
		func.getArgs().setVector(argumentsVector);
		func.setFlags(flags);
		func.setStartTokenVecIndex(functionStartLineTokenVecIndex);
		Scope<Object> currentScope = null;
		boolean isFriendlyFunction = false;
		if (Parser.tokenNextIsKeyword("throw")) {
			Parser.parseThrowInsideFunctionHead(history);
			Node<Object> throwInsideFunctionHeadNode = NodeFunctions.nodePop();
			func.setThrowInsideFunctionHead(throwInsideFunctionHeadNode);
		}
		// Parse den Funktionskörper, falls einer existiert (Es ist keine Forward-Declaration).
		Node<Object> bodyNode = null;
		if (Parser.tokenNextIsSymbol('{')) {
			if (Parser.functionIsFriendFlag) {
				Compiler.compilerError(Parser.currentProcess, "Sie können eine befreundete Methode innerhalb einer Klasse nicht definieren, sondern müssen eine Forward-Declaration verwenden.\n");
			}
			Parser.parseFunctionBody(Parser.historyBegin(0)); // handhabt die '}'.
			bodyNode = NodeFunctions.nodePop();
			func.setBodyN(bodyNode);
			bodyNode.getBinded().setEntity(functionNode);
			if (Parser.functionNodeVec == null) {
				Parser.functionNodeVec = Vector.vectorCreate();
			}
			Parser.functionNodeVec.vectorPush(functionNode);
		}
		// Wenn kein '{' existiert, muss es eine Forward-Declaration sein (die mit Semikolon schließt).
		else {
			func.setAccessSpecifier(accessSpecifier);
			func.setHasForwardDeclaration(true);
			// Falls es eine befreundete Methode ist, müssen wir den Scope außerhalb des aktuellen Körpers nutzen (entweder angegebene Struktur/Namensraum oder globaler Scope).
			if (Parser.functionIsFriendFlag) {
				isFriendlyFunction = true;
			}
			if (Parser.tokenNextIsKeyword("throw")) {
				Parser.parseThrowInsideFunctionHead(history);
				Node<Object> throwInsideFunctionHeadNode = NodeFunctions.nodePop();
				func.setThrowInsideFunctionHead(throwInsideFunctionHeadNode);
			}
			if ((!(Parser.isParsingString) || (Namespace.namespaceOrMemberIsDefined(Parser.currentProcess, "std", "string")))) {
				Parser.expectSym(';');
			}
		}
		// Falls wir uns nicht in einer globalen Funktion befinden, legen wir das Symbol im entsprechenden Scope an.
		// Der aktuelle Scope meint den Scope der Funktion. Daher müssen wir uns den Elternscope anschauen. Ist davon der Eltern-
		// scope null, befinden wir uns im Wurzelscope. Sonst befinden wir uns in einer Struktur oder einer Funktion.
		currentScope = Scope.scopeCurrent(Parser.currentProcess).getParent();
		Node<Object> friendlyFunction = null;
		// Falls ein Namespace übergeben wurde (Beispiel: "int classname::foo() {}"), dann benötigen wir den Scope des jeweiligen Namensraums.
		if (namespaceIdentifierNode != null && !(isFriendlyFunction)) {
			// Gibt den Scope des Körpers des angegebenen Namensraums oder der Klasse zurück, damit die Funktion dort registriert wird.
			currentScope = Parser.getDeclaredNamespaceOrStructBodyScope(history, (String) namespaceIdentifierNode.getValue());
			// Methoden lassen sich nur in Klassen oder Namensräumen von außerhalb definieren, wenn eine Forward-Declaration vorhanden ist.
			Parser.checkForExistingForwardDeclarationInNamespaceOrStructBody(history, (String) namespaceIdentifierNode.getValue(), functionNode, accessSpecifier);
		}
		else if (isFriendlyFunction) {
			// Suche nach dem Scope der befreundeten Funktion.
			currentScope = null;
			// Falls es die Methode schon gibt, fügen wir die aktuelle Klasse/Struktur als Friend ein. Sonst erstellen wir eine Forward-Declaration.
			friendlyFunction = null;
			friendlyFunction = Parser.addScopeAsFriendToFriendlyFunctionAndReturnIt(history, nameToken, namespaceIdentifierNode, functionNode, accessSpecifier);
			// Erstelle eine Referenz zur Eintragung im Körpervektor. Es wird kein Symbol erstellt.
			if (friendlyFunction != null) {
				functionNode = friendlyFunction;
			}
			else {
				if (namespaceIdentifierNode != null) {
					currentScope = Parser.getDeclaredNamespaceOrStructBodyScope(history, (String) namespaceIdentifierNode.getValue());
				}
			}
			if (currentScope == null) {
				currentScope = Parser.currentProcess.getScope().getRoot();
			}
		}
		// Es wird ein neues Symbol angelegt, falls die befreundete Funktion noch nicht in diesem Scope existiert.
		Symresolver.symresolverBuildForFunctionNode(Parser.currentProcess, nameToken, functionNode, currentScope, isFriendlyFunction, func.getFlags());
		// Falls wir nicht von außen auf eine Klasse zugreifen, drücken wir den Funktionsknoten in den Stack.
		if (namespaceIdentifierNode == null) {
			NodeFunctions.nodePush(functionNode);
		}
		else {
			Parser.declaredFunctionFromOutsideNamespace = true;
		}
		Parser.currentNamespaceIdentifierInDeclarationFromOutside = priorCurrentNamespaceIdentifierInDeclarationFromOutside;
		Parser.parserCurrentFunction = currentFunctionParent;
		Parser.functionIsFriendFlag = false;
		Parser.parserScopeFinish();
		// Die Analyse der Funktionslänge macht nur für nicht-Forward-Declarations Sinn.
		if (Parser.functionEndLine != null) {
			int functionStartLine;
			if (retType == null) {
				functionStartLine = Parser.currentDestructorTildeToken.getPos().getLineNumber();
				Parser.currentDestructorTildeToken = null;
			}
			else {
				functionStartLine = retType.getStartToken().getPos().getLineNumber();
			}
			CleanCodeAnalyzer.analyzeMethodFormatting(Parser.currentProcess, nameToken, functionStartLine, functionStartLineTokenVecIndex, Parser.functionEndLine, Parser.functionEndLineTokenVecIndex, functionNode, bodyNode);
		}
		CleanCodeAnalyzer.analyzeMethodArguments(Parser.currentProcess, nameToken, argumentsVector, functionNode);
		if (Parser.tryCatchBlockFlagInsideFunction) {
			CleanCodeAnalyzer.analyzeTryCatchStmtInsideMethod(Parser.currentProcess, nameToken, functionNode, bodyNode);
			Parser.tryCatchBlockFlagInsideFunction = false;
		}
		Parser.functionStartLineTokenVecIndex = null;
		Parser.functionEndLine = null;
		Parser.functionEndLineTokenVecIndex = null;
	}
	
	public static void parseSymbol() {
		if (Parser.tokenNextIsSymbol('{')) {
			VariableSize variableSize = new VariableSize();
			History<Object> history = Parser.historyBegin(HistoryFlag.HISTORY_FLAG_IS_GLOBAL_SCOPE);
			Parser.parseBody(variableSize, history, BodyFlag.BODY_IS_FLAG_SYMBOL);
			Node<Object> bodyNode = NodeFunctions.nodePop();
			
			NodeFunctions.nodePush(bodyNode);
		}
		else if (Parser.tokenNextIsSymbol(':')) {
			Parser.parseLabel(Parser.historyBegin(0));
			return;
		}
		else if (Parser.tokenNextIsSymbol('#')) {
			Parser.tempHashTokenVecIndex = Parser.currentProcess.getTokenVec().getPeekIndex();
			Parser.tokenNext();
			if (Parser.tokenNextIsKeyword("include")) {
				Parser.parseLibraryDirective(Parser.historyBegin(0));
			}
			else {
				Compiler.compilerError(Parser.currentProcess, "Präprozessordirektive werden von diesem Compiler noch nicht unterstützt.\n");
			}
		}
		else Compiler.compilerError(Parser.currentProcess, "Es wurde ein nicht valides Symbol eingegeben.\n");
	}
	
	public static int parseStatement(History<Object> history) {
		// Möglichen Namespace in der Form "Datentyp classname::foo()" abfangen.
		Node<Object> namespaceIdentifierNode = null;
		int index = 1;
		Token<Object> token = Parser.tokenPeekAt(index);
		if (token.getType() == TokenType.TOKEN_TYPE_IDENTIFIER && Parser.isNamespaceOrStructAsNamespaceNotation(history, (String) token.getValue())) {
			Parser.tokenNext();
			namespaceIdentifierNode = Parser.parseNamespaceOrStructAsNamespaceNotation(history, false);
			Parser.currentNamespace = (String) namespaceIdentifierNode.getValue();
		}
		// Dieser Fall meint einen Aufruf in der Form "classname::foo()".
		else if (Token.tokenIsOperator(token, "::")) {
			token = Parser.tokenPeekNext();
			if (token.getType() == TokenType.TOKEN_TYPE_IDENTIFIER && Parser.isNamespaceOrStructAsNamespaceNotation(history, (String) token.getValue())) {
				namespaceIdentifierNode = Parser.parseNamespaceOrStructAsNamespaceNotation(history, false);
				Parser.currentNamespace = (String) namespaceIdentifierNode.getValue();
			}
		}
		// Statements können Keywords sein, wie "return 56;".
		if (Parser.tokenPeekNext() != null && Parser.tokenPeekNext().getType() == TokenType.TOKEN_TYPE_KEYWORD) {
			return Parser.parseKeyword(history);
		}
		else if (Parser.tokenPeekNext() != null && (Parser.identifierIsConstructorOrObjectOrMethod(Parser.tokenPeekNext(), namespaceIdentifierNode) || Parser.tokenNextIsOperator("~") || (Parser.tokenPeekNext().getType() == TokenType.TOKEN_TYPE_IDENTIFIER && Parser.getNamespaceNodeIfExisting(history, (String) Parser.tokenPeekNext().getValue()) != null))) {				
			// 1. Fall: Destruktor (Tilde wird vom Stack geworfen und die Destruktorflag wird gesetzt).
			boolean isDestructor = false;
			Parser.functionStartLineTokenVecIndex = Parser.currentProcess.getTokenVec().getPeekIndex();
			if (Parser.tokenNextIsOperator("~")) {
				isDestructor = true;
				Parser.tokenNext();
			}
			// 2. Fall: Konstruktor.
			token = Parser.tokenPeekAt(index);
			if (token.getType() == TokenType.TOKEN_TYPE_OPERATOR && SymbolTable.S_EQ((String) token.getValue(), "(")) {
				Parser.parseConstructorOrDestructor(Parser.tokenNext(), history, null, isDestructor, namespaceIdentifierNode);
				return -1;
			}
			// 3. Fall: Struct- oder Klassenvariable oder Funktion mit dem Typ des Structs oder der Klasse.
			if (!(Parser.identifierIsStructDeclarationMethodOrAssignment) && Parser.isStructDatatypeType((String) Parser.tokenPeekNext().getValue())) {
				Parser.identifierIsStructDeclarationMethodOrAssignment = true;
			}
			Parser.parseVariableFunctionOrStructUnion(history, null);
			if (Parser.isConstructorCall) {
				Parser.isConstructorCall = false;
			}
			return 0;
		}
		// Ist es kein Keyword (und auch kein Objekt oder Methode), ist es eine Expression, wie "x = 50;".
		Parser.parseExpressionableRoot(history);
		// Finden wir ein Symbol, das kein Semikolon ist (es ist normal, Statements mit einem Semikolon zu beenden), parsen wir das Symbol.
		if (Parser.tokenPeekNext() != null && Parser.tokenPeekNext().getType() == TokenType.TOKEN_TYPE_SYMBOL && !(Token.tokenIsSymbol(Parser.tokenPeekNext(), ';'))) {
			Parser.parseSymbol();
			return 0;
		}
		// Alle Statements enden mit einem Semikolon.
		Parser.expectSym(';');
		return 0;
	}
	
	public static void parserAppendSizeForNodeStructUnion(History<Object> history, VariableSize _variableSize, Node<Object> node) {
		@SuppressWarnings("unchecked")
		Var<Object> var = (Var<Object>) node.getValue();
		_variableSize.setVariableSize(_variableSize.getVariableSize() + Helper.variableSize(node));
		// Mit Struct-Unionpointern können wir hier nichts anfangen an diesem Punkt.
		if ((var.getType().getFlags() == DatatypeFlag.DATATYPE_FLAG_IS_POINTER)) {
			return;
		}
		// Holen der größten Variablen innerhalb einer Struktur bzw. Union aus dessen Körper.
		@SuppressWarnings("unchecked")
		Body<Object> body = (Body<Object>) Helper.variableStructOrUnionBodyNode(node).getValue();
		Node<Object> largestVarNode = body.getLargestVarNode();
		// Falls die Struktur bzw. Union eine größe Variable besitzt:
		if (largestVarNode != null) {
			// Structs und Unions müssen an die größte Variable in dessen Körper angepasst werden.
			@SuppressWarnings("unchecked")
			Var<Object> largestVar = (Var<Object>) largestVarNode.getValue();
			_variableSize.setVariableSize(_variableSize.getVariableSize() + Helper.alignValue(_variableSize.getVariableSize(), largestVar.getType().getSize()));
		}
	}
	
	// vec -> Liste von Variablenknoten.
	public static void parserAppendSizeForVariableList(History<Object> history, VariableSize variableSize, Vector<Node<Object>> vec) {
		vec.vectorSetPeekPointer(0);
		Node<Object> node = vec.vectorPeekPtr();
		while(node != null) {
			Parser.parserAppendSizeForNode(history, variableSize, node);
			node = vec.vectorPeekPtr();
		}
	}
	
	// Die Funktion nimmt einen Knoten und versucht eine Variablengröße aus dem Knoten zu extrahieren und sie zur Variablengröße zu addieren.
	public static void parserAppendSizeForNode(History<Object> history, VariableSize _variableSize, Node<Object> node) {
		if (node == null) {
			return;
		}
		if (node.getType() == NodeType.NODE_TYPE_VARIABLE) {
			// 1. Fall: Struct oder Union:
			if (NodeFunctions.nodeIsStructOrUnionVariable(node)) {
				Parser.parserAppendSizeForNodeStructUnion(history, _variableSize, node);
				return;
			}
			// 2. Fall: Primitive Variable:
			_variableSize.setVariableSize(_variableSize.getVariableSize() + Helper.variableSize(node));
		}
		else if (node.getType() == NodeType.NODE_TYPE_VARIABLE_LIST) {
			@SuppressWarnings("unchecked")
			VarList<Object> varList = (VarList<Object>) node.getValue();
			Parser.parserAppendSizeForVariableList(history, _variableSize, varList.getList());
		}
	}
	
	// Die Funktion berechnet das Padding für den Körper.
	public static void parserFinalizeBody(History<Object> history, Node<Object> bodyNode, Vector<Node<Object>> bodyVec, VariableSize _variableSize, Node<Object> largestAlignEligibleVarNode, Node<Object> largestPossibleVarNode) {
		if (history.getFlags() == HistoryFlag.HISTORY_FLAG_INSIDE_UNION) {
			// Befinden uns im Körper einer Union.
			// Unions haben aufgrund von geteiltem Speicher die Größe der größten Variable innerhalb der Union.
			if (largestPossibleVarNode != null) {
				// Haben wir mehr als 0 Variablen in der Union, haben wir auch einen größten möglichen Variablenknoten (inkl. Strukturen und Unions).
				_variableSize.setVariableSize(Helper.variableSize(largestPossibleVarNode));
			}
		}
		// Berechnen von Padding, muss für Strukturen und Unions gemacht werden.
		// Erhöhung der Größe, sodass sie in 4 Byte-Blöcke passt.
		int padding = Helper.computeSumPadding(bodyVec);
		_variableSize.setVariableSize(_variableSize.getVariableSize() + padding);
		// Haben wir einen größten zur Anpassung qualifizierten Knoten übergeben bekommen, so müssen wir die Größe auf diesen Datentyp anpassen.
		// Die Größe des largestAlignEligibleVarNode ist gleich der grlßten primitiven Variable (ohne Strukturen und Unions).
		if (largestAlignEligibleVarNode != null) {
			@SuppressWarnings("unchecked")
			Var<Object> largestAlignEligibleVar = (Var<Object>) largestAlignEligibleVarNode.getValue();
			_variableSize.setVariableSize(Helper.alignValue(_variableSize.getVariableSize(), largestAlignEligibleVar.getType().getSize()));
		}
		// Ist padding nicht 0, haben wir eine Anpassung vorgenommen (padded ist true).
		boolean padded = padding != 0;
		@SuppressWarnings("unchecked")
		Body<Object> body = (Body<Object>) bodyNode.getValue();
		body.setLargestVarNode(largestAlignEligibleVarNode);
		body.setPadded(padded);
		body.setSize(_variableSize);
		body.setStatements(bodyVec);
	}
	
	public static void parseBodySingleStatement(VariableSize variableSize, Vector<Node<Object>> bodyVec, History<Object> history, BodyFlag bodyFlag) {
		// Wir starten mit einem leeren Körperknoten.
		NodeFunctions.makeBodyNode(null, new VariableSize(0), false, null, bodyFlag, Scope.scopeCurrent(Parser.currentProcess));
		Node<Object> bodyNode = NodeFunctions.nodePop();
		// Setze den Elternknoten (den aktuellen Körperknoten) ein.
		bodyNode.setBinded(new NodeBinded<Object>());
		bodyNode.getBinded().setOwnerBody(Parser.parserCurrentBody);
		// Wechsel des aktuellen Körperknotens zu diesem neuen Körperknoten.
		Parser.parserCurrentBody = bodyNode;
		// Parse das single-Statement.
		Node<Object> stmtNode = null;
		Parser.parseStatement(Parser.historyDown(history, history.getFlags()));
		stmtNode = NodeFunctions.nodePop();
		bodyVec.vectorPush(stmtNode);
		// Ändere die Variablengröße, abhängig von der Größe des Statement-Knotens.
		Parser.parserAppendSizeForNode(history, variableSize, stmtNode);
		Node<Object> largestVarNode = null;
		// Beim Single-Statement ist die einzige Variable auch die größte Variable.
		if (stmtNode.getType() == NodeType.NODE_TYPE_VARIABLE) {
			largestVarNode = stmtNode;
		}
		Parser.markCurrentTokenVecIndexForCleanCodeAnalysis(bodyFlag, true);
		// Wir haben nur eine Variable in einem single-Statement, also übergeben wir 2x largestVarNode.
		Parser.parserFinalizeBody(history, bodyNode, bodyVec, variableSize, largestVarNode, largestVarNode);
		// Am Ende setzen wir den aktuellen Körperknoten zurück zum vorigen Körperknoten.
		Parser.parserCurrentBody = bodyNode.getBinded().getOwnerBody();
		NodeFunctions.nodePush(bodyNode);
	}
	
	public static void parseBodyMultipleStatements(VariableSize variableSize, Vector<Node<Object>> bodyVec, History<Object> history, BodyFlag bodyFlag) {
		// Erstelle einen leeren Körperknoten und nimm ihn vom Stack, um ihn zu bearbeiten.
		NodeFunctions.makeBodyNode(null, new VariableSize(), false, null, bodyFlag, Scope.scopeCurrent(Parser.currentProcess));
		Node<Object> bodyNode = NodeFunctions.nodePop();
		// Setzen den aktuellen Körperknoten als Elternknoten des neuen Körperknotens ein und
		// machen den neuen Körperknoten zum aktuellen Körperknoten.
		bodyNode.setBinded(new NodeBinded<Object>());
		bodyNode.getBinded().setOwnerBody(Parser.parserCurrentBody);
		// Setze den aktuellen Körperzeiger auf diesen Körper.
		Parser.parserCurrentBody = bodyNode;
		// Erklärungen: Siehe parseBodySingleStatement
		Node<Object> stmtNode = null;
		Node<Object> largestPossibleVarNode = null;
		Node<Object> largestAlignEligibleVarNode = null;
		// Nun erwarten wir geschwungende Klammern, die den Körper definieren. {}
		// Wir müssen die linke geschwungene Klammer vom Stack werfen.
		Parser.expectSym('{');
		// Solange wir die rechte geschwungene Klammer nicht gefunden haben, parsen wir das Statement im Körper.
		while(!(Parser.tokenNextIsSymbol('}'))) {
			// Geben Historyflags in das Parsen des Körpers hinab, damit klar ist, wenn wir uns beispielsweise im Global Scope oder einer Struktur befinden.
			int statementFlag = Parser.parseStatement(Parser.historyDown(history, history.getFlags()));
			// Finden wir einen access Specifier wie "public:", ist das kein Statement und wir setzen nur die entsprechende Flag für den aktuellen Körper.
			if (statementFlag == 1) continue;
			stmtNode = NodeFunctions.nodePop();
			// Wenn der größte mögliche Variablenknoten null ist oder seine größe kleiner ist als die Variable im aktuellen Statement,
			// setze den Statement-Knoten als größt-möglichen Variablenknoten.
			// -> erste Variable wird größter Variablenknoten. Danach werden alle Variablen größter Variablenknoten, deren größer gleich oder größer ist.
			if (stmtNode.getType() == NodeType.NODE_TYPE_VARIABLE) {
				if (largestPossibleVarNode == null) {
					largestPossibleVarNode = stmtNode;
				}
				else {
					@SuppressWarnings("unchecked")
					Var<Object> largestPossibleVar = (Var<Object>) largestPossibleVarNode.getValue();
					@SuppressWarnings("unchecked")
					Var<Object> stmtVar = (Var<Object>) stmtNode.getValue();
					if (largestPossibleVar.getType().getSize() <= stmtVar.getType().getSize()) {
						largestPossibleVarNode = stmtNode;
					}
				}
				// Handelt es sich um eine primitive Variable, gelten die gleichen Regeln für den größten Variablenknoten, der zur Anpassung qualifiziert ist.
				// -> Struct-Anpassung ist anders, sodass wir uns nur für die primitiven Variablen bei der Anpassung interessieren.
				if (NodeFunctions.variableNodeIsPrimitive(stmtNode)) {
					if (largestAlignEligibleVarNode == null) {
						largestAlignEligibleVarNode = stmtNode;
					}
					else {
						@SuppressWarnings("unchecked")
						Var<Object> largestAlignEligibleVar = (Var<Object>) largestAlignEligibleVarNode.getValue();
						@SuppressWarnings("unchecked")
						Var<Object> stmtVar = (Var<Object>) stmtNode.getValue();
						if (largestAlignEligibleVar.getType().getSize() <= stmtVar.getType().getSize()) {
							largestAlignEligibleVarNode = stmtNode;
						}
					}
				}
			}
			// Drücke den Statement-Knoten in den Körpervektor.
			bodyVec.vectorPush(stmtNode);
			// Wir müssen die Variablengröße anpassen, falls dieses Statement eine Variable ist.
			Parser.parserAppendSizeForNode(history, variableSize, NodeFunctions.variableNodeOrList(stmtNode));
		}
		Parser.markCurrentTokenVecIndexForCleanCodeAnalysis(bodyFlag, false);
		// Wirf die rechte geschwungene Klammer vom Stack.
		Parser.expectSym('}');
		Parser.parserFinalizeBody(history, bodyNode, bodyVec, variableSize, largestAlignEligibleVarNode, largestPossibleVarNode);
		// Am Ende setzen wir den aktuellen Körperknoten zurück auf den Elternknoten.
		// Bei einem rekursiven Aufruf wissen wir so bei allen Körpern was der Elternknoten war.
		Parser.parserCurrentBody = bodyNode.getBinded().getOwnerBody();
		// Schiebe den Körperknoten zurück in den Stack.
		NodeFunctions.nodePush(bodyNode);
	}
	
	/**
	 * 
	 * @param variableSize Wird auf die Summe aller Variablengrößen des geparseden Körpers gesetzt.
	 * @param history
	 */
	public static void parseBody(VariableSize variableSize, History<Object> history, BodyFlag bodyFlag) {
		// Erstellt eien neuen Scope für den Körper.
		Parser.parserScopeNew();
		// Lese alle Statements in dem Körper ein.
		int tmpSize = 0x00;
		// Haben wir noch keine Variablengröße, wird sie auf die temporäre Größe gesetzt.
		if (variableSize == null) {
			variableSize = new VariableSize(tmpSize);
		}
		// Erstelle einen Statementsvektor für alle Statements des Körpers.
		Vector<Node<Object>> bodyVec = Vector.vectorCreate();
		// Unterscheidung zwischen single-Statements wie "if (a) x = 5;" und multi-Statements,
		// die innerhalb von geschwungenen Klammern stehen.
		// 1. Fall: Finden wir keine '{', parsen wir ein single-Statement.
		if (!(Parser.tokenNextIsSymbol('{'))) {
			Parser.parseBodySingleStatement(variableSize, bodyVec, history, bodyFlag);
			Parser.parserScopeFinish();
			return;
		}
		// 2. Fall: Multi-Statement.
		// Wir haben einige Statements zwischen geschwungenen Klammern { int a; int b; int c; }.
		Parser.parseBodyMultipleStatements(variableSize, bodyVec, history, bodyFlag);
		// Beendet den Scope.
		Parser.parserScopeFinish();
		if (variableSize != null && variableSize.getVariableSize() > 0) {
			if (history.getFlags() == HistoryFlag.HISTORY_FLAG_INSIDE_FUNCTION_BODY) {
				@SuppressWarnings("unchecked")
				Function<Object> func = (Function<Object>) Parser.parserCurrentFunction.getValue();
				func.setStackSize(func.getStackSize() + variableSize.getVariableSize());
			}
		}
	}
	
	public static Node<Object> parseParentStruct(Node<Object> parentClassNode) {
		if (Parser.tokenNextIsSymbol(':')) {
			// Werfe den ':' ab.
			Parser.tokenNext();
			// Überspringe den "public" Access-Specifier, der zwingend angegeben werden muss.
			Token<Object> token = Parser.tokenPeekNext();
			if (!(SymbolTable.S_EQ((String) token.getValue(), "public"))) {
				Compiler.compilerError(Parser.currentProcess, "Hier wird der Access-Specifier \"public\" erwartet, aber etwas anderes wurde einegegeben.\n");
			}
			Parser.tokenNext();
			token = Parser.tokenNext();
			Symbol<Object> parentClassSym = Symresolver.symresolverGetSymbol(Parser.currentProcess, (String) token.getValue(), Scope.scopeCurrent(Parser.currentProcess), NodeType.NODE_TYPE_STRUCT);
			if (parentClassSym == null || parentClassSym.getType() != SymbolType.SYMBOL_TYPE_NODE) {
				Compiler.compilerError(Parser.currentProcess, "Es wurde eine nicht valide Elternklasse eingegeben.\n");
			}
			parentClassNode = (Node<Object>) parentClassSym.getData();
			// Überprüfen, ob Generics korrekt angegeben sind.
			@SuppressWarnings("unchecked")
			Struct<Object> parentClass = (Struct<Object>) parentClassNode.getValue();
			// Falls die Elternklasse keine Generics hat, sollten keine angegeben sein.
			if (Parser.tokenNextAtIsOperator(1, "<")) {
				Parser.tokenNext();
				Parser.parseGenerics(null,false, parentClass.getDtype(), true);
			}
		}
		return parentClassNode;
	}
	
	// Funktion, die die Logik des Parsens eines Strukturkörpers enthält.
	@SuppressWarnings("unchecked")
	public static void parseStructNoNewScope(Datatype<Node<Object>> dtype, boolean isForwardDeclaration, AccessSpecifierFlag accessSpecifier, boolean isClassDefinition) {
		Node<Object> bodyNode = null;
		VariableSize bodyVariableSize = new VariableSize();
		Node<Object> parentClassNode = null;
		// Ist eine Elternklasse angegeben? (Gibt es nicht für Forward-Declarations). Wird im Stile "subclass : public parentclass" angegeben. Structs können auch Elternstructs haben.
		parentClassNode = Parser.parseParentStruct(parentClassNode);
		dtype.setParentStructNode(parentClassNode);
		// Fall: Keine Forward-Declaration, beispielsweise: "struct dog {};".
		if (!(isForwardDeclaration)) {
			// Ist es ein Struct oder eine Klasse? Ist in diesem Compiler aufgrund der geringen Unterschiede zusammengefasst. Es wird eine Flag für die Identifikation eines Körpers angelegt.
			BodyFlag bodyFlag = null;
			if (isClassDefinition) {
				bodyFlag = BodyFlag.BODY_IS_FLAG_CLASS;
			}
			else {
				bodyFlag = BodyFlag.BODY_IS_FLAG_STRUCT;
			}
			Parser.numberOfNestedStructs++;
			// Parsen des Körpers und speichern des Körperknotens in der bodyNode-Variable.
			Parser.parseBody(bodyVariableSize, Parser.historyBegin(HistoryFlag.HISTORY_FLAG_INSIDE_STRUCTURE), bodyFlag);
			bodyNode = NodeFunctions.nodePop();
		}
		// Haben wir keinen Access-Specifier bisher gefunden, schauen wir auf den Elternknoten. Variablen und Funktionen in Structs werden als public deklariert.
		if (accessSpecifier == null) {
			if (bodyNode.getBinded().getOwnerBody() != null) {
				if (bodyNode.getBinded().getOwnerBody().getType() == NodeType.NODE_TYPE_STRUCT) {
					accessSpecifier = AccessSpecifierFlag.ACCESS_SPECIFIER_PUBLIC;
				}
			}
			else accessSpecifier = AccessSpecifierFlag.ACCESS_SPECIFIER_PRIVATE;
		}
		// Wenn wir NULL für den bodyNode übergeben, haben wir eine Forward-Declaration.
		NodeFunctions.makeStructNode(dtype.getTypeStr(), bodyNode, accessSpecifier, isClassDefinition, parentClassNode);
		Node<Object> structNode = NodeFunctions.nodePop();
		// Setze die Elternentität des Körperknotens auf das Struct.
		if (bodyNode != null) {
			Body<Object> structBody = (Body<Object>) bodyNode.getValue();
			dtype.setSize(structBody.getSize().getVariableSize());
			bodyNode.getBinded().setEntity(structNode);
		}
		dtype.setStructOrUnionNode(structNode);
		if (Token.tokenIsIdentifier(Parser.tokenPeekNext())) {
			Token<Object> varName = Parser.tokenNext();
			structNode.setFlags(structNode.getFlags() | NodeFlag.NODE_FLAG_HAS_VARIABLE_COMBINED);
			// Wenn kein Name für die Struktur angegeben ist, müssen wir einen festlegen.
			// Zum Beispiel "struct {} abc;" ist möglich. -> Kein Name des Datentyps vorhanden.
			// Hier wird aus "struct {} abc;" ein "struct abc {};" gemacht.
			Struct<Object> struct = (Struct<Object>) structNode.getValue();
			if ((dtype.getFlags() == DatatypeFlag.DATATYPE_FLAG_STRUCT_UNION_NO_NAME)) {
				dtype.setTypeStr((String) varName.getValue());
				dtype.setFlags(dtype.getFlags() & ~DatatypeFlag.DATATYPE_FLAG_STRUCT_UNION_NO_NAME);
				struct.setName((String) varName.getValue());
			}
			// Variablenknoten erstellen (und Offset berechnen) und als Variable des Structs setzen.
			Parser.makeVariableNodeAndRegister(Parser.historyBegin(0), dtype, varName, null, accessSpecifier, null);
			struct.setVar(NodeFunctions.nodePop());
		}
		// Alle Strukturen enden mit einem Semikolon.
		Parser.expectSym(';');
		// Drücke den Structknoten auf den Stack.
		NodeFunctions.nodePush(structNode);
		// Setz die aktuelle Entität wieder auf den Elternknoten nach dem Parsen.
		Parser.numberOfNestedStructs--;
	}
	
	// Diese Funktion parsed nur den Körper einer Union. Das Keyword "union" und der Bezeichner der Union wurden davor bereits geparsed.
	public static void parseUnionNoNewScope(Datatype<Node<Object>> dtype, boolean isForwardDeclaration, AccessSpecifierFlag accessSpecifier) {
		Node<Object> bodyNode = null;
		VariableSize bodyVariableSize = new VariableSize();
		// Ist es eine Forward-Declaration?
		if (!(isForwardDeclaration)) {
			Parser.parseBody(bodyVariableSize, Parser.historyBegin(HistoryFlag.HISTORY_FLAG_INSIDE_UNION), BodyFlag.BODY_IS_FLAG_UNION);
			bodyNode = NodeFunctions.nodePop();
		}
		// Erstellen des Unionknotens.
		NodeFunctions.makeUnionNode(dtype.getTypeStr(), bodyNode);
		Node<Object> unionNode = NodeFunctions.nodePop();
		// Setze die Elternentität des Körperknotens auf die Union.
		if (bodyNode != null) {
			@SuppressWarnings("unchecked")
			Body<Object> body = (Body<Object>) bodyNode.getValue();
			dtype.setSize(body.getSize().getVariableSize());
			bodyNode.getBinded().setOwnerBody(unionNode);
		}
		// Haben wir einen Bezeichner für die Union-Variable (nicht den Namen der Union, sondern einer Variable vom Typ der Union)?
		// -> Dann erstellen wir einen Variablenknoten vom Typ der Union.
		if (Parser.tokenPeekNext().getType() == TokenType.TOKEN_TYPE_IDENTIFIER) {
			Token<Object> varName = Parser.tokenNext();
			unionNode.setFlags(unionNode.getFlags() | NodeFlag.NODE_FLAG_HAS_VARIABLE_COMBINED);
			Parser.makeVariableNodeAndRegister(Parser.historyBegin(0), dtype, varName, null, null, null);
			@SuppressWarnings("unchecked")
			Union<Object> union = (Union<Object>) unionNode.getValue();
			union.setVar(NodeFunctions.nodePop());
		}
		Parser.expectSym(';');
		NodeFunctions.nodePush(unionNode);
	}
	
	// Funktioniert wie parseStruct.
	public static void parseUnion(Datatype<Node<Object>> dtype, AccessSpecifierFlag accessSpecifier) {
		boolean isForwardDeclaration = !(Token.tokenIsSymbol(Parser.tokenPeekNext(), '{'));
		if (!(isForwardDeclaration)) {
			Parser.parserScopeNew();
		}
		Parser.parseUnionNoNewScope(dtype, isForwardDeclaration, accessSpecifier);
		if (!(isForwardDeclaration)) {
			Parser.parserScopeFinish();
		}
	}
	
	public static void parseStruct(Datatype<Node<Object>> dtype, AccessSpecifierFlag accessSpecifier, boolean isClassDefinition) {
		// Wir müssen Forward-Declarations entdecken, weil jemand statt
		// "struct abc {};" auch "struct abc;" schreiben könnte.
		// Finden wir die '{' nicht, ist es keine Forward-Declaration. Ansonsten schon. Hier gibt es verschiedene Fälle (Elternklasse, Generics, throw()).
		boolean isForwardDeclaration = !((Token.tokenIsSymbol(Parser.tokenPeekNext(), '{') || ((Token.tokenIsSymbol(Parser.tokenPeekNext(), ':') && Token.tokenIsSymbol(Parser.tokenPeekAt(3), '{')))));
		// Sind Generics angegeben?
		if (isForwardDeclaration) {
			Token<Object> token = Parser.tokenPeekNext();
			// Springe hinter die Elternklasse, falls eine existiert.
			boolean hasParentClass = false;
			if (Token.tokenIsSymbol(Parser.tokenPeekNext(), ':')) {
				hasParentClass = true;
			}
			int index = 0;
			if (hasParentClass) {
				index = 3;
				token = Parser.tokenPeekAt(index);
			}
			while(token.getType() != TokenType.TOKEN_TYPE_SYMBOL) {
				token = Parser.tokenPeekAt(index);
				index++;
			}
			if ((char) token.getValue() == '{') {
				isForwardDeclaration = false;
			}
		}
		// Wenn es keine Forward-Declaration ist, müssen wir ein Scope für die Struktur anlegen.
		if (!(isForwardDeclaration)) {
			Parser.parserScopeNew();
		}
		// Parsen des Structs.
		Parser.parseStructNoNewScope(dtype, isForwardDeclaration, accessSpecifier, isClassDefinition);
		// Beende den Scope, falls es keine Forward-Declaration ist.
		if (!(isForwardDeclaration)) {
			Parser.parserScopeFinish();
		}
	}
	
	public static void parseStructOrUnion(Datatype<Node<Object>> dtype, AccessSpecifierFlag accessSpecifier) {
		switch(dtype.getType()) {
			case DATA_TYPE_STRUCT: {
				Parser.currentEntityName = dtype.getTypeStr();
				Parser.parseStruct(dtype, accessSpecifier, false);
				Parser.currentEntityName = null;
				break;
			}
			case DATA_TYPE_CLASS: {
				Parser.currentEntityName = dtype.getTypeStr();
				Parser.parseStruct(dtype, accessSpecifier, true);
				Parser.currentEntityName = null;
				break;
			}
			case DATA_TYPE_UNION: {
				Parser.currentEntityName = dtype.getTypeStr();
				Parser.parseUnion(dtype, accessSpecifier);
				Parser.currentEntityName = null;
				break;
			}
			default:
				Compiler.compilerError(Parser.currentProcess, "COMPILER-FEHLER: Der bereitgestellte Datentyp ist weder eine Struktur, noch eine Klasse, noch eine Union.\n");
		}
	}
	
	public static void tokenReadDots(int amount) {
		for(int i = 0; i < amount; i++) {
			Parser.expectOp(".");
		}
	}
	
	public static void parseVariableFull(History<Object> history, AccessSpecifierFlag accessSpecifier) {
		// Ist ein Namensraum vorhanden?
		if (Parser.insideFunctionArgumentParsing) {
			if (Token.tokenIsOperator(Parser.tokenPeekAt(1), "::")) {
				Parser.parseIdentifier(history);
				Node<Object> namespaceIdentifierNode = NodeFunctions.nodePop();
				String namespaceIdentifier = (String) namespaceIdentifierNode.getValue();
				Parser.expectOp("::");
				if (SymbolTable.S_EQ(namespaceIdentifier, "std")) {
					Parser.temporaryStdNamespaceFlag = true;
				}
				else {
					Scope<Object> namespaceScope = Parser.getDeclaredNamespaceOrStructBodyScope(history, namespaceIdentifier);
					if (namespaceScope == null) {
						Compiler.compilerError(Parser.currentProcess, "Der Namensraum \"" + namespaceIdentifier + "\" existiert nicht.\n");
					}
					Vector<Object> entitiesVec = namespaceScope.getEntities();
					Token<Object> nameToken = Parser.tokenPeekNext();
					boolean dtypeIsExistent = false;
					for(int i = 0; i < entitiesVec.vectorCount(); i++) {
						@SuppressWarnings("unchecked")
						Node<Object> node = (Node<Object>) entitiesVec.vectorPeekAt(i);
						if (node.getType() == NodeType.NODE_TYPE_STRUCT) {
							@SuppressWarnings("unchecked")
							Struct<Object> struct = (Struct<Object>) node.getValue();
							if (SymbolTable.S_EQ(struct.getName(), (String) nameToken.getValue())) {
								dtypeIsExistent = true;
							}
						}
					}
					if (!(dtypeIsExistent)) {
						Compiler.compilerError(Parser.currentProcess, "Der Datentyp \"" + (String) nameToken.getValue() + "\" im Namensraum \"" + namespaceIdentifier + "\" existiert nicht.\n");
					}
					Parser.identifierIsStructDeclarationMethodOrAssignment = true;
				}
			}
		}
		Datatype<Node<Object>> dtype = new Datatype<Node<Object>>();
		Parser.parseDatatype(dtype);
		Token<Object> nameToken = null;
		if (Parser.tokenPeekNext().getType() == TokenType.TOKEN_TYPE_IDENTIFIER) {
			nameToken = Parser.tokenNext();
		}
		Parser.parseVariable(dtype, nameToken, history, accessSpecifier, null);
	}
	
	public static Vector<Node<Object>> parseFunctionArguments(History<Object> history) {
		Parser.insideFunctionArgumentParsing = true;
		Parser.parserScopeNew();
		Vector<Node<Object>> argumentsVec = Vector.vectorCreate();
		// Parsen der Übergabeparameter, bis eine rechte Klammer gefunden wird.
		while(!(Parser.tokenNextIsSymbol(')'))) {
			if (Parser.tokenNextIsOperator(".")) {
				// Wir können "..." am Ende jeder Funktion verwenden, um eine Variablenliste (variante Funktion) zu markieren).
				// erlaubt unendlich viele Argumente bzw. Funktionsparameter.
				Parser.tokenReadDots(3);
				// ... kann nur am Ende stehen.
				Parser.parserScopeFinish();
				return argumentsVec;
			}
			boolean formerIdentifierIsStructDeclarationOrAssignment = Parser.identifierIsStructDeclarationMethodOrAssignment;
			Parser.identifierIsStructDeclarationMethodOrAssignment = false;
			// Alle Variablen die Funktionsparameter sind, sind Upwardstack-Variablen (wir müssen im Stack nach oben gehen, um die Variablen zu erreichen).
			// EBP + 8 zum Beispiel wäre eine Adresse eines Funktionsparameters.
			Parser.parseVariableFull(Parser.historyDown(history, history.getFlags() | HistoryFlag.HISTORY_FLAG_IS_UPWARD_STACK), AccessSpecifierFlag.ACCESS_SPECIFIER_PRIVATE);
			// Nimm den Funktionsparameter aus dem Stack und schiebe ihn in unseren Argumentenvektor der Funktion.
			Node<Object> argumentNode = NodeFunctions.nodePop();
			argumentsVec.vectorPush(argumentNode);
			// Finden wir ein Komma, haben wir weitere Argumente zum Parsen. Finden wir kein Komma, sind wir fertig.
			if (!(Parser.tokenNextIsOperator(","))) {
				break;
			}
			Parser.identifierIsStructDeclarationMethodOrAssignment = formerIdentifierIsStructDeclarationOrAssignment;
			// Komma gefunden, also wird das Komma vom Stack gezogen.
			Parser.tokenNext();
		}
		Parser.parserScopeFinish();
		Parser.insideFunctionArgumentParsing = false;
		return argumentsVec;
	}
	
	public static void parseForwardDeclaration(Datatype<Node<Object>> dtype, AccessSpecifierFlag accessSpecifier, boolean isClassDefinition) {
		// Da es sich um eine Forward-Declaration handelt, parse das Struct.
		Parser.parseStruct(dtype, accessSpecifier, isClassDefinition);
	}
	
	public static int parseNamespaceProclamationWithoutAccessSpecifier(History<Object> history) {
		// Parse den Namensraum oder Klassennamen.
		int index = 1;
		Token<Object> token = Parser.tokenPeekAt(index);
		Node<Object> namespaceIdentifierNode = Parser.parseNamespaceOrStructAsNamespaceNotation(history, true);
		// Gibt es einen Funktionskörper / Ist es eine Funktionsdeklaration?
		while(!(Token.tokenIsSymbol(token, ')'))) {
			index++;
			token = Parser.tokenPeekAt(index);
		}
		// Ist es der Aufruf einer Funktion?
		if (!(Parser.possibleFunctionCallInsideExpression) && Parser.identifierIsPossibleFunctionCall(history, (String) Parser.tokenPeekAt(1).getValue(), namespaceIdentifierNode) && !(Token.tokenIsSymbol(Parser.tokenPeekAt(index + 1), '{'))) {
			NodeFunctions.nodePush(namespaceIdentifierNode);
			return 0;
		}
		Parser.expectOp("::");
		// Da wir uns außerhalb der Klasse befinden oder des Namensraums, nehmen wir den Access-Specifier "public" an.
		return Parser.parseVariableFunctionOrStructUnionDefinedAfterAccessSpecifierAndNamespace(history, AccessSpecifierFlag.ACCESS_SPECIFIER_PUBLIC, Parser.tokenPeekNext(), namespaceIdentifierNode);
	}
	
	public static int parseVariableFunctionOrStructUnionDefinedAfterAccessSpecifierAndNamespace(History<Object> history, AccessSpecifierFlag accessSpecifier, Token<Object> token, Node<Object> namespaceIdentifierNode) {
		if (namespaceIdentifierNode != null && SymbolTable.S_EQ((String) namespaceIdentifierNode.getValue(), currentEntityName)) {
			Compiler.compilerError(Parser.currentProcess, "Sie können den Namensraum der Klasse \"" + Parser.currentEntityName + "\" nicht erneut innerhalb der Klasse referenzieren.\n");
		}
		// 1. Fall: Gilt nur für eine Variable oder Methode.
		if (SymbolTable.isKeywordVariableModifier((String) token.getValue()) || SymbolTable.keywordIsDatatype((String) token.getValue())) {
			Parser.parseVariableFunctionOrStructUnion(history, accessSpecifier);
			return -1;
		}
		// 2. Fall: Konstruktor der aktuellen Klasse oder befreundeter Konstruktor einer anderen Klasse
		else if (Parser.identifierIsConstructorOrObjectOrMethod(token, namespaceIdentifierNode)) {
			token = Parser.tokenPeekAt(1);
			Parser.functionStartLineTokenVecIndex = Parser.currentProcess.getTokenVec().getPeekIndex();
			if (token.getType() == TokenType.TOKEN_TYPE_OPERATOR && SymbolTable.S_EQ((String) token.getValue(), "(")) {
				Parser.parseConstructorOrDestructor(Parser.tokenNext(), history, accessSpecifier, false, namespaceIdentifierNode);
			}
			// 3. Fall: Klassenvariable oder Methode (oder Operatorüberladen).
			else if (token.getType() == TokenType.TOKEN_TYPE_IDENTIFIER || (token.getType() == TokenType.TOKEN_TYPE_KEYWORD && (SymbolTable.S_EQ((String) token.getValue(), "operator")))) {
				Parser.parseVariableFunctionOrStructUnion(history, accessSpecifier);
			}
			return -1;
		}
		// 4. Fall: Destruktor der aktuellen Klasse oder befreundeter Destruktur einer anderen Klasse.
		else if (Parser.tokenNextIsOperator("~")) {
			if (Parser.identifierIsConstructorOrObjectOrMethod(Parser.tokenPeekAt(1), namespaceIdentifierNode)) {
				Parser.currentDestructorTildeToken = Parser.tokenPeekNext();
				Parser.functionStartLineTokenVecIndex = Parser.currentProcess.getTokenVec().getPeekIndex();
				Parser.expectOp("~");
				Parser.parseConstructorOrDestructor(Parser.tokenNext(), history, accessSpecifier, true, namespaceIdentifierNode);
				return -1;
			}
			Compiler.compilerError(Parser.currentProcess, "Hier wird ein Destruktor der Klasse \"" + Parser.currentEntityName + "\" erwartet.\n");
		}
		// 5. Fall: Parsen beispielsweise einer Klassendefinition oder einer Variable.
		else if (token.getType() == TokenType.TOKEN_TYPE_IDENTIFIER && (Token.tokenIsSymbol(Parser.tokenPeekAt(1), ';')) || Token.tokenIsOperator(Parser.tokenPeekAt(1), "(")) {
			// Ist die Variable eine Struct-Deklaration?
			if (Parser.getNamespaceNodeIfExisting(history, (String) namespaceIdentifierNode.getValue()) != null) {
				Parser.identifierIsStructDeclarationMethodOrAssignment = true;
			}
			Parser.parseVariableFunctionOrStructUnion(history, accessSpecifier);
			Parser.identifierIsStructDeclarationMethodOrAssignment = false;
			return -1;
		}
		// 6. Fall: Globales Funktion mit einem Struct-Rückgabetyp.
		else if (Scope.scopeCurrent(Parser.currentProcess).getParent() == null && Parser.getNamespaceNodeIfExisting(history, (String) token.getValue()) != null) {
			Parser.identifierIsStructDeclarationMethodOrAssignment = true;
			Parser.parseVariableFunctionOrStructUnion(history, accessSpecifier);
			Parser.identifierIsStructDeclarationMethodOrAssignment = false;
			return -1;
		}
		// 1. Fehler: Nicht existierender Namensraum.
		else if (Parser.tokenPeekAt(1).getType() == TokenType.TOKEN_TYPE_OPERATOR && SymbolTable.S_EQ((String) Parser.tokenPeekAt(1).getValue(), "::")) {
			Compiler.compilerError(Parser.currentProcess, "Der von Ihnen angegebene Namensraum \"" + (String) token.getValue() + "\" existiert nicht.\n");
		}
		Compiler.compilerError(Parser.currentProcess, "Nach einem Access Specifier muss das Symbol ':' oder eine Variable oder eine Funktion folgen, aber etwas anderes wurde eingegeben. Letzteres gilt ebenfalls für das Keyword \"friend\".\n");
		return 0;
	}
	
	public static int parseAccessSpecifier(History<Object> history) {
		// Wirf den Access Specifier vom Stack.
		Token<Object> token = Parser.tokenNext();
		Token<Object> possibleFriendToken = Parser.tokenPeekNext();
		// Welcher Access Specifier ist es?
		AccessSpecifierFlag accessSpecifier = null;
		if (SymbolTable.S_EQ((String) token.getValue(), "protected")) {
			accessSpecifier = AccessSpecifierFlag.ACCESS_SPECIFIER_PROTECTED;
		} 
		else if (SymbolTable.S_EQ((String) token.getValue(), "public")) {
			accessSpecifier =  AccessSpecifierFlag.ACCESS_SPECIFIER_PUBLIC;
		}
		else {
			accessSpecifier = AccessSpecifierFlag.ACCESS_SPECIFIER_PRIVATE;
		}
		token = Parser.tokenPeekNext();
		// 1. Fall: "friend".
		if (possibleFriendToken.getType() == TokenType.TOKEN_TYPE_KEYWORD && SymbolTable.S_EQ((String) possibleFriendToken.getValue(), "friend")) {
			return Parser.parseFriend(history, accessSpecifier);
		}
		// Parsen einer möglichen Namensraum oder Klassenangabe in der Form "classname::foo()".
		Node<Object> namespaceIdentifierNode = null;
		if (token.getType() == TokenType.TOKEN_TYPE_IDENTIFIER && Parser.isNamespaceOrStructAsNamespaceNotation(history, (String) token.getValue())) {
			namespaceIdentifierNode = Parser.parseNamespaceOrStructAsNamespaceNotation(history, false);
			token = Parser.tokenPeekNext();
		}
		// 2. Fall: Ankündigung für folgende Variablen und Methoden:
		if (Parser.tokenNextIsSymbol(':')) {
			if (Parser.parserCurrentBody != null) {
				@SuppressWarnings("unchecked")
				Body<Object> body = (Body<Object>) Parser.parserCurrentBody.getValue();
				body.setAccessSpecifierFlag(accessSpecifier);
				Parser.tokenNext();
				// Geben 1 zurück, damit beim Körper parsen kein Statement erwartet wird.
				return 1;
			}
			Compiler.compilerError(Parser.currentProcess, "Globale Access-Specifier Ankündigungen werden von diesem Compiler nicht unterstützt.\n");
		}
		// Die anderen Fälle werden in einer eigenen Methode abgehandelt.
		return Parser.parseVariableFunctionOrStructUnionDefinedAfterAccessSpecifierAndNamespace(history, accessSpecifier, token, namespaceIdentifierNode);
	}
	
	public static Datatype<Node<Object>> getExistingStructDatatypeByTypeStr(History<Object> history, String typeStr) {
		Node<Object> namespaceNode = Parser.getNamespaceNodeIfExisting(history, typeStr);
		if (namespaceNode == null || namespaceNode.getType() != NodeType.NODE_TYPE_STRUCT) {
			return null;
		}
		@SuppressWarnings("unchecked")
		Struct<Object> struct = (Struct<Object>) namespaceNode.getValue();
		if (Parser.currentSUDatatype != null && SymbolTable.S_EQ(struct.getName(), Parser.currentSUDatatype.getTypeStr())) {
			return Parser.currentSUDatatype;
		}
		return DatatypeFunctions.memcpy(struct.getDtype());
	}
	
	// Wird aufgerufen, wenn es sich um einen Konstruktor handelt. Wird in eine Funktion geparsed, die den Datentyp der Klasse des Konstruktors enthält.
	public static void parseConstructorOrDestructor(Token<Object> token, History<Object> history, AccessSpecifierFlag accessSpecifier, boolean isDestructor, Node<Object> namespaceIdentifierNode) {
		FunctionFlag functionFlag = FunctionFlag.FUNCTION_NODE_FLAG_IS_NATIVE_CONSTRUCTOR;
		if (isDestructor) {
			functionFlag = FunctionFlag.FUNCTION_NODE_FLAG_IS_NATIVE_DESTRUCTOR;
		}
		//Checke, ob es sich um eine Funktionsdeklaration handelt.
		if (Parser.tokenNextIsOperator("(")) {
			AccessSpecifierFlag checkedAccessSpecifier = Parser.handleAccessSpecifier(accessSpecifier, Parser.parserCurrentBody);
			if (!(isDestructor)) {
				Datatype<Node<Object>> constructorDatatype = Parser.getExistingStructDatatypeByTypeStr(history, (String) token.getValue());
				Parser.parseFunction(constructorDatatype, token, history, checkedAccessSpecifier, functionFlag, namespaceIdentifierNode);
			}
			else {
				Parser.parseFunction(null, token, history, checkedAccessSpecifier, functionFlag, namespaceIdentifierNode);
			}
		}
	}
	
	public static boolean identifierIsConstructorOrObjectOrMethod(Token<Object> nameToken, Node<Object> namespaceIdentifierNode) {
		if (nameToken.getType() != TokenType.TOKEN_TYPE_IDENTIFIER) {
			return false;
		}
		String name = (String) nameToken.getValue();
		// In welcher Klassen befinden wir uns? Oder welcher Namensraum wurde in der Form "classname::foo()" genannt?
		String classOrStructName;
		if (namespaceIdentifierNode == null) {
			if (Parser.currentSUDatatype != null) {
				classOrStructName = Parser.currentSUDatatype.getTypeStr();
			}
			else {
				classOrStructName = Parser.currentEntityName;
			}
		}
		else {
			classOrStructName = (String) namespaceIdentifierNode.getValue();
		}
		// Ist der Bezeichner gleich der Bezeichner der Klasse?
		if (SymbolTable.S_EQ(classOrStructName, name)) {
			return true;
		}
		return false;
	}
	
	public static String accessSpecifierToString(AccessSpecifierFlag flag) {
		if (flag == AccessSpecifierFlag.ACCESS_SPECIFIER_PUBLIC) {
			return "public";
		}
		else if (flag == AccessSpecifierFlag.ACCESS_SPECIFIER_PROTECTED) {
			return "protected";
		}
		return "private";
	}
	
	public static AccessSpecifierFlag handleAccessSpecifier(AccessSpecifierFlag accessSpecifier, Node<Object> bodyNode) {
		if (bodyNode == null && accessSpecifier != null) {
			return accessSpecifier;
		}
		else if (bodyNode != null) {
			@SuppressWarnings("unchecked")
			Body<Object> currentBody = (Body<Object>) bodyNode.getValue();
			// Finden wir eine Access-Specifier-Flag im Körper und eine vor dem aktuellen Objekt, ist es ein Fehler.
			if (currentBody.getAccessSpecifierFlag() != null && accessSpecifier != null) {
				Compiler.compilerError(Parser.currentProcess, "Es ist nur ein Access-Specifier pro Entität erlaubt, sie haben jedoch \"" + Parser.accessSpecifierToString(accessSpecifier) + "\" und \"" + Parser.accessSpecifierToString(currentBody.getAccessSpecifierFlag()) + "\" angegeben.\n");
			}
			// Ist im Körper eine Access-Specifier-Flag gesetzt, nutzen wir diese für den accessSpecifier.
			else if (currentBody.getAccessSpecifierFlag() != null && accessSpecifier == null) {
				return currentBody.getAccessSpecifierFlag();
			}
			// Haben wir im Körper keine Flag gesetzt, aber eine vor der Entität, geben wir die vor der Entität zurück.
			else if (currentBody.getAccessSpecifierFlag() == null && accessSpecifier != null) {
				return accessSpecifier;
			}
			// Haben wir auch diese nicht, so sind Variablen in Struct und Unions immer public.
			else if (currentBody.getBodyFlag() == BodyFlag.BODY_IS_FLAG_STRUCT || currentBody.getBodyFlag() == BodyFlag.BODY_IS_FLAG_UNION) {
				return AccessSpecifierFlag.ACCESS_SPECIFIER_PUBLIC;
			}
		}
		// Standardmäßig nutzen wir in C++ den "private" Access-Specifier (außer im Wurzelscope, der ist immer sichtbar).
		if (Scope.scopeCurrent(Parser.currentProcess).getParent() == null) {
			return AccessSpecifierFlag.ACCESS_SPECIFIER_PUBLIC;
		}
		return AccessSpecifierFlag.ACCESS_SPECIFIER_PRIVATE;
	}
	
	public static Node<Object> parseNamespaceOrStructAsNamespaceNotation(History<Object> history, boolean insideExpression) {
		Parser.parseIdentifier(history);
		if (!(insideExpression)) {
			Parser.expectOp("::");
		}
		return NodeFunctions.nodePop();
	}
	
	public static void parseVariableFunctionOrStructUnion(History<Object> history, AccessSpecifierFlag accessSpecifier) {
		AccessSpecifierFlag checkedAccessSpecifier = Parser.handleAccessSpecifier(accessSpecifier, Parser.parserCurrentBody);
		CleanCodeObservation<Object> priorCurrentStructOrUnioNameObservation = Parser.currentStructOrUnionNameObservation;
		Integer priorFunctionStartLineTokenVecIndex = Parser.functionStartLineTokenVecIndex;
		Datatype<Node<Object>> dtype = new Datatype<Node<Object>>();
		Parser.parseDatatype(dtype);
		// Checke, ob es eine Strukturdeklaration ist.
		// Finden wir eine Struktur oder eine Union und das nächste Token ist eine öffnende geschweifte Klammer, haben wir eine Struktur gefunden.
		if(DatatypeFunctions.datatypeIsStructOrUnion(dtype) && (Parser.tokenNextIsSymbol('{') || Parser.tokenNextIsSymbol(':'))) {
			NodeFunctions.makeStructNode(dtype.getTypeStr(), null, checkedAccessSpecifier, false, null);
			Node<Object> node = NodeFunctions.nodePop();
			if (Parser.currentStructOrUnionNameObservation != null) {
				Parser.currentStructOrUnionNameObservation.setEntity(node);
				Parser.currentStructOrUnionNameObservation = priorCurrentStructOrUnioNameObservation;
			}
			Symresolver.symresolverBuildForNode(Parser.currentProcess, null, node, Scope.scopeCurrent(Parser.currentProcess), null);
			Datatype<Node<Object>> parentDtype = Parser.currentSUDatatype;
			Parser.currentSUDatatype = dtype; // Setzen eine globale Variable, um einen Konstruktor in der Struktur/Klasse auf den Datentyp zu setzen.
			Parser.parseStructOrUnion(dtype, checkedAccessSpecifier);
			// suNode -> Struct oder Union Knoten.
			Node<Object> suNode = NodeFunctions.nodePop();
			if (suNode.getType() == NodeType.NODE_TYPE_STRUCT) {
				@SuppressWarnings("unchecked")
				Struct<Object> struct = (Struct<Object>) node.getValue();
				struct.cloneFromStructNode(suNode);
				if (struct.getBodyN() != null) {
					struct.getBodyN().getBinded().setEntity(node);
				}
				struct.setDtype(dtype);
				if (struct.getBodyN() != null) {
					// Analysiere den Strukturköper.
					CleanCodeAnalyzer.analyzeStructBody(Parser.currentProcess, dtype, Parser.structEndLineTokenVecIndex, node, struct.getBodyN(), Parser.numberOfNestedStructs);
					Parser.structEndLineTokenVecIndex = Parser.priorStructEndLineTokenVecIndex;
				}
			}
			else if (suNode.getType() == NodeType.NODE_TYPE_UNION) {
				@SuppressWarnings("unchecked")
				Union<Object> union = (Union<Object>) node.getValue();
				if (union.getBodyN() != null) {
					union.getBodyN().getBinded().setEntity(node);
				}
			}
			NodeFunctions.nodePush(node);
			// Setzen die globale Variable zurück und erstellen eine Shallow-Copy des Datentyps.
			// Falls ein Konstruktor das Datentyp-Objekt verwendet, teilen sich die Objekte nicht die Referenzen.
			dtype = DatatypeFunctions.memcpy(dtype);
			Parser.currentSUDatatype = parentDtype;
			Parser.functionStartLineTokenVecIndex = priorFunctionStartLineTokenVecIndex;
			return;
		}
		// Ist es eine Forward-Declaration?
		if (Parser.tokenNextIsSymbol(';')) {
			boolean isClassDefinition = false;
			if (dtype.getType() == DatatypeType.DATA_TYPE_CLASS) {
				isClassDefinition = true;
			}
			Parser.parseForwardDeclaration(dtype, checkedAccessSpecifier, isClassDefinition);
			return;
		}
		// Ignoriere Integer-Abkürzungen falls nötig, beispielsweise "long int" wird zu "long".
		Parser.parserIgnoreInt(dtype);
		// Ist ein Namensraum in der Form "classname::" oder "namespace::" angegeben?
		Node<Object> namespaceIdentifierNode = null;
		if (Parser.isNamespaceOrStructAsNamespaceNotation(history, (String) Parser.tokenPeekNext().getValue())) {
			namespaceIdentifierNode = Parser.parseNamespaceOrStructAsNamespaceNotation(history, false);
		}
		// Haben wir "int abc", ist nach dem "int" (oben in den Datatype geparsed) nun das "abc", der Identifier auf dem Token-Stack.
		Token<Object> nameToken = Parser.tokenNext();
		// Überladen wir gerade einen Operator?
		if (Parser.functionIsOperatorFlag) {
			// Wir nehmen das "operator"-Token vom Stack.
			nameToken = Parser.tokenNext();
			// Wir setzen den zu überladenen Operator als Identifier ein, da er nun der Funktionsname wird.
			nameToken.setType(TokenType.TOKEN_TYPE_IDENTIFIER);
		}
		// Hat jemand so etwas wie "int 49" eingegeben, steht hier kein Identifier.
		if (nameToken.getType() != TokenType.TOKEN_TYPE_IDENTIFIER) {
			Compiler.compilerError(Parser.currentProcess, "Erwarten einen validen Namen für die gegebene Variablendeklaration.\n");
		}
		//Checke, ob es sich um eine Funktionsdeklaration handelt.
		if (Parser.tokenNextIsOperator("(")) {
			FunctionFlag flags = FunctionFlag.FUNCTION_NODE_FLAG_IS_NATIVE;
			if (Parser.functionIsOperatorFlag) {
				flags = FunctionFlag.FUNCTION_NODE_FLAG_IS_NATIVE_OVERLOADED_OPERATOR;
			}
			Parser.parseFunction(dtype, nameToken, history, checkedAccessSpecifier, flags, namespaceIdentifierNode);
			return;
		}
		// Falls nicht, Variable parsen.
		Parser.parseVariable(dtype, nameToken, history, checkedAccessSpecifier, namespaceIdentifierNode);
		
		// Schauen, ob mehrere Variablen hintereinander deklariert werden.
		// Beispielsweise "int x, e, d = 50;".
		// Dann legen wir eine Variablenliste in einem Vektor an.
		if (Token.tokenIsOperator(Parser.tokenPeekNext(), ",")) {
			Vector<Node<Object>> varList = Vector.vectorCreate();
			// Nimm die erste Variable vom Stack und gebe Sie in die Variablenliste.
			Node<Object> varNode = NodeFunctions.nodePop();
			varList.vectorPush(varNode);
			// Bearbeite alle weiteren Variablen.
			while(Token.tokenIsOperator(Parser.tokenPeekNext(), ",")) {
				Parser.tokenNext(); // Schmeiße das Komma vom Stack
				// Nimm den Identifier vom Tokenstack und speichere ihn in einer Variable.
				nameToken = Parser.tokenNext(); 
				Parser.currentEntityName = (String) nameToken.getValue();
				Parser.parseVariable(dtype, nameToken, history, checkedAccessSpecifier, namespaceIdentifierNode);
				// Nimm den Knoten vom Nodestack und gebe ihn in die Variablenliste.
				varNode = NodeFunctions.nodePop();
				varList.vectorPush(varNode);
			}
			// Lege die Variablenliste in einem Knoten an.
			Parser.makeVariableListNode(varList);
		}
		// Erwarte ein Semikolon (außer im Sonderfall forEach-Schleife.
		if (!(Parser.insideForEachLoop)) {
			Parser.expectSym(';');
		}
		if (Parser.possibleFunctionCallInsideExpression) {
			Parser.possibleFunctionCallInsideExpression = false;
		}
	}
	
	public static Node<Object> parseElse(History<Object> history, Integer elseStmtStartTokenVecIndex, Token<Object> keywordToken) {
		VariableSize varSize = new VariableSize();
		Parser.parseBody(varSize, history, BodyFlag.BODY_IS_FLAG_ELSE_STATEMENT);
		Node<Object> bodyNode = NodeFunctions.nodePop();
		NodeFunctions.makeElseNode(bodyNode);
		Node<Object> elseNode = NodeFunctions.nodePop();
		CleanCodeAnalyzer.analyzeStmt(Parser.currentProcess, keywordToken, elseNode, bodyNode, elseStmtStartTokenVecIndex, Parser.elseStmtEndTokenVecIndex, null);
		Parser.elseStmtEndTokenVecIndex = Parser.priorElseStmtEndTokenVecIndex;
		return elseNode;
	}
	
	public static Node<Object> parseElseOrElseIf(History<Object> history) {
		Node<Object> node = null;
		int elseStmtStartTokenVecIndex = Parser.currentProcess.getTokenVec().getPeekIndex();
		Token<Object> keywordToken = Parser.tokenPeekNext();
		if (Parser.tokenNextIsKeyword("else")) {
			// Wir haben ein else oder ein else if gefunden.
			// Wirf das "else" vom Stack.
			Parser.tokenNext();
			if (Parser.tokenNextIsKeyword("if")) {
				// Wir haben ein else if gefunden (kein else).
				Parser.parseIfStmt(Parser.historyDown(history, 0), elseStmtStartTokenVecIndex, keywordToken);
				node = NodeFunctions.nodePop();
				return node;
			}
			// Es ist ein else-Statement.
			Parser.parseElse(Parser.historyDown(history, 0), elseStmtStartTokenVecIndex, keywordToken);
		}
		return node;
	}
	
	public static void parseIfStmt(History<Object> history, Integer elseIfStmtStartTokenVecIndex, Token<Object> elseKeywordToken) {
		int ifStmtStartTokenVecIndex;
		Token<Object> keywordToken = null;
		String additionalKeyword = null;
		if (elseKeywordToken == null) {
			ifStmtStartTokenVecIndex = Parser.currentProcess.getTokenVec().getPeekIndex();
			keywordToken = Parser.tokenPeekNext();
		}
		else {
			additionalKeyword = "if";
			ifStmtStartTokenVecIndex = elseIfStmtStartTokenVecIndex;
			keywordToken = elseKeywordToken;
		}
		Parser.expectKeyword("if");
		Parser.expectOp("(");
		// Parse die Bedingung, alles in den runden Klammern ( ).
		Parser.parseExpressionableRoot(history);
		Parser.expectSym(')');
		// Wirf den Bedingungsknoten vom Stack.
		Node<Object> condNode = NodeFunctions.nodePop();
		VariableSize varSize = new VariableSize();
		// Parse den Körper des Statements, alles in den geschwungenen Klammern { }.
		Parser.parseBody(varSize, history, BodyFlag.BODY_IS_FLAG_IF_STATEMENT);
		// Wirf den Körperknoten vom Stack.
		Node<Object> bodyNode = NodeFunctions.nodePop();
		// Erstelle den If-Knoten. Next-Knoten ist vorerst NULL.
		NodeFunctions.makeIfNode(condNode, bodyNode, Parser.parseElseOrElseIf(history));
		Node<Object> ifNode = NodeFunctions.nodePeek();
		CleanCodeAnalyzer.analyzeStmt(Parser.currentProcess, keywordToken, ifNode, bodyNode, ifStmtStartTokenVecIndex, Parser.ifStmtEndTokenVecIndex, additionalKeyword);
		Parser.ifStmtEndTokenVecIndex = Parser.priorIfStmtEndTokenVecIndex;
	}
	
	// Die Funktion parsed etwas in der Form "while (1)".
	public static void parseKeywordParenthesesExpression(String keyword) {
		Parser.expectKeyword(keyword);
		Parser.expectOp("(");
		Parser.parseExpressionableRoot(Parser.historyBegin(0));
		Parser.expectSym(')');
	}
	
	public static void parseDefault(History<Object> history) {
		int defaultStmtStartTokenVecIndex = Parser.currentProcess.getTokenVec().getPeekIndex();;
		Token<Object> keywordToken = Parser.tokenPeekNext();
		Parser.expectKeyword("default");
		Parser.expectSym(':');
		VariableSize varSize = new VariableSize(0);
		Parser.parseBody(varSize, history, BodyFlag.BODY_IS_FLAG_DEFAULT_STATEMENT);
		Node<Object> bodyNode = NodeFunctions.nodePop();
		NodeFunctions.makeDefaultNode(bodyNode);
		Node<Object> defaultNode = NodeFunctions.nodePop();
		if (history.get_switch() != null) {
			history.get_switch().getCaseData().setDefaultCaseNode(defaultNode);
		}
		else {
			Compiler.compilerError(Parser.currentProcess, "Vor einem default-Statement müssen Sie zuerst ein Switch-Statement beginnen.\n");
		}
		CleanCodeAnalyzer.analyzeStmt(Parser.currentProcess, keywordToken, defaultNode, bodyNode, defaultStmtStartTokenVecIndex, Parser.defaultStmtEndTokenVecIndex, null);
		Parser.defaultStmtEndTokenVecIndex = Parser.priorDefaultStmtEndTokenVecIndex;
	}
	
	public static void parseCatchStmt(History<Object> history) {
		int catchStmtStartTokenVecIndex = Parser.currentProcess.getTokenVec().getPeekIndex();;
		Token<Object> keywordToken = Parser.tokenPeekNext();
		Parser.expectKeyword("catch");
		Parser.expectOp("(");
		Parser.parseExpressionableRoot(history);
		Parser.expectSym(')');
		Node<Object> expNode = NodeFunctions.nodePop();
		VariableSize varSize = new VariableSize(0);
		Parser.parseBody(varSize, history, BodyFlag.BODY_IS_FLAG_CATCH_STATEMENT);
		Node<Object> bodyNode = NodeFunctions.nodePop();
		NodeFunctions.makeCatchNode(expNode, bodyNode);
		Node<Object> catchNode = NodeFunctions.nodePeek();
		CleanCodeAnalyzer.analyzeStmt(Parser.currentProcess, keywordToken, catchNode, bodyNode, catchStmtStartTokenVecIndex, Parser.catchStmtEndTokenVecIndex, null);
		Parser.catchStmtEndTokenVecIndex = Parser.priorCatchStmtEndTokenVecIndex;
	}
	
	public static void parseTryCatchStmt(History<Object> history) {
		if (Parser.parserCurrentFunction != null) {
			Parser.tryCatchBlockFlagInsideFunction = true;
		}
		int tryCatchStmtStartTokenVecIndex = Parser.currentProcess.getTokenVec().getPeekIndex();;
		Token<Object> keywordToken = Parser.tokenPeekNext();
		Parser.expectKeyword("try");
		VariableSize varSize = new VariableSize(0);
		Parser.parseBody(varSize, history, BodyFlag.BODY_IS_FLAG_TRY_CATCH_STATEMENT);
		Node<Object> bodyNode = NodeFunctions.nodePop();
		Vector<Node<Object>> catchExpNodes = Vector.vectorCreate();
		while(Parser.tokenNextIsKeyword("catch")) {
			Parser.parseCatchStmt(history);
			catchExpNodes.vectorPush(NodeFunctions.nodePop());
		}
		NodeFunctions.makeTryCatchNode(bodyNode, catchExpNodes);
		Node<Object> tryCatchNode = NodeFunctions.nodePeek();
		CleanCodeAnalyzer.analyzeStmt(Parser.currentProcess, keywordToken, tryCatchNode, bodyNode, tryCatchStmtStartTokenVecIndex, Parser.tryCatchStmtEndTokenVecIndex, "catch");
		Parser.tryCatchStmtEndTokenVecIndex = Parser.priorTryCatchStmtEndTokenVecIndex;
	}
	
	public static void parseThrowInsideFunctionHead(History<Object> history) {
		Vector<Datatype<Node<Object>>> exceptionDtypesVec = null;
		Parser.expectKeyword("throw");
		exceptionDtypesVec = Vector.vectorCreate();
		Parser.expectOp("(");
		Token<Object> token = Parser.tokenPeekNext();
		Parser.insideThrowInsideFunctionHead = true;
		while(!(Token.tokenIsSymbol(token, ')'))) {
			Datatype<Node<Object>> exceptionDtype = new Datatype<Node<Object>>();
			Parser.parseDatatype(exceptionDtype);
			exceptionDtypesVec.vectorPush(exceptionDtype);
			if (!(Parser.tokenNextIsSymbol(')'))) {
				Parser.expectOp(",");
			}
			token = Parser.tokenPeekNext();
		}
		Parser.insideThrowInsideFunctionHead = false;
		Parser.expectSym(')');
		NodeFunctions.makeThrowInsideFunctionHeadNode(exceptionDtypesVec);		
	}
	
	public static void parseThrow(History<Object> history) {
		Node<Object> expNode = null;
		Parser.expectKeyword("throw");
		if (!(Parser.tokenNextIsSymbol(';'))) {
			Parser.parseExpressionableRoot(history);
			expNode = NodeFunctions.nodePop();
		}
		NodeFunctions.makeThrowNode(expNode);
		Parser.expectSym(';');
	}
	
	// Beispiel: "case 5:". Parsed einen Fall eines Switch-Statements.
	public static void parseCase(History<Object> history) {
		int caseStmtStartTokenVecIndex = Parser.currentProcess.getTokenVec().getPeekIndex();;
		Token<Object> keywordToken = Parser.tokenPeekNext();
		Parser.expectKeyword("case");
		// Parse die Expression (im Beispiel die 5) und nimm sie vom Stack.
		Parser.parseExpressionableRoot(history);
		Node<Object> caseExpNode = NodeFunctions.nodePop();
		Parser.expectSym(':');
		VariableSize variableSize = new VariableSize(0);
		// Während wir den Körper parsen, können wir die Informationen zu den Fällen im Case-Vektor von History speichern.
		Parser.parseBody(variableSize, history, BodyFlag.BODY_IS_FLAG_CASE_STATEMENT);
		Node<Object> bodyNode = NodeFunctions.nodePop();
		NodeFunctions.makeCaseNode(caseExpNode, bodyNode);
		// Unterstützen nur Zahlen als Fälle, wobei Definitionen wie "case APPLE: " auch unterstützt werden.
		if (caseExpNode.getType() != NodeType.NODE_TYPE_NUMBER) {
			Compiler.compilerError(Parser.currentProcess, "Wir unterstützen nur Zahlen als Fälle in unserem Subset von C++ zu diesem Zeitpunkt.\n");
		}
		// Nimm den Fall vom Stack und registriere ihn in der History.
		Node<Object> caseNode = NodeFunctions.nodePop();
		Parser.parserRegisterCase(history, caseNode);
		CleanCodeAnalyzer.analyzeStmt(Parser.currentProcess, keywordToken, caseNode, bodyNode, caseStmtStartTokenVecIndex, Parser.caseStmtEndTokenVecIndex, null);
		Parser.caseStmtEndTokenVecIndex = Parser.priorCaseStmtEndTokenVecIndex;
	}
	
	public static void parseSwitch(History<Object> history) {
		Token<Object> keywordToken = Parser.tokenPeekNext();
		ParserHistorySwitch<Object> _switch = Parser.parserNewSwitchStatement(history);
		Parser.parseKeywordParenthesesExpression("switch");
		Node<Object> switchExpNode = NodeFunctions.nodePop();
		VariableSize variableSize = new VariableSize(0);
		// Während wir den Körper parsen, können wir die Informationen zu den Fällen im Case-Vektor von History speichern.
		Parser.parseBody(variableSize, history, BodyFlag.BODY_IS_FLAG_SWITCH_STATEMENT);
		Node<Object> bodyNode = NodeFunctions.nodePop();
		// Erstellen des Switch-Knotens.
		NodeFunctions.makeSwitchNode(switchExpNode, bodyNode, _switch.getCaseData().getCases(), _switch.getCaseData().getDefaultCaseNode());
		Node<Object> switchNode = NodeFunctions.nodePeek();
		CleanCodeAnalyzer.analyzeIfSwitchStmtIsInsideInheritedClass(Parser.currentProcess, keywordToken, Parser.currentSUDatatype, switchNode);
		// Wir verwenden die History, um den switch-Status zu tracken.
		Parser.parserEndSwitchStatement(_switch);
	}
	
	public static void parseDoWhile(History<Object> history) {
		int doWhileLoopStmtStartTokenVecIndex = Parser.currentProcess.getTokenVec().getPeekIndex();;
		Token<Object> keywordToken = Parser.tokenPeekNext();
		Parser.expectKeyword("do");
		VariableSize varSize = new VariableSize(0);
		Parser.parseBody(varSize, history, BodyFlag.BODY_IS_FLAG_DO_WHILE_LOOP_STATEMENT);
		Node<Object> bodyNode = NodeFunctions.nodePop();
		Parser.parseKeywordParenthesesExpression("while");
		Node<Object> expNode = NodeFunctions.nodePop();
		Parser.expectSym(';');
		NodeFunctions.makeDoWhileNode(bodyNode, expNode);
		Node<Object> doWhileNode = NodeFunctions.nodePeek();
		CleanCodeAnalyzer.analyzeStmt(Parser.currentProcess, keywordToken, doWhileNode, bodyNode, doWhileLoopStmtStartTokenVecIndex, Parser.doWhileLoopStmtEndTokenVecIndex, "while");
		Parser.doWhileLoopStmtEndTokenVecIndex = Parser.priorDoWhileLoopStmtEndTokenVecIndex;
	}
	
	public static void parseWhile(History<Object> history) {
		int whileLoopStmtStartTokenVecIndex = Parser.currentProcess.getTokenVec().getPeekIndex();;
		Token<Object> keywordToken = Parser.tokenPeekNext();
		Parser.parseKeywordParenthesesExpression("while");
		Node<Object> expNode = NodeFunctions.nodePop();
		VariableSize variableSize = new VariableSize(0);
		Parser.parseBody(variableSize, history, BodyFlag.BODY_IS_FLAG_WHILE_LOOP_STATEMENT);
		Node<Object> bodyNode = NodeFunctions.nodePop();
		NodeFunctions.makeWhileNode(expNode, bodyNode);
		Node<Object> whileNode = NodeFunctions.nodePeek();
		CleanCodeAnalyzer.analyzeStmt(Parser.currentProcess, keywordToken, whileNode, bodyNode, whileLoopStmtStartTokenVecIndex, Parser.whileLoopStmtEndTokenVecIndex, null);
		Parser.whileLoopStmtEndTokenVecIndex = Parser.priorWhileLoopStmtEndTokenVecIndex;
	}
	
	// Parsed den Initialisierer oder die Bedingung (erste beiden Teile) einer for-Schleife und gibt zurück, ob dieser einen Inhalt enthält, oder nur ein Semikolon (beides valide).
	public static boolean parseForLoopPart(History<Object> history, boolean initializer) {
		if (Parser.tokenNextIsSymbol(';')) {
			// Falls wir ein Semikolon finden, haben wir nichts in dem Teil. Zum Beispiel beim Initialisierer: "for(;".
			// Ignoriere das Semikolon,
			Parser.tokenNext();
			return false;
		}
		Parser.parseExpressionableRoot(history);
		// Beim Initialisierer wird das Semikolon beim Parsen der Variable oder der Variablenliste vom Stack genommen, bei der Bedingung nicht.
		//if (initializer != true) Parser.expectSym(';');
		return true;
	}
	
	// Parsed den dritten Teil (den Loop) der For-Schleife und gibt zurück, ob dieser Teil existiert.
	public static boolean parseForLoopPartLoop(History<Object> history) {
		// Finden wir eine rechte Klammer, haben wir die For-Loop abgeschlossen.
		if (Parser.tokenNextIsSymbol(')')) {
			return false;
		}
		// Sonst parsen wir die Expression.
		Parser.parseExpressionableRoot(history);
		return true;
	}
	
	public static void parseForStmt(History<Object> history) {
		Node<Object> initNode = null;
		Node<Object> condNode = null;
		Node<Object> loopNode = null;
		Node<Object> bodyNode = null;
		int forLoopStmtStartTokenVecIndex = Parser.currentProcess.getTokenVec().getPeekIndex();;
		Token<Object> keywordToken = Parser.tokenPeekNext();
		// Parsen des for-Keywords und der linken Klammer:
		Parser.expectKeyword("for");
		Parser.expectOp("(");
		// Ist es eine For-Schleife oder eine For-Each-Schleife?
		Token<Object> token = Parser.tokenPeekAt(2);
		if (Token.tokenIsSymbol(token, ':')) {
			Parser.insideForLoopInitializing = false;
			// Falls wir ein ':' finden 2 Tokens später, beispielsweise bei "for(int i : intArray)", ist es eine forEach-Schleife.
			// Das ist der einzige Fall, in dem ':' ein Operator ist, und kein Symbol.
			Parser.parseForEachStmt(history, forLoopStmtStartTokenVecIndex, keywordToken);
			return;
		}
		Parser.insideForLoopInitializing = true;
		// Wenn wir einen Initialisierer finden, speichern wir ihn in unserem Init-Knoten.
		if (Parser.parseForLoopPart(history, true)) {
			initNode = NodeFunctions.nodePop();
		}
		Parser.insideForLoopInitializing = false;
		Parser.insideForLoopConditional = true;
		if (Parser.parseForLoopPart(history, false)) {
			condNode = NodeFunctions.nodePop();
		}
		Parser.insideForLoopConditional = false;
		if (Parser.parseForLoopPartLoop(history)) {
			loopNode = NodeFunctions.nodePop();
		}
		// Wir erwarten die rechte Klammer
		Parser.expectSym(')');
		VariableSize varSize = new VariableSize();
		Parser.parseBody(varSize, history, BodyFlag.BODY_IS_FLAG_FOR_LOOP_STATEMENT);
		bodyNode = NodeFunctions.nodePop();
		NodeFunctions.makeForNode(initNode, condNode, loopNode, bodyNode);
		Node<Object> forNode = NodeFunctions.nodePeek();
		CleanCodeAnalyzer.analyzeStmt(Parser.currentProcess, keywordToken, forNode, bodyNode, forLoopStmtStartTokenVecIndex, Parser.forLoopStmtEndTokenVecIndex, null);
		Parser.forLoopStmtEndTokenVecIndex = Parser.priorForLoopStmtEndTokenVecIndex;
	}
	
	public static void parseForEachStmt(History<Object> history, int forEachLoopStmtStartTokenVecIndex, Token<Object> keywordToken) {
		boolean priorInsideForEachLoop = Parser.insideForEachLoop;
		Parser.insideForEachLoop = true;
		// Da eine forEach-Schleife keine Operatoren bestitzt, ist es keine Expression.
		Node<Object> initNode = null;
		Node<Object> loopNode = null;
		Node<Object> bodyNode = null;
		Parser.parseExpressionableRoot(Parser.historyDown(history, HistoryFlag.HISTORY_FLAG_PARENTHESES_IS_NOT_A_FUNCTION_CALL));
		initNode = NodeFunctions.nodePop();
		Parser.expectSym(':');
		Parser.parseExpressionableRoot(Parser.historyDown(history, HistoryFlag.HISTORY_FLAG_PARENTHESES_IS_NOT_A_FUNCTION_CALL));
		loopNode = NodeFunctions.nodePop();
		Parser.expectSym(')');
		VariableSize varSize = new VariableSize();
		Parser.parseBody(varSize, history, BodyFlag.BODY_IS_FLAG_FOR_EACH_LOOP_STATEMENT);
		bodyNode = NodeFunctions.nodePop();
		NodeFunctions.makeForEachNode(initNode, loopNode, bodyNode);
		Node<Object> forEachNode = NodeFunctions.nodePeek();
		CleanCodeAnalyzer.analyzeStmt(Parser.currentProcess, keywordToken, forEachNode, bodyNode, forEachLoopStmtStartTokenVecIndex, Parser.forEachLoopStmtEndTokenVecIndex, null);
		Parser.forEachLoopStmtEndTokenVecIndex = Parser.priorForEachLoopStmtEndTokenVecIndex;
		Parser.insideForEachLoop = priorInsideForEachLoop;
	}
	
	public static void parseReturn(History<Object> history) {
		Token<Object> keywordToken = Parser.tokenPeekNext();
		Parser.expectKeyword("return");
		// 1. Fall: Void (Return ohne Expression).
		if (Parser.tokenNextIsSymbol(';')) {
			Parser.expectSym(';');
			NodeFunctions.makeReturnNode(null); // Expression-Knoten wird auf NULL gesetzt.
			return;
		}
		// 2. Fall: Return-Statement, das eine Expression zurückgibt (zum Beispiel "return 50;").
		Parser.parseExpressionableRoot(history);
		Node<Object> exp = NodeFunctions.nodePop();
		NodeFunctions.makeReturnNode(exp);
		Node<Object> returnNode = NodeFunctions.nodePeek();
		Parser.expectSym(';');
		CleanCodeAnalyzer.analyzeReturnStatementInStructMethod(Parser.currentProcess, keywordToken, returnNode, exp, Parser.parserCurrentFunction);
	}
	
	public static void parseForTenary(History<Object> history) {
		// Die Bedingung wurde zu diesem Zeitpunkt schon geparsed.
		Node<Object> conditionNode = NodeFunctions.nodePop();
		// Parse die if-Bedingung oder wahre Bedingung und nimm sie vom Stack.
		Parser.expectOp("?");
		Parser.parseExpressionableRoot(Parser.historyDown(history, HistoryFlag.HISTORY_FLAG_PARENTHESES_IS_NOT_A_FUNCTION_CALL));
		Node<Object> trueResultNode = NodeFunctions.nodePop();
		// Parse die else-Bedingung oder falsche Bedingung und nimm sie vom Stack.
		Parser.expectSym(':');
		Parser.parseExpressionableRoot(Parser.historyDown(history, HistoryFlag.HISTORY_FLAG_PARENTHESES_IS_NOT_A_FUNCTION_CALL));
		Node<Object> falseResultNode = NodeFunctions.nodePop();
		NodeFunctions.makeTenaryNode(trueResultNode, falseResultNode);
		Node<Object> tenaryNode = NodeFunctions.nodePop();
		// Speichere die Bedingungen als Expression ab und schiebe sie in den Stack. Das "?" signalisiert ein Tenary.
		NodeFunctions.makeExpNode(conditionNode, tenaryNode, "?");
	}
	
	@SuppressWarnings("deprecation")
	public static void parseSizeof(History<Object> history) {
		Parser.expectKeyword("sizeof");
		Parser.expectOp("(");
		Datatype<Node<Object>> dtype = new Datatype<Node<Object>>();
		Parser.parseDatatype(dtype);
		NodeFunctions.nodeCreate(new Node<Object>(NodeType.NODE_TYPE_NUMBER, new Long(dtype.getSize())));
		Parser.expectSym(')');
	}
	
	public static void parseNamespaceBody(History<Object> history, Node<Object> identifierNode, String identifier) {
		Scope<Object> currentScope = Scope.scopeCurrent(Parser.currentProcess);
		VariableSize varSize = new VariableSize(0);
		Parser.parseBody(varSize, history, BodyFlag.BODY_IS_FLAG_NAMESPACE);
		Node<Object> bodyNode = NodeFunctions.nodePop();
		
		NodeFunctions.makeNamespaceNode(identifierNode, bodyNode, currentScope, true);
		Node<Object> namespaceNode = NodeFunctions.nodePop();
		Symresolver.symresolverRegisterSymbol(Parser.currentProcess, identifier, SymbolType.SYMBOL_TYPE_NODE, namespaceNode, currentScope);
		NodeFunctions.nodePush(namespaceNode);
		Parser.noNewlinesBetweenNamespaceDefinitionsObservation = CleanCodeAnalyzer.analyzeNamespaceDefinitionsRegardingNewlines(Parser.currentProcess, Parser.noNewlinesBetweenNamespaceDefinitionsObservation, Parser.tempStartNamespaceDefinitionTokenVecIndex, identifier, namespaceNode);
	}
	
	public static void parseNamespaceWithBodyAndIdentifier(History<Object> history) {
		Parser.parseIdentifier(Parser.historyBegin(0));
		Node<Object> identifierNode = NodeFunctions.nodePop();
		String identifier = (String) identifierNode.getValue();
		Node<Object> existingNamespaceNode = Parser.getNamespaceNodeIfExisting(history, identifier);
		if (existingNamespaceNode != null) {
			Parser.handleExistingNamespace(history, identifierNode, identifier, false, true);
		}
		Parser.parseNamespaceBody(history, identifierNode, identifier);
	}
	
	public static void parseNamespaceAliasDefinition(History<Object> history) {
		Parser.parseExpressionableRoot(history);
		Node<Object> expNode = NodeFunctions.nodePop();
		NodeFunctions.nodePush(expNode);
		@SuppressWarnings("unchecked")
		Expression<Object> exp = (Expression<Object>) expNode.getValue();
		Node<Object> leftIdentifierNode = exp.getLeft();
		String rightIdentifier = (String) exp.getRight().getValue();
		// Eintragen des Alias in den Namensraum.
		Node<Object> namespaceNode = Parser.getNamespaceNodeIfExisting(history, rightIdentifier);
		if (namespaceNode == null || namespaceNode.getType() == NodeType.NODE_TYPE_STRUCT) {
			String leftIdentifier = (String) leftIdentifierNode.getValue();
			if (namespaceNode.getType() == NodeType.NODE_TYPE_STRUCT) {
				Compiler.compilerError(currentProcess, "Der angegebene Namensraum \"" + rightIdentifier + "\", für den Sie den Alias \"" + leftIdentifier + "\" angeben wollen, ist eine Struktur oder eine Klasse und kein Namensraum.\n");
			}
			Compiler.compilerError(currentProcess, "Der angegebene Namensraum \"" + rightIdentifier + "\", für den Sie den Alias \"" + leftIdentifier + "\" angeben wollen, existiert nicht.\n");
		}
		@SuppressWarnings("unchecked")
		Namespace<Object> namespace = (Namespace<Object>) namespaceNode.getValue();
		namespace.getIdentifierNodes().vectorPush(leftIdentifierNode);
		Parser.expectSym(';');
	}
	
	public static void parseNamespaceWithBodyWithoutIdentifier(History<Object> history) {
		// Da uns kein Bezeichner zur Verfügung steht, verwenden wir einen zufälligen Namen.
		Token<Object> randomNameToken = Parser.parserBuildRandomTypeName();
		String randomName = (String) randomNameToken.getValue();
		NodeFunctions.nodeCreate(new Node<Object>(NodeType.NODE_TYPE_IDENTIFIER, randomName));
		Node<Object> randomNameNode = NodeFunctions.nodePop();
		Parser.parseNamespaceBody(history, randomNameNode, randomName);
	}
	
	// Rekursive Funktion, die die Struktur "namespace A::B:: inline C {" -> Body in "namespace A { namespace B { namespace C {" -> Body überführt.
	public static Node<Object> parseNestedNamespaces(History<Object> history, VariableSize varSize, boolean isFirstCall) {
		// Abbruchbedingung, bei der Körper in dem Körperknoten des Namensraums auf der zweittiefsten Ebene geparsed wird.
		if (Parser.tokenNextIsSymbol('{')) {
			Parser.parseBody(varSize, history, BodyFlag.BODY_IS_FLAG_NAMESPACE);
			return NodeFunctions.nodePop();
		}
		Parser.parserScopeNew();
		Token<Object> token = Parser.tokenPeekNext();
		if (SymbolTable.S_EQ((String) token.getValue(), "inline") && !(isFirstCall)) {
			Parser.tokenNext();
		}
		else if (SymbolTable.S_EQ((String) token.getValue(), "inline") && isFirstCall) {
			Compiler.compilerError(Parser.currentProcess, "Verschachtelte Namensräume dürfen vor dem ersten Mitglied kein \"inline\" enthalten.\n");
		}
		Parser.parseIdentifier(Parser.historyBegin(0));
		Node<Object> identifierNode = NodeFunctions.nodePop();
		String identifier = (String) identifierNode.getValue();
		Node<Object> existingNamespaceNode = Parser.getNamespaceNodeIfExisting(history, identifier);
		if (existingNamespaceNode != null) {
			Parser.handleExistingNamespace(history, identifierNode, identifier, false, true);
		}
		Vector<Node<Object>> bodyVec = Vector.vectorCreate();
		token = Parser.tokenPeekNext();
		if (token.getType() != TokenType.TOKEN_TYPE_SYMBOL || (char) token.getValue() != '{') {
			Parser.expectOp("::");
		}
		else if (token.getType() == TokenType.TOKEN_TYPE_SYMBOL && (char) token.getValue() == '{') {
			// Finden wir das gesuchte Symbol '{', kommen wir beim nächsten Aufruf der Funktion in die Abbruchbedingung der Rekursion.
		}
		else {
			Compiler.compilerError(Parser.currentProcess, "An dieser Stelle wird der Operator \"::\" oder das Symbol \'{\' erwartet, um Namensräume verschachtelt zu definieren.\n");
		}
		Node<Object> node = Parser.parseNestedNamespaces(history, varSize, false);
		Node<Object> bodyNode = null;
		// Falls wir uns im vorletzten Aufruf befinden, speichern wir den Körperknoten ab.
		if (node.getType() == NodeType.NODE_TYPE_BODY) {
			bodyNode = node;
		}
		// Sonst müssen wir einen neuen Körperknoten bauen, in dem sich der verschachtelte Namensraum befindet.
		// Dann befindet sich der Namensraum-Knoten auf der unteren Ebene in der "node"-Variable.
		else {
			Node<Object> nestedNamespaceNode = node;
			bodyVec.vectorPush(nestedNamespaceNode);
			NodeFunctions.makeBodyNode(bodyVec, varSize, false, null, BodyFlag.BODY_IS_FLAG_NAMESPACE, Scope.scopeCurrent(Parser.currentProcess));
			bodyNode = NodeFunctions.nodePop();
		}
		Scope<Object> currentScope = Scope.scopeCurrent(Parser.currentProcess).getParent();
		NodeFunctions.makeNamespaceNode(identifierNode, bodyNode, currentScope, true);
		Node<Object> namespaceNode = NodeFunctions.nodePop();
		if (currentScope.getParent() != null) {
			Symresolver.symresolverBuildForNode(Parser.currentProcess, null, namespaceNode, currentScope, null);
		}
		Parser.parserScopeFinish();
		// Befinden wir uns im ersten Aufruf, drücken wir den Knoten auf unseren Abstract Syntax Tree Vektor.
		if (isFirstCall) {
			NodeFunctions.nodePush(namespaceNode);
		}
		return namespaceNode;
	}
	
	public static void parseNamespace(History<Object> history) {
		if (Parser.tokenNextIsKeyword("inline")) {
			Parser.tempStartNamespaceDefinitionTokenVecIndex = Parser.currentProcess.getTokenVec().getPeekIndex();
			Parser.tokenNext();
		}
		if (Parser.tempStartNamespaceDefinitionTokenVecIndex == null) {
			Parser.tempStartNamespaceDefinitionTokenVecIndex = Parser.currentProcess.getTokenVec().getPeekIndex();
		}
		Parser.expectKeyword("namespace");
		// a) "inline namespace" -> Bezeichner -> Body, b) "namespace" -> Bezeichner -> Body
		// und c) "namespace" -> Bezeichner (neuer Alias) -> "=" -> Bezeichner (vorhandener Namensraum).
		// d) "namespace {" -> Body -> "}". e) "namespace" -> Bezeichner -> "::" -> Bezeichner
		// e) 
		if (Parser.tokenPeekNext().getType() == TokenType.TOKEN_TYPE_IDENTIFIER) {
			Token<Object> token = Parser.tokenPeekAt(1);
			// a) und b).
			if (token.getType() == TokenType.TOKEN_TYPE_SYMBOL && (char) token.getValue() == '{') {
				Parser.parseNamespaceWithBodyAndIdentifier(history);
				Parser.tempStartNamespaceDefinitionTokenVecIndex = null;
				return;
			}
			// c).
			else if (token.getType() == TokenType.TOKEN_TYPE_OPERATOR && SymbolTable.S_EQ((String) token.getValue(), "=")) {				
				// Parsen der Expression.
				Parser.parseNamespaceAliasDefinition(history);
				Parser.tempStartNamespaceDefinitionTokenVecIndex = null;
				return;
			}
			// e) und f).
			else if (token.getType() == TokenType.TOKEN_TYPE_OPERATOR && SymbolTable.S_EQ((String) token.getValue(), "::")) {
				Parser.parseNestedNamespaces(history, new VariableSize(0), true);
				Parser.tempStartNamespaceDefinitionTokenVecIndex = null;
				return;
			}
		}
		// d) "namespace {" -> Body -> "}".
		if (Parser.tokenNextIsSymbol('{')) {
			Parser.parseNamespaceWithBodyWithoutIdentifier(history);
		}
		Parser.tempStartNamespaceDefinitionTokenVecIndex = null;
	}
	
	public static void parseNamespaceMemberName(History<Object> history, Node<Object> namespaceNode, String identifier) {
		Parser.expectOp("::");
		Parser.parseIdentifier(history);
		Node<Object> memberIdentifier = NodeFunctions.nodePop();
		String memberName = (String) memberIdentifier.getValue();
		@SuppressWarnings("unchecked")
		Namespace<Object> namespace = (Namespace<Object>) namespaceNode.getValue();
		if (Namespace.includesMemberName(namespace, memberName)) {
			Compiler.compilerError(Parser.currentProcess, "Das Mitglied \"" + memberName + "\" im Namensraum \"" + (String) identifier + "\" ist bereits definiert.\n");
		}
		else if (namespace.getCompletelyDefined()) {
			Compiler.compilerError(Parser.currentProcess, "Der Namensraum \"" + (String) identifier + "\" ist bereits komplett definiert, sodass bereits alle Mitglieder des Namensraums sichtbar sind. Sie können daher keine weiteren Mitglieder definieren.\n");
		}
		Namespace.addMemberName(namespace, memberIdentifier);
	}
	
	public static void handleExistingNamespace(History<Object> history, Node<Object> namespaceNode, String identifier, boolean memberDefinitionIsValid, boolean namespaceIsFullyDefined) {
		@SuppressWarnings("unchecked")
		Namespace<Object> namespace = (Namespace<Object>) namespaceNode.getValue();
		// 1. Fall: Wir kennen den Namensraum schon, fügen aber eine Mitgliedsentität hinzu (eigentlich sind alle Mitglieder sichtbar).
		if (namespaceNode.getType() == NodeType.NODE_TYPE_NAMESPACE && Parser.tokenNextIsOperator("::") && memberDefinitionIsValid && !(namespaceIsFullyDefined)) {
			Parser.parseNamespaceMemberName(history, namespaceNode, identifier);
			return;
		}
		// 2. Fall: Es wurden vorher bereits Mitglieder definiert und nun wurde der Namensraum komplett definiert.
		else if (namespaceNode.getType() == NodeType.NODE_TYPE_NAMESPACE && Parser.tokenNextIsSymbol(';') && !(namespaceIsFullyDefined)) {
			namespace.setCompletelyDefined(true);
			namespace.setDeclaredMember(null);
			return;
		}
		// 4. Fall: Der Namensraum ist bereits voll definiert und es wird versucht, weitere Mitglider anzulegene.
		else if (namespaceIsFullyDefined && Parser.tokenNextIsOperator("::")){
			Compiler.compilerError(Parser.currentProcess, "Der Namensraum \"" + (String) identifier + "\" ist bereits komplett definiert, sodass bereits alle Mitglieder des Namensraums sichtbar sind. Sie können daher keine weiteren Mitglieder definieren.\n");
		}
		// 5. Fall: Wir kennen den Namensraum schon, und er wird hier erneut komplett definiert.
		Compiler.compilerError(Parser.currentProcess, "Der Namensraum \"" + identifier + "\" ist bereits definiert. Sie können ihn nicht erneut anlegen.\n");

	}
	
	public static Node<Object> getNamespaceNodeIfExisting(History<Object> history, String identifier) {
		Symbol<Object> sym = Symresolver.symresolverGetSymbol(Parser.currentProcess, identifier, Scope.scopeCurrent(Parser.currentProcess), null);
		if (sym != null) {
			Node<Object> namespaceNode = (Node<Object>) sym.getData();
			if (namespaceNode.getType() == NodeType.NODE_TYPE_NAMESPACE || namespaceNode.getType() == NodeType.NODE_TYPE_STRUCT) {
				return namespaceNode;
			}
		}
		return null;
	}
	
	// Parsed einen Namensraum, der mit "using" oder "using namespace" im aktuellen Scope platziert wird.
	public static void parseUsing(History<Object> history) {
		Parser.tempStartNamespaceDefinitionTokenVecIndex = Parser.currentProcess.getTokenVec().getPeekIndex();
		Parser.expectKeyword("using");
		// 1. "using namespace" -> Bezeichner.
		if (Parser.tokenNextIsKeyword("namespace")) {
			Parser.expectKeyword("namespace");
		}
		// 2. Fall: "using" -> Bezeichner.
		if (Parser.tokenPeekNext().getType() == TokenType.TOKEN_TYPE_IDENTIFIER) {
			Parser.parseIdentifier(history);
		}
		else {
			Compiler.compilerError(Parser.currentProcess, "An dieser Stelle wird ein Bezeichner für einen Namensraum erwartet, aber etwas anderes wurde eingegeben.\n");
		}
		Node<Object> identifierNode = NodeFunctions.nodePop();
		String identifier = (String) identifierNode.getValue();
		Node<Object> existingNamespaceNode = Parser.getNamespaceNodeIfExisting(history, identifier);
		if (existingNamespaceNode != null) {
			@SuppressWarnings("unchecked")
			Namespace<Object> namespace = (Namespace<Object>) existingNamespaceNode.getValue();
			Parser.handleExistingNamespace(history, existingNamespaceNode, identifier, true, namespace.getCompletelyDefined());
			Parser.expectSym(';');
			return;
		}
		Node<Object> newNamespaceNode = null;
		if (Parser.tokenNextIsOperator("::") && existingNamespaceNode == null) {
			NodeFunctions.makeNamespaceNode(identifierNode, null, Scope.scopeCurrent(Parser.currentProcess), false);
			newNamespaceNode = (Node<Object>) NodeFunctions.nodePop();
			Parser.parseNamespaceMemberName(history, newNamespaceNode, identifier);
		}
		else {
			NodeFunctions.makeNamespaceNode(identifierNode, null, Scope.scopeCurrent(Parser.currentProcess), true);
			newNamespaceNode = (Node<Object>) NodeFunctions.nodePop();
		}
		NodeFunctions.nodePush(newNamespaceNode);
		Symresolver.symresolverBuildForNamespaceNode(Parser.currentProcess, newNamespaceNode, Scope.scopeCurrent(Parser.currentProcess));
		Parser.expectSym(';');
		Parser.noNewlinesBetweenNamespaceDefinitionsObservation = CleanCodeAnalyzer.analyzeNamespaceDefinitionsRegardingNewlines(Parser.currentProcess, Parser.noNewlinesBetweenNamespaceDefinitionsObservation, Parser.tempStartNamespaceDefinitionTokenVecIndex, identifier, newNamespaceNode);
		Parser.tempStartNamespaceDefinitionTokenVecIndex = null;
	}
	
	public static int parseFriend(History<Object> history, AccessSpecifierFlag accessSpecifier) {
		if (Parser.currentEntityName == null) {
			Compiler.compilerError(Parser.currentProcess, "Das Keyword \"friend\" kann nur innerhalb einer Klasse oder einer Struktur verwendet werden.\n");
		}
		Token<Object> token = Parser.tokenNext();
		token = Parser.tokenPeekAt(0);
		Node<Object> namespaceIdentifierNode = null;
		if (token.getType() == TokenType.TOKEN_TYPE_IDENTIFIER && Parser.isNamespaceOrStructAsNamespaceNotation(history, (String) token.getValue())) {
			namespaceIdentifierNode = Parser.parseNamespaceOrStructAsNamespaceNotation(history, false);
		}
		token = Parser.tokenPeekAt(2);
		// Parsen des Operatorüberladens.
		if (SymbolTable.S_EQ((String) Parser.tokenPeekAt(1).getValue(), "operator") && Parser.tokenPeekAt(2).getType() == TokenType.TOKEN_TYPE_OPERATOR && SymbolTable.S_EQ((String) Parser.tokenPeekAt(3).getValue(), "(")) {
			Parser.functionIsOperatorFlag = true;
			Parser.functionIsFriendFlag = true;
		}
		else if (token.getType() == TokenType.TOKEN_TYPE_OPERATOR && SymbolTable.S_EQ((String) token.getValue(), "(")) {
			Parser.functionIsFriendFlag = true;
		}
		else if (token.getType() == TokenType.TOKEN_TYPE_SYMBOL && (char) token.getValue() == ';'){
			Parser.varIsFriendFlag = true;
		}
		else {
			Compiler.compilerError(Parser.currentProcess, "Bei einer \"friend\"-Deklaration wird eine Funktion oder eine Variable eines anderen Namensraums oder des Global-Scopes erwartet, aber etwas anderes wurde eingegeben.\n");
		};
		return parseVariableFunctionOrStructUnionDefinedAfterAccessSpecifierAndNamespace(history, accessSpecifier, Parser.tokenPeekNext(), namespaceIdentifierNode);
	}
	
	public static Vector<Object> appendNewValidGenericPlaceholders(GenericPlaceholders<Object> newGenericPlaceholders) {
		Vector<Object> newGenericPlaceholdersVector = newGenericPlaceholders.getGenericIdentifierNodeVector();
		newGenericPlaceholdersVector.vectorSetPeekPointer(0);
		for(int i = 0; i < newGenericPlaceholdersVector.vectorCount(); i++) {
			Parser.currentValidGenericPlaceholders.vectorPush(newGenericPlaceholdersVector.vectorPeekPtrAt(i));
		}
		return Parser.currentValidGenericPlaceholders;
	}
	
	// Die Methode bekommt den Datentyp des generischen Structs übergeben, macht eine Kopie davon
	// und übersetzt die Generics in die zu parsenden konkret existierenden Datentypen.
	// Außerdem wird überprüft, ob die Anzahl der übergebenen Datentypen korrekt sind.
	public static void parseDefinitionsOfGenerics(Datatype<Node<Object>> concreteDtype) {
		Vector<Datatype<Node<Object>>> concreteDatatypesVector = Vector.vectorCreate();
		Parser.expectOp("<");
		while(!(Parser.tokenNextIsOperator(">"))) {
			Datatype<Node<Object>> newDtype = new Datatype<Node<Object>>();
			Parser.parseDatatype(newDtype);
			concreteDatatypesVector.vectorPush(newDtype);
			// Wirf den Datentyp vom Stack.
			if (!(Token.tokenIsOperator(Parser.tokenPeekNext(), ">"))) {
				expectOp(",");
			}
		}
		expectOp(">");
		NodeFunctions.makeConcreteDefinitionOfGenericStructNode(concreteDatatypesVector);
		concreteDtype.setConcreteDatatypesNode(NodeFunctions.nodePop());
	}
	
	public static boolean structHasDefinedGenerics(Datatype<Node<Object>> structDtype) {
		@SuppressWarnings("unchecked")
		Struct<Object> struct = (Struct<Object>) structDtype.getStructOrUnionNode().getValue();
		return struct.getDtype().getGenericDatatypesNode() != null;
	}
	
	// Parsed beispielsweise ein "generic <typename T>" oder "generic <class T>", wobei "T" als valider generischer Bezeichner in den globalen Vektor gespeichert wird.
	// Ist es keine Typename-Definition, so parsed diese Funktion die generischen Typen nach einer Struct-Definition.
	// Alte valide Bezeichner werden dabei gelöscht. Aufzählungen sind mithilfe von "," möglich.
	// Werden die Generics einer Elternklasse geparsed, so muss überprüft werden, ob die korrekten generischen Typen eingetragen sind.
	@SuppressWarnings("unchecked")
	public static void parseGenerics(History<Object> history, boolean isTypenameDefinition, Datatype<Node<Object>> dtype, boolean isParentClass) {
		Token<Object> token = Parser.tokenPeekNext();
		if (dtype != null && (token.getType() != TokenType.TOKEN_TYPE_OPERATOR || !(SymbolTable.S_EQ((String) token.getValue(), "<")))) {
			return;
		}
		if (dtype != null && dtype.getStructOrUnionNode() != null && Parser.structHasDefinedGenerics(dtype)) {
			Parser.insideGenericsParsing = true;
			Parser.parseDefinitionsOfGenerics(dtype);
			Parser.insideGenericsParsing = false;
			return;
		}
		
		Vector<Object> newValidGenerics = Vector.vectorCreate();
		if (isTypenameDefinition) {
			Parser.expectKeyword("generic");
		}
		Parser.expectOp("<");
		while(!(Parser.tokenNextIsOperator(">"))) {
			if (isTypenameDefinition) {
				if (SymbolTable.S_EQ((String) Parser.tokenPeekNext().getValue(), "typename")) {
					Parser.expectKeyword("typename");
				}
				else if (SymbolTable.S_EQ((String) Parser.tokenPeekNext().getValue(), "class")) {
					Parser.expectKeyword("class");
				}
				else {
					Compiler.compilerError(Parser.currentProcess, "Hier wird das Keyword \"typename\" oder \"class\" erwartet.\n");
				}
			}
			token = Parser.tokenPeekNext();
			if (token.getType() == TokenType.TOKEN_TYPE_IDENTIFIER) {
				Parser.parseIdentifier(history);
				Node<Object> genericIdentifierNode = NodeFunctions.nodePop();
				String genericIdentifier = (String) genericIdentifierNode.getValue();
				boolean genericIdentifierHasAlreadyBeenDefined = DatatypeFunctions.genericTypeHasAlreadyBeenDefined(Parser.currentValidGenericPlaceholders, genericIdentifier);
				// Generics dürfen bei der Definition nicht doppelt vorkommen, daher gleichen wir den neuen generischen Typen mit allen bisherigen Typen ab.
				if (isTypenameDefinition && genericIdentifierHasAlreadyBeenDefined) {
					Compiler.compilerError(Parser.currentProcess, "Sie können den generischen Typ \"" + genericIdentifier + "\" nicht mehrfach definieren.\n");
				}
				// Sind wir nicht bei der Typename-Definition, sondern schauen uns ein neues Struct an, so muss der generische Typename schon mit typename definiert worden sein.
				else if (!(isTypenameDefinition) && !(genericIdentifierHasAlreadyBeenDefined)) {
					Compiler.compilerError(Parser.currentProcess, "Sie müssen den generischen Typen \"" + genericIdentifier + "\" zuerst mit \"generic <typename " + genericIdentifier + ">\" definieren, bevor Sie ihn verwenden können.\n");
				}
				newValidGenerics.vectorPush(genericIdentifierNode);
			}
			if (!(Parser.tokenNextIsOperator(">"))) {
				Parser.expectOp(",");
			}
		}
		Parser.expectOp(">");
		NodeFunctions.makeGenericTypenameNode(newValidGenerics);
		// Sind wir in der Typename-Definition, so drücken wir die neuen definierten Typen in den globalen Vektor, der alle validen generischen Typen verwaltet.
		if (isTypenameDefinition) {
			Node<Object> currentValidGenericPlaceholderNode = NodeFunctions.nodePop();
			// Wir hängen die neu definierten Generic-Typen an den globalen Vektor an, der sich alle definierten generischen Typen merkt.
			if (Parser.currentValidGenericPlaceholders == null) {
				Parser.currentValidGenericPlaceholders = Vector.vectorCreate();
			}
			Parser.currentValidGenericPlaceholders = Parser.appendNewValidGenericPlaceholders((GenericPlaceholders<Object>) currentValidGenericPlaceholderNode.getValue());
			NodeFunctions.nodePush(currentValidGenericPlaceholderNode);
		}
		// Falls es keine Typename-Definition ist, drücken wir den erstellten Knoten der definierten validen Generics in den Datentyp des Structs oder der Klasse.
		// Es sei denn, wir parsen eine Elternklasse. Dann müssen wir überprüfen, ob die richtigen Generics angegeben sind.
		else {
			if (!isParentClass) {
				dtype.setGenericDatatypesNode(NodeFunctions.nodePop());
			}
			else {
				// Falls die generischen Datentypen nicht identisch sind, haben wir einen Compiler-Fehler.
				if (!(DatatypeFunctions.genericsAreValidForParentClass(dtype, NodeFunctions.nodePop()))) {
					Compiler.compilerError(Parser.currentProcess, "Für die Struktur bzw. Klasse \"" + dtype.getTypeStr() + "\" wurden bei der Angabe als Elternstruktur die falschen generischen Datentypen angegeben.\n");
				}
			}
		}
	}
	
	public static void parseDelete(History<Object> history) {
		Parser.expectKeyword("delete");
		int index = 0;
		boolean isArray = false;
		if (Token.tokenIsOperator(Parser.tokenPeekNext(), "[") && Token.tokenIsSymbol(Parser.tokenPeekAt(1), ']')) {
			isArray = true;
			Parser.expectOp("[");
			Parser.expectSym(']');
		}
		// Ist ein Namensraum angegeben?
		String namespaceIdentifier = null;
		if (Token.tokenIsOperator(Parser.tokenPeekAt(index + 1), "::")) {
			namespaceIdentifier = (String) Parser.tokenPeekAt(index).getValue();
			index += 2;
		}
		Token<Object> identifierToken;
		identifierToken = Parser.tokenPeekAt(index);
		String varIdentifier = (String) identifierToken.getValue();
		Symbol<Object> sym = null;
		Scope<Object> scope = Scope.scopeCurrent(currentProcess);
		if (namespaceIdentifier == null) {
			sym = Symresolver.symresolverGetSymbol(Parser.currentProcess, varIdentifier, Scope.scopeCurrent(Parser.currentProcess), null);
		}
		String structIdentifierInDeclarationFromOutside = Parser.currentNamespaceIdentifierInDeclarationFromOutside;
		if (sym == null && structIdentifierInDeclarationFromOutside != null) {
			sym = Symresolver.symresolverGetSymbol(Parser.currentProcess, varIdentifier, Parser.getDeclaredNamespaceOrStructBodyScope(history, structIdentifierInDeclarationFromOutside), null);
		}
		else if (namespaceIdentifier != null && sym == null){
			scope = Parser.getDeclaredNamespaceOrStructBodyScope(history, namespaceIdentifier);
			if (scope == null) {
				Compiler.compilerError(Parser.currentProcess, "Der Namensraum \"" + namespaceIdentifier + "\" existiert nicht.\n");
			}
			sym = Symresolver.symresolverGetSymbol(Parser.currentProcess, varIdentifier, scope, null);
		}
		if (sym == null) {
			Compiler.compilerError(Parser.currentProcess, "Die Variable \"" + varIdentifier + "\" existiert nicht.\n");
		}
		Node<Object> varNode = sym.getData();
		if (varNode.getType() == NodeType.NODE_TYPE_VARIABLE) {
			if (varNode.getType() == NodeType.NODE_TYPE_VARIABLE) {
				@SuppressWarnings("unchecked")
				Var<Object> var = (Var<Object>) varNode.getValue();
				if (!(isArray)) {
					if (CleanCodeAnalyzer.calledFunctionIdentifierVec == null) {
						CleanCodeAnalyzer.calledFunctionIdentifierVec = Vector.vectorCreate();
					}
					CleanCodeAnalyzer.calledFunctionIdentifierVec.vectorPush(new FunctionCall(var.getType().getTypeStr(), FunctionFlag.FUNCTION_NODE_FLAG_IS_NATIVE_DESTRUCTOR));
				}
				if (var.getType().getArray() != null && !(isArray)) {
					Compiler.compilerError(Parser.currentProcess, "Wenn Sie den Array \"" + varIdentifier + "\" löschen möchten, müssen Sie \"delete[]\" verwenden.\n");
				}
				else if (var.getType().getArray() == null && isArray) {
					Compiler.compilerError(Parser.currentProcess, "Wenn Sie die Variable \"" + varIdentifier + "\" löschen möchten, müssen Sie \"delete\" ohne \"[]\" verwenden.\n");
				}
			}
		}
		// Erstelle einen Knoten für das delete-Statement.
		Parser.parseExpressionableSingle(history);
		Node<Object> expNode = NodeFunctions.nodePop();
		NodeFunctions.makeDeleteNode(expNode, isArray);
		Parser.expectSym(';');
	}
	
	public static int parseKeyword(History<Object> history) {
		Token<Object> token = Parser.tokenPeekNext();
		if (SymbolTable.S_EQ((String) token.getValue(), "sizeof")) {
			Parser.parseSizeof(history);
			return 0;
		}
		else if (SymbolTable.keywordIsAccessSpecifier((String) token.getValue())) {
			return Parser.parseAccessSpecifier(history);
		}
		else if (SymbolTable.S_EQ((String) token.getValue(), "string")) {
			if (Namespace.namespaceOrMemberIsDefined(Parser.currentProcess, "std", "string") || Parser.temporaryStdNamespaceFlag) {
				boolean priorIsParsingString = Parser.isParsingString;
				Parser.isParsingString = true;
				Parser.parseVariableFunctionOrStructUnion(history, null);
				Parser.isParsingString = priorIsParsingString;
				return -1;
			}
			Compiler.compilerError(Parser.currentProcess, "Der Datentyp \"string\" wird erst unterstützt, wenn der Namensraum \"std\" definiert ist.\n");
		}
		else if (SymbolTable.isKeywordVariableModifier((String) token.getValue()) || SymbolTable.keywordIsDatatype((String) token.getValue())) {
			Parser.parseVariableFunctionOrStructUnion(history, null);
			return -1;
		}
		else if (SymbolTable.S_EQ((String) token.getValue(), "try")) {
			Parser.parseTryCatchStmt(history);
			return 0;
		}
		else if (SymbolTable.S_EQ((String) token.getValue(), "break")) {
			Parser.parseBreak(history);
			return 0;
		}
		else if (SymbolTable.S_EQ((String) token.getValue(), "continue")) {
			Parser.parseContinue(history);
			return 0;
		}
		else if (SymbolTable.S_EQ((String) token.getValue(), "return")) {
			Parser.parseReturn(history);
			return 0;
		}
		else if (SymbolTable.S_EQ((String) token.getValue(), "if")) {
			Parser.parseIfStmt(history, null, null);
			return 0;
		}
		else if (SymbolTable.S_EQ((String) token.getValue(), "for")) {
			Parser.parseForStmt(history);
			return 0;
		}
		else if (SymbolTable.S_EQ((String) token.getValue(), "while")) {
			Parser.parseWhile(history);
			return 0;
		}
		else if (SymbolTable.S_EQ((String) token.getValue(), "do")) {
			Parser.parseDoWhile(history);
			return 0;
		}
		else if (SymbolTable.S_EQ((String) token.getValue(), "switch")) {
			Parser.parseSwitch(history);
			return 0;
		}
		else if (SymbolTable.S_EQ((String) token.getValue(), "goto")) {
			Parser.parseGoto(history);
			return 0;
		}
		else if (SymbolTable.S_EQ((String) token.getValue(), "case")) {
			Parser.parseCase(history);
			return 1;
		}
		else if (SymbolTable.S_EQ((String) token.getValue(), "default")) {
			Parser.parseDefault(history);
			return 0;
		}
		else if (SymbolTable.S_EQ((String) token.getValue(), "using")) {
			Parser.parseUsing(history);
			return 0;
		}
		else if (SymbolTable.S_EQ((String) token.getValue(), "inline") || SymbolTable.S_EQ((String) token.getValue(), "namespace")) {
			Parser.parseNamespace(history);
			return 0;
		}
		else if (SymbolTable.S_EQ((String) token.getValue(), "friend")) {
			return Parser.parseFriend(history, null);
		}
		else if (SymbolTable.S_EQ((String) token.getValue(), "generic")) {
			Parser.parseGenerics(history, true, null, false);
			return 0;
		}
		else if (SymbolTable.S_EQ((String) token.getValue(), "throw")) {
			Parser.parseThrow(history);
			return 0;
		}
		else if (SymbolTable.S_EQ((String) token.getValue(), "delete")) {
			Parser.parseDelete(history);
			return 0;
		}
		Compiler.compilerError(Parser.currentProcess, "Das Keyword \"" + (String) token.getValue() + "\" wird noch nicht unterstützt.\n");
		return 0;
	}
	
	public static void parseContinue(History<Object> history) {
		Parser.expectKeyword("continue");
		Parser.expectSym(';');
		NodeFunctions.makeContinueNode();
	}
	
	public static void parseBreak(History<Object> history) {
		Parser.expectKeyword("break");
		Parser.expectSym(';');
		NodeFunctions.makeBreakNode();
	}
	
	// Goto nimmt einen Bezeichner wie "goto abc55;".
	public static void parseGoto(History<Object> history) {
		Token<Object> keywordToken = Parser.tokenPeekNext();
		Parser.expectKeyword("goto");
		Parser.parseIdentifier(Parser.historyBegin(0));
		Parser.expectSym(';');
		Node<Object> labelNode = NodeFunctions.nodePop();
		NodeFunctions.makeGotoNode(labelNode);
		Node<Object> gotoNode = NodeFunctions.nodePeek();
		CleanCodeAnalyzer.analyzeGoto(Parser.currentProcess, keywordToken, Parser.parserCurrentFunction, gotoNode);
	}
	
	public static void parseLabel(History<Object> history) {
		// Zu diesem Zeitpunkt haben wir schon einen Bezeichner geparsed (den Labelnamen).
		Parser.expectSym(';');
		Node<Object> labelNameNode = NodeFunctions.nodePop();
		if (labelNameNode.getType() != NodeType.NODE_TYPE_IDENTIFIER) {
			Compiler.compilerError(Parser.currentProcess, "Es wurde ein Bezeichner für Labels erwartet, aber etwas anderes wurde eingegeben.\n");
		}
		NodeFunctions.makeLabelNode(labelNameNode);
	}
	
	public static void parseString(History<Object> history) {
		Parser.parseSingleTokenToNode();
	}
	
	public static void parseLibraryDirective(History<Object> history) {
		// Entfernen des "include" keywords.
		Parser.tokenNext();
		// Die Operatoren "<" und ">" wurden schon im Lexer nicht beachtet.
		assert(Parser.tokenPeekNext().getType() == TokenType.TOKEN_TYPE_IDENTIFIER);
		Parser.parseIdentifier(history);
		Node<Object> libraryNameNode = NodeFunctions.nodePop();
		String identifier = (String) libraryNameNode.getValue();
		if (SymbolTable.S_EQ(identifier, "iostream")) {
			Parser.iostreamLibraryIsIncluded = true;
		}
		else if (SymbolTable.S_EQ(identifier, "iomanip")) {
			Parser.iomanipLibraryIsiIncluded = true;
		}
		NodeFunctions.makeLibraryNode(libraryNameNode);
		Node<Object> libraryNode = NodeFunctions.nodePop();
		NodeFunctions.nodePush(libraryNode);
		Parser.noNewlinesBetweenLibraryDirectivesObservation = CleanCodeAnalyzer.analyzeLibraryDirectiveRegardingNewlines(Parser.currentProcess, Parser.noNewlinesBetweenLibraryDirectivesObservation, Parser.tempHashTokenVecIndex, identifier, libraryNode);
		Parser.tempHashTokenVecIndex = null;
	}
	
	// Wenn wir etwas wie "classname::method(){}" parsen wollen, überprüfen wir, ob wir an der richtigen Stelle sind.
	public static boolean isNamespaceOrStructAsNamespaceNotation(History<Object> history, String identifier) {
		Node<Object> node = Parser.getNamespaceNodeIfExisting(history, identifier);
		if (node != null) {
			Token<Object> expectedDoubleColonToken = Parser.tokenPeekAt(1);
			if (expectedDoubleColonToken.getType() == TokenType.TOKEN_TYPE_OPERATOR && SymbolTable.S_EQ((String) expectedDoubleColonToken.getValue(), "::")) {
				return true;
			}
			else if (expectedDoubleColonToken.getType() == TokenType.TOKEN_TYPE_SYMBOL && (char) expectedDoubleColonToken.getValue() == ':') {
				Compiler.compilerError(Parser.currentProcess, "Sie haben das Symbol \':\' statt dem Operator \"::\" angegeben.\n");
			}
		}
		return false;
	}
	
	// Wenn wir etwas wie "classname a;" parsen wollen, überprüfen wir, ob wir an der richtigen Stelle sind.
	public static boolean isStructDatatypeType(String identifier) {
		Symbol<Object> sym = Symresolver.symresolverGetSymbol(Parser.currentProcess, identifier, Scope.scopeCurrent(Parser.currentProcess), NodeType.NODE_TYPE_STRUCT);
		if (sym != null && sym.getData().getType() == NodeType.NODE_TYPE_STRUCT) {
			int index = 1;
			Token<Object> token = Parser.tokenPeekAt(index);
			// Sind Generics angegeben?
			if (token.getType() == TokenType.TOKEN_TYPE_OPERATOR && Token.tokenIsOperator(token, "<")) {
				index++;
				Parser.isStructWithDefinedGenerics = true;
				token = Parser.tokenPeekAt(index);
				while(!(Token.tokenIsOperator(token, ">"))) {
					token = Parser.tokenPeekAt(index);
					index++;
				}
				token = Parser.tokenPeekAt(index);
			}
			// Sind Unary-Operatoren angegeben?
			while (Parser.tokenPeekAt(index).getType() == TokenType.TOKEN_TYPE_OPERATOR && SymbolTable.isUnaryOperator((String) Parser.tokenPeekAt(index).getValue())) {
				index++;
			}
			// Sind Array-Klammern angegeben?
			boolean array = false;
			while ((Parser.tokenPeekAt(index).getType() == TokenType.TOKEN_TYPE_OPERATOR && Token.tokenIsOperator(Parser.tokenPeekAt(index), "[")) || (Parser.tokenPeekAt(index).getType() == TokenType.TOKEN_TYPE_SYMBOL && (char) Parser.tokenPeekAt(index).getValue() == ']')) {
				array = true;
				index++;
			}
			// Ist ein Namensraum angegeben?
			token = Parser.tokenPeekAt(index);
			// Ist ein Namensraum angegeben?
			boolean namespace = false;
			if (token.getType() == TokenType.TOKEN_TYPE_IDENTIFIER && Parser.getNamespaceNodeIfExisting(null, (String) token.getValue()) != null && Token.tokenIsOperator(Parser.tokenPeekAt(index + 1), "::")) {
				namespace = true;
				index += 2;
			}
			token = Parser.tokenPeekAt(index + 1);
			if (token.getType() == TokenType.TOKEN_TYPE_SYMBOL && (Character) token.getValue() == ';') {
				Parser.structDeclarationWithoutParentheses = true;
				return true;
			}
			// Parsen wir gerade Funktionsargumente?
			else if (Parser.insideFunctionArgumentParsing) {
				return true;
			}
			// Parsen wir gerade Argumente in einem throw-Funktionskopf?
			else if (Parser.insideThrowInsideFunctionHead) {
				return true;
			}
			// Parsen wir ein Operator-Overloading?
			else if ((token.getType() == TokenType.TOKEN_TYPE_OPERATOR && Parser.functionIsOperatorFlag)) {
				return true;
			}
			// Parsen wir einen Konstruktoraufruf?
			else if (Token.tokenIsOperator(token, "=") && (Token.tokenIsKeyword(Parser.tokenPeekAt(index + 2), "new") || ((Token.tokenIsIdentifier(Parser.tokenPeekAt(index + 2)) && SymbolTable.S_EQ((String) Parser.tokenPeekAt(index + 2).getValue(), identifier))) && !(array) && !(namespace))) {
				Parser.isConstructorCall = true;
				return true;
			}
			else if (Token.tokenIsOperator(token, "=") && (Token.tokenIsKeyword(Parser.tokenPeekAt(index + 2), "new") || ((Token.tokenIsIdentifier(Parser.tokenPeekAt(index + 4)) && SymbolTable.S_EQ((String) Parser.tokenPeekAt(index + 4).getValue(), identifier))) && !(array) && namespace)) {
				Parser.isConstructorCall = true;
				return true;
			}
			// Ist es eine konkrete Angabe zu Generics?
			else if (Token.tokenIsOperator(Parser.tokenPeekAt(index), ",") || Token.tokenIsOperator(Parser.tokenPeekAt(index), ">") && !(Token.tokenIsSymbol(token, ')'))) {
				return true;
			}
			// Arrayinitialisierung im C++ Style.
			else if (Token.tokenIsOperator(token, "=") && (Token.tokenIsKeyword(Parser.tokenPeekAt(index + 2), "new") || (Token.tokenIsIdentifier(Parser.tokenPeekAt(index + 2)) && SymbolTable.S_EQ((String) Parser.tokenPeekAt(index + 2).getValue(), identifier))) && array) {
				return true;
			}
			// Arrayinitialisierung im C-Style.
			else if (Token.tokenIsOperator(token, "[") && Parser.tokenPeekAt(index + 2).getType() == TokenType.TOKEN_TYPE_NUMBER && Token.tokenIsSymbol(Parser.tokenPeekAt(index + 3), ']')) {
				return true;
			}
			// Parsen wir einen sonstigen Funktionsaufruf oder Variablenbezeichner (eine Expression)?
			else if (Token.tokenIsOperator(token, "=")) {
				index += 2;
				token = Parser.tokenPeekAt(index);
				// Ist ein Namensraum angegeben?
				if (namespace) {
					index += 1;
				}
				token = Parser.tokenPeekAt(index);
				if (Token.tokenIsOperator(token, "::")) {
					index += 1;
				}
				token = Parser.tokenPeekAt(index);
				// Hier können auch Unary-Operatoren stehen.
				while (token.getType() == TokenType.TOKEN_TYPE_OPERATOR && SymbolTable.isUnaryOperator((String) Parser.tokenPeekAt(index).getValue())) {
					index++;
				}
				if (Parser.tokenPeekAt(index).getType() == TokenType.TOKEN_TYPE_IDENTIFIER) {
					return true;
				}
			}
			// Parsen wir eine Struct-Funktion (bei der Verwendung von "static" landen wir hier)?
			else if (Token.tokenIsIdentifier(Parser.tokenPeekAt(index)) && Token.tokenIsOperator(token, "(")) {
				return true;
			}
		}
		return false;
	}
	
	// Wenn wir etwas wie "classname a = classname b" parsen wollen, überprüfen wir, ob wir an der richtigen Stelle sind,
	public static boolean isStructInitialization(String identifier) {
		Symbol<Object> sym = Symresolver.symresolverGetSymbol(Parser.currentProcess, identifier, Scope.scopeCurrent(Parser.currentProcess), NodeType.NODE_TYPE_STRUCT);
		if (sym != null && sym.getData().getType() == NodeType.NODE_TYPE_STRUCT) {
			Token<Object> expectedAssignmentToken = Parser.tokenPeekAt(2);
			if (expectedAssignmentToken.getValue() instanceof String && SymbolTable.S_EQ((String) expectedAssignmentToken.getValue(), "=")) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isStructFunction(String identifier) {
		Symbol<Object> sym = Symresolver.symresolverGetSymbol(Parser.currentProcess, identifier, Scope.scopeCurrent(Parser.currentProcess), NodeType.NODE_TYPE_STRUCT);
		if (sym != null && sym.getData().getType() == NodeType.NODE_TYPE_STRUCT) {
			int index = 1;
			Token<Object> token = Parser.tokenPeekAt(index);
			// Sind Generics angegeben?
			if (token.getType() == TokenType.TOKEN_TYPE_OPERATOR && Token.tokenIsOperator(token, "<")) {
				index++;
				Parser.isStructWithDefinedGenerics = true;
				token = Parser.tokenPeekAt(index);
				while(token.getType() != TokenType.TOKEN_TYPE_OPERATOR && !(Token.tokenIsOperator(token, ">"))) {
					token = Parser.tokenPeekAt(index);
					index++;
				}
				token = Parser.tokenPeekAt(index);
			}
			// Sind Unary-Operatoren angegeben?
			while (Parser.tokenPeekAt(index).getType() == TokenType.TOKEN_TYPE_OPERATOR && SymbolTable.isUnaryOperator((String) Parser.tokenPeekAt(index).getValue())) {
				index++;
			}
			// Sind Array-Klammern angegeben?
			while ((Parser.tokenPeekAt(index).getType() == TokenType.TOKEN_TYPE_OPERATOR && Token.tokenIsOperator(Parser.tokenPeekAt(index), "[")) || (Parser.tokenPeekAt(index).getType() == TokenType.TOKEN_TYPE_SYMBOL && (char) Parser.tokenPeekAt(index).getValue() == ']')) {
				index++;
			}
			// Ist ein Namensraum angegeben?
			if (Parser.getNamespaceNodeIfExisting(null, (String) Parser.tokenPeekAt(index).getValue()) != null && Token.tokenIsOperator(Parser.tokenPeekAt(index + 1), "::")) {
				index += 2;
			}
			Token<Object> expectedParenthesisToken = Parser.tokenPeekAt(index + 1);
			if (expectedParenthesisToken == null) {
				Compiler.compilerError(Parser.currentProcess, "Hier werden () erwartet.\n");
			}
			if (expectedParenthesisToken.getValue() instanceof String && SymbolTable.S_EQ((String) expectedParenthesisToken.getValue(), "(")) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean parsePossibleStructDeclaration(String identifier, History<Object> history) {
		if (Parser.isStructDatatypeType(identifier) || Parser.isStructInitialization(identifier)) {
			Parser.identifierIsStructDeclarationMethodOrAssignment = true;
			Parser.parseVariableFunctionOrStructUnion(history, null);
			Parser.structDeclarationWithoutParentheses = false;
			Parser.isConstructorCall = false;
			return true;
		}
		else if (Parser.isStructFunction(identifier)) {
			Parser.identifierIsStructDeclarationMethodOrAssignment = true;
			Token<Object> token = Parser.tokenPeekNext();
			int index = 0;
			// Sind Generics angegeben?
			if (token.getType() == TokenType.TOKEN_TYPE_OPERATOR && Token.tokenIsOperator(token, "<")) {
				index++;
				token = Parser.tokenPeekAt(index);
				while(!(Token.tokenIsOperator(token, ">"))) {
				token = Parser.tokenPeekAt(index);
					index++;
				}
				token = Parser.tokenPeekAt(index);
			}
			// Sind Unary-Operatoren angegeben?
			while (Parser.tokenPeekAt(index).getType() == TokenType.TOKEN_TYPE_OPERATOR && SymbolTable.isUnaryOperator((String) Parser.tokenPeekAt(index).getValue())) {
				index++;
			}
			// Sind Array-Klammern angegeben?
			while ((Parser.tokenPeekAt(index).getType() == TokenType.TOKEN_TYPE_OPERATOR && Token.tokenIsOperator(Parser.tokenPeekAt(index), "[")) || (Parser.tokenPeekAt(index).getType() == TokenType.TOKEN_TYPE_SYMBOL && (char) Parser.tokenPeekAt(index).getValue() == ']')) {
				index++;
			}
			token = Parser.tokenPeekAt(index);
			Node<Object> namespaceIdentifierNode = null;
			if (token.getType() == TokenType.TOKEN_TYPE_IDENTIFIER && Parser.isNamespaceOrStructAsNamespaceNotation(history, (String) token.getValue())) {
				namespaceIdentifierNode = Parser.parseNamespaceOrStructAsNamespaceNotation(history, false);
			}
			token = Parser.tokenPeekAt(index + 2);
			// Parsen des Operatorüberladens.
			if (SymbolTable.S_EQ((String) Parser.tokenPeekAt(index).getValue(), "operator") && token.getType() == TokenType.TOKEN_TYPE_OPERATOR && SymbolTable.S_EQ((String) token.getValue(), "(")) {
				Parser.functionIsOperatorFlag = true;
			}
			Parser.parseVariableFunctionOrStructUnion(history, null);
			return true;
		}
		return false;
	}
	
	public static void handleEndlSemicolon() {
		Token<Object> token = Parser.tokenPeekNext();
		if (token.getType() == TokenType.TOKEN_TYPE_SYMBOL) {
			Parser.expectSym(';');
			token = Parser.tokenPeekNext();
			// Steht das Semikolon an einer validen Stelle?
			if (token != null && token.getType() != TokenType.TOKEN_TYPE_IDENTIFIER && token.getType() != TokenType.TOKEN_TYPE_KEYWORD && token.getType() != TokenType.TOKEN_TYPE_SYMBOL && token.getType() != TokenType.TOKEN_TYPE_NEWLINE && token.getType() != TokenType.TOKEN_TYPE_COMMENT) {
				Compiler.compilerError(Parser.currentProcess, "Nach einem \"endl\" darf nur am Ende des Statements ein ';' stehen.\n");
			}
		}
	}
	
	// Gibt zurück, ob eine Funktion mit dem gegebenen Namen existiert (prüft nicht die übergebenen Parameter).
	public static boolean identifierIsPossibleFunctionCall(History<Object> history, String identifier, Node<Object> namespaceIdentifierNode) {
		Symbol<Object> sym = null;
		Scope<Object> scope = null;
		if (namespaceIdentifierNode == null) {
			scope = Scope.scopeCurrent(Parser.currentProcess);
		}
		else {
			scope = Parser.getDeclaredNamespaceOrStructBodyScope(history, (String) namespaceIdentifierNode.getValue());
		}
		sym = Symresolver.symresolverGetSymbol(Parser.currentProcess, identifier, scope, NodeType.NODE_TYPE_FUNCTION);
		if (sym != null && sym.getType() == SymbolType.SYMBOL_TYPE_NATIVE_FUNCTION) {
			return true;
		}
		return false;
	}
	
	// Kümmert sich um "cout" und "cerr", sowie "setw", "setprecision" und "setfill".
	public static void handleStdCout(Token<Object> token) {
		String tokenValue = null;
		if (token.getType() != TokenType.TOKEN_TYPE_SYMBOL && token.getType() != TokenType.TOKEN_TYPE_NUMBER) {
			tokenValue = (String) token.getValue();
		}
		else if (token.getType() == TokenType.TOKEN_TYPE_NUMBER) {
			tokenValue = ((Long) token.getValue()).toString();
		}
		// Parsen wir gerade einen Namensraum, verlassen wir die Funktion.
		if ((token.getType() == TokenType.TOKEN_TYPE_IDENTIFIER && Parser.tokenNextIsOperator("::")) || (token.getType() == TokenType.TOKEN_TYPE_OPERATOR && SymbolTable.S_EQ((String) tokenValue, "::"))) {
			return;
		}
		// 1. Fall: Wir sind am Anfang und finden ein "cout" oder "cerr"?
		if (!(Parser.coutFlags[0]) && token.getType() == TokenType.TOKEN_TYPE_IDENTIFIER && (SymbolTable.S_EQ(tokenValue, "cout") || SymbolTable.S_EQ(tokenValue, "cerr"))) {
			if (!(Namespace.namespaceOrMemberIsDefined(Parser.currentProcess, "std", tokenValue)) && !(Parser.temporaryStdNamespaceFlag)) {
				Compiler.compilerError(Parser.currentProcess, "Um \"" + tokenValue + "\" nutzen zu können, müssen Sie den Namensraum \"std\" einbinden.\n");
			}
			if (!(Parser.iostreamLibraryIsIncluded)) {
				Compiler.compilerError(Parser.currentProcess, "Um \"" + tokenValue + "\" nutzen zu können, müssen Sie die Bibliothek \"iostream\" einbinden.\n");
			}
			if (SymbolTable.S_EQ(tokenValue, "cout") && Parser.temporaryStdNamespaceFlag || Namespace.namespaceOrMemberIsDefined(Parser.currentProcess, "std", tokenValue)) {
				Parser.coutFlags[0] = true;
				Parser.temporaryStdNamespaceFlag = false;
			}
			return;
		}
		// 2. Fall: Wir sind beim Operator "<<" den wir erwarten.
		else if (Parser.coutFlags[0] && !(Parser.coutFlags[1])) {
			if (token.getType() == TokenType.TOKEN_TYPE_OPERATOR && SymbolTable.S_EQ(tokenValue, "<<")) {
				Parser.coutFlags[1] = true;
			}
			else {
				Compiler.compilerError(Parser.currentProcess, "Hier wird ein \"<<\"-Operator erwartet, aber etwas anderes wurde eingegeben.\n");
			}
			return;
		}
		// 3. Fall: Wir erwarten eine Nummer oder einen String oder einen Bezeichner, den wir ausgeben möchten.
		else if (Parser.coutFlags[1] && !(Parser.coutFlags[2])) {
			if (token.getType() == TokenType.TOKEN_TYPE_IDENTIFIER && SymbolTable.S_EQ(tokenValue, "endl")) {
				if (!(Namespace.namespaceOrMemberIsDefined(Parser.currentProcess, "std", tokenValue)) && !(Parser.temporaryStdNamespaceFlag)) {
					Compiler.compilerError(Parser.currentProcess, "Um \"" + tokenValue + "\" nutzen zu können, müssen Sie den Namensraum \"std\" einbinden.\n");
				}
			}
			else if (token.getType() == TokenType.TOKEN_TYPE_IDENTIFIER && (SymbolTable.S_EQ(tokenValue, "setw") || SymbolTable.S_EQ(tokenValue, "setfill") || SymbolTable.S_EQ(tokenValue, "setprecision"))) {
				if (!(Namespace.namespaceOrMemberIsDefined(Parser.currentProcess, "std", tokenValue)) && !(Parser.temporaryStdNamespaceFlag)) {
					Compiler.compilerError(Parser.currentProcess, "Um \"" + tokenValue + "\" nutzen zu können, müssen Sie den Namensraum \"std\" einbinden.\n");
				}
				if (!(Parser.iomanipLibraryIsiIncluded)) {
					Compiler.compilerError(Parser.currentProcess, "Um \"" + tokenValue + "\" nutzen zu können, müssen Sie die Bibliothek \"iomanip\" einbinden.\n");
				}
			}
			if (token.getType() == TokenType.TOKEN_TYPE_NUMBER || token.getType() == TokenType.TOKEN_TYPE_STRING || (token.getType() == TokenType.TOKEN_TYPE_IDENTIFIER && Parser.coutFlags[1])) {
				Parser.coutFlags[2] = true;
				Parser.temporaryStdNamespaceFlag = false;
			}
			else {
				Compiler.compilerError(Parser.currentProcess, "Hier wird etwas erwartet, was Sie auf dem Bildschirm ausgeben wollen.\n");
			}
			return;
		}
		// 4. Fall: Wir finden Klammern für einen Funktionsaufruf.
		else if (token.getType() == TokenType.TOKEN_TYPE_OPERATOR && SymbolTable.S_EQ(tokenValue, "(") && Parser.coutFlags[2] && !(Parser.possibleFunctionCallInsideExpressionInCoutStatementEnd)) {
			Parser.possibleFunctionCallInsideExpressionInCoutStatementStart = true;
			return;
		}
		// 5. Fall: Wir finden Klammern für ein Array-Elementzugriff.
		else if (token.getType() == TokenType.TOKEN_TYPE_OPERATOR && SymbolTable.S_EQ(tokenValue, "[") && Parser.coutFlags[2] && !(Parser.possibleArrayAccessInsideExpressionInCoutStatementEnd)) {
			Parser.possibleArrayAccessInsideExpressionInCoutStatementStart = true;
			return;
		}
		// 6 Fall: Wir finden ein Semikolon und schließen das Statement ab.
		else if (token.getType() == TokenType.TOKEN_TYPE_SYMBOL && (char) token.getValue() == ';' && (Parser.coutFlags[3] || Parser.possibleArrayAccessInsideExpressionInCoutStatementEnd)) {
			if (Parser.parserCurrentBody == null) {
				Parser.expectSym(';');
			}
			Parser.coutFlags[0] = false;
			Parser.coutFlags[1] = false;
			Parser.coutFlags[2] = false;
			Parser.coutFlags[3] = false;
			Parser.possibleFunctionCallInsideExpressionInCoutStatementStart = false;
			return;
		}
		// 7. Fall: Wir finden kein Semikolon, es wird weitere Inputs geben.
		else if ((token.getType() != TokenType.TOKEN_TYPE_SYMBOL || (char) token.getValue() != ';') && Parser.coutFlags[2] && !(Parser.coutFlags[3]) && !(Parser.possibleFunctionCallInsideExpressionInCoutStatementStart) && !(Parser.possibleArrayAccessInsideExpressionInCoutStatementStart) && !(Token.tokenIsSymbol(token, ')'))) {
			Parser.coutFlags[1] = false; 
			Parser.coutFlags[2] = false;
			Parser.handleStdCout(token);
			return;
		}
	}
	
	// Kümmert sich um "cin".
	public static void handleStdCin(Token<Object> token) {
		String tokenValue = null;
		if (token.getType() != TokenType.TOKEN_TYPE_SYMBOL && (token.getType() == TokenType.TOKEN_TYPE_IDENTIFIER || token.getType() == TokenType.TOKEN_TYPE_OPERATOR)) {
			tokenValue = (String) token.getValue();
		}
		// Parsen wir gerade einen Namensraum, verlassen wir die Funktion.
		if ((token.getType() == TokenType.TOKEN_TYPE_IDENTIFIER && Parser.tokenNextIsOperator("::")) || (token.getType() == TokenType.TOKEN_TYPE_OPERATOR && SymbolTable.S_EQ((String) tokenValue, "::"))) {
			return;
		}
		// 1. Fall: Wir sind am Anfang und finden ein "cout" oder "cerr"?
		if (!(Parser.cinFlags[0]) && token.getType() == TokenType.TOKEN_TYPE_IDENTIFIER &&  SymbolTable.S_EQ(tokenValue, "cin")) {
			if (!(Namespace.namespaceOrMemberIsDefined(Parser.currentProcess, "std", tokenValue)) && !(Parser.temporaryStdNamespaceFlag)) {
				Compiler.compilerError(Parser.currentProcess, "Um \"" + tokenValue + "\" nutzen zu können, müssen Sie den Namensraum \"std\" einbinden.\n");
			}
			if (!(Namespace.namespaceOrMemberIsDefined(Parser.currentProcess, "std", tokenValue)) && !(Parser.temporaryStdNamespaceFlag)) {
				Compiler.compilerError(Parser.currentProcess, "Um \"" + tokenValue + "\" nutzen zu können, müssen Sie den Namensraum \"std\" einbinden.\n");
			}
			if (SymbolTable.S_EQ(tokenValue, "cin") && Parser.temporaryStdNamespaceFlag || Namespace.namespaceOrMemberIsDefined(Parser.currentProcess, "std", tokenValue)) {
				Parser.cinFlags[0] = true;
				Parser.temporaryStdNamespaceFlag = false;
			}
			return;
		}
		// 2. Fall: Wir sind beim Operator ">>" den wir erwarten.
		else if (Parser.cinFlags[0] && !(Parser.cinFlags[1])) {
			if (token.getType() == TokenType.TOKEN_TYPE_OPERATOR && SymbolTable.S_EQ(tokenValue, ">>")) {
				Parser.cinFlags[1] = true;
			}
			else {
				Compiler.compilerError(Parser.currentProcess, "Hier wird ein \">>\"-Operator erwartet, aber etwas anderes wurde eingegeben.\n");
			}
			return;
		}
		// 3. Fall: Wir erwarten einen Bezeichner, da wir die Eingabe in eine bestehende Variable speichern möchten.
		else if (Parser.cinFlags[1] && !(Parser.cinFlags[2])) {
			if (token.getType() == TokenType.TOKEN_TYPE_IDENTIFIER && SymbolTable.S_EQ(tokenValue, "endl")) {
				Compiler.compilerError(Parser.currentProcess, "\"" + tokenValue + "\" wird nur bei \"cout\"-Statements verwendet.\n");
			}
			if (token.getType() == TokenType.TOKEN_TYPE_IDENTIFIER && Parser.cinFlags[1]) {
				Parser.cinFlags[2] = true;
				Parser.temporaryStdNamespaceFlag = false;
			}
			else {
				Compiler.compilerError(Parser.currentProcess, "Hier wird ein Bezeichner der Variable erwartet, in der Sie eine Eingabe vom Bildschirm speichern möchten.\n");
			}
			return;
		}
		// 4. Fall: Wir finden ein Semikolon und schließen das Statement ab.
		else if (token.getType() == TokenType.TOKEN_TYPE_SYMBOL && (char) token.getValue() == ';' && Parser.cinFlags[3]) {
			Parser.expectSym(';');
			Parser.cinFlags[0] = false;
			Parser.cinFlags[1] = false;
			Parser.cinFlags[2] = false;
			Parser.cinFlags[3] = false;
			return;
		}
		// 5. Fall: Weitere Inputs gibt es nicht bei cin-Statements, daher haben wir einen Compiler-Error.
		else if((token.getType() != TokenType.TOKEN_TYPE_SYMBOL || (char) token.getValue() != ';') && Parser.cinFlags[2] && !(Parser.cinFlags[3])) {
			Compiler.compilerError(Parser.currentProcess, "\"" + tokenValue + "\" ist keine valide Eingabe in einem cin-Statement.\n");
		}
	}
	
	public static int parseStdAsNamespaceNotation(History<Object> history, String std) {
		if (SymbolTable.S_EQ(std, "std") && SymbolTable.S_EQ((String) Parser.tokenPeekAt(1).getValue(), "::")) {
			Parser.temporaryStdNamespaceFlag = true;
			if (SymbolTable.S_EQ((String) Parser.tokenPeekAt(2).getValue(), "string")) {
				Parser.tokenNext();
				Parser.expectOp("::");
				return -1;
			}
		}
		return 0;
	}
	
	public static void checkForCorrectConcreteGenericsWhileStructInit(Datatype<Node<Object>> structDatatypeWithGenerics) {
		if (structDatatypeWithGenerics.getConcreteDatatypesNode() == null) {
			Compiler.compilerError(Parser.currentProcess, "Für die Klasse \"" + structDatatypeWithGenerics.getTypeStr() + "\" sind keine konkreten Datentypen für die Generics eingetragen, weswegen Sie auch keine angeben können.\n");
		}
		Vector<Datatype<Node<Object>>> concreteDatatypesVectorToCheck = Vector.vectorCreate();
		Parser.expectOp("<");
		while(!(Parser.tokenNextIsOperator(">"))) {
			Datatype<Node<Object>> newDtype = new Datatype<Node<Object>>();
			Parser.parseDatatype(newDtype);
			concreteDatatypesVectorToCheck.vectorPush(newDtype);
			// Wirf den Datentyp vom Stack.
			if (!(Token.tokenIsOperator(Parser.tokenPeekNext(), ">"))) {
				expectOp(",");
			}
		}
		expectOp(">");
		@SuppressWarnings("unchecked")
		ConcreteDefinitionOfGenericStruct<Object> concreteDefinitionOfGenericStruct = (ConcreteDefinitionOfGenericStruct<Object>) structDatatypeWithGenerics.getConcreteDatatypesNode().getValue();
		Vector<Datatype<Node<Object>>> concreteDatatypesVector = concreteDefinitionOfGenericStruct.getConcreteDatatypesVector();
		if (concreteDefinitionOfGenericStruct.getConcreteDatatypesVector().vectorCount() != concreteDatatypesVectorToCheck.vectorCount()) {
			Compiler.compilerError(Parser.currentProcess, "Für die Klasse \"" + structDatatypeWithGenerics.getTypeStr() + "\" müssen Sie beim Konstruktoraufruf genauso viele Datentypen bei der konkreten Angabe für die generischen Datentypen angeben, wie auf der linken Seite des Zuweisungsstatements.\n");
		}
		for(int i = 0; i < concreteDatatypesVector.vectorCount(); i++) {
			if (!(SymbolTable.S_EQ(concreteDatatypesVector.vectorPeekAt(i).getTypeStr(), concreteDatatypesVectorToCheck.vectorPeekAt(i).getTypeStr()))) {
				Compiler.compilerError(Parser.currentProcess, "Beim Konstruktoraufruf der Klasse \"" + structDatatypeWithGenerics.getTypeStr() + "\" ist der angegebene konkrete Datentyp für die generischen Variablen an der Stelle " + (i + 1) + " nicht identisch auf den beiden Seiten des Zuweisungsaufrufs.\n");
			}
		}
	}
	
	public static void parseConstructorExpressionRightSide(History<Object> history, String classname) {
		Datatype<Node<Object>> structDatatypeWithGenerics = Parser.currentSUDatatype;
		if (!(SymbolTable.S_EQ(structDatatypeWithGenerics.getTypeStr(), classname))) {
			Compiler.compilerError(Parser.currentProcess, "Bei einem Konstruktorenaufruf der Klasse \"" + structDatatypeWithGenerics.getTypeStr() + "\" müssen Sie auf der rechten Seite des Zuweisungsstatements die gleiche Klasse oder Struktur angeben, wie auf der linken Seite.\n");
		}
		Parser.parseIdentifier(history);
		if (Token.tokenIsOperator(Parser.tokenPeekNext(), "<")) {
			Parser.checkForCorrectConcreteGenericsWhileStructInit(structDatatypeWithGenerics);
		}
		Parser.isConstructorCall = false;
		CleanCodeAnalyzer.parsedConstructorCall = true;
	}
	
	// Schaut sich das aktuelle Token an. Finden wir eine Zahl, so wird sie
	// auf den Nodestack gepusht. Finden wir einen Operator, so könnte es eine
	// Expression sein und rufen parseExp() auf.
	public static int parseExpressionableSingle(History<Object> history) {
		Token<Object> token = Parser.tokenPeekNext();
		// In diesem Fall haben wir bereits das ganze Inputfile geparsed.
		if (token == null) {
			return -1;
		}
		history.setFlags(history.getFlags() | NodeFlag.NODE_FLAG_INSIDE_EXPRESSION);
		int res = -1;
		Parser.cinOrCoutStatementOverFlag = false;
		switch(token.getType()) {
			case TOKEN_TYPE_NUMBER: {
				Parser.parseSingleTokenToNode();
				res = 0;
				break;
			}
			case TOKEN_TYPE_IDENTIFIER: {
				// Ist es eine Struct- Definition oder Deklarierung oder Funktion des Typs eines Structs?
				if(Parser.parsePossibleStructDeclaration((String) token.getValue(), history)) {		
					res = -1;
					break;
				}
				if (Parser.isNamespaceOrStructAsNamespaceNotation(history, (String) token.getValue()) && !(SymbolTable.S_EQ((String) token.getValue(), "std")) && !(Parser.insideVariableParsing)) {
					res = Parser.parseNamespaceProclamationWithoutAccessSpecifier(history);
					break;
				}
				// Befinden wir uns bei einem Konstruktorenaufruf wie "Complex<int> a = new Complex<int>();" auf der rechten Seite?
				if (Parser.isConstructorCall) {
					Parser.parseConstructorExpressionRightSide(history, (String) token.getValue());
					res = 0;
					break;
				}
				// Falls wir ein "std::string" parsen, schmeißen wir "std::" vom Stack und parsen die string Variable, statt einer Expression.
				if (Parser.parseStdAsNamespaceNotation(history, (String) token.getValue()) == -1) {
					Parser.parseKeyword(history);
					break;
				}
				Parser.parseIdentifier(history);
				if (!(Parser.possibleFunctionCallInsideExpression) && Parser.identifierIsPossibleFunctionCall(history, (String) token.getValue(), Parser.tempNamespaceIdentifierNode)) {
					Parser.possibleFunctionCallInsideExpression = true;
				}
				res = 0;
				break;
			}
			case TOKEN_TYPE_OPERATOR: {
				if (!(Parser.cinFlags[0])) {
					Parser.handleStdCout(token);
				}
				if (!(Parser.coutFlags[0])) {
					Parser.handleStdCin(token);
				}
				Parser.parseExp(history);
				if (Parser.arrayExpressionIsNotFinished) {
					res = 0;
				}
				break;
			}
			case TOKEN_TYPE_KEYWORD: {
				res = Parser.parseKeyword(history);
				break;
			}
			case TOKEN_TYPE_STRING: {
				Parser.parseString(history);
				res = 0;
				break;
			}
			default: {
				break;
			}
		}
		if (Parser.coutFlags[2] && !(Parser.possibleFunctionCallInsideExpressionInCoutStatementStart) && !(Parser.possibleArrayAccessInsideExpressionInCoutStatementStart) && !(Parser.coutFlags[3])) {
			if (Parser.tokenPeekNext().getType() == TokenType.TOKEN_TYPE_SYMBOL && (char) Parser.tokenPeekNext().getValue() == ';') {
				Parser.coutFlags[3] = true;
				Parser.cinOrCoutStatementOverFlag = true;
			}
		}
		if (Parser.cinFlags[2] && !(Parser.cinFlags[3])) {
			if (Parser.tokenPeekNext().getType() == TokenType.TOKEN_TYPE_SYMBOL && (char) Parser.tokenPeekNext().getValue() == ';') {
				Parser.cinFlags[3] = true;
				Parser.cinOrCoutStatementOverFlag = true;
			}
		}
		if (token.getType() != TokenType.TOKEN_TYPE_KEYWORD && token.getType() != TokenType.TOKEN_TYPE_OPERATOR && !(Parser.possibleFunctionCallInsideExpressionInCoutStatementStart)) {
			if (!(Parser.cinFlags[0])) {
				Parser.handleStdCout(token);
			}
			if (!(Parser.coutFlags[0])) {
				Parser.handleStdCin(token);
			}
		}
		if ((Parser.cinFlags[0]) && Parser.possibleFunctionCallInsideExpressionInCoutStatementStart && token.getType() == TokenType.TOKEN_TYPE_SYMBOL && Token.tokenIsSymbol(token, ')')) {
			Parser.possibleFunctionCallInsideExpressionInCoutStatementStart = false;
			Parser.possibleFunctionCallInsideExpressionInCoutStatementEnd = true;
		}
		if ((Parser.coutFlags[0]) && Parser.possibleArrayAccessInsideExpressionInCoutStatementStart && Token.tokenIsSymbol(token, ']')) {
			Parser.possibleArrayAccessInsideExpressionInCoutStatementStart = false;
			Parser.possibleArrayAccessInsideExpressionInCoutStatementEnd = true;
		}
		if (Parser.cinOrCoutStatementOverFlag) {
			res = -1;
		}
		if (Parser.possibleFunctionCallInsideExpressionInCoutStatementEnd && !(Token.tokenIsSymbol(token, ')'))) {
			res = 0;
			Parser.possibleFunctionCallInsideExpressionInCoutStatementEnd = false;
		}
		if (Parser.arrayExpressionFlag && res == -1 && Parser.tokenNextAtIsOperator(0, "=")) {
			res = 0;
		}
		else if (Parser.arrayExpressionFlag && res == 0 && Parser.tokenNextIsSymbol(';')) {
			res = -1;
			Parser.arrayExpressionFlag = false;
			Parser.arrayExpressionIsNotFinished = false;
		}
		else if (Parser.arrayExpressionFlag && res == -1 && Parser.tokenPeekNext().getType() == TokenType.TOKEN_TYPE_OPERATOR) {
			res = 0;
		}
		else if (Parser.insideParsingExpressionAsArrayBracketSize && Parser.tokenNextIsSymbol(']') && !(Parser.possibleArrayAccessInsideExpressionInCoutStatementStart)) {
			res = -1;
		}
		else if (Parser.insideParsingExpressionAsArrayBracketSize && Parser.tokenPeekNext().getType() == TokenType.TOKEN_TYPE_OPERATOR) {
			res = 0;
		}
		if (Parser.possibleFunctionCallInsideExpression && Parser.parserLastToken.getType() == TokenType.TOKEN_TYPE_SYMBOL && (char) Parser.parserLastToken.getValue() == ')' && !(Parser.insideFunctionArgumentParsing)) {
			if (!(Parser.insideVariableParsing) && Parser.tempNamespaceIdentifierNode == null) {
				Parser.expectSym(';');
				Parser.possibleFunctionCallInsideExpression = false;
			}
			else if (!(Parser.insideVariableParsing) && Parser.tempNamespaceIdentifierNode == null) {
				Parser.possibleFunctionCallInsideExpression = false;
			}
			else if (!(Parser.insideVariableParsing) && Parser.tempNamespaceIdentifierNode != null) {
				Parser.expectSym(';');
				Parser.possibleFunctionCallInsideExpression = false;
			}
		}
		if ((Parser.insideForLoopInitializing || Parser.insideForLoopConditional) && !(Parser.insideVariableParsing) && Token.tokenIsSymbol(Parser.tokenPeekNext(), ';')) {
			res = -1;
			Parser.expectSym(';');
		}
		return res;
	}
	
	// Geht durch alle Tokens und versucht aus den entstanden Nodes Expressions zu erstellen.
	// Haben wir ein Token gefunden welches Teil einer Expression sein könnte,schauen wir, ob es Teil einer Expression ist.
	public static void parseExpressionable(History<Object> history) {
		while(Parser.parseExpressionableSingle(history) == 0) {
			
		}
	}
	
	public static void parseKeywordForGlobal() {
		Parser.parseKeyword(Parser.historyBegin(0));
		if (!(Parser.declaredFunctionFromOutsideNamespace)) {
			Node<Object> node = NodeFunctions.nodePop();
			switch(node.getType()) {
				case NODE_TYPE_STRUCT:
					break;
				case NODE_TYPE_UNION:
					break;
				case NODE_TYPE_VARIABLE:
					break;
				case NODE_TYPE_FUNCTION:
					break;
			}
			NodeFunctions.nodePush(node);
		}
	}
	
	// Gehe durch alle Tokens und erstelle Knoten.
	public static int parseNext() {
		Token<Object> token = Parser.tokenPeekNext();
		if (token == null) {
			return -1;
		}
		
		int res = 0;
		switch(token.getType()) {
			// Diese drei Typen können direkt in Knoten übersetzt werden.
			case TOKEN_TYPE_NUMBER: {
				Parser.parseExpressionable(Parser.historyBegin(0));
				break;
			}
			case TOKEN_TYPE_IDENTIFIER: {
				Parser.parseExpressionable(Parser.historyBegin(0));
				break;
			}
			case TOKEN_TYPE_STRING: {
				Parser.parseExpressionable(Parser.historyBegin(0));
				break;
			}
			case TOKEN_TYPE_KEYWORD: {
				Parser.parseKeywordForGlobal();
				break;
			}
			case TOKEN_TYPE_SYMBOL: {
				Parser.parseSymbol();
				break;
			}
			default: {
				break;
			}
		}
		return res;
	}
	
	public static ParseMessage parse(CompileProcess process) {
		CleanCodeAnalyzer.analyzeFileSize(process);
		Scope.scopeCreateRoot(process); // Erstelle den Global-Scope.
		Parser.currentProcess = process;
		Parser.parserLastToken = null;
		NodeFunctions.nodeSetVector(process.getNodeVec(), process.getNodeTreeVec());
		Parser.parserBlankNode = NodeFunctions.nodeCreate(new Node<Object>(NodeType.NODE_TYPE_BLANK));
		Parser.parserFixupSys = Fixup.fixupSysNew();
		Node<Object> node = null;
		process.getTokenVec().vectorSetPeekPointer(0);
		while(Parser.parseNext() == 0) {
			// Schaut sich den letzten Knoten im Vektor für normale Operationen an.
			node = NodeFunctions.nodePeek();
			// Danach wird der Knoten in den Treevektor gelegt.
			if (!(Parser.declaredFunctionFromOutsideNamespace)) {
				process.getNodeTreeVec().vectorPush(node);
			}
			else {
				Parser.declaredFunctionFromOutsideNamespace = false;
			}
		}
		CleanCodeAnalyzer.analysisAfterParsing(Parser.currentProcess, Parser.noNewlinesBetweenLibraryDirectivesObservation, Parser.noNewlinesBetweenNamespaceDefinitionsObservation, Symresolver.newlineBeforeGlobalDefinedMethodFromOutsideStruct, Parser.functionNodeVec);
		assert(Fixup.fixupsResolve(Parser.parserFixupSys, Parser.currentProcess));
		return ParseMessage.PARSE_ALL_OK;
	}
}
