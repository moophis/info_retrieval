package indexbuilder;

import indexReader.PageRank;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by soushimei on 2/13/14.
 */
public class PageRankBuilder {
    private final String documentIndexFolderPath;
    private final String rawInfoFolderPath;


    public PageRankBuilder(String path,
                           String documentIndexFolder,
                           String rawInfoFolder) {
        documentIndexFolderPath = path + documentIndexFolder;
        rawInfoFolderPath = path + rawInfoFolder;
    }
    private static HashMap<String, HashSet<String> > pageRankData = new HashMap<>();
    private static HashMap<String, Integer> outDegree = new HashMap<>();

    /// Calculate the pagerank of the document
    /// pageRank_filePath is the path and name of the index returned
    public void calcPageRank() {
        // read in raw data
        readRawDataFromDisk();
        // begin calc page rank
        // set the page rank to be uniformly equal
        double initPageRank = 1.0/outDegree.size();
        HashMap<String, Double> pageRank = new HashMap<>();
        for (Map.Entry<String, Integer> m : outDegree.entrySet()) {
            pageRank.put(m.getKey(), initPageRank);
        }

        // begin iteration
        double eps = 1e-8, error = 1.0, lambda = 0.15;
        while (error > eps) {
            error = 0.0;
            for (Map.Entry<String, Integer> m : outDegree.entrySet()) {
                HashSet<String> pagePoint2Me = pageRankData.get(m.getKey());
                double rank = 0.0;
                if (pagePoint2Me == null || pagePoint2Me.isEmpty()) {
                    // no page points to the current page

                } else {
                    for (String page : pagePoint2Me) {
                        rank += (double)(pageRank.get(page)) / (double)(outDegree.get(page));
                    }
                }
                // compute the error
                rank = lambda/outDegree.size() + (1-lambda)*rank;
                error += Math.abs(pageRank.get(m.getKey()) - rank);
                pageRank.put(m.getKey(), rank);
            }
        }

        // set the page rank to the PageRankBuilder
        PageRank.getInstance().setPageRank(pageRank);
    }
    private void readRawDataFromDisk() {
        String pageOutDegree_filePath = rawInfoFolderPath + "pageOutDegree.txt";
        String pageRank_filePath = rawInfoFolderPath + "pageRank.txt";

        // read the out degree of the urls
        File f = new File(pageOutDegree_filePath);
        BufferedReader reader;

        try {
            reader = new BufferedReader(new FileReader(f));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] strs = line.split(":");
                String md5 = strs[0];
                int outdeg = Integer.parseInt(strs[1]);
                outDegree.put(md5, outdeg);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        // read the page in degree
        f = new File(pageRank_filePath);
        try {
            reader = new BufferedReader(new FileReader(f));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] strs = line.split(":");
                String md5 = strs[0];
                pageRankData.put(md5, new HashSet<String>());
                int len = strs.length;
                for (int i = 1; i < len; ++i) {
                    pageRankData.get(md5).add(strs[i]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
