import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

public class ClientMain {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub

		try {
			IUno uno = (IUno) Naming.lookup ("//localhost/Uno");
			
			Scanner scanner = new Scanner(System.in);
			System.out.print("Seu nome: ");
            String playerName = scanner.nextLine();
            
            int playerId = uno.registerPlayer(playerName);
            
            if (playerId == -1) {
				System.out.println("Usuario ja cadastrado.");
				scanner.close();
				return;
			}
			
			if (playerId == -2) {
				System.out.println("Numero maximo de jogadores atingido.");
				scanner.close();
				return;
			}
				
			System.out.println("Seu id: " + playerId);
			
			new Client(playerId, uno).runClient(scanner);
			

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
	}

}
