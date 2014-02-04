package crawler;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.codec.digest.DigestUtils;

public class CrawlerSW extends WebCrawler{
	private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g" 
            + "|png|tiff?|mid|mp2|mp3|mp4|xml|wmf"
            + "|wav|avi|mov|mpeg|ram|m4v|pdf|csv" 
            + "|txt|cpp|c|h|cc|java|py|m|class|o|tmp"
            + "|perl|pl|vb|r|q|s|asm|rb|pas|bak|sh|awk|sed"
            + "|rm|smil|wmv|swf|wma|zip|rar|gz|jar))$");

	/**
	* You should implement this function to specify whether
	* the given url should be crawled or not (based on your
	* crawling logic).
	*/
	@Override
	public boolean shouldVisit(WebURL url) {
		String href = url.getURL().toLowerCase();
		if (FILTERS.matcher(href).matches()) {
			return false;
		}
		if (!href.contains(".ics.uci.edu") 
				&& !href.contains("/ics.uci.edu")) {
			return false;
		}
		return specialPolicy(href);
	}
	
	 /**
	 * Put any policies specifically to UC Irvine ICS site. 
	 */
	private boolean specialPolicy(String href) {
		// only parse calendar events from 2006 ~ 2014
		if (href.contains("calendar.ics.uci.edu") && href.contains("year=")) {
			int pos = href.lastIndexOf("year=");
			String year = href.substring(pos+4, href.length());
			int numYear = Integer.parseInt(year);
			if (numYear > 2014 || numYear < 2006) {
				return false;
			}
		} 
		if (href.contains("drzaius.ics.uci.edu/cgi-bin/")) {
			return false;
		}
		if (href.contains("fano.ics.uci.edu")) {
			return false;
		}
		if (href.contains("djp3-pc2.ics.uci.edu")) {
			return false;
		}
		// ignore machine learning dynamic pages
		if (href.contains("http://archive.ics.uci.edu/ml/datasets.html?")) {
			return false;
		}
		// ignore physics.ics.uci.edu
		if (href.contains("physics.uci.edu")) {
			return false;
		}
		// ignore the any other dynamic page
//		if(!href.contains("calendar.ics.uci.edu") && href.contains("?")) {
//			return false;
//		}
	
		// ignore the machine learning dataset
		if (href.contains("machine-learning-databases")) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * Generate MD5 value of a file content.
	 * 
	 * @param fileContent - Content of a file.
	 * @return MD5 value.
	 */
	private String getMD5(String fileContent) {
		if (fileContent == "")
			return null;
		
		return DigestUtils.md5Hex(fileContent);
	}

   /**
	* This function is called when a page is fetched and ready 
	* to be processed by your program.
	*/
	@Override
	public synchronized void visit(Page page) {   
		Long threadId = Thread.currentThread().getId();
		String url = page.getWebURL().getURL();
		
		System.out.println("URL: " + url + " ThreadID: " + threadId);
		
		if (page.getParseData() instanceof HtmlParseData) {
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			String text = htmlParseData.getText();
//			String html = htmlParseData.getHtml();
			List<WebURL> links = htmlParseData.getOutgoingUrls();
			
			Integer size = links.size();
			
			/*
			 * Pages fetched from different crawler threads are 
			 * stored separately. 
			 */
			long offset = -1;
			try {
				offset = DatabaseBuilder.writePageContent(threadId, text);
			} catch (IOException e) {
				e.printStackTrace();
			}
			String meta = null;
			if (offset != -1) {
				meta = DatabaseBuilder.buildIndexLine(getMD5(text), offset, size, url);
				DatabaseBuilder.writePageIndex(threadId, meta);
			}
			
//			StringToFile.toFile(url, Controller.crawlStorageFolder + "data/info/" 
//					+ fileName + ".txt");
//			StringToFile.toFile(size.toString(), Controller.crawlStorageFolder + "data/info/" 
//					+ fileName + ".txt");
//			StringToFile.toFile(text, Controller.crawlStorageFolder + "data/text/"
//					+ fileName + ".txt");

//			System.out.println("Text length: " + text.length());
//			System.out.println("Html length: " + html.length());
//			System.out.println("Number of outgoing links: " + links.size());
//			System.out.println("Fetch time: " + currentTime);
		}
	}
}
