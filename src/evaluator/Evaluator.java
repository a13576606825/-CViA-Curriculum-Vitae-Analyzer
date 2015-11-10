package evaluator;

import interpreter.InterpreterRule;
import interpreter.SmartDictionary;
import interpreter.SmartInterpreter;
import interpreter.CategoryRule.CategoryEntry;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import edu.emory.mathcs.backport.java.util.Collections;
import predefinedValues.PredefinedValue;
import predefinedValues.PredefinedValuesFactory;

public class Evaluator {

	private HashMap<String, JSONObject> cvMap;
	
	public Evaluator() {
		cvMap = new HashMap<String, JSONObject>();
	}
	
	public void addCV(File cvFile) {
		String name = cvFile.getName();
		if(!cvMap.containsKey(name)) {
			JSONObject data = new SmartInterpreter(cvFile).build();
			cvMap.put(name, data);
		}
	}
	
	public void addJsonObj(JSONObject json) {
		
	}
	
	public void removeCV(File cvFile) {
		String name = cvFile.getName();
		if(cvMap.containsKey(name)) {
			cvMap.remove(name);
		}
	}
	
	public ArrayList<Result> query(ArrayList<Requirement> filters) {
		
		ArrayList<Result> resultList = new ArrayList<Result>();
		for (Entry<String, JSONObject> cvEntry : cvMap.entrySet()) {
		    String currentName = cvEntry.getKey();
		    int currentScore = 0;
		    JSONObject currentCV = cvEntry.getValue();
		    ArrayList<String> filterStringList = new ArrayList<String>();
		    for (Requirement filter: filters) {
		    	String category = filter.getCategory();
		    	String type = filter.getType();
		    	String keyword = filter.getKeyword();
		    	String toCompare = filter.getValue();
		    	int PriorityWeight = filter.getPriority().toGrade();
		    	Comparator comparator = filter.getComparator();
		    			    	
		    	JSONArray typeArray = (JSONArray) currentCV.get(category);
		    
		    	if(typeArray != null) {
		    		String auxilaryInfo = ""; 
			    	//boolean foundMatch = false;

			    	for(Iterator iterator = typeArray.iterator(); iterator.hasNext();) {
						JSONObject entry =  (JSONObject)iterator.next();
						if(type==null || type.trim().equalsIgnoreCase("")) {
							// only do string match
							String info = (String)entry.get("info");
							if(keyword == "" || SmartDictionary.containsIgnoreConjugation(info, keyword)) {
								currentScore += PriorityWeight;
								auxilaryInfo += "|Matched| " + info ;
							}
									
						} else {
							if(entry.containsKey(type)) {
								PredefinedValue value = PredefinedValuesFactory.fromString((String)entry.get(type), type);
								String info = (String)entry.get("info");
								
								if(value != null &&value.compare(comparator, toCompare) && (keyword == "" || SmartDictionary.containsIgnoreConjugation(info, keyword))){
									currentScore += PriorityWeight;
									auxilaryInfo += "|Matched| " + info ;			
								}
								
							}
						}
						
						
					}
			    	filterStringList.add(auxilaryInfo);
			    }
		    }
		    	
		    
		    Result res = new Result(currentName, currentScore, filterStringList);
		    resultList.add(res);
		}
		Collections.sort(resultList);
		return resultList;
		
	}
	
}
