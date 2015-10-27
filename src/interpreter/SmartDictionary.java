package interpreter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.morph.WordnetStemmer;

/**
 * Singleton pattern applied here
 * @author JJ
 *
 */
public class SmartDictionary {
	private static IDictionary dict;
	private static WordnetStemmer stemmer;
	private static void initDict() {
		
		URL url;
		try {
			url = new URL("file", null,
					Utils.WordNetPath);
			dict = new Dictionary(url);
			dict.open();
			stemmer = new WordnetStemmer(dict);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Utils.debug("fail to init WordNet Dict");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			}
	
	
	
	
	public static boolean equalsIgnoreConjugation(String word, String anoWord){
		if(dict == null || !dict.isOpen()) {
			initDict();
		}
		if(word == null || anoWord == null || word.trim().equals("") || anoWord.trim().equals("")) {
			return false;
		}
		List<String> l1 = stemmer.findStems(word.trim(),  null);
		List<String> l2 = stemmer.findStems(anoWord.trim(),  null);
//		Utils.debug(l1.size() + " " + l2.size());
		for(String a : l1) {
			for(String b : l2) {
				 
				if(!a.isEmpty() && a != null && a.equalsIgnoreCase(b)){
					return true;
				}
			}
		}
		return false;
	
	}
}
