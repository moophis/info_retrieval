package indexbuilder;


import Strucutre.PagePositions;
import Strucutre.WordPagePosition;
import indexReader.Doc2MD5;
import indexReader.MD52Doc;
import indexReader.PageRank;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

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

    private String URL2MD5_fileName = "URL_to_MD5.txt";
    private String MD52URL_fileName = "MD5_to_URL.txt";
    private String PageRank_fileName = "PageRankBuilder.txt";
    private String InverseIndex_fileName = "InverseIndex.txt";

    public IndexBuilder(String path) {
        this.path = path + "/";
        inverseIndexFolder = "InverseIndex/";
        documentIndexFolder = "DocumentIndex/";
        tempFolder = "Temp/";
        rawHTMLFolder = "raw/text/";
        rawInfoFolder = "raw/info/";

        // check if the directory exists
        boolean success;
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
                System.out.println("Fail to creat directory "
                        + documentIndexFolder + "!");
                initialization = false;
            }
        }
        // check Temp Folder
        success = (new File(this.path + tempFolder)).exists();
        if (!success) {
            success = (new File(this.path + tempFolder)).mkdir();
            if (!success) {
                System.out.println("Fail to creat directory "
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
        DocIndexBuilder docIndexBuilder = new DocIndexBuilder(path,
                documentIndexFolder, rawHTMLFolder, rawInfoFolder);
        // docIndexBuilder.build(URL2MD5_fileName, MD52URL_fileName);
        Doc2MD5.getInstance().readFromDisk(path + documentIndexFolder + URL2MD5_fileName);
        // Doc2MD5.getInstance().write2Disk(path + documentIndexFolder + "copy " + URL2MD5_fileName);
        MD52Doc.getInstance().readFromDisk(path + documentIndexFolder + MD52URL_fileName);
        // MD52Doc.getInstance().write2Disk(path + documentIndexFolder + "copy " + MD52URL_fileName);

        // build page rank
        System.out.println("Build the PageRankBuilder index");
        // PageRankBuilder pageRankBuilder = new PageRankBuilder(path,
        //        documentIndexFolder, rawInfoFolder);
        // pageRankBuilder.calcPageRank();
        // PageRank.getInstance().write2Disk(path + documentIndexFolder + PageRank_fileName);
        PageRank.getInstance().readFromDisk(path + documentIndexFolder + PageRank_fileName);


        System.out.println("Begin build inverse index");
        /*
                                build inverse index

         */
        // for building index
        ArrayList<WordPagePosition> splitLists = new ArrayList<>();
        HashMap<String, PagePositions> mergeFirstPhaseMap = new HashMap<>();
        // step 1: split the documents into <term, URL(MD5), pos> same below
        System.out.println("split the documents");
        SplitDocuments splitDocuments;
        splitDocuments = new SplitDocuments(path,
                tempFolder, rawHTMLFolder, rawInfoFolder);
        splitDocuments.split(splitLists);

        // step 2: stem the term into stemmed version
        System.out.println("stem the documents");
        StemDocuments stemDocuments;
        stemDocuments = new StemDocuments(path, tempFolder);
        stemDocuments.stem(splitLists);

        // step 3: merge the pair <term, URL1, pos1>... to <term, <URL1, pos1>, <URL1, pos2>,... >
        System.out.println("First merging phase");
        MergeTermFirstPhase mergeTermFirstPhase;
        mergeTermFirstPhase = new MergeTermFirstPhase(path, tempFolder);
        mergeTermFirstPhase.merge(splitLists, mergeFirstPhaseMap);

        // step 4: merge the pair <term, <URL1, pos1>, <URL1, pos2>,... > to <term, <URL/MD5, <pos1, pos2, pos3,...,> > >
        // In this phase, the generated file is already the inverse index needed
        System.out.println("Second merging phase; inverse index built");
        MergeTermSecondPhase mergeTermSecondPhase;
        mergeTermSecondPhase = new MergeTermSecondPhase(path, tempFolder, documentIndexFolder);
        String inverseIndexFileName;
        inverseIndexFileName = mergeTermSecondPhase.merge(mergeFirstPhaseMap);

        // step 5: stat the inverse index and output inverse index rank
        System.out.println("Calculate the tf-idf rank of inverse index");
        InverseIndexRankBuilder inverseIndexRankBuilder;
        inverseIndexRankBuilder = new InverseIndexRankBuilder(path, documentIndexFolder);
        inverseIndexRankBuilder.build(InverseIndex_fileName);


        System.out.println("Finish building index");
    }


    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: add \"path\"");
            return;
        }
        System.out.println(args.length);
        System.out.println(args[0]);

        IndexBuilder indexBuilder = new IndexBuilder(args[0]);
        try {
            indexBuilder.buildIndex();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
