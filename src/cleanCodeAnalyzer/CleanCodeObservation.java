package cleanCodeAnalyzer;

import lexer.Position;
import parser.Node;

// Eine Clean-Code-Beobachtung gibt an, wie sauber eine bestimmte Stelle im Code
// ist. Dabei wird ein Tipp erstellt (falls nötig) und es werden Punkte vergeben.
public class CleanCodeObservation<T> implements Comparable<T>{
	private String advise; // Welchen Hinweis möchten wir dem User geben?
	private int cleanCodeObservationScore; // Wie viele Punkte hat es für den Eintrag gegeben?
	private int highestPossibleScoreForThisObservation; // Wie viele Punkte waren erreichbar für den Eintrag?
	private Node<T> entity; // Auf welche Entität im Code bezieht sich die Beobachtung?
	private CleanCodeAnalysisBroadCategory broadCategory; // Zu welcher Oberkategorie der Analyse gehört die Beobachtung?
	private CleanCodeAnalysisConcreteCategory concreteCategory;
	private Position pos; // Wo befindet sich die Entität im Code? (Zeile und Spalte).
	
	public CleanCodeObservation(String advise, int cleanCodeObservationScore, int highestPossibleScoreForThisObservation, Node<T> entity, CleanCodeAnalysisBroadCategory broadCategory, CleanCodeAnalysisConcreteCategory concreteCategory, Position pos) {
		this.advise = advise;
		this.cleanCodeObservationScore = cleanCodeObservationScore;
		this.highestPossibleScoreForThisObservation = highestPossibleScoreForThisObservation;
		this.entity = entity;
		this.broadCategory = broadCategory;
		this.concreteCategory = concreteCategory;
		this.pos = pos;
	}
	
	public CleanCodeObservation(CleanCodeAnalysisBroadCategory broadCategory, CleanCodeAnalysisConcreteCategory concreteCategory) {
		this.advise = "";
		this.broadCategory = broadCategory;
		this.concreteCategory = concreteCategory;
	}
	
	public CleanCodeObservation(CleanCodeAnalysisBroadCategory broadCategory, CleanCodeAnalysisConcreteCategory concreteCategory, Position pos) {
		this.advise = "In Zeile " + pos.getLineNumber() + ", Spalte " + pos.getColNumber() + ":\n";
		this.broadCategory = broadCategory;
		this.concreteCategory = concreteCategory;
		this.pos = pos;
	}
	
	public CleanCodeObservation(CleanCodeAnalysisBroadCategory broadCategory, CleanCodeAnalysisConcreteCategory concreteCategory, Position pos, Node<T> entity) {
		if (pos != null) {
			this.advise = "In Zeile " + pos.getLineNumber() + ", Spalte " + pos.getColNumber() + ":\n";
		}
		else {
			this.advise = "";
		}
		this.broadCategory = broadCategory;
		this.concreteCategory = concreteCategory;
		this.pos = pos;
		this.entity = entity;
	}
	
	public String getAdvise() {
		return advise;
	}
	
	public void setAdvise(String advise) {
		this.advise = advise;
	}
	
	public int getCleanCodeObservationScore() {
		return cleanCodeObservationScore;
	}
	
	public void setCleanCodeObservationScore(int cleanCodeObservationScore) {
		this.cleanCodeObservationScore = cleanCodeObservationScore;
	}

	public Node<T> getEntity() {
		return entity;
	}

	public void setEntity(Node<T> entity) {
		this.entity = entity;
	}

	public Position getPos() {
		return pos;
	}

	public void setPos(Position pos) {
		this.pos = pos;
	}

	public CleanCodeAnalysisBroadCategory getBroadCategory() {
		return this.broadCategory;
	}

	public void setCategory(CleanCodeAnalysisBroadCategory broadCategory) {
		this.broadCategory = broadCategory;
	}

	public int getHighestPossibleScoreForThisObservation() {
		return highestPossibleScoreForThisObservation;
	}

	public void setHighestPossibleScoreForThisObservation(int highestPossibleScoreForThisObservation) {
		this.highestPossibleScoreForThisObservation = highestPossibleScoreForThisObservation;
	}

	public CleanCodeAnalysisConcreteCategory getConcreteCategory() {
		return concreteCategory;
	}

	public void setConcreteCategory(CleanCodeAnalysisConcreteCategory concreteCategory) {
		this.concreteCategory = concreteCategory;
	}

	@Override
	public int compareTo(T observation) {
		@SuppressWarnings("unchecked")
		CleanCodeObservation<Object> cleanCodeObservation = (CleanCodeObservation<Object>) observation;
		if (this.getPos() == null && cleanCodeObservation.getPos() == null) {
			return 0;
		}
		if (this.getPos() == null) {
			return -1;
		}
		else if (cleanCodeObservation.getPos() == null) {
			return 1;
		}
		else if (this.getPos().getLineNumber() > cleanCodeObservation.getPos().getLineNumber()) {
			return 1;
		}
		else if (this.getPos().getLineNumber() < cleanCodeObservation.getPos().getLineNumber()) {
			return -1;
		}
		else if (this.getPos().getLineNumber() == cleanCodeObservation.getPos().getLineNumber()) {
			if (this.getPos().getColNumber() > cleanCodeObservation.getPos().getColNumber()) {
				return 1;
			}
			else if (this.getPos().getColNumber() < cleanCodeObservation.getPos().getColNumber()) {
				return -1;
			}
		}
		return 0;
	}
	
	public int getNumberOfNewlinesInAdvise() {
		int numberOfNewlines = 0;
		for(int i = 0; i < this.getAdvise().length(); i++) {
			if (this.getAdvise().charAt(i) == '\n') {
				numberOfNewlines++;
			}
		}
		return numberOfNewlines;
	}
}
