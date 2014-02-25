package indexReader;

import indexbuilder.StemDocuments;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

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

                String[] strs = title.split("\\W");

                StringBuilder stringBuilder = new StringBuilder();
                for (String str :strs) {
                    stringBuilder.append(stemmer.stemWord(str)).append(":");
                }

                md5Title.put(md5, stringBuilder.toString());
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("Error process " + filePath);
            e.printStackTrace();
        }
    }

    public String getTitle(String md5) {
        if (!md5Title.containsKey(md5)) {
            return null;
        }
        return md5Title.get(md5);
    }

    public boolean contains(String md5, String str) {
        if (!md5Title.containsKey(md5)) {
            return false;
        }
        return md5Title.get(md5).contains(str);
    }

    private HashMap<String, String> md5Title = new HashMap<>();;
}
