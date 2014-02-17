package indexbuilder;

import Strucutre.WordPagePosition;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

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
    
    private void splitTest(String filePath) throws IOException {
    	RandomAccessFile raf = null;
    	
    	try {
			raf = new RandomAccessFile(filePath, "r");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    	
    	String line;
    	while (!(line = raf.readLine()).isEmpty()) {
    		
    	}
    }
    
    /** test */
    public static void main(String args[]) {
    	System.out.println("In SplitDocuments test ...");
    	File input = new File("/Users/liqiangw/Test/IR/test/testHtml.txt");
    	Document doc;
    	
		try {
			doc = Jsoup.parse(input, "UTF-8");
			System.out.println(doc.text());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
