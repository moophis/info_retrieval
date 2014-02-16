package indexbuilder;

import Strucutre.WordPagePosition;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by soushimei on 2/13/14.
 */
public class StemDocuments {
    private final String tempFolderPath;
    public StemDocuments(String path,
                                    String tempFolder) {
        tempFolderPath = path + tempFolder;
    }

    public void stem(ArrayList<WordPagePosition> splitLists) {

    }

    public String stemWord(String word) {
        return word;
    }
}
