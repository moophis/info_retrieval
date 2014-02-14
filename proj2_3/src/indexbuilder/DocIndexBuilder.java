package indexbuilder;

import crawler.Controller;
import indexReader.Doc2MD5;
import indexReader.MD52Doc;

import java.io.*;
import org.apache.commons.codec.digest.DigestUtils;
/**
 * Created by soushimei on 2/13/14.
 */
public class DocIndexBuilder {
    private final String DocumentIndexFolderPath;
    private final String RawHTMLFolderPath;
    private final String RawInfoFolderPath;


    public DocIndexBuilder(String path,
                           String DocumentIndexFolder,
                           String RawHTMLFolder,
                           String RawInfoFolder) {
        DocumentIndexFolderPath = path + DocumentIndexFolder;
        RawHTMLFolderPath = path + RawHTMLFolder;
        RawInfoFolderPath = path + RawInfoFolder;
    }



    /// Building the index of document
    public void build(String URL2MD5_fileName, String MD52URL_fileName) throws IOException {
        String URL2MD5_filePath = DocumentIndexFolderPath + URL2MD5_fileName;
        String MD52URL_filePath = DocumentIndexFolderPath + MD52URL_fileName;

        Doc2MD5 doc2MD5 = Doc2MD5.getInstance();
        doc2MD5.clear();
        MD52Doc md52Doc = MD52Doc.getInstance();
        md52Doc.clear();

        for (Integer i = 13; i <= 19; i++) {
            String fileName = "thread" + i.toString() + ".txt";
            String infPath = RawInfoFolderPath + fileName;
            String txtPath = RawHTMLFolderPath + fileName;
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
            case "md5": // (string)
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
