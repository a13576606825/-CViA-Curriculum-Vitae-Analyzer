package predefinedValues;

import interpreter.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SmartDuration implements PredefinedValue {

	public static final String  DATE_FORMAT_NOW = "yyyy-MM-dd";
	public static final SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);

	private String startDateString;
	private String endDateString;
	public final static String separator = "- -";
	
	SmartDuration(Date startDate, Date endDate) {
		startDateString = sdf.format(startDate);
		endDateString =  sdf.format(endDate);
		
	}
	
	@Override
	public int compareTo(PredefinedValue o) {
		long curDuration = fromString(toString());
		if(o instanceof SmartDate) {
			long oDuration = fromString(o.toString());
			if(curDuration < oDuration) {
				return -1;
			} else if(curDuration== oDuration) {
				return 0;
			} else {
				return 1;
			}
		}
		return 0;
	}

	@Override
	public PredefinedValuesType getType() {
		// TODO Auto-generated method stub
		return PredefinedValuesType.Duration;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return startDateString + separator + endDateString;
	}
	
	
	// return num of days in the duration
	protected long fromString(String s) {
		try {
			String[] datesStr = s.split(separator);
			if(datesStr.length >=2) {
				Date sDate = sdf.parse(datesStr[0]);
				Date eDate = sdf.parse(datesStr[1]);
				long diffDays = (sDate.getTime() - sDate.getTime()) / (24 * 60 * 60 * 1000);
				return diffDays;
			}
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Utils.debug("fail to covert String to SmartDate");
		}
		return -1;
	}


}
