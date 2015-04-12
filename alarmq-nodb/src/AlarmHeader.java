import java.util.ArrayList;
import java.util.logging.Logger;


public class AlarmHeader {

	private final static Logger LOGGER = Logger.getLogger(AlarmHeader.class.getName()); 
	private String AlarmHeaderName;
	private int Occurrances;
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
		boolean foundAh = false;
		for (AlarmHeaderCorrelation ahc:  CorrelatedAHs) {
			if(aAlarmHeader.equals(ahc.getAlarmHeader())) {
				ahc.addMatch();
				foundAh = true;
				break;
			}
		}
		if (!foundAh) {
			CorrelatedAHs.add(new AlarmHeaderCorrelation(aAlarmHeader));
		}
	}
	

	public String print() {
		String ret = new String("AlarmHeader - " + AlarmHeaderName + "\n");
		for (AlarmHeaderCorrelation corrAh: CorrelatedAHs) {
			ret = ret + "  " + " \"" + corrAh.getAlarmHeader() + " \" matched " + " " + String.format("%.2f", corrAh.getProbabilityMatch()) + "%\n";
		} 
		
		return ret;
	}
	
	void updateTotalOccurrances (int nrofOccurrances) {
		LOGGER.fine(AlarmHeaderName + " - Setting total number of occurrances");
		for (AlarmHeaderCorrelation ahc:  CorrelatedAHs) {
				ahc.setTotalOccurrances(nrofOccurrances);
		}
	}

	public int getOccurrances() {
		return Occurrances;
	}

}
