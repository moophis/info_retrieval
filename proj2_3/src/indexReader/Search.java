package indexReader;

import Strucutre.TF_IDF_Positions;
import indexbuilder.IndexBuilder;
import indexbuilder.StemDocuments;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by soushimei on 2/19/14.
 */
public class Search {
    public static void search(String query_input,
                       ArrayList<String> md5s, HashMap<String, ArrayList<Integer>> positions, HashMap<String, Double> scores) {
        if (query_input == null || query_input.isEmpty())
            return;
        String[] queries = query_input.split("\\W");
        ArrayList<String> queriesList = new ArrayList<>();
        HashSet<String> checkExists = new HashSet<>();
        StemDocuments stemmer = new StemDocuments();
        for (String q : queries) {
            if (!checkExists.contains(q)) {
                queriesList.add(stemmer.stemWord(q));
                checkExists.add(q);
            }
        }
        /*
            calculate posting urls
         */
        HashMap<String, HashMap<String, TF_IDF_Positions>> inverseIndex = InverseIndex.getInstance().getInverseIndex();
        HashMap<String, Double>maginitude = new HashMap<>();

        // get first term's inverse index
        if (!inverseIndex.containsKey(queriesList.get(0))) {
            // cannot find the term
            return;
        }
        HashMap<String, TF_IDF_Positions> firstInverseIndex = inverseIndex.get(queriesList.get(0));

        for (Map.Entry<String, TF_IDF_Positions> m : firstInverseIndex.entrySet()) {
            boolean hitAll = true;
            for (int j = 1; j < queriesList.size(); ++j) {
                String term = queriesList.get(j);
                if (!inverseIndex.containsKey(term)) {
                    // cannot find the term
                    System.out.println("Cannot find the term: " + term);
                    return;
                }
                HashMap<String, TF_IDF_Positions> otherInverseIndex = inverseIndex.get(term);
                if (!otherInverseIndex.containsKey(m.getKey())) {
                    hitAll = false;
                    break;
                }
            }
            if (hitAll) {
                // add md5 that all hits
                md5s.add(m.getKey());
                // add pos that all hits
                ArrayList<Integer> pos = new ArrayList<>();
                for (String t : queriesList) {
                    inverseIndex.get(t).get(m.getKey()).positions.get(0);
                }
                positions.put(m.getKey(), pos);
                // add scores
                scores.put(m.getKey(), 0.0);
                maginitude.put(m.getKey(), 0.0);
            }
        }

        // calculate the Cosine Similarity
        double corpus = MD52Doc.getInstance().size();
        for (String query : queriesList) {
            for (String md5 : md5s) {
                double score = scores.get(md5);
                score += inverseIndex.get(query).get(md5).tf_idf;
                        // * Math.log(corpus / (1 + inverseIndex.get(query).size()));
                scores.put(md5, score);
                double mag = maginitude.get(md5);
                mag += inverseIndex.get(query).get(md5).tf_idf * inverseIndex.get(query).get(md5).tf_idf;
                maginitude.put(md5, mag);
             }
        }
        // normalize and consider the pagerank
        for (Map.Entry<String, Double> m : scores.entrySet()) {
            double score = m.getValue() / Math.sqrt(maginitude.get(m.getKey()));
            score += PageRank.getInstance().getAPageRank(m.getKey()) * corpus / 4;
            m.setValue(score);
        }
    }

    public static void main(String[] args) {
        if (args.length <= 1) {
            System.out.println("Usage: add \"path\"");
            return;
        }

        IndexBuilder indexBuilder = new IndexBuilder(args[0]);
        try {
            indexBuilder.buildIndex();
        } catch (IOException e) {
            e.printStackTrace();
        }


        StringBuffer buf = new StringBuffer();
        for (int i = 1; i < args.length; ++i) {
            buf.append(args[i]).append(" ");
        }

        String query_input = buf.toString();
        System.out.println("Searching " + query_input);
        ArrayList<String> md5s = new ArrayList<>();
        HashMap<String, ArrayList<Integer>> positions = new HashMap<>();
        HashMap<String, Double> scores = new HashMap<>();
        search(query_input,md5s, positions, scores);
        for (String md5 : md5s) {
            String url = MD52Doc.getInstance().getURL(md5);
            Double score = scores.get(md5);
            System.out.println(url + ": " + score.toString());
        }
    }
    
}
