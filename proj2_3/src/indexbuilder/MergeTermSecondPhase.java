package indexbuilder;

import Strucutre.PagePositions;

import java.util.HashMap;

/**
 * Created by soushimei on 2/13/14.
 */
public class MergeTermSecondPhase {
    private final String tempFolderPath;
    private final String documentIndexFolderPath;

    public MergeTermSecondPhase(String path,
                                String tempFolder,
                                String documentIndexFolder) {
        tempFolderPath = path + tempFolder;
        documentIndexFolderPath = path + documentIndexFolder;
    }

    public String merge(HashMap<String, PagePositions> mergeFirstPhaseMap) {
        String inverseIndexFileName = documentIndexFolderPath + "InverseIndex.txt";
        return inverseIndexFileName;
    }
}
