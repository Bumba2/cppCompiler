package compiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import cleanCodeAnalyzer.CleanCodeObservation;
import cleanCodeAnalyzer.CleanCodeScore;
import helpers.Vector;
import lexer.LexProcess;
import lexer.Position;
import lexer.Token;
import parser.Node;
import parser.Symbol;
import parser.Symbols;
import parser.Symresolver;

// implementiert einen Compilerprozess
public class CompileProcess {
	
	private CompileProcessInputFile cFile;
	private File outFile;
	private int flags; // Die Flags abhängig davon, wie das File compiled werden soll .
	private Position pos; // Wo befinden wir uns im CompileProzess?
	private Vector<Token<Object>> tokenVec; // Erhält die Tokens nach dem Lex-Prozess
	private Vector<Node<Object>> nodeVec; // Wird zum pushen und poppen der Nodes beim Parsen verwendet.
	private Vector<Node<Object>> nodeTreeVec; // Enthält die Wurzel des Abstract Syntax Trees.
	private CompileScope<Object> scope; // Hält Referenzen auf den Global-Scope und einen aktuellen Scope.
	private Symbols<Symbol<Object>> symbols; // Hält die Symbole des Kompilierungsprozesses in Symboltabellen (Vektoren von Symbolen).
	private Vector<CleanCodeObservation<Object>> cleanCodeObservations;
	private CleanCodeScore cleanCodeScore;
	private String compilerMessage = "";
	
	public CompileProcess(String filename, String filenameOut, int flags) throws FileNotFoundException  {
		File file = new File(filename);
		if (file.exists()) {
	        file.setReadOnly();
	    } else {
	        throw new FileNotFoundException();
	    }
		this.outFile = null;
		if (filenameOut != null) {
			outFile = new File(filenameOut);
			try {
				outFile.createNewFile();
			} catch (IOException e) {
				String output = "Die Ausgabedatei konnte nicht erstellt werden.\n";
				this.setCompilerMessage(output);
				System.out.println(output);
			}
		}
		this.tokenVec = null; // Tokenvektor für den Lexer.
		// Knotenvektoren für den Parser.
		this.setNodeVec(Vector.vectorCreate());
		this.setNodeTreeVec(Vector.vectorCreate());
		this.setCleanCodeObservations(Vector.vectorCreate());
		
		this.cFile = new CompileProcessInputFile(file, filename);
		this.setFlags(flags);
		this.pos = new Position(0, 0, filename);
		this.scope = new CompileScope<Object>();
		this.symbols = new Symbols<Symbol<Object>>();
		
		// Initialisieren des Symbolauflösers und einer Symboltabelle.
		Symresolver.symresolverInitialize(this);
	}

	public Position getPos() {
		return pos;
	}

	public void setPos(Position pos) {
		this.pos = pos;
	}
	
	public CompileProcessInputFile getCFile() {
		return this.cFile;
	}
	
	public void setCFile(CompileProcessInputFile cFile) {
		this.cFile = cFile;
	}
	
	public static <T> char compileProcessNextChar(LexProcess<T> lexProcess) throws IOException {
		CompileProcess compileProcess = lexProcess.getCompileProcess();
		compileProcess.getPos().setColNumber(compileProcess.getPos().getColNumber() + 1);
		char c = (char) compileProcess.getCFile().getInStream().read();
		if (c == '\n') {
			compileProcess.getPos().setLineNumber(compileProcess.getPos().getLineNumber() + 1);
			compileProcess.getPos().setColNumber(1);
		}
		return c;
	}
	
	public static <T> char compileProcessPeekChar(LexProcess<T> lexProcess) throws IOException {
		CompileProcess compileProcess = lexProcess.getCompileProcess();
		char c = (char) compileProcess.getCFile().getInStream().read();
		compileProcess.getCFile().getInStream().unread(c);
		return c;
	}
	
	public static <T> void compileProcessPushChar(LexProcess<T> lexProcess, char c) throws IOException {
		CompileProcess compileProcess = lexProcess.getCompileProcess();
		compileProcess.getCFile().getInStream().unread(c);
	}

	public int getFlags() {
		return flags;
	}

	public void setFlags(int flags) {
		this.flags = flags;
	}

	public Vector<Token<Object>> getTokenVec() {
		return tokenVec;
	}

	public void setTokenVec(Vector<Token<Object>> tokenVec) {
		this.tokenVec = tokenVec;
	}

	public Vector<Node<Object>> getNodeVec() {
		return nodeVec;
	}

	public void setNodeVec(Vector<Node<Object>> nodeVec) {
		this.nodeVec = nodeVec;
	}

	public Vector<Node<Object>> getNodeTreeVec() {
		return nodeTreeVec;
	}

	public void setNodeTreeVec(Vector<Node<Object>> nodeTreeVec) {
		this.nodeTreeVec = nodeTreeVec;
	}

	public CompileScope<Object> getScope() {
		return scope;
	}

	public void setScope(CompileScope<Object> scope) {
		this.scope = scope;
	}

	public Symbols<Symbol<Object>> getSymbols() {
		return symbols;
	}

	public void setSymbols(Symbols<Symbol<Object>> symbols) {
		this.symbols = symbols;
	}

	public Vector<CleanCodeObservation<Object>> getCleanCodeObservations() {
		return cleanCodeObservations;
	}

	public void setCleanCodeObservations(Vector<CleanCodeObservation<Object>> cleanCodeObservations) {
		this.cleanCodeObservations = cleanCodeObservations;
	}

	public CleanCodeScore getCleanCodeScore() {
		return cleanCodeScore;
	}

	public void setCleanCodeScore(CleanCodeScore cleanCodeScore) {
		this.cleanCodeScore = cleanCodeScore;
	}

	public String getCompilerMessage() {
		return compilerMessage;
	}

	public void setCompilerMessage(String compilerMessage) {
		this.compilerMessage = compilerMessage;
	}
}
