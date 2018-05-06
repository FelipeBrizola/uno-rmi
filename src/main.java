import java.util.Random;
import java.util.Stack;

public class main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Stack<Card> deck = new Stack<>();
		
		try {
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
			
			for(int i = 0; i < 4; i += 1){
				// coringas
				deck.push(new Card(null, TypeCard.JOKER, -1));
				deck.push(new Card(null, TypeCard.JOKER_4, -1));
			}
			
			
			
			int playerOneId = 0;
			int playerTwoId = 1;
			Stack<Card> newDeck = new Stack<>();
			
			Random generator =  new Random(playerOneId + playerTwoId);
			
			while(deck.size() > 0) {
				int randonIndex = generator.nextInt(deck.size());
				newDeck.push(deck.get(randonIndex));
				deck.remove(randonIndex);
			}
			
			deck = newDeck;
			
			
			System.out.println(deck.size());
			for(Card card : deck) {
				System.out.println(card.getNumber() + " - " + card.getColor() + " - " + card.getType());
			}
			
			// deck.toString();
			
			
			
	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
