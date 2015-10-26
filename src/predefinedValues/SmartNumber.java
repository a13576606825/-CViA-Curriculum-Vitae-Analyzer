package predefinedValues;

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
}
