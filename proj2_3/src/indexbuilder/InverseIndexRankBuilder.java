package indexbuilder;

/**
 * Created by soushimei on 2/13/14.
 */
public class InverseIndexRankBuilder {
    private final String documentIndexFolderPath;


    public InverseIndexRankBuilder(String path,
                                   String DocumentIndexFolder) {
        documentIndexFolderPath = path + DocumentIndexFolder;
    }

    // return the index file built
    public void build(String InverseIndex_fileName) {
        String inverseIndexRankFilePath = documentIndexFolderPath + InverseIndex_fileName;
    }

}
