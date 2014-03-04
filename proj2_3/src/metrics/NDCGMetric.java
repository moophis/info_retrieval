package metrics;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by soushimei on 3/3/14.
 */
public class NDCGMetric {
    public static ArrayList<Double> compute(ArrayList<String> ideal,
                                     ArrayList<String> actual) {
        ArrayList<Double> ndcg = new ArrayList<>();
        // remove the index file name at the end of url
        ideal = indexStriper(ideal);
        actual = indexStriper(actual);

        // compute relevence
        HashMap<String, Double> idealRelevence = computeIdealRelevence(ideal);
        HashMap<String, Double> actualRelevence = computeActualRelevence(actual, idealRelevence);

        // compute dcgs
        HashMap<String, Double> idealDCGs = computeDCG(ideal, idealRelevence);
        HashMap<String, Double> actualDCGs = computeDCG(actual, actualRelevence);

        // compute ndcgs
        ndcg = computeNDCG(ideal, idealDCGs, actualDCGs);

        return ndcg;
    }

    private static ArrayList<Double> computeNDCG(ArrayList<String> urls,
                                                       HashMap<String, Double> idealDCGs,
                                                       HashMap<String, Double> actualDCGs) {
        ArrayList<Double> ndcgs = new ArrayList<>();
        double prevIdealDCG = 0.0;
        double prevActualDCG = 0.0;

        for (String url : urls) {
            prevIdealDCG += idealDCGs.get(url);
            if (actualDCGs.containsKey(url)) {
            	prevActualDCG += actualDCGs.get(url);
            }
            ndcgs.add(prevActualDCG / prevIdealDCG);
        }
        return ndcgs;
    }

    private static HashMap<String, Double> computeDCG(ArrayList<String> urls,
                                                      HashMap<String, Double> relevence) {
        HashMap<String, Double> dcgs = new HashMap<>();
        HashMap<String, Double> discounts = computeDiscount(urls);

        for (String url : urls) {
            double rel = relevence.get(url);
            double dis = discounts.get(url);
            double dcg;
            if (Math.abs(dis - 1.0) < 1e-6) {
                dcg = rel;
            } else {
                dcg = rel / (Math.log(dis) / Math.log(2));
            }
            dcgs.put(url, dcg);
        }

        return dcgs;
    }

    private static HashMap<String, Double> computeActualRelevence(ArrayList<String> actual,
                                                                  HashMap<String, Double> idealRelevence) {
        HashMap<String, Double> relevence = new HashMap<>();
        for (String str : actual) {
            if (idealRelevence.containsKey(str)) {
                relevence.put(str, idealRelevence.get(str));
            } else {
                relevence.put(str, 0.0);
            }
        }
        return relevence;
    }

    private static HashMap<String, Double> computeIdealRelevence(ArrayList<String> ideal) {
        HashMap<String, Double> relevence = new HashMap<>();
        int relevanceScale = ideal.size();

        for (String str : ideal) {
            relevence.put(str, (double)relevanceScale);
            --relevanceScale;
        }

        return relevence;
    }

    private static HashMap<String, Double> computeDiscount(ArrayList<String> urls) {
        HashMap<String, Double> discounts = new HashMap<>();
        int discount = 1;

        for (String str : urls) {
            discounts.put(str, (double)discount);
            ++discount;
        }

        return discounts;
    }

    private static ArrayList<String> indexStriper(ArrayList<String> urls) {
        ArrayList<String> adjustedUrls = new ArrayList<>();
        for (String url : urls) {
            int pos_php = url.lastIndexOf("index.php");
            int pos_html = url.lastIndexOf("index.html");
            int pos_htm = url.lastIndexOf("index.htm");
            if (pos_php != -1) {
                url = url.substring(0, pos_php);
            } else if (pos_htm != -1) {
                url = url.substring(0, pos_htm);
            } else if (pos_html != -1) {
                url = url.substring(0, pos_html);
            }
            adjustedUrls.add(url);
        }
        return adjustedUrls;
    }

}
