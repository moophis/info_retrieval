package parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import crawler.Controller;
import crawler.StringToFile;
import ir.assignments.two.a.*;
import ir.assignments.two.b.*;

/**
 * Do some statistics about the web pages.
 * XXX: You should import codes from the first assignment!  
 */
public class Stats {
	/**
	 * Get the sub-domain from a valid URL.
	 */
	private static String getSubdomain(String url) {
		if (!url.contains("ics.uci.edu")) 
			return null;
		
		String subdomain = "http://";
		String[] strings = url.split("/");
		
		/*
		 * Here we ignore the error checking procedure
		 * just for simplicity. 
		 */
		subdomain += strings[2];
		
		return subdomain;
	}
	
	/**
	 * Extract the page information from an "info" line.  
	 */
	private static String getPageInfo(String line, String type) {
		if (line == null) 
			return line;
		
		String[] strings = line.split(":");
		
		switch (type) {
		case "md5": // (string)
			return strings[0];
		case "position": // starting position (long)
			return strings[1];
		case "out-degree": // (int)
			return strings[2];
		case "url": // (string)
			return "http:" + strings[4]; 
		default:
			System.err.println("Invalid parameter!");
		}
		
		return null;
	}
	
	
	/**
	 * Print sub domain frequencies to console and (or) a file.
	 */
	public static void subdomainsStats() throws IOException {
		List<Frequency> list = null;
		List<String> urls = new ArrayList<String>();
		for (Integer i = 13; i <= 19; i++) {
			String path = Controller.crawlStorageFolder + "data/info/thread"
					+ i.toString() + ".txt";
			File f = new File(path);
			BufferedReader reader = null;
			
			try {
				reader = new BufferedReader(new FileReader(f));
			} catch (FileNotFoundException e) {
				System.err.println("Cannot find info file for thread " + i);
				e.printStackTrace();
			}
			
			String line;
			while ((line = reader.readLine()) != null) {
				String subdomain = getSubdomain(getPageInfo(line, "url"));
				urls.add(subdomain);
			}
			reader.close();
			
			list = WordFrequencyCounter.computeWordFrequencies(urls);
			for (Frequency fr : list) {
				StringToFile.toFile(fr.toString(), 
						Controller.crawlStorageFolder + "data/info/Subdomains.txt");
				System.out.println(fr);
			}
		}
		
	}
	
	/**
	 * Find longest page in terms of number of words.  
	 * @throws IOException 
	 */
	public static void findLongestPage() throws IOException {
		long maxlen = 0l;
		String longestPage = null;
		
		for (Integer i = 13; i <= 19; i++) {
			String infPath = Controller.crawlStorageFolder + "data/info/thread"
					+ i.toString() + ".txt";
			String txtPath = Controller.crawlStorageFolder + "data/text/thread"
					+ i.toString() + ".txt";
			File infoFile = new File(infPath);
			File textFile = new File(txtPath);
			BufferedReader reader = null;
			
			System.out.println("***Info " + i + "***");
			
			try {
				reader = new BufferedReader(new FileReader(infoFile));
			} catch (FileNotFoundException e) {
				System.err.println("Cannot find info file for thread " + i);
				e.printStackTrace();
			}
			
			/*
			 * Should really care about the first line and the last line of 
			 * the info files. 
			 */
			String line, url;
			long curPos = 0, nextPos = 0, len;
			
			line = reader.readLine(); // first line
			url = getPageInfo(line, "url");
			while ((line = reader.readLine()) != null) {
				nextPos = Long.parseLong(getPageInfo(line, "position"));
				len = nextPos - curPos;
				
				if (len <= 10)
					System.out.println(url + ":" + len);
				
				if (len > maxlen) {
					maxlen = len;
					longestPage = url;
				}
				
				curPos = nextPos;
				url = getPageInfo(line, "url");
			}
			// deal with the last line
			len = textFile.length() - curPos;
			System.out.println("file len: " + textFile.length());
			if (len <= 10)
				System.out.println(url + ":" + len);
			if (len > maxlen) {
				maxlen = len;
				longestPage = url;
			}
			
			reader.close();
		}
		
		System.out.println("--------------------------------");
		System.out.println("Longest page: " + longestPage);
		System.out.println("Max length: " + maxlen);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		String s = "493c2dc36f30bacdbe87155c478aedaf:282741:137:http://www.ics.uci.edu/ugrad/resources/";
//		System.out.println(getPageInfo(s, "url"));
//		try {
//			subdomainsStats();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		try {
			findLongestPage();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
