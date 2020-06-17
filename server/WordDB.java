package server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class WordDB {
	private ArrayList<String> wordDB = new ArrayList<>();
	private int dbLength;
	
	public WordDB() {
		initWordDB();
	}
	
	private void initWordDB() {
		BufferedReader br;
		String path = "./data/kr_korean-noun.csv";
		int i=0;

		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));
			String line = "";

			while((line = br.readLine()) != null){
				String line1[] = line.split(",", -1);
				
				
				if(line1[1].equals("명사") || line1[1].equals("대명사")) {
					wordDB.add(line1[0]);
					i++;
				}
				
			}
			
			dbLength = i;
			
			br.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String selectFirstString() {
		String firstString = null;
		int randIdx;
		
		randIdx = new Random().nextInt(dbLength);
		firstString = wordDB.get(randIdx);
		
		return firstString; 
	}
	
	public boolean contains(String word) {
		if(wordDB.contains(word)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public String getCorrectWord(char charWord) {
		ArrayList<String> tmpList = new ArrayList<String>();
		
		for(int i=0; i< wordDB.size(); i++) {
			if(wordDB.get(i).charAt(0)==charWord) {
				tmpList.add(wordDB.get(i));
			}
		}
		
		int idx = new Random().nextInt(tmpList.size());
		
		return tmpList.get(idx);
	}
}
