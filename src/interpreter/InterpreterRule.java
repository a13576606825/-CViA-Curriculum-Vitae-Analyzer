package interpreter;

import interpreter.CategoryRule.CategoryEntry;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import predefinedValues.PredefinedValuesType;


public class InterpreterRule {

	private static boolean isInit = false;
	static JSONArray categoryRules;
	private static ArrayList<String> categoryList;
	private static HashMap<String, CategoryRule> categoryMap;
	
	public final static String info = "info";
	public final static String InvalidCategory = "invalidCategory";
	public final static String startCategory = "personalInfo";
	
	public final static String Key_PrimaryKeys = "primaryKeys";
	public final static String Key_SecondaryKeys = "secondaryKeys";
	public final static String Key_MultipleEntry = "multipleEntry";
	public final static String Key_Data = "data";
	// sub level keys
	public final static String Key_Name = "name";
	public final static String Key_HasPredefinedValues = "hasPredefinedValues";
	public final static String Key_PredefinedValuesType = "predefinedValuesType";
	
	// step 1 
	public static ArrayList<String> getCategoryList() {
		if(!isInit) {
			init();
		} 
		return categoryList;
	}
	// step 2
	public static ArrayList<String> getPredefinedValuesTypesByCategory(String category) {
		ArrayList<String> output = new ArrayList<String>();
		CategoryRule categoryRule  = getRuleForCategory(category);
		for(CategoryEntry entry: categoryRule.getCategoryEntries()) {
			if(!entry.type.equalsIgnoreCase("Info")) {
				output.add(entry.type);
			}
			
			
		}
		return output;
		
	}
	public static CategoryRule getRuleForCategory(String category) {
		if(!isInit) {
			init();
		} 
		if(categoryMap.containsKey(category)) {
			return categoryMap.get(category);
		}
		return null;
	}
	
	public static String isNewCategory(String line) {
		if(!isInit) {
			init();
		} 
		// remove all symbol and split by space
		String[] wordArray = line.replaceAll("[^A-Za-z0-9 ]", "").split("\\s+");
		
		if( wordArray.length> 3 || wordArray.length <= 0) {
			
			return InvalidCategory;
		}
		ArrayList<String> tokens = new ArrayList<String>(Arrays.asList(wordArray));
		
		for(String category: categoryList) {
			if(isCategoryTitle(tokens, category)) {
				
				return category;
			}
		}
		
		return InvalidCategory;
	}

	private static void init() {
		JSONParser parser = new JSONParser();
		categoryMap = new HashMap<String, CategoryRule>();
		 categoryList = new ArrayList<String>();

		try {

			Object obj = parser.parse(new FileReader("InterpreterRule.json"));
			categoryRules = (JSONArray) obj;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (org.json.simple.parser.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		isInit = true;
		extractCategoryList();
	}
	
	private static boolean isCategoryTitle(ArrayList<String> tokens, String category) {
		
		
		if(categoryMap.containsKey(category)) {
			CategoryRule value = categoryMap.get(category);
			ArrayList<String> primaryKeys = value.getPrimaryKeys();
			
			for(String primaryKey: primaryKeys) {
				for(String token: tokens) {
					if(SmartDictionary.equalsIgnoreConjugation(token, primaryKey)) {
						return true;
					}
				}
			}
			
			int count = 0;
			ArrayList<String> secondaryKeys = value.getSecondaryKeys();
			for(String token: tokens) {
				for(String secondaryKey: secondaryKeys) {	
					if(SmartDictionary.equalsIgnoreConjugation(token, secondaryKey)) {
						count++;
					}
				}
			}
			
			if(count >= 2) {
				return true;
			}	
			
		}
		return false;
	}
	
	private static void extractCategoryList() {
		
		Iterator<JSONObject> itr = categoryRules.iterator();
		while(itr.hasNext()) {
			JSONObject curCategory = itr.next();
			for(Iterator iterator = curCategory.keySet().iterator(); iterator.hasNext();) {
			    String key = (String) iterator.next();
			    categoryMap.put(key, new CategoryRule((JSONObject)curCategory.get(key)));
			    categoryList.add(key);
			}
		}
		
	}
	public static void main(String[] args) {
//		Utils.debug(isNewCategory("personal details"));
//		Utils.debug(getRuleForCategory(startCategory).getPrimaryKeys());
		Utils.debug(getRuleForCategory("education").getCategoryEntries().get(1).name);
	}
}
