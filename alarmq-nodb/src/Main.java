import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Logger;


public class Main {

	private final static Logger LOGGER = Logger.getLogger(Main.class.getName()); 
	private ArrayList<AlarmHeader> ahList;
	private final static long CORRELATION_TIME_WINDOW = 3;


	public Main() {
		ahList = new ArrayList<AlarmHeader>();
	}

	public static void main(String[] args) {
		Main main = new Main();
		main.analyze();
		main.print();
	}

	private void print() {
		for (AlarmHeader ah: ahList)
			ah.print();
	}

	private AlarmHeader addAlarmHeader( String aAlarmHeaderName) {
		
		AlarmHeader ret = null;
		boolean foundAh = false;
		
		for (AlarmHeader ah: ahList) {
			if (ah.isSameAlarmHeader(aAlarmHeaderName)) {
				ret = ah;
				ah.addOccurrance();
				foundAh = true;
				break;
			}
		}
		if (!foundAh) {
			ret = new AlarmHeader(aAlarmHeaderName);
			ahList.add(ret);
		}
		return ret;
	}

	/**
	 * @param args
	 */
	public void analyze() {


		ReadCsv reader = new ReadCsv();
		List<Tuple> tupleList = null;
		try {
			tupleList = reader.readCvs("/home/carltmik/Downloads/ALC4-3.csv");
		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Collections.sort(tupleList);

		for(Tuple analysisTuple: tupleList) {

			AlarmHeader listAlarmHeader = null;
			ArrayList<AlarmHeader> corrAhs = new ArrayList<AlarmHeader>();


			listAlarmHeader = addAlarmHeader(analysisTuple.getAh());


			final long TWO_MINUTE_IN_MILLIS=(CORRELATION_TIME_WINDOW*60*100);//millisecs
			Date tm = analysisTuple.getTm();
			Date sw_after = new Date(tm.getTime() +  TWO_MINUTE_IN_MILLIS);
			Date sw_before = new Date(tm.getTime() -  TWO_MINUTE_IN_MILLIS);

			LOGGER.finer("Working with Alarm Header \"" + analysisTuple.getAh() + "\" at " + analysisTuple.getTm().toString());
			{
				ListIterator<Tuple> li = tupleList.listIterator();
				Tuple loopTuple = null;

				while(li.hasNext()) {
					loopTuple = li.next();
					if( loopTuple.getTm().after(sw_before)) {
						break;
					}
				}

				while(li.hasNext()) {
					loopTuple = li.next();
					if( loopTuple.getMo().equals(analysisTuple.getMo())) {
						boolean found = false;
						for (AlarmHeader ah: corrAhs) {
							found = true;
							break;
						}
						if (!found) {
							/*
							 * If the correlated alarm header is the same as the analysis alarm header it should be 
							 * excluded. E.g. if time mark is the same and name is the same, don't add it.
							 */
							if ((loopTuple.compareTo(analysisTuple) != 0) || (!loopTuple.getAh().equals(analysisTuple.getAh()))) {
								corrAhs.add(new AlarmHeader(loopTuple.getAh()));
							}
						}
					}
					if( !loopTuple.getTm().after(sw_after)) {
						/*
						 * We have passed the correlation time window. Add the identified correlated alarm headers and
						 * break to get next tuple.
						 */
						for (AlarmHeader ah: corrAhs) {
							listAlarmHeader.addMatch(ah.getAlarmHeaderName());
						}
						break;
					}
				}
			}

		}
	}
}