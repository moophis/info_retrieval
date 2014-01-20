package ir.assignments.two.b;

import ir.assignments.two.a.Frequency;

import java.util.Comparator;

/**
 * Comparator implementation of Frequency class 
 */
public class FrequencyComparator implements Comparator<Frequency> {
	public static final FrequencyComparator FREQ = 
			new FrequencyComparator(0);
	public static final FrequencyComparator TXT_ALNUM = 
			new FrequencyComparator(1);
	public static final FrequencyComparator TXT_WORDSIZE = 
			new FrequencyComparator(2);
	private int type = 0;
	
	private FrequencyComparator(int type) {
		this.type = type;
	}
	
	@Override
	public int compare(Frequency f1, Frequency f2) {
		if (type == 0) {	// sort frequency
			return f1.getFrequency() > f2.getFrequency() ?
					-1 : f1.getFrequency() == f2.getFrequency() ? 0 : 1;
		} else if (type == 1) {	// sort string alphabetically
			return f1.getText().compareTo(f2.getText());
		} else {  // sort by word counts
			return wordLength(f2) - wordLength(f1);
		}
	}
	
	private int wordLength(Frequency f) {
		if (f == null)
			return 0;
		
		String str = f.getText();
		int len = str.length();
		int wordcnt = 0;
		
		for (int i = 0; i < len; i++) {
			if (str.charAt(i) != ' ') 
				wordcnt++;
		}
		
		return wordcnt;
	}
}