import java.util.ArrayList;

public class Game {

	private ArrayList<Card> tableDeck = new ArrayList<>();
	private ArrayList<Card> deck = new ArrayList<>();
	private ArrayList<Player> players = new ArrayList<>();
	
	public Game(Player playerOne, Player playerTwo) {
		this.players.add(playerOne);
		this.players.add(playerTwo);
		
		// embaralhar cartas
		// inicializar tabledeck com carta
	}

	public ArrayList<Card> getDeck() {
		return deck;
	}

	public void setDeck(ArrayList<Card> deck) {
		this.deck = deck;
	}

	public ArrayList<Card> getTableDeck() {
		return tableDeck;
	}

	public void setTableDeck(ArrayList<Card> tableDeck) {
		this.tableDeck = tableDeck;
	}
	
}
