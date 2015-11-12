package predefinedValues;

import java.util.Date;

import evaluator.Comparator;

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
		double curDuration = getGPAValue(toString());
		if(o instanceof SmartGPA) {
			double oDuration = getGPAValue(o.toString());
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
	
	private double getGPAValue(String s) {
		String[] sStr = s.split(separator);
		if(sStr.length >=2) {
			double value = Double.parseDouble(sStr[0]);
			double base = Double.parseDouble(sStr[1]);
			return value / base;
		}
		return 0.0;
	}
	
	
	public static SmartGPA fromString(String stringValue) {
		String[] sStr = stringValue.split(separator);
		if(sStr.length >=2) {
			double value = Double.parseDouble(sStr[0]);
			double base = Double.parseDouble(sStr[1]);
			return new SmartGPA(value, base);
		}
		return null;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return valueString + separator + baseString;
		
	}
	
	
	public static String[] getPossibleValues() {
		String[] returned = {"0.5", "0.6", "0.7", "0.8", "0.9"};
		return returned;
	}
	
	@Override
	public boolean compare(Comparator comparator, String toCompare) {
		try {
			double toCompareDouble = Double.parseDouble(toCompare);
			double self = getGPAValue(toString());
			return comparator.compareTwoDouble(self, toCompareDouble);
			
		} catch(NumberFormatException e) {
			return false;
		}
		
		
		
	}
}
