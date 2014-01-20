package ir.assignments.two.d;

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
		
		if (words.isEmpty())
			return list;
		
		/*
		 * In this implementation, I choose Manacher's Algorithm which deals
		 * with longest palindrome substring with O(n) time complexity as well
		 * as O(n) space complexity.
		 * 
		 * For more information about this algorithm, refer to 
		 * http://leetcode.com/2011/11/longest-palindromic-substring-part-ii.html
		 * http://www.felix021.com/blog/read.php?2040
		 **/
		String str = buildString(words);
		System.out.println(str);
		int[] P = manacherAlgorithm(buildString(words));
		for (int i : P) {
			System.out.print(i + " ");
		}
		System.out.println();
		
		// post-processing: split and convert re-assembled string into
		// the original natural ones.
		for (int i = 0; i < P.length; i++) {
			if (P[i] == 0) // skip unused information
				continue;
			/* 
			 * Here we use r > 1 as condition is because 
			 * the word of single character is not counted
			 * as a palindrome.
			 * 
			 * That would take O(n^2) time...
			 **/
			for (int r = P[i]; r > 1; r = r - 2) {
				if (str.charAt(i - r) == '?' && str.charAt(i + r) == '?') {
					String s = reparseString(str.substring(i - r, i + r + 1));
					updateMap(s);
				}
			}
		}
		
		for (Map.Entry<String, Integer> m : freqMap.entrySet()) {
			list.add(new Frequency(m.getKey(), m.getValue()));
		}
		
		/*
		 * In my implementation, the size of a word in Frequency class 
		 * is counted without considering spaces.
		 * 
		 * Since sorting by word size enjoys highest priority, I just 
		 * put it lastly.
		 */
		Collections.sort(list, FrequencyComparator.TXT_ALNUM);		
		Collections.sort(list, FrequencyComparator.FREQ);
		Collections.sort(list, FrequencyComparator.TXT_WORDSIZE);

		return list;
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
	 * Pre-processing step for running Manacher's Algorithm by inserting 
	 * informational symbols around each original string characters.
	 * 
	 * Specification:
	 * '^' -> the beginning of the whole string;
	 * '?' -> the separator of each single word;
	 * '#' -> the separator of each single character within a word;
	 * '$' -> the end of the whole string.
	 * 
	 * Example:
	 *     input: "abab ba"
	 *     output: "^?a#b#a#b?b#a?$"
	 *     
	 * @param words a list of words
	 * @return assembled string
	 **/
	private static String buildString(ArrayList<String> words) {
		StringBuilder sb = new StringBuilder();
		
		if (words == null)
			return "^$";
		
		sb.append("^?");
		for (String s : words) {
			int len = s.length();
			for (int i = 0; i < len - 1; i++) {
				sb.append(s.charAt(i));
				sb.append('#');
			}
			sb.append(s.charAt(len - 1));
			sb.append('?');
		}
		sb.append('$');
		
		return sb.toString();
	}
	
	/**
	 * The main body of the Manacher's algorithm.  
	 * 
	 * @param s the pre-processed string.
	 * @return an array of diameters of the palindrome indexed by the 
	 *         position of each single character.
	 **/
	private static int[] manacherAlgorithm(String s) {
		if (s == "")
			return null;
		
		int[] p = new int[s.length()];
		final int len = s.length();
		
		for (int i = 0; i < len; i++) 
			p[i] = 0;
		
		int mx = 0, id = 0;
		for (int i = 1; i < len - 1; i++) {
			p[i] = (mx > i) ? Math.min(p[2 * id - i], mx - i) : 0;
			while (s.charAt(i + 1 + p[i]) == s.charAt(i - 1 - p[i])
				|| (s.charAt(i + 1 + p[i]) == '#') && (s.charAt(i - 1 - p[i]) == '?')
				|| (s.charAt(i + 1 + p[i]) == '?') && (s.charAt(i - 1 - p[i]) == '#')) {
				p[i] = p[i] + 1;
			}
			if (i + p[i] > mx) {
				mx = i + p[i];
				id = i;
			}
		}
		
		return p;
	}
	
	/**
	 * Remove those separators and organize strings as it 
	 * originally were.
	 * 
	 * @param s the string to be converted.
	 * @return the processed string.
	 **/
	private static String reparseString(String s) {
		StringBuilder sb = new StringBuilder();
		final int len = s.length();
		
//		System.out.println("reparsing: " + s);
		
		/*
		 * now we assume that the format of the input 
		 * string is correct 
		 **/
		for (int i = 0; i < len; i++) {
			if (i == 0 || i == len - 1)
				continue;
			if (s.charAt(i) == '?')
				sb.append(' ');
			else if (s.charAt(i) == '#')
				continue;
			else // alphanumeric
				sb.append(s.charAt(i));
		}
		
		return sb.toString();
	}
	
	/**
	 * (PHASED OUT, DO NOT USE)
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
	 * (PHASED OUT, DO NOT USE)
	 * Predicate whether the input string is a palindrome.
	 * 
	 * @param str Input string to be checked.
	 * @return Judgment.
	 */
	private static boolean isPal(String str) {
		if (str == "")
			return true;
		//System.out.println(str);
		
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
	 * (PHASED OUT, DO NOT USE)
	 * Predicate whether several strings combined are a palindrome.
	 * 
	 * @param from Index of the first word.
	 * @param to Index of the last word.
	 * @return Judgment.
	 */
	private static boolean isPal(ArrayList<String> words, int from, int to) {
		final int N = words.size();
		int totalSize = 0; 
		
		assert(from >=  0 && from < N && from <= to && to < N);
		
		// count the total size of strings
		for (int i = from; i <= to; i++)
			totalSize += words.get(i).length();
		
		// compare characters
		int start = 0;
		int end = words.get(to).length() - 1;
		int currentFrom = from, currentTo = to;
		for (int i = totalSize >> 1; i > 0; i--) {
			// check whether we should move to next word
			if (start >= words.get(currentFrom).length()) {
				start = 0;
				currentFrom++;
			}
			if (end < 0) {
				end = words.get(--currentTo).length() - 1;
			}
			assert(currentFrom <= currentTo);
			
			// do compare
			if (words.get(currentFrom).charAt(start++)
					!= words.get(currentTo).charAt(end--)) 
				return false;
		}
			
		return true;
	}
	
	/**
	 * Runs the 2-gram counter. The input should be the path to a text file.
	 * 
	 * @param args The first element should contain the path to a text file.
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		//File file = new File(args[0]);
		File file = new File("/Users/liqiangw/Test/HP2.txt");
		ArrayList<String> words = Utilities.tokenizeFile(file);
		List<Frequency> frequencies = computePalindromeFrequencies(words);
		Utilities.printFrequencies(frequencies);
	}
}
