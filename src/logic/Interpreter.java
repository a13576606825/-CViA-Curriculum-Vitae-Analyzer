package logic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.*;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;

public class Interpreter {
	private static ArrayList<String> fileName;
	private static IndexWriter writer;
	private static ArrayList<Integer> score;

	public static void main(String[] args) {
		ArrayList<String> temp = new ArrayList<String>();
		ArrayList<String> querystr = new ArrayList<String>();
		
		temp.add("test.txt");
		temp.add("test2.txt");
		querystr.add("Software Engineer");
		querystr.add("bachelor of Science in Information Technology");
		querystr.add("hardware");
		ArrayList <Integer> scores = getQueryScore(temp, querystr, true);
		int count = 0;
		// each score at the index is for the file with the same index at the given filenames arraylist from ziyu's part
		while (count < scores.size()){
			System.out.printf("%d\n", score.get(count));
			count++;
		}

	}

	public static ArrayList<Integer> getQueryScore(ArrayList<String> fileName1,
			ArrayList<String> querystr, boolean useSynonymChecker) {
		fileName = fileName1;
		score = new ArrayList<Integer>(fileName.size());
		int pop = 0;
		IDictionary dict;
		while (pop < fileName.size()){
			score.add(0);
			pop++;
		}
		StandardAnalyzer analyzer = new StandardAnalyzer();

		Directory index = new RAMDirectory();
		// Directory index = FSDirectory.open(new File("index-dir"));
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		try {
			writer = new IndexWriter(index, config);

			indexing();

			writer.close();

			URL url = new URL("file", null,
					"C:/Program Files (x86)/WordNet/2.1/dict");

			// construct the dictionary object and open it
		 dict = new Dictionary(url);
			dict.open();
		} catch (Exception e) {
			e.printStackTrace();
			return null;

		}

			// iterate over words associated with the synset

			int count = 0;
			while (count < querystr.size()) {
				if (useSynonymChecker) {
					// look up first sense of the word "dog "
					Scanner sc = new Scanner(querystr.get(count));
					
					int counter = 0;
					while (sc.hasNext()) {
						String nextWord = sc.next();
						
						try{
						IIndexWord idxWord = dict.getIndexWord(nextWord,
								POS.NOUN);
						IWordID wordID = idxWord.getWordIDs().get(0);
						IWord word = dict.getWord(wordID);

						ISynset synset = word.getSynset();
						for (IWord w : synset.getWords()) {
							//System.out.println(w.getLemma());
							query(analyzer, index, w.getLemma().replace("_", " "));
						}
						}catch (Exception e1){
							System.out.printf("\n%s not found in dictionary\n\n", nextWord);
						}
					}
				} else {
					try{

					query(analyzer, index, querystr.get(count));
					}catch (Exception e2){
						e2.printStackTrace();
					}
				}
				count++;
			}
			return score;

		
	}

	private static void query(StandardAnalyzer analyzer, Directory index,
			String searchWord) throws ParseException, IOException {
		String querystr = searchWord;
		Query q = new QueryParser("line", analyzer).parse(querystr);

		int hitsPerPage = 10;
		IndexReader reader = DirectoryReader.open(index);
		IndexSearcher searcher = new IndexSearcher(reader);
		TopScoreDocCollector collector = TopScoreDocCollector
				.create(hitsPerPage);

		searcher.search(q, collector);
		ScoreDoc[] hits = collector.topDocs().scoreDocs;

		System.out.println("Query string: " + querystr);
		System.out.println("Found " + hits.length + " hits.");
		for (int i = 0; i < hits.length; ++i) {
			int docId = hits[i].doc;
			Document d = searcher.doc(docId);
			
			int fileIndex = fileName.indexOf(d.get("fileName"));
			score.set(fileIndex, score.get(fileIndex)+ 10 - i);
			// System.out.println((i + 1) + ". " + d.get("lineNum") + "\t"
			// + d.get("line"));
		}// Finally , close reader
	}

	private static void indexing() {
		int counter = 0;
		while (counter < fileName.size()) {
			File file = new File(fileName.get(counter));

			try {

				Scanner sc = new Scanner(file);
				int count = 0;
				String line = new String();
				;

				while (sc.hasNextLine()) {
					line = line + "\n" + sc.nextLine();

					count++;
				}
				addDoc(writer, line, fileName.get(counter));
				sc.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			counter++;
		}
	}

	private static void addDoc(IndexWriter w, String line, String fileName)
			throws IOException {
		Document doc = new Document();
		doc.add(new TextField("line", line, Field.Store.YES));

		// Here, we use a string field for course_code to avoid tokenizing.
		doc.add(new StringField("fileName", fileName, Field.Store.YES));
		w.addDocument(doc);
	}

}
