package indexbuilder;

import Strucutre.WordPagePosition;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by soushimei on 2/13/14.
 */
public class SplitDocuments {
    private final String tempFolderPath;
    private final String rawHTMLFolderPath;
    private final String rawInfoFolderPath;
    public SplitDocuments(String path,
                          String tempFolder,
                          String rawHTMLFolder,
                          String rawInfoFolder) {
        tempFolderPath = path + tempFolder;
        rawHTMLFolderPath = path + rawHTMLFolder;
        rawInfoFolderPath = path + rawInfoFolder;
    }

    public void split(ArrayList<WordPagePosition> splitLists) {

    }

}
