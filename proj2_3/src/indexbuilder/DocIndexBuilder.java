package indexbuilder;

import indexReader.Doc2MD5;
import indexReader.MD52Depth;
import indexReader.MD52Doc;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.*;

/**
 * Created by soushimei on 2/13/14.
 */
public class DocIndexBuilder {
    private final String documentIndexFolderPath;
    private final String pureTextFolderPath;
    private final String pureInfoFolderPath;


    public DocIndexBuilder(String path,
                           String documentIndexFolder,
                           String pureHTMLFolder,
                           String pureInfoFolder) {
        documentIndexFolderPath = path + documentIndexFolder;
        pureTextFolderPath = path + pureHTMLFolder;
        pureInfoFolderPath = path + pureInfoFolder;
    }


    /// Building the index of document
    public void build(String urlToMD5FileName, String md5ToUrlFileName, String md5ToDepthFileName) throws IOException {
        String URL2MD5_filePath = documentIndexFolderPath + urlToMD5FileName;
        String MD52URL_filePath = documentIndexFolderPath + md5ToUrlFileName;
        String MD52DEPTH_filePath = documentIndexFolderPath + md5ToDepthFileName;

        Doc2MD5 doc2MD5 = Doc2MD5.getInstance();
        doc2MD5.clear();
        MD52Doc md52Doc = MD52Doc.getInstance();
        md52Doc.clear();
        MD52Depth md52Depth = MD52Depth.getInstance();
        md52Depth.clear();

        for (Integer i = 13; i <= 19; i++) {
            String fileName = "thread" + i.toString() + ".txt";
            String infPath = pureInfoFolderPath + fileName;
            String txtPath = pureTextFolderPath + fileName;
            File infoFile = new File(infPath);
            File textFile = new File(txtPath);
            BufferedReader reader = null;

            try {
                reader = new BufferedReader(new FileReader(infoFile));
            } catch (FileNotFoundException e) {
                System.err.println("Cannot find info file for thread " + i);
                e.printStackTrace();
            }

			/*
             * Should really care about the first line and the last line of
			 * the info files.
			 */
            String line, url;
            long curPos = 0, nextPos = 0, len;
            int depth;
            String md5;

            line = reader.readLine(); // first line
            url = getPageInfo(line, "url");
            depth = Integer.parseInt(getPageInfo(line, "depth"));
            while ((line = reader.readLine()) != null) {
                md5 = DigestUtils.md5Hex(url);
                nextPos = Long.parseLong(getPageInfo(line, "position"));
                len = nextPos - curPos;

                doc2MD5.putHtmlFileInfo(url, curPos, len, fileName);
                md52Doc.setURL(md5, url);
                md52Depth.setDepth(md5, depth);

                curPos = nextPos;
                url = getPageInfo(line, "url");
                depth = Integer.parseInt(getPageInfo(line, "depth"));
            }
            // deal with the last line
            md5 = DigestUtils.md5Hex(url);
            len = textFile.length() - curPos;
            doc2MD5.putHtmlFileInfo(url, curPos, len, fileName);
            md52Doc.setURL(md5, url);
            md52Depth.setDepth(md5, depth);

            reader.close();
        }
        doc2MD5.write2Disk(URL2MD5_filePath);
        md52Doc.write2Disk(MD52URL_filePath);
        md52Depth.write2Disk(MD52DEPTH_filePath);
    }

    /**
     * Extract the page information from an "info" line.
     */
    private static String getPageInfo(String line, String type) {
        if (line == null)
            return line;

        String[] strings = line.split(":");

        switch (type) {
            case "urlMd5": // (string)
                return strings[0];
            case "position": // starting position (long)
                return strings[1];
            case "depth": // (int)
                return strings[2];
            case "url": // (string)
                return "http:" + strings[4];
            default:
                System.err.println("Invalid parameter!");
        }

        return null;
    }

}
