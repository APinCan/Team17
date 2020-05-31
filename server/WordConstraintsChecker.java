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
	private WordDB wordDB;
	private ArrayList<String> wordHistory = new ArrayList<String>();
	private int wordLengthConstraint;
	
	public WordConstraintsChecker(WordDB wordDB) {
		this.wordDB = wordDB;
	}
	
	public WordConstraintsChecker(String wordBefore, String wordAfter, WordDB wordDB) {
		this(wordDB);
		this.wordBefore = wordBefore;
		this.wordAfter = wordAfter;
	}
	
	public void startGame() {
		String word = this.wordDB.selectFirstString();
		this.wordBefore = word;
		setWordLengthConstraints(word.length());
		
//		return word;
	}
	
	public String getFirstString() {
		String first = wordDB.selectFirstString();
		
		return first;
	}
	
	private void setWordLengthConstraints(int wordLength) {
		this.wordLengthConstraint = wordLength;
	}
	
	public boolean checkConstraints(String wordBefore, String wordAfter, int wordLength) {
		boolean ret = true;
//		this.wordAfter=wordAfter;
		
		// word length constraint
		if(wordLength != wordAfter.length()) {
			ret = false; // if length different
			System.out.println("break word length rule");
		}
		// word history constraint
		else if(wordHistory.contains(this.wordAfter)) {
			ret = false; // if wordHistory have wordAfter
			System.out.println("break word history rule");
		}
		// check correct word game
		else if(this.wordBefore.charAt(this.wordBefore.length()-1) != this.wordAfter.charAt(0)) {
			ret = false; // if not word game
			System.out.println("break word correct rule");
		}
		// check word list
		else if(!wordDB.contains(this.wordAfter)) {
			ret = false; // if not contain wordAfter
			System.out.println("break word list rule"+"  "+this.wordAfter);
		}
		
		this.wordBefore = this.wordAfter;
		
		return ret;
	}
	
	public boolean checkConstraints(String wordBefore, String wordAfter) {
		boolean ret=false;
		this.wordBefore=wordBefore;	
//		ret = this.checkConstraints(wordAfter);
		
		return ret;
		
	}
	
//	public static void main(String args[]) {
//		WordDB worddb = new WordDB();
//		WordConstraintsChecker checker = new WordConstraintsChecker(worddb);
//		checker.setWordLengthConstraints(2);
//		
//		checker.checkConstraints("ÀüÂ÷", "Â÷½´");
//	}
	
}
