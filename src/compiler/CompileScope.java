package compiler;

import parser.Scope;

public class CompileScope<T> {
	// Wurzelscope oder global Scope
	private Scope<T> root;
	// Scope an dem wir aktuell arbeiten
	private Scope<T> current;
	
	public CompileScope() {
		this.root = null;
		this.current = null;
	}
	
	public Scope<T> getRoot() {
		return root;
	}
	public void setRoot(Scope<T> root) {
		this.root = root;
	}
	public Scope<T> getCurrent() {
		return current;
	}
	public void setCurrent(Scope<T> current) {
		this.current = current;
	}
}
