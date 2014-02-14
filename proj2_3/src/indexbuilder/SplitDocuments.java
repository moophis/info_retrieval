package indexbuilder;

import Strucutre.Word_Page_Position;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by soushimei on 2/13/14.
 */
public class SplitDocuments {
    private final String TempFolderPath;
    private final String RawHTMLFolderPath;
    private final String RawInfoFolderPath;
    public SplitDocuments(String path,
                          String TempFolder,
                          String RawHTMLFolder,
                          String RawInfoFolder) {
        TempFolderPath = path + TempFolder;
        RawHTMLFolderPath = path + RawHTMLFolder;
        RawInfoFolderPath = path + RawInfoFolder;
    }

    public void split(ArrayList<Word_Page_Position> splitLists) {

    }

}
