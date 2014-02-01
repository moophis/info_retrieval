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
	 * Extract the URL from an "info" line.  
	 */
	private static String getURL(String line) {
		if (line == null) 
			return line;
		
		String[] strings = line.split(":");
		
		return "http:" + strings[4]; 
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
				String subdomain = getSubdomain(getURL(line));
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
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			subdomainsStats();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
