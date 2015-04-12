import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class ReadCsv {

	private final static Logger LOGGER = Logger.getLogger(ReadCsv.class.getName()); 

	public List<Tuple> readCvs(String csvFileName) throws NumberFormatException, IOException  {

		List<Tuple> list = new ArrayList<Tuple>();

		// open file input stream
		BufferedReader reader;
		reader = new BufferedReader(new FileReader(csvFileName));

		// read file line by line
		String line = null;
		Scanner scanner = null;
		int index = 0;

		int lines = 0;
		while ((line = reader.readLine()) != null) {
			boolean skipTuple = false;
			Tuple tuple = new Tuple();
			scanner = new Scanner(line);
			scanner.useDelimiter(";");
			while (scanner.hasNext()) {
				String data = scanner.next();
				if (index == 0)  {
					String inter = data.replace(",", "");
					tuple.setId((Integer.parseInt(inter)));
				}
				else if (index == 13) {
					Date date = parseDate(data);
					if(date != null ) {
						tuple.setTm(date);
					}
					else {
						scanner.close();
						lines++;
						LOGGER.finest("Invalid date, skipping tuple" );
						skipTuple = true;
						break;
					}
				}
				else if (index == 14)
					tuple.setMo(data);
				else if (index == 15)
					tuple.setAt(data);
				else if (index == 16) {
					Date date = parseDate(data);
					if(date != null ) {
						tuple.setEv_tm(date);
					}
					else {
						scanner.close();
						lines++;
						LOGGER.finest("Invalid date, skipping tuple" );
						skipTuple = true;
						break;
					}
				}
				else if (index == 20)
					tuple.setSev(data);
				else if (index == 25)
					tuple.setAh(data);
				index++;
			}
			index = 0;
			if (!skipTuple) {
				list.add(tuple);
				scanner.close();
				lines++;
			}
		}
		

		//close reader
		reader.close();
		
		return list;

	}
	
	private Date parseDate(String data) {
		
		Date ret = null;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy hh:mm:ss a");
		                                           // 08/Feb/2015 11:14:16 a.m.
		
		// Make SimpleDateFormat handle the somewhat odd AM format (a.m. -> AM)
		String inter = data.replace("a.m.", "AM");
		inter = inter.replace("p.m.", "PM");
		
		try { 
			ret = sdf.parse(inter);
		}
		catch (ParseException e) {
			LOGGER.fine("Invalid date, skipping tuple" );
		}

		// Returns null if parsing fails
		return ret;
	}
	
}