package crawler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import ir.assignments.two.a.*;
import ir.assignments.two.b.*;
import ir.assignments.two.c.*;


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
    public static String getPageInfo(String line, String type) {
        if (line == null)
            return line;

        String[] strings = line.split(":");

        switch (type) {
            case "urlMd5": // (string)
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
            String path = Controller.crawlStorageFolder + "raw/info/thread"
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
                        Controller.crawlStorageFolder + "raw/info/Subdomains.txt");
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
            String infPath = Controller.crawlStorageFolder + "raw/info/thread"
                    + i.toString() + ".txt";
            String txtPath = Controller.crawlStorageFolder + "raw/text/thread"
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
     * Stop words pattern.
     */
    public final static String stopWords = "([a-z]|[0-9]+|about|above|after|again|against|all|also"
            + "|am|an|another|and|any|are|aren't|as|at|be|because|been|before|being|below|between"
            + "|both|but|by|can|can't|cannot|could|couldn't|did|didn't|do|does|doesn't|doing"
            + "|don't|down|during|each|few|for|from|further|had|hadn't|has|hasn't|have|haven't"
            + "|having|he|he'd|he'll|he's|her|here|here's|hers|herself|him|himself|his|how|how's"
            + "|i|i'd|i'll|i'm|i've|if|in|into|is|isn't|it|it's|its|itself|let's|me|more|most|mustn't"
            + "|may|might|my|myself|no|nor|not|of|off|on|once|only|or|other|ought|our|ours"
            + "|ourselves|out|over|own|same|shan't|she|she'd|she'll|she's|should|shouldn't"
            + "|so|some|such|than|that|that's|the|their|theirs|them|themselves|then|there|there's"
            + "|these|they|they'd|they'll|they're|they've|this|those|through|to|too|under|until|up"
            + "|very|was|wasn't|we|we'd|we'll|we're|we've|were|weren't|what|what's|when|when's|where"
            + "|where's|which|while|who|who's|whom|why|why's|will|with|won't|would|wouldn't|you|you'd|you'll"
            + "|you're|you've|your|yours|yourself|yourselves|['_-]+|[0-9]{2}[ap]m)$";
    private final static Pattern FILTERS = Pattern.compile(stopWords);

    /**
     *	Print 500 most common words in this domain.
     * @throws IOException
     */
    public static void commonWords() throws IOException {
        List<Frequency> freq = null;
        List<String> words = new ArrayList<String>();

        for (Integer i = 13; i <= 19; i++) {
            String path = Controller.crawlStorageFolder + "data/text/thread"
                    + i.toString() + ".txt";
            File f = new File(path);
            System.out.println(f);

            List<String> w = Utilities.tokenizeFile(f);
            words.addAll(w);
        }

        freq = WordFrequencyCounter.computeWordFrequencies(words);

        int counter = 500;
        for (Frequency f : freq) {
            String word = f.getText();
            if (!FILTERS.matcher(word).matches()) {
                System.out.println(f);
                counter--;
            }
            if (counter == 0)
                break;
        }
    }

    /**
     * Print the 20 most common 2-grams.
     */
    public static void commonTwoGrams() throws IOException {
        List<Frequency> freq = null;
        ArrayList<String> words = new ArrayList<String>();

        for (Integer i = 13; i <= 19; i++) {
            String path = Controller.crawlStorageFolder + "data/text/thread"
                    + i.toString() + ".txt";
            File f = new File(path);
            System.out.println(f);

            List<String> w = Utilities.tokenizeFile(f);
            words.addAll(w);
        }

        freq = TwoGramFrequencyCounter.computeTwoGramFrequencies(words);

        int counter = 20;
        for (Frequency f : freq) {
            String word = f.getText();
            String[] strings = word.split(" ");
            if (!FILTERS.matcher(strings[0]).matches()
                    && !FILTERS.matcher(strings[1]).matches()) {
                System.out.println(f);
                counter--;
            }
            if (counter == 0)
                break;
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
//		try {
//			subdomainsStats();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
        try {
//			findLongestPage();
//			commonWords();
            commonTwoGrams();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}