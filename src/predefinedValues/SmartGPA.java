package predefinedValues;

import java.util.Date;

public class SmartGPA implements PredefinedValue {
	
	public final static String separator = "/";

	String baseString;
	String valueString;
	
	
	SmartGPA(double value, double base) {
		baseString = base+"";
		valueString = value +"";
	}
	
	public int compareTo(PredefinedValue o) {
		// TODO Auto-generated method stub
		double curDuration = fromString(toString());
		if(o instanceof SmartGPA) {
			double oDuration = fromString(o.toString());
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
		return PredefinedValuesType.GPA;
	}
	
	private double fromString(String s) {
		String[] sStr = s.split(separator);
		if(sStr.length >=2) {
			double value = Double.parseDouble(sStr[0]);
			double base = Double.parseDouble(sStr[1]);
			return value / base;
		}
		return 0.0;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return valueString + separator + baseString;
		
	}
}
