package lexer;

import compiler.CompileProcess;
import compiler.Compiler;
import helpers.Buffer;

import java.util.function.Predicate;
import java.io.IOException;
import java.lang.Object;

public class Lexer<T> {
	public static LexProcess<Object> lexProcess;
	public static final char EOF = 65535;
	public static Token<Object> tmpToken;
	public static boolean handleColon = false;
	
	public static Character LEX_GETC_IF(Buffer buffer, Character c, Predicate<Character> exp, boolean isSpecialNumber) throws IOException {
		// Falls wir uns eine Hexadezimalzahl ansehen, müssen wir die Buchstaben in kleine Buchstaben überführen.
		for(c = (isSpecialNumber) ? Character.toLowerCase(Lexer.peekc()) : Lexer.peekc(); exp.test(c); c = (isSpecialNumber) ? Character.toLowerCase(Lexer.peekc()) : Lexer.peekc()) {
			buffer.bufferWrite(c);
			Lexer.nextc();
		}
		return c;
	}
	
	public static char peekc() throws IOException {
		return CompileProcess.compileProcessPeekChar(lexProcess);
	}
	
	// Liest das nächste Zeichen des Inputfiles
	public static char nextc() throws IOException{
		// Gehen ein Zeichen weiter
		char c = CompileProcess.compileProcessNextChar(lexProcess);
		
		// Wir müssen hier in den Expression-Buffer schreiben, falls wir nested Expressions finden.
		if (Lexer.lexIsInExpression() && !(Lexer.handleColon)) {
			// Immer, wenn wir nextc() innerhalb einer Expression (also innerhalb von Klammern) aufrufen, schrieben wir in den Buffer.
			// Zum Beispiel innerhalb von (40+30) schreiben wir die Character innerhalb der Klammern in den Buffer. Am Ende steht 40+30 im Buffer.
			Lexer.lexProcess.getParenthesesBuffer().bufferWrite(c);
		}
		lexProcess.getPos().setColNumber(lexProcess.getPos().getColNumber() + 1);
		// Neue Zeile erreicht
		if (c == '\n') {
			lexProcess.getPos().setLineNumber(lexProcess.getPos().getLineNumber() + 1);
			lexProcess.getPos().setColNumber(1);
		}
		return c;
	}
	
	public static void pushc(char c) throws IOException {
		CompileProcess.compileProcessPushChar(lexProcess, c);
	}
	
	public static char assertNextChar(char c) throws IOException {
		char nextC = Lexer.nextc();
		assert(c == nextC);
		return nextC;
	}
	
	private static Position lexFilePosition() {
		Position pos = new Position(lexProcess.getPos().getLineNumber(), lexProcess.getPos().getColNumber(), lexProcess.getPos().getFilename());
		return pos;
	}
	
	public static Token<Object> tokenCreate(Token<Object> token) {
		Lexer.tmpToken = token.memcpy();
		Lexer.tmpToken.setPos(lexFilePosition());
		if (Lexer.lexIsInExpression()) {
			assert(Lexer.lexProcess.getParenthesesBuffer() != null);
			// Wir speichern den Inhalt zwischen den Klammern einer Expression als String im aktuellen Token.
			// Haben wir die Expression (5+10+20) sind innerhalb der Klammern 5 Tokens, wir können aber immer die ganze Expression sehen von dem Token aus.
			Lexer.tmpToken.setBetweenBrackets(Lexer.lexProcess.getParenthesesBuffer().bufferPtr());
		}
		return Lexer.tmpToken;
	}
	
	public static Token<Object> lexerLastToken() {
		return lexProcess.getTokenVec().vectorBackOrNull();
	}
	
	public static Token<Object> handleWhitespace() throws IOException {
		Token<Object> lastToken = Lexer.lexerLastToken();
		if (lastToken != null) {
			lastToken.setWhitespaces(lastToken.getWhitespaces() + 1);
		}
		Lexer.nextc();
		return Lexer.readNextToken();
	}
	
	private static String readNumberStr() throws IOException {
		Buffer buffer = Buffer.bufferCreate();
		char c = Lexer.peekc();
		Lexer.LEX_GETC_IF(buffer, c, (x) -> x >= '0' && x <= '9', false);
		// Wir sind am Ende des Tokens, daher schreiben wir einen Nullterminator in den Buffer
		//buffer.bufferWrite((char) 0x00);
		return buffer.bufferPtr();
	}
	
	private static long readNumber() throws IOException {
		String s = Lexer.readNumberStr();
		// Konvertiert den Number-String in eine primitive long-Variable
		return Long.valueOf(s).longValue();
	}
	
	public static TokenNumberType lexerNumberType(char c) {
		TokenNumberType res = TokenNumberType.NUMBER_TYPE_NORMAL;
		if (c == 'L') {
			res = TokenNumberType.NUMBER_TYPE_LONG;
		}
		else if (c == 'f' ) {
			res = TokenNumberType.NUMBER_TYPE_FLOAT;
		}
		return res;
	}
	
	public static Token<Object> tokenMakeNumberForValue(Number number) throws IOException {
		TokenNumberType numberType = Lexer.lexerNumberType(Lexer.peekc());
		if (numberType != TokenNumberType.NUMBER_TYPE_NORMAL) {
			// Falls ein Buchstabe nach der Zahl steht, wie bei 583L oder 583f, entfernen wir den Buchstaben nach der Typfeststellung.
			Lexer.nextc();
		}
		return Lexer.tokenCreate(new Token<Object>(TokenType.TOKEN_TYPE_NUMBER, number, numberType));
	}
	
	public static Token<Object> tokenMakeNumber() throws IOException {
		return Lexer.tokenMakeNumberForValue(Lexer.readNumber());
	}
	
	public static Token<Object> tokenMakeString(char startDelim, char endDelim) throws IOException {
		Buffer buf = Buffer.bufferCreate();
		assert(Lexer.nextc() == startDelim);
		Character c = Lexer.nextc();
		// Solange der Character nicht den end-Delimiter oder End-of-File erreicht hat,
		// wird der nächste Character gelesen
		for(c = Lexer.nextc(); c != endDelim && c != Lexer.EOF; c = Lexer.nextc()) {
			if (c == '\\') {
				// Hier müssen wir einen Escape handeln.
				continue;
			}
			buf.bufferWrite(c);
		}
		return Lexer.tokenCreate(new Token<Object>(TokenType.TOKEN_TYPE_STRING, buf.bufferPtr()));
	}
	
	public static boolean opTreatedAsOne(char op) {
		return op == '(' || op == '[' || op == ',' || op == '.' || op == '*' || op == '?';
	}
	
	public static boolean isSingleOperator(char op) {
		return op == '+' || op == '-' || op == '/' || op == '*' || op == '=' || op == '>' || op == '<' || op == '|' || op == '&' || op == '^' || op == '%' || op == '!' || op == '(' || op == '[' || op == ',' || op == '.' || op == '~' || op == '?' || op == ':';
	}
	
	// returns true, wenn der C/C++ Operator valide ist
	public static boolean opValid(String op) {
		return SymbolTable.S_EQ(op, "::") || SymbolTable.S_EQ(op, "+") || SymbolTable.S_EQ(op, "-") || SymbolTable.S_EQ(op, "*") || SymbolTable.S_EQ(op, "/") || SymbolTable.S_EQ(op, "!") || SymbolTable.S_EQ(op, "^") || SymbolTable.S_EQ(op, "+=") || SymbolTable.S_EQ(op, "-=") || SymbolTable.S_EQ(op, "*=") || SymbolTable.S_EQ(op, "/=") || SymbolTable.S_EQ(op, ">>") || SymbolTable.S_EQ(op, "<<") || SymbolTable.S_EQ(op, ">=") || SymbolTable.S_EQ(op, "<=") || SymbolTable.S_EQ(op, ">") || SymbolTable.S_EQ(op, "<") || SymbolTable.S_EQ(op, "||") || SymbolTable.S_EQ(op, "&&") || SymbolTable.S_EQ(op, "|") || SymbolTable.S_EQ(op, "&") || SymbolTable.S_EQ(op, "++") || SymbolTable.S_EQ(op, "--") || SymbolTable.S_EQ(op, "=") || SymbolTable.S_EQ(op, "!=") || SymbolTable.S_EQ(op, "==") || SymbolTable.S_EQ(op, "->") || SymbolTable.S_EQ(op, "(") || SymbolTable.S_EQ(op, "[") || SymbolTable.S_EQ(op, ",") || SymbolTable.S_EQ(op, ".") || SymbolTable.S_EQ(op, "...") || SymbolTable.S_EQ(op, "~") || SymbolTable.S_EQ(op, "?") || SymbolTable.S_EQ(op, "%");
	}
	
	public static void readOpFlushBackKeepFirst(Buffer buffer) throws IOException {
		String data = buffer.bufferPtr();
		int len = buffer.getLen();
		for(int i = len - 1; i >= 1; i--) {
			Lexer.pushc(data.charAt(i));
		}
	}
	
	public static String readOp() throws IOException {
		boolean singleOperator = true;
		Character op;
		op = Lexer.nextc(); // Hier wird beim Beispiel ++ das erste + eingelesen
		Buffer buffer = Buffer.bufferCreate();
		buffer.bufferWrite(op);
		if (op == '*' && Lexer.peekc() == '=') {
			// '*=' -> '*' könnte ein einzelner Operator sein, aber wir erstellen dafür einen Spezialfall.
			buffer.bufferWrite(Lexer.peekc());
			// Überspringe '='.
			Lexer.nextc();
			singleOperator = false;
		}
		// Suche ob ++ oder -- (Inkrementation und Dekrementation) vorliegt.
		else if (!(Lexer.opTreatedAsOne(op))) {
			for(int i = 0; i < 1; i++) {
				op = Lexer.peekc(); // Schauen uns das nächste Zeichen an
				// Wenn es auch ein einfacher Operator ist, speichern wir das zweite plus zwischen und gehen endgültig ein Zeichen weiter.
				if (Lexer.isSingleOperator(op)) {
					buffer.bufferWrite(op);
					Lexer.nextc();
					singleOperator = false;
				}
			}
		}
		String ptr = buffer.bufferPtr();
		// Jetzt muss geprüft werden, ob der zweite Operator valide ist (+- wäre nicht valide).
		if (singleOperator == false) {
			if (Lexer.opValid(ptr) == false) {
				Lexer.readOpFlushBackKeepFirst(buffer);
				ptr = Character.toString(ptr.charAt(0));
			}
		}
		// Ist der Operator nicht valide, wird ein Fehler ausgegeben.
		else if (Lexer.opValid(ptr) == false) {
			String msg = "Der Operator " + ptr + " ist nicht valide.\n";
			Compiler.compilerError(Lexer.lexProcess.getCompileProcess(), msg, ptr);
		}
		return ptr;
	}
	
	public static void lexFinishExpression() {
		// Unser Zähler für die Expressions wird dekrementiert, sobald wir mit einer Expression fertig sind
		Lexer.lexProcess.setCurrentExpressionCount(Lexer.lexProcess.getCurrentExpressionCount() - 1);
		if (Lexer.lexProcess.getCurrentExpressionCount() < 0) {
			// Hier haben wir eine ) ohne ein voriges ( Symbol.
			Compiler.compilerError(lexProcess.getCompileProcess(), "Sie haben eine Expression geschlossen, die Sie nicht geöffnet haben.");
		}
	}
	
	public static void lexNewExpression() {
		// Unser Zähler für die Expressions wird inkrementiert, wenn wir eine Expression gefunden haben.
		Lexer.lexProcess.setCurrentExpressionCount(Lexer.lexProcess.getCurrentExpressionCount() + 1);
		if (Lexer.lexProcess.getCurrentExpressionCount() == 1) {
			Lexer.lexProcess.setParenthesesBuffer(Buffer.bufferCreate());
		}
		// Wenn wir nested expressions haben wie ( ( ) ) wird der Buffer für die inneren Expressions benötigt.
	}
	
	public static boolean lexIsInExpression() {
		return Lexer.lexProcess.getCurrentExpressionCount() > 0;
	}
	
	public static Token<Object> tokenHandleColon() throws IOException {
		Lexer.handleColon = true;
		char colon = Lexer.nextc();
		Lexer.handleColon = false;
		char c = Lexer.peekc();
		Lexer.pushc(colon);
		if (c == ':') {
			return  Lexer.tokenMakeOperatorOrString();
		}
		return Lexer.tokenMakeSymbol();
	}
	
	public static Token<Object> tokenMakeOperatorOrString() throws IOException {
		char op = Lexer.peekc();
		// Wir müssen checken, ob ein include-Statement vorliegt (<abc.h> beispielsweise)
		if (op == '<') {
			Token<Object> lastToken = Lexer.lexerLastToken();
			if (Token.tokenIsKeyword(lastToken, "include")) {
				return Lexer.tokenMakeString('<', '>');
			}
		}
		Token<Object> token = Lexer.tokenCreate(new Token<Object>(TokenType.TOKEN_TYPE_OPERATOR, Lexer.readOp()));
		// Für eine Expression müssen wir ebenfals checken
		if (op == '(') {
			Lexer.lexNewExpression();
		}
		return token;
	}
	
	public static Token<Object> tokenMakeOneLineComment() throws IOException {
		Buffer buffer = Buffer.bufferCreate();
		char c = 0;
		// Ein einzeiliger Kommentar soll gelesen werden, bis eine neue Zeile oder das Ende des Files entdeckt wurden.
		Lexer.LEX_GETC_IF(buffer, c, (x) -> x != '\n' && x != Lexer.EOF, false);
		return Lexer.tokenCreate(new Token<Object>(TokenType.TOKEN_TYPE_COMMENT, buffer.bufferPtr()));
	}
	
	public static Token<Object> tokenMakeMultilineComment() throws IOException {
		Buffer buffer = Buffer.bufferCreate();
		Character c = 0;
		while(true) {
			c = Lexer.LEX_GETC_IF(buffer, c, (x) -> x != '*' && x != Lexer.EOF, false);
			// Wurde der Kommentar nicht beendet, aber wir haben das Ende des Files erreicht, ist es ein Fehler
			if (c == Lexer.EOF) {
				Compiler.compilerError(Lexer.lexProcess.getCompileProcess(), "Sie haben den mehrzeiligen Kommentar nicht geschlossen.\n");
			}
			else if (c == '*') {
				// Überspringe den *
				Lexer.nextc();
				
				// Mit einem / wird der Kommentar beendet.
				if (Lexer.peekc() == '/') {
					Lexer.nextc();
					break;
				}
			}
		}
		return Lexer.tokenCreate(new Token<Object>(TokenType.TOKEN_TYPE_COMMENT, buffer.bufferPtr()));
	}
	
	public static Token<Object> handleComment() throws IOException {
		char c = Lexer.peekc();
		// Wenn wir einen / finden, wissen wir, dass wir einen Kommentar gefunden haben.
		if (c == '/') {
			Lexer.nextc();
			if (Lexer.peekc() == '/') {
				// Wir haben einen einzeiligen Kommentar gefunden
				Lexer.nextc();
				return Lexer.tokenMakeOneLineComment();
			}
			else if (Lexer.peekc() == '*') {
				// Wir haben einen mehrzeiligen Kommentar entdeckt
				Lexer.nextc();
				return Lexer.tokenMakeMultilineComment();
			}
			// Ist es weder ein einzeiliger-, noch ein mehrzeiliger Kommentar, verweist der / auf eine Division.
			Lexer.pushc('/');
			return Lexer.tokenMakeOperatorOrString();
		}
		// Falls wir keinen Kommentar gefunden haben, geben wir null zurück.
		return null;
	}
	
	public static Token<Object> tokenMakeSymbol() throws IOException {
		Character c = Lexer.nextc();
		// Wenn das Zeichen eine rechte Klammer ist, müssen wir die Expression schließen.
		if (c == ')' ) {
			Lexer.lexFinishExpression();
		}
		Token<Object> token = Lexer.tokenCreate(new Token<Object>(TokenType.TOKEN_TYPE_SYMBOL, c));
		return token;
	}
	
	public static Token<Object> tokenMakeIdentifierOrKeyword() throws IOException {
		Buffer buffer = Buffer.bufferCreate();
		Character c = 0;
		// Identifier bestehen aus buchstaben, Ziffern und Unterstrichen.
		// Wir müssen uns darum nicht sorgen, dass eine Zahl am Anfang steht, weil wir nur in diesen Fall kommen wenn keine Ziffer am Anfang steht (sonst wäre es eine Zahl)
		Lexer.LEX_GETC_IF(buffer, c, (x) -> (x >= 'a' && x <= 'z') || (x >= 'A' && x <= 'Z') || (x >= '0' && x <= '9') || (x == '_'), false);
		
		// Checken, ob es ein Keyword ist
		if (SymbolTable.isKeyword(buffer.bufferPtr())) {
			return Lexer.tokenCreate(new Token<Object>(TokenType.TOKEN_TYPE_KEYWORD, buffer.bufferPtr()));
		}
		// Sonst: Identifier Token zurückgeben
		return Lexer.tokenCreate(new Token<Object>(TokenType.TOKEN_TYPE_IDENTIFIER, buffer.bufferPtr()));
	}
	
	public static Token<Object> readSpecialToken() throws IOException {
		// Special Tokens sind Tokens, bei denen wir nicht sofort wissen was für ein Token es ist (z. B. ist es identifier oder keyword?).
		// Die Funktion liest das Special Token ein und wenn es keins findet gibt sie null zurück.
		Character c = Lexer.peekc();
		if (SymbolTable.isAlpha(c) || c == '_') {
			return Lexer.tokenMakeIdentifierOrKeyword();
		}
		return null;
	}
	
	public static Token<Object> tokenMakeNewline() throws IOException {
		Lexer.nextc();
		return Lexer.tokenCreate(new Token<Object>(TokenType.TOKEN_TYPE_NEWLINE, "\n"));
	}
	
	public static char lexGetEscapedChar(char c) {
		char co = 0;
		switch(c) {
			case('n'): {
				co = '\n';
				break;
			}
			case('\\'): {
				co = '\\';
				break;
			}
			case('t'): {
				co = '\t';
				break;
			}
			case('\''): {
				co = '\'';
				break;
			}
		}
		return co;
	}
	
	public static void lexerPopToken() throws IOException {
		Lexer.lexProcess.getTokenVec().vectorPop();
	}
	
	public static String readHexNumberStr() throws IOException {
		Buffer buffer = Buffer.bufferCreate();
		char c = Character.toLowerCase(Lexer.peekc());
		Lexer.LEX_GETC_IF(buffer, c, (x) -> (x >= '0' && x <= '9') || (x >= 'a'&& x <= 'f'), true);
		return buffer.bufferPtr();
	}
	
	public static Token<Object> tokenMakeSpecialNumberHexadecimal() throws IOException {
		// Wirf das 'x' aus
		Lexer.nextc();
		
		long number = 0;
		// Wir ändern hier 0xABC635 zu "ABC635", womit wir besser arbeiten können.
		String numberStr = Lexer.readHexNumberStr();
		// Nun müssen wir den Hexadezimal-String in eine valide Zahl verwandeln.
		number = Long.valueOf(numberStr, 16).longValue();
		return Lexer.tokenMakeNumberForValue(number);
	}
	
	public static void lexerValidateBinaryString(String str) throws IOException {
		int len = str.length();
		for(int i = 0; i < len; i++) {
			if (str.charAt(i) != '0' && str.charAt(i) != '1') {
				Compiler.compilerError(Lexer.lexProcess.getCompileProcess(), "Das ist keine valide Binärzahl.\n");
			}
		}
	}
	
	public static Token<Object> tokenMakeSpecialNumberBinary() throws IOException {
		// Überspringe das 'b' (z. B. bei einem 0b11001) -> die 0 wurde schon übersprungen.
		Lexer.nextc();
		
		long number = 0;
		String numberStr = Lexer.readNumberStr();
		Lexer.lexerValidateBinaryString(numberStr);
		number = Long.valueOf(numberStr, 2).longValue();
		return Lexer.tokenMakeNumberForValue(number);
	}
	
	public static Token<Object> tokenMakeSpecialNumber() throws IOException {
		Token<Object> token = null;
		Token<Object> lastToken = Lexer.lexerLastToken();
		// Checken, ob eine 0 im letzten Token gespeichert ist, die eine Binär- oder Hexadezimalzahl ankündigt.
		if (lastToken == null || (lastToken.getType() != TokenType.TOKEN_TYPE_NUMBER)) {
			return Lexer.tokenMakeIdentifierOrKeyword();
		}
		else if (lastToken.getType() == TokenType.TOKEN_TYPE_NUMBER) {
			if ((Long) lastToken.getValue() != 0) {
				return Lexer.tokenMakeIdentifierOrKeyword();
			}
		}
		// Letztes Token (0) auswerfen
		Lexer.lexerPopToken();
		
		// Handelt es sich um eine Hexadezimal- oder eine Binärzahl?
		char c = Lexer.peekc();
		// Finden wir ein x, wie beispielsweise bei 0x500, handelt es sich um eine Hexadezimalzahl. Die anfängliche 0 wurde vorher lexerPopToken() ausgeworfen.
		if (c == 'x') {
			token = Lexer.tokenMakeSpecialNumberHexadecimal();
		}
		else if (c == 'b') {
			token = Lexer.tokenMakeSpecialNumberBinary();
		}
		return token;
	}
	
	public static Token<Object> tokenMakeQuote() throws IOException {
		// Wir checken mit assertNextChar, ob das nächste Zeichen ein ' ist.
		Lexer.assertNextChar('\'');
		char c = Lexer.nextc();
		if (c == '\\') {
			// Hier behandeln wir einen Escape:
			// Beim Beispiel '\n' ist c zuerst das '\\', und wird hierunter zum 'n'.
			c = Lexer.nextc();
			c = Lexer.lexGetEscapedChar(c);
		}
		
		if (Lexer.nextc() != '\'') {
			Compiler.compilerError(Lexer.lexProcess.getCompileProcess(), "Sie haben ein Anführungszeichen geöffnet ' es aber nicht mit einem ' Zeichen geschlossen.");
		}
		// chars werden als Zahlen abgespeichert, da sie Zahlen zwischen 0 und 255 symbolisieren (ASCII)
		return Lexer.tokenCreate(new Token<Object>(TokenType.TOKEN_TYPE_NUMBER, c));
	}
	
	public static Token<Object> readNextToken() throws IOException {
		Token<Object> token = null;
		Character c = Lexer.peekc();
		token = Lexer.handleComment();
		// Falls handleComment() nicht null zurückgibt, wissen wir, dass wir einen Kommentar gefunden haben
		if (token != null) {
			return token;
		}
		// Symbol ist eine Zahl eine Zahl
		if (SymbolTable.isNumeric(c)) {
			token = Lexer.tokenMakeNumber();
		}
		else if (c == '"') {
			token = Lexer.tokenMakeString('"', '"');
		}
		else if (c == '\'') {
			token = Lexer.tokenMakeQuote();
		}
		else if (SymbolTable.isOperatorExcludingDivision(c)) {
			token = Lexer.tokenMakeOperatorOrString();
		}
		else if (SymbolTable.isSymbolWithoutColon(c)) {
			token = Lexer.tokenMakeSymbol();
		}
		else if (c == ':') {
			token = Lexer.tokenHandleColon();
		}
		else if (c == 'b') {
			token = Lexer.tokenMakeSpecialNumber();
		}
		else if (c == 'x') {
			token = Lexer.tokenMakeSpecialNumber();
		}
		// Im Moment interessieren wir uns nicht für Whitespaces und ignorieren sie.
		else if (c == ' ') {
			token = Lexer.handleWhitespace();
		}
		else if (c == '\t') {
			token = Lexer.handleWhitespace();
		}
		else if (c == '\n') {
			token = Lexer.tokenMakeNewline();
		}
		else if (c == Lexer.EOF) {
			// Wir sind fertig mit der lexikalischen Analyse bei dem File.
		}
		else {
			token = Lexer.readSpecialToken();
			// Fehler: Wir erkennen das Token nicht
			if (token == null) {
				Compiler.compilerError(lexProcess.getCompileProcess(), "Unerwartetes Token.\n");
			}
		}
		return token;
	}
	
	public static LexicalAnalysis lex(LexProcess<Object> process) throws IOException {
		process.setCurrentExpressionCount(0);
		process.setParenthesesBuffer(null);
		lexProcess = process;
		process.getPos().setFilename(process.getCompileProcess().getCFile().getAbsPath());
		
		Token<Object> token = Lexer.readNextToken();
		// Lese alle Tokens im Input-C-File
		while (token != null) {
			process.getTokenVec().vectorPush(token);
			token = Lexer.readNextToken();
		}
		
		return LexicalAnalysis.LEXICAL_ANALYSIS_ALL_OK;
	}
	
	// Erstellt Tokens aus einem Input-String.
	public static LexProcess<Object> tokensBuildForString(CompileProcess compileProcess, String str) throws IOException {
		Buffer buffer = Buffer.bufferCreate();
		buffer.bufferPrintf(str);
		LexProcess<Object> lexProcess = LexProcess.lexProcessCreate(compileProcess, buffer);
		if (lexProcess == null) return null;
		if (Lexer.lex(lexProcess) != LexicalAnalysis.LEXICAL_ANALYSIS_ALL_OK) {
			return null;
		}
		return lexProcess;
	}
}
