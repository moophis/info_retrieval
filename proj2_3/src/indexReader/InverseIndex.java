package indexReader;

public class InverseIndex {
	private static InverseIndex uniqueInstance;
	private InverseIndex() {}
	public static InverseIndex getInstance() {
		if (uniqueInstance == null) {
			uniqueInstance = new InverseIndex();
		}
		return uniqueInstance;
	}


}
