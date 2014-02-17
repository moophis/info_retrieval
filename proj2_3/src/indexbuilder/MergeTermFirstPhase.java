package indexbuilder;

import Strucutre.PagePositions;
import Strucutre.WordPagePosition;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by soushimei on 2/13/14.
 */
public class MergeTermFirstPhase {
    private final String tempFolderPath;

    public MergeTermFirstPhase(String path,
                               String TempFolder) {
        tempFolderPath = path + TempFolder;
    }

    public void merge(ArrayList<WordPagePosition> splitLists,
                      HashMap<String, PagePositions> mergeFirstPhaseMap) {

    }
}
