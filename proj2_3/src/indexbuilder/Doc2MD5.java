package indexbuilder;

public class Doc2MD5 {
	private static Doc2MD5 uniqueInstance;
	private Doc2MD5() {}
	public static Doc2MD5 getInstance() {
		if (uniqueInstance == null) {
			uniqueInstance = new Doc2MD5();
		}
		return uniqueInstance;
	}
}
