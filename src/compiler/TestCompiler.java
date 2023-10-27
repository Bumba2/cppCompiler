package compiler;

import java.io.IOException;

public class TestCompiler {

	public static void main(String[] args) {
		CompilerMessage res = null;
		try {
			res = Compiler.compileFile("src/compiler/test.c", "./test", 0);
		}
		catch (IOException e) {
			System.out.println("Unbekannte Antwort für die kompilierte Datei.");
			return;
		}
		if (res == CompilerMessage.COMPILER_FILE_COMPILED_OK) {
			System.out.println("\n2. Compilernachricht:\n");
			System.out.println("Alles hat gut kompiliert.");
		}
		else if (res == CompilerMessage.COMPILER_FAILED_WITH_ERRORS) {
			System.out.println("Die Kompilierung ist gescheitert.");
		}
		else {
			System.out.println("Unbekannte Antwort für die kompilierte Datei.");
		}
	}

}
