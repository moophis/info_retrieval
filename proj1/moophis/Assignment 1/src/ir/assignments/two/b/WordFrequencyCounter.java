package ir.assignments.two.b;

import ir.assignments.two.a.Frequency;
import ir.assignments.two.a.Utilities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Comparator implementation of Frequency class 
 */

class FrequencyComparator implements Comparator<Frequency> {
	public static final FrequencyComparator FREQ = 
			new FrequencyComparator(0);
	public static final FrequencyComparator TXT = 
			new FrequencyComparator(1);
	private int type = 0;
	
	private FrequencyComparator(int type) {
		this.type = type;
	}
	
	@Override
	public int compare(Frequency f1, Frequency f2) {
		if (type == 0) {
			return f1.getFrequency() > f2.getFrequency() ?
					-1 : f1.getFrequency() == f2.getFrequency() ? 0 : 1;
		} else {
			return f1.getText().compareTo(f2.getText());
		}
	}
}

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
		List<Frequency> list = new ArrayList<Frequency>();
		HashMap<String, Integer> freqMap = new HashMap<String, Integer>();
		
		for (String s : words) {
			if (!freqMap.containsKey(s)) {
				freqMap.put(s, 1);
			} else {
				freqMap.put(s, freqMap.get(s) + 1);
			}
		}
		
		for (Map.Entry<String, Integer> m : freqMap.entrySet()) {
			list.add(new Frequency(m.getKey(), m.getValue()));
		}
		
		Collections.sort(list, FrequencyComparator.TXT);		
		Collections.sort(list, FrequencyComparator.FREQ);		
		return list;
	}
	
	
	/**
	 * Runs the word frequency counter. The input should be the path to a text file.
	 * 
	 * @param args The first element should contain the path to a text file.
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		//File file = new File(args[0]);
		File file = new File("/Users/liqiangw/text.txt");
		List<String> words = Utilities.tokenizeFile(file);
		List<Frequency> frequencies = computeWordFrequencies(words);
		Utilities.printFrequencies(frequencies);
	}
}


