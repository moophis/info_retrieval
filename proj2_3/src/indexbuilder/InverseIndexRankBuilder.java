package indexbuilder;

import indexReader.InverseIndex;
import indexReader.MD52Doc;

import java.util.HashMap;
import java.util.Map;

import structure.TF_IDF_Positions;

/**
 * Created by soushimei on 2/13/14.
 */
public class InverseIndexRankBuilder {
    private final String documentIndexFolderPath;


    public InverseIndexRankBuilder(String path,
                                   String DocumentIndexFolder) {
        documentIndexFolderPath = path + DocumentIndexFolder;
    }

    // return the index file built
    // TF is the number of times that term t occurs in document d
    // IDF is obtained by dividing the total number of documents by
    // the number of documents containing the term, and then taking the logarithm of that quotient.
    public void build(HashMap<String, HashMap<String, TF_IDF_Positions>> mergeSecondPhaseMap) {
        // first calculate the total number of documents
        int N = MD52Doc.getInstance().size();

        // iteratively calc the tf-idf
        for (Map.Entry<String, HashMap<String, TF_IDF_Positions>> m : mergeSecondPhaseMap.entrySet()) {
            String term = m.getKey();
            HashMap<String, TF_IDF_Positions> termInEachPage = m.getValue();

            // number of document containing the term:
            double df = (double)termInEachPage.size();

            for (Map.Entry<String, TF_IDF_Positions> n : termInEachPage.entrySet()) {
                TF_IDF_Positions tf_idf_positions = n.getValue();
                // number of times that term occurs in document
                double tf = (double)tf_idf_positions.positions.size();
                tf_idf_positions.tf_idf = (1 + Math.log(tf)) * Math.log(N / df);
            }

        }

        InverseIndex.getInstance().setInverseIndex(mergeSecondPhaseMap);
    }

}
