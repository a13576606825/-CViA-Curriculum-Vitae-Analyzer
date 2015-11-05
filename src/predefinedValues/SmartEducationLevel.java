package predefinedValues;

public class SmartEducationLevel implements PredefinedValue {

	public static final String phd = "Phd";
	public static final String master = "Master";
	public static final String bachelor = "Bachelor";
	String[] levels = {bachelor, master, phd};
	
	
	
	private String levelString; 
	
	public SmartEducationLevel(String level) {
		levelString = level;
	}
	@Override
	public int compareTo(PredefinedValue o) {
		// TODO Auto-generated method stub
		int cur = fromString(toString());
		int ano = fromString(o.toString());
		if(cur < ano) {
			return -1;
		} else if(cur == ano) {
			return 0;
		} else {
			return 1;
		}
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return levelString;
	}
	@Override
	public PredefinedValuesType getType() {
		// TODO Auto-generated method stub
		return PredefinedValuesType.EducationLevel;
	}

	private int fromString(String s) {
		for(int i=0; i<levels.length; i++) {
			if(s.equalsIgnoreCase(levels[i])) {
				return i+1;
			}
		}
		return 0;
	}
	
	@Override
	public String[] getPossibleValues() {
		return levels;
	}
}
