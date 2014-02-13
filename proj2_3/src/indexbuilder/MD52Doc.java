package indexbuilder;

public class MD52Doc {
	private static MD52Doc uniqueInstance;
	private MD52Doc() {}
	public static MD52Doc getInstance() {
		if (uniqueInstance == null) {
			uniqueInstance = new MD52Doc();
		}
		return uniqueInstance;
	}
}
