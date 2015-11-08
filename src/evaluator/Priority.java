package evaluator;

public enum Priority {
	High ("high"),
	Medium ("medium"),
	Low ("low"),
	Empty ("");
	
	private final String priority;
	
	private Priority(String priority) {
		this.priority = priority;
	}
	
	public String toString() {
		return this.priority;
	}
	
	public static Priority getPriority(String s) {
		if (s.equals("high")) {
			return Priority.High;
		} else if (s.equals("medium")) {
			return Priority.Medium;
		} else if (s.equals("low")) {
			return Priority.Low;
		} else {
			return Priority.Empty;
		}
	}
	
	public int toGrade() {
		if(this == High) {
			return 10;
		} else if(this == Medium) {
			return 3;
		} else if(this == Low) {
			return 1;
		} else {
			return 0;
		}
		
	}
}
