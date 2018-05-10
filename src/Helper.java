import java.util.Scanner;
import java.util.Stack;

public class Helper {

    public static String intColorToStrColor(int color) {

        switch (color) {
        case 0:
            return "BLUE";
        case 2:
            return "GREEN";
        case 3:
            return "RED";
        case 1:
            return "YELLOW";

        default:
            return "";
        }
    }

    public static boolean isInt(String number) {
        try {
            Integer.parseInt(number);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static String playWithJoker(Scanner scanner) {
        System.out.println("Selecione o Id da proxima cor ativa: ");
        System.out.println("Id: 0  | BLUE");
        System.out.println("Id: 1  | YELLOW");
        System.out.println("Id: 2  | GREEN");
        System.out.println("Id: 3  | RED");
        return scanner.nextLine();
    }
    
    public static String cardToString(Card card) {

		if (card.getColor() != null && card.getNumber() != -1 && card.getType() == null)
			return card.getNumber() + "/" + card.getColor();

		if (card.getColor() != null && card.getNumber() == -1 && card.getType() != null)
			return card.getType() + "/" + card.getColor();

		if (card.getColor() == null && card.getNumber() == -1 && card.getType() != null)
			return card.getType() + "/*";

		return "";
	}

	public static Card stringToCard(String cardStr) {

		String[] values = cardStr.split("/");

		// coringa
		if (cardStr.contains("*"))
			return new Card(null, TypeCard.valueOf(values[0]), -1);

		try {
			int number = Integer.parseInt(values[0]);
			return new Card(ColorCard.valueOf(values[1]), null, number);
		} catch (Exception e) {
			return new Card(ColorCard.valueOf(values[1]), TypeCard.valueOf(values[0]), -1);
		}

	}
	
	public static int sumScore(Stack<Card> deck) {
		int sum = 0;

		for (Card card : deck) {

			if (card.getType() == TypeCard.JOKER || card.getType() == TypeCard.JOKER_4)
				sum += 50;
			else if (card.getType() == TypeCard.MORE_2 || card.getType() == TypeCard.SKIP
					|| card.getType() == TypeCard.REVERSE)
				sum += 20;
			else
				sum += card.getNumber();
		}

		return sum;
	}
}