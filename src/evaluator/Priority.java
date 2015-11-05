package evaluator;

public enum Priority {
	High ("high"),
	Medium ("medium"),
	Low ("low");
	
	private final String priority;
	
	private Priority(String priority) {
		this.priority = priority;
	}
	
	public String toString() {
		return this.priority;
	}
}
