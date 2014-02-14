package indexReader;

import java.util.HashMap;

public class PageRank {
	private static PageRank uniqueInstance;
	private PageRank() {}
	public static PageRank getInstance() {
		if (uniqueInstance == null) {
			uniqueInstance = new PageRank();
		}
		return uniqueInstance;
	}

    public void write2Disk() {

    }
    public void readFromDisk() {

    }

    private HashMap<String, Double> pageRank = new HashMap<>();

    public double getPageRank(String MD5) {
        if (pageRank.containsKey(MD5)) {
            return pageRank.get(MD5);
        } else {
            return 0.0;
        }
    }

    public void setPageRank(String MD5, double rank) {
        pageRank.put(MD5, rank);
    }

    public void clear() {
        pageRank.clear();
    }

    public int size() {
        return pageRank.size();
    }
}

