package indexbuilder;

import Strucutre.WordPagePosition;
import crawler.StringToFile;
import crawler.Stats;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import org.apache.commons.codec.digest.DigestUtils;

import org.jsoup.nodes.*;
import org.jsoup.Jsoup;

/**
 * Created by soushimei on 2/13/14.
 * 
 * For the file name, we assume that they are "thread13.txt"
 * to "thread19.txt".
 */
public class SplitDocuments {
    private final String tempFolderPath;
    private final String rawHTMLFolderPath;
    private final String rawInfoFolderPath;
    private final String path;
    
    public SplitDocuments(String path,
                          String tempFolder,
                          String rawHTMLFolder,
                          String rawInfoFolder) {
    	this.path = path;
        tempFolderPath = this.path + tempFolder;
        rawHTMLFolderPath = this.path + rawHTMLFolder;
        rawInfoFolderPath = this.path + rawInfoFolder;
    }

    public void split(ArrayList<WordPagePosition> splitLists) {
    	String fileName = "thread";
    }
    
    /** test */
    private void splitTest(String filePath) throws IOException {
    	RandomAccessFile lineRead = null;
    	RandomAccessFile charRead = null;
    	
    	try {
			lineRead = new RandomAccessFile(filePath, "r");
			charRead = new RandomAccessFile(filePath, "r");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    	
    	String line;
    	long pos = 0, linePos = 0;;
    	int lineNo = 0;
    	while (!(line = lineRead.readLine()).isEmpty()) {
    		String[] buf = line.toLowerCase().split("[^0-9A-Za-z'_-]+");

    		lineNo++;
    		for (String s : buf) {
    			System.out.println("Line " + lineNo + ": word: " + s + ", beginning at " + pos);
    			int wordLen = s.length();
    			charRead.seek(pos + linePos);
    			
    			System.out.print("-->Read char by char, word: ");
    			for (int i = 0; i < wordLen; i++) {
    				System.out.print(charRead.readChar());
    			}
    			System.out.print('\n');
    			
    			linePos += wordLen;
    			for (int i = wordLen; i < line.length() 
    					|| !Character.isLetterOrDigit(line.charAt(i)); i++)
    				linePos++;
    		}
    		
    		pos += linePos + 1; // '\n'. XXX: do not know if there is a 'r'.
    		linePos = 0;
    	}
    }
    
    public static void htmlToText() throws IOException {
    	for (Integer i = 13; i <= 19; i++) {
    		String pureTextPath = "/Users/liqiangw/Test/IR/pure/text/thread" 
    				+ i.toString() + ".txt";
    		String pureInfoPath = "/Users/liqiangw/Test/IR/pure/info/thread" 
					+ i.toString() + ".txt";
    		String htmlTextPath = "/Users/liqiangw/Test/IR/raw/text/thread" 
					+ i.toString() + ".txt";
    		String htmlInfoPath = "/Users/liqiangw/Test/IR/raw/info/thread" 
					+ i.toString() + ".txt";
    		
    		File htmlTextFile = new File(htmlTextPath);
    		RandomAccessFile htmlInfoReader = null, htmlTextReader = null;
    		
    		try {
                htmlInfoReader = new RandomAccessFile(htmlInfoPath, "r");
                htmlTextReader = new RandomAccessFile(htmlTextPath, "r");
                System.out.println(htmlInfoReader.toString());
                System.out.println(htmlTextReader.toString());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
    		
    		String line, url;
            long curHtmlPos = 0, nextHtmlPos = 0, htmlLen = 0;
            long curPurePos = 0;
            Document doc;
            
            line = htmlInfoReader.readLine(); // first line
            System.out.println(line);
            url = Stats.getPageInfo(line, "url");
            StringToFile.toFile(buildPureIndexLine(url, curPurePos), pureInfoPath);
            while ((line = htmlInfoReader.readLine()) != null) {
                nextHtmlPos = Long.parseLong(Stats.getPageInfo(line, "position"));
                htmlLen = nextHtmlPos - curHtmlPos;
                
                byte[] b = new byte[(int) htmlLen];
                System.out.println("curHtmlPos = " + curHtmlPos + ", htmlLen = " + htmlLen);
                htmlTextReader.read(b, 0, (int) htmlLen);
                String buf = new String(b);
                doc = Jsoup.parse(buf);
                StringToFile.toFile(doc.text(), pureTextPath);
                curPurePos += doc.text().length();
                
                curHtmlPos = nextHtmlPos;
                url = Stats.getPageInfo(line, "url");
                StringToFile.toFile(buildPureIndexLine(url, curPurePos), pureInfoPath);
            }
            // deal with the last line
            htmlLen = htmlTextFile.length() - curHtmlPos;
            byte[] b = new byte[(int) htmlLen];
            htmlTextReader.read(b, 0, (int) htmlLen);
            String buf = new String(b);
            StringToFile.toFile(buf, pureTextPath);
            
            htmlInfoReader.close();
            htmlTextReader.close();
    	}
    }
    
    private static String buildPureIndexLine(String url, Long pos) {
    	return DigestUtils.md5Hex(url) + ":" + pos.toString();
    }
    
    /**
     * @deprecated 
     */
    public static void htmlToTextOld() {
    	for (Integer i = 13; i <= 19; i++) {
    		String purePath = "/Users/liqiangw/Test/IR/raw/pure/thread" 
    						+ i.toString() + ".txt";
    		String htmlPath = "/Users/liqiangw/Test/IR/raw/text/thread" 
					+ i.toString() + ".txt";
    		
    		File input = new File(htmlPath);
        	Document doc;
        	
    		try {
    			doc = Jsoup.parse(input, "UTF-8");
    			StringToFile.toFile(doc.text(), purePath);
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    	}
    }
    
    /** test 
     * @throws IOException */
    public static void main(String args[]) throws IOException {
    	System.out.println("In SplitDocuments test ...");
    	htmlToText();
    }
}
