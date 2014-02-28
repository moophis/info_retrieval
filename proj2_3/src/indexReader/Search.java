package indexReader;

import indexbuilder.IndexBuilder;
import indexbuilder.StemDocuments;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import structure.TF_IDF_Positions;

/**
 * Created by soushimei on 2/19/14.
 */
public class Search {
    private static void calcCloseness(ArrayList<String> queriesList,
                                      ArrayList<String> hitMd5s,
                                      HashMap<String, Double> closeness) {
        PriorityQueue<Integer> queue;
        if (queriesList.isEmpty() || hitMd5s.isEmpty())
            return;
        HashMap<String, HashMap<String, TF_IDF_Positions>> inverseIndex = InverseIndex.getInstance().getInverseIndex();

        int maximumWordLen = 0;

        for (String query : queriesList) {
            maximumWordLen = Math.max(maximumWordLen, query.length());
        }


        for (String hitMd5 : hitMd5s) {
            if (queriesList.size() == 1) {
                // only one term, no need to check its closeness
                closeness.put(hitMd5, 1.0);
                continue;
            }
            queue = new PriorityQueue<>();
            for (String query : queriesList) {
                ArrayList<Integer> positions = inverseIndex.get(query).get(hitMd5).positions;
                for (int pos : positions) {
                    queue.add(pos);
                }
            }
            // calc closeness
            if (queue.isEmpty() || queue.size() == 1) {
                // only less than 2 positions in this queue
                // i.e. only 1 position for all queries
                closeness.put(hitMd5, 1.0);
                continue;
            }
            int pos = queue.poll();
            int len = queue.size();
            int neighborCount = 0;

            // calc closeness within the priority queue
            while (!queue.isEmpty()) {
                int inv = Math.abs(queue.peek() - pos);
                if (inv <= maximumWordLen * 3) {// if two words are pretty close, that is a great hit
                    ++neighborCount;
                }
                pos = queue.poll();
            }

            int closenessLevel = 4;
            if (neighborCount > closenessLevel)
                neighborCount = closenessLevel;
            double cl = (double)neighborCount;
            closeness.put(hitMd5, cl);
        }
    }

    private static void calcTitleScores(ArrayList<String> queriesList,
                                      ArrayList<String> hitMd5s,
                                      HashMap<String, Double> titleScores) {
        if (queriesList.isEmpty() || hitMd5s.isEmpty())
            return;
        for (String hitMd5 : hitMd5s) {
            double score = 0.0;
            for (String query : queriesList) {
                if (MD52Title.getInstance().contains(hitMd5, query)) {
                    score += 1.0;
                }
            }
            titleScores.put(hitMd5, score);
        }

    }

    private static void calcAnchorScores(ArrayList<String> queriesList,
                                        ArrayList<String> hitMd5s,
                                        HashMap<String, Double> anchorScores) {
        if (queriesList.isEmpty() || hitMd5s.isEmpty())
            return;
        for (String hitMd5 : hitMd5s) {
            double score = 0.0;
            for (String query : queriesList) {
                if (MD52Anchor.getInstance().contains(hitMd5, query)) {
                    score += 1.0;
                }
            }
            anchorScores.put(hitMd5, score);
        }

    }

    // NOTE: this is the method user should use
    public static long search(String query_input,
                              ArrayList<String> hitMd5s, HashMap<String, Integer> hitPositions,
                              HashMap<String, Double> scores) {
        HashMap<String, Double> pageRanks = new HashMap<>();
        HashMap<String, Double> tf_idfs = new HashMap<>();
        HashMap<String, Double> closeness = new HashMap<>();
        HashMap<String, Double> titleScores = new HashMap<>();
        HashMap<String, Double> anchorScores = new HashMap<>();
        return searchDebug(query_input, hitMd5s, hitPositions,
                scores, tf_idfs, closeness, pageRanks, titleScores, anchorScores);
    }

    public static long searchBasic(String query_input,
                              ArrayList<String> hitMd5s, HashMap<String, Integer> hitPositions,
                              HashMap<String, Double> scores) {
        HashMap<String, Double> pageRanks = new HashMap<>();
        HashMap<String, Double> tf_idfs = new HashMap<>();
        HashMap<String, Double> closeness = new HashMap<>();
        HashMap<String, Double> titleScores = new HashMap<>();
        HashMap<String, Double> anchorScores = new HashMap<>();
        long timeSpent = 0;
        timeSpent = searchDebug(query_input, hitMd5s, hitPositions,
                scores, tf_idfs, closeness, pageRanks, titleScores, anchorScores);
        // clear the advanced scores
        scores.clear();

        for (Map.Entry<String, Double> m : tf_idfs.entrySet()) {
            scores.put(m.getKey(), m.getValue());
        }
        return timeSpent;
    }

    public static long searchDebug(String query_input,
                                   ArrayList<String> hitMd5s, HashMap<String, Integer> hitPositions,
                                   HashMap<String, Double> scores,
                                   HashMap<String, Double> tf_idfs,
                                   HashMap<String, Double> closeness,
                                   HashMap<String, Double> pageRanks,
                                   HashMap<String, Double> titleScores,
                                   HashMap<String, Double> anchorScores) {
        long beginTime = System.nanoTime();
        if (query_input == null || query_input.isEmpty()) {
            return System.nanoTime() - beginTime;
        }
        String[] queries = query_input.split("\\W");
        ArrayList<String> queriesList = new ArrayList<>();
        HashSet<String> checkExists = new HashSet<>();
        StemDocuments stemmer = new StemDocuments();
        for (String q : queries) {
            if (q == null || q.isEmpty()) {
                continue;
            }
            if (!checkExists.contains(q)) {
                queriesList.add(stemmer.stemWord(q));
                checkExists.add(q);
            }
        }

        if (queriesList.isEmpty()) {
            return System.nanoTime() - beginTime;
        }
        /*
            calculate posting urls
         */
        HashMap<String, HashMap<String, TF_IDF_Positions>> inverseIndex = InverseIndex.getInstance().getInverseIndex();
        HashMap<String, Double>maginitude = new HashMap<>();

        // get first term's inverse index
        if (!inverseIndex.containsKey(queriesList.get(0))) {
            // cannot find the term
            System.out.println("Cannot find the term: " + queriesList.get(0));
            return System.nanoTime() - beginTime;
        }
        HashMap<String, TF_IDF_Positions> firstInverseIndex = inverseIndex.get(queriesList.get(0));
        // use this to calc the closeness

        for (Map.Entry<String, TF_IDF_Positions> m : firstInverseIndex.entrySet()) {
            boolean hitAll = true;
            for (int j = 1; j < queriesList.size(); ++j) {
                String term = queriesList.get(j);
                if (!inverseIndex.containsKey(term)) {
                    // cannot find the term
                    System.out.println("Cannot find the term: " + term);
                    return System.nanoTime() - beginTime;
                }
                HashMap<String, TF_IDF_Positions> otherInverseIndex = inverseIndex.get(term);
                if (!otherInverseIndex.containsKey(m.getKey())) {
                    hitAll = false;
                    break;
                }
            }
            if (hitAll) {
                // add md5 that all hits
                hitMd5s.add(m.getKey());
                // add the very first pos that the first item hits
                hitPositions.put(m.getKey(), inverseIndex.get(queriesList.get(0)).get(m.getKey()).positions.get(0));
                // add tf_idfs
                tf_idfs.put(m.getKey(), 0.0);
                maginitude.put(m.getKey(), 0.0);
                // add PageRank
                pageRanks.put(m.getKey(), PageRank.getInstance().getAPageRank(m.getKey()));
            }
        }
        // calculate the closeness
        calcCloseness(queriesList, hitMd5s, closeness);

        // calculate the title score;
        calcTitleScores(queriesList, hitMd5s, titleScores);

        // calculate the anchor score:
        calcAnchorScores(queriesList, hitMd5s, anchorScores);

        // calculate the Cosine Similarity
        double corpus = MD52Doc.getInstance().size();
        for (String query : queriesList) {
            for (String md5 : hitMd5s) {
                double tf_idf = tf_idfs.get(md5);
                tf_idf += inverseIndex.get(query).get(md5).tf_idf;
                        // * Math.log(corpus / (1 + inverseIndex.get(query).size()));
                tf_idfs.put(md5, tf_idf);
                double mag = maginitude.get(md5);
                mag += inverseIndex.get(query).get(md5).tf_idf * inverseIndex.get(query).get(md5).tf_idf;
                maginitude.put(md5, mag);
             }
        }

        // calc max
        final double EPS = 1e-7;
        double maxPageRank = EPS;
        double maxTfIdf = EPS;
        double maxCloseness = EPS;
        double maxTitleScores = EPS;
        double maxAnchorScores = EPS;

        for (String hitMd5 : hitMd5s) {
            // calc max tf-idf
            double tf_idf = tf_idfs.get(hitMd5) / Math.sqrt(maginitude.get(hitMd5));
            tf_idfs.put(hitMd5, tf_idf);
            if (tf_idf > maxTfIdf) {
                maxTfIdf = tf_idf;
            }
            // calc max PageRank
            double pageRank = pageRanks.get(hitMd5);
            if (pageRank > maxPageRank) {
                maxPageRank = pageRank;
            }
            // calc max closeness
            double cl = closeness.get(hitMd5);
            if (cl > maxCloseness) {
                maxCloseness = cl;
            }
            // calc max title score
            double ts = titleScores.get(hitMd5);
            if (ts > maxTitleScores) {
                maxTitleScores = ts;
            }
            // calc max anchor score
            double as = anchorScores.get(hitMd5);
            if (as > maxAnchorScores) {
                maxAnchorScores = as;
            }
        }

        // normalizing
        // int lenUrl = MD52Doc.getInstance().getURL(hitMd5).length();
        double fracPageRank, fracTf_idf, fracCloseness, fracTitle, fracAnchor;
        if (queriesList.size() == 1) {
            fracPageRank = .2; fracTf_idf = .3; fracCloseness = 0; fracTitle = 0.25; fracAnchor = 0.25;
        } else {
            fracPageRank = .1; fracTf_idf = .4; fracCloseness = .2; fracTitle = 0.2; fracAnchor = 0.1;
        }
        // assigning scores
        for (String hitMd5 : hitMd5s) {
            double pageRank = pageRanks.get(hitMd5) / maxPageRank;
            double tf_idf = tf_idfs.get(hitMd5) / maxTfIdf;
            double cl = closeness.get(hitMd5) / maxCloseness;
            double ts = titleScores.get(hitMd5) / maxTitleScores;
            double as = anchorScores.get(hitMd5) / maxAnchorScores;

            pageRanks.put(hitMd5, pageRank);
            closeness.put(hitMd5, cl);
            tf_idfs.put(hitMd5, tf_idf);
            titleScores.put(hitMd5, ts);
            anchorScores.put(hitMd5, as);

            double score = (pageRank * fracPageRank
                    + tf_idf * fracTf_idf
                    + cl * fracCloseness
                    + ts * fracTitle
                    + as * fracAnchor);
            scores.put(hitMd5, score);
        }
        return System.nanoTime() - beginTime;
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: add \"path\"");
            return;
        }

        IndexBuilder indexBuilder = new IndexBuilder(args[0]);
        try {
            indexBuilder.buildIndex();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Ready to searchDebug");

        while (true) {
            BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
            String query_input = null;
            try {
                query_input = buf.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }


            System.out.println("Searching " + query_input);
            ArrayList<String> md5s = new ArrayList<>();
            HashMap<String, Integer> positions = new HashMap<>();
            HashMap<String, Double> scores = new HashMap<>();
            HashMap<String, Double> pageRanks = new HashMap<>();
            HashMap<String, Double> tf_idfs = new HashMap<>();
            HashMap<String, Double> closeness = new HashMap<>();
            HashMap<String, Double> titleScores = new HashMap<>();
            HashMap<String, Double> anchorScores = new HashMap<>();
            long timeSpent = 0;
            timeSpent = Search.searchDebug(query_input, md5s, positions,
                    scores,
                    tf_idfs,
                    closeness,
                    pageRanks,
                    titleScores,
                    anchorScores);
            for (String md5 : md5s) {
                String url = MD52Doc.getInstance().getURL(md5);
                Double score = scores.get(md5);
                Double pageRank = pageRanks.get(md5);
                Double tf_idf = tf_idfs.get(md5);
                Double cl = closeness.get(md5);
                Double ts = titleScores.get(md5);
                Double anchor = anchorScores.get(md5);
                System.out.println(new StringBuilder().append(url)
                        .append(":\tscore: ").append(score.toString())
                        .append("\tPageRank: ").append(pageRank.toString())
                        .append("\ttf_idf: ").append(tf_idf.toString())
                        .append("\tcloseness: ").append(cl.toString())
                        .append("\tanchor: ").append(anchor.toString())
                        .append("\ttitleScores: ").append(ts.toString()).toString());

            }
            System.out.println("Total time spent " + new Long(timeSpent).toString());
            if (query_input.equals("quit"))
                break;
        }
    }
    
}
