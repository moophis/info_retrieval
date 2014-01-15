package ir.assignments.two.d;

import ir.assignments.two.a.Frequency;
import ir.assignments.two.a.Utilities;
import ir.assignments.two.b.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class PalindromeFrequencyCounter {
	/**
	 * Raw data is stored in a hash map  
	 */
	private static Map<String, Integer> freqMap = new HashMap<String, Integer>();
	
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
		List<Frequency> list = new ArrayList<Frequency>();
		final int N = words.size();
		
		if (words.isEmpty())
			return list;
		
		// FIXME: It is just a naive implementation...
		for (int i = 0; i < N; i++) 
			for (int j = i; j < N; j++) {
				String s = stringAssembler(words, i, j);
				if (isPal(s)) {
					updateMap(s);
				}
			}
		
		for (Map.Entry<String, Integer> m : freqMap.entrySet()) {
			list.add(new Frequency(m.getKey(), m.getValue()));
		}
		/*
		 * FIXME: When there is a tie, should the sequence be kept in order
		 * of occurrence? 
		 */
		//Collections.sort(list, FrequencyComparator.TXT);		
		Collections.sort(list, FrequencyComparator.FREQ);	

		return list;
	}
	
	/**
	 * Assemble the strings from words[from, to].
	 * 
	 * @param words raw words
	 * @param from beginning index
	 * @param to ending index
	 */
	private static String stringAssembler(ArrayList<String> words, int from, int to) {
		String assemble = new String();
		final int N = words.size();
		
		assert(from >=  0 && from < N && from <= to && to < N);
		
		for (int i = from; i < to; i++) { 
			assemble += words.get(i);
			assemble += " ";
		}
		assemble += words.get(to);
		
		return assemble;
	}
	
	/**
	 * Predicate whether the input string is a palindrome.
	 * 
	 * @param str Input string to be checked.
	 * @return Judgment.
	 */
	private static boolean isPal(String str) {
		if (str == "")
			return true;
		System.out.println(str);
		
		for (int i = 0, j = str.length() - 1; i <= j; i++, j--) {
			// skip spaces
			while (str.charAt(i) == ' ')
				i++;
			while (str.charAt(j) == ' ')
				j--;
			
			if (str.charAt(i) != str.charAt(j))
				return false;
		}
		
		return true;
	}
	
	/**
	 * Update the frequency statistics.
	 * 
	 * @param str Input string.
	 */
	private static void updateMap(String str) {
		if (str == "") 
			return;
		
		if (!freqMap.containsKey(str)) {
			freqMap.put(str, 1);
		} else {
			freqMap.put(str, freqMap.get(str) + 1);
		}		
	}
	
	/**
	 * Runs the 2-gram counter. The input should be the path to a text file.
	 * 
	 * @param args The first element should contain the path to a text file.
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		//File file = new File(args[0]);
		File file = new File("/Users/liqiangw/text.txt");
		ArrayList<String> words = Utilities.tokenizeFile(file);
		List<Frequency> frequencies = computePalindromeFrequencies(words);
		Utilities.printFrequencies(frequencies);
	}
}
