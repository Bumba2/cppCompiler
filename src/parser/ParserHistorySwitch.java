package parser;

public class ParserHistorySwitch<T> {
	private HistoryCases<T> caseData;
	
	public ParserHistorySwitch() {
		this.caseData = null;
	}
	
	public ParserHistorySwitch(HistoryCases<T> caseData) {
		this.caseData = new HistoryCases<T>();
	}

	public HistoryCases<T> getCaseData() {
		return caseData;
	}

	public void setCaseData(HistoryCases<T> caseData) {
		this.caseData = caseData;
	}
}
