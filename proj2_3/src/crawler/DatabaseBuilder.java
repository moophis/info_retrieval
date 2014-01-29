package crawler;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;

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
		Long threadId = (long) 0;
		
		// write a-z 10 times
		String text = "abcdefghijklmnopqrstuvwxyz";
		int times = 10;
		
		// begin write page into text and info file
		long offset = -1;
		for (int i = 0; i < times; ++i) {
			try {
				offset = DatabaseBuilder.writePageContent(threadId, text);
			} catch (IOException e) {
				e.printStackTrace();
			}
			String meta = null;
			if (offset != -1) {
				meta = DatabaseBuilder.buildIndexLine(DigestUtils.md5Hex(text), offset, 0, "This is an URL");
				DatabaseBuilder.writePageIndex(threadId, meta);
			}
		}
		
		// begin read the index
		List<Integer> positions = new ArrayList<Integer>();
		String fileName = "thread0.txt";
		String filePath = Controller.crawlStorageFolder + "data/info/";
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(filePath + fileName));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return;
		} 
		String line = "abc";
		try {
			while (line != null) {
				line = br.readLine();
				if (line != null) {
					String[] strs = line.split(":");
					positions.add(Integer.parseInt(strs[1]));
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			br.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// get the tail of the file 
		RandomAccessFile raf = null;
		filePath = Controller.crawlStorageFolder + "data/text/";
		try {
			raf = new RandomAccessFile(filePath+fileName, "r");
			int post = (int) raf.length();
			positions.add(post);
		} catch (FileNotFoundException e) {
			System.err.println("Index file for " + threadId.toString() + " does not exist."
					          + " Now create one...");	
		} catch (IOException e) {
			// TODO Auto-generated catch block 
			e.printStackTrace();
		}
		try {
			raf.close();
		} catch (IOException e) {
			// TODO TAuto-generated catch block 
			e.printStackTrace();
		}
		
		System.out.println("Print the positions");
		for (Integer item : positions) {
			System.out.println(item);
		}
		
		// begin read the text file
		filePath = Controller.crawlStorageFolder + "data/text/";
		try {
			raf = new RandomAccessFile(filePath+fileName, "r");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// print the length of the file
		try {
			System.out.println(fileName + "'s size is " + Integer.toString((int) raf.length()));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// read file
		try {
			for (int i = 0; i < positions.size()-1; ++i) {
				int beg = positions.get(i);
				int size = positions.get(i+1) - positions.get(i);
				byte[] b = new byte[size*10];
				raf.seek(beg);
				raf.read(b, 0, size);
				System.out.println(beg);
				System.out.println(new String(b));
			}			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// close the file
		try {
			raf.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
