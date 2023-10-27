package parserFixup;

import compiler.CompileProcess;
import helpers.Vector;

// Forward-Declaren wir eine Struktur, haben wir eine Nullpointer-Exception, falls wir auf die Struktur
// zugreifen. In dem Fixup erstellen wir eine Struktur ohne Speicherplatz, um keinen Nullpointer zu haben.
public class Fixup<T> {
	private FixupFlag flags;
	private FixupSystem<T> system;
	private FixupConfig config;
	
	public Fixup() {
		
	}

	public FixupFlag getFlags() {
		return flags;
	}

	public void setFlags(FixupFlag flags) {
		this.flags = flags;
	}

	public FixupSystem<T> getSystem() {
		return system;
	}

	public void setSystem(FixupSystem<T> system) {
		this.system = system;
	}

	public FixupConfig getConfig() {
		return config;
	}

	public void setConfig(FixupConfig config) {
		this.config = config;
	}
	
	public static FixupSystem<Object> fixupSysNew() {
		FixupSystem<Object> system = new FixupSystem<Object>();
		system.setFixups(Vector.vectorCreate());
		return system;
	}
	
	public static FixupConfig fixupConfig(Fixup<Object> fixup) {
		return fixup.getConfig();
	}
	
	public static void fixupStartIteration(FixupSystem<Object> system) {
		system.getFixups().vectorSetPeekPointer(0);
	}
	
	@SuppressWarnings("unchecked")
	public static Fixup<Object> fixupNext(FixupSystem<Object> system) {
		return (Fixup<Object>) system.getFixups().vectorPeekPtr();
	}
	
	// Zählt alle nicht-aufgelösten Fixups.
	public static int fixupSysUnresolvedFixupsCount(FixupSystem<Object> system) {
		int count = 0;
		Fixup.fixupStartIteration(system);
		Fixup<Object> fixup = Fixup.fixupNext(system);
		while(fixup != null) {
			if (fixup.getFlags() == FixupFlag.FIXUP_FLAG_RESOLVED) {
				fixup = Fixup.fixupNext(system);
				continue;
			}
			count++;
			fixup = Fixup.fixupNext(system);
		}
		return count;
	}
	
	// Erstellt eine Shallow-Copy der Fixup-Konfiguration.
	public static FixupConfig memcpy(FixupConfig config) {
		FixupConfig newConfig = new FixupConfig(config.getPrivateData());
		return newConfig;
	}
	
	public static Fixup<Object> fixupRegister(FixupSystem<Object> system, FixupConfig config) {
		Fixup<Object> fixup = new Fixup<Object>();
		fixup.setConfig(Fixup.memcpy(config));
		fixup.setSystem(system);
		system.getFixups().vectorPush(fixup);
		return fixup;
	}
	
	public static boolean fixupResolve(Fixup<Object> fixup, CompileProcess currentProcess) {
		if (fixupConfig(fixup).datatypeStructNodeFix(fixup, currentProcess)) {
			fixup.setFlags(FixupFlag.FIXUP_FLAG_RESOLVED);
			return true;
		}
		// Der Fix ist fehlgeschlagen.
		return false;
	}
	
	public static Object fixupPrivate(Fixup<Object> fixup) {
		return fixup.getConfig().getPrivateData();
	}
	
	public static boolean fixupsResolve(FixupSystem<Object> system, CompileProcess currentProcess) {
		Fixup.fixupStartIteration(system);
		Fixup<Object> fixup = Fixup.fixupNext(system);
		while(fixup != null) {
			if (fixup.getFlags() == FixupFlag.FIXUP_FLAG_RESOLVED) {
				continue;
			}
			Fixup.fixupResolve(fixup, currentProcess);
			fixup = Fixup.fixupNext(system);
		}
		return Fixup.fixupSysUnresolvedFixupsCount(system) == 0;
	}
}
