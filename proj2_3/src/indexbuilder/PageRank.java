package indexbuilder;

public class PageRank {
	private static PageRank uniqueInstance;
	private PageRank() {}
	public static PageRank getInstance() {
		if (uniqueInstance == null) {
			uniqueInstance = new PageRank();
		}
		return uniqueInstance;
	}
}
