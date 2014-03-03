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
        return ndcg;
    }
    private static void indexStriper(ArrayList<String> urls) {
        for (String url : urls) {
            int pos_php = url.lastIndexOf("index.php");
            int pos_html = url.lastIndexOf("index.html");
            int pos_htm = url.lastIndexOf("index.htm");
            if (pos_php != -1) {
                url = url.substring(0, pos_htm);
            }
        }
    }

}
