import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class Game {

	private Stack<Card> tableDeck = new Stack<>();
	private Stack<Card> deck = new Stack<>();
	private ArrayList<Player> players = new ArrayList<>();
	private GameStatus status;
	
	private void toDealTheCards(Stack<Card> deck) {
		Stack<Card> deckPlayerOne = new Stack<>();
		Stack<Card> deckPlayerTwo = new Stack<>();
		Card card;
		
		for(int i = 0; i < 7; i += 1) {
			card = deck.pop();
			deckPlayerOne.push(card);
			card = deck.pop();
			deckPlayerTwo.push(card);
		}
		
		players.get(0).setDeck(deckPlayerOne);
		players.get(1).setDeck(deckPlayerTwo);
	}
	
	private Stack<Card> toShuffleCards(Stack<Card> deck) {
		int playerOneId = players.get(0).getId();
		int playerTwoId = players.get(1).getId();
		Stack<Card> newDeck = new Stack<>();
		
		Random generator =  new Random(playerOneId + playerTwoId);
		
		while(deck.size() > 0) {
			int randonIndex = generator.nextInt(deck.size());
			newDeck.push(deck.get(randonIndex));
			deck.remove(randonIndex);
		}
		
		this.deck = newDeck;
		
		return this.deck;
		
	}
	
	private Stack<Card> createDeck() throws Exception {
		
			// cria cartas normais
			for (ColorCard color : ColorCard.values()) {
				
				for(int i = 0; i < 10; i += 1) {
					
					// so tem 1 carta com numero zero
					deck.push(new Card(color, null, i));
					if (i != 0)					
						deck.push(new Card(color, null, i));
				}
				
				// cartas especiais
				for(int j = 0; j < 2; j +=1) {
					deck.push(new Card(color, TypeCard.SKIP, -1));
					deck.push(new Card(color, TypeCard.REVERSE, -1));
					deck.push(new Card(color, TypeCard.MORE_2, -1));
					
				}
				
			}
			
			for(int i = 0; i < 4; i += 1) {
				// coringas
				deck.push(new Card(null, TypeCard.JOKER, -1));
				deck.push(new Card(null, TypeCard.JOKER_4, -1));
			}
	
		return deck;
		
	}
	
	public Game(Player playerOne) {
		this.players.add(playerOne);
	}
	
	public void addOpponent(Player opponent) throws Exception {
		this.players.add(opponent);
		
		Stack<Card> deck = createDeck();
		deck = toShuffleCards(deck);
		deck = toShuffleCards(deck);
		toDealTheCards(deck);
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
	
	public void showDeck() {
		for(Card card : deck) {
			System.out.println(card.getNumber() + " - " + card.getColor() + " - " + card.getType());
		}
	}

}
