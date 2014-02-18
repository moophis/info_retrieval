package indexbuilder;

import indexReader.Doc2MD5;
import indexReader.MD52Doc;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.*;

/**
 * Created by soushimei on 2/13/14.
 */
public class DocIndexBuilder {
    private final String documentIndexFolderPath;
    private final String rawHTMLFolderPath;
    private final String rawInfoFolderPath;


    public DocIndexBuilder(String path,
                           String documentIndexFolder,
                           String rawHTMLFolder,
                           String rawInfoFolder) {
        documentIndexFolderPath = path + documentIndexFolder;
        rawHTMLFolderPath = path + rawHTMLFolder;
        rawInfoFolderPath = path + rawInfoFolder;
    }


    /// Building the index of document
    public void build(String urlToMD5FileName, String md5ToUrlFileName) throws IOException {
        String URL2MD5_filePath = documentIndexFolderPath + urlToMD5FileName;
        String MD52URL_filePath = documentIndexFolderPath + md5ToUrlFileName;

        Doc2MD5 doc2MD5 = Doc2MD5.getInstance();
        doc2MD5.clear();
        MD52Doc md52Doc = MD52Doc.getInstance();
        md52Doc.clear();

        for (Integer i = 13; i <= 19; i++) {
            String fileName = "thread" + i.toString() + ".txt";
            String infPath = rawInfoFolderPath + fileName;
            String txtPath = rawHTMLFolderPath + fileName;
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

            line = reader.readLine(); // first line
            url = getPageInfo(line, "url");
            while ((line = reader.readLine()) != null) {
                nextPos = Long.parseLong(getPageInfo(line, "position"));
                len = nextPos - curPos;

                doc2MD5.putHtmlFileInfo(url, curPos, len, fileName);
                md52Doc.setURL(DigestUtils.md5Hex(url), url);

                curPos = nextPos;
                url = getPageInfo(line, "url");
            }
            // deal with the last line
            len = textFile.length() - curPos;
            doc2MD5.putHtmlFileInfo(url, curPos, len, fileName);
            md52Doc.setURL(DigestUtils.md5Hex(url), url);

            reader.close();
        }
        doc2MD5.write2Disk(URL2MD5_filePath);
        md52Doc.write2Disk(MD52URL_filePath);
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
            case "out-degree": // (int)
                return strings[2];
            case "url": // (string)
                return "http:" + strings[4];
            default:
                System.err.println("Invalid parameter!");
        }

        return null;
    }

}
