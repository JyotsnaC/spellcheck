package spell;

import spell.SpellChecker;
import spell.Spellmistake;

/**
 * This class has main method which *generates* words with spelling mistakes of the above form, 
 * starting with correctly spelled English words. Pipe its output into the first 
 * program and verify that there are no occurrences of "NO SUGGESTION" in the output.
 * @author jyotsna chatradhi
 */
public class Spellcheckautomata {
	/**
	 * Main method to generate Mistake word and Spell the correct one for generated word
	 * @param args
	 */
	public static void main(String[] args) {
		String dictionaryPath = "/usr/share/dict/words";
		if(args.length == 2 && "-d".equals(args[0])) {
			dictionaryPath = args[1];
		}
		//Infinite loop for the process
		for ( ; ; ) {
			String word = new Spellmistake(dictionaryPath).spellmistake();
			System.out.println("Generate Mispelled Word: " + word);
			SpellChecker sc = new SpellChecker(dictionaryPath);
			System.out.println("Spell Check Word: " + sc.spellcheck(word));
		}
	}

}
