package interpreter;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import predefinedValues.PredefinedValuesType;

public class CategoryRule {
	private boolean isMultipleEntry;
	private ArrayList<String> primaryKeys;
	private ArrayList<String> secondaryKeys;
	private ArrayList<CategoryEntry> categoryEntries;
	
	
	public CategoryRule(JSONObject obj) {
		isMultipleEntry = false; 
		primaryKeys = new  ArrayList<String>();
		secondaryKeys = new  ArrayList<String>();
		categoryEntries = new ArrayList<CategoryEntry>();
		
		if(obj.containsKey(InterpreterRule.Key_MultipleEntry)) {
			isMultipleEntry = (boolean)obj.get(InterpreterRule.Key_MultipleEntry);
		}
		if(obj.containsKey(InterpreterRule.Key_PrimaryKeys)) {
			JSONArray primaryKeysJS = (JSONArray)obj.get(InterpreterRule.Key_PrimaryKeys);
			for(Iterator<String> primaryKeysItr = primaryKeysJS.iterator();primaryKeysItr.hasNext();) {
				String next = primaryKeysItr.next();
				primaryKeys.add(next);
			}
		}
		if(obj.containsKey(InterpreterRule.Key_SecondaryKeys)) {
			JSONArray secondaryKeysJS = (JSONArray) obj.get(InterpreterRule.Key_SecondaryKeys);
			for(Iterator<String> secondaryKeysItr = secondaryKeysJS.iterator();secondaryKeysItr.hasNext();) {
				secondaryKeys.add((String)secondaryKeysItr.next());
			}
		}
		if(obj.containsKey(InterpreterRule.Key_Data)) {
			JSONArray dataEntries = (JSONArray)obj.get(InterpreterRule.Key_Data);
			for(Iterator iterator = dataEntries.iterator(); iterator.hasNext();) {
				categoryEntries.add(new CategoryEntry((JSONObject)iterator.next()));
			}
		}
	}
	public boolean isMultipleEntry(){
		return isMultipleEntry;
	}
	public ArrayList<String> getPrimaryKeys() {
		return primaryKeys;
	}
	public ArrayList<String> getSecondaryKeys() {
		return secondaryKeys;
	}
	public ArrayList<CategoryEntry> getCategoryEntries(){
		return categoryEntries;
	}
	
	
	public class CategoryEntry {
		public String name;
		public String type;
		
		public CategoryEntry(JSONObject obj) {
			if(obj.containsKey(InterpreterRule.Key_Name)) {
				name = (String)obj.get(InterpreterRule.Key_Name);
			} else {
				name = "";
			}
			if(obj.containsKey(InterpreterRule.Key_PredefinedValuesType)) {
				type = (String)obj.get(InterpreterRule.Key_PredefinedValuesType);
			} else {
				type = "";
			}
		}
	}
}