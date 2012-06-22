package spell;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

/**
 * This class helps to create a mispelled word to help test the files.
 * @author jyotsnachatradhi
 *
 */
public class Spellmistake {
    /**
     * Intializes
     * @param dictionaryPath
     */
	public Spellmistake(String dictionaryPath) {
		this.dictionaryPath = dictionaryPath;
	}

	String dictionaryPath;
	static Random random = new Random(System.currentTimeMillis());
	static int random_additionalLetters = 1; // Maximum number of additional letters to randomly add
	static String[] vowels = {"a", "e", "i", "o", "u"};
    
	/**
	 * Three spell mistakes are taken into considerations
	 * The class of spelling mistakes to be created is as follows:
	 * 1) Case (upper/lower) errors: "inSIDE" => "inside"
	 * 2) Repeated letters: "jjoobbb" => "job"
	 * 3) Incorrect vowels: "weke" => "wake"
	 * @return string with the spelling mistake
	 */
	public String spellmistake() {
		Scanner scanner = null;
		String chosenWord = null;
		int i = 2;
		try {
			scanner = new Scanner(new FileInputStream(dictionaryPath));
			while(scanner.hasNextLine()) {
				String word = scanner.nextLine();
				if(chosenWord == null) {
					chosenWord = word;
				} else {
					if(random.nextFloat() <= 1.0/i) {
						chosenWord = word;
					}
				}
				i++;
			}
		} catch(IOException e) {
			System.err.println(e.toString());
			e.printStackTrace();
		} finally {
			if(scanner != null) {
				scanner.close();
			}
		}
		Random rand = new Random(); 
		int c = rand.nextInt(8);
		switch(c){
		case 1: 
			return changeCase(chosenWord.toLowerCase());
		case 2: 
			return changeCase(repeatLetters(chosenWord.toLowerCase()));
		case 3: 
			return changeCase(changeVowels(repeatLetters(chosenWord.toLowerCase())));
		case 4:
			return changeCase(changeVowels(chosenWord.toLowerCase()));
		case 5: 
			return changeVowels(repeatLetters(chosenWord.toLowerCase()));
		case 6:
			return repeatLetters(chosenWord.toLowerCase());
		case 7:
			return changeVowels(chosenWord.toLowerCase());
		default:
			return chosenWord.toLowerCase();
		}
	}

	/**
	 * This method changes case of the given word.
	 * @param word is string
	 * @return a string with change in case
	 */
	public static String changeCase(String word) {
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < word.length(); i++) {
			char c = word.charAt(i);
			if(random.nextFloat() <= 0.5) {
				sb.append(Character.toString(c).toLowerCase());
			} else {
				sb.append(Character.toString(c).toUpperCase()); 
			}
		}
		return sb.toString();
	}

	/**
	 * This method changes vowels of given word
	 * @param word is a string
	 * @return a string with change of vowels in the word
	 */
	public static String changeVowels(String word) {
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < word.length(); i++) {
			char c = word.charAt(i);
			if(isVowel(c)) {
				if(random.nextFloat() <= 0.5) {
					String randomVowel = vowels[random.nextInt(vowels.length)];
					sb.append(randomVowel);
				} else {
					sb.append(c);
				}
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	/**
	 * Add Repetition of letter to the given words 
	 * @param word is a string
	 * @return a word with repetition of letters
	 */
	public static String repeatLetters(String word) {
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < word.length(); i++) {
			char c = word.charAt(i);
			sb.append(c);
			for(int j = 0; j < random.nextInt(random_additionalLetters + 1); j++) {
				if(random.nextFloat() <= 0.5) {
					sb.append(c);
				}
			}
		}
		return sb.toString();
	}

	/**
	 * Check if the given character is a vowel
	 * @param c
	 * @retrun true if it is a vowel else false
	 */
	public static boolean isVowel(char c) {
		return ((c == 'a') || (c == 'e') || (c == 'i') || (c == 'o') || (c == 'u'));
	}

}