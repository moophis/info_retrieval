package crawler;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;



public class Controller {
//	final static String crawlStorageFolder = "/Users/liqiangw/Test/IR/";
	
//	final static String crawlStorageFolder = "/media/liqiangw/IR/";
	final static String crawlStorageFolder = "/home/liqiangw/IR/";
    public static void main(String[] args) throws Exception {
        int numberOfCrawlers = 7;
        int politenessDelay = 300;
        String userAgentString = "UCI IR 42682148 93845414";
        int maxDepthOfCrawling = 30;
        
        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(crawlStorageFolder);
        config.setPolitenessDelay(politenessDelay);
        config.setUserAgentString(userAgentString);
        config.setResumableCrawling(true);
        // Unlimited number of pages can be crawled.
        config.setMaxPagesToFetch(-1);
        config.setMaxDepthOfCrawling(maxDepthOfCrawling);
        /*
         * Instantiate the controller for this crawl.
         */
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
        
        
        /*
         * For each crawl, you need to add some seed urls. These are the first
         * URLs that are fetched and then the crawler starts following links
         * which are found in these pages
         */
        controller.addSeed("http://www.ics.uci.edu/");

        /*
         * Start the crawl. This is a blocking operation, meaning that your code
         * will reach the line after this only when crawling is finished.
         */
        controller.start(CrawlerSW.class, numberOfCrawlers);    
    }
}
