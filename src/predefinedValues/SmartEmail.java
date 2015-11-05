package predefinedValues;

import evaluator.Comparator;


public class SmartEmail implements PredefinedValue {
	private String emailString;
	
	SmartEmail(String email) {
		emailString = email;
	}
	
	
	@Override
	public int compareTo(PredefinedValue o) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public PredefinedValuesType getType() {
		// TODO Auto-generated method stub
		return PredefinedValuesType.Email;
	}
	public String toString() {
		// TODO Auto-generated method stub
		return emailString;	
	}
	public static SmartEmail fromString(String value) {
		return new SmartEmail(value);
	}
	
	
	@Override
	public boolean compare(Comparator comparaotr, String toCompare) {
		// TODO Auto-generated method stub
		return false;
	}
}
