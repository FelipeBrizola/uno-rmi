import java.util.ArrayList;
import java.util.Stack;

public class Player {

	private String name;
	private int id;
	private boolean isPlaying;
	private Stack<Card> deck;
	
	public Player(String name, int id) {
		this.name = name;
		this.isPlaying = false;
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
	public boolean getIsPlaying() {
		return isPlaying;
	}
	public void setIsPlaying(boolean status) {
		this.isPlaying = status;
	}

	public Stack<Card> getDeck() {
		return deck;
	}

	public void setDeck(Stack<Card> deck) {
		this.deck = deck;
	}
	
}
