import java.util.ArrayList;

public class Player {

	private String name;
	private int id;
	private boolean isPlaying;
	private ArrayList<Card> deck;
	
	public Player(String name, int id) {
		this.name = name;
		this.isPlaying = false;
		this.id = id;
		this.setDeck(new ArrayList<>());
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

	public ArrayList<Card> getDeck() {
		return deck;
	}

	public void setDeck(ArrayList<Card> deck) {
		this.deck = deck;
	}
	
}
