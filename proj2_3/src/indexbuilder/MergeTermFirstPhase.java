package indexbuilder;

import Strucutre.PagePosition;
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

    // merge the pair <term, URL1, pos1>... to <term, <URL1, pos1>, <URL1, pos2>,... >
    public void merge(ArrayList<WordPagePosition> splitLists,
                      HashMap<String, ArrayList<PagePosition> > mergeFirstPhaseMap) {
        for (WordPagePosition wpp : splitLists) {
            if (!mergeFirstPhaseMap.containsKey(wpp.word)) {
                ArrayList<PagePosition> pagePositions = new ArrayList<>();
                mergeFirstPhaseMap.put(wpp.word, pagePositions);
            }
            PagePosition pagePosition = new PagePosition();
            pagePosition.urlMD5 = wpp.urlMd5;
            pagePosition.position = wpp.position;
            mergeFirstPhaseMap.get(wpp.word).add(pagePosition);
        }
    }
}
