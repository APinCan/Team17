package server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class WordConstraintsChecker {
	String wordBefore, wordAfter;
	ArrayList<String> wordDB = new ArrayList<>();
	
	public WordConstraintsChecker() {
		initWordDB();
	}
	
	public WordConstraintsChecker(String wordBefore, String wordAfter) {
		this();
		this.wordBefore = wordBefore;
		this.wordAfter = wordAfter;
	}
	
	private void initWordDB() {
		File dbCSV;
		BufferedReader br;
		int i=0;
		String path = "./data/kp_korean-noun.csv";
		
		HashSet<String> hashset = new HashSet<>();

		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));
			String line = "";

			while((line = br.readLine()) != null){
				String line1[] = line.split(",", -1);
				//String line1[] = line.split(",");
				
// 접사 어미 조사 북한어	
				
//				for(String data : wordDB) {
//					if(!wordDB.contains(data))
//						wordDB.add(data);
//				}
			
//				Iterator<String> itr = wordDB.iterator();
//				while(itr.hasNext()) {
//					System.out.println(itr.next());
//				}
				
				
//				wordDB.add(line1[1]);
				
//				if(line1[1] == "명사") {
//					wordDB.add(line1[0]);
//				}
				
//				if(i%5000 == 0) {
//					System.out.println("current word : "+line1[0]);
//				}
					
				i++;
			}
			
			System.out.println(wordDB);
			
			br.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		

	}
	
	
	public static void main(String args[]) {
		WordConstraintsChecker checker = new WordConstraintsChecker();
	}
	
}
