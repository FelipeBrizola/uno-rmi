import  java.rmi.Naming;

public class Client {

	public static void main (String[] argv) {
		
		try {
			IUno game = (IUno) Naming.lookup ("//localhost/Uno");
			
			int playerOneId = game.registerPlayer("Felipe");
			int playerTwoId = game.registerPlayer("Adversario_Felipe");

		} catch (Exception e) {
			System.out.println ("GameClient failed:");
			e.printStackTrace();
		}
	}
}
