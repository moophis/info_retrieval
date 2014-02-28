package indexReader;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;

import structure.HtmlFileInfo;

public class Doc2MD5 {
	private static Doc2MD5 uniqueInstance;

    private Doc2MD5() {}
    public static Doc2MD5 getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new Doc2MD5();
        }
        return uniqueInstance;
    }

    private HashMap<String, HtmlFileInfo> dictionary = new HashMap<>();
    /*      HtmlFileInfo
            public String MD5 = "";
            public long startPos = 0;
            public long htmlLen = 0;
            public String fileName = "";
     */

    public void printNumberDocuments() {
        System.out.println("Number of documents: " + Integer.toString(dictionary.size()));
    }

    public void write2Disk(String filePath) {
        FileWriter fw;
        try {
            fw = new FileWriter(filePath, false);

            for (Map.Entry<String, HtmlFileInfo> m : dictionary.entrySet()) {
                String buf = new StringBuilder().append(m.getKey()).append("$").append(m.getValue().MD5).append("$").append(m.getValue().startPos).append("$").append(m.getValue().htmlLen).append("$").append(m.getValue().fileName).toString();
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
            HtmlFileInfo htmlFileInfo = new HtmlFileInfo();
            String url = "";
            while ((line = reader.readLine()) != null) {
                url = readLine2HtmlFileInfo(line, htmlFileInfo);
                dictionary.put(url, htmlFileInfo);
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("Error process " + filePath);
            e.printStackTrace();
        }
    }

    private String readLine2HtmlFileInfo(String line, HtmlFileInfo htmlFileInfo) {
        if (line == null) {
            return null;
        }
        String[] strings = line.split("\\$");
        htmlFileInfo.MD5 = strings[1];
        htmlFileInfo.startPos = Long.parseLong(strings[2]);
        htmlFileInfo.htmlLen = Long.parseLong(strings[3]);
        htmlFileInfo.fileName = strings[4];
        return strings[0];
    }

    public boolean getHtmlFileInfo(String URL, HtmlFileInfo htmlFileInfo) {
        if(dictionary.containsKey(URL)) {
            htmlFileInfo.fileName = dictionary.get(URL).fileName;
            htmlFileInfo.htmlLen = dictionary.get(URL).htmlLen;
            htmlFileInfo.startPos = dictionary.get(URL).startPos;
            htmlFileInfo.MD5 = dictionary.get(URL).MD5;
            return true;
        }
        return false;
    }
    public void putHtmlFileInfo(String URL,
                                long startPos, long htmlLen, String fileName) {
        HtmlFileInfo htmlFileInfo = new HtmlFileInfo();
        htmlFileInfo.MD5 = DigestUtils.md5Hex(URL);
        htmlFileInfo.startPos = startPos;
        htmlFileInfo.htmlLen = htmlLen;
        htmlFileInfo.fileName = fileName;
        dictionary.put(URL, htmlFileInfo);
    }
    public int size() {
        return dictionary.size();
    }
    public void clear() {
        dictionary.clear();
    }
}

/*
/// MD5,
/// relative postion in the HTML
/// length of the HTML
/// raw file name
public class HtmlFileInfo {
    public String MD5 = "";
    public int startPos = 0;
    public int htmlLen = 0;
    public String fileName = "";
}
 */


