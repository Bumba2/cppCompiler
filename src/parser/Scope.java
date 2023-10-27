package parser;

import compiler.CompileProcess;
import helpers.Vector;

public class Scope<T> {
	private int flags;
	private Vector<T> entities; // Hält die Elemente in einem Scope.
	// Anzahl von Bytes, die ein Scope verwendet. Angepasst an 16 Bytes.
	private int size;
	// Elternscope, der in der Hierarchie weiter oben liegt. Ist NULL, wenn es kein ELternscope gibt.
	private Scope<T> parent;
	private Vector<T> namespaces; // In einem Scope können mehrere Namensräume parallel existieren.
	
	public static Scope<Object> scopeAlloc() {
		Scope<Object> scope = new Scope<Object>();
		scope.setSize(0);
		scope.setEntities(Vector.vectorCreate());
		scope.getEntities().vectorSetPeekPointerEnd(); // Setzen den Peekpointer an die letzte Position.
		// Durch die Decrement-Flag peeken wir nach hinten und nicht nach vorne.
		scope.getEntities().setFlags(Vector.VECTOR_FLAG_PEEK_DECREMENT); 
		scope.setEntities(Vector.vectorCreate());
		return scope;
	}
	
	public static void scopeDealloc(Scope<Object> scope) {
		// Später bearbeiten.
	}
	
	// Wir erstellen Funktionalität für den Wurzelscope (Global Scope) und geben diesen zurück.
	public static Scope<Object> scopeCreateRoot(CompileProcess compileProcess) {
		// Sicherstellen, dass es noch keine globalen und aktuellen Scopes im aktuellen compile-Prozess gibt.
		assert(compileProcess.getScope().getRoot() == null);
		assert(compileProcess.getScope().getCurrent() == null);
		
		// Global Scope erstellen und Wurzelscope und aktuellen Scope im Kompilizierungsprozess auf diesen setzen.
		Scope<Object> rootScope = Scope.scopeAlloc();
		compileProcess.getScope().setRoot(rootScope);
		compileProcess.getScope().setCurrent(rootScope);
		return rootScope;
	}
	
	public static void scopeFreeRoot(CompileProcess compileProcess) {
		scopeDealloc(compileProcess.getScope().getRoot());
		compileProcess.getScope().setRoot(null);
		compileProcess.getScope().setCurrent(null);
	}
	
	public static Scope<Object> scopeNew(CompileProcess compileProcess, int flags) {
		// Checken, dass wir schon einen Global-Scope und einen aktuellen Scope initialisiert haben.
		// Sonst hätten wir diese Funktion aufgerufen, bevor wir den Wurzelscope erstellt haben, was nicht erlaubt ist.
		assert(compileProcess.getScope().getRoot() != null);
		assert(compileProcess.getScope().getCurrent() != null);
		// Wir erstellen einen neuen Scope und setzen den aktuellen Scope als Elternscope ein.
		Scope<Object> newScope = Scope.scopeAlloc();
		newScope.setFlags(flags);
		newScope.setParent(compileProcess.getScope().getCurrent());
		// Der neue Scope wird der neue aktuelle Scope und wird zurückgegeben.
		compileProcess.getScope().setCurrent(newScope);
		return newScope;
	}
	
	public static void scopeIterationStart(Scope<Object> scope) {
		// Wir starten vom Beginn unserer Scopes
		scope.getEntities().vectorSetPeekPointer(0);
		if ((scope.getEntities().getFlags() == Vector.VECTOR_FLAG_PEEK_DECREMENT)) {
			scope.getEntities().vectorSetPeekPointerEnd();
		}
	}
	
	public static void scopeIterationEnd(Scope<Object> scope) {
		
	}
	
	// Gibt das vorige Element zurück im Scope zurück.
	public static Object scopeIterateBack(Scope<Object> scope) {
		if (scope.getEntities().getCount() == 0) {
			return null;
		}
		return scope.getEntities().vectorPeekPtr();
	}
	
	// Welches Element wurde als letztes in den Scope gesteckt?
	public static Object scopeLastEntityAtScope(Scope<Object> scope) {
		if (scope.getEntities().getCount() == 0) {
			return null;
		}
		return scope.getEntities().vectorBackPtr();
	}
	
	// Suche nach dem letzten Element im Scope (und falls nicht vorhanden in den Elternscopes) und gib es zurück.
	// Wir suchen zwischen dem Startscope scope und stopScope (ein beliebiges Elternscope) nach dem Element.
	public static Object scopeLastEntityFromScopeStopAt(Scope<Object> scope, Scope<Object> stopScope) {
		if (scope == stopScope) {
			return null;
		}
		Object last = Scope.scopeLastEntityAtScope(scope);
		// Wenn last nicht null ist, haben wir ein Element gefunden.
		if (last != null) {
			return last;
		}
		// Haben wir im Scope kein Element gefunden, müssen wir im Elternscope nachsehen.
		Scope<Object> parent = scope.getParent();
		if (parent != null) {
			return Scope.scopeLastEntityFromScopeStopAt(parent, stopScope);
		}
		// Findet die Rekursion bis zum stopScope kein Element, geben wir NULL zurück.
		return null;
	}
	
	// Alias der Funktion scopeLastEntityFromScopeStopAt, jedoch wird der aktuelle Scope des Kompilierungsprozesses als Input verwendet.
	// Dadurch müssen wir uns um den Startscope nicht mehr kümmern.
	public static Object scopeLastEntityStopAt(CompileProcess compileProcess, Scope<Object> stopScope) {
		return scopeLastEntityFromScopeStopAt(compileProcess.getScope().getCurrent(), stopScope);
	}
	
	// Gibt das letzte Element des aktuellen Scopes des Kompilierungsprozesses zurück.
	public static Object scopeLastEntity(CompileProcess compileProcess) {
		return scopeLastEntityStopAt(compileProcess, null);
	}
	
	public static void scopePush(CompileProcess compileProcess, Object ptr, int elemSize) {
		compileProcess.getScope().getCurrent().getEntities().vectorPush(ptr);
		// Wir berechnen im Vorhinein die Größe jedes Elements, dass wir in den Scope schieben und addieren es zur Gesamtgröße des Scopes.
		compileProcess.getScope().getCurrent().setSize(compileProcess.getScope().getCurrent().getSize() + elemSize);
	}
	
	public static void scopeFinish(CompileProcess compileProcess) {
		// Der neue aktuelle Scope wird auf den Elternknoten des alten aktuellen Scopes gesetzt.
		Scope<Object> newCurrentScope = compileProcess.getScope().getCurrent().getParent();
		Scope.scopeDealloc(compileProcess.getScope().getCurrent());
		compileProcess.getScope().setCurrent(newCurrentScope);
		// Wenn wir noch einen Globalscope haben, aber der aktuelle Scope NULL ist, dann haben wir den Global Scope gelöscht.
		if (compileProcess.getScope().getRoot() != null && compileProcess.getScope().getCurrent() == null) {
			compileProcess.getScope().setRoot(null);
		}
	}
	
	public static Scope<Object> scopeCurrent(CompileProcess compileProcess) {
		return compileProcess.getScope().getCurrent();
	}

	public int getFlags() {
		return flags;
	}

	public void setFlags(int flags) {
		this.flags = flags;
	}

	public Vector<T> getEntities() {
		return entities;
	}

	public void setEntities(Vector<T> entities) {
		this.entities = entities;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}



	public Scope<T> getParent() {
		return parent;
	}

	public void setParent(Scope<T> parent) {
		this.parent = parent;
	}

	public Vector<T> getNamespaces() {
		return namespaces;
	}

	public void setNamespaces(Vector<T> namespaces) {
		this.namespaces = namespaces;
	}
	
}
