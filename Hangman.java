// TODO: comment this file

import acm.program.*;
import acm.util.*;
import java.io.*;     // for File
import java.util.*;   // for Scanner

public class Hangman extends ConsoleProgram {
	
	// Creates a new private instance of the HangmanLexicon class 
	private HangmanLexicon lexicon  = new HangmanLexicon();
	
	public void run() {
		setFont("Monospaced-Bold-12");
		final int NUM_GUESSES_PER_GAME = 8;

		int numberOfGames = 0;
		int gamesWon = 0;
		int mostRemainingGuesses = 0;
		
		printWelcomeMessage();
		
		do {
			String randomWord = lexicon.getRandomWord();
			randomWord = randomWord.toUpperCase();
			int numberOfGuessesLeft = runSingleHangmanGame(randomWord, NUM_GUESSES_PER_GAME);
			
			// a game was won if the game ended with numberOfGuessesLeft > 0
			if(numberOfGuessesLeft > 0){
				gamesWon++;
			}
			// find highest number of numberOfGuessesLeft 
			if (numberOfGuessesLeft > mostRemainingGuesses){
				mostRemainingGuesses = numberOfGuessesLeft;
			}
			numberOfGames++;
		} while(playAgain());

		printStats(numberOfGames, gamesWon, mostRemainingGuesses);
		// System.exit(0);
		
	}
    
	/**
	 * Method prints a welcome message explaining the game.
	 */
	private void printWelcomeMessage(){
		println("Welcome to CS 106A Hangman! In this classic game,\n I will think of a random word, and you will try to guess\n letters from that word. Every time you guess a letter \n that isn\'t in my word, a new body part of the\n hanging man will appear. Guess correctly to avoid the gallows!\n");
	}
	
	/**
	 * method to print statistics of multi-game plays
	 * @param numGames, the number of games played
	 * @param NumberOfGamesWon, the number of games ended where the user did not run out of guesses
	 * @param BestGame, the game ended with the highest number of guesses remaining
	 */
	private void printStats(int numGames, int NumberOfGamesWon, int BestGame){
		// calculate win percentage
		double winPercentage = ((double) NumberOfGamesWon/numGames) * 100;
		
		// print stats
		println("Overall statistics: ");
		println("Games Played: " + numGames);
		println("Games won: " + NumberOfGamesWon);
		println("Win percent: " + String.format("%.2g", winPercentage) + "%");
		println("Best game: " + BestGame +" guess(es) remaining");
		println("Thanks for playing!");
	}
	
	/**
	 * Boolean method prompts the user to indicate if they want to play again and returns true for Y and false for N.
	 * @return boolean indicating whether the user typed a phrase beginning with Y (true) or N (false)
	 */
	private boolean playAgain(){
		while(true){
			String playAgain = readLine("Do you want to play again (Y/N)? ").toUpperCase();
			// check if starts with Y or N and re-prompt until it does 
			if(playAgain.startsWith("Y")){
				return true;
			} else if (playAgain.startsWith("N")){
				return false;
			} else {
				println("Invalid input. Please type Y or N.");
			}
		}
	}
	
	/**
	 * Method runs a single iteration of the hangman game with a new random word. 
	 * @param randomWord
	 * @param guessedLetters
	 * @param guessesLeft
	 */
	private int runSingleHangmanGame(String randomWordBeingGuessed, int startingNumberOfGuesses){
		// while loop prompts for guesses, evaluates guesses and displays graphic until the user guesses the word or runs out of guesses. 
		int guessesLeft = startingNumberOfGuesses;
		String guessedLetters = "";
		while(guessesLeft > 0) {
			displayGraphic(guessesLeft);
			displayHint(randomWordBeingGuessed, guessedLetters, guessesLeft);
			String thisGuess = getNextGuess(guessedLetters);
			guessedLetters += thisGuess;
			guessesLeft = checkGuess(randomWordBeingGuessed, thisGuess, guessesLeft);
			
			if(hasWon(randomWordBeingGuessed, guessedLetters)){
				break;
			}
		}
		
		if(hasWon(randomWordBeingGuessed, guessedLetters)){
			println("You win! You guessed my word " + randomWordBeingGuessed);
		} else {
			displayGraphic(guessesLeft);
			println("You lose! You failed to guess my word \"" + randomWordBeingGuessed + "\".");
		}
		return guessesLeft;
	}
	
	
	
	/**
	 * method to display the hint as a series of dashes. The hint is updated to show correct guesses in the place of a dash. 
	 * @param randomWord
	 * @param guessedLetters
	 * @param guessesLeft
	 */
	private void displayHint(String randomWord, String guessedLetters, int guessesLeft){
		// display a line of dashes the same length as the secret word 
		print("\nSecret Word : ");
		for (int i = 0; i < randomWord.length(); i++ ){
			// use guessed letters to print hint 
			String currentLetter = randomWord.substring(i, i + 1);
			if(guessedLetters.contains(currentLetter)){
				print(currentLetter + " ");
			} else {
				print("_ ");
			}
			
		}
		println("\nYour guesses: " + guessedLetters);
		println("Guesses left: " + guessesLeft);

	}
	
	/**
	 * Method determines whether the user has won the game yet
	 * @param String randomWord, the random word the user must guess
	 * @param String guessedLetters, the cumulative unique guesses the user has tried
	 * @return boolean false if there is a letter in the random word that has not been guessed and true otherwise.
	 */
	private boolean hasWon(String randomWord, String guessedLetters){
		for (int i = 0; i < randomWord.length(); i++ ){
		// for each letter in randomWord check if it is in guessedLetters 
			String currentLetter = randomWord.substring(i, i + 1);
			if(!guessedLetters.contains(currentLetter)){
				// user hasn't won yet so stop checking
				return false;
			} 
		}
		return true;
	}

	
	/**
	 * Prompts the user to guess a letter. It checks that user input is a single letter that has not been guessed before.
	 * @param guessedLetters, the string of unique letters the user has already guessed 
	 * @return String thisGuess, the user's latest guess
	 */
	 private String getNextGuess(String guessedLetters){
		 // Prompt user to type guesses
		while(true){
			String thisGuess = readLine("Your guess? ").toUpperCase();
			if(thisGuess.length() != 1) {
				println("Invalid input; you must type a single letter from A-Z.");
			} else if(!Character.isLetter(thisGuess.charAt(0))) {
				println("Invalid input; you must type a single letter from A-Z.");
			} else if(guessedLetters.contains(thisGuess)) {
				println("Invalid input, you have already guessed that letter.");
			} else {
				// add user's guess to the string guessedLetters
				return thisGuess;
			}		
		}
	} 
	 
	 private int checkGuess(String randomWord, String thisGuess, int guessesLeft){
			// Figure out is the guess correct
			if (randomWord.contains(thisGuess)){
				println("Correct! That letter is found in my word.");
			} else {
				println("Incorrect. That letter is not found in my word. ");
				guessesLeft--;
			}
			return guessesLeft;

	 }
	 
	 private void displayGraphic(int guessesLeft){
			 try {
				 Scanner hangman = new Scanner(new File("res/display" + guessesLeft + ".txt"));
				 while (hangman.hasNextLine()) {
					 String line = hangman.nextLine();
					 println(line); 
				 }
				 
			 } catch (FileNotFoundException fnfe){
				 println("Could not hang the man." + fnfe);
			 }  
			
		 
		 
	 }
	
	
	
}
