package predefinedValues;


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
	
	@Override
	public String[] getPossibleValues() {
		return levels;
	}
}
