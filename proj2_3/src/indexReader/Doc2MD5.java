package indexReader;

import java.util.HashMap;

import Strucutre.HtmlFileInfo;
import org.apache.commons.codec.digest.DigestUtils;

public class Doc2MD5 {
	private static Doc2MD5 uniqueInstance;

    private Doc2MD5() {}
    public static Doc2MD5 getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new Doc2MD5();
        }
        return uniqueInstance;
    }

    private HashMap<String, HtmlFileInfo> dictionary = new HashMap();

    public void write2Disk() {

    }
    public void readFromDisk() {

    }

    public boolean getHtmlFileInfo(String URL, HtmlFileInfo htmlFileInfo) {
        if(dictionary.containsKey(URL)) {
            htmlFileInfo = dictionary.get(URL);
            return true;
        }
        return false;
    }
    public void putHtmlFileInfo(String URL,
                                int startPos, int htmlLen, String fileName) {
        HtmlFileInfo htmlFileInfo = new HtmlFileInfo();
        htmlFileInfo.MD5 = DigestUtils.md5Hex(URL);
        htmlFileInfo.startPos = startPos;
        htmlFileInfo.htmlLen = htmlLen;
        htmlFileInfo.fileName = fileName;
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


