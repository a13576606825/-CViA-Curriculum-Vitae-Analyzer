package predefinedValues;

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
	@Override
	public String[] getPossibleValues() {
		String[] returned = {"null"};
		return returned;
	}
}
