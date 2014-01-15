package ir.assignments.two.a;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
// import java.util.HashSet;

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
	 */
	public static ArrayList<String> tokenizeFile(File input) {
		// TODO Write body!
		ArrayList<String> strs = new ArrayList<String>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(input));
			String line = reader.readLine();
			while (line != null) {
				String[] ss = line.split("\\W");
				for (String item : ss) {
					if (item.length() > 0)
						strs.add(item.toLowerCase());
				}
				line = reader.readLine();
			}
			reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return strs;
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
		// Here we assume the items' name are unique
		// HashSet<String> itemNames = new HashSet<String>();
		if (frequencies == null || frequencies.size() == 0) {
			System.out.println("Total item count: " + Integer.toString(0));
			System.out.println("Unique item count: " + Integer.toString(0));
			return;
		}			
		int totalCount = 0;
		for (Frequency item : frequencies) {
			totalCount += item.getFrequency();
		}
		// Compute the number of gram in each item
		int numSpaces = frequencies.get(0).getText().trim().split("\\W").length;

		if (numSpaces >= 2) {
			System.out.println("Total " + Integer.toString(numSpaces) +"-gram count: " + Integer.toString(totalCount));
			System.out.println("Unique " + Integer.toString(numSpaces) +"-gram count: " + Integer.toString(frequencies.size()));
		} else {
			System.out.println("Total item count: " + Integer.toString(totalCount));
			System.out.println("Unique item count: " + Integer.toString(frequencies.size()));
		}
		System.out.println("");
		for (Frequency item : frequencies) {
			System.out.println(item.getText() + "\t" + item.getFrequency());
		}
	}
}
