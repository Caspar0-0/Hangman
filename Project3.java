//////////////////// ALL ASSIGNMENTS INCLUDE THIS SECTION /////////////////////
//
// Title:            Project3: HANGMAN
// Files:            Project3.java
// Semester:         CS 302 Summer 2017
//
// Author:           Caspar Chen
// Email:            jcchen4@wisc.edu
// CS Login:         caspar
// Lecturer's Name:  Steve Earth
// Lab Section:      (none)
//
//////////////////// PAIR PROGRAMMERS COMPLETE THIS SECTION ///////////////////
//
// Partner Name:     Wenbo Ouyang
// Partner Email:    wouyang4@wisc.edu
// Partner CS Login: (none)
// Lecturer's Name:  Steve Earth
// Lab Section:
//
// VERIFY THE FOLLOWING BY PLACING AN X NEXT TO EACH TRUE STATEMENT:
//    _X_ Write-up states that Pair Programming is allowed for this assignment.
//    _X_ We have both read the CS302 Pair Programming policy.
//    _X_ We have registered our team prior to the team registration deadline.
//
///////////////////////////// CREDIT OUTSIDE HELP /////////////////////////////
//
// Students who get help from sources other than their partner must fully
// acknowledge and credit those sources of help here.  Instructors and TAs do
// not need to be credited here, but tutors, friends, relatives, room mates
// strangers, etc do.
//
// Persons:          Sandy
// Online Sources:   (none)
//
/////////////////////////////// 80 COLUMNS WIDE ///////////////////////////////
/**
 * This program is a game between the user and the program, both as the word
 * chooser and the word guesser. The Chooser displays any valid English word,
 * but with asterisks in place of each letter.The Guesser picks a letter and
 * the Chooser reveals all asterisks which correspond to that letter.
 * If the guessed letter is not in the word, then the Guesser receives a strike.
 * If the entire word is revealed before the tenth strike, the Guesser wins,
 * otherwise the Chooser wins. When the computer is the Guesser, it will 
 * maintain a list of all the words that are consistent with all the 
 * information known about the secret solution word thus far. When the 
 * computer is the Chooser, first it chooses a random word equally out of
 * the entire word list. Then when the human makes a guess at a letter, 
 * it looks at all possible answers it might give for positions of that 
 * letter. At the end of a game, the program will ask whether for a restart.
 * Bugs: no Bugs
 *
 * @author Caspar Chen && Wenbo Ouyang  */

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Random;
import java.util.LinkedHashSet;
import java.lang.Math;

public class Project3 {
	public final static int MAX_WORD_LENGTH = 30;
	public static final int SEED = 777;
	public static Random ranGen = new Random(SEED);
	public static Scanner scnr = new Scanner(System.in);
	public static Scanner str;
	public static Scanner input;
	public static String[] letterarray = { "a", "b", "c", "d", "e", "f", "g",
			"h", "i", "j", "k", "l", "m", "n", "o",
			"p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z" };
	public static ArrayList<String> letterList = new ArrayList<String>
	(Arrays.asList(letterarray));
	public static ArrayList<String> letterList1 = new ArrayList<String>
	(letterList);
	public static ArrayList<String> letterGuess = new ArrayList<String>();
	public static ArrayList<Double> protable1 = new ArrayList<Double>();
	public static ArrayList<String> wordnew = new ArrayList<String>();
	public static ArrayList<String> wordrenewal = new ArrayList<String>();
	public static LinkedHashSet<String> lhs = new LinkedHashSet<String>();
	public static ArrayList<ArrayList<String>> total = new ArrayList
			<ArrayList<String>>();
	public static ArrayList<Integer> position = new ArrayList<Integer>();
	public static ArrayList<Integer> positiontrue = new ArrayList<Integer>();
	public static ArrayList<Double> freq = new ArrayList<Double>();
	public static ArrayList<Character> wronginformation = new ArrayList
			<Character>();
	public static ArrayList<String> wordCopy = new ArrayList<String>();
	private static ArrayList<String> matchingAnswers = new ArrayList
			<String>();
	public static ArrayList<String> word = new ArrayList<String>();
	public static ArrayList<Integer> rightNum= new ArrayList<Integer>();
	public static void main(String[] args) throws IOException {
		FileInputStream myFile = null;
		myFile = new FileInputStream("words.txt");
		Scanner fileScnr = new Scanner(myFile);// setup myFile for scanner
		// record words from words.txt
		while (fileScnr.hasNextLine()) {
			word.add(fileScnr.nextLine());
		}
		for (int i = 0; i < word.size(); i++) {
			wordCopy.add(word.get(i));
		}
	
	// generate ranNum for choosing the word by the computer as the guesser

		// declare and initialize string variable
		String computerChoice = "";
		String role1 = "", value = "";
		String entryLetter = "", wordReveal = "";
	String restart = ""; // declare a string variable for restarting a game
		String numLetter;
		// declare and initialize int and double variable
		int num = 0, indexCorrect = 1;
		int lets = -1;
		int strikes = 0;
		int nextnum = -1;
		double sumFreq = 0;
		// declare and initialize boolean variable
		boolean zero = false, zero2 = false;
		boolean negNum = false;
		boolean gameEnd = true;
		boolean humanChoice = true;
		boolean noword = false;
		boolean repeat = false;
		// the list of secret word
		ArrayList<String> list = new ArrayList<String>();

		// loop for a new game when user does not put anything start with
		// 'N','n'
		while (restart.length() == 0 || 
				(restart.charAt(0) != 78 && restart.charAt(0) != 110)) {
			//determine a random word equally out of 
			//the entire word list at first for computer guesser
			int randNum = ranGen.nextInt(word.size());
			//reset variable and ArrayList
			letterList.clear();
			wordCopy.clear();
			lhs.clear();
			wordrenewal.clear();
			wordnew.clear();
			freq.clear();
			protable1.clear();
			positiontrue.clear();
			wronginformation.clear();
			list.clear();
			gameEnd = true;
			strikes = 0;
			rightNum.clear();
			letterGuess.clear();
			for(int i=0;i<letterList1.size();i++){
				letterList.add(letterList1.get(i));
			}
			for(int i=0;i<word.size();i++){
				wordCopy.add(word.get(i));
			}
			
			
			do {
				do {
					System.out.print("Do you wish to be the word " 
				+ "[C]hooser or the word [G]uesser: ");
					role1 = scnr.nextLine();
				} while (role1.length() == 0);
				//true if humanChoice is the chooser
				if (Character.toUpperCase(role1.charAt(0)) == 'C') {
					humanChoice = true;
				} else if (Character.toUpperCase(role1.charAt(0)) == 'G') {
					humanChoice = false;
				}
			} while (Character.toUpperCase(role1.charAt(0)) != 'G' && 
					Character.toUpperCase(role1.charAt(0)) != 'C');
			if (humanChoice) {
				System.out.println("");
				do {

	System.out.print("How many letters are in your chosen word: ");
					
					numLetter = scnr.nextLine();
					input = new Scanner(numLetter);
					if (input.hasNextInt()) {
						num = input.nextInt();
						if (num < 1 || num > MAX_WORD_LENGTH) {
							System.out.println("Your word must be have " 
						+ "between 1 and 30 letters, try again.");
							numLetter = "";
						}
					} else {
						numLetter = "";
				System.out.println("You must enter an integer, try again.");
					}

				} while (numLetter.length() == 0);
			} else {
				computerChoice = word.get(randNum);
				num = computerChoice.length();
				findMatching(num);
			}
			System.out.println("No letters guessed yet.");
			System.out.println("No strikes yet -- all ten still remaining.");
			for (int i = 0; i < num; i++) {
				list.add("*");
			}
			System.out.print("Word revealed thus far: ");
			for (int i = 0; i < num; i++) {
				System.out.print(" " + list.get(i));
			}
			System.out.println();
			// start the game with the user being the Guesser
			while (gameEnd && !humanChoice) {
				do {
					System.out.print("Enter a letter: ");
					entryLetter = scnr.nextLine();
					entryLetter = entryLetter.toUpperCase();
				} while (entryLetter.length() == 0);
				//let the computer secretly change the word
computerChoice = changeSecretWord(matchingAnswers, entryLetter.charAt(0));
							
				// check if the letter is not in the secret word
		if (computerChoice.indexOf
				(Character.toLowerCase(entryLetter.charAt(0))) == -1) {
					letterGuess.add(entryLetter.substring(0, 1).toLowerCase());
					strikes += 1;
	System.out.println("The letter '" + 
					Character.toLowerCase(entryLetter.charAt(0))
							+ "' is not in the word -- Strike!");
					// ten strikes result to an automatic lost
					if (strikes == 10) {
						System.out.println("");
						System.out.println(
								"The guesser has lost by not " 
						+ "finding the word before reaching ten strikes.");
						System.out
								.println("\nThe computer had chosen the word '" 
						+ computerChoice.toLowerCase() + "'.");
						gameEnd = false;

					} else {
				
						System.out.print("Letters guessed so far: ");
						for (int i = 0; i < letterGuess.size(); i++) {
							System.out.print(letterGuess.get(i) + " ");
						}

						System.out.println("");

					System.out.println("Guesser currently has " 
						+ strikes + " strikes (" + (10 - strikes)
								+ " to go until automatic loss)");

						System.out.print("Word revealed thus far: ");
						for (int i = 0; i < list.size(); i++) {
				System.out.print(Character.toUpperCase(list.get(i).charAt(0)) + " ");
						}
						System.out.println("");
					}
				} else {
					letterGuess.add(entryLetter.substring(0, 1).toUpperCase());
					// check if there are more than one correct letter in the
					// word
					for (int i = 0; i < computerChoice.length(); i++) {
		if (computerChoice.charAt(i) == Character.toLowerCase(entryLetter.charAt(0))) {
			indexCorrect = i;
	list.set(computerChoice.indexOf(Character.toLowerCase(computerChoice.charAt(i)),
									indexCorrect), entryLetter);
	position.add(computerChoice.indexOf(Character.toLowerCase(computerChoice.charAt(i)),
									indexCorrect) + 1);
						}

					}
System.out.println("The letter '" + Character.toLowerCase(entryLetter.charAt(0))
							+ "' is at positions: " + position);

					position.clear();
					//check if the word is been found
					if (list.indexOf("*") == -1 && strikes < 10) {
						gameEnd = false;
						System.out.println();
						System.out.print("The guesser has won by finding the word '");
						for (int i = 0; i < list.size(); i++) {
							System.out.print(Character.toUpperCase(list.get(i).charAt(0)));
						}
						System.out.println("' before the tenth strike.");
					} else {
						
						System.out.print("Letters guessed so far: ");
						for (int i = 0; i < letterGuess.size(); i++) {
							System.out.print(letterGuess.get(i) + " ");
						}
						System.out.println("");
						if (strikes == 0) {
							System.out.println("No strikes yet -- all ten still remaining.");

						} else {

	System.out.println("Guesser currently has " + strikes + " strikes (" + (10 - strikes)
						+ " to go until automatic loss)");

						}

						System.out.print("Word revealed thus far: ");
						for (int i = 0; i < list.size(); i++) {
	System.out.print(Character.toUpperCase(list.get(i).charAt(0)) + " ");

						}
						System.out.println();
					}
				}

			}
			// start the game with the user being the Chooser
			while (gameEnd && humanChoice) {
				System.out.println("");
				sumFreq = 0;

				for (int i = 0; i < wordCopy.size(); i++) {
					if (wordCopy.get(i).length() == num) {
						wordnew.add(wordCopy.get(i));
					}
				}
				for (int i = 0; i < letterList.size(); i++) {
					freq.add(new Double(0));
				}

				for (int i = 0; i < wordnew.size(); i++) {
					for (int j = 0; j < letterList.size(); j++) {
						if (wordnew.get(i).indexOf(letterList.get(j)) != -1) {
							freq.set(j, freq.get(j) + 1);
						}
					}
				}

				for (int i = 0; i < freq.size(); i++) {
					sumFreq = sumFreq + freq.get(i);
				}

				for (int i = 0; i < freq.size(); i++) {
					protable1.add(freq.get(i) / sumFreq);
				}

				String str1 = likelyChoice();// computer's choice on the letter
				String m = str1;// use String m for further modification
				if (num != 1) {
		System.out.print("The computer chooses the letter '" + m + "' for its turn.");

					System.out.println("");
				}
				do {
					negNum = false;
					repeat = false;
					zero = false;
					zero2 = false;
					if (num != 1) {
System.out
.print("Enter locations that " + "computer's guess appears in your word (0 if none): ");
						value = scnr.nextLine();// user's answer to tell which
												// location
						str = new Scanner(value);// second Scanner scanned from
													// the String
						if (value.length() == 0) {
	System.out.println("You must enter at least one number, try again.");
							repeat = true;
						}

						else if (!str.hasNextInt()) {

	System.out.println("All entries must be integers, try again.");

							repeat = true;

						} else {

							lets = str.nextInt();
							if (lets == 0) {

								zero = true;
							}
							//check if all the inputs are valid by the rule
							if (str.hasNextInt()) {
								while (str.hasNextInt()) {
									
									nextnum = str.nextInt();
									zero2 = true;
									if (nextnum <= lets) {
System.out.println("Positions must be entered in increasing order, try again.");
										repeat = true;

									}
									
									if (nextnum > num || nextnum < 0) {
										negNum = true;
										repeat = true;
										
									}
									
									if (lets == 0) {
										if (str.hasNext() || zero2) {
System.out.println("There should be no entries after a 0");
											repeat = true;
											break;
										} else {
											
											noword = true;
										}
										zero = true;
									}
									if (!zero) {
										rightNum.add(lets);
										rightNum.add(nextnum);
										
									}
								
						
								}
								
								boolean neg = false; 
								for(int i = 0; i< rightNum.size(); i++) {
									
								
									if(rightNum.get(i)<0||rightNum.get(i)>num){
										rightNum.clear();
										neg = true;
										break;
									}
									else{
										
									}
								}
								if(!neg){
								for(int i = 0; i< rightNum.size(); i++) {
									list.set(rightNum.get(i)-1, m);
								}
								}
								if (!zero&&!negNum) {
									letterGuess.add(m.toUpperCase());

								}
								
								if(noword){
									letterGuess.add(m.toLowerCase());
									strikes = strikes + 1;
									for (int i = 0; i < wordnew.size(); i++) {
										if (wordnew.get(i).indexOf(m) == -1) {
											wordrenewal.add(wordnew.get(i));
										}
									}
								}
							
								if (negNum || lets < 0 || lets > num) {

									System.out.println(
"Numbers must be in range of 0 up to " + num + ", the word length.");
									repeat = true;

								}
							}

							else {
								if (!zero) {
									letterGuess.add(m.toUpperCase());
									if (lets > num || lets < 0) {

										System.out.println(
"Numbers must be in range of 0 up to " + num + ", the word length.");
										repeat = true;
									} else {
										
										list.set(lets - 1, m);
										repeat = false;

									}
								}
								if (zero) {
									letterGuess.add(m.toLowerCase());
									strikes = strikes + 1;
									for (int i = 0; i < wordnew.size(); i++) {
										if (wordnew.get(i).indexOf(m) == -1) {
											wordrenewal.add(wordnew.get(i));
										}
									}
								}
							}

						}

					}
				} while (repeat);

				for (int i = 0; i < list.size(); i++) {
					if (list.get(i) == m) {
						positiontrue.add(i);
					}
				}

				//add the remaining possible words to an ArrayList
				for (int i = 0; i < wordnew.size(); i++) {
					if(positionsame(wordnew.get(i),m).equals(positiontrue)){
						wordrenewal.add(wordnew.get(i));
					}
				}
				//check if the possible words ArrayList is empty
				if (wordrenewal.size() == 0) {
					if(num!=1){
					System.out.print("Letters guessed so far: ");
					for (int i = 0; i < letterGuess.size(); i++) {
						System.out.print(letterGuess.get(i) + " ");
					}
					System.out.println("");
					System.out.println("Guesser currently has " 
					+ strikes + " strikes (" + (10 - strikes)
							+ " to go until automatic loss)");
					System.out.print("Word revealed thus far: ");
					for (int i = 0; i < list.size(); i++) {
	System.out.print(Character.toUpperCase(list.get(i).charAt(0)) + " ");
					}
					System.out.println("");
					}
					System.out.println("The computer does not know your word.");
					System.out.println();
					System.out
					.println("The guesser has lost" 
					+ " by not finding the word before reaching ten strikes.");
			
					do {
						System.out.println();
						System.out.print("The Chooser should reveal their word now: ");

						wordReveal = scnr.nextLine();
					} while (wordReveal.length() == 0);

					//check and print error prompt for the word revealed 
					if (wordReveal.length() != num) {
				System.out.println
			("That does not match the length you stated at the start of the game.");
						break;
					} else {
						for (int i = 0; i < wordReveal.length(); i++) {
							if (list.indexOf(wordReveal.charAt(i)) != i
		&& letterGuess.indexOf(String.valueOf(wordReveal.charAt(i))) != -1) {
			wronginformation.add((wordReveal.charAt(i)));

							}
						}
					}
					
					break;
				}
				lhs.addAll(wordrenewal);
				wordrenewal.clear();
				wordrenewal.addAll(lhs);
				wordCopy.clear();
				//add all the remaining possible words to wordCopy
				for (int i = 0; i < wordrenewal.size(); i++) {
					wordCopy.add(wordrenewal.get(i));
				}
				//reset some ArrayLists during game
				lhs.clear();
				wordrenewal.clear();
				wordnew.clear();
				freq.clear();
				protable1.clear();
				positiontrue.clear();
				letterList.remove(m);

			
				if (strikes == 10) { // ten strikes result to an automatic lost
					System.out.println("");
					System.out
							.println("The guesser has lost" 
					+ " by not finding the word before reaching ten strikes.");

					do {

						System.out.println("");
						System.out.print("The Chooser should reveal their word now: ");

						wordReveal = scnr.nextLine();
					} while (wordReveal.length() == 0);

					//check and print error prompt for the word revealed 
					if (wordReveal.length() != num) {
						System.out.println
			("That does not match the length you stated at the start of the game.");
						break;
					} else {
						for (int i = 0; i < wordReveal.length(); i++) {
							if (list.indexOf(wordReveal.charAt(i)) != i
		&& letterGuess.indexOf(String.valueOf(wordReveal.charAt(i))) != -1) {
								wronginformation.add((wordReveal.charAt(i)));

							}
						}
						for (int i = 0; i < wronginformation.size(); i++) {
		System.out.println("Your statement on my guess '" + wronginformation.get(i)
+ "' is not consistent with your reported solution.");
						}

					}
					

					break;
				}
				if (list.indexOf("*") == -1 && strikes < 10) {
					// check if the list has done guessing
					gameEnd = false;
					System.out.println("");
					System.out.print("The guesser has won by finding the word '");
					for (int i = 0; i < list.size(); i++) {
						System.out.print(Character.toUpperCase(list.get(i).charAt(0)));
					}
					System.out.println("' before the tenth strike.");
				} else {
					if (num != 1) {
						
					System.out.print("Letters guessed so far: ");
					for (int i = 0; i < letterGuess.size(); i++) {
						System.out.print(letterGuess.get(i) + " ");
					}
					System.out.println("");
		System.out.println("Guesser currently has " 
					+ strikes + " strikes (" + (10 - strikes)
							+ " to go until automatic loss)");
					System.out.print("Word revealed thus far: ");
					for (int i = 0; i < list.size(); i++) {
						System.out.print(
						Character.toUpperCase(list.get(i).charAt(0)) + " ");
					}
					System.out.println("");
					}
				}
			}

			// prompt for restart
			System.out.print("Do you wish to play again (Y/N)? ");
			restart = scnr.nextLine();
		}
		fileScnr.close();//close the scanner at last
	}

	/**
	 * This function takes no argument and is used for the computer to generate
	 * a letter when it is its guessing turn. It is using a random generated
	 * number to compare with the intervals of the frequency of 26 English
	 * letters.
	 * 
	 * @return a single string representing the chosen letter for the computer
	 */
	public static String likelyChoice() {
		double sum = 0;// refers to the total sum of all probabilities
		double sum1 = 0;// refers to the cumulative sum of all probabilities
		double k = 0; // refers to the random generated number from ranGen
		int m = 0;// refers to the index for choosing the letter in
		// letterList ArrayList

		for (int i = 0; i < letterList.size(); i++) {
			sum = sum + protable1.get(i);
		}
		ArrayList<Double> protable2 = new ArrayList<Double>();
		ArrayList<Double> protable3 = new ArrayList<Double>();

		for (int i = 0; i < letterList.size(); i++) {
			protable2.add(protable1.get(i) / sum);
		}
		// add elements for the cumulative prob distribution for the letters
		// computer choose from
		for (int i = 0; i < letterList.size(); i++) {
			for (int j = 0; j <= i; j++) {
				sum1 = sum1 + protable2.get(j);
			}
			protable3.add(sum1);
			sum1 = 0;
		}

		k = ranGen.nextDouble();
		
		// break out of the loop if the index is found
		for (int i = 0; i < letterList.size(); i++) {

			if (k < protable3.get(0)) {
				m = 0;
				break;
			} else if (k < protable3.get(i)) {
				m = i;
				break;
			}
		}
		return letterList.get(m);
	}
/**
 * This method is to calcuate how many String el in String obj, i.e. frequency
 * @param obj refers to a string variable obj
 * @param el refers to a string variable el
 * @return an int sum
 */
	public static int numelement(String obj, String el) {

		int sum = 0;
		for (int i = 0; i < obj.length(); i++) {
			if (obj.charAt(i) == el.charAt(0)) {
				sum += 1;
			}
		}
		return sum;
	}

/**
 * This method is for the tie break, which convert the word using reverse
 * binary order and returns a int and pos[2] would be 0010 and pos[1,3] would be 0101.
 * And then convert the binary to decimal and compare their value for later use.
* @param userEntry
*            refers to the String userEntry which is a English word
* @param guess
*            refers to a char guessed from user
* @return a sum of what this String word represents
	 */
	public static int generateKey(String userEntry, char guess) {
		String temp = "";
		int sumFreq = 0;
		userEntry = userEntry.toUpperCase();
		if (Character.isLowerCase(guess))
			guess -= 32;
		for (int i = 0; i < userEntry.length(); i++) {
			if (userEntry.charAt(i) == guess) {
				temp += "1";
			} else {
				temp += "0";
			}
		}
		for (int i = 0; i < temp.length(); i++) {
			if (temp.charAt(i) == '1') {
				sumFreq = sumFreq + (int) Math.pow(2, i);

			}

		}

		return sumFreq;
	}

	/**
	 * This method is to sort the Arraylist argument and take the first one
	 * according to alphabetic order
	 * @param words refers to an ArrayList
	 * @return a String word
	 */

	public static String findByAlphabet(ArrayList<String> words) {
		Collections.sort(words);
		return words.get(0);
	}



	/**
	 * This method is find and add all those words that have the same 
	 * length as the user puts to an Arraylist matchingAnswers
	 * @param length refers to the int length of the word
	 */
	public static void findMatching(int length) {
		for (int i = 0; i < word.size(); i++) {
			if (word.get(i).length() == length) {
				matchingAnswers.add(word.get(i));
			}
		}
	}
	/**
	 * This method is for when the computer is the chooser, it secretly change
	 * its solution word so that the position answer has the largest number of
	 * words that could match it. It returns the correct String solution
	 * @param myArray refers to an ArrayList argument
	 * @param a refers to a char a guessed from user
	 * @return a single String, the first one in ArrayList myArray
	 */
	public static String changeSecretWord(ArrayList<String> myArray, char a) {

		int max = 0;
		int temp = ((String) myArray.get(0)).length();
		int index = (int) Math.pow(2, temp);
		ArrayList<ArrayList<String>> group = new ArrayList<ArrayList<String>>();
		for (int i = 0; i < index; i++)
			group.add(new ArrayList<String>(index));

		int number[] = new int[index];
		for (int i = 0; i < myArray.size(); i++) {
			String maybe = (String) myArray.get(i);
			int sec_Word = generateKey(maybe, a);
			group.get(sec_Word).add(maybe);
			number[sec_Word] += 1;
			if (number[sec_Word] > number[max]) {
				max = sec_Word;
			}
			if (number[sec_Word] == number[max] && sec_Word < max) {
				max = sec_Word;
			}
		}
		myArray.clear();
		for (int j = 0; j < number[max]; j++) {
			myArray.add((String) group.get(max).get(j));
		}

		return (String) myArray.get(0);
	}
	/**
	 * This method is used for display the position for String el in String obj
	 * @param obj refers to a string variable obj
	 * @param el refers to a string variable el
	 * @return an ArrayList answer
	 */
	public static ArrayList<Integer> positionsame(String obj, String el) {
        ArrayList<Integer> answer = new ArrayList<Integer>();
        for (int i = 0; i < obj.length(); i++) {
            if (obj.charAt(i) == el.charAt(0)) {
                answer.add(i);
            }
        }
        if(answer.size()==0){
            answer.add(-1);
        }
        return answer;
    }

}