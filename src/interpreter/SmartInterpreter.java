package interpreter;

import interpreter.CategoryRule.CategoryEntry;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import predefinedValues.PredefinedValue;
import predefinedValues.PredefinedValuesFactory;

public class SmartInterpreter {

	private JSONObject exportDataMap;
	private String currentCategory;
	// first string is value name, second is value 
	private ArrayList<JSONObject> currentDataMap; 
	private File toInterprete;
	private String infoBuffer = "";
	
	public SmartInterpreter(File text){
		toInterprete = text;
		exportDataMap = new JSONObject();
		currentCategory = InterpreterRule.startCategory;
		currentDataMap = new ArrayList<JSONObject>();
	}
	
	public JSONObject build() {
		String fileName = toInterprete.getName().substring(0, toInterprete.getName().indexOf("."));
		String export = Utils.TEST_TEMP_PATH+ fileName +".json";
		File file = new File(export);
		try {
			if(file.exists()) {
				JSONParser parser = new JSONParser();
				Object obj = parser.parse(new FileReader(file));
				exportDataMap = (JSONObject) obj;
				return exportDataMap;
			}
			

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (org.json.simple.parser.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		try (BufferedReader br = new BufferedReader(new FileReader(toInterprete))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		    	//Utils.debug("Prcoess line: " + line);
		    	processLine(line); // process the line.
		    }
		    
		    finalizeCategory();
			currentDataMap.clear();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		// if file doesnt exists, then create it
		try {
			if (!file.exists()) {
				file.delete();
				file.createNewFile();
			}
			boolean shouldWrite = true;
			if(shouldWrite) {
				FileWriter fw = new FileWriter(file.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(exportDataMap.toJSONString());
				bw.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return exportDataMap;
		
	}

	public void processLine(String line) {
		String possibleNewCate = InterpreterRule.isNewCategory(line);
		if(possibleNewCate != InterpreterRule.InvalidCategory) {
			//Utils.debug("-----------------Foudn a new Category:" +possibleNewCate+"-----------------");
			
			finalizeCategory();
			currentCategory = possibleNewCate;
			
			
			return;
		} 
		
		CategoryRule catRule = InterpreterRule.getRuleForCategory(currentCategory);
		
		
		
		ArrayList<CategoryEntry> entries = catRule.getCategoryEntries();
		for(CategoryEntry entry: entries) {
			
			String valueType = entry.type;
			PredefinedValue v = PredefinedValuesFactory.getValue(line, valueType);
			
			if(v != null) {
				addValueToCurrentDataMap(entry.name, v.toString());
				//Utils.debug("-----------------Foudn a new definedValue of type:" +v.getType().toString()+"-----------------");
				
			}
		}
		
		infoBuffer+= (line + " ");
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void finalizeCategory() {
		
		
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
		currentDataMap.clear();
	}

	@SuppressWarnings({ "unused", "unchecked" })
	private JSONArray exportCategoryData() {
		pushBufferInfo();
		JSONArray returned = new JSONArray();
		CategoryRule catRule = InterpreterRule.getRuleForCategory(currentCategory);
		for(JSONObject map:currentDataMap) {
			returned.add(map);
		}
		return returned;
		 
		
	}
	@SuppressWarnings("unchecked")
	private void addValueToCurrentDataMap(String name, String value) {
		if(currentDataMap.size() == 0) {
			JSONObject newMap = new JSONObject();
			newMap.put(name, value);
			currentDataMap.add(newMap);
		} else {
			JSONObject nearstMap = currentDataMap.get(currentDataMap.size()-1);
			if(!nearstMap.containsKey(name)) {
				nearstMap.put(name, value);
			} else {
				pushBufferInfo();
				JSONObject newMap = new JSONObject();
				newMap.put(name, value);
				currentDataMap.add(newMap);
			}
		}
	}
	@SuppressWarnings("unchecked")
	private void pushBufferInfo() {
		if(currentDataMap.size() == 0) {
			JSONObject newMap = new JSONObject();
			newMap.put(InterpreterRule.info, infoBuffer);
			
			currentDataMap.add(newMap);
		} else {
			JSONObject nearstMap = currentDataMap.get(currentDataMap.size()-1);
			nearstMap.put(InterpreterRule.info, infoBuffer);
			
		}
		infoBuffer = "";
	}
	
	public static void main(String[] args) {
//		SmartInterpreter test = new SmartInterpreter(new File(Utils.TEST_TEMP_PATH + "DesmondLim.txt"));
//		SmartInterpreter test = new SmartInterpreter(new File(Utils.TEST_TEMP_PATH + "CV_Yamini_Bhaskar.txt"));
		SmartInterpreter test = new SmartInterpreter(new File(Utils.TEST_TEMP_PATH + "CV_Praveen.txt"));
//		SmartInterpreter test = new SmartInterpreter(new File(Utils.TEST_TEMP_PATH + "DonnabelleEmbodo.txt"));
//		SmartInterpreter test = new SmartInterpreter(new File(Utils.TEST_TEMP_PATH + "RussellOng.txt"));
		
		test.build();
		
	}
	
}
