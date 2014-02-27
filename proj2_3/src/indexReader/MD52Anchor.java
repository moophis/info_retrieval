package indexReader;

import indexbuilder.StemDocuments;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by soushimei on 2/27/14.
 */
public class MD52Anchor {
    private MD52Anchor(){}
    private static MD52Anchor uniqueInstance;
    public static MD52Anchor getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new MD52Anchor();
        }
        return uniqueInstance;
    }

    private HashMap<String, HashSet<String>> anchorTexts = new HashMap<>();

    public void readFromDisk(String filePath) {
        File f = new File(filePath);
        BufferedReader reader;
        anchorTexts.clear();

        StemDocuments stemmer = new StemDocuments();
        try {
            reader = new BufferedReader(new FileReader(f));
            String line;
            while ((line = reader.readLine()) != null) {
                String md5 = line.substring(0, 32);
                String title = line.substring(32);

                String[] strs = title.split("\\W");

                if (!anchorTexts.containsKey(md5)) {
                    anchorTexts.put(md5, new HashSet<String>());
                }

                for (String str :strs) {
                    if (str != null && !str.isEmpty()) {
                        anchorTexts.get(md5).add(stemmer.stemWord(str));
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("Error process " + filePath);
            e.printStackTrace();
        }
    }

    public boolean contains(String md5, String str) {
        if (!anchorTexts.containsKey(md5)) {
            return false;
        }
        return anchorTexts.get(md5).contains(str);
    }
}
