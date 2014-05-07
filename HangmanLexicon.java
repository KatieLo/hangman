// TODO: fully implement this file
// TODO: comment this file

import acm.util.*;
import java.io.*;
import java.util.*;

public class HangmanLexicon {
	public String getRandomWord() {
		String result = "";
		try {
			// Read the words in the file into the array list wordList
			Scanner input = new Scanner(new File("res/lexicon-large.txt"));
			ArrayList<String> wordList = new ArrayList<String>();
			
			while(input.hasNext()){
				wordList.add(input.next());
			}
			
			// Assign the string found at a random index in the array list to result
			RandomGenerator randy = RandomGenerator.getInstance();
			int randomIndex = randy.nextInt(0, wordList.size() - 1);
			result = wordList.get(randomIndex);
		} 
		catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		}
		return result;
	}
}
	
	/**
	 * Method gets a random word from a text file. It creates a string type array list of words from the file and returns the string at a random index in the array list.
	 * @return a string which is a random word
	 */
	/*public String getRandomWordFromFixedSet() {
		String result = "";
		try {
			// Read the words in the file into the array list wordList
			Scanner input = new Scanner(new File("res/lexicon-large.txt"));
			ArrayList<String> wordList = new ArrayList<String>();
			
			while(input.hasNext()){
				wordList.add(input.next());
			}
			
			// Assign the string found at a random index in the array list to result
			RandomGenerator randy = RandomGenerator.getInstance();
			int randomIndex = randy.nextInt(0, wordList.size() - 1);
			result = wordList.get(randomIndex);
		} 
		catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		}
		return result;
	} 
	
	
} */
