package indexReader;

import Strucutre.TF_IDF_Positions;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class InverseIndex {
	private static InverseIndex uniqueInstance;
	private InverseIndex() {}
	public static InverseIndex getInstance() {
		if (uniqueInstance == null) {
			uniqueInstance = new InverseIndex();
		}
		return uniqueInstance;
	}

    public void write2Disk(String filePath) {
        if (inverseIndex == null) {
            System.err.println("page rank index has not been initilized yet. Tyr build pagerank first.");
            return;
        }
        FileWriter fw;
        try {
            fw = new FileWriter(filePath, false);

            for (Map.Entry<String, HashMap<String, TF_IDF_Positions>> m : inverseIndex.entrySet()) {
                String term = m.getKey();
                // write term
                fw.write(term + "\n");
                HashMap<String, TF_IDF_Positions> doc_tfidfPoisitions = m.getValue();
                // write doc number
                Integer docNum = new Integer(doc_tfidfPoisitions.size());
                fw.write(docNum.toString() + "\n");
                for (Map.Entry<String, TF_IDF_Positions> n : doc_tfidfPoisitions.entrySet()) {
                    // write document md5
                    fw.write(n.getKey());
                    // write tf idf value
                    TF_IDF_Positions tf_idf_positions = n.getValue();
                    Double tf_idf = new Double(tf_idf_positions.tf_idf);
                    fw.write(tf_idf.toString() + "\n");
                    // write number of positions
                    Integer posNum = new Integer(tf_idf_positions.positions.size());
                    fw.write(posNum.toString() + "\n");
                    // write positions
                    for (Integer integer : tf_idf_positions.positions) {
                        fw.write(integer + "\n");
                    }
                }
            }
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void readFromDisk(String filePath) {
        File f = new File(filePath);
        BufferedReader reader;
        if (inverseIndex == null)
            inverseIndex = new HashMap<>();

        try {
            reader = new BufferedReader(new FileReader(f));

            String line;
            while ((line = reader.readLine()) != null) {
                // read a term
                String term = line;
                inverseIndex.put(term, new HashMap<String, TF_IDF_Positions>());

                // read doc number
                int docNum = 0;
                line = reader.readLine();
                docNum = Integer.parseInt(line);

                for (int doci = 0; doci < docNum; ++doci) {
                    HashMap<String, TF_IDF_Positions> doc_tfidfPoisitions = inverseIndex.get(term);
                    // read document md5
                    String md5 = reader.readLine();
                    doc_tfidfPoisitions.put(md5, new TF_IDF_Positions());
                    TF_IDF_Positions tf_idf_positions = doc_tfidfPoisitions.get(md5);

                    // read tf idf value
                    line = reader.readLine();
                    double tf_idf = Double.parseDouble(line);
                    tf_idf_positions.tf_idf = tf_idf;

                    // read number of positions
                    line = reader.readLine();
                    int posNum = Integer.parseInt(line);
                    for (int posi = 0; posi < posNum; ++posi) {
                        // read positions
                        line = reader.readLine();
                        int pos = Integer.parseInt(line);
                        tf_idf_positions.positions.add(pos);
                    }
                }
            }

            reader.close();
        } catch (IOException e) {
            System.err.println("Error process " + filePath);
            e.printStackTrace();
        }
    }

    public void setInverseIndex(HashMap<String, HashMap<String, TF_IDF_Positions> > result) {
        inverseIndex = result;
    }

    private HashMap<String, HashMap<String, TF_IDF_Positions> > inverseIndex;

}
