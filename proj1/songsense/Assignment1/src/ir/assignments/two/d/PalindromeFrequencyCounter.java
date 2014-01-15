package ir.assignments.two.d;

import ir.assignments.two.a.Frequency;
import ir.assignments.two.a.Utilities;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Map.Entry;

public class PalindromeFrequencyCounter {
	/**
	 * This class should not be instantiated.
	 */
	private PalindromeFrequencyCounter() {}
	
	/**
	 * Takes the input list of words and processes it, returning a list
	 * of {@link Frequency}s.
	 * 
	 * This method expects a list of lowercase alphanumeric strings.
	 * If the input list is null, an empty list is returned.
	 * 
	 * There is one frequency in the output list for every 
	 * unique palindrome found in the original list. The frequency of each palindrome
	 * is equal to the number of times that palindrome occurs in the original list.
	 * 
	 * Palindromes can span sequential words in the input list.
	 * 
	 * The returned list is ordered by decreasing size, with tied palindromes sorted
	 * by frequency and further tied palindromes sorted alphabetically. 
	 * 
	 * The original list is not modified.
	 * 
	 * Example:
	 * 
	 * Given the input list of strings 
	 * ["do", "geese", "see", "god", "abba", "bat", "tab"]
	 * 
	 * The output list of palindromes should be 
	 * ["do geese see god:1", "bat tab:1", "abba:1"]
	 *  
	 * @param words A list of words.
	 * @return A list of palindrome frequencies, ordered by decreasing frequency.
	 */
	private static List<Frequency> computePalindromeFrequencies(ArrayList<String> words) {
		// TODO Write body!
		// You will likely want to create helper methods / classes to help implement this functionality
		List<Frequency> freqs = new ArrayList<Frequency>();
		if (words == null || words.size() == 0) {
			return freqs;
		}
		// determine if a sequential of words is palindrome and then push it into dictionary
		HashMap<String, Integer> dictFreqs = new HashMap<String, Integer>();
		for (int i = 0; i < words.size(); ++i) {
			String word = "";
			for (int j = i; j < words.size(); ++j) {
				word += " " + words.get(j);
				word = word.trim();
				if (PalindromeFrequencyCounter.isPalindrome(word)) {
					int freq = dictFreqs.containsKey(word) ? dictFreqs.get(word) : 0;
					dictFreqs.put(word, freq-1);
				}
			}
		}
		// read each unique string into tree map to sort their order by frequency
		TreeMap<Integer, ArrayList<String>> sortedMap = new TreeMap<Integer, ArrayList<String>>();
		for (Entry<String, Integer> entry: dictFreqs.entrySet()) {
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
	private static boolean isPalindrome(String word) {
		word = word.replaceAll("\\s", "");
		for (int i = 0; i < Math.floor(word.length() / 2); ++i) {
			if (word.charAt(i) != word.charAt(word.length() - i - 1))
				return false;
		}
		return true;
	}
	/**
	 * Runs the 2-gram counter. The input should be the path to a text file.
	 * 
	 * @param args The first element should contain the path to a text file.
	 */
	public static void main(String[] args) {
		if (args == null || args.length == 0) {
			System.out.println("args is empty");
			return;
		}
		File file = new File(args[0]);
		ArrayList<String> words = Utilities.tokenizeFile(file);
		List<Frequency> frequencies = computePalindromeFrequencies(words);
		Utilities.printFrequencies(frequencies);
	}
}
