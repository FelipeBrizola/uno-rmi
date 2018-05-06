import java.util.ArrayList;
import java.util.Stack;

public class Player {

	private String name;
	private int id;
	private boolean isMyTurn;
	private Stack<Card> deck;
	
	public Player(String name, int id) {
		this.name = name;
		this.isMyTurn = false;
		this.id = id;
		this.setDeck(new Stack<>());
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public boolean getIsMyTurn() {
		return isMyTurn;
	}
	public void setIsMyTurn(boolean isMyTurn) {
		this.isMyTurn = isMyTurn;
	}

	public Stack<Card> getDeck() {
		return deck;
	}

	public void setDeck(Stack<Card> deck) {
		this.deck = deck;
	}

	public String showDeck() {
		String deckString = "";

		for(int i =0; i < this.deck.size(); i += 1) {
			deckString = deckString.concat("Id: " + i + " | " + "Numero: " + this.deck.get(i).getNumber());

			if (this.deck.get(i).getColor() != null)
				deckString = deckString.concat(" | " + "Cor: " + this.deck.get(i).getColor());

			if (this.deck.get(i).getType() != null)
				deckString = deckString.concat(" | " + "Tipo: " + this.deck.get(i).getType());

			deckString = deckString.concat("\n");
			
		}
		return deckString;
	}
	
}
