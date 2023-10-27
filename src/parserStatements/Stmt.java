package parserStatements;

public class Stmt<T> {
	private T stmt;
	
	public Stmt(T stmt) {
		this.setStmt(stmt);
	}

	public T getStmt() {
		return stmt;
	}

	public void setStmt(T Stmt) {
		this.stmt = Stmt;
	}
}
