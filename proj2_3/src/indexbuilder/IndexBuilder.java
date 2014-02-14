package indexbuilder;


import Strucutre.Page_Positions;
import Strucutre.Word_Page_Position;
import indexReader.Doc2MD5;
import indexReader.MD52Doc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class IndexBuilder {
    private boolean Initialization;
    private final String path;
    private final String InverseIndexFolder;
    private final String DocumentIndexFolder;
    private final String TempFolder;
    private final String RawHTMLFolder;
    private final String RawInfoFolder;

    private String URL2MD5_fileName = "URL_to_MD5.txt";
    private String MD52URL_fileName = "MD5_to_URL.txt";
    private String PageRank_fileName = "PageRank.txt";
    private String InverseIndex_fileName = "InverseIndex.txt";

    public IndexBuilder(String path) {
        this.path = path + "/";
        InverseIndexFolder = "InverseIndex/";
        DocumentIndexFolder = "DocumentIndex/";
        TempFolder = "Temp";
        RawHTMLFolder = "raw/text/";
        RawInfoFolder = "raw/info/";

        // check if the directory exists
        boolean success;
        success = (new File(this.path + RawHTMLFolder)).exists();
        if (!success) {
            System.out.println("Fail to find the raw html data at "
                    + this.path + RawHTMLFolder + " !");
            Initialization = false;
            return;
        }
        success = (new File(this.path + RawInfoFolder)).exists();
        if (!success) {
            System.out.println("Fail to find the raw info data at "
                    + this.path + RawInfoFolder + " !");
            Initialization = false;
            return;
        }
        Initialization = true;

        // check InverseIndex Folder
        success = (new File(this.path + InverseIndexFolder)).exists();
        if (!success) {
            success = (new File(this.path + InverseIndexFolder)).mkdir();
            if (!success) {
                System.out.println("Fail to creat directory "
                        + InverseIndexFolder + "!");
                Initialization = false;
            }
        }
        // check InverseIndex Folder
        success = (new File(this.path + DocumentIndexFolder)).exists();
        if (!success) {
            success = (new File(this.path + DocumentIndexFolder)).mkdir();
            if (!success) {
                System.out.println("Fail to creat directory "
                        + DocumentIndexFolder + "!");
                Initialization = false;
            }
        }
        // check Temp Folder
        success = (new File(this.path + TempFolder)).exists();
        if (!success) {
            success = (new File(this.path + TempFolder)).mkdir();
            if (!success) {
                System.out.println("Fail to creat directory "
                        + TempFolder + "!");
                Initialization = false;
            }
        }
    }


    /// Main function to build the index
    public void buildIndex() throws IOException {
        System.out.println("Begin building index");
        if (!Initialization) {
            System.out.println("Fail to initialize index builder!");
            return;
        }
        /*
                                build MD52URL/URL2MD5
         */
        // this is quite easy, one class can finish this
        System.out.println("Build the indices from URL to MD5 and from MD5 to URL");
        DocIndexBuilder docIndexBuilder = new DocIndexBuilder(path,
                DocumentIndexFolder, RawHTMLFolder, RawInfoFolder);
        // docIndexBuilder.build(URL2MD5_fileName, MD52URL_fileName);
        Doc2MD5.getInstance().readFromDisk(path + DocumentIndexFolder + URL2MD5_fileName);
        // Doc2MD5.getInstance().write2Disk(path + DocumentIndexFolder + "copy " + URL2MD5_fileName);
        MD52Doc.getInstance().readFromDisk(path + DocumentIndexFolder + MD52URL_fileName);
        // MD52Doc.getInstance().write2Disk(path + DocumentIndexFolder + "copy " + MD52URL_fileName);

        // build page rank
        System.out.println("Build the PageRank index");
        PageRank pageRank = new PageRank(path,
                DocumentIndexFolder, RawHTMLFolder, RawInfoFolder);
        pageRank.calcPageRank(URL2MD5_fileName);

        System.out.println("Begin build inverse index");
        /*
                                build inverse index

         */
        // for building index
        ArrayList<Word_Page_Position> splitLists = new ArrayList<>();
        HashMap<String, Page_Positions> mergeFirstPhaseMap = new HashMap<>();
        // step 1: split the documents into <term, URL(MD5), pos> same below
        System.out.println("split the documents");
        SplitDocuments splitDocuments;
        splitDocuments = new SplitDocuments(path,
                TempFolder, RawHTMLFolder, RawInfoFolder);
        splitDocuments.split(splitLists);

        // step 2: stem the term into stemmed version
        System.out.println("stem the documents");
        StemDocuments stemDocuments;
        stemDocuments = new StemDocuments(path, TempFolder);
        stemDocuments.stem(splitLists);

        // step 3: merge the pair <term, URL1, pos1>... to <term, <URL1, pos1>, <URL1, pos2>,... >
        System.out.println("First merging phase");
        MergeTermFirstPhase mergeTermFirstPhase;
        mergeTermFirstPhase = new MergeTermFirstPhase(path, TempFolder);
        mergeTermFirstPhase.merge(splitLists, mergeFirstPhaseMap);

        // step 4: merge the pair <term, <URL1, pos1>, <URL1, pos2>,... > to <term, <URL/MD5, <pos1, pos2, pos3,...,> > >
        // In this phase, the generated file is already the inverse index needed
        System.out.println("Second merging phase; inverse index built");
        MergeTermSecondPhase mergeTermSecondPhase;
        mergeTermSecondPhase = new MergeTermSecondPhase(path, TempFolder, DocumentIndexFolder);
        String inverseIndexFileName;
        inverseIndexFileName = mergeTermSecondPhase.merge(mergeFirstPhaseMap);

        // step 5: stat the inverse index and output inverse index rank
        System.out.println("Calculate the tf-idf rank of inverse index");
        InverseIndexRankBuilder inverseIndexRankBuilder;
        inverseIndexRankBuilder = new InverseIndexRankBuilder(path, DocumentIndexFolder);
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
