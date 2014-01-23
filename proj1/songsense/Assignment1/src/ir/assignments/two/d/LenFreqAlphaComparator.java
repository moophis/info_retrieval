package ir.assignments.two.d;

import ir.assignments.two.a.*;
import java.util.Comparator;

public class LenFreqAlphaComparator implements Comparator<Frequency>{
    @Override
    public int compare(Frequency f1, Frequency f2) {
    	if (f1.getText().replaceAll("\\W", "").length() > f2.getText().replaceAll("\\W", "").length()) {
    		return -1;
    	} else if (f1.getText().replaceAll("\\W", "").length() < f2.getText().replaceAll("\\W", "").length()) {
    		return 1;
    	} else if (f1.getFrequency() > f2.getFrequency()) {
    		return -1;
    	} else if(f1.getFrequency() < f2.getFrequency()) {
    		return 1;
    	} else {
    		return f1.getText().compareTo(f2.getText());
    	}
    }
}
