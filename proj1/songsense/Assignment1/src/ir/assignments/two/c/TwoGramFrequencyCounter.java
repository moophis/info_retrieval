package ir.assignments.two.c;

import ir.assignments.two.a.Frequency;
import ir.assignments.two.a.Utilities;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.Map.Entry;

/**
 * Count the total number of 2-grams and their frequencies in a text file.
 */
public final class TwoGramFrequencyCounter {
	/**
	 * This class should not be instantiated.
	 */
	private TwoGramFrequencyCounter() {}
	
	/**
	 * Takes the input list of words and processes it, returning a list
	 * of {@link Frequency}s.
	 * 
	 * This method expects a list of lowercase alphanumeric strings.
	 * If the input list is null, an empty list is returned.
	 * 
	 * There is one frequency in the output list for every 
	 * unique 2-gram in the original list. The frequency of each 2-grams
	 * is equal to the number of times that two-gram occurs in the original list. 
	 * 
	 * The returned list is ordered by decreasing frequency, with tied 2-grams sorted
	 * alphabetically. 
	 * 
	 * 
	 * 
	 * Example:
	 * 
	 * Given the input list of strings 
	 * ["you", "think", "you", "know", "how", "you", "think"]
	 * 
	 * The output list of 2-gram frequencies should be 
	 * ["you think:2", "how you:1", "know how:1", "think you:1", "you know:1"]
	 *  
	 * @param words A list of words.
	 * @return A list of two gram frequencies, ordered by decreasing frequency.
	 */
	public static List<Frequency> computeTwoGramFrequencies(ArrayList<String> words) {
		// TODO Write body!
		List<Frequency> freqs = new ArrayList<Frequency>(); 
		if (words == null || words.size() <= 1) {
			return freqs;
		}
		// read words into hash map to get frequency of each unique string
		HashMap<String, Integer> freqDict = new HashMap<String, Integer>();
		for (int i = 0; i < words.size()-1; ++i) {
			String word_1 = words.get(i), word_2 = words.get(i+1), word;
			 // convert any UPPER CASE word into lower case and catenate two words
			word = word_1 = word_1.toLowerCase() + " " + word_2.toLowerCase();
			if (freqDict.containsKey(word)) {
				freqDict.put(word, freqDict.get(word)-1);
			} else {
				freqDict.put(word, -1);
			}
		}
		// read each unique string into tree map to sort their order by frequency
		TreeMap<Integer, ArrayList<String>> sortedMap = new TreeMap<Integer, ArrayList<String>>();
		for (Entry<String, Integer> entry: freqDict.entrySet()) {
		    String key = entry.getKey();
		    int value = entry.getValue();
		    if (sortedMap.containsKey(value)) {
		    	sortedMap.get(value).add(key);
		    } else {
		    	ArrayList<String >tempList = new ArrayList<String>();
		    	tempList.add(key);
		    	sortedMap.put(value, tempList);
		    }
		}
		// feed the words and their frequency into output list
		for (Entry<Integer, ArrayList<String>> entry : sortedMap.entrySet()) {
			int frequency = entry.getKey();
			ArrayList<String> strList = entry.getValue();
			Collections.sort(strList); // sort the words with alphabetically within tied words 
			for (String str : strList) {
				Frequency freq = new Frequency(str, -frequency);
				freqs.add(freq);
			}
		}
		return freqs;
	}
	
	/**
	 * Runs the 2-gram counter. The input should be the path to a text file.
	 * 
	 * @param args The first element should contain the path to a text file.
	 */
	public static void main(String[] args) {
		if (args == null || args.length == 0) {
			System.out.println("arg0 is empty");
		}
		File file = new File(args[0]);
		ArrayList<String> words = Utilities.tokenizeFile(file);
		List<Frequency> frequencies = computeTwoGramFrequencies(words);
		Utilities.printFrequencies(frequencies);
	}
}
