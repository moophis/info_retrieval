package crawler;

import java.io.*;

public class StringToFile {
	private static FileWriter fw;

	public static void toFile(String buf, String path) {
//		System.out.println("File to: " + path);
		try {
			fw = new FileWriter(path, true);
			
			fw.write(buf + "\n");
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
