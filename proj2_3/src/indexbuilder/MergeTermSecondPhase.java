package indexbuilder;

import Strucutre.Page_Positions;

import java.util.HashMap;

/**
 * Created by soushimei on 2/13/14.
 */
public class MergeTermSecondPhase {
    private final String TempFolderPath;
    private final String DocumentIndexFolderPath;

    public MergeTermSecondPhase(String path,
                               String TempFolder,
                               String DocumentIndexFolder) {
        TempFolderPath = path + TempFolder;
        DocumentIndexFolderPath = path + DocumentIndexFolder;
    }

    public String merge(HashMap<String, Page_Positions> mergeFirstPhaseMap) {
        String inverseIndexFileName = DocumentIndexFolderPath + "InverseIndex.txt";
        return  inverseIndexFileName;
    }
}
