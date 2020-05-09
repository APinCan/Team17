package server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class WordDB {
	private ArrayList<String> wordDB = new ArrayList<>();
	
	public WordDB() {
		initWordDB();
	}
	
	private void initWordDB() {
		File dbCSV;
		BufferedReader br;
		int i=0;
		String path = "./data/kp_korean-noun.csv";

		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));
			String line = "";

			while((line = br.readLine()) != null){
				String line1[] = line.split(",", -1);
				
				if(line1[1]=="명사" || line1[1]=="대명사") {
					wordDB.add(line1[0]);
				}
			}
						
			br.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<String> getWordDB() {
		return wordDB;
	}
}
