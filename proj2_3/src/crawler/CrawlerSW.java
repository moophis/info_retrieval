package crawler;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

import java.util.List;
import java.util.regex.Pattern;

public class CrawlerSW extends WebCrawler{
	private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g" 
            + "|png|tiff?|mid|mp2|mp3|mp4"
            + "|wav|avi|mov|mpeg|ram|m4v|pdf" 
            + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

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
		if (!href.contains("ics.uci.edu")) {
			return false;
		}
		if (href.contains("calendar") && href.contains("year=")) {
			int pos = href.lastIndexOf("year=");
			String year = href.substring(pos+4, href.length());
			int numYear = Integer.parseInt(year);
			if (numYear > 2015 || numYear < 2004) {
				return false;
			}
		}
		return true;
	}

	/**
	* This function is called when a page is fetched and ready 
	* to be processed by your program.
	*/
	@Override
	public void visit(Page page) {          
		String url = page.getWebURL().getURL();
		System.out.println("URL: " + url);

		if (page.getParseData() instanceof HtmlParseData) {
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			String text = htmlParseData.getText();
			String html = htmlParseData.getHtml();
			List<WebURL> links = htmlParseData.getOutgoingUrls();
			
			/*
			 * Write fetched pages into files 
			 */
			Long currentTime = System.currentTimeMillis();
			StringToFile.toFile(text, Controller.crawlStorageFolder + "data/text/" , 
					currentTime.toString() + ".txt");
//			StringToFile.toFile(html, Controller.crawlStorageFolder + "data/html/", 
//					htmlParseData.getTitle() + ".html");

			System.out.println("Text length: " + text.length());
			System.out.println("Html length: " + html.length());
			System.out.println("Number of outgoing links: " + links.size());
			System.out.println("Fetch time: " + currentTime);
		}
	}
}
