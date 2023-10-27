package compiler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import cleanCodeAnalyzer.CleanCodeScore;

public class CsvOutputFileHandler {
	
	public static void handleCsvOutputFile(CompileProcess compileProcess) throws IOException {
		Path path = Paths.get("cleanCodeAppAnalysis.csv");
		File csvOutputFile = null;
		FileWriter fileWriter = null;
		int numberOfLines = -1;
		// Die Datei existiert schon.
		if (path.toFile().isFile()) {
			csvOutputFile = path.toFile();
			fileWriter = new FileWriter(csvOutputFile, true);
			// Wir benötigen die Anzahl der Zeilen zur Bestimmung des Indexes des neuen Eintrags
			numberOfLines += (int) Files.lines(path).count();
		}
		// Die Datei existiert noch nicht.
		else {
			csvOutputFile = new File("cleanCodeAppAnalysis.csv");
			fileWriter = new FileWriter(csvOutputFile);
			String firstLine = "vars_score_analyzer, methods_score_analyzer, class_score_analyzer, global_score_analyzer, total_score_analyzer, code_example\n";
			fileWriter.write(firstLine);
			numberOfLines++;
		}
		CleanCodeScore scores = compileProcess.getCleanCodeScore();
		String newLine = 
		String.valueOf(scores.getVariableScore()) + ", " +
		String.valueOf(scores.getMethodScore()) + ", " +
		String.valueOf(scores.getClassScore()) + ", " +
		String.valueOf(scores.getGlobalScore()) + ", " +
		String.valueOf(scores.getTotalScore()) + ", " +
		String.valueOf(numberOfLines + 1) + "\n";
		fileWriter.append(newLine);
		// Schließen der Datei.
		fileWriter.close();
	}
}
