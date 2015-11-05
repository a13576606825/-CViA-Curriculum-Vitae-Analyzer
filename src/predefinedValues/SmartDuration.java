package predefinedValues;

import interpreter.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import evaluator.Comparator;

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
//		long curDuration = fromString(toString());
//		if(o instanceof SmartDate) {
//			long oDuration = fromString(o.toString());
//			if(curDuration < oDuration) {
//				return -1;
//			} else if(curDuration== oDuration) {
//				return 0;
//			} else {
//				return 1;
//			}
//		}
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
	private long getNumOfDays(String s) {
		try {
			Date sDate = sdf.parse(startDateString);
			Date eDate = sdf.parse(endDateString);
			long diffDays = (eDate.getTime() - sDate.getTime()) / (24 * 60 * 60 * 1000);
			return diffDays;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}
	public static SmartDuration fromString(String value) {
		try {
			String[] datesStr = value.split(separator);
			if(datesStr.length >=2) {
				Date sDate = sdf.parse(datesStr[0]);
				Date eDate = sdf.parse(datesStr[1]);
				return new SmartDuration(sDate, eDate);
			}
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Utils.debug("fail to covert String to SmartDate");
		}
		return null;
	}
	
	public static String[] getPossibleValues() {
		String[] returned = {"0.5", "1", "2", "3", "5", "10"};
		return returned;
	}
	@Override
	public boolean compare(Comparator comparator, String toCompare) {
		try {
			double toCompareDouble = Double.parseDouble(toCompare);
			double daysPerYear = 365;
			double self = getNumOfDays(toString());
			return comparator.compareTwoDouble(self/daysPerYear, toCompareDouble);
			
		} catch(NumberFormatException e) {
		  
		}
		
		
		return false;
	}

}
