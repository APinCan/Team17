package server;

import java.io.File;
import java.util.ArrayList;

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
		File dbCSV = new File("./data/kr_korean.csv");
		System.out.println("���� ���� ����"+dbCSV.exists());
		
		if(!dbCSV.exists()) {
			System.out.println("can not found DB CSV file");
		}
	}
	
	/*
	public static void main(String args[]) {
		WordConstraintsChecker checker = new WordConstraintsChecker();
	}
	*/
}
