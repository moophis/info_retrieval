package indexbuilder;

import Strucutre.WordPagePosition;

import java.util.ArrayList;

/**
 * Created by soushimei on 2/13/14.
 */
public class StemDocuments {
    private final String tempFolderPath;

    public StemDocuments() {
        tempFolderPath = "";
    }

    public StemDocuments(String path, String tempFolder) {
        tempFolderPath = path + tempFolder;
    }

    public void stem(ArrayList<WordPagePosition> splitLists) {
        PorterStemmer stemmer = new PorterStemmer();
        for (WordPagePosition wpp : splitLists) {
            wpp.word = stemmer.stem(wpp.word.toLowerCase());
        }
    }

    public String stemWord(String word) {
        PorterStemmer stemmer = new PorterStemmer();
        return stemmer.stem(word.toLowerCase());
    }

    public static void main(String[] args) {
        StemDocuments stemmer = new StemDocuments("", "");
        System.out.println(stemmer.stemWord("Document"));
        System.out.println(stemmer.stemWord("will"));
        System.out.println(stemmer.stemWord("describe"));
        System.out.println(stemmer.stemWord("marketing"));
        System.out.println(stemmer.stemWord("strategies"));
        System.out.println(stemmer.stemWord("carried"));
        System.out.println(stemmer.stemWord("out"));
        System.out.println(stemmer.stemWord("by"));
        System.out.println(stemmer.stemWord("U.S."));
        System.out.println(stemmer.stemWord("companies"));
        System.out.println(stemmer.stemWord("for"));
        System.out.println(stemmer.stemWord("their"));
        System.out.println(stemmer.stemWord("agricultural"));
        System.out.println(stemmer.stemWord("chemicals"));

        ArrayList<WordPagePosition> splitLists = new ArrayList<>();
        WordPagePosition wpp = new WordPagePosition();
        wpp.word = "Document";
        splitLists.add(wpp);
        wpp = new WordPagePosition();
        wpp.word = "describe";
        splitLists.add(wpp);
        stemmer.stem(splitLists);

        for (WordPagePosition item : splitLists) {
            System.out.println(item.word);
        }
    }

}
