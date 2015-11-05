package evaluator;

public enum Comparator {
	Larger (">"),
	Smaller ("<"),
	Equal ("="),
	LargerOrEqual (">="),
	SmallerOrEqual ("<="),
	NotEqual ("!=");
	
	private final String comparator;
	
	private Comparator(String comparator) {
		this.comparator = comparator;
	}
	
	public String toString() {
		return this.comparator;
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
		} 
		return false;
	}
	
	
	
	
}
