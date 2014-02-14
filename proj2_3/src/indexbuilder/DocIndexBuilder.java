package indexbuilder;

/**
 * Created by soushimei on 2/13/14.
 */
public class DocIndexBuilder {
    private final String DocumentIndexFolderPath;
    private final String RawHTMLFolderPath;
    private final String RawInfoFolderPath;


    public DocIndexBuilder(String path,
                           String DocumentIndexFolder,
                           String RawHTMLFolder,
                           String RawInfoFolder) {
        DocumentIndexFolderPath = path + DocumentIndexFolder;
        RawHTMLFolderPath = path + RawHTMLFolder;
        RawInfoFolderPath = path + RawInfoFolder;
    }

    /// Building the index of document
    public void build(String URL2MD5_filePath, String MD52URL_filePath) {
        String URL2MD5_fileName = "URL_to_MD5.txt";
        String MD52URL_fileName = "MD5_to_URL.txt";
        URL2MD5_filePath = DocumentIndexFolderPath + URL2MD5_fileName;
        MD52URL_filePath = DocumentIndexFolderPath + MD52URL_fileName;

        buildURL2MD5(URL2MD5_filePath);

        buildMD52URL(MD52URL_filePath);
    }

    /// Building the index of URL to
    /// MD5,
    /// relative postion in the HTML
    /// length of the HTML
    /// raw file name
    /// The variable URL2MD5_filePath is the place to save the index
    private void buildURL2MD5(String URL2MD5_filePath) {

    }
    /// Building the index of MD5 to URL
    /// The variable MD52URL_filePath is the place to save the index
    private void buildMD52URL(String MD52URL_filePath) {

    }

}
