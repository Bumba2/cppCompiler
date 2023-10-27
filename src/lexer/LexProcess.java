package lexer;

import compiler.CompileProcess;
import helpers.Buffer;
import helpers.Vector;

public class LexProcess<T> {
	private Position pos;
	private Vector<Token<T>> tokenVec;
	private CompileProcess compileProcess;
	/**
	 * Wie viele Klammern sind dort im Moment?
	 * Z. B. ((50)) -> Wert von 2 für currentExpressionCount
	 */
	private int currentExpressionCount;
	private Buffer parenthesesBuffer;
	// Private Daten, die der Lexer nicht versteht, aber der User des Lexers versteht.
	private Buffer privateData;
	
	public LexProcess (CompileProcess compileProcess, Buffer privateData) throws NullPointerException{
		this.compileProcess = compileProcess;
		this.tokenVec = new Vector<Token<T>>();
		this.setPrivateData(privateData);
		this.compileProcess.getPos().setLineNumber(1);
		this.compileProcess.getPos().setColNumber(1);
		this.pos = new Position(1, 1, compileProcess.getCFile().getFp().getAbsolutePath());
	}
	
	public Position getPos() {
		return pos;
	}
	public void setPos(Position pos) {
		this.pos = pos;
	}
	public Vector<Token<T>> getTokenVec() {
		return tokenVec;
	}
	public void setTokenVec(Vector<Token<T>> tokenVec) {
		this.tokenVec = tokenVec;
	}
	public CompileProcess getCompileProcess() {
		return this.compileProcess;
	}
	public void setCompileProcess(CompileProcess compileProcess) {
		this.compileProcess = compileProcess;
	}


	public int getCurrentExpressionCount() {
		return currentExpressionCount;
	}


	public void setCurrentExpressionCount(int currentExpressionCount) {
		this.currentExpressionCount = currentExpressionCount;
	}


	public Buffer getParenthesesBuffer() {
		return parenthesesBuffer;
	}


	public void setParenthesesBuffer(Buffer parenthesesBuffer) {
		this.parenthesesBuffer = parenthesesBuffer;
	}


	public Buffer getPrivateData() {
		return privateData;
	}


	public void setPrivateData(Buffer privateData) {
		this.privateData = privateData;
	}
	
	public static <T> LexProcess<T> lexProcessCreate(CompileProcess compileProcess, Buffer privateData) {
		return new LexProcess<T>(compileProcess, privateData);
	}
	
	public Buffer lexProcessPrivate() {
		return this.getPrivateData();
	}
	
	public Vector<Token<T>> lexProcessTokens(LexProcess<T> process) {
		return process.getTokenVec();
	}
	
	public char lexerStringBufferNextChar() {
		Buffer buf = this.getPrivateData();
		return buf.bufferRead();
	}
	
	public char lexerStringBufferPeekChar() {
		Buffer buf = this.getPrivateData();
		return buf.bufferPeek();
	}
	
	public void lexerStringBufferPushChar(char c) {
		Buffer buf = this.getPrivateData();
		buf.bufferWrite(c);
	}
}
