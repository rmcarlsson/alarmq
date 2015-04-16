public class AlarmHeaderCorrelation {
	
	private int matches;
	private float probabilityMatch;
	private String AlarmHeader;
	
	public AlarmHeaderCorrelation(String aName) {
		AlarmHeader = aName;
		matches = 1;
	}

	public void addMatch() { 
		this.matches++;
	}

	public String getAlarmHeader() {
		return AlarmHeader;
	}
	
	public float getProbabilityMatch(int totalOccurrances) {
		probabilityMatch = ((float)((float)matches/(float)totalOccurrances)*100);
		return probabilityMatch;
	}
	
}
