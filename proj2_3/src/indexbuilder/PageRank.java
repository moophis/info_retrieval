package indexbuilder;

/**
 * Created by soushimei on 2/13/14.
 */
public class PageRank {
    private final String documentIndexFolderPath;
    private final String rawHTMLFolderPath;
    private final String rawInfoFolderPath;


    public PageRank(String path,
                    String documentIndexFolder,
                    String rawHTMLFolder,
                    String rawInfoFolder) {
        documentIndexFolderPath = path + documentIndexFolder;
        rawHTMLFolderPath = path + rawHTMLFolder;
        rawInfoFolderPath = path + rawInfoFolder;
    }

    /// Calculate the pagerank of the document
    /// pageRank_filePath is the path and name of the index returned
    public void calcPageRank(String pageRankzFileName) {
        String pageRank_filePath = documentIndexFolderPath + pageRankzFileName;

    }
}
