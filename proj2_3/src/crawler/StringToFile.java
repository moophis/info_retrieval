package crawler;

import java.io.*;

public class StringToFile {
	private static BufferedWriter bw;
	private static FileWriter fw;

	public static void toFile(String buf, String path, String title) {
		System.out.println("File to: " + path + title);
		try {
			fw = new FileWriter(path + title);
			bw = new BufferedWriter(fw);
			
			bw.write(buf);
			bw.flush();
			bw.close();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
