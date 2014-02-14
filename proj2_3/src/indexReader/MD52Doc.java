package indexReader;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

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


    public void write2Disk(String filePath) {
        FileWriter fw;
        try {
            fw = new FileWriter(filePath, false);

            for (Map.Entry<String, String> m : dictionary.entrySet()) {
                String buf = new StringBuilder().append(m.getKey()).append("$").append(m.getValue()).toString();
                fw.write(buf + "\n");
            }
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void readFromDisk(String filePath) {
        File f = new File(filePath);
        BufferedReader reader;
        dictionary.clear();

        try {
            reader = new BufferedReader(new FileReader(f));
            String line;
            String md5 = "";
            String url = "";
            while ((line = reader.readLine()) != null) {
                String[] strings = line.split("\\$");
                md5 = strings[0];
                url = strings[1];
                dictionary.put(md5, url);
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("Error process " + filePath);
            e.printStackTrace();
        }
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
