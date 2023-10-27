package cleanCodeAnalyzer;

import java.util.ArrayList;
import compiler.CompileProcess;
import helpers.Vector;
import lexer.SymbolTable;
import lexer.Token;
import lexer.TokenType;
import parser.Node;
import parser.NodeType;
import parser.Parser;
import parser.Expression;
import parser.Body;
import parser.Parenthesis;
import parserDatatypes.Function;
import parserDatatypes.FunctionFlag;
import parserDatatypes.AccessSpecifierFlag;
import parserDatatypes.Datatype;
import parserDatatypes.DatatypeFlag;
import parserDatatypes.Struct;
import parserDatatypes.Var;
import parserDatatypes.VarList;

public class CleanCodeAnalyzer {
	public static Vector<String> analyzedStructOrUnionNames = Vector.vectorCreate();
	public static MethodNamingConvention methodNamingConvention = null;
	public static int numberOfWhitespacesPerIndentation = 0;
	public static boolean isFirstLibraryDirective = true;
	public static boolean foundNewlineTokenBeforeLibraryDirective = false;
	public static boolean isFirstNamespaceDefinition = true;
	public static boolean foundNewlineTokenBeforeNamespaceDefinition = false;
	public static boolean isFirstGlobalDefinedFunctionFromOutsideStruct = true;
	public static boolean foundNewlineTokenBeforeGlobalDefinedFunctionFromOutstideStruct = true;
	public static Vector<FunctionCall> calledFunctionIdentifierVec = null;
	public static boolean constructorCall = false;
	public static boolean priorConstructorCall = false;
	public static boolean parsedConstructorCall = false;

	
	public static void analyzeFileSize(CompileProcess compileProcess) {
		int cleanCodeScore = 0;
		CleanCodeObservation<Object> observation = new CleanCodeObservation<Object>(CleanCodeAnalysisBroadCategory.GLOBAL_ANALYSIS, CleanCodeAnalysisConcreteCategory.FILE_SIZE_ANALYSIS, null, null);
		compileProcess.getCleanCodeObservations().vectorPush(observation);
		Token<Object> lastTokenInFile = compileProcess.getTokenVec().vectorPeekAt(compileProcess.getTokenVec().vectorCount() - 1);
		int numberOfLinesInFile = lastTokenInFile.getPos().getLineNumber();
		if (numberOfLinesInFile <= 200) {
			cleanCodeScore += 2;
			observation.setAdvise(null);
		}
		else if (numberOfLinesInFile > 200 && numberOfLinesInFile <= 500) {
			cleanCodeScore += 1;
			observation.setAdvise(observation.getAdvise() + "\tDer Code wäre noch sauberer, wenn die Größe der Datei nicht mehr als 200 Zeilen betragen würde. Sie beträgt jedoch " + numberOfLinesInFile + " Zeilen.\n"); 
		}
		else {
			observation.setAdvise(observation.getAdvise() + "\tDer Code wäre sauberer, wenn die Größe der Datei nicht mehr als 500 Zeilen, besser noch, nicht mehr als 200 Zeilen, betragen würde. Sie beträgt jedoch " + numberOfLinesInFile + " Zeilen.\n"); 
		}
		observation.setCleanCodeObservationScore(observation.getCleanCodeObservationScore() + cleanCodeScore);
		observation.setHighestPossibleScoreForThisObservation(observation.getHighestPossibleScoreForThisObservation() + 2);
	}
	
	private static void analyzeIdentifierForHungarianNotation(CleanCodeObservation<Object> observation, String identifier, String datatypeName) {
		int cleanCodeScore = 0;
		if (!(identifier.contains(datatypeName))) {
			cleanCodeScore += 1;
		}
		else {
			observation.setAdvise(observation.getAdvise() + "\tDer Name der Variable \"" + identifier + "\" enthält den Namen des Datentyps \"" + datatypeName + "\" im Bezeichner, sodass die Ungarische Notation verwendet wird.\n"); 
		}
		observation.setCleanCodeObservationScore(observation.getCleanCodeObservationScore() + cleanCodeScore);
		observation.setHighestPossibleScoreForThisObservation(observation.getHighestPossibleScoreForThisObservation() + 1);
	}
	
	private static boolean structOrUnionNameAlreadyAnalyzed(String structOrUnionName) {
		for(int i = 0; i < CleanCodeAnalyzer.analyzedStructOrUnionNames.vectorCount(); i++) {
			if (SymbolTable.S_EQ(CleanCodeAnalyzer.analyzedStructOrUnionNames.vectorPeekAt(i), structOrUnionName)) {
				return true;
			}
		}
		return false;
	}
	
	private static boolean identifierContainsDigit(String identifier) {
		for(int i = 0; i < identifier.length(); i++) {
			if (Character.isDigit(identifier.charAt(i))) {
				return true;
			}
		}
		return false;
	}
	
	private static boolean identifierConsistsOfUpperCaseAndUnderscores(String identifier) {
		for (int i = 0; i < identifier.length(); i++) {
			if (i == 0 && !(Character.isUpperCase(identifier.charAt(i)))) {
				return false;
			}
			if (!(Character.isUpperCase(identifier.charAt(i))) && identifier.charAt(i) != '_') {
				return false;
			}
			// Natürlich sollten '_' nicht nebeneinander vorkommen, sondern nur als Worttrenner.
			if (i < identifier.length() - 1 && identifier.charAt(i) == '_' && identifier.charAt(i + 1) == '_') {
				return false;
			}
		}
		if (!(Character.isUpperCase(identifier.charAt(identifier.length() - 1)))) {
			return false;
		}
		return true;
	}
	
	private static void analyzeSructOrUnionNameForBeingPascalCase(CleanCodeObservation<Object> observation, String identifier) {
		int cleanCodeScore = 0;
		if (CleanCodeAnalyzer.identifierIsPascalCase(identifier)) {	
			cleanCodeScore += 1;
		}
		else {
			observation.setAdvise(observation.getAdvise() + "\tDer Name der Struktur- oder Klasse, \"" + identifier + "\" entspricht nicht der Title-Case Namenskonvention. Für einen Bezeichner von eine Struktur oder Klasse sollte ein Substantiv oder ein substantivistischer Ausdruck alz Bezeichner verwendet werden, die mit einem Großbuchstaben beginnen. Dabei werden neue Wörter mit einem Großbuchstaben begonnen und zwischen den Anfängen der Wörter werden nur Kleinbuchstaben verwendet.\n"); 
		}
		observation.setCleanCodeObservationScore(observation.getCleanCodeObservationScore() + cleanCodeScore);
		observation.setHighestPossibleScoreForThisObservation(observation.getHighestPossibleScoreForThisObservation() + 1);
	}
	
	private static void analyzeMethodNameForLowerCaseBeginning(CleanCodeObservation<Object> observation, String identifier) {
		int cleanCodeScore = 0;
		// Startet der Name mit einem Großbuchstaben, vergeben wir einen Punkt.
		if (Character.isLowerCase(identifier.charAt(0))) {
			cleanCodeScore += 1;
		}
		else {
			observation.setAdvise(observation.getAdvise() + "\tDer Name der Funktion bzw. Methode, \"" + identifier + "\", startet nicht mit einem Kleinbuchstaben. Für einen Bezeichner von einer Methode bzw. einer Funktion sollte ein Verb oder ein Ausdruck mit einem Verb verwendet werden, die mit einem Kleinbuchstaben beginnen. Accessoren, mutatoren und Prädikate sollten nach ihrem Wert benannt werden und einen Präfix wie \"get\", \"set\" oder \"is\" beinhalten.\n"); 
		}
		observation.setCleanCodeObservationScore(observation.getCleanCodeObservationScore() + cleanCodeScore);
		observation.setHighestPossibleScoreForThisObservation(observation.getHighestPossibleScoreForThisObservation() + 1);
	}
	
	private static String getStringOfMethodNamingConvention(MethodNamingConvention methodNamingConvention) {
		if (methodNamingConvention == MethodNamingConvention.CAMEL_CASE) {
			return "Camel-Case";
		}
		else if (methodNamingConvention == MethodNamingConvention.PASCAL_CASE) {
			return "Pascal-Case";
		}
		else if (methodNamingConvention == MethodNamingConvention.SNAKE_CASE) {
			return "Snake-Case";
		}
		else if (methodNamingConvention == MethodNamingConvention.TITLE_CASE) {
			return "Title-Case";
		}
		return null;
	}
	
	private static boolean identifierIsCamelCase(String identifier) {
		boolean isCamelCase = true;
		boolean usesLowerCase = false;
		boolean usesUpperCase = false;
		for(int i = 0; i < identifier.length() - 1; i++) {
			// Wir starten mit einem Kleinbuchstaben.
			if (i == 0 && !(Character.isLowerCase(identifier.charAt(i)))) {
				isCamelCase = false;
			}
			// Wir wollen nur Buchstaben bei Camel-Case.
			if (!(Character.isLetter(identifier.charAt(i)))) {
				isCamelCase = false;
			}
			if (Character.isLowerCase(identifier.charAt(i))) {
				usesLowerCase = true;
			}
			if (Character.isUpperCase(identifier.charAt(i))) {
				usesUpperCase = true;
			}
			// Wir wollen keine zwei Großbuchstaben nebeneinander beim Camel-Case.
			if (Character.isUpperCase(identifier.charAt(i)) && Character.isUpperCase(identifier.charAt(i + 1))) {
				isCamelCase = false;
			}
		}
		if (isCamelCase && (usesLowerCase || usesUpperCase)) {
			if (Character.isLowerCase(identifier.charAt(identifier.length() - 1))) {
				usesLowerCase = true;
			}
			// Großbuchstabe sollte nicht am Ende stehen.
			if (Character.isUpperCase(identifier.charAt(identifier.length() - 1))) {
				isCamelCase = false;
			}
		}
		if (isCamelCase && usesLowerCase && usesUpperCase) {
			return true;
		}
		return false;
	}
	
	private static boolean identifierIsSnakeCase(String identifier) {
		boolean isSnakeCase = true;
		boolean usesLowerCase = false;
		boolean usesUnderscore = false;
		for(int i = 0; i < identifier.length() - 1; i++) {
			if (i == 0 && !(Character.isLowerCase(identifier.charAt(i)))) {
				isSnakeCase = false;
			}
			if (!(Character.isLowerCase(identifier.charAt(i)) || identifier.charAt(i) == '_')) {
				isSnakeCase = false;
			}
			if (Character.isLowerCase(identifier.charAt(i))) {
				usesLowerCase = true;
			}
			else if (identifier.charAt(i) == '_') {
				usesUnderscore = true;
			}
			// Wir wollen keine zwei Unterstriche nebeneinander beim Snake-Case.
			if (identifier.charAt(i) == '_' && identifier.charAt(i + 1) == '_') {
				isSnakeCase = false;
			}
		}
		if (isSnakeCase && (usesLowerCase || usesUnderscore)) {
			if (Character.isLowerCase(identifier.charAt(identifier.length() - 1))) {
				usesLowerCase = true;
			}
			// Unterstrich sollte nicht am Ende stehen.
			else if (identifier.charAt(identifier.length() - 1) == '_') {
				isSnakeCase = false;
			}
		}
		if (isSnakeCase && usesLowerCase && usesUnderscore) {
			return true;
		}
		return false;
	}
	
	private static boolean identifierIsPascalCase(String identifier) {
		boolean isPascalCase = true;
		boolean usesLowerCase = false;
		boolean usesUpperCase = false;
		for(int i = 0; i < identifier.length() - 1; i++) {
			// Wir starten mit einem Großbuchstaben.
			if (i == 0 && !(Character.isUpperCase(identifier.charAt(i)))) {
				isPascalCase = false;
			}
			// Wir wollen nur Buchstaben bei Pascal-Case.
			if (!(Character.isLetter(identifier.charAt(i)))) {
				isPascalCase = false;
			}
			if (Character.isLowerCase(identifier.charAt(i))) {
				usesLowerCase = true;
			}
			if (Character.isUpperCase(identifier.charAt(i))) {
				usesUpperCase = true;
			}
			// Wir wollen keine zwei Großbuchstaben nebeneinander beim Pascal-Case.
			if (Character.isUpperCase(identifier.charAt(i)) && Character.isUpperCase(identifier.charAt(i + 1))) {
				isPascalCase = false;
			}
		}
		if (isPascalCase && (usesLowerCase || usesUpperCase)) {
			if (Character.isLowerCase(identifier.charAt(identifier.length() - 1))) {
				usesLowerCase = true;
			}
			// Großbuchstabe sollte nicht am Ende stehen.
			if (Character.isUpperCase(identifier.charAt(identifier.length() - 1))) {
				isPascalCase = false;
			}
		}
		if (isPascalCase && usesLowerCase && usesUpperCase) {
			return true;
		}
		return false;
	}
	
	private static boolean identifierIsTitleCase(String identifier) {
		boolean isTitleCase = true;
		boolean usesUpperCase = false;
		boolean usesUnderscore = false;
		for(int i = 0; i < identifier.length() - 1; i++) {
			if (i == 0 && !(Character.isUpperCase(identifier.charAt(i)))) {
				isTitleCase = false;
			}
			if (!(Character.isUpperCase(identifier.charAt(i)) || identifier.charAt(i) == '_')) {
				isTitleCase = false;
			}
			if (Character.isUpperCase(identifier.charAt(i))) {
				usesUpperCase = true;
			}
			else if (identifier.charAt(i) == '_') {
				usesUnderscore = true;
			}
			// Wir wollen keine zwei Unterstriche nebeneinander beim Title-Case.
			if (identifier.charAt(i) == '_' && identifier.charAt(i + 1) == '_') {
				isTitleCase = false;
			}
		}
		if (isTitleCase && (usesUpperCase || usesUnderscore)) {
			if (Character.isLowerCase(identifier.charAt(identifier.length() - 1))) {
				usesUpperCase = true;
			}
			// Unterstrich sollte nicht am Ende stehen.
			else if (identifier.charAt(identifier.length() - 1) == '_') {
				isTitleCase = false;
			}
		}
		if (isTitleCase && usesUpperCase && usesUnderscore) {
			return true;
		}
		return false;
	}
	
	private static MethodNamingConvention getNamingConventionFromCurrentMethodIdentifier(String identifier) {
		if (identifier.length() < 2) {
			return null;
		}
		if (CleanCodeAnalyzer.identifierIsCamelCase(identifier)) {
			return MethodNamingConvention.CAMEL_CASE;
		}
		else if (CleanCodeAnalyzer.identifierIsPascalCase(identifier)) {
			return MethodNamingConvention.PASCAL_CASE;
		}
		else if (CleanCodeAnalyzer.identifierIsSnakeCase(identifier)) {
			return MethodNamingConvention.SNAKE_CASE;
		}
		else if (CleanCodeAnalyzer.identifierIsTitleCase(identifier)) {
			return MethodNamingConvention.TITLE_CASE;
		}
		return null;
	}
	
	private static void analyzeMethodNameForUsingConsistentMethodNameConvention(CleanCodeObservation<Object> observation, String identifier) {
		int cleanCodeScore = 0;
		MethodNamingConvention methodNamingConvention = CleanCodeAnalyzer.getNamingConventionFromCurrentMethodIdentifier(identifier);
		// Beim ersten Eintrag (oder Falls kein Namensschema entdeckt wurde) vergeben wir noch keinen einen Punkt.
		if (CleanCodeAnalyzer.methodNamingConvention == null) {
			CleanCodeAnalyzer.methodNamingConvention = methodNamingConvention;
			observation.setHighestPossibleScoreForThisObservation(observation.getHighestPossibleScoreForThisObservation() - 1);
		}
		else {
			if (methodNamingConvention != CleanCodeAnalyzer.methodNamingConvention && methodNamingConvention != null) {
				observation.setAdvise(observation.getAdvise() + "\tBisher haben Sie für Funktions- oder Methodennamen das " + CleanCodeAnalyzer.getStringOfMethodNamingConvention(CleanCodeAnalyzer.methodNamingConvention) + " genutzt, es wurde jedoch " + CleanCodeAnalyzer.getStringOfMethodNamingConvention(methodNamingConvention) + " gefunden.\n");
			}
			else if (methodNamingConvention == null) {
				observation.setAdvise(observation.getAdvise() + "\tBisher haben Sie für Funktions- oder Methodennamen das " + CleanCodeAnalyzer.getStringOfMethodNamingConvention(CleanCodeAnalyzer.methodNamingConvention) + " genutzt, es wurde jedoch etwas anderes gefunden.\n");
			}
			else {
				cleanCodeScore += 1;
			}
		}
		observation.setCleanCodeObservationScore(observation.getCleanCodeObservationScore() + cleanCodeScore);
		observation.setHighestPossibleScoreForThisObservation(observation.getHighestPossibleScoreForThisObservation() + 1);
	}
	
	private static void analyzeIdentifierForSearchability(CleanCodeObservation<Object> observation, String identifier, boolean insideFunction) {
		int cleanCodeScore = 0;
		// Enthält der Name keine Zahlen, vergeben wir einen Punkt.
		if (!(CleanCodeAnalyzer.identifierContainsDigit(identifier))) {
			cleanCodeScore += 1;
		}
		else if (observation.getBroadCategory() == CleanCodeAnalysisBroadCategory.CLASS_ANALYSIS) {
			observation.setAdvise(observation.getAdvise() + "\tDer Name der Struktur oder Klasse, \"" + identifier + "\", enthält Ziffern, die die Suchbarkeit im Code verringern.\n"); 
		}
		else if (observation.getBroadCategory() == CleanCodeAnalysisBroadCategory.METHOD_ANALYSIS) {
			observation.setAdvise(observation.getAdvise() + "\tDer Name der Funktion oder Methode, \"" + identifier + "\", enthält Ziffern, die die Suchbarkeit im Code verringern.\n"); 
		}
		else if (observation.getBroadCategory() == CleanCodeAnalysisBroadCategory.VARIABLE_ANALYSIS) {
			observation.setAdvise(observation.getAdvise() + "\tDer Name der Variable, \"" + identifier + "\", enthält Ziffern, die die Suchbarkeit im Code verringern.\n"); 
		}
		// Enthält der Name mehr als 1 Zeichen, vergeben wir einen Punkt (längere Namen sind prinzipiell leichter suchbar). In lokalen Methoden sind Variablen mit einem Zeichen in Ordnung.
		if ((identifier.length() > 1 || insideFunction)) {
			cleanCodeScore += 1;
		}
		else if (observation.getBroadCategory() == CleanCodeAnalysisBroadCategory.CLASS_ANALYSIS) {
			observation.setAdvise(observation.getAdvise() + "\tDer Name der Struktur oder Klasse, \"" + identifier + "\", enthält nur ein Zeichen. Längere Bezeichner sind einfacher suchbar im Code.\n"); 
		}
		else if (observation.getBroadCategory() == CleanCodeAnalysisBroadCategory.METHOD_ANALYSIS) {
			observation.setAdvise(observation.getAdvise() + "\tDer Name der Funktion oder Methode, \"" + identifier + "\", enthält nur ein Zeichen. Längere Bezeichner sind einfacher suchbar im Code.\n"); 
		}
		else if (observation.getBroadCategory() == CleanCodeAnalysisBroadCategory.VARIABLE_ANALYSIS) {
			observation.setAdvise(observation.getAdvise() + "\tDer Name der Variable, \"" + identifier + "\", enthält nur ein Zeichen. Längere Bezeichner sind einfacher suchbar im Code.\n"); 
		}
		// Enthält der Name nur Großbuchstaben und '_', so ist er am einfachsten suchbar (nur für Variablennamen sinnvoll).
		if (CleanCodeAnalyzer.identifierConsistsOfUpperCaseAndUnderscores(identifier) && observation.getBroadCategory() == CleanCodeAnalysisBroadCategory.VARIABLE_ANALYSIS) {
			cleanCodeScore += 1;
		}
		else if (observation.getBroadCategory() == CleanCodeAnalysisBroadCategory.VARIABLE_ANALYSIS) {
			observation.setAdvise(observation.getAdvise() + "\tDer Name der Variable, \"" + identifier + "\", enthält nicht nur Großbuchstaben und '_' zur Trennung von Wörtern. Dann wäre sie am einfachsten suchbar im Code.\n"); 
		}
		if (observation.getBroadCategory() == CleanCodeAnalysisBroadCategory.VARIABLE_ANALYSIS) {
			observation.setCleanCodeObservationScore(observation.getCleanCodeObservationScore() + cleanCodeScore);
			observation.setHighestPossibleScoreForThisObservation(observation.getHighestPossibleScoreForThisObservation() + 3);
		}
		else {
			observation.setCleanCodeObservationScore(observation.getCleanCodeObservationScore() + cleanCodeScore);
			observation.setHighestPossibleScoreForThisObservation(observation.getHighestPossibleScoreForThisObservation() + 2);
		}
	}
	
	private static void analyzeIdentifierOfForLoopInitializer(CleanCodeObservation<Object> observation, String identifier) {
		int cleanCodeScore = 0;
		// Enthält der Name keine Zahlen, vergeben wir einen Punkt.
		if (!(CleanCodeAnalyzer.identifierContainsDigit(identifier))) {
			cleanCodeScore += 1;
		}
		// Bezeichner in For-Schleifen haben sich etabliert, dürfen aber nicht 'l' oder 'I' heißen.
		if (identifier.length() == 1 && !(SymbolTable.S_EQ(identifier, "l") && !(SymbolTable.S_EQ(identifier, "I")))) {
			cleanCodeScore += 1;
		}
		else if (identifier.length() > 1 && !(SymbolTable.S_EQ(identifier, "l") && !(SymbolTable.S_EQ(identifier, "I")))) {
			observation.setAdvise(observation.getAdvise() + "\tDer Name der Variable \"" + identifier + "\" ist ein Initialisierer einer For-Schleife. Hier haben sich Variablenbezeichner mit lediglich einem Zeichen Länge etabliert.\n"); 
		}
		else if (identifier.length() == 1 && SymbolTable.S_EQ(identifier, "l")) {
			observation.setAdvise(observation.getAdvise() + "\tDer Name der Variable \"" + identifier + "\" ist ein Initialisierer einer For-Schleife. Da der Bezeichner 'l' schwierig vom Bezeichner 'I' zu unterscheiden ist, ist es kein guter Bezeichner.\n"); 
		}
		else if (identifier.length() == 1 && SymbolTable.S_EQ(identifier, "I")) {
			observation.setAdvise(observation.getAdvise() + "\tDer Name der Variable \"" + identifier + "\" ist ein Initialisierer einer For-Schleife. Da das der Bezeichner 'I' schwierig vom Bezeichner 'l' zu unterscheiden ist, ist es kein guter Bezeichner.\n"); 
		}
		observation.setCleanCodeObservationScore(observation.getCleanCodeObservationScore() + cleanCodeScore);
		observation.setHighestPossibleScoreForThisObservation(observation.getHighestPossibleScoreForThisObservation() + 2);
	}
	
	private static void analyzeIdentifierForStartingWithMemberPrefix(CleanCodeObservation<Object> observation, String identifier) {
		if (identifier.length() == 1) {
			return;
		}
		int cleanCodeScore = 0;
		// Startet der Name mit einem Unterstrich, so ist das ein Zeichen für den Beginn mit einem Member-Präfix.
		if (identifier.charAt(0) != 'm' || identifier.charAt(1) != '_') {
			cleanCodeScore += 1;
		}
		else if (identifier.length() > 1 && observation.getBroadCategory() == CleanCodeAnalysisBroadCategory.CLASS_ANALYSIS) {
			observation.setAdvise(observation.getAdvise() + "\tDer Name der Struktur- oder Klasse \"" + identifier + "\" startet mit einem 'm_'. Member-Präfixe sollten nach Möglichkeit nicht mehr verwendet werden.\n"); 
		}
		else if (identifier.length() > 1 && observation.getBroadCategory() == CleanCodeAnalysisBroadCategory.METHOD_ANALYSIS) {
			observation.setAdvise(observation.getAdvise() + "\tDer Name der Funktion oder Methode \"" + identifier + "\" startet mit einem 'm_'. Member-Präfixe sollten nach Möglichkeit nicht mehr verwendet werden.\n"); 
		}
		else if (identifier.length() > 1 && observation.getBroadCategory() == CleanCodeAnalysisBroadCategory.VARIABLE_ANALYSIS) {
			observation.setAdvise(observation.getAdvise() + "\tDer Name der Variable \"" + identifier + "\" startet mit einem 'm_'. Member-Präfixe sollten nach Möglichkeit nicht mehr verwendet werden.\n"); 
		}
		observation.setCleanCodeObservationScore(observation.getCleanCodeObservationScore() + cleanCodeScore);
		observation.setHighestPossibleScoreForThisObservation(observation.getHighestPossibleScoreForThisObservation() + 1);
	}
	
	private static int analyzeIdentifierForRedundantContext(CleanCodeObservation<Object> observation, String identifier, String parentIdentifier, boolean analyzeForRedundantClassContext) {
		String prefix = "";
		int cleanCodeScore = 0;
		for(int i = 0; i < parentIdentifier.length(); i++) {
			if (Character.isUpperCase(parentIdentifier.charAt(i))) {
				prefix += parentIdentifier.charAt(i);
			}
		}
		// Falls der Präfix keine 2 Buchstaben enthält, oder der Präfix länger ist als der Strukturname, gehen wir davon aus, dass es kein Präfix ist.
		if (prefix.length() <= 1 || prefix.length() > identifier.length()) {
			cleanCodeScore += 1;
		}
		else {
			// Enthält der Strukturname nicht den Präfix, so vergeben wir einen Punkt.
			boolean prefixIsPartOfIdentifier = true;
			for(int i = 0; i < prefix.length(); i++) {
				if (identifier.charAt(i) != prefix.charAt(i)) {
					prefixIsPartOfIdentifier = false;
				}
			}
			if (!(prefixIsPartOfIdentifier)) {
				cleanCodeScore += 1;
			}
			else if (observation.getBroadCategory() == CleanCodeAnalysisBroadCategory.CLASS_ANALYSIS) {
				observation.setAdvise(observation.getAdvise() + "\tDer Name der Struktur- oder Klasse, \"" + identifier + "\", enthält den Präfix \"" + prefix + "\" der Union, Struktor oder Klasse \"" + parentIdentifier + "\".\n");
			}
			else if (observation.getBroadCategory() == CleanCodeAnalysisBroadCategory.METHOD_ANALYSIS) {
				observation.setAdvise(observation.getAdvise() + "\tDer Name der Funktion oder Methode, \"" + identifier + "\" enthält den Präfix \"" + prefix + "\" der Union, Struktor oder Klasse \"" + parentIdentifier + "\".\n");
			}
			else if (observation.getBroadCategory() == CleanCodeAnalysisBroadCategory.VARIABLE_ANALYSIS) {
				if (analyzeForRedundantClassContext) {
					observation.setAdvise(observation.getAdvise() + "\tDer Name der Variable, \"" + identifier + "\", enthält den Präfix \"" + prefix + "\" der Union, Struktor oder Klasse \"" + parentIdentifier + "\".\n");
				}
				else {
					observation.setAdvise(observation.getAdvise() + "\tDer Name der Variable, \"" + identifier + "\", enthält den Präfix \"" + prefix + "\" der Funktion oder Methode \"" + parentIdentifier + "\".\n");
				}
			}
		}
		return cleanCodeScore;
	}
	
	private static void analyzeIdentifierForRedundantClassContext(CleanCodeObservation<Object> observation, String identifier, Datatype<Node<Object>> parentSUDatatype) {
		// Es soll kein Präfix hinzugefügt werden, der beispielsweise eine Abkürzung der Klasse enthält, in der sich eine verschachtelte Klasse befindet.
		int cleanCodeScore = 0;
		if (parentSUDatatype == null) {
			cleanCodeScore += 1;
		}
		else {
			String parentIdentifier = parentSUDatatype.getTypeStr();
			boolean analyzeForRedundantClassContext = true;
			cleanCodeScore = CleanCodeAnalyzer.analyzeIdentifierForRedundantContext(observation, identifier, parentIdentifier, analyzeForRedundantClassContext);
		}
		observation.setCleanCodeObservationScore(observation.getCleanCodeObservationScore() + cleanCodeScore);
		observation.setHighestPossibleScoreForThisObservation(observation.getHighestPossibleScoreForThisObservation() + 1);
	}
	
	private static void analyzeIdentifierForRedundantMethodContext(CleanCodeObservation<Object> observation, String identifier, Node<Object> parentFuncNode) {
		int cleanCodeScore = 0;
		if (parentFuncNode == null) {
			cleanCodeScore += 1;
		}
		else {
			@SuppressWarnings("unchecked")
			Function<Object> parentFunc = (Function<Object>) parentFuncNode.getValue();
			String parentIdentifier = parentFunc.getName();
			boolean analyzeForRedundantClassContext = false;
			cleanCodeScore = CleanCodeAnalyzer.analyzeIdentifierForRedundantContext(observation, identifier, parentIdentifier, analyzeForRedundantClassContext);
		}
		observation.setCleanCodeObservationScore(observation.getCleanCodeObservationScore() + cleanCodeScore);
		observation.setHighestPossibleScoreForThisObservation(observation.getHighestPossibleScoreForThisObservation() + 1);
	}
	
	private static void analyzeStructVariablePlacement(CompileProcess compileProcess, CleanCodeObservation<Object> observation, Body<Object> body, String structIdentifier) {
		int cleanCodeScore = 0;
		boolean foundAtLeastOneVar = false;
		boolean foundSomethingElseThanVar = false;
		boolean allVarsAreAtTheTopOfTheClass = true;
		boolean allVarsAreBelowEachOtherWithoutAFreeLine = true;
		Vector<Node<Object>> bodyVec = body.getStatements();
		for(int i = 0; i < bodyVec.vectorCount(); i++) {
			Node<Object> node = bodyVec.vectorPeekPtrAt(i);
			if (node.getType() != NodeType.NODE_TYPE_VARIABLE && node.getType() != NodeType.NODE_TYPE_VARIABLE_LIST) {
				foundSomethingElseThanVar = true;
			}
			else if (node.getType() == NodeType.NODE_TYPE_VARIABLE || node.getType() == NodeType.NODE_TYPE_VARIABLE_LIST) {
				foundAtLeastOneVar = true;
				// Kommen wir in diesen Fall und haben schon etwas anderes als eine Variable gefunden, so haben wir weitere Variablen danach gefunden.
				if (foundSomethingElseThanVar) {
					allVarsAreAtTheTopOfTheClass = false;
					allVarsAreBelowEachOtherWithoutAFreeLine = false;
					break;
				}
				if (allVarsAreAtTheTopOfTheClass && allVarsAreBelowEachOtherWithoutAFreeLine && node.getType() == NodeType.NODE_TYPE_VARIABLE) {
					@SuppressWarnings("unchecked")
					Var<Object> var = (Var<Object>) node.getValue();
					Token<Object> possibleAccessSpecifierToken = compileProcess.getTokenVec().vectorPeekPtrAt(var.getType().getStartTokenVecIndex() - 1);
					Token<Object> lastTokenBeforeCurrentVar = compileProcess.getTokenVec().vectorPeekPtrAt(var.getType().getStartTokenVecIndex() - 2);
					// Sonderfälle: Access Specifier, entweder als "public:" oder als "public".
					if (possibleAccessSpecifierToken.getType() == TokenType.TOKEN_TYPE_KEYWORD && SymbolTable.keywordIsAccessSpecifier((String) possibleAccessSpecifierToken.getValue())) {
						lastTokenBeforeCurrentVar = compileProcess.getTokenVec().vectorPeekPtrAt(var.getType().getStartTokenVecIndex() - 3);
					}
					else if (Token.tokenIsSymbol(lastTokenBeforeCurrentVar, ':')) {
						lastTokenBeforeCurrentVar = compileProcess.getTokenVec().vectorPeekPtrAt(var.getType().getStartTokenVecIndex() - 5);
					}
					// Entweder befinden wir uns am Anfang, dann sollte das letzte Token ein '{' sein (Strukturkörperbeginn), oder nicht, dann sollte es ein ';' sein (Ende der Variablendefinition).
					if (!((i == 0 && Token.tokenIsSymbol(lastTokenBeforeCurrentVar, '{') || (i != 0 && Token.tokenIsSymbol(lastTokenBeforeCurrentVar, ';'))))) {
						allVarsAreBelowEachOtherWithoutAFreeLine = false;
					}
				}
				else if (allVarsAreAtTheTopOfTheClass && allVarsAreBelowEachOtherWithoutAFreeLine && node.getType() == NodeType.NODE_TYPE_VARIABLE_LIST) {
					@SuppressWarnings("unchecked")
					VarList<Object> varList = (VarList<Object>) node.getValue();
					@SuppressWarnings("unchecked")
					Var<Object> var = (Var<Object>) varList.getList().vectorPeekPtrAt(0).getValue();
					Token<Object> possibleAccessSpecifierToken = compileProcess.getTokenVec().vectorPeekPtrAt(var.getType().getStartTokenVecIndex() - 1);
					Token<Object> lastTokenBeforeCurrentVar = compileProcess.getTokenVec().vectorPeekPtrAt(var.getType().getStartTokenVecIndex() - 2);
					if (possibleAccessSpecifierToken.getType() == TokenType.TOKEN_TYPE_KEYWORD && SymbolTable.keywordIsAccessSpecifier((String) possibleAccessSpecifierToken.getValue())) {
						lastTokenBeforeCurrentVar = compileProcess.getTokenVec().vectorPeekPtrAt(var.getType().getStartTokenVecIndex() - 3);
					}
					else if (Token.tokenIsSymbol(lastTokenBeforeCurrentVar, ':')) {
						lastTokenBeforeCurrentVar = compileProcess.getTokenVec().vectorPeekPtrAt(var.getType().getStartTokenVecIndex() - 5);
					}
					if (!((i == 0 && Token.tokenIsSymbol(lastTokenBeforeCurrentVar, '{') || (i != 0 && Token.tokenIsSymbol(lastTokenBeforeCurrentVar, ';'))))) {
						allVarsAreBelowEachOtherWithoutAFreeLine = false;
					}
				}
			}
		}
		// Es gibt 1 Punkt dafür, wenn alle Variablen ganz am Anfang der Struktur oder Klasse deklariert werden.
		if (allVarsAreAtTheTopOfTheClass && foundAtLeastOneVar) {
			cleanCodeScore += 1;
		}
		else if (foundAtLeastOneVar) {
			observation.setAdvise(observation.getAdvise() + "\tDie Struktur oder Klasse \"" + structIdentifier + "\" besitzt Variablen, die nicht am Anfang der Klasse deklariert wurden. Sauberer wäre es, alle Struktur- oder Klassenvariablen am Beginn der Klasse zu deklarieren.");
		}
		// Es gibt 1 Punkt dafür, wenn alle Variablen am Anfang der Struktur oder Klasse deklariert werden und zusätzlich keine leere Zeile und kein Kommentar zwischen ihnen steht.
		if (allVarsAreAtTheTopOfTheClass && allVarsAreBelowEachOtherWithoutAFreeLine && foundAtLeastOneVar) {
			cleanCodeScore += 1;
		}
		else if (foundAtLeastOneVar) {
			observation.setAdvise(observation.getAdvise() + "\tDie Struktur oder Klasse \"" + structIdentifier + "\" besitzt Variablen, bei denen in der Zeile vor der jeweiligen Variablendeklaration etwas anderes als eine weitere Variablendeklaration gefunden wurde. Hier sollte keine neue Zeile und auch kein Kommentar zwischen den Variablendeklarationen verwendet werden.\n");
		}
		observation.setCleanCodeObservationScore(observation.getCleanCodeObservationScore() + cleanCodeScore);
		if (foundAtLeastOneVar) {
			observation.setHighestPossibleScoreForThisObservation(observation.getHighestPossibleScoreForThisObservation() + 2);
		}
		else {
			observation.setHighestPossibleScoreForThisObservation(observation.getHighestPossibleScoreForThisObservation() + 0);
		}
	}
	
	public static void analyzeStructMethodPlacement(CompileProcess compileProcess, CleanCodeObservation<Object> observation, Body<Object> body, String structIdentifier, int numberOfNestedStructs, boolean forwardDeclarationsAreAnalyzed) {
		int cleanCodeScore = 0;
		boolean allMethodsHaveOneIndentation = true;
		int numberOfExpectedWhitespacesAtTheBeginningOfTheLine = CleanCodeAnalyzer.numberOfWhitespacesPerIndentation * numberOfNestedStructs + CleanCodeAnalyzer.numberOfWhitespacesPerIndentation;
		boolean foundFirstMethod = false;
		boolean isFirstMethod = false;
		boolean foundSomethingElseThanMethod = false;
		boolean allMethodsAreBelowEachOther = true;
		boolean allDefinedMethodsAreBelowEachOtherWithOneFreeLineInBetween = true;
		boolean allForwardDeclarationsAreBelowEachOtherWithoutAFreeLineInBetween = true;
		Vector<Node<Object>> bodyVec = body.getStatements();
		for(int i = 0; i < bodyVec.vectorCount(); i++) {
			Node<Object> node = bodyVec.vectorPeekPtrAt(i);
			// Iterieren wir durch die Startvariablen, interessiert uns die Iteration nicht.
			if (node.getType() != NodeType.NODE_TYPE_FUNCTION && !(foundFirstMethod)) {
				continue;
			}
			else if (node.getType() != NodeType.NODE_TYPE_FUNCTION && foundFirstMethod) {
				foundSomethingElseThanMethod = true;
			}
			else if (node.getType() == NodeType.NODE_TYPE_FUNCTION) {
				@SuppressWarnings("unchecked")
				Function<Object> func = (Function<Object>) node.getValue();
				// Analysieren wir gerade nicht die richtigen Funktionen, schauen wir weiter (Unterschied Forward-Declaration zu definierter Methode).
				if (func.getHasForwardDeclaration() != forwardDeclarationsAreAnalyzed && !(foundFirstMethod)) {
					continue;
				}
				else if (func.getHasForwardDeclaration() != forwardDeclarationsAreAnalyzed && foundFirstMethod) {
					foundSomethingElseThanMethod = true;
					continue;
				}
				// Kommen wir das erste Mal in diesen Fall, so haben wir die erste Methode gefunden.
				if (!(foundFirstMethod)) {
					foundFirstMethod = true;
					isFirstMethod = true;
				}
				// Finden wir erneut eine Funktion, nachdem wir schon etwas anderes gefunden haben (nachdem wir bereits eine Funktion entdeckt haben), können nicht alle Funktionen untereinander stehen.
				else if (foundSomethingElseThanMethod) {
					allMethodsAreBelowEachOther = false;
				}
				int currentFuncStartTokenVecIndex = func.getStartTokenVecIndex();
				Token<Object> lastTokenBeforeCurrentMethod = compileProcess.getTokenVec().vectorPeekPtrAt(currentFuncStartTokenVecIndex - 2);
				// Sonderfälle: Access Specifier, entweder als "public:" oder als "public".
				// 1. Fall: "public".
				currentFuncStartTokenVecIndex = CleanCodeAnalyzer.checkTokenVecIndexForAccessSpecifierNamespaceAndGenerics(compileProcess, currentFuncStartTokenVecIndex);
				// 2. Fall: "public:".
				if (allMethodsAreBelowEachOther && ((allDefinedMethodsAreBelowEachOtherWithOneFreeLineInBetween && !(forwardDeclarationsAreAnalyzed)) || (allForwardDeclarationsAreBelowEachOtherWithoutAFreeLineInBetween && forwardDeclarationsAreAnalyzed))) {
					if (Token.tokenIsSymbol(lastTokenBeforeCurrentMethod, ':')) {
						currentFuncStartTokenVecIndex -= 3;
					}
					Token<Object> lastLastTokenBeforeCurrentMethod = compileProcess.getTokenVec().vectorPeekPtrAt(currentFuncStartTokenVecIndex - 3);
					lastTokenBeforeCurrentMethod = compileProcess.getTokenVec().vectorPeekPtrAt(currentFuncStartTokenVecIndex - 2);
					// Entweder befinden wir uns am Anfang, dann sollte das letzte Token ein '{' sein (Strukturkörperbeginn), oder nicht, dann sollte es ein ';' sein (am Beginn) oder ein '}' (Ende der vorigen Methode).
					if (forwardDeclarationsAreAnalyzed && !((i == 0 && Token.tokenIsSymbol(lastLastTokenBeforeCurrentMethod, '{') && lastTokenBeforeCurrentMethod.getType() == TokenType.TOKEN_TYPE_NEWLINE) || (i != 0 && isFirstMethod && Token.tokenIsSymbol(lastLastTokenBeforeCurrentMethod, ';') && lastTokenBeforeCurrentMethod.getType() == TokenType.TOKEN_TYPE_NEWLINE) || (i != 0 && !(isFirstMethod) && Token.tokenIsSymbol(lastLastTokenBeforeCurrentMethod, '}') && lastTokenBeforeCurrentMethod.getType() == TokenType.TOKEN_TYPE_NEWLINE))) {
						allDefinedMethodsAreBelowEachOtherWithOneFreeLineInBetween = false;
					}
					else if (!(forwardDeclarationsAreAnalyzed) && !((i == 0 && Token.tokenIsSymbol(lastTokenBeforeCurrentMethod, '{')) || (i != 0 && isFirstMethod && lastTokenBeforeCurrentMethod.getType() == TokenType.TOKEN_TYPE_NEWLINE)) || (i != 0 && !(isFirstMethod) && Token.tokenIsSymbol(lastTokenBeforeCurrentMethod, ';'))) {
						allForwardDeclarationsAreBelowEachOtherWithoutAFreeLineInBetween = false;
					}
				}
				if (allMethodsHaveOneIndentation) {
					Token<Object> newLineTokenBeforeFuncStart = compileProcess.getTokenVec().vectorPeekPtrAt(currentFuncStartTokenVecIndex - 1);
					int numberOfActualWhitespacesAtTheBeginningOfTheLine = newLineTokenBeforeFuncStart.getWhitespaces();
					if (numberOfActualWhitespacesAtTheBeginningOfTheLine != 0 && numberOfActualWhitespacesAtTheBeginningOfTheLine != numberOfExpectedWhitespacesAtTheBeginningOfTheLine) {
						allMethodsHaveOneIndentation = false;
					}
				}
				isFirstMethod = false;
			}
		}
		if (foundFirstMethod && allMethodsAreBelowEachOther) {
			cleanCodeScore += 1;
		}
		else if (foundFirstMethod) {
			observation.setAdvise(observation.getAdvise() + "\tDie Struktur oder Klasse \"" + structIdentifier + "\" besitzt Methoden, die nicht alle untereinander definiert sind. Sauberer wäre es, alle Methoden einer Struktur oder Klasse untereinander zu definieren.");
		}
		if (foundFirstMethod && allMethodsAreBelowEachOther && allDefinedMethodsAreBelowEachOtherWithOneFreeLineInBetween && !(forwardDeclarationsAreAnalyzed)) {
			cleanCodeScore += 1;
		}
		else if (foundFirstMethod && !(forwardDeclarationsAreAnalyzed)) {
			observation.setAdvise(observation.getAdvise() + "\tDie Struktur oder Klasse \"" + structIdentifier + "\" besitzt Methoden, bei denen in der Zeile vor der jeweiligen Methodendefinition etwas anderes als eine leere Zeile gefunden wurde. Hier sollte leere Zeile stehen, um die Methoden sichtbar voneinander abzugrenzen und lesbarer zu machen.\n");
		}
		if (foundFirstMethod && allMethodsAreBelowEachOther && allForwardDeclarationsAreBelowEachOtherWithoutAFreeLineInBetween && forwardDeclarationsAreAnalyzed) {
			cleanCodeScore += 1;
		}
		else if (foundFirstMethod && !(forwardDeclarationsAreAnalyzed)) {
			observation.setAdvise(observation.getAdvise() + "\tDie Struktur oder Klasse \"" + structIdentifier + "\" besitzt Forward-Declaration-Methoden, bei denen in der Zeile vor der jeweiligen Methodendefinition etwas anderes als eine andere Forward Declaration (oder eine Leerzeile bei der ersten Deklaration) gefunden wurde.\n");
		}
		if (allMethodsHaveOneIndentation && foundFirstMethod) {
			cleanCodeScore += 1;
		}
		else if (foundFirstMethod) {
			observation.setAdvise(observation.getAdvise() + "\tDie Struktur oder Klasse \"" + structIdentifier + "\" besitzt Methoden, die mehr oder weniger als eine konsistente Einrückung besitzen. Sauberer wäre es, wenn jede Methode genau eine Einrückung innerhalb der Struktur oder Klasse besitzt.\n");
		}
		observation.setCleanCodeObservationScore(observation.getCleanCodeObservationScore() + cleanCodeScore);
		if (foundFirstMethod) {
			observation.setHighestPossibleScoreForThisObservation(observation.getHighestPossibleScoreForThisObservation() + 3);
		}
		else {
			observation.setHighestPossibleScoreForThisObservation(observation.getHighestPossibleScoreForThisObservation() + 0);
		}
	}
	
	public static void analyzeNestedStructPlacement(CompileProcess compileProcess, CleanCodeObservation<Object> observation, Body<Object> body, String structIdentifier) {
		int cleanCodeScore = 0;
		boolean foundFirstNestedStruct = false;
		boolean isFirstNestedStruct = false;
		boolean foundSomethingElseThanNestedStruct = false;
		boolean allNestedStructsAreBelowEachOther = true;
		boolean allNestedStructsAreBelowEachOtherWithOneFreeLineInBetween = true;
		Vector<Node<Object>> bodyVec = body.getStatements();
		for(int i = 0; i < bodyVec.vectorCount(); i++) {
			Node<Object> node = bodyVec.vectorPeekPtrAt(i);
			// Iterieren wir durch die Startvariablen, interessiert uns die Iteration nicht.
			if (node.getType() != NodeType.NODE_TYPE_STRUCT && !(foundFirstNestedStruct)) {
				continue;
			}
			else if (node.getType() != NodeType.NODE_TYPE_STRUCT && foundFirstNestedStruct) {
				foundSomethingElseThanNestedStruct = true;
			}
			else if (node.getType() == NodeType.NODE_TYPE_STRUCT) {
				// Kommen wir das erste Mal in diesen Fall, so haben wir die erste verschachtelte Struktur gefunden.
				if (!(foundFirstNestedStruct)) {
					foundFirstNestedStruct = true;
					isFirstNestedStruct = true;
				}
				// Finden wir erneut eine Funktion, nachdem wir schon etwas anderes gefunden haben (nachdem wir bereits eine Funktion entdeckt haben), können nicht alle Funktionen untereinander stehen.
				else if (foundSomethingElseThanNestedStruct) {
					allNestedStructsAreBelowEachOther = false;
				}
				if (allNestedStructsAreBelowEachOther && allNestedStructsAreBelowEachOtherWithOneFreeLineInBetween) {
					@SuppressWarnings("unchecked")
					Struct<Object> nestedStruct = (Struct<Object>) node.getValue();
					int currentNestedStructStartTokenVecIndex = nestedStruct.getDtype().getStartTokenVecIndex();
					Token<Object> possibleAccessSpecifierToken = compileProcess.getTokenVec().vectorPeekPtrAt(currentNestedStructStartTokenVecIndex - 1);
					Token<Object> lastTokenBeforeCurrentMethod = compileProcess.getTokenVec().vectorPeekPtrAt(currentNestedStructStartTokenVecIndex - 2);
					// Sonderfälle: Access Specifier, entweder als "public:" oder als "public".
					if (possibleAccessSpecifierToken.getType() == TokenType.TOKEN_TYPE_KEYWORD && SymbolTable.keywordIsAccessSpecifier((String) possibleAccessSpecifierToken.getValue())) {
						currentNestedStructStartTokenVecIndex -= 1;
					}
					else if (Token.tokenIsSymbol(lastTokenBeforeCurrentMethod, ':')) {
						currentNestedStructStartTokenVecIndex -= 3;
					}
					Token<Object> lastLastTokenBeforeCurrentNestedStruct = compileProcess.getTokenVec().vectorPeekPtrAt(currentNestedStructStartTokenVecIndex - 3);
					lastTokenBeforeCurrentMethod = compileProcess.getTokenVec().vectorPeekPtrAt(currentNestedStructStartTokenVecIndex - 2);
					// Entweder befinden wir uns am Anfang, dann sollte das letzte Token ein '{' sein (Strukturkörperbeginn), oder nicht, dann sollte es ein ';' sein (am Beginn) oder ein '}' (Ende der vorigen Methode).
					if (!((i == 0 && Token.tokenIsSymbol(lastLastTokenBeforeCurrentNestedStruct, '{') && lastTokenBeforeCurrentMethod.getType() == TokenType.TOKEN_TYPE_NEWLINE) || (i != 0 && Token.tokenIsSymbol(lastLastTokenBeforeCurrentNestedStruct, ';') && lastTokenBeforeCurrentMethod.getType() == TokenType.TOKEN_TYPE_NEWLINE) || (i != 0 && isFirstNestedStruct && Token.tokenIsSymbol(lastLastTokenBeforeCurrentNestedStruct, '}') && lastTokenBeforeCurrentMethod.getType() == TokenType.TOKEN_TYPE_NEWLINE))) {
						allNestedStructsAreBelowEachOtherWithOneFreeLineInBetween = false;
					}
				}
				isFirstNestedStruct = false;
			}
		}
		// Es gibt 1 Punkt dafür, wenn alle Variablen ganz am Anfang der Struktur oder Klasse deklariert werden.
		if (foundFirstNestedStruct && allNestedStructsAreBelowEachOther) {
			cleanCodeScore += 1;
		}
		else if (foundFirstNestedStruct) {
			observation.setAdvise("\tDie Struktur oder Klasse \"" + structIdentifier + "\" besitzt verschachtelte Strukturen oder Klassen, die nicht alle untereinander definiert sind. Sauberer wäre es, alle verschachtelten Strukturen oder Klassen einer Struktur oder Klasse untereinander zu definieren.");
		}
		// Es gibt 1 Punkt dafür, wenn alle Variablen am Anfang der Struktur oder Klasse deklariert werden und zusätzlich keine leere Zeile und kein Kommentar zwischen ihnen steht.
		if (foundFirstNestedStruct && allNestedStructsAreBelowEachOther && allNestedStructsAreBelowEachOtherWithOneFreeLineInBetween) {
			cleanCodeScore += 1;
		}
		else if (foundFirstNestedStruct) {
			observation.setAdvise("\tDie Struktur oder Klasse \"" + structIdentifier + "\" besitzt verschachtelte Strukturen oder Klassen, bei denen in der Zeile vor der jeweiligen Struktur- oder Klassendefinition etwas anderes als eine leere Zeile gefunden wurde. Hier sollte leere Zeile stehen, um die verschachtelten Strukturen oder Klassen sichtbar voneinander abzugrenzen und lesbarer zu machen.");
		}
		observation.setCleanCodeObservationScore(observation.getCleanCodeObservationScore() + cleanCodeScore);
		if (foundFirstNestedStruct) {
			observation.setHighestPossibleScoreForThisObservation(observation.getHighestPossibleScoreForThisObservation() + 2);
		}
		else if (foundFirstNestedStruct) {
			observation.setHighestPossibleScoreForThisObservation(observation.getHighestPossibleScoreForThisObservation() + 0);
		}
	}
	
	private static void analyzeStructVariableAccessSpecifier(CompileProcess compileProcess, CleanCodeObservation<Object> observation, Body<Object> body, String structIdentifier) {
		int cleanCodeScore = 0;
		boolean correctOrderOfAccessSpecifiers = true;
		boolean foundPrivateStaticVar = false;
		boolean foundPrivateVar = false;
		boolean foundAtLeastOneVar = false;
		boolean foundPublicOrProtectedVar = false;
		Vector<Node<Object>> bodyVec = body.getStatements();
		for(int i = 0; i < bodyVec.vectorCount(); i++) {
			Node<Object> node = bodyVec.vectorPeekPtrAt(i);
			if (node.getType() != NodeType.NODE_TYPE_VARIABLE && node.getType() != NodeType.NODE_TYPE_VARIABLE_LIST) {
				continue;
			}
			else if (node.getType() == NodeType.NODE_TYPE_VARIABLE) {
				if (!(foundAtLeastOneVar)) {
					foundAtLeastOneVar = true;
				}
				@SuppressWarnings("unchecked")
				Var<Object> var = (Var<Object>) node.getValue();
				if (var.getAccessSpecifier() == AccessSpecifierFlag.ACCESS_SPECIFIER_PUBLIC && var.getType().getFlags() == DatatypeFlag.DATATYPE_FLAG_IS_STATIC) {
					if (foundPrivateStaticVar || foundPrivateVar) {
						correctOrderOfAccessSpecifiers = false;
						break;
					}
				}
				else if (var.getAccessSpecifier() != AccessSpecifierFlag.ACCESS_SPECIFIER_PRIVATE && var.getType().getFlags() == DatatypeFlag.DATATYPE_FLAG_IS_STATIC) {
					foundPrivateStaticVar = true;
					if (foundPrivateVar) {
						correctOrderOfAccessSpecifiers = false;
						break;
					}
				}
				else if (var.getAccessSpecifier() == AccessSpecifierFlag.ACCESS_SPECIFIER_PRIVATE && var.getType().getFlags() != DatatypeFlag.DATATYPE_FLAG_IS_STATIC) {
					foundPrivateVar = true;
				}
				else {
					foundPublicOrProtectedVar = true;
				}
			}
			else if (node.getType() == NodeType.NODE_TYPE_VARIABLE_LIST) {
				if (!(foundAtLeastOneVar)) {
					foundAtLeastOneVar = true;
				}
				@SuppressWarnings("unchecked")
				VarList<Object> varList = (VarList<Object>) node.getValue();
				@SuppressWarnings("unchecked")
				Var<Object> var = (Var<Object>) varList.getList().vectorPeekPtrAt(0).getValue();
				if (var.getAccessSpecifier() != AccessSpecifierFlag.ACCESS_SPECIFIER_PRIVATE) {
					if (var.getAccessSpecifier() == AccessSpecifierFlag.ACCESS_SPECIFIER_PUBLIC && var.getType().getFlags() == DatatypeFlag.DATATYPE_FLAG_IS_STATIC) {
						if (foundPrivateStaticVar || foundPrivateVar) {
							correctOrderOfAccessSpecifiers = false;
							break;
						}
					}
					else if (var.getAccessSpecifier() != AccessSpecifierFlag.ACCESS_SPECIFIER_PRIVATE && var.getType().getFlags() == DatatypeFlag.DATATYPE_FLAG_IS_STATIC) {
						foundPrivateStaticVar = true;
						if (foundPrivateVar) {
							correctOrderOfAccessSpecifiers = false;
							break;
						}
					}
					else if (var.getAccessSpecifier() == AccessSpecifierFlag.ACCESS_SPECIFIER_PRIVATE && var.getType().getFlags() != DatatypeFlag.DATATYPE_FLAG_IS_STATIC) {
						foundPrivateVar = true;
					}
					else {
						foundPublicOrProtectedVar = true;
					}
				}
			}
		}
		if (!(foundPublicOrProtectedVar) && foundAtLeastOneVar) {
			cleanCodeScore += 1;
		}
		else if (!(foundAtLeastOneVar)) {
			
		}
		else {
			observation.setAdvise(observation.getAdvise() + "\tDie Struktur oder Klasse \"" + structIdentifier + "\" besitzt public oder protected Variablen, die nach Möglichkeit vermieden werden sollten. Protected Variablen dürfen in Ausnahmefällen verwendet werden.\n");
		}
		if (correctOrderOfAccessSpecifiers && foundAtLeastOneVar) {
			cleanCodeScore += 1;
		}
		else if (!(foundAtLeastOneVar)) {
			
		}
		else {
			observation.setAdvise(observation.getAdvise() + "\tDie Struktur oder Klasse \"" + structIdentifier + "\" besitzt keine saubere Reihenfolge der Deklaration von Struktur- bzw. Klassenvariablen. Sauberer wäre es zuerst die \"public static\"-Variablen, dann die \"private static\"-Variablen und dann die \"private\" Variablen zu deklarieren.\n");
		}
		observation.setCleanCodeObservationScore(observation.getCleanCodeObservationScore() + cleanCodeScore);
		if (foundAtLeastOneVar) {
			observation.setHighestPossibleScoreForThisObservation(observation.getHighestPossibleScoreForThisObservation() + 2);
		}
		else {
			observation.setHighestPossibleScoreForThisObservation(observation.getHighestPossibleScoreForThisObservation() + 0);
		}
		if (observation.getCleanCodeObservationScore() == observation.getHighestPossibleScoreForThisObservation()) {
			observation.setAdvise(null);
		}
	}
	
	private static void analyzeStructRegardingGettersAndSetters(CompileProcess compileProcess, CleanCodeObservation<Object> observation, Body<Object> body, String structIdentifier) {
		int cleanCodeScore = 0;
		// Zuerst speichern wir die (angepassten) Klassenvariablennamen und Methodennamen in 2 Vektoren.
		Vector<Node<Object>> bodyVec = body.getStatements();
		Vector<String> varNames = Vector.vectorCreate();
		Vector<String> methodNames = Vector.vectorCreate();
		boolean varWereFound = false;
		boolean methodWereFound = false;
		for(int i = 0; i < bodyVec.vectorCount(); i++) {
			Node<Object> node = bodyVec.vectorPeekPtrAt(i);
			if (node.getType() == NodeType.NODE_TYPE_VARIABLE) {
				if (!(varWereFound)) {
					varWereFound = true;
				}
				@SuppressWarnings("unchecked")
				Var<Object> var = (Var<Object>) node.getValue();
				String varNameWithCapitalBeginning = Character.toUpperCase(var.getName().charAt(0)) + var.getName().substring(1);
				varNames.vectorPush(varNameWithCapitalBeginning);
			}
			else if (node.getType() == NodeType.NODE_TYPE_VARIABLE_LIST) {
				if (!(varWereFound)) {
					varWereFound = true;
				}
				@SuppressWarnings("unchecked")
				VarList<Object> varList = (VarList<Object>) node.getValue();
				Vector<Node<Object>> varListVec = varList.getList();
				for(int j = 0; j < varListVec.vectorCount(); j++) {
					@SuppressWarnings("unchecked")
					Var<Object> var = (Var<Object>) varListVec.vectorPeekPtrAt(j).getValue();
					String varNameWithCapitalBeginning = Character.toUpperCase(var.getName().charAt(0)) + var.getName().substring(1);
					varNames.vectorPush(varNameWithCapitalBeginning);
				}
			}
			else if (node.getType() == NodeType.NODE_TYPE_FUNCTION) {
				if (!(methodWereFound)) {
					methodWereFound = true;
				}
				@SuppressWarnings("unchecked")
				Function<Object> func = (Function<Object>) node.getValue();
				String funcName = func.getName();
				methodNames.vectorPush(funcName);
			}
		}
		// Nun schauen wir, ob Getter und Setter mit den entsprechenden üblichen Namen vorhanden sind.
		boolean gettersWereFound = false;
		boolean settersWereFound = false;
		for(int i = 0; i < methodNames.vectorCount(); i++) {
			String methodName = methodNames.vectorPeekPtrAt(i);
			for(int j = 0; j < varNames.vectorCount(); j++) {
				String varNameWithCapitalBeginning = varNames.vectorPeekPtrAt(j);
				String possibleGetter = "get" + varNameWithCapitalBeginning;
				String possibleSetter = "set" + varNameWithCapitalBeginning;
				if (SymbolTable.S_EQ(methodName, possibleGetter)) {
					gettersWereFound = true;
				}
				else if (SymbolTable.S_EQ(methodName, possibleSetter)) {
					settersWereFound = true;
				}
			}
		}
		if (varWereFound && methodWereFound) {
			if (!(gettersWereFound)) {
				cleanCodeScore += 1;
			}
			else {
				observation.setAdvise(observation.getAdvise() + "\tDie Struktur oder Klasse \"" + structIdentifier + "\" besitzt offensichtliche Getter-Methoden. Abstrakte Interfaces, mit denen der Benutzer die Essenz der Daten manipulieren kann, ohne deren konkrete Implementierung kennen zu müssen, wären sauberer.");
			}
			if (!(settersWereFound)) {
				cleanCodeScore += 1;
			}
			else {
				observation.setAdvise(observation.getAdvise() + "\tDie Struktur oder Klasse \"" + structIdentifier + "\" besitzt offensichtliche Setter-Methoden. Abstrakte Interfaces, mit denen der Benutzer die Essenz der Daten manipulieren kann, ohne deren konkrete Implementierung kennen zu müssen, wären sauberer.");
			}
			if (cleanCodeScore == 2) {
				observation.setAdvise(null);
			}
			observation.setCleanCodeObservationScore(observation.getCleanCodeObservationScore() + cleanCodeScore);
			observation.setHighestPossibleScoreForThisObservation(observation.getHighestPossibleScoreForThisObservation() + 2);
		}
		else {
			observation.setCleanCodeObservationScore(observation.getCleanCodeObservationScore() + cleanCodeScore);
			observation.setHighestPossibleScoreForThisObservation(observation.getHighestPossibleScoreForThisObservation() + 0);
			observation.setAdvise(null);
		}
	}
	
	@SuppressWarnings("unchecked")
	private static void analyzeStructIndentationDepth(CompileProcess compileProcess, Token<Object> nameToken, Integer structEndTokenVecIndex, Node<Object> structNode, int numberOfNestedStructs) {
		IndentationAnalysis structIndentationAnalysis = new IndentationAnalysis();
		String identifier = (String) nameToken.getValue();
		CleanCodeObservation<Object> observation = new CleanCodeObservation<Object>(CleanCodeAnalysisBroadCategory.CLASS_ANALYSIS, CleanCodeAnalysisConcreteCategory.INDENTATION_ANALYSIS, nameToken.getPos(), structNode);
		compileProcess.getCleanCodeObservations().vectorPush(observation);
		int cleanCodeScore = 0;
		Vector<Token<Object>> tokenVec = compileProcess.getTokenVec();
		Struct<Object> struct = (Struct<Object>) structNode.getValue();
		int structStartTokenVecIndex = CleanCodeAnalyzer.checkTokenVecIndexForAccessSpecifierNamespaceAndGenerics(compileProcess, struct.getDtype().getStartTokenVecIndex() - 1);
		Token<Object> priorToken = tokenVec.vectorPeekPtrAt(structStartTokenVecIndex - 1);
		int numberOfActualWhitespacesPriorStructBeginning = 0;
		if (priorToken != null) {
			numberOfActualWhitespacesPriorStructBeginning = priorToken.getWhitespaces();
		}
		Token<Object> token = tokenVec.vectorPeekPtrAt(structStartTokenVecIndex);
		// Nun wird noch geschaut, ob die Einrückungstiefe der Struktor oder Klasse 4 Ebenen oder weniger entspricht.
		Token<Object> lastToken = null;
		int currentLine = token.getPos().getLineNumber();
		boolean newLine = false;
		for (int i = structStartTokenVecIndex + 1; i <= structEndTokenVecIndex; i++) {
			lastToken = token;
			int priorLine = lastToken.getPos().getLineNumber();
			token = tokenVec.vectorPeekPtrAt(i);
			currentLine = token.getPos().getLineNumber();
			if (newLine) {
				CleanCodeAnalyzer.analyzeIndentationByLookAtLastTokenInPriorLine(observation, lastToken, token, identifier, structIndentationAnalysis, numberOfActualWhitespacesPriorStructBeginning);
				newLine = false;
			}
			if (priorLine != currentLine) {
				newLine = true;
			}
		}
		int higestIndentationDepth = structIndentationAnalysis.getHighestIndentationDepth();
		if (higestIndentationDepth > 4) {
			observation.setAdvise(observation.getAdvise() + "\tDie Struktur oder Klasse \"" + identifier + "\" verwendet eine Einrückungstiefe von bis zu " + structIndentationAnalysis.getHighestIndentationDepth() + ", wobei optimalerweise eine Einrückungstiefe von 3 bis 4 nicht überschritten werden sollte.\n");
		}
		else if (higestIndentationDepth == 4) {
			cleanCodeScore += 1;
			observation.setAdvise(observation.getAdvise() + "\tDie Struktur oder Klasse \"" + identifier + "\" verwendet eine Einrückungstiefe von bis zu " + structIndentationAnalysis.getHighestIndentationDepth() + ". Noch sauberer wäre eine maximale Einrückungstiefe von 3 Ebenen.\n");
		}
		else {
			cleanCodeScore += 2;
		}
		// Falls wir eine konsistente Einrückung finden, vergeben wir einen Punkt
		if (structIndentationAnalysis.getConsistentIndentation()) {
			cleanCodeScore += 1;
		}
		else {
			observation.setAdvise(observation.getAdvise() + "\tDie Struktur oder Klasse \"" + identifier + "\" verwendet keine konsistente Einrückung. Ideal wäre eine Anzahl von 2 oder 4 Leerezeichen je Einrückung, die durchgehend verwendet wird.\n");
		}
		// Falls wir uns nicht in einer verschachtelten Struktur befinden, sollte die Struktur bei 0 Leerzeichen in der Zeile beginnen. Sonst bei der gefundenen ersten Anzahl an Leerzeichen für eine Einrückung in der Klasse mal der Verschachtelungstiefe der Struktur.
		int numberOfExpectedWhitespacesPriorStructBeginning = CleanCodeAnalyzer.numberOfWhitespacesPerIndentation * numberOfNestedStructs;
		if (numberOfExpectedWhitespacesPriorStructBeginning == numberOfActualWhitespacesPriorStructBeginning) {
			cleanCodeScore += 1;
		}
		else {
			observation.setAdvise(observation.getAdvise() + "\tVor der Struktur oder Klasse \"" + identifier + "\" werden " + numberOfExpectedWhitespacesPriorStructBeginning + " erwartet. Es wurden jedoch " + numberOfActualWhitespacesPriorStructBeginning + " gefunden.\n");
		}
		observation.setCleanCodeObservationScore(observation.getCleanCodeObservationScore() + cleanCodeScore);
		observation.setHighestPossibleScoreForThisObservation(observation.getHighestPossibleScoreForThisObservation() + 4);
		if (observation.getCleanCodeObservationScore() == observation.getHighestPossibleScoreForThisObservation()) {
			observation.setAdvise(null);
		}
	}
	
	public static void analyzeStructBody(CompileProcess compileProcess, Datatype<Node<Object>> dtype, Integer structEndTokenVecIndex, Node<Object> structNode, Node<Object> bodyNode, int numberOfNestedStructs) {
		String identifier = dtype.getTypeStr();
		Token<Object> structStartToken = dtype.getStartToken();
		// 1. Analyse der Einrückungen
		CleanCodeAnalyzer.analyzeStructIndentationDepth(compileProcess, structStartToken, structEndTokenVecIndex, structNode, numberOfNestedStructs);
		// 2. Analyse der vertikalen Dichte.
		CleanCodeObservation<Object> verticalDensityObservation = new CleanCodeObservation<Object>(CleanCodeAnalysisBroadCategory.CLASS_ANALYSIS, CleanCodeAnalysisConcreteCategory.VERTICAL_DENSITY_ANALYSIS, structStartToken.getPos(), structNode);
		compileProcess.getCleanCodeObservations().vectorPush(verticalDensityObservation);
		@SuppressWarnings("unchecked")
		Body<Object> body = (Body<Object>) bodyNode.getValue();
		CleanCodeAnalyzer.analyzeStructVariablePlacement(compileProcess, verticalDensityObservation, body, identifier);
		// Einmal analysieren wir für Forward-Declaration-Methoden und einmal die Definitionen.
		CleanCodeAnalyzer.analyzeStructMethodPlacement(compileProcess, verticalDensityObservation, body, identifier, numberOfNestedStructs, true);
		CleanCodeAnalyzer.analyzeStructMethodPlacement(compileProcess, verticalDensityObservation, body, identifier, numberOfNestedStructs, false);
		CleanCodeAnalyzer.analyzeNestedStructPlacement(compileProcess, verticalDensityObservation, body, identifier);
		if (verticalDensityObservation.getCleanCodeObservationScore() == verticalDensityObservation.getHighestPossibleScoreForThisObservation()) {
			verticalDensityObservation.setAdvise(null);
		}
		// 3. Analyse der Access Specifier von Variablen (sollten nur private sein).
		CleanCodeObservation<Object> accessSpecifierObservation = new CleanCodeObservation<Object>(CleanCodeAnalysisBroadCategory.CLASS_ANALYSIS, CleanCodeAnalysisConcreteCategory.ACCESS_SPECIFIER_ANALYSIS, structStartToken.getPos(), structNode); 
		compileProcess.getCleanCodeObservations().vectorPush(accessSpecifierObservation);
		CleanCodeAnalyzer.analyzeStructVariableAccessSpecifier(compileProcess, accessSpecifierObservation, body, identifier);
		// 4. Analyse der Methodennamen (Getter und Setter sind nicht erwünscht).
		CleanCodeObservation<Object> getterAndSetterObservation = new CleanCodeObservation<Object>(CleanCodeAnalysisBroadCategory.CLASS_ANALYSIS, CleanCodeAnalysisConcreteCategory.IDENTIFIER_ANALYSIS, structStartToken.getPos(), structNode);
		compileProcess.getCleanCodeObservations().vectorPush(getterAndSetterObservation);
		CleanCodeAnalyzer.analyzeStructRegardingGettersAndSetters(compileProcess, getterAndSetterObservation, body, identifier);
		// 5. Analyse der Breite von Zeilen (nicht mehr als 120 Zeichen sind erwünscht).
		CleanCodeObservation<Object> widthObservation = new CleanCodeObservation<Object>(CleanCodeAnalysisBroadCategory.CLASS_ANALYSIS, CleanCodeAnalysisConcreteCategory.WIDTH_ANALYSIS, structStartToken.getPos(), structNode);
		compileProcess.getCleanCodeObservations().vectorPush(widthObservation);
		CleanCodeAnalyzer.analyzeEntityWidth(compileProcess, widthObservation, dtype.getStartToken(), dtype.getStartTokenVecIndex().intValue(), structEndTokenVecIndex.intValue(), null);
	}
	
	public static CleanCodeObservation<Object> analyzeStructOrUnionName(CompileProcess compileProcess, Token<Object> nameToken, Datatype<Node<Object>> parentSUDatatype) {
		// Wenn wir den Struktornamen noch nicht analysiert haben (die Namen kommen wegen Strukturdefinition und den Deklarationen häufiger vor),
		// dann analysieren wir den Namen auf Suchbarkeit.
		String identifier = (String) nameToken.getValue();
		if (!(CleanCodeAnalyzer.structOrUnionNameAlreadyAnalyzed(identifier))) {
			CleanCodeObservation<Object> observation = new CleanCodeObservation<Object>(CleanCodeAnalysisBroadCategory.CLASS_ANALYSIS, CleanCodeAnalysisConcreteCategory.IDENTIFIER_ANALYSIS, nameToken.getPos());
			compileProcess.getCleanCodeObservations().vectorPush(observation);
			CleanCodeAnalyzer.analyzedStructOrUnionNames.vectorPush(identifier);
			CleanCodeAnalyzer.analyzeSructOrUnionNameForBeingPascalCase(observation, identifier);
			CleanCodeAnalyzer.analyzeIdentifierForSearchability(observation, identifier, false);
			CleanCodeAnalyzer.analyzeIdentifierForStartingWithMemberPrefix(observation, identifier);
			// Wir überprüfen, ob im Strukturnamen ein Präfix einer Struktur vorhanden ist, die die Klasse umschließt.
			// parentIdentifier meint daher nicht den Namen einer Elternklasse im Sinne von Polymorphie, sondern eine Klasse
			// die die aktuelle Klasse in ihrem Körper enthhält.
			CleanCodeAnalyzer.analyzeIdentifierForRedundantClassContext(observation, identifier, null);
			if (observation.getCleanCodeObservationScore() == observation.getHighestPossibleScoreForThisObservation()) {
				observation.setAdvise(null);
			}
			return observation;
		}
		return null;
	}
	
	public static void analyzeMethodName(CompileProcess compileProcess, Token<Object> nameToken, Node<Object> funcNode, String dtypeIdentifier, Datatype<Node<Object>> parentSUDatatype) {
		String identifier = (String) nameToken.getValue();
		CleanCodeObservation<Object> observation = new CleanCodeObservation<Object>(CleanCodeAnalysisBroadCategory.METHOD_ANALYSIS, CleanCodeAnalysisConcreteCategory.IDENTIFIER_ANALYSIS, nameToken.getPos(), funcNode);
		compileProcess.getCleanCodeObservations().vectorPush(observation);
		CleanCodeAnalyzer.analyzeMethodNameForLowerCaseBeginning(observation, identifier);
		CleanCodeAnalyzer.analyzeMethodNameForUsingConsistentMethodNameConvention(observation, identifier);
		CleanCodeAnalyzer.analyzeIdentifierForSearchability(observation, identifier, false);
		CleanCodeAnalyzer.analyzeIdentifierForStartingWithMemberPrefix(observation, identifier);
		CleanCodeAnalyzer.analyzeIdentifierForRedundantClassContext(observation, identifier, parentSUDatatype);
		CleanCodeAnalyzer.analyzeIdentifierForHungarianNotation(observation, identifier, dtypeIdentifier);
		if (SymbolTable.S_EQ(observation.getAdvise(), "In Zeile " + nameToken.getPos().getLineNumber() + ", Spalte " + nameToken.getPos().getColNumber() + ":\n")) {
			observation.setAdvise(null);
		}
	}

	public static void analyzeVarName(CompileProcess compileProcess, Token<Object> nameToken, Node<Object> varNode, String dtypeIdentifier, Datatype<Node<Object>> parentSUDatatype, Node<Object> funcNode, boolean isForLoopInitializer) {
		String identifier = (String) nameToken.getValue();
		CleanCodeObservation<Object> observation = new CleanCodeObservation<Object>(CleanCodeAnalysisBroadCategory.VARIABLE_ANALYSIS, CleanCodeAnalysisConcreteCategory.IDENTIFIER_ANALYSIS, nameToken.getPos(), varNode);
		compileProcess.getCleanCodeObservations().vectorPush(observation);
		// Wir machen die umfassendere Bezeichneranalyse nur bei Variablen außerhalb einer For-Schleife, da diese laut Konvention nur einen Buchstaben haben soll, wo viele Analysen irrelevant werden.
		if (!(isForLoopInitializer)) {
			boolean insideFunction = funcNode != null;
			CleanCodeAnalyzer.analyzeIdentifierForSearchability(observation, identifier, insideFunction);
			CleanCodeAnalyzer.analyzeIdentifierForStartingWithMemberPrefix(observation, identifier);
			CleanCodeAnalyzer.analyzeIdentifierForRedundantClassContext(observation, identifier, parentSUDatatype);
			CleanCodeAnalyzer.analyzeIdentifierForRedundantMethodContext(observation, identifier, funcNode);
			CleanCodeAnalyzer.analyzeIdentifierForHungarianNotation(observation, identifier, dtypeIdentifier);
		}
		else {
			CleanCodeAnalyzer.analyzeIdentifierOfForLoopInitializer(observation, identifier);
		}
		if (SymbolTable.S_EQ(observation.getAdvise(), "In Zeile " + nameToken.getPos().getLineNumber() + ", Spalte " + nameToken.getPos().getColNumber() + ":\n")) {
			observation.setAdvise(null);
		}
	}
	
	public static void analyzeConstructorMethod(CompileProcess compileProcess, Token<Object> nameToken, Node<Object> funcNode) {
		CleanCodeObservation<Object> observation = new CleanCodeObservation<Object>(CleanCodeAnalysisBroadCategory.METHOD_ANALYSIS, CleanCodeAnalysisConcreteCategory.ACCESS_SPECIFIER_ANALYSIS, nameToken.getPos(), funcNode);
		compileProcess.getCleanCodeObservations().vectorPush(observation);
		int cleanCodeScore = 0;
		@SuppressWarnings("unchecked")
		Function<Object> func = (Function<Object>) funcNode.getValue();
		if (func.getAccessSpecifier() == AccessSpecifierFlag.ACCESS_SPECIFIER_PRIVATE) {
			cleanCodeScore += 1;
		}
		else {
			observation.setAdvise(observation.getAdvise() + "\tDer überladene Konstruktor der Klasse \"" + (String) nameToken.getValue() + "\" verwendet den Access-Specifier \"public\". Sauberer wäre es, den überladenen Konstruktor als \"private\" zu deklarieren. Statt dem überladenen Konstruktor sollten statische Factory-Methoden mit Namen verwendet werden, die die an den Konstruktor übergebenen Arguente näher beschreiben. Diese könnten den als \"private\" deklarierten überladenen Konstruktor aufrufen.\n"); 
		}
		observation.setCleanCodeObservationScore(observation.getCleanCodeObservationScore() + cleanCodeScore);
		observation.setHighestPossibleScoreForThisObservation(observation.getHighestPossibleScoreForThisObservation() + 1);
	}

	public static void analyzeMethodHeight(CompileProcess compileProcess, Token<Object> nameToken, int funcStartLine, int funcEndLine, Node<Object> funcNode) {
		String identifier = (String) nameToken.getValue();
		@SuppressWarnings("unchecked")
		Function<Object> func = (Function<Object>) funcNode.getValue();
		String tildeToAddIfDestructor = CleanCodeAnalyzer.checkForTildeToAdd(func.getFlags());
		CleanCodeObservation<Object> observation = new CleanCodeObservation<Object>(CleanCodeAnalysisBroadCategory.METHOD_ANALYSIS, CleanCodeAnalysisConcreteCategory.HEIGHT_ANALYSIS, nameToken.getPos(), funcNode);
		compileProcess.getCleanCodeObservations().vectorPush(observation);
		int funcLength = funcEndLine - funcStartLine;
		int cleanCodeScore = 0;
		if (funcLength <= 20) {
			cleanCodeScore += 2;
		}
		else if (funcLength <= 100) {
			cleanCodeScore += 1;
			observation.setAdvise(observation.getAdvise() + "\tDie Methode oder Funktion \"" + tildeToAddIfDestructor + identifier + "\" könnte sauberer sein, wenn sie nicht länger als 20 Zeilen lang wäre. Sie ist jedoch " + funcLength + " Zeilen lang.\n");
		}
		else {
			observation.setAdvise(observation.getAdvise() + "\tDie Methode oder Funktion \"" + tildeToAddIfDestructor + identifier + "\" könnte sauberer sein, wenn sie nicht länger als 100, besser noch, nicht länger als 20 Zeilen lang wäre. Sie ist jedoch " + funcLength + " Zeilen lang.\n");
		}
		if (SymbolTable.S_EQ(observation.getAdvise(), "In Zeile " + nameToken.getPos().getLineNumber() + ", Spalte " + nameToken.getPos().getColNumber() + ":\n")) {
			observation.setAdvise(null);
		}
		observation.setCleanCodeObservationScore(observation.getCleanCodeObservationScore() + cleanCodeScore);
		observation.setHighestPossibleScoreForThisObservation(observation.getHighestPossibleScoreForThisObservation() + 2);
	}
	
	private static void analyzeColumnWidthByLookAtLastTokenInLine(CleanCodeObservation<Object> observation, Token<Object> lastTokenInLine, String identifier, Boolean allLinesAreThin, int numberOfCharsBoundary) {
		int numberOfCharsInLine = lastTokenInLine.getPos().getColNumber();
		if (numberOfCharsInLine > numberOfCharsBoundary) {
			allLinesAreThin = false;
			observation.setAdvise(observation.getAdvise() + lastTokenInLine.getPos().getLineNumber() + ", ");
		}
	}
	
	// Analysiert die Breite von Methoden (maximal 150 Zeichen) und von Klassen (maximal 120 Zeichen).
	private static void analyzeEntityWidth(CompileProcess compileProcess, CleanCodeObservation<Object> observation, Token<Object> nameToken, int startTokenVecIndex, int endTokenVecIndex, Node<Object> entityNode) {
		String identifier = (String) nameToken.getValue();
		if (observation.getBroadCategory() == CleanCodeAnalysisBroadCategory.METHOD_ANALYSIS) {
			@SuppressWarnings("unchecked")
			Function<Object> func = (Function<Object>) entityNode.getValue();
			String tildeToAddIfDestructor = CleanCodeAnalyzer.checkForTildeToAdd(func.getFlags());
			observation.setAdvise(observation.getAdvise() + "\tDie Methode oder Funktion \"" + tildeToAddIfDestructor + identifier + "\" könnte sauberer sein, wenn sie nicht länger als 150 Zeichen je Zeile lang wäre. Diese Anzahl wird jedoch in den Zeilen ");
		}
		else {
			observation.setAdvise(observation.getAdvise() + "\tDie Struktur oder Klasse \"" + identifier + "\" könnte sauberer sein, wenn sie nicht länger als 120 Zeichen je Zeile lang wäre. Diese Anzahl wird jedoch in den Zeilen ");
		}
		int cleanCodeScore = 0;
		Vector<Token<Object>> tokenVec = compileProcess.getTokenVec();
		// Sonderfall: AccessSpecifier.
		if (startTokenVecIndex != 0) {
			Token<Object> possibleAccessSpecifierToken = tokenVec.vectorPeekPtrAt(startTokenVecIndex - 1);
			if (possibleAccessSpecifierToken.getType() == TokenType.TOKEN_TYPE_KEYWORD && SymbolTable.keywordIsAccessSpecifier((String) tokenVec.vectorPeekPtrAt(startTokenVecIndex).getValue())) {
				startTokenVecIndex -= 1;
			}
		}
		else {
			startTokenVecIndex = 0;
		}
		Token<Object> token = tokenVec.vectorPeekPtrAt(startTokenVecIndex);
		Token<Object> lastToken = null;
		int currentLine = tokenVec.vectorPeekAt(startTokenVecIndex).getPos().getLineNumber();
		Boolean allLinesAreThin = true;
		for(int i = startTokenVecIndex + 1; i <= endTokenVecIndex + 1; i++) {
			lastToken = token;
			int priorLine = lastToken.getPos().getLineNumber();
			token = tokenVec.vectorPeekPtrAt(i);
			currentLine = token.getPos().getLineNumber();
			// Wenn wir eine neue Zeile erreicht haben, schauen wir uns das letzte Token der vorigen Zeile an und zählen die Anzahl der Spalten.
			if (priorLine != currentLine) {
				if (observation.getBroadCategory() == CleanCodeAnalysisBroadCategory.METHOD_ANALYSIS) {
					CleanCodeAnalyzer.analyzeColumnWidthByLookAtLastTokenInLine(observation, lastToken, identifier, allLinesAreThin, 150);
				}
				else {
					CleanCodeAnalyzer.analyzeColumnWidthByLookAtLastTokenInLine(observation, lastToken, identifier, allLinesAreThin, 120);
				}
			}
		}
		if (allLinesAreThin) {
			cleanCodeScore += 1;
			observation.setAdvise(null);
		}
		else {
			observation.setAdvise(observation.getAdvise() + " überschritten.\n");
		}
		observation.setCleanCodeObservationScore(observation.getCleanCodeObservationScore() + cleanCodeScore);
		observation.setHighestPossibleScoreForThisObservation(observation.getHighestPossibleScoreForThisObservation() + 1);
	}
	
	private static void analyzeIndentationByLookAtLastTokenInPriorLine(CleanCodeObservation<Object> observation, Token<Object> newLineToken, Token<Object> firstTokenInNewLine, String identifier, IndentationAnalysis indentationAnalysis, int numberOfWhitespacesBeforeEntityStart) {
		// Eine Leerzeile zählt nicht.
		if (newLineToken.getWhitespaces() == 0) {
			return;
		}
		indentationAnalysis.setNumberOfWhitespacesForIndentationInPriorLine(indentationAnalysis.getNumberOfWhitespacesForIndentationInCurrentLine());
		indentationAnalysis.setNumberOfWhitespacesForIndentationInCurrentLine(newLineToken.getWhitespaces());
		boolean plusIndentation = false;
		boolean minusIndentation = false;
		// Bei der ersten Einrückung schauen wir, wie viele Leerzeichen verwendet wurden.
		if (indentationAnalysis.getAdditionalNumberOfWhitespacesPerIndentation() == -1) {
			indentationAnalysis.setAdditionalNumberOfWhitespacesPerIndentation(indentationAnalysis.getNumberOfWhitespacesForIndentationInCurrentLine() - numberOfWhitespacesBeforeEntityStart);
			CleanCodeAnalyzer.numberOfWhitespacesPerIndentation = indentationAnalysis.getNumberOfWhitespacesForIndentationInCurrentLine();
			if (indentationAnalysis.getAdditionalNumberOfWhitespacesPerIndentation() != 2 && indentationAnalysis.getAdditionalNumberOfWhitespacesPerIndentation() != 4) {
				if (observation.getBroadCategory() == CleanCodeAnalysisBroadCategory.METHOD_ANALYSIS) {
					observation.setAdvise(observation.getAdvise() + "\tDie Funktion oder Methode \"" + identifier + "\" verwendet eine Anzahl von " + indentationAnalysis.getAdditionalNumberOfWhitespacesPerIndentation() + " Leerzeichen je Einrückung, wobei per Konvention durchgehend eine einheitliche Einrückung von 2 oder 4 zusätzlichen Leerzeichen verwendet werden sollte, sobald eine neue Einrückung angelegt wird.\n");
				}
				else if (observation.getBroadCategory() == CleanCodeAnalysisBroadCategory.CLASS_ANALYSIS) {
					observation.setAdvise(observation.getAdvise() + "\tDie Struktur oder Klasse \"" + identifier + "\" verwendet eine Anzahl von " + indentationAnalysis.getAdditionalNumberOfWhitespacesPerIndentation() + " Leerzeichen je Einrückung, wobei per Konvention durchgehend eine einheitliche Einrückung von 2 oder 4 zusätzlichen Leerzeichen verwendet werden sollte, sobald eine neue Einrückung angelegt wird.\n");
				}
			}
		}
		// Wir erhöhen oder senken die aktuelle Einrückungstiefe.
		if (indentationAnalysis.getNumberOfWhitespacesForIndentationInCurrentLine() > indentationAnalysis.getNumberOfWhitespacesForIndentationInPriorLine()) {
			indentationAnalysis.setCurrentIndentationDepth(indentationAnalysis.getCurrentIndentationDepth() + 1);
			plusIndentation = true;
			if (indentationAnalysis.getCurrentIndentationDepth() > indentationAnalysis.getHighestIndentationDepth()) {
				indentationAnalysis.setHighestIndentationDepth(indentationAnalysis.getHighestIndentationDepth() + 1);
			}
		}
		else if (indentationAnalysis.getNumberOfWhitespacesForIndentationInCurrentLine() < indentationAnalysis.getNumberOfWhitespacesForIndentationInPriorLine()) {
			indentationAnalysis.setCurrentIndentationDepth(indentationAnalysis.getCurrentIndentationDepth() - 1);
			minusIndentation = true;
		}
		// Im else-Fall sind wir auf der gleichen Ebene, keine neue Einrückung.
		else {
			return;
		}
		// Nun vergeben wir noch Tipps, um eine gute Einrückung zu gewährleisten.
		int expectedNumberOfWhitespaces;
		if (plusIndentation) {
			expectedNumberOfWhitespaces = indentationAnalysis.getNumberOfWhitespacesForIndentationInPriorLine() + indentationAnalysis.getAdditionalNumberOfWhitespacesPerIndentation();
			if (indentationAnalysis.getNumberOfWhitespacesForIndentationInCurrentLine() != expectedNumberOfWhitespaces) {
				if (observation.getNumberOfNewlinesInAdvise() < 4) {
					observation.setAdvise(observation.getAdvise() + "\tDie Funktion oder Methode \"" + identifier + "\" verwendet in Zeile " + firstTokenInNewLine.getPos().getLineNumber() + " eine abweichende Einrückung von " + indentationAnalysis.getNumberOfWhitespacesForIndentationInCurrentLine() + " Leerzeichen für die Einrückung, wobei eine Einrückung von " + expectedNumberOfWhitespaces + " Leerzeichen erwartet wurde.\n");
				}
				else if (observation.getNumberOfNewlinesInAdvise() == 4) {
					observation.setAdvise(observation.getAdvise() + "\t...\n");
				}
				indentationAnalysis.setConsistentIndentation(false);
			}
		}
		else if (minusIndentation) {
			expectedNumberOfWhitespaces = indentationAnalysis.getNumberOfWhitespacesForIndentationInPriorLine() - indentationAnalysis.getAdditionalNumberOfWhitespacesPerIndentation();
			if (indentationAnalysis.getNumberOfWhitespacesForIndentationInCurrentLine() != expectedNumberOfWhitespaces) {
				if (observation.getNumberOfNewlinesInAdvise() < 4) {
					observation.setAdvise(observation.getAdvise() + "\tDie Funktion oder Methode \"" + identifier + "\" verwendet in Zeile " + firstTokenInNewLine.getPos().getLineNumber() + " eine abweichende Einrückung von " + indentationAnalysis.getNumberOfWhitespacesForIndentationInCurrentLine() + " Leerzeichen für die Einrückung, wobei eine Einrückung von " + expectedNumberOfWhitespaces + " Leerzeichen erwartet wurde.\n");
				}
				else if (observation.getNumberOfNewlinesInAdvise() == 4) {
					observation.setAdvise(observation.getAdvise() + "\t...\n");
				}
				indentationAnalysis.setConsistentIndentation(false);
			}
		}
	}
	
	private static void analyzeMethodIndentationDepth(CompileProcess compileProcess, Token<Object> nameToken, int funcStartTokenVecIndex, int funcEndTokenVecIndex, Node<Object> funcNode) {
		IndentationAnalysis methodIndentationAnalysis = new IndentationAnalysis();
		String identifier = (String) nameToken.getValue();
		@SuppressWarnings("unchecked")
		Function<Object> func = (Function<Object>) funcNode.getValue();
		String tildeToAddIfDestructor = CleanCodeAnalyzer.checkForTildeToAdd(func.getFlags());
		CleanCodeObservation<Object> observation = new CleanCodeObservation<Object>(CleanCodeAnalysisBroadCategory.METHOD_ANALYSIS, CleanCodeAnalysisConcreteCategory.INDENTATION_ANALYSIS, nameToken.getPos(), funcNode);
		compileProcess.getCleanCodeObservations().vectorPush(observation);
		int cleanCodeScore = 0;
		Vector<Token<Object>> tokenVec = compileProcess.getTokenVec();
		funcStartTokenVecIndex = CleanCodeAnalyzer.checkTokenVecIndexForAccessSpecifierNamespaceAndGenerics(compileProcess, funcStartTokenVecIndex);
		Token<Object> possibleAccessSpecifierToken = tokenVec.vectorPeekPtrAt(funcStartTokenVecIndex - 1);
		if (possibleAccessSpecifierToken != null && possibleAccessSpecifierToken.getType() == TokenType.TOKEN_TYPE_KEYWORD && SymbolTable.keywordIsAccessSpecifier((String) tokenVec.vectorPeekPtrAt(funcStartTokenVecIndex).getValue())) {
			funcStartTokenVecIndex -= 1;
		}
		Token<Object> priorToken = tokenVec.vectorPeekPtrAt(funcEndTokenVecIndex - 1);
		int numberOfWhitespacesBeforeCurrentMethod = priorToken.getWhitespaces();
		Token<Object> token = tokenVec.vectorPeekPtrAt(funcStartTokenVecIndex);
		// Nun wird noch geschaut, ob die Einrückungstiefe der Methode maximal 1 bis 2 Ebenen entspricht.
		Token<Object> lastToken = null;
		int currentLine = token.getPos().getLineNumber();
		boolean newLine = false;
		for (int i = funcStartTokenVecIndex + 1; i <= funcEndTokenVecIndex; i++) {
			lastToken = token;
			int priorLine = lastToken.getPos().getLineNumber();
			token = tokenVec.vectorPeekPtrAt(i);
			currentLine = token.getPos().getLineNumber();
			if (newLine) {
				CleanCodeAnalyzer.analyzeIndentationByLookAtLastTokenInPriorLine(observation, lastToken, token, identifier, methodIndentationAnalysis, numberOfWhitespacesBeforeCurrentMethod);
				newLine = false;
			}
			if (priorLine != currentLine) {
				newLine = true;
			}
		}
		if (methodIndentationAnalysis.getHighestIndentationDepth() > 2) {
			observation.setAdvise(observation.getAdvise() + "\tDie Funktion oder Methode \"" + tildeToAddIfDestructor + identifier + "\" verwendet eine Einrückungstiefe von bis zu " + methodIndentationAnalysis.getHighestIndentationDepth() + ", wobei optimalerweise eine Einrückungstiefe von 1 bis 2 nicht überschritten werden sollte.\n");
		}
		else if (methodIndentationAnalysis.getHighestIndentationDepth() == 2) {
			cleanCodeScore += 1;
			observation.setAdvise(observation.getAdvise() + "\tDie Funktion oder Methode \"" + tildeToAddIfDestructor + identifier + "\" verwendet eine Einrückungstiefe von bis zu " + methodIndentationAnalysis.getHighestIndentationDepth() + ". Noch sauberer wäre eine maximale Einrückungstiefe von einer Ebene.\n");
		}
		else {
			cleanCodeScore += 2;
		}
		if (methodIndentationAnalysis.getConsistentIndentation()) {
			cleanCodeScore += 1;
		}
		else {
			observation.setAdvise(observation.getAdvise() + "\tDie Funktion oder Methode \"" + identifier + "\" verwendet keine konsistente Einrückung. Ideal wäre eine Einrückung von 2 oder 4 Zeichen, die durchgehend verwendet wird.\n");
		}
		observation.setCleanCodeObservationScore(observation.getCleanCodeObservationScore() + cleanCodeScore);
		observation.setHighestPossibleScoreForThisObservation(observation.getHighestPossibleScoreForThisObservation() + 3);
		if (observation.getCleanCodeObservationScore() == observation.getHighestPossibleScoreForThisObservation()) {
			observation.setAdvise(null);
		}
	}
	
	public static void checkForMethodHeaderComment(CompileProcess compileProcess, Token<Object> nameToken, int functionStartTokenVecIndex, Node<Object> funcNode) {
		String identifier = (String) nameToken.getValue();
		@SuppressWarnings("unchecked")
		Function<Object> func = (Function<Object>) funcNode.getValue();
		String tildeToAddIfDestructor = CleanCodeAnalyzer.checkForTildeToAdd(func.getFlags());
		boolean noFunctionHeaderComment = true;
		int cleanCodeScore = 0;
		CleanCodeObservation<Object> observation = null;
		if (functionStartTokenVecIndex != 0 && functionStartTokenVecIndex != 1) {
			Token<Object> possibleCommentToken = Parser.currentProcess.getTokenVec().vectorPeekAt(functionStartTokenVecIndex - 2);
			if (possibleCommentToken.getType() == TokenType.TOKEN_TYPE_COMMENT) {
				String comment = (String) possibleCommentToken.getValue();
				if (comment.contains("@param") || comment.contains("@return") || comment.contains("@note")) {
					noFunctionHeaderComment = false;
					observation = new CleanCodeObservation<Object>(CleanCodeAnalysisBroadCategory.METHOD_ANALYSIS, CleanCodeAnalysisConcreteCategory.COMMENT_ANALYSIS, possibleCommentToken.getPos(), funcNode);
					compileProcess.getCleanCodeObservations().vectorPush(observation);
					observation.setAdvise(observation.getAdvise() + "\tDie Funktion oder Methode \"" + tildeToAddIfDestructor + identifier + "\" verwendet einen Funktion- bzw. Methodenkopfkommentar. Diese sollten nach Möglichkeit nicht verwendet werden. Verwenden Sie stattdessen bedeutungsvolle Bezeichner.\n");
				}
			}
		}
		if (noFunctionHeaderComment) {
			observation = new CleanCodeObservation<Object>(CleanCodeAnalysisBroadCategory.METHOD_ANALYSIS, CleanCodeAnalysisConcreteCategory.COMMENT_ANALYSIS, nameToken.getPos(), funcNode);
			compileProcess.getCleanCodeObservations().vectorPush(observation);
			cleanCodeScore += 1;
		}
		observation.setCleanCodeObservationScore(observation.getCleanCodeObservationScore() + cleanCodeScore);
		observation.setHighestPossibleScoreForThisObservation(observation.getHighestPossibleScoreForThisObservation() + 1);
		if (observation.getCleanCodeObservationScore() == observation.getHighestPossibleScoreForThisObservation()) {
			observation.setAdvise(null);
		}
	}
	
	private static void analyzeVariablePlacementInMethod(CompileProcess compileProcess, Token<Object> nameToken, Node<Object> bodyNode, Node<Object> funcNode) {
		String identifier = (String) nameToken.getValue();
		@SuppressWarnings("unchecked")
		Function<Object> func = (Function<Object>) funcNode.getValue();
		String tildeToAddIfDestructor = CleanCodeAnalyzer.checkForTildeToAdd(func.getFlags());
		CleanCodeObservation<Object> observation = new CleanCodeObservation<Object>(CleanCodeAnalysisBroadCategory.METHOD_ANALYSIS, CleanCodeAnalysisConcreteCategory.VERTICAL_DENSITY_ANALYSIS, nameToken.getPos(), funcNode);
		compileProcess.getCleanCodeObservations().vectorPush(observation);
		int cleanCodeScore = 0;
		@SuppressWarnings("unchecked")
		Body<Object> body = (Body<Object>) bodyNode.getValue();
		Vector<Node<Object>> bodyVec = body.getStatements();
		boolean foundSomethingElseThanVar = false;
		boolean localVarsArePlacedAtTheBeginning = true;
		boolean foundAtLeastOneVar = false;
		for(int i = 0; i < bodyVec.vectorCount(); i++) {
			Node<Object> node = bodyVec.vectorPeekPtrAt(i);
			if (node.getType() != NodeType.NODE_TYPE_VARIABLE && node.getType() != NodeType.NODE_TYPE_VARIABLE_LIST) {
				foundSomethingElseThanVar = true;
			}
			else if (node.getType() == NodeType.NODE_TYPE_VARIABLE || node.getType() == NodeType.NODE_TYPE_VARIABLE_LIST) {
				foundAtLeastOneVar = true;
				if (foundSomethingElseThanVar) {
					localVarsArePlacedAtTheBeginning = false;
					break;
				}
			}
		}
		if (localVarsArePlacedAtTheBeginning && foundAtLeastOneVar) {
			cleanCodeScore += 1;
			observation.setAdvise(null);
		}
		else {
			if (foundAtLeastOneVar && !(localVarsArePlacedAtTheBeginning)) {
				observation.setAdvise(observation.getAdvise() + "\tDie Funktion oder Methode \"" + tildeToAddIfDestructor + identifier + "\" verwendet Variablen, die nicht zum Beginn der Funktion oder Methode deklariert werden. Die Deklaration aller lokalen Variablen sollte nach Möglichkeit ganz oben im Funktions- bzw. Methodenkörper stattfinden.\n");
			}
		}
		if (foundAtLeastOneVar) {
			observation.setCleanCodeObservationScore(observation.getCleanCodeObservationScore() + cleanCodeScore);
			observation.setHighestPossibleScoreForThisObservation(observation.getHighestPossibleScoreForThisObservation() + 1);
		}
		else {
			observation.setAdvise(null);
			observation.setHighestPossibleScoreForThisObservation(observation.getHighestPossibleScoreForThisObservation() + 0);
		}
	}

	public static void analyzeMethodFormatting(CompileProcess compileProcess, Token<Object> nameToken, int functionStartLine, int functionStartTokenVecIndex, Integer functionEndLine, Integer functionEndTokenVecIndex, Node<Object> functionNode, Node<Object> bodyNode) {
		CleanCodeAnalyzer.analyzeMethodHeight(compileProcess, nameToken, functionStartLine, functionEndLine, functionNode);
		CleanCodeObservation<Object> widthObservation = new CleanCodeObservation<Object>(CleanCodeAnalysisBroadCategory.METHOD_ANALYSIS, CleanCodeAnalysisConcreteCategory.WIDTH_ANALYSIS, nameToken.getPos(), functionNode);
		compileProcess.getCleanCodeObservations().vectorPush(widthObservation);
		CleanCodeAnalyzer.analyzeEntityWidth(compileProcess, widthObservation, nameToken, functionStartTokenVecIndex, functionEndTokenVecIndex, functionNode);
		CleanCodeAnalyzer.analyzeMethodIndentationDepth(compileProcess, nameToken, functionStartTokenVecIndex, functionEndTokenVecIndex, functionNode);
		CleanCodeAnalyzer.checkForMethodHeaderComment(compileProcess, nameToken, functionStartTokenVecIndex, functionNode);
		CleanCodeAnalyzer.analyzeVariablePlacementInMethod(compileProcess, nameToken, bodyNode, functionNode);
	}
	
	private static String checkForTildeToAdd(FunctionFlag flag) {
		if (flag == FunctionFlag.FUNCTION_NODE_FLAG_IS_NATIVE_DESTRUCTOR) {
			return "~";
		}
		return "";
	}
		
	private static int analyzeNumberOfArguments(CleanCodeObservation<Object> observation, String identifier, Vector<Node<Object>> argumentsVector, FunctionFlag funcFlag) {
		String tildeToAddIfDestructor = CleanCodeAnalyzer.checkForTildeToAdd(funcFlag);
		int cleanCodeScore = 0;
		int numberOfArguments = argumentsVector.vectorCount();
		switch(numberOfArguments) {
			case 0: {
				cleanCodeScore += 4;
				break;
			}
			case 1: {
				cleanCodeScore += 3;
				observation.setAdvise(observation.getAdvise() + "\tDie Funktion oder Methode \"" + tildeToAddIfDestructor + identifier + "\" verwendet eine Anzahl von 1 Übergabeparametern (monadisch). 0 Parameter (niladisch) wären noch sauberer.\n");
				break;
			}
			case 2: {
				cleanCodeScore += 2;
				observation.setAdvise(observation.getAdvise() + "\tDie Funktion oder Methode \"" + tildeToAddIfDestructor + identifier + "\" verwendet eine Anzahl von 2 Übergabeparametern (dyadisch). 1 Parameter (monadisch), oder besser noch, 0 Paremeter (niladisch), wären noch sauberer.\n");
				break;
			}
			case 3: {
				cleanCodeScore += 1;
				observation.setAdvise(observation.getAdvise() + "\tDie Funktion oder Methode \"" + tildeToAddIfDestructor + identifier + "\" verwendet eine Anzahl von 3 Übergabeparametern (triadisch). 1 Parameter (monadisch), oder besser noch, 0 Paremeter (niladisch), wären deutlich sauberer.\n");
				break;
			}
			default: {
				observation.setAdvise(observation.getAdvise() + "\tDie Funktion oder Methode \"" + tildeToAddIfDestructor + identifier + "\" verwendet eine Anzahl von mehr als 3 Übergabeparametern (polyadisch). 1 Parameter (monadisch), oder besser noch, 0 Paremeter (niladisch), wären deutlich sauberer.\n");
				break;
			}
		}
		return cleanCodeScore;
	}
	
	private static int analyzeMethodArgumentTypes(CleanCodeObservation<Object> observation, String identifier, Vector<Node<Object>> argumentsVector, FunctionFlag funcFlag) {
		int cleanCodeScore = 0;
		int numberOfArguments = argumentsVector.vectorCount();
		// Wir vergeben einen Punkt, falls ein Struct-Typ verwendet wurde. Und einen dafür, das keine Booleans verwendet wurden.
		boolean structTypeIsPresent = false;
		boolean booleanTypeIsPresent = false;
		String tildeToAddIfDestructor = CleanCodeAnalyzer.checkForTildeToAdd(funcFlag);
		for(int i = 0; i < numberOfArguments; i++) {
			Node<Object> node = argumentsVector.vectorPeekPtrAt(i);
			if (node.getType() == NodeType.NODE_TYPE_VARIABLE) {
				@SuppressWarnings("unchecked")
				Var<Object> var = (Var<Object>) node.getValue();
				if (var.getType().getStructOrUnionNode() != null) {
					Node<Object> suNode = var.getType().getStructOrUnionNode();
					if (suNode.getType() == NodeType.NODE_TYPE_STRUCT) {
						structTypeIsPresent = true;
					}
				}
				else {
					if (SymbolTable.S_EQ(var.getType().getTypeStr(), "bool")) {
						booleanTypeIsPresent = true;
					}
				}
			}
			if (structTypeIsPresent && booleanTypeIsPresent) {
				break;
			}
		}
		if (structTypeIsPresent || numberOfArguments < 2) {
			cleanCodeScore += 1;
		}
		else {
			observation.setAdvise(observation.getAdvise() + "\tDie Funktion oder Methode \"" + tildeToAddIfDestructor + identifier + "\" verwendet 2 oder mehr Übergabeparameter mit primitiven Datentypen. Die Anzahl der Übergabeparameter ließe sich durch die Verwendung von Struct- oder Klassentypen reduzieren.\n");
		}
		if (!(booleanTypeIsPresent) || numberOfArguments == 0) {
			cleanCodeScore += 1;
		}
		else {
			observation.setAdvise(observation.getAdvise() + "\tDie Funktion oder Methode \"" + tildeToAddIfDestructor + identifier + "\" verwendet Übergabeparameter vom boolean-Datentyp. Das deutet darauf hin, dass die Funktion oder Methode mehr als eine Aufgabe ausführt (eine Aufgabe bei \"true\", eine andere Aufgabe bei \"false\"). Sauberer wäre die Beschränkung auf eine Aufgabe pro Funktion oder Methode.\n");
		}
		return cleanCodeScore;
	}
	
	public static void analyzeMethodArgumentsFormatting(CompileProcess compileProcess, CleanCodeObservation<Object> observation, Node<Object> funcNode) {
		int cleanCodeScore = 0;
		@SuppressWarnings("unchecked")
		Function<Object> func = (Function<Object>) funcNode.getValue();
		String identifier = func.getName();
		String tildeToAddIfDestructor = CleanCodeAnalyzer.checkForTildeToAdd(func.getFlags());
		int index = func.getStartTokenVecIndex();
		Vector<Token<Object>> tokenVec = compileProcess.getTokenVec();
		boolean nameTokenFound = false;
		Token<Object> token = null;
		while(!(nameTokenFound)) {
			token = tokenVec.vectorPeekPtrAt(index);
			if (!(Token.tokenIsIdentifier(token) && SymbolTable.S_EQ((String) token.getValue(), identifier))) {
				index++;
			}
			else {
				nameTokenFound = true;
			}
		}
		Token<Object> openingParenthesisToken = tokenVec.vectorPeekPtrAt(index + 1);
		// 1. Kein Leerzeichen zwischen Methodennamen und '(' (in der gleichen Zeile).
		if (token.getWhitespaces() == 0 && (openingParenthesisToken.getPos().getLineNumber() == token.getPos().getLineNumber())) {
			cleanCodeScore += 1;
		}
		else {
			observation.setAdvise(observation.getAdvise() + "\tDie Funktion oder Methode \"" + tildeToAddIfDestructor + identifier + "\" verwendet 1 oder mehr Leerzeichen zwischen Funktions- bzw. Methodennamen und der öffnenden Klammer. Hier sollte kein Leerzeichen stehen, um visuell zu demonstrieren, dass Funktions- bzw. Methodennamen und Argumente zusammengehören.\n");
		}
		// 2. Zwischen Kommata und Datentyp soll ein Leerzeichen stehen (macht nur bei mehr als 1 Funktionsargument Sinn).
		if (func.getArgs().getVector().vectorCount() > 1) {
			boolean hasOneWhitespaceBetweenCommas = true;
			boolean foundClosingParenthesis = false;
			while(!(foundClosingParenthesis)) {
				token = tokenVec.vectorPeekPtrAt(index);
				if (!(Token.tokenIsSymbol(token, ')'))) {
					if (Token.tokenIsOperator(token, ",")) {
					 if (token.getWhitespaces() != 1) {
						 hasOneWhitespaceBetweenCommas = false;
					 }
					}
					index++;
				}
				else {
					foundClosingParenthesis = true;
				}
			}
			if (hasOneWhitespaceBetweenCommas) {
				cleanCodeScore += 1;
			}
			else {
				observation.setAdvise(observation.getAdvise() + "\tDie Funktion oder Methode \"" + tildeToAddIfDestructor + identifier + "\" weniger oder mehr als 1 Leerzeichen zwischen den Übergabeparametern. Hier sollte zwischen den Kommata und den Datentypen der Übergabeparameter genau 1 Leerzeichen stehen, um zu betonen, dass die Argumente seperat sind.\n");
			}
			observation.setCleanCodeObservationScore(observation.getCleanCodeObservationScore() + cleanCodeScore);
			observation.setHighestPossibleScoreForThisObservation(observation.getHighestPossibleScoreForThisObservation() + 2);
		}
		else {
			observation.setCleanCodeObservationScore(observation.getCleanCodeObservationScore() + cleanCodeScore);
			observation.setHighestPossibleScoreForThisObservation(observation.getHighestPossibleScoreForThisObservation() + 1);
		}
	}
	
	public static void analyzeMethodArguments(CompileProcess compileProcess, Token<Object> nameToken, Vector<Node<Object>> argumentsVector, Node<Object> funcNode) {
		String identifier = (String) nameToken.getValue();
		@SuppressWarnings("unchecked")
		Function<Object> func = (Function<Object>) funcNode.getValue();
		CleanCodeObservation<Object> observation = new CleanCodeObservation<Object>(CleanCodeAnalysisBroadCategory.METHOD_ANALYSIS, CleanCodeAnalysisConcreteCategory.METHOD_HEAD_ANALYSIS, nameToken.getPos(), funcNode);
		compileProcess.getCleanCodeObservations().vectorPush(observation);
		int cleanCodeScore = 0;
		cleanCodeScore += CleanCodeAnalyzer.analyzeNumberOfArguments(observation, identifier, argumentsVector, func.getFlags());
		cleanCodeScore += CleanCodeAnalyzer.analyzeMethodArgumentTypes(observation, identifier, argumentsVector, func.getFlags());
		CleanCodeAnalyzer.analyzeMethodArgumentsFormatting(compileProcess, observation, funcNode);
		observation.setCleanCodeObservationScore(observation.getCleanCodeObservationScore() + cleanCodeScore);
		observation.setHighestPossibleScoreForThisObservation(observation.getHighestPossibleScoreForThisObservation() + 6);
		if (SymbolTable.S_EQ(observation.getAdvise(), "In Zeile " + nameToken.getPos().getLineNumber() + ", Spalte " + nameToken.getPos().getColNumber() + ":\n")) {
			observation.setAdvise(null);
		}
	}
	
	private static int analyzeStmtForLineLengthAndFunctionCall(CleanCodeObservation<Object> observation, int numberOfLines, String keyword, Node<Object> bodyNode) {
		int cleanCodeScore = 0;
		// 1. Das Statement sollte nur 1 Zeile lang sein. Dann wäre bei einem dreizeiligen Statement (wie "if (COND){\nSTMT\n}) die Differenz der Zeilen zwischen dem ersten und letzten Token nicht größer als 2.
		if (numberOfLines <= 2) {
			cleanCodeScore += 1;
			if (bodyNode != null) {
				@SuppressWarnings("unchecked")
				Body<Object> body = (Body<Object>) bodyNode.getValue();
				Vector<Node<Object>> bodyVec = body.getStatements();
				if (bodyVec != null) {
					if (bodyVec.vectorCount() == 1) {
						Node<Object> insideBodyStatement = bodyVec.vectorPeekPtrAt(0);
						if (insideBodyStatement.getType() == NodeType.NODE_TYPE_EXPRESSION) {
							@SuppressWarnings("unchecked")
							Expression<Object> exp = (Expression<Object>) insideBodyStatement.getValue();
							// 2. Das Statement sollte am besten einen Funktionsaufruf beinhalten.
							if (SymbolTable.S_EQ(exp.getOp(), "()")) {
								cleanCodeScore += 1;
								observation.setAdvise(null);
							}
							else {
								observation.setAdvise(observation.getAdvise() + "\tDas " + keyword + "-Statement sollte im Körper ausschließlich einen Funktionsaufruf mit einem aussagekräftigen Namen beinhalten, um noch sauberer zu sein.\n");
							}
						}
						else {
							observation.setAdvise(observation.getAdvise() + "\tDas " + keyword + "-Statement sollte im Körper ausschließlich einen Funktionsaufruf mit einem aussagekräftigen Namen beinhalten, um noch sauberer zu sein.\n");
						}
					}
					else {
						observation.setAdvise(observation.getAdvise() + "\tDas " + keyword + "-Statement sollte im Körper ausschließlich einen Funktionsaufruf mit einem aussagekräftigen Namen beinhalten, um noch sauberer zu sein.\n");
					}
				}
			}
		}
		else {
			observation.setAdvise(observation.getAdvise() + "\tDas " + keyword + "-Statement sollte im Körper nicht länger als eine Zeile lang sein, wobei das Statement nach Möglichkeit nur einen Funktionsaufruf mit einem aussagekräftigen Namen beinhalten soll.\n");
		}
		return cleanCodeScore;
	}

	public static void analyzeStmt(CompileProcess compileProcess, Token<Object> keywordToken, Node<Object> stmtNode, Node<Object> bodyNode, int StmtStartTokenVecIndex, int StmtEndTokenVecIndex, String additionalKeyword) {
		String keyword = (String) keywordToken.getValue();
		if (additionalKeyword != null) {
			keyword = keyword + "-" + additionalKeyword;
		}
		CleanCodeObservation<Object> observation = new CleanCodeObservation<Object>(CleanCodeAnalysisBroadCategory.METHOD_ANALYSIS, CleanCodeAnalysisConcreteCategory.STMT_ANALYSIS, keywordToken.getPos(), stmtNode);
		compileProcess.getCleanCodeObservations().vectorPush(observation);
		Vector<Token<Object>> tokenVec = compileProcess.getTokenVec();
		Token<Object> firstTokenInStmt = tokenVec.vectorPeekPtrAt(StmtStartTokenVecIndex);
		Token<Object> lastTokenInStmt = tokenVec.vectorPeekPtrAt(StmtEndTokenVecIndex);
		int numberOfLines = lastTokenInStmt.getPos().getLineNumber() - firstTokenInStmt.getPos().getLineNumber();
		int cleanCodeScore = CleanCodeAnalyzer.analyzeStmtForLineLengthAndFunctionCall(observation, numberOfLines, keyword, bodyNode);
		observation.setCleanCodeObservationScore(observation.getCleanCodeObservationScore() + cleanCodeScore);
		observation.setHighestPossibleScoreForThisObservation(observation.getHighestPossibleScoreForThisObservation() + 2);
	}

	public static void analyzeIfSwitchStmtIsInsideInheritedClass(CompileProcess compileProcess, Token<Object> keywordToken, Datatype<Node<Object>> currentSUDatatype, Node<Object> switchNode) {
		CleanCodeObservation<Object> observation = new CleanCodeObservation<Object>(CleanCodeAnalysisBroadCategory.METHOD_ANALYSIS, CleanCodeAnalysisConcreteCategory.STMT_ANALYSIS, keywordToken.getPos(), switchNode);
		compileProcess.getCleanCodeObservations().vectorPush(observation);
		int cleanCodeScore = 0;
		if (currentSUDatatype == null) {
			observation.setAdvise(observation.getAdvise() + "\tDas Switch-Statement befindet sich außerhalb einer Klasse oder Struktur. Switch-Statements sollten nur in Klassen oder Strukturen verwendet werden, die erben. So können vererbte Methoden in den Switch-Statements angewendet werden.\n");
		}
		else {
			if (currentSUDatatype.getParentStructNode() == null) {
				observation.setAdvise(observation.getAdvise() + "\tDas Switch-Statement befindet sich innerhalb einer Klasse oder Struktur, die nicht von einer Elternklasse- oder Struktur, erbt. Switch-Statements sollten nur in Klassen oder Strukturen verwendet werden, die erben. So können vererbte Methoden in den Switch-Statements angewendet werden.\n");
			}
			else {
				cleanCodeScore += 1;
				observation.setAdvise(null);
			}
		}
		observation.setCleanCodeObservationScore(observation.getCleanCodeObservationScore() + cleanCodeScore);
		observation.setHighestPossibleScoreForThisObservation(observation.getHighestPossibleScoreForThisObservation() + 1);
	}

	public static void analyzeTryCatchStmtInsideMethod(CompileProcess compileProcess, Token<Object> nameToken, Node<Object> functionNode, Node<Object> bodyNode) {
		String identifier = (String) nameToken.getValue();
		CleanCodeObservation<Object> observation = new CleanCodeObservation<Object>(CleanCodeAnalysisBroadCategory.METHOD_ANALYSIS, CleanCodeAnalysisConcreteCategory.STMT_ANALYSIS, nameToken.getPos(), functionNode);
		compileProcess.getCleanCodeObservations().vectorPush(observation);
		int cleanCodeScore = 0;
		@SuppressWarnings("unchecked")
		Body<Object> body = (Body<Object>) bodyNode.getValue();
		if (body.getStatements().vectorCount() == 1) {
			cleanCodeScore += 1;
			observation.setAdvise(null);
		}
		else  {
			@SuppressWarnings("unchecked")
			Function<Object> func = (Function<Object>) functionNode.getValue();
			String tildeToAddIfDestructor = CleanCodeAnalyzer.checkForTildeToAdd(func.getFlags());
			observation.setAdvise(observation .getAdvise() + "\tDie Funktion oder Methode \"" + tildeToAddIfDestructor + identifier + "\" nutzt einen Try-/Catch-Block. Dabei sollte try das erste Wort im Funktionskörper sein und nach dem catch-Block sollte der Funktionskörper enden.\n");
		}
		observation.setCleanCodeObservationScore(observation.getCleanCodeObservationScore() + cleanCodeScore);
		observation.setHighestPossibleScoreForThisObservation(observation.getHighestPossibleScoreForThisObservation() + 1);
	}

	public static void analyzeGoto(CompileProcess compileProcess, Token<Object> keywordToken, Node<Object> currentFuncNode, Node<Object> gotoNode) {
		int cleanCodeScore = 0;
		// Ist das goto-Statement in einer Methode, bekommen Sie für den Methodenscore einen Punkt abgezogen je goto-Statement. Sonst für den allgemeinen Score.
		if (currentFuncNode != null) {
			@SuppressWarnings("unchecked")
			Function<Object> func = (Function<Object>) currentFuncNode.getValue();
			String identifier = func.getName();
			CleanCodeObservation<Object> observation = new CleanCodeObservation<Object>(CleanCodeAnalysisBroadCategory.METHOD_ANALYSIS, CleanCodeAnalysisConcreteCategory.STMT_ANALYSIS, keywordToken.getPos(), gotoNode);
			compileProcess.getCleanCodeObservations().vectorPush(observation);
			// Wir ziehen für jedes Goto einen Punkt ab.
			cleanCodeScore -= 1;
			String tildeToAddIfDestructor = "";
			if (func.getFlags() == FunctionFlag.FUNCTION_NODE_FLAG_IS_NATIVE_DESTRUCTOR) {
				tildeToAddIfDestructor += "~";
			}
			observation.setAdvise(observation .getAdvise() + "\tDie Funktion oder Methode \"" + tildeToAddIfDestructor + identifier + "\" nutzt ein goto-Statement. Diese sollten nach Möglichkeit nicht verwendet werden.\n");
			observation.setCleanCodeObservationScore(observation.getCleanCodeObservationScore() + cleanCodeScore);
			observation.setHighestPossibleScoreForThisObservation(0);
		}
		else {
			CleanCodeObservation<Object> observation = new CleanCodeObservation<Object>(CleanCodeAnalysisBroadCategory.GLOBAL_ANALYSIS, CleanCodeAnalysisConcreteCategory.STMT_ANALYSIS, keywordToken.getPos(), gotoNode);
			compileProcess.getCleanCodeObservations().vectorPush(observation);
			// Wir ziehen für jedes Goto einen Punkt ab.
			cleanCodeScore -= 1;
			observation.setAdvise(observation .getAdvise() + "\tEs wird ein goto-Statement genutzt. Diese sollten nach Möglichkeit nicht verwendet werden.\n");
			observation.setCleanCodeObservationScore(observation.getCleanCodeObservationScore() + cleanCodeScore);
			observation.setHighestPossibleScoreForThisObservation(0);
		}
	}

	// Diese Methode analysiert Methoden mit einem Struct Rückgabetyp, ob im aktuellen return-Statement "NULL" oder "nullptr" zurückgegeben wird.
	public static void analyzeReturnStatementInStructMethod(CompileProcess compileProcess, Token<Object> keywordToken, Node<Object> returnNode, Node<Object> expNode, Node<Object> currentFunctionNode) {
		// Befinden wir uns in einer nicht-Structfunktion, macht das Überprüfen keinen Sinn, da wir nur nach einer eventuellen NULL-Rückgabe testen.
		if (currentFunctionNode == null) {
			return;
		}
		@SuppressWarnings("unchecked")
		Function<Object> func = (Function<Object>) currentFunctionNode.getValue();
		// Sonderfall: Destruktor.
		if (func.getRtype() == null || func.getRtype().getStructOrUnionNode() != null) {
			CleanCodeObservation<Object> observation = new CleanCodeObservation<Object>(CleanCodeAnalysisBroadCategory.METHOD_ANALYSIS, CleanCodeAnalysisConcreteCategory.STMT_ANALYSIS, keywordToken.getPos(), returnNode);
			compileProcess.getCleanCodeObservations().vectorPush(observation);
			int cleanCodeScore = 0;
			boolean foundNullPtr = false;
			if (expNode.getType() == NodeType.NODE_TYPE_IDENTIFIER) {
				String possibleNullPtr = (String) expNode.getValue();
				if (SymbolTable.S_EQ(possibleNullPtr, "NULL") || SymbolTable.S_EQ(possibleNullPtr, "nullptr")) {
					foundNullPtr = true;
				}
			}
			if (!(foundNullPtr)) {
				cleanCodeScore += 1;
				observation.setAdvise(null);
			}
			else {
				String tildeToAddIfDestructor = CleanCodeAnalyzer.checkForTildeToAdd(func.getFlags());
				observation.setAdvise(observation.getAdvise() + "\tIn der Funktion oder Mehtode \"" + tildeToAddIfDestructor + func.getName() + "\" wird ein return-Statement verwendet. NULL als Rückgabewert sollte in einer Funktion oder Methode vermieden werden.\n");
			}
			observation.setCleanCodeObservationScore(observation.getCleanCodeObservationScore() + cleanCodeScore);
			observation.setHighestPossibleScoreForThisObservation(observation.getHighestPossibleScoreForThisObservation() + 1);
		}
	}

	public static void analyzeWhitespacesAroundAssignmentOperator(CompileProcess compileProcess, Integer assignmentTokenIndexInTokenVec, Token<Object> nameToken, Node<Object> varNode) {
		String identifier = (String) nameToken.getValue();
		CleanCodeObservation<Object> observation = new CleanCodeObservation<Object>(CleanCodeAnalysisBroadCategory.VARIABLE_ANALYSIS, CleanCodeAnalysisConcreteCategory.ASSIGNMENT_ANALYSIS, nameToken.getPos(), varNode);
		compileProcess.getCleanCodeObservations().vectorPush(observation);
		int cleanCodeScore = 0;
		Vector<Token<Object>> tokenVec = compileProcess.getTokenVec();
		Token<Object> tokenBeforeAssignmentToken = tokenVec.vectorPeekPtrAt(assignmentTokenIndexInTokenVec - 1);
		Token<Object> assignmentToken = tokenVec.vectorPeekPtrAt(assignmentTokenIndexInTokenVec);
		if (tokenBeforeAssignmentToken.getWhitespaces() == 1 && assignmentToken.getWhitespaces() == 1) {
			cleanCodeScore += 1;
			observation.setAdvise(null);
		}
		else {
			observation.setAdvise(observation.getAdvise() + "\tBei der Zuweisungsanweisung bezüglich der Variable \"" + identifier + "\" sollte ein Leerzeichen links und rechts des Zuweisungsoperators verwendet werden, um die Teilung der Anweisung in zwei Elemente, links und rechts der Zuweisung, zu betonen.\n");
		}
		observation.setCleanCodeObservationScore(observation.getCleanCodeObservationScore() + cleanCodeScore);
		observation.setHighestPossibleScoreForThisObservation(observation.getHighestPossibleScoreForThisObservation() + 1);
	}

	// Diese Methode schaut, dass im Dateikopf zuerst die #include-Direktiven stehen und dann die definierten Namensräume.
	public static void analyzeFileHead(CompileProcess compileProcess) {
		Vector<Node<Object>> nodeTreeVec = compileProcess.getNodeTreeVec();
		CleanCodeObservation<Object> observation = new CleanCodeObservation<Object>(CleanCodeAnalysisBroadCategory.GLOBAL_ANALYSIS, CleanCodeAnalysisConcreteCategory.FILE_HEAD_ANALYSIS, null, null);
		compileProcess.getCleanCodeObservations().vectorPush(observation);
		int cleanCodeScore = 0;
		boolean foundAtLeastOneLibraryDirective = false;
		boolean libraryDirectivesAreAtTopOfFile = true;
		boolean foundSomethingElseThanLibraryDirective = false;
		for(int i = 0; i < nodeTreeVec.vectorCount(); i++) {
			Node<Object> node = nodeTreeVec.vectorPeekPtrAt(i);
			if (node.getType() != NodeType.NODE_TYPE_DIRECTIVE_LIBRARY) {
				foundSomethingElseThanLibraryDirective = true;
			}
			else {
				if (foundSomethingElseThanLibraryDirective) {
					libraryDirectivesAreAtTopOfFile = false;
					break;
				}
				if (!(foundAtLeastOneLibraryDirective)) {
					foundAtLeastOneLibraryDirective = true;
				}
			}
		}
		boolean foundAtLeastOneNamespaceDefinition = false;
		boolean namespaceDefinitionsAreAtTopOfFile = true;
		boolean foundSomethingElseThanNamespaceDefinition = false;
		for(int i = 0; i < nodeTreeVec.vectorCount(); i++) {
			Node<Object> node = nodeTreeVec.vectorPeekPtrAt(i);
			if (node.getType() == NodeType.NODE_TYPE_DIRECTIVE_LIBRARY) {
				continue;
			}
			else if (node.getType() != NodeType.NODE_TYPE_NAMESPACE) {
				foundSomethingElseThanNamespaceDefinition = true;
			}
			else {
				if (foundSomethingElseThanNamespaceDefinition) {
					namespaceDefinitionsAreAtTopOfFile = false;
					break;
				}
				if (!(foundAtLeastOneNamespaceDefinition)) {
					foundAtLeastOneNamespaceDefinition = true;
				}
			}
		}
		if (foundAtLeastOneLibraryDirective) {
			if (libraryDirectivesAreAtTopOfFile) {
				cleanCodeScore += 1;
			}
			else {
				observation.setAdvise(observation.getAdvise() + "Es sind nicht alle Bibliotheken, die verwendet werden, im Dateikopf angegeben. Sauberer wäre es, auf das Importieren von Bibliothek abseits des Dateikopfes zu verzichten.\n");
			}
			observation.setCleanCodeObservationScore(observation.getCleanCodeObservationScore() + 1);
			observation.setHighestPossibleScoreForThisObservation(observation.getHighestPossibleScoreForThisObservation() + 1);
		}
		else {
			observation.setCleanCodeObservationScore(observation.getCleanCodeObservationScore());
			observation.setHighestPossibleScoreForThisObservation(observation.getHighestPossibleScoreForThisObservation() + 0);
		}
		if (foundAtLeastOneNamespaceDefinition) {
			if (namespaceDefinitionsAreAtTopOfFile) {
				cleanCodeScore += 1;
			}
			else {
				observation.setAdvise(observation.getAdvise() + "Es sind nicht alle Namensräume, die definiert werden, im Dateikopf angegeben. Sauberer wäre es, auf das Importieren von Bibliothek abseits des Dateikopfes zu verzichten und die Definition von Namensräumen unter den Anweisungen zur Einbindung von Bibliotheken anzugeben.\n");
			}
			observation.setCleanCodeObservationScore(observation.getCleanCodeObservationScore() + 1);
			observation.setHighestPossibleScoreForThisObservation(observation.getHighestPossibleScoreForThisObservation() + 1);
		}
		else {
			observation.setCleanCodeObservationScore(observation.getCleanCodeObservationScore());
			observation.setHighestPossibleScoreForThisObservation(observation.getHighestPossibleScoreForThisObservation() + 0);
		}
		if (observation.getCleanCodeObservationScore() == observation.getHighestPossibleScoreForThisObservation()) {
			observation.setAdvise(null);
		}
	}

	public static void handlePerfectLibaryDirectivePlacing(CleanCodeObservation<Object> libraryDirectivesObservation) {
		if (libraryDirectivesObservation != null && libraryDirectivesObservation.getCleanCodeObservationScore() == libraryDirectivesObservation.getHighestPossibleScoreForThisObservation()) {
			libraryDirectivesObservation.setAdvise(null);
		}
	}
	
	public static void handlePerfectNamespaceDefinitionsPlacing(CleanCodeObservation<Object> namespaceDefinitionsObservation) {
		if (namespaceDefinitionsObservation != null && namespaceDefinitionsObservation.getCleanCodeObservationScore() == namespaceDefinitionsObservation.getHighestPossibleScoreForThisObservation()) {
			namespaceDefinitionsObservation.setAdvise(null);
		}
	}
	
	public static CleanCodeObservation<Object> analyzeLibraryDirectiveRegardingNewlines(CompileProcess compileProcess, CleanCodeObservation<Object> observation, Integer hashTokenVecIndex, String identifier, Node<Object> libraryNode) {
		// Haben wir bereits eine Leerzeile vor einer Bibliotheksdirektive gefunden, so gibt es keine Punkte mehr zu vergeben.
		if (CleanCodeAnalyzer.foundNewlineTokenBeforeLibraryDirective) {
			return observation;
		}
		Vector<Token<Object>> tokenVec = compileProcess.getTokenVec();
		Token<Object> hashToken = tokenVec.vectorPeekPtrAt(hashTokenVecIndex);
		if (CleanCodeAnalyzer.isFirstLibraryDirective) {
			observation = new CleanCodeObservation<Object>(CleanCodeAnalysisBroadCategory.GLOBAL_ANALYSIS, CleanCodeAnalysisConcreteCategory.FILE_HEAD_ANALYSIS, hashToken.getPos(), libraryNode);
			compileProcess.getCleanCodeObservations().vectorPush(observation);
		}
		else {
			observation.setPos(hashToken.getPos());
			observation.setEntity(libraryNode);
			observation.setAdvise("In Zeile " + hashToken.getPos().getLineNumber() + ", Spalte " + hashToken.getPos().getColNumber() + ":\n");
		}
		if (hashTokenVecIndex - 2 > 0) {
			Token<Object> possibleNewlineToken = tokenVec.vectorPeekPtrAt(hashTokenVecIndex);
			if (possibleNewlineToken.getType() == TokenType.TOKEN_TYPE_NEWLINE) {
				CleanCodeAnalyzer.foundNewlineTokenBeforeLibraryDirective = true;
			}
		}
		if (!(CleanCodeAnalyzer.foundNewlineTokenBeforeLibraryDirective)) {
			observation.setCleanCodeObservationScore(1);
		}
		else {
			observation.setCleanCodeObservationScore(0);
			observation.setAdvise(observation.getAdvise() + "\tVor der Bibliotheksdirektive \"" + identifier + "\" befindet sich eine Leerzeile. Sauberer wäre es, auf diese Leerzeile zu verzichten.\n");
		}
		if (CleanCodeAnalyzer.isFirstLibraryDirective) {
			observation.setHighestPossibleScoreForThisObservation(1);
			CleanCodeAnalyzer.isFirstLibraryDirective = false;
		}
		return observation;
	}
	
	public static CleanCodeObservation<Object> analyzeNamespaceDefinitionsRegardingNewlines(CompileProcess compileProcess, CleanCodeObservation<Object> observation, Integer startTokenVecIndex, String identifier, Node<Object> namespaceNode) {
		// Haben wir bereits eine Leerzeile vor einer Namensraumdefinition gefunden (außer erste Zeile), so gibt es keine Punkte mehr zu vergeben.
		if (CleanCodeAnalyzer.foundNewlineTokenBeforeNamespaceDefinition) {
			return observation;
		}
		Vector<Token<Object>> tokenVec = compileProcess.getTokenVec();
		Token<Object> startToken = tokenVec.vectorPeekPtrAt(startTokenVecIndex);
		if (CleanCodeAnalyzer.isFirstNamespaceDefinition) {
			observation = new CleanCodeObservation<Object>(CleanCodeAnalysisBroadCategory.GLOBAL_ANALYSIS, CleanCodeAnalysisConcreteCategory.FILE_HEAD_ANALYSIS, startToken.getPos(), namespaceNode);
			compileProcess.getCleanCodeObservations().vectorPush(observation);
		}
		else {
			observation.setPos(startToken.getPos());
			observation.setEntity(namespaceNode);
			observation.setAdvise("In Zeile " + startToken.getPos().getLineNumber() + ", Spalte " + startToken.getPos().getColNumber() + ":\n");
		}
		if (startTokenVecIndex - 2 > 0 && !(CleanCodeAnalyzer.isFirstNamespaceDefinition)) {
			Token<Object> possibleNewlineToken = tokenVec.vectorPeekPtrAt(startTokenVecIndex);
			if (possibleNewlineToken.getType() == TokenType.TOKEN_TYPE_NEWLINE) {
				CleanCodeAnalyzer.foundNewlineTokenBeforeNamespaceDefinition = true;
			}
		}
		if (!(CleanCodeAnalyzer.foundNewlineTokenBeforeNamespaceDefinition)) {
			observation.setCleanCodeObservationScore(1);
		}
		else {
			observation.setCleanCodeObservationScore(0);
			observation.setAdvise(observation.getAdvise() + "\tVor der Definition des Namensraumes \"" + identifier + "\" befindet sich eine Leerzeile. Sauberer wäre es, auf diese Leerzeile zu verzichten.\n");
		}
		if (CleanCodeAnalyzer.isFirstNamespaceDefinition) {
			CleanCodeAnalyzer.isFirstNamespaceDefinition = false;
			observation.setHighestPossibleScoreForThisObservation(1);
		}
		return observation;
	}
	
	private static int checkTokenVecIndexForAccessSpecifierNamespaceAndGenerics(CompileProcess compileProcess, int currentStartTokenVecIndex) {
		Token<Object> priorToken = null;
		Vector<Token<Object>> tokenVec = compileProcess.getTokenVec();
		Token<Object> token = tokenVec.vectorPeekPtrAt(currentStartTokenVecIndex + 1);
		if (currentStartTokenVecIndex > 0) {
			if (Token.tokenIsOperator(token, ">")) {
				while(!(Token.tokenIsOperator(token, "<"))) {
					currentStartTokenVecIndex--;
					token = tokenVec.vectorPeekPtrAt(currentStartTokenVecIndex);
				}
				currentStartTokenVecIndex--;
			}
			priorToken = tokenVec.vectorPeekPtrAt(currentStartTokenVecIndex - 1);
			if (Token.tokenIsOperator(priorToken, "::")) {
				currentStartTokenVecIndex -= 2;
				priorToken = tokenVec.vectorPeekPtrAt(currentStartTokenVecIndex - 1);
			}
			if (priorToken.getType() == TokenType.TOKEN_TYPE_KEYWORD && SymbolTable.keywordIsAccessSpecifier((String) priorToken.getValue())) {
				return currentStartTokenVecIndex - 1;
			}
		}
		return currentStartTokenVecIndex;
	}
	
	public static void analyzeGlobalScopeWhitespacingAndNewLines(CompileProcess compileProcess) {
		int cleanCodeScore = 0;
		CleanCodeObservation<Object> observation = new CleanCodeObservation<Object>(CleanCodeAnalysisBroadCategory.GLOBAL_ANALYSIS, CleanCodeAnalysisConcreteCategory.VERTICAL_DENSITY_ANALYSIS, null, null);
		compileProcess.getCleanCodeObservations().vectorPush(observation);
		boolean noNewlineBeforeVariable = true;
		boolean alwaysNewlineBeforeFunction = true;
		boolean alwaysNewlineBeforeStruct = true;
		boolean firstVariableWasFound = false;
		boolean firstFunctionWasFound = false;
		boolean firstStructWasFound = false;
		boolean firstLineVariable = false;
		Token<Object> possibleNewlineTokenToCheck = null;
		Vector<Node<Object>> nodeTreeVec = compileProcess.getNodeTreeVec();
		Vector<Token<Object>> tokenVec = compileProcess.getTokenVec();
		for(int i = 0; i < nodeTreeVec.vectorCount(); i++) {
			Node<Object> node = nodeTreeVec.vectorPeekPtrAt(i);
			if (node.getType() == NodeType.NODE_TYPE_VARIABLE || node.getType() == NodeType.NODE_TYPE_VARIABLE_LIST) {
				if (node.getType() == NodeType.NODE_TYPE_VARIABLE) {
					@SuppressWarnings("unchecked")
					Var<Object> var = (Var<Object>) node.getValue();
					int startTokenVecIndex = CleanCodeAnalyzer.checkTokenVecIndexForAccessSpecifierNamespaceAndGenerics(compileProcess, var.getType().getStartTokenVecIndex());
					if (startTokenVecIndex > 1) {
						possibleNewlineTokenToCheck = tokenVec.vectorPeekPtrAt(startTokenVecIndex - 2);
					}
					if (possibleNewlineTokenToCheck == null || !(possibleNewlineTokenToCheck.getType() == TokenType.TOKEN_TYPE_NEWLINE)) {
						noNewlineBeforeVariable = false;
					}
					if (possibleNewlineTokenToCheck == null) {
						firstLineVariable = true;
					}
				}
				else if (node.getType() == NodeType.NODE_TYPE_VARIABLE_LIST) {
					@SuppressWarnings("unchecked")
					VarList<Object> varList = (VarList<Object>) node.getValue();
					@SuppressWarnings("unchecked")
					Var<Object> var = (Var<Object>) varList.getList().vectorPeekPtrAt(0).getValue();
					int startTokenVecIndex = CleanCodeAnalyzer.checkTokenVecIndexForAccessSpecifierNamespaceAndGenerics(compileProcess, var.getType().getStartTokenVecIndex());
					if (startTokenVecIndex > 1) {
						possibleNewlineTokenToCheck = tokenVec.vectorPeekPtrAt(startTokenVecIndex - 2);
					}
					if (possibleNewlineTokenToCheck == null || !(possibleNewlineTokenToCheck.getType() == TokenType.TOKEN_TYPE_NEWLINE)) {
						noNewlineBeforeVariable = false;
					}
					if (possibleNewlineTokenToCheck == null) {
						firstLineVariable = true;
					}
				}
				firstVariableWasFound = true;
			}
			// Bei Funktionen wollen wir vor jeder (definierten) Funktion eine leere Zeile sehen, um die Funktion optisch abzutrennen.
			else if (node.getType() == NodeType.NODE_TYPE_FUNCTION) {
				@SuppressWarnings("unchecked")
				Function<Object> func = (Function<Object>) node.getValue();
				int startTokenVecIndex = CleanCodeAnalyzer.checkTokenVecIndexForAccessSpecifierNamespaceAndGenerics(compileProcess, func.getStartTokenVecIndex());
				if (startTokenVecIndex > 1) {
					possibleNewlineTokenToCheck = tokenVec.vectorPeekPtrAt(startTokenVecIndex - 2);
				}
				if (possibleNewlineTokenToCheck == null || possibleNewlineTokenToCheck.getType() != TokenType.TOKEN_TYPE_NEWLINE) {
					alwaysNewlineBeforeFunction = false;
				}
				firstFunctionWasFound = true;
			}
			// Das gleiche bei Strukturen bzw. Klassen.
			else if (node.getType() == NodeType.NODE_TYPE_STRUCT) {
				@SuppressWarnings("unchecked")
				Struct<Object> struct = (Struct<Object>) node.getValue();
				int startTokenVecIndex = CleanCodeAnalyzer.checkTokenVecIndexForAccessSpecifierNamespaceAndGenerics(compileProcess, struct.getDtype().getStartTokenVecIndex() - 1);
				if (startTokenVecIndex > 1) {
					possibleNewlineTokenToCheck = tokenVec.vectorPeekPtrAt(startTokenVecIndex - 2);
				}
				if (possibleNewlineTokenToCheck == null || possibleNewlineTokenToCheck.getType() != TokenType.TOKEN_TYPE_NEWLINE) {
					alwaysNewlineBeforeStruct = false;
				}
				firstStructWasFound = true;
			}
		}
		observation.setHighestPossibleScoreForThisObservation(observation.getHighestPossibleScoreForThisObservation() + 1);
		if (firstVariableWasFound && noNewlineBeforeVariable) {
			cleanCodeScore += 1;
		}
		else if (firstVariableWasFound && !(noNewlineBeforeVariable) && firstLineVariable) {
			cleanCodeScore += 1;
		}
		else if (!(firstVariableWasFound)) {
			observation.setHighestPossibleScoreForThisObservation(observation.getHighestPossibleScoreForThisObservation() - 1);
		}
		else {
			observation.setAdvise(observation.getAdvise() + "Es wurden globale Variablen gefunden (außer der ersten globalen Variable), hinter der sich eine Leerzeile befindet. Sauberer wäre es, keine Leerzeilen vor der Initialisierung von globalen Variablen zu verwenden (außer der ersten Variable) und sie in einem Block nach den Bibliotheksdirektiven und den Definitionen von Namensräumen zu deklarieren.\n");
		}
		observation.setHighestPossibleScoreForThisObservation(observation.getHighestPossibleScoreForThisObservation() + 1);
		if (firstFunctionWasFound && alwaysNewlineBeforeFunction) {
			cleanCodeScore += 1;
		}
		else if (!(firstFunctionWasFound)) {
			observation.setHighestPossibleScoreForThisObservation(observation.getHighestPossibleScoreForThisObservation() - 1);
		}
		else {
			observation.setAdvise(observation.getAdvise() + "Es wurden globale Funktionen gefunden, hinter der sich keine Leerzeile befindet. Sauberer wäre es, eine Leerzeile vor der Definition von globalen Funktionen zu verwenden.\n");
		}
		observation.setHighestPossibleScoreForThisObservation(observation.getHighestPossibleScoreForThisObservation() + 1);
		if (firstStructWasFound && alwaysNewlineBeforeStruct) {
			cleanCodeScore += 1;
		}
		else if (!(firstStructWasFound)) {
			observation.setHighestPossibleScoreForThisObservation(observation.getHighestPossibleScoreForThisObservation() - 1);
		}
		else {
			observation.setAdvise(observation.getAdvise() + "Es wurden globale Strukturen oder Klassen gefunden, hinter der sich keine Leerzeile befindet. Sauberer wäre es, eine Leerzeile vor der Definition von globalen Strukturen oder Klassen zu verwenden.\n");
		}
		observation.setCleanCodeObservationScore(cleanCodeScore);
		if (observation.getCleanCodeObservationScore() == observation.getHighestPossibleScoreForThisObservation()) {
			observation.setAdvise(null);
		}
	}

	public static void analyzeFormattingOnGlobalScope(CompileProcess compileProcess, CleanCodeObservation<Object> libraryDirectivesObservation, CleanCodeObservation<Object> namespaceDefnitionsObservation, CleanCodeObservation<Object> globalDefinedMethodFromStructObservation) {
		CleanCodeAnalyzer.analyzeFileHead(compileProcess);
		CleanCodeAnalyzer.analyzeGlobalScopeWhitespacingAndNewLines(compileProcess);
		CleanCodeAnalyzer.handlePerfectLibaryDirectivePlacing(libraryDirectivesObservation);
		CleanCodeAnalyzer.handlePerfectNamespaceDefinitionsPlacing(namespaceDefnitionsObservation);
		CleanCodeAnalyzer.handlePerfectGlobalDefinedMethodFromStructFormatting(globalDefinedMethodFromStructObservation);
	}

	private static void handlePerfectGlobalDefinedMethodFromStructFormatting(CleanCodeObservation<Object> observation) {
		if (observation == null) {
			return;
		}
		if (CleanCodeAnalyzer.foundNewlineTokenBeforeGlobalDefinedFunctionFromOutstideStruct) {
			observation.setAdvise(null);
		}
		else if (observation.getCleanCodeObservationScore() == observation.getHighestPossibleScoreForThisObservation()) {
			observation.setAdvise(null);
		}
	}

	// Diese Methode wurde außerhalb einer Klasse definiert und dann einer Forward-Declaration in einer Klasse hinzugefügt.
	public static CleanCodeObservation<Object> analyzeFormattingForGlobalDefinedStructMethod(CompileProcess compileProcess, Node<Object> outsideStructDefinedMethodNode, CleanCodeObservation<Object> observation) {
		if (!(CleanCodeAnalyzer.foundNewlineTokenBeforeGlobalDefinedFunctionFromOutstideStruct)) {
			return observation;
		}
		int cleanCodeScore = 0;
		@SuppressWarnings("unchecked")
		Function<Object> outsideStructDefinedMethod = (Function<Object>) outsideStructDefinedMethodNode.getValue();
		int startTokenVecIndex = CleanCodeAnalyzer.checkTokenVecIndexForAccessSpecifierNamespaceAndGenerics(compileProcess, outsideStructDefinedMethod.getStartTokenVecIndex());
		Vector<Token<Object>> tokenVec = compileProcess.getTokenVec();
		Token<Object> startToken = tokenVec.vectorPeekPtrAt(startTokenVecIndex);
		if (CleanCodeAnalyzer.isFirstGlobalDefinedFunctionFromOutsideStruct) {
			observation = new CleanCodeObservation<Object>(CleanCodeAnalysisBroadCategory.GLOBAL_ANALYSIS, CleanCodeAnalysisConcreteCategory.VERTICAL_DENSITY_ANALYSIS, startToken.getPos(), outsideStructDefinedMethodNode);
			compileProcess.getCleanCodeObservations().vectorPush(observation);
			CleanCodeAnalyzer.isFirstGlobalDefinedFunctionFromOutsideStruct = false;
		}
		Token<Object> possibleNewlineToken = null;
		if (startTokenVecIndex > 1) {
			possibleNewlineToken = tokenVec.vectorPeekPtrAt(startTokenVecIndex - 2);
		}
		if (possibleNewlineToken == null || possibleNewlineToken.getType() == TokenType.TOKEN_TYPE_NEWLINE) {
			cleanCodeScore += 1;
		}
		else {
			CleanCodeAnalyzer.foundNewlineTokenBeforeGlobalDefinedFunctionFromOutstideStruct = false;
			String tildeToAddIfDestructor = "";
			if (outsideStructDefinedMethod.getFlags() == FunctionFlag.FUNCTION_NODE_FLAG_IS_NATIVE_DESTRUCTOR) {
				tildeToAddIfDestructor += "~";
			}
			observation.setAdvise(observation.getAdvise() + "\tDie global definierte Funktion oder Methode \"" + tildeToAddIfDestructor + outsideStructDefinedMethod.getName() + "\" besitzt keine Leerzeile vor sich. Sauberer wäre es, vor jeder definierten Methode oder Funktion eine Leerzeile zur visuellen Abgrenzung zu verwenden.\n");
		}
		if (!(CleanCodeAnalyzer.foundNewlineTokenBeforeGlobalDefinedFunctionFromOutstideStruct)) {
			observation.setCleanCodeObservationScore(0);
		}
		else {
			observation.setCleanCodeObservationScore(observation.getCleanCodeObservationScore() + cleanCodeScore);
		}
		observation.setHighestPossibleScoreForThisObservation(observation.getHighestPossibleScoreForThisObservation() + 1);
		return observation;
	}

	public static void analyzeNegativeUnaryOperator(CompileProcess compileProcess, Token<Object> unaryOpToken, Node<Object> unaryNode, Datatype<Node<Object>> currentSUDatatype, Node<Object> currentFunction) {
		CleanCodeObservation<Object> observation = null;
		if (currentSUDatatype != null) {
			observation = new CleanCodeObservation<Object>(CleanCodeAnalysisBroadCategory.CLASS_ANALYSIS, CleanCodeAnalysisConcreteCategory.EXPRESSION_ANALYSIS, unaryOpToken.getPos(), unaryNode);
		}
		else if (currentFunction != null) {
			observation = new CleanCodeObservation<Object>(CleanCodeAnalysisBroadCategory.METHOD_ANALYSIS, CleanCodeAnalysisConcreteCategory.EXPRESSION_ANALYSIS, unaryOpToken.getPos(), unaryNode);
		}
		else {
			observation = new CleanCodeObservation<Object>(CleanCodeAnalysisBroadCategory.GLOBAL_ANALYSIS, CleanCodeAnalysisConcreteCategory.EXPRESSION_ANALYSIS, unaryOpToken.getPos(), unaryNode);
		}
		compileProcess.getCleanCodeObservations().vectorPush(observation);
		observation.setAdvise(observation.getAdvise() + "\tStatt eines negativen Vergleichs sollte immer ein Methodenaufruf mit einem aussagekräftigen Namen verwendet werden.\n");
		observation.setCleanCodeObservationScore(-1);
		observation.setHighestPossibleScoreForThisObservation(0);
	}
	
	private static void analyzeFunctionCallByDepthFirstSearch(FunctionCall functionCall, Node<Object> node) {
		if (node != null && node.getType() != NodeType.NODE_TYPE_BLANK) {
			if (node.getType() == NodeType.NODE_TYPE_EXPRESSION) {
				@SuppressWarnings("unchecked")
				Expression<Object> exp = (Expression<Object>) node.getValue();
				CleanCodeAnalyzer.analyzeFunctionCallByDepthFirstSearch(functionCall, exp.getLeft());
				CleanCodeAnalyzer.analyzeFunctionCallByDepthFirstSearch(functionCall, exp.getRight());
			}
			else if (node.getType() == NodeType.NODE_TYPE_IDENTIFIER) {
				boolean setIdentifier = false;
				String identifier = (String) node.getValue();
				if (functionCall.getIdentifier() == null) {
					functionCall.setIdentifier(identifier);
					setIdentifier = true;
					if (CleanCodeAnalyzer.parsedConstructorCall) {
						functionCall.setFunctionFlag(FunctionFlag.FUNCTION_NODE_FLAG_IS_NATIVE_CONSTRUCTOR);
						CleanCodeAnalyzer.parsedConstructorCall = false;
					}
					else {
						functionCall.setFunctionFlag(FunctionFlag.FUNCTION_NODE_FLAG_IS_NATIVE);
					}
				}
				else if (SymbolTable.S_EQ(identifier, "NULL") || SymbolTable.S_EQ(identifier, "nullptr")) {
					functionCall.setNumberOfNullValues(functionCall.getNumberOfNullValues() + 1);
				}
				if (!(setIdentifier)) {
					functionCall.setNumberOfArguments(functionCall.getNumberOfArguments() + 1);
				}
			}
			else if (node.getType() == NodeType.NODE_TYPE_EXPRESSION_PARENTHESES) {
				@SuppressWarnings("unchecked")
				Parenthesis<Object> parenthesis = (Parenthesis<Object>) node.getValue();
				CleanCodeAnalyzer.analyzeFunctionCallByDepthFirstSearch(functionCall, parenthesis.getExp());
			}
		}
	}

	public static void analyzeNullValuesInMethodCalls(CompileProcess compileProcess, Token<Object> parenthesisToken, Node<Object> expNode, Datatype<Node<Object>> currentSUDatatype, Node<Object> currentFunction) {
		FunctionCall functionCall = new FunctionCall();
		if (CleanCodeAnalyzer.calledFunctionIdentifierVec == null) {
			CleanCodeAnalyzer.calledFunctionIdentifierVec = Vector.vectorCreate();
		}
		CleanCodeAnalyzer.calledFunctionIdentifierVec.vectorPush(functionCall);
		CleanCodeAnalyzer.analyzeFunctionCallByDepthFirstSearch(functionCall, expNode);	
		if (functionCall.getNumberOfNullValues() > 0) {
			CleanCodeObservation<Object> observation = null;
			if (currentSUDatatype != null) {
				observation = new CleanCodeObservation<Object>(CleanCodeAnalysisBroadCategory.CLASS_ANALYSIS, CleanCodeAnalysisConcreteCategory.EXPRESSION_ANALYSIS, parenthesisToken.getPos(), expNode);
			}
			else {
				observation = new CleanCodeObservation<Object>(CleanCodeAnalysisBroadCategory.METHOD_ANALYSIS, CleanCodeAnalysisConcreteCategory.EXPRESSION_ANALYSIS, parenthesisToken.getPos(), expNode);
			}
			compileProcess.getCleanCodeObservations().vectorPush(observation);
			String tildeToAddIfDestructor = CleanCodeAnalyzer.checkForTildeToAdd(functionCall.getFunctionFlag());
			observation.setAdvise(observation.getAdvise() + "\tAn die Methode oder Funktion \"" + tildeToAddIfDestructor + functionCall.getIdentifier() + "\" erhält " + functionCall.getNumberOfNullValues() + " null-Werte. \"NULL\" oder \"nullptr\" sollte nicht als Übergabeparameter für Funktions- oder Methodenaufrufe verwendet werden. Die Übergabe von null-Werten führt zu vermehrten NullPointerExceptions.\n");
			observation.setCleanCodeObservationScore(-1);
			observation.setHighestPossibleScoreForThisObservation(0);
		}
	}
	
	// Methode prüft ob Methode mit dem Methodennamen und der gleichen Anzahl von Parametern genutzt wird, da überladene Methoden zu testen zu schwierig ist.
	private static void analyzeUsageOfMethods(CompileProcess compileProcess, Vector<Node<Object>> functionNodeVec) {
		Vector<FunctionCall> calledFunctionIdentifierVec = CleanCodeAnalyzer.calledFunctionIdentifierVec;
		if (calledFunctionIdentifierVec == null) {
			calledFunctionIdentifierVec = Vector.vectorCreate();
		}
		if (functionNodeVec == null) {
			return;
		}
		Vector<Token<Object>> tokenVec = compileProcess.getTokenVec();
		for(int i = 0; i < functionNodeVec.vectorCount(); i++) {
			Node<Object> funcNode = functionNodeVec.vectorPeekPtrAt(i);
			@SuppressWarnings("unchecked")
			Function<Object> func = (Function<Object>) funcNode.getValue();
			boolean functionHasBeenUsed = false;
			for(int j = 0; j < calledFunctionIdentifierVec.vectorCount(); j++) {
				FunctionCall functionCall = calledFunctionIdentifierVec.vectorPeekPtrAt(j);
				if (SymbolTable.S_EQ(functionCall.getIdentifier(), func.getName()) && functionCall.getNumberOfArguments() == func.getArgs().getVector().vectorCount() && functionCall.getFunctionFlag() == func.getFlags()) {
					functionHasBeenUsed = true;
				}
			}
			if (!(functionHasBeenUsed)) {
				int functionStartTokenIndex = CleanCodeAnalyzer.checkTokenVecIndexForAccessSpecifierNamespaceAndGenerics(compileProcess, func.getStartTokenVecIndex());
				Token<Object> startToken = tokenVec.vectorPeekPtrAt(functionStartTokenIndex);
				CleanCodeObservation<Object> observation = new CleanCodeObservation<Object>(CleanCodeAnalysisBroadCategory.METHOD_ANALYSIS, CleanCodeAnalysisConcreteCategory.USAGE_ANALYSIS, startToken.getPos(), funcNode);
				compileProcess.getCleanCodeObservations().vectorPush(observation);
				observation.setCleanCodeObservationScore(-1);
				observation.setHighestPossibleScoreForThisObservation(0);
				String tildeToAddIfDestructor = CleanCodeAnalyzer.checkForTildeToAdd(func.getFlags());
				observation.setAdvise(observation.getAdvise() + "\tDie Funktion oder Methode \"" + tildeToAddIfDestructor + func.getName() + "\" wird nicht aufgerufen und ist damit eine sogenannte \"tote Funktion\". Methoden oder Funktionen, die nicht verwendet werden, sollten aus Platzgründen aus dem Code entfernt werden.\n");
			}
		}
	}
	
	public static void insertionSort(ArrayList<CleanCodeObservation<Object>> cleanCodeObservations){
    	CleanCodeObservation<Object> toBeInsertedValue;
    	int j;
        for(int i = 1; i < cleanCodeObservations.size(); i++) {
        	toBeInsertedValue = cleanCodeObservations.get(i);
        	j = i;
        	while (j > 0 && cleanCodeObservations.get(j - 1).compareTo(toBeInsertedValue) > 0) {
        		cleanCodeObservations.set(j, cleanCodeObservations.get(j - 1));
        		j = j - 1;
        	}
        	cleanCodeObservations.set(j, toBeInsertedValue);
        }
    }
	
	private static void sortObservationsByPosition(CompileProcess compileProcess) {
		ArrayList<CleanCodeObservation<Object>> cleanCodeObservationsAsList = new ArrayList<CleanCodeObservation<Object>>();
		Vector<CleanCodeObservation<Object>> cleanCodeObservationsAsVector = compileProcess.getCleanCodeObservations();
		for(int i = 0; i < cleanCodeObservationsAsVector.vectorCount(); i++) {
			cleanCodeObservationsAsList.add(cleanCodeObservationsAsVector.vectorPeekPtrAt(i));
		}
		CleanCodeAnalyzer.insertionSort(cleanCodeObservationsAsList);
		Vector<CleanCodeObservation<Object>> newCleanCodeObservationsAsVector = Vector.vectorCreate();
		for(int i = 0; i < cleanCodeObservationsAsList.size(); i++) {
			newCleanCodeObservationsAsVector.vectorPush(cleanCodeObservationsAsList.get(i));
		}
		compileProcess.setCleanCodeObservations(newCleanCodeObservationsAsVector);
	}

	public static void analysisAfterParsing(CompileProcess compileProcess, CleanCodeObservation<Object> noNewlinesBetweenLibraryDirectivesObservation, CleanCodeObservation<Object> noNewlinesBetweenNamespaceDefinitionsObservation, CleanCodeObservation<Object> newlineBeforeGlobalDefinedMethodFromOutsideStruct, Vector<Node<Object>> functionNodeVec) {
		CleanCodeAnalyzer.analyzeFormattingOnGlobalScope(compileProcess, noNewlinesBetweenLibraryDirectivesObservation, noNewlinesBetweenNamespaceDefinitionsObservation, newlineBeforeGlobalDefinedMethodFromOutsideStruct);
		CleanCodeAnalyzer.analyzeUsageOfMethods(compileProcess, functionNodeVec);
		CleanCodeAnalyzer.sortObservationsByPosition(compileProcess);
	}
}
