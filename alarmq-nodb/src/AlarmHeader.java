import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;


public class AlarmHeader {

	private final static Logger LOGGER = Logger.getLogger(AlarmHeader.class.getName()); 
	private String AlarmHeaderName;
	private int Occurrances;
	private Map<String, AlarmHeaderCorrelation> CorrAlarmHeaders = new HashMap<String, AlarmHeaderCorrelation>();

	private ArrayList<AlarmHeaderCorrelation> CorrelatedAHs;
	
	public String getAlarmHeaderName() {
		return AlarmHeaderName;
	}
	
	public AlarmHeader(String aName) {
		LOGGER.finer("Creating new AlarmHeader");
		AlarmHeaderName = aName;
		CorrelatedAHs = new ArrayList<AlarmHeaderCorrelation>();
		Occurrances = 1;
	}
	
	public void addOccurrance () {
		Occurrances++;
	}
	
	
	boolean isSameAlarmHeader(String aAlarmHeader) {
	  return AlarmHeaderName.equals(aAlarmHeader);
	}
	
	
	public void addMatch(String aAlarmHeader) {
		if( !CorrAlarmHeaders.containsKey(aAlarmHeader)) {
			CorrAlarmHeaders.put(aAlarmHeader, new AlarmHeaderCorrelation(aAlarmHeader));
		}
		else {
			CorrAlarmHeaders.get(aAlarmHeader).addMatch();
		}
	}
	

	public void print() {
		System.out.println("AlarmHeader - " + AlarmHeaderName + " --  Probability of correlated alarms");
		Iterator it = CorrAlarmHeaders.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, AlarmHeaderCorrelation> pair = (Map.Entry<String, AlarmHeaderCorrelation>)it.next();
		    System.out.println("   " + pair.getKey() + " has prob. " + String.format("%.2f", pair.getValue().getProbabilityMatch(Occurrances)));
		}
		System.out.println("\n");

	}

	public int getOccurrances() {
		return Occurrances;
	}

}
