package indexReader;

import java.util.HashMap;

public class MD52Doc {
	private static MD52Doc uniqueInstance;
	private MD52Doc() {}
	public static MD52Doc getInstance() {
		if (uniqueInstance == null) {
			uniqueInstance = new MD52Doc();
		}
		return uniqueInstance;
	}
    // given MD5, get the URL
    private HashMap<String, String> dictionary = new HashMap<>();


    public void write2Disk() {

    }
    public void readFromDisk() {

    }

    public boolean getURL(String MD5, String URL) {
        if (dictionary.containsKey(MD5)) {
            URL = dictionary.get(MD5);
            return true;
        }
        else {
            return false;
        }
    }
    public void setURL(String MD5, String URL) {
        dictionary.put(MD5, URL);
    }
    public int size() {
        return dictionary.size();
    }
    public void clear() {
        dictionary.clear();
    }
}
