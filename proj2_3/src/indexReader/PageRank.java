package indexReader;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class PageRank {
	private static PageRank uniqueInstance;
	private PageRank() {}
	public static PageRank getInstance() {
		if (uniqueInstance == null) {
			uniqueInstance = new PageRank();
		}
		return uniqueInstance;
	}

    public void write2Disk(String filePath) {
        if (pageRank == null) {
            System.err.println("page rank index has not been initilized yet. Tyr build pagerank first.");
            return;
        }
        FileWriter fw;
        try {
            fw = new FileWriter(filePath, false);

            for (Map.Entry<String, Double> m : pageRank.entrySet()) {
                String buf = new StringBuilder().append(m.getKey()).append(":").append(m.getValue()).toString();
                fw.write(buf + "\n");
            }
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void readFromDisk(String filePath) {
        File f = new File(filePath);
        BufferedReader reader;
        if (pageRank == null)
            pageRank = new HashMap<>();

        try {
            reader = new BufferedReader(new FileReader(f));
            String line;
            String md5 = "";
            double rank = 0.0;
            while ((line = reader.readLine()) != null) {
                String[] strings = line.split(":");
                md5 = strings[0];
                rank = Double.parseDouble(strings[1]);
                pageRank.put(md5, rank);
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("Error process " + filePath);
            e.printStackTrace();
        }
    }

    private HashMap<String, Double> pageRank = null;

    public HashMap<String, Double> getPageRank() {
        return pageRank;
    }

    public double getAPageRank(String md5) {
        if (pageRank.containsKey(md5))
            return pageRank.get(md5);
        else
            return 0.0;
    }

    public void setPageRank(HashMap<String, Double> pk) {
        pageRank = pk;
    }

    public void clear() {
        pageRank.clear();
    }

    public void printMaxPageRank() {
        double maxPageRank = 0.0;
        String maxMd5 = "";
        for (Map.Entry<String, Double> m : pageRank.entrySet()) {
            if (maxPageRank < m.getValue()) {
                maxPageRank = m.getValue();
                maxMd5 = m.getKey();
            }
        }
        System.out.println("URL with maximum page rank: " + MD52Doc.getInstance().getURL(maxMd5));
        System.out.println("Maximum page rank is: " + maxPageRank);
    }

    public int size() {
        return pageRank.size();
    }
}

