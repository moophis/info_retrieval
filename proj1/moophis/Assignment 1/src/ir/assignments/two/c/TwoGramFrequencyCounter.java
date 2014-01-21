package ir.assignments.two.c;

import ir.assignments.two.a.Frequency;
import ir.assignments.two.a.Utilities;
import ir.assignments.two.b.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
	private static List<Frequency> computeTwoGramFrequencies(ArrayList<String> words) {
		// TODO Write body!
		List<Frequency> list = new ArrayList<Frequency>();
		HashMap<String, Integer> freqMap = new HashMap<String, Integer>();
		
		if (words.isEmpty())
			return list;
			
		Iterator<String> it = words.iterator();
		String first = it.next();
		String second;
		for (int i = 0; i < words.size() - 1; i++) {
			second = it.next();
			String twoGram = first + " " + second;
			
			if (!freqMap.containsKey(twoGram)) {
				freqMap.put(twoGram, 1);
			} else {
				freqMap.put(twoGram, freqMap.get(twoGram) + 1);
			}
			
			first = second;
		}
		
		for (Map.Entry<String, Integer> m : freqMap.entrySet()) {
			list.add(new Frequency(m.getKey(), m.getValue()));
		}
		Collections.sort(list, FrequencyComparator.TXT_ALNUM);		
		Collections.sort(list, FrequencyComparator.FREQ);	
		
		return list;
	}
	
	/**
	 * Runs the 2-gram counter. The input should be the path to a text file.
	 * 
	 * @param args The first element should contain the path to a text file.
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		File file = new File(args[0]);
		//File file = new File("/Users/liqiangw/Test/HP2.txt");
		ArrayList<String> words = Utilities.tokenizeFile(file);
		List<Frequency> frequencies = computeTwoGramFrequencies(words);
		Utilities.printFrequencies(frequencies);
	}
}
