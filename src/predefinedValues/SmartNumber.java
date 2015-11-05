package predefinedValues;

import evaluator.Comparator;

public class SmartNumber implements PredefinedValue {
	String numString;
	
	SmartNumber(String num) {
		numString = num;
	}
	@Override
	public int compareTo(PredefinedValue o) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public PredefinedValuesType getType() {
		// TODO Auto-generated method stub
		return PredefinedValuesType.Number;
	}

	public String toString() {
		// TODO Auto-generated method stub
		return numString;
		
	}
	
	public static SmartNumber fromString(String stringValue) {
		// TODO Auto-generated method stub
		return new SmartNumber(stringValue);
		
	}
	@Override
	public boolean compare(Comparator comparator, String toCompare) {
		// TODO Auto-generated method stub
		return false;
	}
}
