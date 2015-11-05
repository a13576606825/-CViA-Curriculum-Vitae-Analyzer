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
}
