package cleanCodeAnalyzer;

import compiler.CompileProcess;
import helpers.Vector;
import lexer.SymbolTable;

public class CleanCodeScore {
	private double variableScore;
	private double methodScore;
	private double classScore;
	private double globalScore;
	private double totalScore;
	private String advise;
	
	public static void calculateScores(CompileProcess compileProcess, CleanCodeScore cleanCodeScore) {
		Vector<CleanCodeObservation<Object>> cleanCodeObservationVec = compileProcess.getCleanCodeObservations();
		double variableScore = 0;
		double variablePossibleScore = 0;
		double variableRelativeScore;
		double methodScore = 0;
		double methodPossibleScore = 0;
		double methodRelativeScore;
		double classScore = 0;
		double classPossibleScore = 0;
		double classRelativeScore;
		double globalScore = 0;
		double globalPossibleScore = 0;
		double globalRelativeScore;
		double totalScore;
		for(int i = 0; i < cleanCodeObservationVec.vectorCount(); i++) {
			CleanCodeObservation<Object> observation = cleanCodeObservationVec.vectorPeekPtrAt(i);
			if (observation.getBroadCategory() == CleanCodeAnalysisBroadCategory.VARIABLE_ANALYSIS) {
				variableScore += observation.getCleanCodeObservationScore();
				variablePossibleScore += observation.getHighestPossibleScoreForThisObservation();
			}
			else if (observation.getBroadCategory() == CleanCodeAnalysisBroadCategory.METHOD_ANALYSIS) {
				methodScore += observation.getCleanCodeObservationScore();
				methodPossibleScore += observation.getHighestPossibleScoreForThisObservation();
			}
			else if (observation.getBroadCategory() == CleanCodeAnalysisBroadCategory.CLASS_ANALYSIS) {
				classScore += observation.getCleanCodeObservationScore();
				classPossibleScore += observation.getHighestPossibleScoreForThisObservation();
			}
			else if (observation.getBroadCategory() == CleanCodeAnalysisBroadCategory.GLOBAL_ANALYSIS) {
				globalScore += observation.getCleanCodeObservationScore();
				globalPossibleScore += observation.getHighestPossibleScoreForThisObservation();
			}
		}
		if (variableScore < 0) {
			variableScore = 0;
		}
		if (methodScore < 0) {
			methodScore = 0;
		}
		if (classScore < 0) {
			classScore = 0;
		}
		if (globalScore < 0) {
			globalScore = 0;
		}
		if (variablePossibleScore > 0) {
			variableRelativeScore = variableScore / variablePossibleScore * 10;
		}
		else {
			variableRelativeScore = 10.0;
		}
		cleanCodeScore.setVariableScore(variableRelativeScore);
		if (methodPossibleScore > 0) {
			methodRelativeScore = methodScore / methodPossibleScore * 10;
		}
		else {
			methodRelativeScore = 10.0;
		}
		cleanCodeScore.setMethodScore(methodRelativeScore);
		if (classPossibleScore > 0) {
			classRelativeScore = classScore / classPossibleScore * 10;
		}
		else {
			classRelativeScore = 10.0;
		}
		cleanCodeScore.setClassScore(classRelativeScore);
		if (globalPossibleScore > 0) {
			globalRelativeScore = globalScore / globalPossibleScore * 10;
		}
		else {
			globalRelativeScore = 10.0;
		}
		cleanCodeScore.setGlobalScore(globalRelativeScore);
		totalScore = variableRelativeScore * 0.25 + methodRelativeScore * 0.25 + classRelativeScore * 0.25 + globalRelativeScore * 0.25;
		cleanCodeScore.setTotalScore(totalScore);

	}
	
	public static void organizeAdvises(CompileProcess compileProcess, CleanCodeScore cleanCodeScore) {
		Vector<String> advises = Vector.vectorCreate();
		boolean positionAlreadyFound = false;
		Vector<CleanCodeObservation<Object>> cleanCodeObservations = compileProcess.getCleanCodeObservations();
		for(int i = 0; i < cleanCodeObservations.vectorCount(); i++) {
			CleanCodeObservation<Object> observation = cleanCodeObservations.vectorPeekPtrAt(i);
			String advise = observation.getAdvise();
			if (advise == null) {
				continue;
			}
			String advisePositionAsString;
			if (observation.getPos() != null) {
				advisePositionAsString = advise.substring(0, advise.indexOf('\t'));
				for(int j = 0; j < advises.vectorCount(); j++) {
					String newAdvise = advises.vectorPeekPtrAt(j);
					String newAdvisePositionAsString = newAdvise.substring(0, advise.indexOf('\t'));
					if (SymbolTable.S_EQ(advisePositionAsString, newAdvisePositionAsString)) {
						positionAlreadyFound = true;
						String strToAdd = newAdvise.substring(observation.getAdvise().indexOf('\t'));
						String concatenatedAdvise = advise + strToAdd;
						advises.vectorPop();
						advises.vectorPush(concatenatedAdvise);
					}
				}
			}
			if (!(positionAlreadyFound)) {
				advises.vectorPush(advise);
			}
			positionAlreadyFound = false;
		}
		String output = "";
		for(int i = 0; i < advises.vectorCount(); i++) {
			output += advises.vectorPeekPtrAt(i);
		}
		cleanCodeScore.setAdvise(output);
	}
	
	public CleanCodeScore(CompileProcess compileProcess) {
		CleanCodeScore.calculateScores(compileProcess, this);
		CleanCodeScore.organizeAdvises(compileProcess, this);
	}

	public double getVariableScore() {
		return variableScore;
	}

	public void setVariableScore(double variableScore) {
		this.variableScore = variableScore;
	}

	public double getClassScore() {
		return classScore;
	}

	public void setClassScore(double classScore) {
		this.classScore = classScore;
	}

	public double getMethodScore() {
		return methodScore;
	}

	public void setMethodScore(double methodScore) {
		this.methodScore = methodScore;
	}

	public double getGlobalScore() {
		return globalScore;
	}

	public void setGlobalScore(double globalScore) {
		this.globalScore = globalScore;
	}

	public double getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(double totalScore) {
		this.totalScore = totalScore;
	}

	public String getAdvise() {
		return advise;
	}

	public void setAdvise(String advise) {
		this.advise = advise;
	}
}
