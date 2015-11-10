package interpreter;

import java.util.ArrayList;

public class Utils {
	
	public static final String WordNetPath = "/usr/local/Cellar/wordnet/3.1/dict";
	public static final String TEST_TEMP_PATH = "test/temp/";
	
	
	
    public static void debug(String s) {
    	System.out.println(s);
    }
    public static void debug(ArrayList<String> lst) {
    	for(int i=0; i<lst.size(); i++) {
    		System.out.println(lst.get(i));		
    	}
    
    }
}
