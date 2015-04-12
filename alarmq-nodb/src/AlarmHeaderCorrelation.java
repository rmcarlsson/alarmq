import java.util.logging.Logger;


public class AlarmHeaderCorrelation {
	
	private final static Logger LOGGER = Logger.getLogger(AlarmHeaderCorrelation.class.getName()); 
	private int matches;
	private int TotalOccurrances;
	private float probabilityMatch;


	private String AlarmHeader;
	
	public AlarmHeaderCorrelation(String aName) {
		LOGGER.finer("Creating new AlarmHeaderCollelation, e.g. match found");
		AlarmHeader = aName;
		matches = 1;
	}

	public void addMatch() {
		LOGGER.finer("Match found again for " + AlarmHeader); 
		this.matches++;
	}

	public String getAlarmHeader() {
		return AlarmHeader;
	}
	
	public int getTotalOccurrances() {
		return TotalOccurrances;
	}

	public void setTotalOccurrances(int totalOccurrances) {
		TotalOccurrances = totalOccurrances;
		probabilityMatch = ((float)((float)matches/(float)TotalOccurrances)*100);
		LOGGER.fine("   " + AlarmHeader + " - Matches is " + matches + " and total number of occurrances is " + TotalOccurrances + ". Probaility is " + probabilityMatch);
	}

	public float getProbabilityMatch() {
		return probabilityMatch;
	}
	
}
