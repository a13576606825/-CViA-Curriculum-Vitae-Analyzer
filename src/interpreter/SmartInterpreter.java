package interpreter;

import interpreter.CategoryRule.CategoryEntry;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import predefinedValues.PredefinedValue;
import predefinedValues.PredefinedValuesFactory;
import predefinedValues.PredefinedValuesType;

public class SmartInterpreter {

	private static JSONObject exportDataMap;
	private String currentCategory;
	private ArrayList<HashMap<String, PredefinedValue>> currentDataMap; 
	private File toInterprete;
	private String infoBuffer = "";
	
	public SmartInterpreter(File text){
		toInterprete = text;
		exportDataMap = new JSONObject();
		currentCategory = InterpreterRule.startCategory;
		currentDataMap = new ArrayList<HashMap<String, PredefinedValue>>();
	}
	
	public void build() {
		try (BufferedReader br = new BufferedReader(new FileReader(toInterprete))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		    	Utils.debug("Prcoess line: " + line);
		    	processLine(line); // process the line.
		    }
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		Utils.debug(exportDataMap.toJSONString());
		
	}
	
	public void processLine(String line) {
		String possibleNewCate = InterpreterRule.isNewCategory(line);
		if(possibleNewCate != InterpreterRule.InvalidCategory) {
			Utils.debug("-----------------Foudn a new Category:" +possibleNewCate+"-----------------");
			
			if(!exportDataMap.containsKey(currentCategory)){
				JSONArray categoryData = exportCategoryData();
				exportDataMap.put(currentCategory, categoryData);
			} else {
				JSONArray original = (JSONArray) exportDataMap.get(currentCategory);
				JSONArray categoryData = exportCategoryData();
				for(Iterator itr = categoryData.iterator(); itr.hasNext();) {
					original.add(itr.next());
				}
				exportDataMap.put(currentCategory, original);
			}

			currentCategory = possibleNewCate;
			currentDataMap.clear();
			return;
		} 
		
		CategoryRule catRule = InterpreterRule.getRuleForCategory(currentCategory);
		
		infoBuffer+= line;
		
		ArrayList<CategoryEntry> entries = catRule.getCategoryEntries();
		for(CategoryEntry entry: entries) {
			
			String valueType = entry.type;
			PredefinedValue v = PredefinedValuesFactory.getValue(line, valueType);
			if(v != null) {
				addValueToCurrentDataMap(entry.name, v);
			}
		}
	}
	private JSONArray exportCategoryData() {
		JSONArray returned = new JSONArray();
		CategoryRule catRule = InterpreterRule.getRuleForCategory(currentCategory);
		for(HashMap<String, PredefinedValue> map:currentDataMap) {
			JSONObject cur = new JSONObject(); 
			for(Iterator<String> itr = (Iterator<String>) map.keySet(); itr.hasNext();) {
				String key = itr.next();
				cur.put(key, map.get(key));
			}
			returned.add(map);
		}
		return returned;
		 
		
	}
	private void addValueToCurrentDataMap(String name, PredefinedValue value) {
		if(currentDataMap.size() == 0) {
			HashMap<String, PredefinedValue> newMap = new HashMap<String, PredefinedValue>();
			newMap.put(name, value);
			currentDataMap.add(newMap);
		} else {
			HashMap<String, PredefinedValue> nearstMap = currentDataMap.get(currentDataMap.size()-1);
			if(!nearstMap.containsKey(name)) {
				nearstMap.put(name, value);
			} else {
				nearstMap.put(InterpreterRule.info, PredefinedValuesFactory.getValue(infoBuffer, PredefinedValuesType.Info.toString()));
				infoBuffer = "";
				HashMap<String, PredefinedValue> newMap = new HashMap<String, PredefinedValue>();
				newMap.put(name, value);
				currentDataMap.add(newMap);
			}
		}
		
	}
	
	public static void main(String[] args) {
		SmartInterpreter test = new SmartInterpreter(new File("test/temp/DesmondLim.txt"));
		test.build();
		
	}
	
}
