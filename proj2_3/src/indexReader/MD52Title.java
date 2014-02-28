package indexReader;

import indexbuilder.StemDocuments;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by soushimei on 2/24/14.
 */
public class MD52Title {
    private static MD52Title uniqueInstance;
    private MD52Title() {}
    public static MD52Title getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new MD52Title();
        }
        return uniqueInstance;
    }

    public void readFromDisk(String filePath) {
        File f = new File(filePath);
        BufferedReader reader;
        md5Title.clear();

        StemDocuments stemmer = new StemDocuments();

        try {
            reader = new BufferedReader(new FileReader(f));
            String line;
            while ((line = reader.readLine()) != null) {
                String md5 = line.substring(0, 32);
                String title = line.substring(32);

                md5OrigTitle.put(md5, title);

                String[] strs = title.split("\\W");

                if (!md5Title.containsKey(md5)) {
                    md5Title.put(md5, new HashSet<String>());
                }

                for (String str :strs) {
                    if (str != null && !str.isEmpty()) {
                        md5Title.get(md5).add(stemmer.stemWord(str));
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("Error process " + filePath);
            e.printStackTrace();
        }
    }

    // see if an url's title contains a string
    public boolean contains(String md5, String str) {
        if (!md5Title.containsKey(md5)) {
            return false;
        }

        return md5Title.get(md5).contains(str);
    }

    // get the original title
    public String getTitle(String md5) {
        if (!md5OrigTitle.containsKey(md5)) {
            return null;
        }
        return md5OrigTitle.get(md5);
    }

    private HashMap<String, HashSet<String>> md5Title = new HashMap<>();
    private HashMap<String, String> md5OrigTitle = new HashMap<>();
}
