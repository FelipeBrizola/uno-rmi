import java.util.ArrayList;
import java.util.Stack;

public class Game {

	private Stack<Card> tableDeck = new Stack<>();
	private Stack<Card> deck = new Stack<>();
	private ArrayList<Player> players = new ArrayList<>();
	private GameStatus status;
	
	public Game(Player playerOne) {
		this.players.add(playerOne);
		
		// embaralhar cartas
		// inicializar tabledeck com carta
	}
	
	public void addOpponent(Player opponent) {
		this.players.add(opponent);
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

	public GameStatus getStatus() {
		return status;
	}

	public void setStatus(GameStatus status) {
		this.status = status;
	}

}
