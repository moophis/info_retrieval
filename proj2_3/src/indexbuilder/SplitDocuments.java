package indexbuilder;

import Strucutre.PagePosition;
import Strucutre.TF_IDF_Positions;
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
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

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
    private final String pureInfoFolderPath;
    private final String pureTextFolderPath;
    private final String path;
    
    private final static Pattern FILTERS = Pattern.compile(Stats.stopWords);
    
    public SplitDocuments(String path,
                          String tempFolder,
                          String rawHTMLFolder,
                          String rawInfoFolder) {
    	this.path = path;
        tempFolderPath = this.path + tempFolder;
        rawHTMLFolderPath = this.path + rawHTMLFolder;
        rawInfoFolderPath = this.path + rawInfoFolder;
        pureInfoFolderPath = this.path + "/pure/info";
        pureTextFolderPath = this.path + "/pure/text";
    }

    private PorterStemmer porterStemmer = new PorterStemmer();

    /**
     * Extract each word from pure text database, find relative position in 
     * a document and retrieve its URL (represented as MD5 value). 
     */
    public void splitAndMerge(HashMap<String, HashMap<String, TF_IDF_Positions> > mergeSecondPhaseMap) throws IOException {
    	RandomAccessFile infoFile = null;
    	RandomAccessFile textFile = null;
    	
    	for (Integer t = 13; t <= 19; t++) {
    		String pureInfoPath = pureInfoFolderPath + "/thread" + t.toString() + ".txt";
    		String pureTextPath = pureTextFolderPath + "/thread" + t.toString() + ".txt";
    		try {
    			infoFile = new RandomAccessFile(pureInfoPath, "r");
    			textFile = new RandomAccessFile(pureTextPath, "r");
    		} catch (FileNotFoundException e) {
    			e.printStackTrace();
    		}
    		
    		String line;
        	int pos = 0, linePos = 0;
        	
        	String adjInfo = infoFile.readLine();
        	String nextInfo = infoFile.readLine();
        	int adjPos = Integer.parseInt(getPureIndexLine(adjInfo, "position"));
        	int nextPos = Integer.parseInt(getPureIndexLine(nextInfo, "position"));
        	
        	while ((line = textFile.readLine()) != null) {
        		String[] buf = line.toLowerCase().split("[^0-9A-Za-z]+");
        		
        		for (int i = 0; i < line.length() 
    					&& !Character.isLetterOrDigit(line.charAt(i)); i++)
    				linePos++;
        		for (String s : buf) {
        			int wordLen = s.length();
        			String word = line.substring(linePos, linePos + wordLen);
        			
        			int curPos = pos + linePos;
        			// Always maintain the condition: adjPos <= curPos < nextPos
        			while (curPos >= nextPos) {
        				adjPos = nextPos;
        				adjInfo = nextInfo;
        				nextInfo = infoFile.readLine();
                        if (nextInfo == null)
                            break;
        				nextPos = Integer.parseInt(getPureIndexLine(nextInfo, "position"));
        			}
        			assert(curPos >= adjPos);

                    word = porterStemmer.stem(word.toLowerCase());
        			if (!FILTERS.matcher(word).matches() && word != null && !word.isEmpty()) {
	        			int offset = curPos - adjPos;
	        			String adjMD5 = getPureIndexLine(adjInfo, "url-md5");

                        // merge
                        if (adjMD5 != null && !adjMD5.isEmpty()) {
                            if (!mergeSecondPhaseMap.containsKey(word)) {
                                mergeSecondPhaseMap.put(word, new HashMap<String, TF_IDF_Positions>());
                            }
                            HashMap<String, TF_IDF_Positions> tf_idf_positionsHashMap = mergeSecondPhaseMap.get(word);
                            if (!tf_idf_positionsHashMap.containsKey(adjMD5)) {
                                tf_idf_positionsHashMap.put(adjMD5,new TF_IDF_Positions());
                                tf_idf_positionsHashMap.get(adjMD5).tf_idf = 0.0;
                                tf_idf_positionsHashMap.get(adjMD5).positions = new ArrayList<>();
                            }
                            tf_idf_positionsHashMap.get(adjMD5).positions.add(offset);
                        }
        			}
        			
        			linePos += wordLen;
        			while (linePos < line.length() 
        					&& !Character.isLetterOrDigit(line.charAt(linePos)))
        				linePos++;
        		}
        		
        		pos += linePos + 1; 
        		linePos = 0;
        	}

    		
    		infoFile.close();
    		textFile.close();
    	}
    }
    
    /** test */
    private static void splitTest(String filePath) throws IOException {
    	RandomAccessFile lineRead = null;
    	
    	try {
			lineRead = new RandomAccessFile(filePath, "r");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    	
    	String line;
    	int pos = 0, linePos = 0;
    	int lineNo = 0;
    	while ((line = lineRead.readLine()) != null) {
    		String[] buf = line.toLowerCase().split("[^0-9A-Za-z]+");

    		lineNo++;
    		
    		for (int i = 0; i < line.length() 
					&& !Character.isLetterOrDigit(line.charAt(i)); i++)
				linePos++;
    		for (String s : buf) {
    			System.out.println("Line " + lineNo + ": word: " + s + ", beginning at "
    						+ pos + "+" + linePos);
    			int wordLen = s.length();
//    			byte[] bytes = new byte[wordLen];
//    			charRead.seek(pos + linePos);
//    			charRead.read(bytes, 0, wordLen);
//    			String buffer = new String(bytes);
    			String buffer = line.substring(linePos, linePos + wordLen);
    			
    			System.out.println("-->Read byte by byte, word: " + buffer);
//    			for (int i = 0; i < wordLen; i++) {
//    				System.out.print(charRead.readChar());
//    			}
//    			System.out.println(", @" + charRead.getFilePointer());
    			
    			linePos += wordLen;
    			while (linePos < line.length() 
    					&& !Character.isLetterOrDigit(line.charAt(linePos)))
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
    		String pureTitlePath = "/Users/liqiangw/Test/IR/pure/title/thread"
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
            url = Stats.getPageInfo(line, "url");
            StringToFile.toFile(buildPureIndexLine(url, curPurePos), pureInfoPath);
            while ((line = htmlInfoReader.readLine()) != null) {
                nextHtmlPos = Long.parseLong(Stats.getPageInfo(line, "position"));
                htmlLen = nextHtmlPos - curHtmlPos;
                
                byte[] b = new byte[(int) htmlLen];
                htmlTextReader.read(b, 0, (int) htmlLen);
                String buf = new String(b);
                
                doc = Jsoup.parse(buf);
                StringToFile.toFile(doc.text(), pureTextPath); // body text
                
                // store title
                StringToFile.toFile(buildPureTitleLine(url, doc.title()), pureTitlePath);
                
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
            
            doc = Jsoup.parse(buf);
            StringToFile.toFile(doc.text(), pureTextPath);
            
            // store title
            StringToFile.toFile(buildPureTitleLine(url, doc.title()), pureTitlePath);
            
            curPurePos += doc.text().length();

            htmlInfoReader.close();
            htmlTextReader.close();
    	}
    }
    
    private static String buildPureIndexLine(String url, Long pos) {
    	return DigestUtils.md5Hex(url) + ":" + pos.toString();
    }
    
    private static String buildPureTitleLine(String url, String title) {
    	System.out.println(title);
    	return DigestUtils.md5Hex(url) + title;
    }
    
    private String getPureIndexLine(String line, String type) {
        if (line == null || line == "")
            return null;
    	String[] strings = line.split(":");
    	
    	switch (type) {
    	case "url-md5":
    		return strings[0];
    	case "position":
    		return strings[1];
    	default:
    		System.err.println("In vaild input type!");
    	}
    	
    	return null;
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
//    	splitTest("/Users/liqiangw/Test/IR/pure/text/thread13.txt");
    	
//    	String testPath = "/Users/liqiangw/Test/IR/test/test2.txt";
//    	String testOutputPath = "/Users/liqiangw/Test/IR/test/test2out.txt";
//    	File input = new File(testPath);
//    	Document doc;
//		try {
//			doc = Jsoup.parse(input, "UTF-8");
//			StringToFile.toFile(doc.text(), testOutputPath);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
    }
}
