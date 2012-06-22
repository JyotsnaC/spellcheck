package spell;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.Iterator;
/**
 * This class has a main method that reads a large list of English words 
 * (e.g. from /usr/share/dict/words on a unix system) into memory, 
 * and then reads words from stdin, and prints either the best spelling suggestion, 
 * or "NO SUGGESTION" if no suggestion can be found. 
 * 
 * @author jyotsna chatradhi
 */
public class SpellChecker {

	public SpellChecker(String dictionaryPath) {
		this.dictionaryPath = dictionaryPath;
		this.dictionary = new HashMap<String, Set<String>>();
		loadDictionary();
	}

	String dictionaryPath; // Path to file of dictionary word list. e.g. "/usr/share/dict/words"
	HashMap<String, Set<String>> dictionary;//Stores the phonetic word as the key and set of all words which generate same phonetic word
	
	/**
	 * Used internally to populate dictionary from dictionaryPath
	 * Complexity: O(n) where n is the number of words in the dictionary.
	 */ 
	private void loadDictionary() {
		Scanner scanner = null;
		String tempsoundex = null;
		HashSet<String> wordset = null;
		try {
			scanner = new Scanner(new FileInputStream(dictionaryPath));
			while(scanner.hasNextLine()) {
				String word = scanner.nextLine();
				tempsoundex = soundex(word.toLowerCase());
				wordset = (HashSet<String>) dictionary.get(tempsoundex);
				if(wordset != null){
					wordset.add(word.toLowerCase());
				} else{
					wordset = new HashSet<String>();
					wordset.add(word.toLowerCase());
				}
				dictionary.put(soundex(word.toLowerCase()), wordset);
			}
		} catch(IOException e) {
			System.err.println(e.toString());
			e.printStackTrace();
		} finally {
			if(scanner != null) {
				scanner.close();
			}
		}
	}

	/**
	 * This method replace below conversion parameter as per the phonetic algorithm
	 * Replace consonants with digits as follows (after the first letter):
     * b, f, p, v => 1
	 * c, g, j, k, q, s, x, z => 2
	 * d, t => 3
	 * l => 4
     * m, n => 5
     * r => 6
	 * @param char C of a given string
	 * @return an integer as string for the corresponding alphabet
	 */
	private static String getCode(char c){
		switch(c){
		case 'B': case 'F': case 'P': case 'V':
			return "1";
		case 'C': case 'G': case 'J': case 'K':
		case 'Q': case 'S': case 'X': case 'Z':
			return "2";
		case 'D': case 'T':
			return "3";
		case 'L':
			return "4";
		case 'M': case 'N':
			return "5";
		case 'R':
			return "6";
		default:
			return "";
		}
	}

	/**
	 * Soundex algorithm is implemented to index the words with there key paramaeters.
	 * For more on soundex algorithm please visit below url
	 * http://en.wikipedia.org/wiki/Soundex
	 * 
	 * @param s is string obtained from the dictionary.
	 * @return string after considering the soundex algorithm
	 */
	public static String soundex(String s){
		String code, previous, soundex;
		code = s.toUpperCase().charAt(0) + "";
		previous = "7";
		for(int i = 1;i < s.length();i++){
			String current = getCode(s.toUpperCase().charAt(i));
			if(current.length() > 0 && !current.equals(previous)){
				code = code + current;
			}
			previous = current;
		}
		soundex = (code + "0000").substring(0, 4);
		return soundex;
	}

	/**
	 * Query the spellchecker with a given input string and returns a suggestion. 
	 * If no word can be suggested, "NO SUGGESTION" will be returned.
	 * Complexity: O(n) where n is the length of the input string
	 */
	public String spellcheck(String word) {
		int distance = 0;
		int distancetemp = 100;
		String matchingword = null;
		HashSet<String> wordset = (HashSet<String>) dictionary.get(soundex(word.toLowerCase()));
		if (wordset == null){
			return "NO SUGGESTION";
		}
		else if(wordset.contains(word.toLowerCase())){
			return word.toLowerCase();
		}
		else{
			Iterator<String> it = wordset.iterator();
			while(it.hasNext()){
				String words = it.next();
				distance = computeLevenshteinDistance(words, word.toLowerCase());
				if (distancetemp > distance){
					distancetemp = distance;
					matchingword = words;		
				}
			}
			return matchingword;	  
		}
	}
 
	/**
	 * Minumum of given numbers
	 * @param a is an integer
	 * @param b is an integer
	 * @param c is an integer
	 * @return an interger which is minimum of a b c
	 */
	private static int minimum(int a, int b, int c) {
		return Math.min(Math.min(a, b), c);
	}

	/**
	 * Has computed the distance between the strings using Levenshtein Distance algorithm which helps
	 * in finding out the nearest word to the misspelled one
	 * More information of the algorithm can be obtained from the below url
	 * http://en.wikipedia.org/wiki/Levenshtein_distance
	 * @param str1 is a string 
	 * @param str2 is a string
	 * @return the distance between the strings str1 and str2
	 */
	public static int computeLevenshteinDistance(CharSequence str1,
			CharSequence str2) {
		int[][] distance = new int[str1.length() + 1][str2.length() + 1];

		for (int i = 0; i <= str1.length(); i++)
			distance[i][0] = i;
		for (int j = 0; j <= str2.length(); j++)
			distance[0][j] = j;

		for (int i = 1; i <= str1.length(); i++)
			for (int j = 1; j <= str2.length(); j++)
				distance[i][j] = minimum(
						distance[i - 1][j] + 1,
						distance[i][j - 1] + 1,
						distance[i - 1][j - 1]
								+ ((str1.charAt(i - 1) == str2.charAt(j - 1)) ? 0
										: 1));

		return distance[str1.length()][str2.length()];
	}

	/**
	 * Main Method
	 * @param args
	 */
	public static void main(String[] args) {
		String dictionaryPath = "/usr/share/dict/words";
		if(args.length == 2 && "-d".equals(args[0])) {
			dictionaryPath = args[1];
		}
		SpellChecker sc = new SpellChecker(dictionaryPath);
		String input;
		String prompt = "> ";
		System.out.print(prompt);
		Scanner scanner = new Scanner(System.in);
		while(scanner.hasNextLine()) {
			input = scanner.nextLine();
			System.out.println(sc.spellcheck(input));
			System.out.print(prompt);
		}
		System.out.println();
	}

}
