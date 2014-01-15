package ir.assignments.two.b;

import ir.assignments.two.a.Frequency;

import java.util.Comparator;

/**
 * Comparator implementation of Frequency class 
 */
public class FrequencyComparator implements Comparator<Frequency> {
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
		if (type == 0) {	// sort frequency
			return f1.getFrequency() > f2.getFrequency() ?
					-1 : f1.getFrequency() == f2.getFrequency() ? 0 : 1;
		} else {	// sort string
			return f1.getText().compareTo(f2.getText());
		}
	}
}