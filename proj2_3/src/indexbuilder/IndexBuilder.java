package indexbuilder;


import indexReader.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import structure.TF_IDF_Positions;

/**
 * Main entry.
 */
public class IndexBuilder {
    private boolean initialization;
    private final String path;
    private final String inverseIndexFolder;
    private final String documentIndexFolder;
    private final String tempFolder;
    private final String rawHTMLFolder;
    private final String rawInfoFolder;
    private final String pureTextFolder;
    private final String pureInfoFolder;
    private final String pureTitleFolder;

    private String URL2MD5_fileName = "URL_to_MD5.txt";
    private String MD52URL_fileName = "MD5_to_URL.txt";
    private String PageRank_fileName = "PageRankBuilder.txt";
    private String title_fileName = "title.txt";
    private String anchor_fileName = "anchorText.txt";
    private String InverseIndex_fileName = "InverseIndex.txt";

    public IndexBuilder(String path) {
        this.path = path + "/";
        inverseIndexFolder = "InverseIndex/";
        documentIndexFolder = "DocumentIndex/";
        tempFolder = "Temp/";
        rawHTMLFolder = "raw/text/";
        rawInfoFolder = "raw/info/";
        pureTextFolder = "pure/text/";
        pureInfoFolder = "pure/info/";
        pureTitleFolder = "pure/title/";


        // check if the directory exists
        boolean success;
        success = (new File(this.path + pureTextFolder)).exists();
        if (!success) {
            System.out.println("Fail to find the raw html data at "
                    + this.path + pureTextFolder + " !");
            initialization = false;
            return;
        }
        success = (new File(this.path + pureInfoFolder)).exists();
        if (!success) {
            System.out.println("Fail to find the raw info data at "
                    + this.path + pureInfoFolder + " !");
            initialization = false;
            return;
        }
        success = (new File(this.path + rawHTMLFolder)).exists();
        if (!success) {
            System.out.println("Fail to find the raw html data at "
                    + this.path + rawHTMLFolder + " !");
            initialization = false;
            return;
        }
        success = (new File(this.path + rawInfoFolder)).exists();
        if (!success) {
            System.out.println("Fail to find the raw info data at "
                    + this.path + rawInfoFolder + " !");
            initialization = false;
            return;
        }
        success = (new File(this.path + pureTitleFolder)).exists();
        if (!success) {
            System.out.println("Fail to find the pure title data at "
                    + this.path + pureTitleFolder + " !");
            initialization = false;
            return;
        }
        initialization = true;

        // check InverseIndex Folder
        success = (new File(this.path + inverseIndexFolder)).exists();
        if (!success) {
            success = (new File(this.path + inverseIndexFolder)).mkdir();
            if (!success) {
                System.out.println("Fail to create directory "
                        + inverseIndexFolder + "!");
                initialization = false;
            }
        }
        // check InverseIndex Folder
        success = (new File(this.path + documentIndexFolder)).exists();
        if (!success) {
            success = (new File(this.path + documentIndexFolder)).mkdir();
            if (!success) {
                System.out.println("Fail to create directory "
                        + documentIndexFolder + "!");
                initialization = false;
            }
        }
        // check Temp Folder
        success = (new File(this.path + tempFolder)).exists();
        if (!success) {
            success = (new File(this.path + tempFolder)).mkdir();
            if (!success) {
                System.out.println("Fail to create directory "
                        + tempFolder + "!");
                initialization = false;
            }
        }
    }


    /// Main function to build the index
    public void buildIndex() throws IOException {
        System.out.println("Begin building index");
        if (!initialization) {
            System.out.println("Fail to initialize index builder!");
            return;
        }
        /*
                                build MD52URL/URL2MD5
         */
        // this is quite easy, one class can finish this
        System.out.println("Build the indices from URL to MD5 and from MD5 to URL");
//        DocIndexBuilder docIndexBuilder = new DocIndexBuilder(path,
//                documentIndexFolder, pureTextFolder, pureInfoFolder);
//        docIndexBuilder.build(URL2MD5_fileName, MD52URL_fileName);
        Doc2MD5.getInstance().readFromDisk(path + documentIndexFolder + URL2MD5_fileName);
//        Doc2MD5.getInstance().write2Disk(path + documentIndexFolder + "copy " + URL2MD5_fileName);
        MD52Doc.getInstance().readFromDisk(path + documentIndexFolder + MD52URL_fileName);
//        MD52Doc.getInstance().write2Disk(path + documentIndexFolder + "copy " + MD52URL_fileName);

        /*
                                build PageRank
        */
        System.out.println("Build the PageRankBuilder index");
        // PageRankBuilder pageRankBuilder = new PageRankBuilder(path,
        //        documentIndexFolder, rawInfoFolder);
        // pageRankBuilder.calcPageRank();
        // PageRank.getInstance().write2Disk(path + documentIndexFolder + PageRank_fileName);
        PageRank.getInstance().readFromDisk(path + documentIndexFolder + PageRank_fileName);
        /*
                                build MD5 to Title
        */
        System.out.println("Build the Title index");
        MD52Title.getInstance().readFromDisk(path + documentIndexFolder + title_fileName);
        /*
                                build MD5 to anchor text
        */
        System.out.println("Build the anchor text index");
        MD52Anchor.getInstance().readFromDisk(path + documentIndexFolder + anchor_fileName);


        System.gc();
        System.out.println("Begin build inverse index");
        /*
                                build inverse index

         */
        // for building index
//        HashMap<String, HashMap<String, TF_IDF_Positions> > mergeSecondPhaseMap = new HashMap<>();
        // step 1: split the documents into <term, URL(MD5), pos> same below
        System.out.println("split the documents");
//        SplitDocuments splitDocuments;
//        splitDocuments = new SplitDocuments(path,
//                tempFolder, rawHTMLFolder, rawInfoFolder);
//        splitDocuments.splitAndMerge(mergeSecondPhaseMap);

        System.gc();
        // step 5: stat the inverse index and output inverse index rank
        System.out.println("Calculate the tf-idf rank of inverse index");
//        InverseIndexRankBuilder inverseIndexRankBuilder;
//        inverseIndexRankBuilder = new InverseIndexRankBuilder(path, documentIndexFolder);
//        inverseIndexRankBuilder.build(mergeSecondPhaseMap);
//        InverseIndex.getInstance().write2Disk(path + documentIndexFolder + InverseIndex_fileName);
        InverseIndex.getInstance().readFromDisk(path + documentIndexFolder + InverseIndex_fileName);


        System.out.println("Finish building index");

        System.out.println("Begin printing statistics");
        Doc2MD5.getInstance().printNumberDocuments();
        PageRank.getInstance().printMaxPageRank();
        InverseIndex.getInstance().printMostFreqWord();
        InverseIndex.getInstance().printNumberTerm();
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: add \"path\"");
            return;
        }

        IndexBuilder indexBuilder = new IndexBuilder(args[0]);
        try {
            indexBuilder.buildIndex();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
