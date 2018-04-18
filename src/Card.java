
public class Card {

	private int number;
	private ColorCard color;
	private TypeCard type;
	
	public Card(ColorCard color, TypeCard type, int number) throws Exception {
		this.color = color;
		this.type = type;
		this.number = number;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public ColorCard getColor() {
		return color;
	}

	public void setColor(ColorCard color) {
		this.color = color;
	}

	public TypeCard getType() {
		return type;
	}

	public void setType(TypeCard type) {
		this.type = type;
	}

	
}
