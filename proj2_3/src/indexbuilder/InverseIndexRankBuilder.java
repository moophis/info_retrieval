package indexbuilder;

/**
 * Created by soushimei on 2/13/14.
 */
public class InverseIndexRankBuilder {
    private final String DocumentIndexFolderPath;


    public InverseIndexRankBuilder(String path,
                                String DocumentIndexFolder) {
        DocumentIndexFolderPath = path + DocumentIndexFolder;
    }

    // return the index file built
    public String build(String inverseIndexFileName) {
        String inverseIndexRankFileName = DocumentIndexFolderPath + "InverseIndexRank.txt";
        return inverseIndexRankFileName;
    }

}
