package compiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;

import cleanCodeAnalyzer.CleanCodeScore;
import lexer.LexProcess;
import lexer.Lexer;
import lexer.LexicalAnalysis;
import lexer.SymbolTable;
import parser.ParseMessage;
import parser.Parser;

public class Compiler {
	
	public static void compilerError(CompileProcess compileProcess, String msg, Object... arguments) {
		msg += "\n";
		String args = "";
		for(Object element : arguments) {
			args += element.toString();
			args += " ";
		}
		String output = "";
		if (SymbolTable.S_EQ(args, "") != true) {
			output += msg + " " + args;
			System.err.println(msg + " " + args);
		}
		output += msg + " in Zeile " + compileProcess.getPos().getLineNumber() + ", Spalte " + compileProcess.getPos().getColNumber() + " in der Datei\n" + compileProcess.getPos().getFilename();
		compileProcess.setCompilerMessage(compileProcess.getCompilerMessage() + output);
		System.err.println(msg + " in Zeile " + compileProcess.getPos().getLineNumber() + ", Spalte " + compileProcess.getPos().getColNumber() + " in der Datei\n" + compileProcess.getPos().getFilename());
		System.exit(-1);
	}
	
	public static void compilerWarning(CompileProcess compileProcess, String msg, Object... arguments) {
		msg += "\n";
		String args = "";
		for(Object element : arguments) {
			args += element.toString();
			args += " ";
		}
		String output = "";
		if (SymbolTable.S_EQ(args, "") != true) {
			output += msg + " " + args;
			System.err.println(msg + " " + args);
		}
		output += msg + " in Zeile " + compileProcess.getPos().getLineNumber() + ", Spalte " + compileProcess.getPos().getColNumber() + " in der Datei\n" + compileProcess.getPos().getFilename();
		System.err.println(msg + " in Zeile " + compileProcess.getPos().getLineNumber() + ", Spalte " + compileProcess.getPos().getColNumber() + " in der Datei\n" + compileProcess.getPos().getFilename());
		compileProcess.setCompilerMessage(compileProcess.getCompilerMessage() + output);
	}
	
	public static CompilerMessage compileFile(String filename, String outFilename, int flags) throws IOException {
		CompileProcess compileProcess;
		try {
			compileProcess = new CompileProcess(filename, outFilename, flags);
		} catch (FileNotFoundException e) {
			String output = "Die Datei " + filename + " konnte nicht gefunden werden.";
			System.out.println(output);
			return CompilerMessage.COMPILER_FAILED_WITH_ERRORS;
		}
		
		// Lexikalische Analyse
		LexProcess<Object> lexProcess;
		try {
			lexProcess = new LexProcess<Object>(compileProcess, null);
		} catch (NullPointerException e) {
			return CompilerMessage.COMPILER_FAILED_WITH_ERRORS;
		}
		if (Lexer.lex(lexProcess) != LexicalAnalysis.LEXICAL_ANALYSIS_ALL_OK) {
			return CompilerMessage.COMPILER_FAILED_WITH_ERRORS;
		}
		// Während des Parsens brauchen wir Zugriff auf die Tokens der lexikalischen Analyse
		compileProcess.setTokenVec(lexProcess.getTokenVec());
		// Durchführen des Parsens
		if (Parser.parse(compileProcess) != ParseMessage.PARSE_ALL_OK) {
			return CompilerMessage.COMPILER_FAILED_WITH_ERRORS;
		}
		// Berechnung der Clean-Code-Scores.
		CleanCodeScore cleanCodeScore;
		try {
			cleanCodeScore = new CleanCodeScore(compileProcess);
			compileProcess.setCleanCodeScore(cleanCodeScore);
		} catch (NullPointerException e) {
			return CompilerMessage.COMPILER_FAILED_WITH_ERRORS;
		}
		// Die Ergebnisse schreiben wir zur Auswertung in eine .csv-Datei.
		try {
			CsvOutputFileHandler.handleCsvOutputFile(compileProcess);
		} catch (IOException e) {
			return CompilerMessage.COMPILER_FAILED_WITH_ERRORS;
		}
		System.out.println("1. Clean-Code-Analyzer-Nachricht:\n");
		System.out.println(compileProcess.getCleanCodeScore().getAdvise());
		return CompilerMessage.COMPILER_FILE_COMPILED_OK;
	}
}
