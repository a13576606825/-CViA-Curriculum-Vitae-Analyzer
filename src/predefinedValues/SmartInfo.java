package predefinedValues;

import evaluator.Comparator;

public class SmartInfo implements PredefinedValue {
	private String info;
	
	public SmartInfo(String info) {
		this.info = info;
	}
	@Override
	public int compareTo(PredefinedValue o) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public PredefinedValuesType getType() {
		// TODO Auto-generated method stub
		return PredefinedValuesType.Info;
	}
	
	public String toString() {
		// TODO Auto-generated method stub
		return info;
		
	}
	
	public static SmartInfo fromString(String stringValue) {
		// TODO Auto-generated method stub
		return new SmartInfo(stringValue);
		
	}
	
	
	@Override
	public boolean compare(Comparator comparator, String toCompare) {
		// TODO Auto-generated method stub
		return false;
	}
}
