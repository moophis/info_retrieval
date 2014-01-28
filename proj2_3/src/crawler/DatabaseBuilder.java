package crawler;

public class DatabaseBuilder {
	/**
	 * Assemble a record line for file index.
	 * MD5:line number in data.db:outdegree:URL 
	 */
	static String buildIndexLine(String md5, long lineNumber, int degree, String url) {
		Long l = new Long(lineNumber);
		Integer i = new Integer(degree);
		
		return md5 + ":" + l.toString() + ":" + i.toString() + ":" + url;
	}
}
