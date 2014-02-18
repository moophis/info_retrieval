package indexReader;

import Strucutre.TF_IDF_Positions;

import java.util.HashMap;

public class InverseIndex {
	private static InverseIndex uniqueInstance;
	private InverseIndex() {}
	public static InverseIndex getInstance() {
		if (uniqueInstance == null) {
			uniqueInstance = new InverseIndex();
		}
		return uniqueInstance;
	}

    public void write2Disk(String filePath) {

    }
    public void readFromDisk(String filePath) {

    }

    public void setInverseIndex(HashMap<String, HashMap<String, TF_IDF_Positions> > result) {
        dictionary = result;
    }

    private HashMap<String, HashMap<String, TF_IDF_Positions> > dictionary;

}
