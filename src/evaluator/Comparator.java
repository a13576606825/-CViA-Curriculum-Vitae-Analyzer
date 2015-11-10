package evaluator;

public enum Comparator {
	Larger (">"),
	Smaller ("<"),
	Equal ("="),
	LargerOrEqual (">="),
	SmallerOrEqual ("<="),
	NotEqual ("!="),
	Empty ("");
	
	private final String comparator;
	
	private Comparator(String comparator) {
		this.comparator = comparator;
	}
	
	public String toString() {
		return this.comparator;
	}
	
	public static Comparator getComparator(String s) {
		if (s.equals(">")) {
			return Comparator.Larger;
		} else if (s.equals("<")) {
			return Comparator.Smaller;
		} else if (s.equals("=")) {
			return Comparator.Equal;
		} else if (s.equals(">=")) {
			return Comparator.LargerOrEqual;
		} else if (s.equals("<=")) {
			return Comparator.SmallerOrEqual;
		} else if (s.equals("!=")) {
			return Comparator.NotEqual;
		} else {
			return Comparator.Empty;
		}
	}
	
	public boolean compareTwoDouble(double s1, double s2) {
		if(this == Larger) {
			return s1>s2;
		} else if(this == Smaller) {
			return s1<s2;
		} else if(this == Equal) {
			return s1 == s2;
		} else if (this == LargerOrEqual) {
			return s1 >= s2;
		} else if (this == SmallerOrEqual) {
			return s1 <= s2;
		} else if(this == NotEqual) {
			return s1 != s2;
		}  else if(this == Empty) {
			return true;
		}
		return false;
	}
	
	
	
	
}
