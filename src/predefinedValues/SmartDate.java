package predefinedValues;

import interpreter.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SmartDate implements PredefinedValue {
	
	public static final String  DATE_FORMAT_NOW = "yyyy-MM-dd";
	public static final SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
	private String dateString;
	
	
	public SmartDate(Date date){
		
		dateString = sdf.format(date);
		
	}
	
	@Override
	public int compareTo(PredefinedValue o) {
		Date curDate = fromString(dateString);
		if(curDate != null && o instanceof SmartDate) {
			Date oDate = fromString(o.toString());
			if(oDate != null) {
				return curDate.compareTo(oDate);
			}
		}
		return 0;
	}
	
	@Override
	public PredefinedValuesType getType() {
		return PredefinedValuesType.Date;
	}
	

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return dateString;
	}

	protected Date fromString(String s) {
		try {
			Date date = sdf.parse(s);
			return date;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Utils.debug("fail to covert String to SmartDate");
		}
		return null;
	}




}
