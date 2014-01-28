package crawler;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class DatabaseBuilder {
	/**
	 * Assemble a record line for file index.
	 * MD5:byte offset in data.db:outdegree:URL 
	 */
	public static String buildIndexLine(String md5, long offset, int degree, String url) {
		Long l = new Long(offset);
		Integer i = new Integer(degree);
		
		return md5 + ":" + l.toString() + ":" + i.toString() + ":" + url;
	}
	
	/**
	 * Append the page content to database categorized by thread id. 
	 * (So that we can avoid the scenario that multiple threads manipulate
	 * the same database file)
	 * 
	 * @param threadId - id of the thread intending to take down new page.
	 * @param text - content of the page.
	 * 
	 * @return the beginning byte position of this page content.
	 * 			On error return -1;
	 * @throws IOException - may occur from RandomAccessFile.
	 */
	public static long writePageContent(Long threadId, String text) throws IOException {
		long pos = -1;
		
		RandomAccessFile raf = null;
		String filePath = Controller.crawlStorageFolder + "data/text/"
							+ "thread" + threadId.toString() + ".txt";
		
		try {
			raf = new RandomAccessFile(filePath, "rws");
			pos = raf.length();
		} catch (FileNotFoundException e) {
			System.err.println("Index file for " + threadId.toString() + " does not exist."
					          + " Now create one...");
			/*
			 * Since a new file will be create in the *finally* block,
			 * the beginning position of this page should be 0. 
			 */
			pos = 0;	
		} finally {
			StringToFile.toFile(text, filePath);
			raf.close();
		}
		
		return pos;
	}
	
	/**
	 * Append the index of a page to index files (also categorized 
	 * by thread id). 
	 *  
	 * @param threadId - id of the thread intending to take down new page.
	 * @param metadata - the navigational string for a page in the database
	 *                   (assembled by buildIndexLine())
	 */
	public static void writePageIndex(Long threadId, String metadata) {
		String filePath = Controller.crawlStorageFolder + "data/info/"
							+ "thread" + threadId.toString() + ".txt";

		StringToFile.toFile(metadata, filePath);
	}
	
	// TODO: write test cases.
	public static void main(String[] args) {
		
	}
}
