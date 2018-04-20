import java.util.ArrayList;
import java.util.Stack;

public class Game {

	private Stack<Card> tableDeck = new Stack<>();
	private Stack<Card> deck = new Stack<>();
	private ArrayList<Player> players = new ArrayList<>();
	
	public Game(Player playerOne, Player playerTwo) {
		this.players.add(playerOne);
		this.players.add(playerTwo);
		
		// embaralhar cartas
		// inicializar tabledeck com carta
	}

	public Stack<Card> getDeck() {
		return deck;
	}

	public void setDeck(Stack<Card> deck) {
		this.deck = deck;
	}

	public Stack<Card> getTableDeck() {
		return tableDeck;
	}

	public void setTableDeck(Stack<Card> tableDeck) {
		this.tableDeck = tableDeck;
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}
	
	public Player getPlayerByPlayerId(int playerId) {
		for(Player player : players)
			if (player.getId() == playerId)
				return player;
		
		return null;
	}

}
