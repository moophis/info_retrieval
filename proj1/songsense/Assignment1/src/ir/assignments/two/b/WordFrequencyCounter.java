package ir.assignments.two.b;

import ir.assignments.two.a.Frequency;
import ir.assignments.two.a.Utilities;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.HashMap;
import java.util.Collections;

/**
 * Counts the total number of words and their frequencies in a text file.
 */
public final class WordFrequencyCounter {
	/**
	 * This class should not be instantiated.
	 */
	private WordFrequencyCounter() {}
	
	/**
	 * Takes the input list of words and processes it, returning a list
	 * of {@link Frequency}s.
	 * 
	 * This method expects a list of lowercase alphanumeric strings.
	 * If the input list is null, an empty list is returned.
	 * 
	 * There is one frequency in the output list for every 
	 * unique word in the original list. The frequency of each word
	 * is equal to the number of times that word occurs in the original list. 
	 * 
	 * The returned list is ordered by decreasing frequency, with tied words sorted
	 * alphabetically.
	 * 
	 * The original list is not modified.
	 * 
	 * Example:
	 * 
	 * Given the input list of strings 
	 * ["this", "sentence", "repeats", "the", "word", "sentence"]
	 * 
	 * The output list of frequencies should be 
	 * ["sentence:2", "the:1", "this:1", "repeats:1",  "word:1"]
	 *  
	 * @param words A list of words.
	 * @return A list of word frequencies, ordered by decreasing frequency.
	 */
	public static List<Frequency> computeWordFrequencies(List<String> words) {
		// TODO Write body!
		List<Frequency> freqs = new ArrayList<Frequency>(); 
		if (words == null || words.size() == 0) {
			return freqs;
		}
		// read words into hash map to get frequency of each unique string
		HashMap<String, Integer> freqDict = new HashMap<String, Integer>();
		for (String word : words) {
			word = word.toLowerCase(); // convert any UPPER CASE word into lower case
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
	 * Runs the word frequency counter. The input should be the path to a text file.
	 * 
	 * @param args The first element should contain the path to a text file.
	 */
	public static void main(String[] args) {
		File file = new File(args[0]);
		List<String> words = Utilities.tokenizeFile(file);
		List<Frequency> frequencies = computeWordFrequencies(words);
		Utilities.printFrequencies(frequencies);
	}
}
