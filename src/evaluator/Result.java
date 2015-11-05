package evaluator;

import java.util.ArrayList;

public class Result implements Comparable<Result> {
	final String fileName;
	//final String Email;
	final int marks;
	//final String personalInfo;
	final ArrayList<String> filterString;
	
	
	public Result(String fileName, int marks, ArrayList<String> filterString) {
		this.fileName = fileName;
		this.marks = marks; 
		this.filterString = filterString;
	}


	@Override
	public int compareTo(Result o) {
		if(marks > o.marks) {
			return 1;
		} else if(marks < o.marks) {
			return -1;
		} else {
			return 0;
		}
	}
}
