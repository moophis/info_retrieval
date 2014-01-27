package crawler;

import java.io.*;

public class StringToFile {
	private static BufferedWriter bw;
	private static FileWriter fw;

	public static void toFile(String buf, String path, String title) {
		System.out.println("File to: " + path + title);
		try {
			fw = new FileWriter(path + title, true);
			
			fw.write(buf + "\n\n");
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
