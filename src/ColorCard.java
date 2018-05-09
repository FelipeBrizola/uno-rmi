
public enum ColorCard {

	BLUE(0), GREEN(2), RED(3), YELLOW(1);
	
	private final int value;

	ColorCard(int value) {
		this.value = value;
	}

	public int getValue() {
		return this.value;
	}
}