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
	private String wordBefore, wordAfter;
	private ArrayList<String> wordDB;
	
	public WordConstraintsChecker(ArrayList<String> wordDB) {
		this.wordDB = wordDB;
	}
	
	public WordConstraintsChecker(String wordBefore, String wordAfter, ArrayList<String> wordDB) {
		this(wordDB);
		this.wordBefore = wordBefore;
		this.wordAfter = wordAfter;
	}
	
	//
	public int checkConstraints(String wordBefore, String wordAfter) {
		int ret = 0;
		this.wordBefore=wordBefore;
		this.wordAfter=wordAfter;
		
		
		
		return ret;
	}
	
	public static void main(String args[]) {
		//WordConstraintsChecker checker = new WordConstraintsChecker();
	}
	
}
