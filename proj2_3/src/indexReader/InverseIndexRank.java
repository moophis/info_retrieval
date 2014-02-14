package indexReader;

public class InverseIndexRank {
	private static InverseIndexRank uniqueInstance;
	private InverseIndexRank() {}
	public static InverseIndexRank getInstance() {
		if (uniqueInstance == null) {
			uniqueInstance = new InverseIndexRank();
		}
		return uniqueInstance;
	}
}
