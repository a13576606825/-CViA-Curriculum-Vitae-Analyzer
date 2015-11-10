package evaluator;

import java.util.ArrayList;

public class Result implements Comparable<Result> {
	private final String fileName;
	//final String Email;
	private final int marks;
	//final String personalInfo;
	private final ArrayList<String> filterString;	
	
	public Result(String fileName, int marks, ArrayList<String> filterString) {
		this.fileName = fileName;
		this.marks = marks; 
		this.filterString = filterString;
	}
	
	public String getfileName() {
		return this.fileName;
	}
	
	public int getMark() {
		return this.marks;
	}
	
	public ArrayList<String> getFilterString() {
		return this.filterString;
	}

	@Override
	public int compareTo(Result o) {
		if(marks > o.marks) {
			return -1;
		} else if(marks < o.marks) {
			return 1;
		} else {
			return 0;
		}
	}
}
