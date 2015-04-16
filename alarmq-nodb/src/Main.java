import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Logger;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;


public class Main extends SwingWorker<Void, String>  {

	private final static Logger LOGGER = Logger.getLogger(Main.class.getName()); 
	private ArrayList<AlarmHeader> ahList;
	private final static long CORRELATION_TIME_WINDOW = 3;
	private File file;
	private JLabel label;
	private JPanel pane;

	public Main(File aFile, JPanel aPane) {
		ahList = new ArrayList<AlarmHeader>();
		 file = aFile;
		 label = new JLabel("Please choose a file");
		 pane = aPane;
	 
	}

	public void print() {
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
	 * @throws  
	 */
	public void analyze()  {
		

		publish("Reading CSV file");
		ReadCsv reader = new ReadCsv();
		List<Tuple> tupleList = null;
		try {
			tupleList = reader.readCvs(file.getAbsolutePath());
		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		publish("Sorting tuples");
		Collections.sort(tupleList);
		publish("Statistic analysis");

		float max = tupleList.size();
		float i=0;
		SimpleDateFormat sdf = new SimpleDateFormat("YYY MMM");
		String analysisDate = "no date";
		ListIterator<Tuple> liOut = tupleList.listIterator();

		// Iterate in reverse.
		while(liOut.hasNext()) {
		//for(Tuple analysisTuple: tupleList) {
			Tuple analysisTuple = liOut.next();
			if (!sdf.format(analysisTuple.getTm()).equals(analysisDate)) {
				analysisDate = sdf.format(analysisTuple.getTm());
				publish("Statistic analysis - analyzing " + analysisDate);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}

			i++;
			setProgress((int)(i*100/max));
				
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
		//publish("Done");
	}

	@Override
	protected Void doInBackground() throws Exception {
		analyze();
		print();
		return null;
	}
	
	protected void process(List<String> chunks) {
        for (String text : chunks) {
   		 	label.setText(text);
        }
        pane.revalidate();
        pane.repaint();

    }
	
	
}