
public enum TypeCard {
	
	JOKER(1), JOKER_4(2), SKIP(3), REVERSE(4), MORE_2(5);

	private final int value;

	TypeCard(int value) {
		this.value = value;
	}

	public int getValue() {
		return this.value;
	}

	// JOKER, JOKER_4, SKIP, REVERSE, MORE_2;
	
}