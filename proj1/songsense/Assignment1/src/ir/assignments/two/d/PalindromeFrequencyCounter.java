package ir.assignments.two.d;

import ir.assignments.two.a.Frequency;
import ir.assignments.two.a.Utilities;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.HashMap;
import java.util.Map.Entry;
import java.lang.StringBuilder;
import java.util.HashSet;

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

	private static List<Frequency> computePalindromeFrequenciesSlow(ArrayList<String> words) {
		// TODO Write body!
		// You will likely want to create helper methods / classes to help implement this functionality
		List<Frequency> freqs = new ArrayList<Frequency>();
		if (words == null || words.size() == 0) {
			return freqs;
		}
		// compute the frequency for each word
		HashMap<String, Integer> dictFreqs = new HashMap<String, Integer>();
		for (int i = 0; i < words.size(); ++i) {
			String word = "";
			for (int j = i; j < words.size(); ++j) {
				word += " " + words.get(j);
				word = word.trim();
				if (PalindromeFrequencyCounter.isPalindrome(word)) {					
					int freq = dictFreqs.containsKey(word) ? dictFreqs.get(word) : 0;
					dictFreqs.put(word, freq+1);
				}
			}
		}
		// insert the words with their frequencies to the list to be returned
		for (Entry<String, Integer> entry: dictFreqs.entrySet()) {
			freqs.add(new Frequency(entry.getKey(), entry.getValue()));
		}
		// sort the collection
		Collections.sort(freqs, new LenFreqAlphaComparator()); 
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
	private static List<Frequency> computePalindromeFrequencies(ArrayList<String> words) {
		// TODO Write body!
		// You will likely want to create helper methods / classes to help implement this functionality
		List<Frequency> freqs = new ArrayList<Frequency>();
		if (words == null || words.size() == 0) {
			return freqs;
		}
		HashMap<Integer,Integer> isWordStartPos = new HashMap<Integer, Integer>();
		HashSet<Integer> isWordEndPos = new HashSet<Integer>();
		// concatenate strings together for Manachers algorithm
		String oneWord = concatenateStrings(words, isWordStartPos, isWordEndPos);
		// System.out.println(isWordStartPos);
		// System.out.println(isWordEndPos);
		// compute the frequency for each word
		HashMap<String, Integer> dictFreqs = 
				computePalindromeTable(oneWord, isWordStartPos, isWordEndPos);
		// insert the words with their frequencies to the list to be returned
		for (Entry<String, Integer> entry: dictFreqs.entrySet()) {
			freqs.add(new Frequency(entry.getKey(), entry.getValue()));
		}
		// sort the collection
		Collections.sort(freqs, new LenFreqAlphaComparator()); 
		return freqs;
	}
	private static HashMap<String, Integer> computePalindromeTable(String word, 
			HashMap<Integer, Integer> isWordStartPos, HashSet<Integer> isWordEndPos) {
		String t = preProcess(word);
		int C = 0, R = 0, n = t.length();
		int[] P = new int[n];
		for (int i = 1 ; i < n-1; ++i) {
			int i_mirror = 2*C - i;
			P[i] = (R > i) ? Math.min(R-i, P[i_mirror]) : 0;
			
			while (t.charAt(i + 1 + P[i]) == t.charAt(i - 1 - P[i]))
				++P[i];
			
			if (i + P[i] > R) {
				C = i;
				R = i + P[i];
			}			
		}
		HashMap<String, Integer> dictFreqs = new HashMap<String, Integer>();
		
		for (int i = 0; i < n; ++i) {
			if (P[i] == 0)
				continue;
			int index = (i - 1 - P[i]) / 2;
			int len = P[i];
			int center = index + len / 2;
			for (int startIndex = index, curLen = len; startIndex <= center; ++startIndex, curLen-=2) {
				// exclude some palindrome that is not a word in the list
				if (!isWordStartPos.containsKey(startIndex) || !isWordEndPos.contains(startIndex + curLen)) 
					continue;
				String palidromeWord = new String();
				for (int start = startIndex; start < startIndex + curLen; ) {
					int end = isWordStartPos.get(start);
					palidromeWord = palidromeWord.concat(word.substring(start, start + end));
					palidromeWord = palidromeWord.concat(" ");
					start += end;
				}
				palidromeWord = palidromeWord.trim();
				if (palidromeWord.length() == 0)
					continue;
				// insert the hit palindrome into the map
				int freq = dictFreqs.containsKey(palidromeWord) ? dictFreqs.get(palidromeWord) : 0;
				dictFreqs.put(palidromeWord, freq + 1);
			}
		}
		
		return dictFreqs;
		
	}
	private static String preProcess(String s) {
		StringBuilder t = new StringBuilder();
		t.append("^");
		for (int i = 0; i < s.length(); i++) {
			t.append("#");
			t.append(s.charAt(i));
		}
		t.append("#");
		t.append("$");
		return t.toString();
	}
	
	private static String concatenateStrings(ArrayList<String> words, 
			HashMap<Integer, Integer> isWordStartPos, HashSet<Integer> isWordEndPos) {
		StringBuilder stringbuilder = new StringBuilder();
		int pos = 0;
		for (String word : words) {
			stringbuilder.append(word);
			isWordStartPos.put(pos, word.length());
			pos += word.length();
			isWordEndPos.add(pos);
		}
		return stringbuilder.toString();
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
