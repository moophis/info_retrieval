package ir.assignments.two.a;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A collection of utility methods for text processing.
 */
public class Utilities {
	/**
	 * Reads the input text file and splits it into alphanumeric tokens.
	 * Returns an ArrayList of these tokens, ordered according to their
	 * occurrence in the original text file.
	 * 
	 * Non-alphanumeric characters delineate tokens, and are discarded.
	 *
	 * Words are also normalized to lower case. 
	 * 
	 * Example:
	 * 
	 * Given this input string
	 * "An input string, this is! (or is it?)"
	 * 
	 * The output list of strings should be
	 * ["an", "input", "string", "this", "is", "or", "is", "it"]
	 * 
	 * @param input The file to read in and tokenize.
	 * @return The list of tokens (words) from the input file, ordered by occurrence.
	 * @throws IOException 
	 */
	public static ArrayList<String> tokenizeFile(File input) throws IOException  {
		// TODO Write body!
		ArrayList<String> words = new ArrayList<String>();
		BufferedReader reader;
		String line;
		
		reader = new BufferedReader(new FileReader(input));
		
		// firstly read strings from the file
		while ((line = reader.readLine()) != null) {
			String[] buf;
//			buf = line.toLowerCase().split("\\W");
			buf = line.toLowerCase().split("[^0-9A-Za-z]+"); // most stringent 
//			buf = line.toLowerCase().split("[^0-9A-Za-z'_-]+"); // allow ' _ -
			if (buf == null) {
				System.err.println("buf is null!");
			} else {
				for (String s : buf) {
					if (s.length() > 0)  // eliminate spaces
						words.add(s);
				}
			}
		}
		
		// then sort the words
		//Collections.sort(words);
		
		reader.close();
		return words;
	}
	
	/**
	 * Takes a list of {@link Frequency}s and prints it to standard out. It also
	 * prints out the total number of items, and the total number of unique items.
	 * 
	 * Example one:
	 * 
	 * Given the input list of word frequencies
	 * ["sentence:2", "the:1", "this:1", "repeats:1",  "word:1"]
	 * 
	 * The following should be printed to standard out
	 * 
	 * Total item count: 6
	 * Unique item count: 5
	 * 
	 * sentence	2
	 * the		1
	 * this		1
	 * repeats	1
	 * word		1
	 * 
	 * 
	 * Example two:
	 * 
	 * Given the input list of 2-gram frequencies
	 * ["you think:2", "how you:1", "know how:1", "think you:1", "you know:1"]
	 * 
	 * The following should be printed to standard out
	 * 
	 * Total 2-gram count: 6
	 * Unique 2-gram count: 5
	 * 
	 * you think	2
	 * how you		1
	 * know how		1
	 * think you	1
	 * you know		1
	 * 
	 * @param frequencies A list of frequencies.
	 */
	public static void printFrequencies(List<Frequency> frequencies) {
		// TODO Write body!
		int totalCount = 0;
		int gram = 1;	// how many words in an item string
		String test;
		
		if (frequencies.isEmpty())
			return;
		
		test = frequencies.get(0).getText();
		for (int i = 0; i < test.length(); i++) {
			if (test.charAt(i) == ' ')
				gram++;
		}
		String assemble = gram > 1 ? (new Integer(gram).toString() + "-gram") : "item";
		
		for (Frequency f : frequencies) {
			totalCount += f.getFrequency();
		}
		System.out.println("Total " + assemble + " count: " + totalCount);
		System.out.println("Unique " + assemble + " count: " + frequencies.size());
		System.out.println("");
		
		for (Frequency f : frequencies) {
			System.out.printf("%-10s\t%-2d\n", f.getText(), f.getFrequency());
		}
	}
	
	// @test 
	public static void main(String[] args) throws IOException {
		ArrayList<String> stringArray;
		File f = new File("/Users/liqiangw/Test/text.txt");
		System.out.println("Utility testing...");
		
		System.out.println(f.isFile());
		System.out.println(f.canRead());

		stringArray = tokenizeFile(f);
		System.out.println("------------------");

		for (String s : stringArray) {
			System.out.println(s);
		}
		System.out.println("------------------");
		System.out.println("total number: " + stringArray.size());
		System.out.println("------------------");
		System.out.println("Frequency statistics test --->");
		List<Frequency> list = Arrays.asList(new Frequency("you think", 2),
											 new Frequency("I think", 2),
											 new Frequency("you know", 1),
											 new Frequency("Know me", 1));
		printFrequencies(list);
	}
}
