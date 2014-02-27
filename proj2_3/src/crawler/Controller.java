package crawler;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

import java.io.FileWriter;
import java.util.HashSet;
import java.util.Map;

public class Controller {
//	public final static String crawlStorageFolder = "/Users/liqiangw/Test/IR/";
	
	public final static String crawlStorageFolder = "/Users/soushimei/Documents/Test/IR/";
    public static void main(String[] args) throws Exception {
        int numberOfCrawlers = 7;
        int politenessDelay = 300;
        String userAgentString = "UCI IR 42682148 93845414";
        int maxDepthOfCrawling = 30;
        int timeOut = 60*1000;
        
        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(crawlStorageFolder);
        config.setPolitenessDelay(politenessDelay);
        config.setUserAgentString(userAgentString);
        config.setResumableCrawling(true);
        // Unlimited number of pages can be crawled.
        config.setMaxPagesToFetch(-1);
        config.setMaxDepthOfCrawling(maxDepthOfCrawling);
        config.setConnectionTimeout(timeOut);
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

        System.out.println("Start saving data");
        FileWriter fw;
        String filePath = crawlStorageFolder + "raw/info/pageRank.txt";
//        fw = new FileWriter(filePath, false);
//        for (Map.Entry<String, HashSet<String> > m : CrawlerSW.pageRankData.entrySet()) {
//            StringBuilder buf = new StringBuilder().append(m.getKey());
//            for (String s : m.getValue()) {
//                buf.append(":").append(s);
//            }
//            fw.write(buf.toString()+"\n");
//        }
//        fw.flush();
//        fw.close();
//
//        filePath = crawlStorageFolder + "raw/info/pageOutDegree.txt";
//        fw = new FileWriter(filePath, false);
//        for (Map.Entry<String, Integer> m : CrawlerSW.outDegree.entrySet()) {
//            StringBuilder buf = new StringBuilder().append(m.getKey()).append(":").append(m.getValue());
//            fw.write(buf.toString()+"\n");
//        }
//        fw.flush();
//        fw.close();

        System.out.println("Start saving anchor text data");
        filePath = crawlStorageFolder + "data/info/anchorText.txt";
        fw = new FileWriter(filePath, false);
        for (Map.Entry<String, HashSet<String>> anchorTexts : CrawlerSW.anchorText.entrySet()) {
            StringBuilder buf = new StringBuilder().append(anchorTexts.getKey());
            for (String anchor : anchorTexts.getValue()) {
                buf.append("$").append(anchor);
            }
            // write to the disk
            fw.write(buf.toString() + "\n");
        }
        fw.flush();
        fw.close();

        System.out.println("Finish!");
    }
}
