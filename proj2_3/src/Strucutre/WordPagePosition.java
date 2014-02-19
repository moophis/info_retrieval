package Strucutre;

/**
 * Created by soushimei on 2/13/14.
 */
public class WordPagePosition {
    public String word;
    public String urlMD5;
    public int position; // int should be OK in this project.
    
    public WordPagePosition(String word, String urlMD5, int position) {
    	this.word = word;
    	this.urlMD5 = urlMD5;
    	this.position = position;
    }
    
    public WordPagePosition() {
    	
    }
    
    public String toString() {
    	return word + ":" + urlMD5 + ":" + position;
    }
}
