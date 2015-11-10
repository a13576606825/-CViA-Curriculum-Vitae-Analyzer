package evaluator;

public class Requirement {
	
	private String category;
	private String type;
	private String keyword;
	private Comparator comparator;
	private String value;
	private Priority priority;
	
	public Requirement() {
	
	}
	
	public Requirement(String category, String type, String keyword, Comparator comparator, String value, Priority priority) {
		this.category = category;
		this.type = type;
		this.keyword = keyword;
		this.comparator = comparator;
		this.value = value;
		this.priority = priority;
	}
	
	public String getCategory() {
		return this.category;
	}
	
	public String getType() {
		return this.type;
	}
	
	public String getKeyword() {
		return this.keyword;
	}
	
	public Comparator getComparator() {
		return this.comparator;
	}
	
	public String getValue() {
		return this.value;
	}
	
	public Priority getPriority() {
		return this.priority;
	}
	
}
