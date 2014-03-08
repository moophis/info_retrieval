package indexReader;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by soushimei on 3/8/14.
 */
public class MD52Depth {
    private static MD52Depth uniqueInstance;
    private MD52Depth() {}
    public static MD52Depth getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new MD52Depth();
        }
        return uniqueInstance;
    }

    public void readFromDisk(String filePath) {
        File f = new File(filePath);
        BufferedReader reader;
        md5Depth.clear();

        try {
            reader = new BufferedReader(new FileReader(f));
            String line;
            while ((line = reader.readLine()) != null) {
                String md5 = line.substring(0, 32);
                int depth = Integer.parseInt(line.substring(32));
                md5Depth.put(md5, depth);
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("Error process " + filePath);
            e.printStackTrace();
        }
    }

    public void write2Disk(String filePath) {
        FileWriter fw;
        try {
            fw = new FileWriter(filePath, false);

            for (Map.Entry<String, Integer> m : md5Depth.entrySet()) {
                String buf = new StringBuilder().append(m.getKey()).append(m.getValue()).toString();
                fw.write(buf + "\n");
            }
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getDepth(String md5) {
        if (md5Depth.containsKey(md5)) {
            return md5Depth.get(md5);
        } else {
            return maxDepth;
        }
    }

    public void setDepth(String md5, int depth) {
        if (depth > maxDepth) {
            depth = maxDepth;
        }

        md5Depth.put(md5, depth);
    }

    public void clear() {
        md5Depth.clear();
    }

    private HashMap<String, Integer> md5Depth = new HashMap<>();
    public final int maxDepth = 30;
}
