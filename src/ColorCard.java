
public enum ColorCard {

	BLUE(0), GREEN(2), RED(3), YELLOW(1);

	private final int value;

	ColorCard(int value) {
		this.value = value;
	}

	public int getValue() {
		return this.value;
	}

	public ColorCard getValue(int key) {
		switch (key) {
		case 0:
			return BLUE;
		case 1:
			return YELLOW;
		case 2:
			return GREEN;

		case 3:
			return RED;

		default:
			break;
		}
		return null;
	}
}