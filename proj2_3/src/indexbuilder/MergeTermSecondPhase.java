package indexbuilder;

import Strucutre.PagePosition;
import Strucutre.TF_IDF_Positions;
import indexReader.InverseIndex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    // merge the pair <term, <URL1, pos1>, <URL1, pos2>,... > to <term, <URL/MD5, <pos1, pos2, pos3,...,> > >
    public void merge(HashMap<String, ArrayList<PagePosition>> mergeFirstPhaseMap,
                      HashMap<String, HashMap<String, TF_IDF_Positions> > mergeSecondPhaseMap) {

        for (Map.Entry<String, ArrayList<PagePosition>> m : mergeFirstPhaseMap.entrySet()) {
            if (!mergeSecondPhaseMap.containsKey(m.getKey())) {
                mergeSecondPhaseMap.put(m.getKey(), new HashMap<String, TF_IDF_Positions>());
            }
            HashMap<String, TF_IDF_Positions> tf_idf_positionsHashMap = mergeSecondPhaseMap.get(m.getKey());
            ArrayList<PagePosition> pagePositions = mergeFirstPhaseMap.get(m.getKey());

            for (PagePosition pp : pagePositions) {
                if (!tf_idf_positionsHashMap.containsKey(pp.urlMD5)) {
                    tf_idf_positionsHashMap.put(pp.urlMD5, new TF_IDF_Positions());
                    tf_idf_positionsHashMap.get(pp.urlMD5).positions = new ArrayList<>();
                }
                TF_IDF_Positions tf_idf_positions = tf_idf_positionsHashMap.get(pp.urlMD5);
                tf_idf_positions.positions.add(pp.position);
            }
        }
    }
}
