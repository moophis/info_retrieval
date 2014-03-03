package metrics;

import java.lang.reflect.Array;
import java.util.ArrayList;

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


        return ndcg;
    }

    private static ArrayList<Double> computeIdealDCG(ArrayList<String> ideal) {
        ArrayList<Double> dcg = new ArrayList<>();
        double relevanceScale = (double)(ideal.size());

        for (String str : ideal) {

        }

        return dcg;
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
