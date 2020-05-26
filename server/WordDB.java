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
	
	public WordDB() {
		initWordDB();
	}
	
	private void initWordDB() {
		BufferedReader br;
		String path = "./data/kp_korean-noun.csv";
		int i=0;

		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));
			String line = "";

			while((line = br.readLine()) != null){
				String line1[] = line.split(",", -1);
				
				if(line1[1].equals("���") || line1[1].equals("����")) {
					wordDB.add(line1[0]);
//					System.out.println(i+"  "+line1[0]);
//					i++;
				}
				
//				System.out.println(i+"  "+line1[0]);
//				i++;
			}
						
			br.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String selectFirstString() {
		Random random = new Random();
		String str = null;
		
		
		
		return str; 
	}
	
	public boolean contains(String word) {
		if(wordDB.contains(word)) {
			return true;
		}
		else {
			return false;
		}
	}
}
