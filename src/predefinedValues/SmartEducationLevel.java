package predefinedValues;

import evaluator.Comparator;

public class SmartEducationLevel implements PredefinedValue {

	public static final String phd = "Phd";
	public static final String master = "Master";
	public static final String bachelor = "Bachelor";
	static String[] levels = {bachelor, master, phd};
	
	
	
	private String levelString; 
	
	public SmartEducationLevel(String level) {
		levelString = level;
	}
	@Override
	public int compareTo(PredefinedValue o) {
		// TODO Auto-generated method stub
		int cur = getEducationLevelRank(toString());
		int ano = getEducationLevelRank(o.toString());
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

	private static int getEducationLevelRank(String s) {
		for(int i=0; i<levels.length; i++) {
			if(s.equalsIgnoreCase(levels[i])) {
				return i+1;
			}
		}
		return 0;
	}
	
	public static SmartEducationLevel fromString(String value) {
		if(getEducationLevelRank(value) != 0) {
			return new SmartEducationLevel(value);
		}
		return null;
	}
	
	
	public static String[] getPossibleValues() {
		return levels;
	}
	
	@Override
	public boolean compare(Comparator comparator, String toCompare) {
		
		double toCompareDouble = (double)getEducationLevelRank(toCompare);
		if(toCompareDouble == 0.0) {
			return false;
		} 
		double self = (double)getEducationLevelRank(toString());
		return comparator.compareTwoDouble(self, toCompareDouble);
	}
}
