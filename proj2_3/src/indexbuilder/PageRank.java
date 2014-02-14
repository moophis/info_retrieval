package indexbuilder;

/**
 * Created by soushimei on 2/13/14.
 */
public class PageRank {
    private final String DocumentIndexFolderPath;
    private final String RawHTMLFolderPath;
    private final String RawInfoFolderPath;


    public PageRank(String path,
                    String DocumentIndexFolder,
                    String RawHTMLFolder,
                    String RawInfoFolder) {
        DocumentIndexFolderPath = path + DocumentIndexFolder;
        RawHTMLFolderPath = path + RawHTMLFolder;
        RawInfoFolderPath = path + RawInfoFolder;
    }

    /// Calculate the pagerank of the document
    /// pageRank_filePath is the path and name of the index returned
    public void calcPageRank(String pageRank_fileName) {
        String pageRank_filePath = DocumentIndexFolderPath + pageRank_fileName;

    }
}
