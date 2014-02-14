package indexbuilder;

import Strucutre.Page_Positions;
import Strucutre.Word_Page_Position;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by soushimei on 2/13/14.
 */
public class MergeTermFirstPhase {
    private final String TempFolderPath;

    public MergeTermFirstPhase(String path,
                               String TempFolder) {
        TempFolderPath = path + TempFolder;
    }

    public void merge(ArrayList<Word_Page_Position> splitLists,
                      HashMap<String, Page_Positions> mergeFirstPhaseMap) {

    }
}
